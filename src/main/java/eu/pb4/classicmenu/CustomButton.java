package eu.pb4.classicmenu;

import com.mojang.minecraft.gui.Button;

public class CustomButton extends Button {
    public final Runnable action;

    public CustomButton(int x, int y, String string, Runnable action) {
        super(-1, x, y, string);
        this.action = action;
    }

    public CustomButton(int x, int y, int width, String string, Runnable action) {
        super(-1, x, y, width, 20, string);
        this.action = action;
    }
}
