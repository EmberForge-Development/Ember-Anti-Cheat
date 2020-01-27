package net.kineticdevelopment.kineticanticheat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import net.kineticdevelopment.kineticanticheat.commands.ScreenGrab;
import net.kineticdevelopment.kineticanticheat.eventbuses.PlayerJoinChecker;
import net.kineticdevelopment.kineticanticheat.packets.MLMHandler;
import net.kineticdevelopment.kineticanticheat.packets.ModListMessage;
import net.kineticdevelopment.kineticanticheat.packets.RPLHandler;
import net.kineticdevelopment.kineticanticheat.packets.ResourcePackListMessage;
import net.kineticdevelopment.kineticanticheat.packets.screengrab.SGCMHandler;
import net.kineticdevelopment.kineticanticheat.packets.screengrab.SGSMHandler;
import net.kineticdevelopment.kineticanticheat.packets.screengrab.ScreenGrabClientMessage;
import net.kineticdevelopment.kineticanticheat.packets.screengrab.ScreenGrabServerMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid="kinetic anti-cheat", version="12.3.0", acceptedMinecraftVersions="[1.12.2]")
public class Main {
  public static final String MODID = "kinetic anti-cheat";
  public static final String VERSION = "12.3.0";
  public static XMLHandler xmlHandler;
  public static ReportLogger reportLogger;
  public static SimpleNetworkWrapper checkerInstance = NetworkRegistry.INSTANCE.newSimpleChannel("kinetic anti-cheat");
  @Mod.Instance("kinetic anti-cheat")
  public static Main modInstance;
  private File modDirectory;
  public ArrayList<File> strangeFiles;
  private ArrayList<String> badWords;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
	  this.strangeFiles = new ArrayList<File>();
	  this.badWords = new ArrayList<String>();
	  this.badWords.add("liteloader");
	  this.badWords.add("ray");
	  this.badWords.add("vape");
	  this.badWords.add("radar");
	  this.badWords.add("wurst");
    
	  checkerInstance.registerMessage(MLMHandler.class, ModListMessage.class, 0, Side.SERVER);
	  checkerInstance.registerMessage(RPLHandler.class, ResourcePackListMessage.class, 1, Side.SERVER);
	  checkerInstance.registerMessage(SGCMHandler.class, ScreenGrabClientMessage.class, 2, Side.CLIENT);
	  checkerInstance.registerMessage(SGSMHandler.class, ScreenGrabServerMessage.class, 3, Side.SERVER);
    
	  xmlHandler = new XMLHandler(event.getModConfigurationDirectory().getPath());
	  if (event.getSide() == Side.CLIENT) {
		  reportLogger = new ReportLogger(
				  Minecraft
				  .getMinecraft()
				  .mcDataDir
				  .getPath());
	  } else {
		  reportLogger = new ReportLogger(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory().getPath());
	  }
	  System.out.println("Loaded accepted mod list!");
	  Iterator<?> i;
	  if (event.getSide() == Side.CLIENT) {
		  if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
			  System.out.println("Loading in a windows environment!");
			  this.modDirectory = new File(Minecraft.getMinecraft().mcDataDir + "\\mods\\");
		  }
		  else {
			  System.out.println("Loading in a non-windows environment!");
			  this.modDirectory = new File(Minecraft.getMinecraft().mcDataDir + "/mods/");
		  }
		  Collection<File> files = FileUtils.listFiles(this.modDirectory, null, true);
		  
		  for (i = files.iterator(); i.hasNext();) {
			  File file = (File)i.next();
			  if(ArrayUtils.nameContains(badWords, file.getName())) {
				  this.strangeFiles.add(file);
			  }
		  }
	  }
  }
  
  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
	  event.registerServerCommand(new ScreenGrab());
  }
  
  public boolean reloadList() {
	  xmlHandler.reload();
	  return true;
  }
  
  @EventHandler
  public void init(FMLInitializationEvent event) {
	  MinecraftForge.EVENT_BUS.register(new PlayerJoinChecker());
  }
}