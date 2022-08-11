package eu.pb4.classicmenu.mixin.backgrounds;

import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.LoadLevelScreen;
import com.mojang.minecraft.gui.NewLevelScreen;
import com.mojang.minecraft.gui.Screen;
import eu.pb4.classicmenu.CustomButton;
import eu.pb4.classicmenu.MainMenuScreen;
import eu.pb4.classicmenu.ModUtils;
import eu.pb4.classicmenu.mixin.ButtonAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(LoadLevelScreen.class)
public abstract class LoadLevelScreenMixin extends Screen {
    @Shadow protected Screen parent;

    @Shadow public abstract void init();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/gui/LoadLevelScreen;fillGradient(IIIIII)V", shift = At.Shift.AFTER))
    private void mainmenu_drawBackground(int mouseY, int par2, CallbackInfo ci) {
        if (this.minecraft.level == null) {
            ModUtils.bindTexture(this.minecraft, "/dirt.png");
            ModUtils.drawBackground(this.minecraft.width, this.minecraft.height, this.blitOffset);
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void mainmenu_addCreateButton(CallbackInfo ci) {
        if (this.minecraft.level == null) {
            this.buttons.add(new CustomButton(this.width / 2 - 100, this.height / 6 + 120 + 12, 114, "Generate new Level...",
                    () -> this.minecraft.openScreen(new NewLevelScreen(this))));
            Button button = (Button) this.buttons.get(5);
            button.x += 120;
            ((ButtonAccessor) button).setW(200 - 120);
        }
    }

    @Redirect(method = "buttonClicked", at = @At(value = "FIELD", target = "Lcom/mojang/minecraft/gui/LoadLevelScreen;o:Z", ordinal = 1))
    private boolean mainmenu_unlockButtons(LoadLevelScreen instance) {
        return true;
    }

    @Redirect(method = "buttonClicked", at = @At(value = "FIELD", target = "Lcom/mojang/minecraft/gui/LoadLevelScreen;o:Z", ordinal = 2))
    private boolean mainmenu_unlockButtons2(LoadLevelScreen instance) {
        return true;
    }
}
