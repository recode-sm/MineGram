package org.TheGoon.minegram.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.TheGoon.minegram.Minegram; // LOGGER 및 SCREEN_OPENER 참조 위해 필요

/**
 * 클라이언트 측 초기화를 담당합니다.
 */
@Environment(EnvType.CLIENT) // 클라이언트 전용
public class MinegramClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //Minegram.SCREEN_OPENER = new ClientScreenOpener();
    }
}