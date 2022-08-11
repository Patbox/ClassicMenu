package eu.pb4.classicmenu;

import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.Screen;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModListScreen extends BaseScreen {
    private final List<ModContainer> mods = new ArrayList<>(FabricLoader.getInstance().getAllMods())
            .stream().sorted(Comparator.comparing((x) -> x.getMetadata().getId())).collect(Collectors.toList());
    private int page = 0;

    private ModContainer selectedContainer;
    private int selectedIndex = -1;

    private final Screen previousScreen;

    public ModListScreen(Screen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void init() {
        this.buttons.clear();

        this.buttons.add(new CustomButton( this.width / 2 - 100, this.height / 6 + 168 + 8, "Back", () -> {
            this.minecraft.openScreen(this.previousScreen);
        }));

        this.buttons.add(new CustomButton((this.width / 2 - 10) / 2 + 5 - 32 - 8, this.height / 6 + 168 - 14, 16, "<", () -> {
            this.page = Math.max(this.page - 1, 0);
        }));
        this.buttons.add(new CustomButton((this.width / 2 - 10) / 2 + 5 + 32 - 8, this.height / 6 + 168 - 14, 16, ">", () -> {
            this.page = Math.min(this.page + 1, (int) Math.ceil((double) this.mods.size() / ((this.height - 50) / 40)) - 1);
        }));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        int maxDisplayed = (this.height - 50) / 40;

        for (int i = 0; i < maxDisplayed; i++) {
            int index = i + this.page * maxDisplayed;

            int baseX = 20;
            int baseY = i * 40 + 30;

            if (this.mods.size() <= index) {
                break;
            }

            if (x >= baseX - 2 && y >= baseY && x <= this.width / 2 - 10 && y <= baseY + 36) {
                this.selectedContainer = this.mods.get(index);
                this.selectedIndex = index;
                break;
            }

        }

        super.mouseClicked(x, y, button);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        ModUtils.bindTexture(this.minecraft, "/dirt.png");
        ModUtils.drawBackground(this.minecraft.width, this.minecraft.height, this.blitOffset);

        drawCenteredString(this.font, "Mods", this.width / 2, 15, 16777215);

        int maxDisplayed = (this.height - 50) / 40;

        for (int i = 0; i < maxDisplayed; i++) {
            int index = i + this.page * maxDisplayed;

            int baseX = 10;
            int baseY = i * 40 + 30;

            if (this.mods.size() <= index) {
                break;
            }

            fill(baseX - 3, baseY - 1, this.width / 2 - 10 + 1, baseY + 37, this.selectedIndex == index ? 0xffffffff : 0xffbbbbbb);
            fill(baseX - 2, baseY, this.width / 2 - 10, baseY + 36, 0xff050505);

            ModContainer mod = this.mods.get(index);

            ModUtils.bindTexture(this.minecraft, "classicmenu:mod_icon/" + mod.getMetadata().getId());
            ModUtils.draw(baseX, baseY + 2, 32, 32, this.blitOffset);

            String name = mod.getMetadata().getName() + " &8(" + mod.getMetadata().getVersion().getFriendlyString() + ")";

            int maxWidth = this.width / 2 - 10 - (baseX + 32 + 4) - 5;
            if (this.font.width(name) > maxWidth) {
                name = mod.getMetadata().getName();
                if (this.font.width(name) > maxWidth) {
                    while (this.font.width(name + "...") > maxWidth) {
                        name = name.substring(0, name.length() - 1);
                    }
                    name = name + "...";
                }
            }
            String[] words = mod.getMetadata().getDescription().split(" ");
            String[] lines = new String[] { "", "" };

            boolean firstLine = true;
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                if (this.font.width(line + " " + word) < maxWidth) {
                    if (line.length() != 0) {
                        line.append(" ");
                    }
                    line.append(word);
                } else if (firstLine) {
                    lines[0] = line.toString();
                    line = new StringBuilder().append(word);
                    firstLine = false;
                } else {
                    lines[1] = line.toString() + "...";
                    break;
                }
            }

            if (line.length() > 0) {
                lines[firstLine ? 0 : 1] = line.toString();
            }

            this.font.draw(name, baseX + 32 + 4, baseY + 2, 0xffffff);

            this.font.draw(lines[0], baseX + 32 + 4 + 2, baseY + 2 + 9, 0x999999);
            this.font.draw(lines[1], baseX + 32 + 4 + 2, baseY + 2 + 9 + 9, 0x999999);
        }

        if (this.selectedContainer != null) {
            int baseX = 10 + this.width / 2;
            int baseY = 30;

            //fill(baseX - 3, baseY - 1, this.width - 10 + 1, this.height / 6 + 168 + 1, 0xffbbbbbb);
            fill(baseX - 2, baseY, this.width - 10, this.height / 6 + 168, 0xbb050505);
            //fillGradient(baseX - 2, baseY, this.width - 10, this.height / 6 + 168, 1610941696, -1607454624);

            ModUtils.bindTexture(this.minecraft, "classicmenu:mod_icon/" + this.selectedContainer.getMetadata().getId());
            ModUtils.draw(baseX, baseY + 2, 32, 32, this.blitOffset);



            ModMetadata metadata = this.selectedContainer.getMetadata();

            int maxWidthTitle = this.width / 2 - 10 - (10 + 32 + 4) - 5;
            int maxWidth = this.width / 2 - 10 - (10 + 4) - 5;

            int y = 0;

            for (String line : ModUtils.splitToFit(metadata.getName(), this.font, maxWidthTitle)) {
                int xAdd = y > 34 ? 0 : 32;

                this.font.draw(line, baseX + xAdd + 4, baseY + 2 + y, 0xffffff);
                y += 9;
            }

            this.font.draw("Version: " + metadata.getVersion(), baseX + (y > 34 ? 0 : 32) + 4, baseY + 2 + y, 0x999999);

            if (y < 34) {
                y = 34;
            }

            for (String line : ModUtils.splitToFit(metadata.getDescription(), this.font, maxWidth)) {
                this.font.draw(line, baseX + 4, baseY + 2 + y, 0xcccccc);
                y += 9;
            }
            y += 9;
            if (metadata.getAuthors().size() > 0) {
                this.font.draw("Authors: ", baseX + 4, baseY + 2 + y, 0xffffff);
                y += 9;

                for (Person line : metadata.getAuthors()) {
                    this.font.draw("- " + line.getName(), baseX + 8, baseY + 2 + y, 0xcccccc);
                    y += 9;
                }
                y += 9;
            }

            if (metadata.getContributors().size() > 0) {
                this.font.draw("Contributors: ", baseX + 4, baseY + 2 + y, 0xffffff);
                y += 9;

                for (Person line : metadata.getContributors()) {
                    this.font.draw("- " + line.getName(), baseX + 8, baseY + 2 + y, 0xcccccc);
                    y += 9;
                }
                y += 9;
            }
        }

        drawCenteredString(this.font,  (this.page + 1) + "/" + ((int) Math.ceil((double) this.mods.size() / maxDisplayed)), (this.width / 2 - 10) / 2 + 5, this.height / 6 + 168 - 8, 0x999999);
        super.render(mouseX, mouseY);
    }


}
