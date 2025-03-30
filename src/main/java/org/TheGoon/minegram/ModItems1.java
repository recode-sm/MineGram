package org.TheGoon.minegram;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModItems1 {
    public static final Item CUSTOM_INGOT = registerItem("grambook", new Item(new FabricItemSettings()));
    private static final Identifier OPEN_GRAMBOOK_SCREEN = new Identifier("minegram", "open_grambook_screen");

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("minegram", name), item);
    }

    public static void registerModItems1() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            if (world.getBlockState(pos).getBlock() == Blocks.LECTERN) {
                if (player.getStackInHand(hand).getItem() == CUSTOM_INGOT) {
                    // 독서대 블록 엔티티 가져오기
                    LecternBlockEntity lecternBlockEntity = (LecternBlockEntity) world.getBlockEntity(pos);
                    if (lecternBlockEntity != null) {
                        // 독서대에 이미 아이템이 있는지 확인
                        if (lecternBlockEntity.getBook().isEmpty()) {
                            // 독서대에 아이템 추가
                            lecternBlockEntity.setBook(player.getStackInHand(hand).copy());
                            player.getStackInHand(hand).decrement(1);
                            // 독서대 블록 상태 업데이트
                            world.setBlockState(pos, world.getBlockState(pos).with(LecternBlock.HAS_BOOK, true));
                            return ActionResult.SUCCESS;
                        } else {
                            // 독서대에 이미 아이템이 있는 경우 메시지 표시
                            player.sendMessage(Text.of("독서대에 이미 아이템이 있습니다."));
                            return ActionResult.FAIL;
                        }
                    } else {
                        player.sendMessage(Text.of("독서대 블록 엔티티를 찾을 수 없습니다."));
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}