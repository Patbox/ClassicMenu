package eu.pb4.classicmenu.mixin;

import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.Screen;
import eu.pb4.classicmenu.CustomButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/gui/Screen;buttonClicked(Lcom/mojang/minecraft/gui/Button;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void mainMenu_clickButton(int y, int button_, int par3, CallbackInfo ci, Button button) {
        if (button instanceof CustomButton) {
            ((CustomButton) button).action.run();
        }
    }
}
