package me.farmans.keybindsfix;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.*;

@Mod(modid = KeybindsFixMod.MODID, version = KeybindsFixMod.VERSION)
public class KeybindsFixMod {
    public static final String MODID = "KeybindsFix";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        String configPath = Minecraft.getMinecraft().mcDataDir + "/config/KeybindsFix_backup_options.txt";
        File config = new File(configPath);
        try {
            if (config.createNewFile()) {
                logger.info("File created: " + config.getName());
            } else {
                String optionsPath = Minecraft.getMinecraft().mcDataDir + "/options.txt";
                File options = new File(optionsPath);
                try (
                        InputStream in = new BufferedInputStream(
                                new FileInputStream(config));
                        OutputStream out = new BufferedOutputStream(
                                new FileOutputStream(options))) {
                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        String optionsPath = Minecraft.getMinecraft().mcDataDir + "/options.txt";
        String configPath = Minecraft.getMinecraft().mcDataDir + "/config/KeybindsFix_backup_options.txt";
        Thread hook = new Thread(() -> {
            File options = new File(optionsPath);
            File config = new File(configPath);
            try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(options));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(config))) {
                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
    }
}
