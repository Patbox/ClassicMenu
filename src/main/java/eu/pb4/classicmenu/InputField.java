package eu.pb4.classicmenu;

import com.mojang.minecraft.gui.DrawableHelper;
import com.mojang.minecraft.gui.Font;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.function.Function;

public class InputField extends DrawableHelper {
    public int x;
    public int y;

    public int width;
    public int height = 20;

    public String value = "";
    public String emptyDisplay = "";

    public TextValidator validator = TextValidator.DEFAULT;

    public boolean selected = false;

    public InputField() {
        this.x = 0;
        this.y = 0;
        this.width = 200;
    }

    public InputField(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 200;
    }

    public InputField(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public boolean isIn(int x, int y) {
        return (x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.height);
    }


    public void render(Font font, int tick) {
        fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, this.selected ? 0xffffffff : 0xffbbbbbb);
        fill(this.x, this.y, this.x + this.width, this.y + this.height, 0xff050505);

        if (!this.value.isEmpty() || this.selected) {
            drawString(font, this.value + (tick / 6 % 2 == 0 && this.selected ? "_" : ""), this.x + 5, this.y + 5, 16777215);
        } else {
            drawString(font, this.emptyDisplay, this.x + 5, this.y + 5, 0x777777);
        }
    }

    public void keyPressed(char c, int key) {
        if (this.selected) {
            if (key == Keyboard.KEY_V && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                try {
                    for (char letter : ((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)).toCharArray()) {
                        if (this.validator.canType(this.value, letter)) {
                            this.value = this.value + letter;
                        }
                    }
                } catch (UnsupportedFlavorException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (key == 14 && this.value.length() > 0) {
                this.value = this.value.substring(0, this.value.length() - 1);
            }

            if (this.validator.canType(this.value, c)) {
                this.value = this.value + c;
            }
        }
    }

    public void mouseClicked(int x, int y, int button) {
    }

    public interface TextValidator {
        static TextValidator DEFAULT = (value, letter) -> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_'*!\\\"#%/()=+?[]{}<>@|$;".indexOf(letter) >= 0 && value.length() < 64;
        boolean canType(String current, char letter);
    }
}
