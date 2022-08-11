package eu.pb4.classicmenu;

import com.mojang.minecraft.Minecraft;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class ModInit {
	public static int missingTexture;
	public static int noIcon;

	public static LogoStyle logoStyle = LogoStyle.RETRO;
	public static boolean splashes = true;
	public static List<String> SPLASHES = new ArrayList<>();

	public static void onInitialize(Minecraft minecraft) {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("classicmenu.properties");

		Properties config = new Properties();
		config.setProperty("logo_type", "retro");
		config.setProperty("show_splash", "true");
		if (Files.exists(configPath)) {
			try {
				InputStream stream = Files.newInputStream(configPath);
				config.load(stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			logoStyle = LogoStyle.valueOf(config.getProperty("logo_type").toUpperCase(Locale.ROOT));
			splashes = config.getProperty("splashes").equals("true");
		} catch (Throwable e) {}

		try {
			OutputStream stream = Files.newOutputStream(configPath, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			config.store(stream, "Logo Types: retro, modern, alt");
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, 0xfb40f9);
		image.setRGB(0, 1, 0xfb40f9);
		image.setRGB(1, 0, 0x000000);
		image.setRGB(1, 1, 0xfb40f9);
		missingTexture = minecraft.textures.addTexture(image);

		ModContainer self = FabricLoader.getInstance().getModContainer("classicmenu").get();

		try {
			Files.readAllLines(self.findPath("splashes.txt").get()).stream().filter(x -> !x.codePoints().anyMatch((y) -> y > 256)).forEach(SPLASHES::add);
		} catch (Throwable t) {
			t.printStackTrace();
			SPLASHES.add("MISSINGNO.");
		}


		ModUtils.registerTexture(minecraft, self.findPath("assets/minecraft-logo.png").get(), "classicmenu:mc_logo");
		ModUtils.registerTexture(minecraft, self.findPath("assets/altlogo.png").get(), "classicmenu:mc_logo_alt");
		ModUtils.registerTexture(minecraft, self.findPath("assets/cobblelogo.png").get(), "classicmenu:mc_logo_retro");
		ModUtils.registerTexture(minecraft, self.findPath("assets/minecraft_icon.png").get(), "classicmenu:mod_icon/minecraft");
		ModUtils.registerTexture(minecraft, self.findPath("assets/java_icon.png").get(), "classicmenu:mod_icon/java");
		noIcon = ModUtils.registerTexture(minecraft, self.findPath("assets/default_mod_icon.png").get(), "classicmenu:default_mod");

		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			try {
				if (!minecraft.textures.idMap.containsKey("classicmenu:mod_icon/" + mod.getMetadata().getId())) {
					ModUtils.registerTexture(minecraft, mod.findPath(mod.getMetadata().getIconPath(64).get()).get(), "classicmenu:mod_icon/" + mod.getMetadata().getId());
				}
			} catch (Throwable throwable) {
				minecraft.textures.idMap.put("classicmenu:mod_icon/" + mod.getMetadata().getId(), noIcon);
			}
		}

		minecraft.user.hasPaid = true;
	}


	public enum LogoStyle {
		MODERN,
		ALT,
		RETRO
	}
}
