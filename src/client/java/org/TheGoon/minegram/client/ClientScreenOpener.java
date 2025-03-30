package org.TheGoon.minegram.client;

import net.minecraft.client.MinecraftClient;
import org.TheGoon.minegram.ScreenOpener;
import org.TheGoon.minegram.client.screen.GramBookScreen;

public class ClientScreenOpener implements ScreenOpener { // 반드시 인터페이스 구현
    @Override
    public void openCustomScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> client.setScreen(new GramBookScreen())); // GramBookScreen 열기
        }
    }
}
