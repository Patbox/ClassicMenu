package eu.pb4.classicmenu;

import com.mojang.minecraft.User;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.net.ConnectionManager;
import org.lwjgl.input.Keyboard;

public class MultiplayerScreen extends BaseScreen {
    private final Screen previousScreen;
    String serverIp = "";
    private final int ticks = 0;

    private InputField serverAddress;
    private InputField playerName;

    public MultiplayerScreen(Screen previous) {
        this.previousScreen = previous;
    }

    public final void init() {
        this.buttons.clear();

        Keyboard.enableRepeatEvents(true);
        this.buttons.add(new CustomButton(this.width / 2 - 100, this.height / 6 + 168 - 26, "Connect", () -> {
            if (this.serverAddress.value.isEmpty()) {
                return;
            }

            try {
                String[] ip = this.serverAddress.value.split(":");
                Level level = new Level();
                level.setData(8, 8, 8, new byte[512]);
                this.minecraft.setLevel(level);

                User user = this.minecraft.user;
                String name;
                String pass;

                if (!this.playerName.value.isEmpty()) {
                    name = this.playerName.value;
                } else if (user == null || user.name == null) {
                    name = "Player";
                } else {
                    name = user.name;
                }

                if (user == null || user.mpPass == null || !this.playerName.value.isEmpty()) {
                    pass = "";
                } else {
                    pass = user.mpPass;
                }

                this.minecraft.connectionManager = new ConnectionManager(this.minecraft, ip[0], ip.length == 1 ? 25565 : Integer.parseInt(ip[1]), name, pass);
                this.minecraft.openScreen(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }));

        this.buttons.add(new CustomButton(this.width / 2 - 100, this.height / 6 + 168, "Back", () -> {
            this.minecraft.openScreen(this.previousScreen);
        }));

        this.serverAddress = new InputField(this.width / 2 - 100, this.height / 2);
        this.playerName = new InputField(this.width / 2 - 100, this.height / 2 - 40);
        this.playerName.emptyDisplay = this.minecraft.user.name;
        this.playerName.validator = (value, letter) -> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_".indexOf(letter) >= 0 && value.length() < 16;

        this.fieldList.add(this.serverAddress);
        this.fieldList.add(this.playerName);
        super.init();
    }

    public final void render(int mouseX, int mouseY) {
        ModUtils.bindTexture(this.minecraft, "/dirt.png");
        ModUtils.drawBackground(this.minecraft.width, this.minecraft.height, this.blitOffset);
        drawCenteredString(this.font, "Connect to server", this.width / 2, 40, 16777215);
        drawString(this.font, "Username:", this.width / 2 - 100, this.height / 2 - 40 - 12, 0xbbbbbb);
        drawString(this.font, "Server Address:", this.width / 2 - 100, this.height / 2 - 12, 0xbbbbbb);

        this.minecraft.font.drawShadow("Logged as: " + this.minecraft.user.name, 1, this.height - 9, 0xFFFFFF);

        super.render(mouseX, mouseY);
    }
}
