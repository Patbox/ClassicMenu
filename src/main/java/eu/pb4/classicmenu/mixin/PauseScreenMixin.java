package eu.pb4.classicmenu.mixin;

import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import eu.pb4.classicmenu.CustomButton;
import eu.pb4.classicmenu.MainMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    @Inject(method = "init", at = @At("TAIL"))
    private void mainmenu_addExis(CallbackInfo ci) {
        this.buttons.add(new CustomButton(this.width / 2 - 100, this.height / 4 + 120 + 24, "Exit to menu", () -> {
            if (this.minecraft.connectionManager != null) {
                this.minecraft.connectionManager.connection.disconnect();
                this.minecraft.connectionManager = null;
            }
            this.minecraft.level = null;
            this.minecraft.openScreen(new MainMenuScreen());
        }));
    }
}
