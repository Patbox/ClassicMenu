package eu.pb4.classicmenu.mixin;

import com.mojang.minecraft.gui.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.class)
public interface ButtonAccessor {
    @Accessor("w")
    int getW();

    @Accessor("w")
    void setW(int w);
}
