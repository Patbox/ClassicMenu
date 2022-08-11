package eu.pb4.classicmenu.mixin;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.MinecraftApplet;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelRenderer;
import eu.pb4.classicmenu.BaseScreen;
import eu.pb4.classicmenu.MainMenuScreen;
import eu.pb4.classicmenu.ModInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow public abstract void openScreen(Screen screen);

	@Shadow public Screen screen;

	@Shadow public Level level;

	@Shadow public LevelRenderer levelRenderer;

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/Minecraft;checkGlError(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 2))
	private void mainmenu_invoke(CallbackInfo ci) {
		ModInit.onInitialize((Minecraft) (Object) this);
	}

	@Redirect(method = "run", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/Minecraft;generateNewLevel(I)V"))
	private void mainmenu_createMenu(Minecraft instance, int i) {
		this.openScreen(new MainMenuScreen());
	}

	@Inject(method = "grabMouse", at = @At("HEAD"), cancellable = true)
	private void mainmenu_DontClose(CallbackInfo ci) {
		if ((this.screen instanceof BaseScreen && !(((BaseScreen) this.screen).isClosable())) || this.level == null || this.levelRenderer == null) {
			ci.cancel();
		}
	}

	@Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
	private void mainmenu_dontClose2(Screen screen, CallbackInfo ci) {
		if (screen == null && ((this.screen instanceof BaseScreen && !(((BaseScreen) this.screen).isClosable())) || this.level == null || this.levelRenderer == null)) {
			ci.cancel();
		}

		if (this.level != null && screen instanceof MainMenuScreen) {
			this.openScreen(null);
			ci.cancel();
		}
	}
}
