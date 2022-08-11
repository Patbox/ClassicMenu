package eu.pb4.classicmenu.mixin.backgrounds;

import com.mojang.minecraft.gui.OptionsScreen;
import com.mojang.minecraft.gui.Screen;
import eu.pb4.classicmenu.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(com.mojang.minecraft.e.c.class )
public class CScreenMixin extends Screen {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/e/c;fillGradient(IIIIII)V", shift = At.Shift.AFTER))
    private void mainmenu_drawBackground(int mouseY, int par2, CallbackInfo ci) {
        if (this.minecraft.level == null) {
            ModUtils.bindTexture(this.minecraft, "/dirt.png");
            ModUtils.drawBackground(this.minecraft.width, this.minecraft.height, this.blitOffset);
        }
    }
}
