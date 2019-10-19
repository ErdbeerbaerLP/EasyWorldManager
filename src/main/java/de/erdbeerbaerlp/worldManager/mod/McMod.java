package de.erdbeerbaerlp.worldManager.mod;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import de.erdbeerbaerlp.worldManager.Main;
import de.erdbeerbaerlp.worldManager.mainWindow;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = McMod.MODID, name = McMod.NAME, version = Main.version, clientSideOnly=true, acceptedMinecraftVersions = "[1.8,1.12.2]")
public class McMod
{
    public static final String MODID = "worldmanager";
    public static final String NAME = "Easy World Manager";
    private static String dir;
    public static mainWindow win;
    public static KeyBinding keyB = new KeyBinding("Open EasyWorldManager", Keyboard.KEY_F4, "EasyWorldManager");
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	dir = event.getModConfigurationDirectory().getPath().replace("config", "saves");
    	ModMetadata meta = event.getModMetadata();
    	meta.modId = MODID;
    	meta.authorList = Lists.newArrayList();
    	meta.authorList.add("ErdbeerbaerLP");
    	meta.version = Main.version;
    	meta.name = NAME;
    	meta.description = "Opens the Easy World Manager on key press";
    	File directory = new File(dir);
    	win = Main.startAsMod(directory);
        MinecraftForge.EVENT_BUS.register(new de.erdbeerbaerlp.worldManager.mod.EventHandler());
    }
    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
    	ClientRegistry.registerKeyBinding(keyB);
    }
    
}
