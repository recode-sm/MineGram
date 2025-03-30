package org.TheGoon.minegram;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock; // LecternBlock 참조
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World; // World 참조
import org.TheGoon.minegram.Minegram;

public class ModItems {
    // --- 변수명 GRAM_BOOK 사용 ---
    public static final Item GRAM_BOOK = registerItem("grambook", new Item(new FabricItemSettings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Minegram.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Minegram.LOGGER.info("Registering Mod Items (Restoring Original Placement Logic) for {}", Minegram.MOD_ID);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.isOf(Blocks.LECTERN)) {
                if (!(world.getBlockEntity(pos) instanceof LecternBlockEntity lecternEntity)) {
                    Minegram.LOGGER.warn("[LecternError] Lectern Block Entity not found at {} on {}", pos, world.isClient ? "Client" : "Server");
                    return ActionResult.PASS;
                }

                ItemStack bookOnLectern = lecternEntity.getBook();
                ItemStack playerItem = player.getStackInHand(hand); // 손에 든 아이템 가져오기

                Minegram.LOGGER.debug("[LecternInteract] On {}: Held={}, Lectern={}, Empty={}",
                        world.isClient ? "Client" : "Server",
                        playerItem.getItem(), bookOnLectern.getItem(), bookOnLectern.isEmpty());

                // --- !!! 핵심: 빈 독서대에 GRAM_BOOK 놓기 (원래 로직 복원 및 서버 측 실행 보장) !!! ---
                if (bookOnLectern.isEmpty() && playerItem.getItem() == GRAM_BOOK) {
                    Minegram.LOGGER.info("[PlaceBook] Condition MET on {}.", world.isClient ? "Client" : "Server");

                    // 서버에서만 실행되어야 하는 로직
                    if (!world.isClient) {
                        Minegram.LOGGER.info("[PlaceBook] Server: Executing placement logic...");

                        // 1. 놓을 책 준비 (복사)
                        ItemStack bookToPlace = playerItem.copy();
                        // bookToPlace.setCount(1); // maxCount(1)이면 불필요

                        // 2. 독서대 엔티티에 책 설정 (가장 중요!)
                        lecternEntity.setBook(bookToPlace);

                        // 3. 플레이어 손에서 아이템 감소 (서바이벌/어드벤처 모드)
                        //    이 코드가 여기에 확실히 있어야 합니다!
                        if (!player.getAbilities().creativeMode) {
                            playerItem.decrement(1);
                        }

                        // 4. 상태 변경 알림 및 저장 (원래 코드 방식 참조)
                        //    setBook 이후 HAS_BOOK 상태가 true로 바뀌어야 함
                        BlockState newState = state.with(LecternBlock.HAS_BOOK, true);
                        //    setBlockState는 setBook이 내부적으로 처리할 수도 있지만, 명시적으로 호출하는 것이 안전할 수 있음
                        //    플래그 2: 클라이언트에 변경 알림 | 플래그 3: 클라이언트에 알림 + 블록 재렌더링
                        world.setBlockState(pos, newState, 3); // 상태 변경 및 동기화 요청
                        lecternEntity.markDirty(); // 블록 엔티티 저장 요청 (setBook이 호출할 수도 있음)
                        // world.updateListeners(pos, state, newState, 3); // setBlockState(..., 3)와 유사 역할

                        Minegram.LOGGER.info("[PlaceBook] Server: Book placed and state updated.");
                    } else {
                        // 클라이언트 측 예측 성공 처리 (선택적)
                        // 클라이언트는 서버로부터 업데이트를 받으면 화면을 갱신합니다.
                        // ActionResult.CONSUME_PARTIAL 또는 SUCCESS 사용 가능
                        Minegram.LOGGER.info("[PlaceBook] Client: Acknowledging placement interaction.");
                    }

                    // 책 놓기 상호작용 성공 (클라이언트/서버 모두)
                    return ActionResult.SUCCESS;
                }
                // --- !!! 책 놓기 로직 끝 !!! ---

                // --- 시나리오 2: 놓인 GRAM_BOOK 우클릭 시 GUI 열기 ---
                else if (!bookOnLectern.isEmpty() && bookOnLectern.getItem() == GRAM_BOOK) {
                    if (world.isClient) {
                        Minegram.LOGGER.info("[OpenGUI] Client: Opening screen...");
                        //Minegram.SCREEN_OPENER.openCustomScreen();
                    } else {
                        Minegram.LOGGER.info("[OpenGUI] Server: Acknowledging GUI open interaction.");
                    }
                    return ActionResult.SUCCESS;
                }

                // --- 시나리오 3: 그 외 ---
                else {
                    Minegram.LOGGER.debug("[OtherCase] Passing interaction on {}.", world.isClient ? "Client" : "Server");
                    return ActionResult.PASS;
                }
            }
            return ActionResult.PASS;
        });
    }
}