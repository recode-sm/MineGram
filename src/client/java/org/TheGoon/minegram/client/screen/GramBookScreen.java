package org.TheGoon.minegram.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GramBookScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier("minegram", "textures/gui/book1.png");

    public GramBookScreen() {
        super(Text.of("Gram Book"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderBackground(context);
        int x = this.width / 2 - 128;
        int y = this.height / 2 - 128;
        context.getMatrices().push();
        context.drawTexture(TEXTURE, x, y, 0, 0, 256, 256);
        context.getMatrices().pop();
    }
}
