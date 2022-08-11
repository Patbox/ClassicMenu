package eu.pb4.classicmenu;

import com.mojang.minecraft.User;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.net.ConnectionManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class BaseScreen extends Screen {
    private int ticks;
    public List<InputField> fieldList = new ArrayList<>();

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        super.init();
    }

    public void close() {
        Keyboard.enableRepeatEvents(false);
    }

    public void tick() {
        ++this.ticks;
    }

    protected void keyPressed(char c, int key) {
        for (InputField field : this.fieldList) {
            if (field.selected) {
                field.keyPressed(c, key);
            }
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        for (InputField input : this.fieldList) {
            input.selected = input.isIn(x, y);
            input.mouseClicked(x, y, button);
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        for (InputField input : this.fieldList) {
            input.render(this.font, this.ticks);
        }

        super.render(mouseX, mouseY);
    }

    public boolean isClosable() {
        return true;
    }
}
