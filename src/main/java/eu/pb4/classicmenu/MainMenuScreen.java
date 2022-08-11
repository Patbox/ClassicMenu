package eu.pb4.classicmenu;

import com.mojang.minecraft.gui.LoadLevelScreen;
import com.mojang.minecraft.gui.OptionsScreen;
import com.mojang.minecraft.util.Mth;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class MainMenuScreen extends BaseScreen {
    private String splash;

    @Override
    public void init() {
        this.buttons.clear();
        int centerPos = (this.width - 200) / 2;
        if (false) {
            this.buttons.add(new CustomButton(0, 0, 64, "SplashTest", () -> this.splash = ModInit.SPLASHES.get(new Random().nextInt(ModInit.SPLASHES.size()))));
        }
        int y = 116;
        this.buttons.add(new CustomButton(centerPos, y, "Singleplayer", () -> this.minecraft.openScreen(new LoadLevelScreen(this))));
        this.buttons.add(new CustomButton(centerPos, y += 25, "Multiplayer", () -> this.minecraft.openScreen(new MultiplayerScreen(this))));
        this.buttons.add(new CustomButton(centerPos, y += 25, 95, "Options", () -> this.minecraft.openScreen(new OptionsScreen(this, this.minecraft.options))));
        this.buttons.add(new CustomButton(centerPos + 106, y, 95, "Mods", () -> this.minecraft.openScreen(new ModListScreen(this))));
        this.buttons.add(new CustomButton(centerPos, y += 25, "Quit the Game", () -> {
            this.minecraft.destroy();
            System.exit(0);
        }));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        ModUtils.bindTexture(this.minecraft, "/dirt.png");
        ModUtils.drawBackground(this.minecraft.width, this.minecraft.height, this.blitOffset);

        if (this.splash == null) {
            this.splash = ModInit.SPLASHES.get(new Random().nextInt(ModInit.SPLASHES.size()));
        }

        /*fillGradient(0, 0, this.minecraft.width, this.minecraft.height, 0x0,
                ModUtils.hvsToRgb((float) (System.currentTimeMillis() / 20000d % 3.14d), 1, 1) | 0xFF000000);*/

        if (ModInit.logoStyle == ModInit.LogoStyle.MODERN) {
            ModUtils.bindTexture(this.minecraft, "classicmenu:mc_logo");
            int center = (this.width - 155 - 119) / 2;
            ModUtils.draw(center, 32, 155, 45, 0, 0, 155 / 256f, 45 / 256f, this.blitOffset);
            ModUtils.draw(center + 155, 32, 119, 45, 0, 45 / 256f, 119 / 256f, 45 * 2 / 256f, this.blitOffset);
        } else if (ModInit.logoStyle == ModInit.LogoStyle.ALT) {
            ModUtils.bindTexture(this.minecraft, "classicmenu:mc_logo_alt");
            int center = (this.width - 240) / 2;
            ModUtils.draw(center, 32, 128, 64, 0, 0, 1, 0.5f, this.blitOffset);
            ModUtils.draw(center + 128, 32, 128, 64, 0, 0.5f, 1, 1f, this.blitOffset);
        } else {
            ModUtils.bindTexture(this.minecraft, "classicmenu:mc_logo_retro");
            int center = (this.width - 256) / 2;
            ModUtils.draw(center, 32, 128, 64, 0, 0, 1, 0.5f, this.blitOffset);
            ModUtils.draw(center + 128, 32, 128, 64, 0, 0.5f, 1, 1f, this.blitOffset);
        }

        super.render(mouseX, mouseY);

        this.font.drawShadow("Minecraft 0.30c (" + FabricLoader.getInstance().getAllMods().size() + " mods)", 1, this.height - 9, 0xFFFFFF);
        this.font.drawShadow("Copyright Mojang AB. Do not distribute!",
                this.width - 1 - this.font.width("Copyright Mojang AB. Do not distribute!"), this.height - 9, 0xFFFFFF);

        if (ModInit.splashes) {
            GL11.glLoadIdentity();
            this.minecraft.q.a();
            GL11.glPushMatrix();

            GL11.glTranslated((this.width / 2 + 90), 70.0D, 0.0D);
            GL11.glRotated(360 - 20, 0, 0, 0.01);

            float scale = 1.8F - Math.abs(Mth.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
            scale = scale * 100.0F / (float) (this.font.width(this.splash) + 32);
            double textWidth = this.font.width(this.splash);

            GL11.glScaled(scale, scale, 0);
            this.font.drawShadow(this.splash, (int) (-textWidth / 2), -8, 0xfff717);

            GL11.glPopMatrix();
            GL11.glFlush();
        }
    }
}
