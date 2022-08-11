package eu.pb4.classicmenu;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.renderer.Tesselator;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModUtils {
    public static int registerTexture(Minecraft minecraft, Path path, String id){
        try {
            int num = minecraft.textures.addTexture(ImageIO.read(Files.newInputStream(path)));
            minecraft.textures.idMap.put(id, num);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            minecraft.textures.idMap.put(id, ModInit.noIcon);
            return ModInit.noIcon;
        }
    }


    public static void draw(int x, int y, int width, int height, float blitOffset) {
        draw(x, y, width, height, 0, 0, 1, 1, blitOffset);
    }

    public static void draw(int x, int y, int width, int height, float u0, float v0, float u1, float v1, float blitOffset) {
        Tesselator var9;
        (var9 = Tesselator.instance).begin();
        var9.vertexUV((float)x, (float)(y + height), blitOffset, u0, v1);
        var9.vertexUV((float)(x + width), (float)(y + height), blitOffset, u1, v1);
        var9.vertexUV((float)(x + width), (float)y, blitOffset, u1, v0);
        var9.vertexUV((float)x, (float)y, blitOffset, u0, v0);
        var9.end();
    }

    public static void drawBackground(int width, int height, float blitOffset) {
        int scaleWidth = width * 240 / height;
        int scaleHeight = 240;
        float textureWidth = 32.0F;

        Tesselator var4;
        (var4 = Tesselator.instance).begin();
        var4.begin();
        var4.color(4210752);
        var4.vertexUV(0.0F, (float)scaleHeight, 0.0F, 0.0F, (float)scaleHeight / textureWidth);
        var4.vertexUV((float)scaleWidth, (float)scaleHeight, 0.0F, (float)scaleWidth / textureWidth, (float)scaleHeight / textureWidth);
        var4.vertexUV((float)scaleWidth, 0.0F, 0.0F, (float)scaleWidth / textureWidth, 0.0F);
        var4.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        var4.end();
    }

    public static void bindTexture(Minecraft minecraft, String s) {
        GL11.glBindTexture(3553, minecraft.textures.getTextureId(s));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static List<String> splitToFit(String input, Font font, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (String word : input.split(" ")) {
            if (font.width(line + " " + word) < maxWidth ||line.length() == 0) {
                if (line.length() != 0) {
                    line.append(" ");
                }
                line.append(word);
            } else {
                lines.add(line.toString());
                line = new StringBuilder().append(word);
            }
        }

        if (line.length() > 0) {
            lines.add(line.toString());
        }

        return lines;
    }

    public static int hvsToRgb(float hue, float saturation, float value) {
        int h = (int) (hue * 6) % 6;
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToInt(value, t, p);
            case 1: return rgbToInt(q, value, p);
            case 2: return rgbToInt(p, value, t);
            case 3: return rgbToInt(p, q, value);
            case 4: return rgbToInt(t, p, value);
            case 5: return rgbToInt(value, p, q);
            default: return 0;
        }
    }

    public static int rgbToInt(float r, float g, float b) {
        return (((int) (r * 0xff)) & 0xFF) << 16 | (((int) (g * 0xff)) & 0xFF) << 8 | (((int) (b * 0xff) & 0xFF));
    }
}
