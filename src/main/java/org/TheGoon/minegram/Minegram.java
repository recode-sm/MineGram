package org.TheGoon.minegram;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Minegram implements ModInitializer {

    public static final String MOD_ID = "minegram";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    //public static ScreenOpener SCREEN_OPENER = ScreenOpener.DUMMY;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {} Mod (Main)", MOD_ID);

        // 1. 아이템 등록 (먼저 실행되는 것이 좋음)
        ModItems.registerModItems();

        // --- !!! 수정된 부분: 아이템 그룹 등록 호출 추가 !!! ---
        // 이 호출이 누락되면 아이템 그룹에 아이템이 추가되지 않습니다.
        ModItemGroups.registerItemGroups();
        // --- !!! 수정 끝 !!! ---
    }
}