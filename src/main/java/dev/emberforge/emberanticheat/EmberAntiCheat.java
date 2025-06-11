package net.kineticdevelopment.kineticanticheat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import net.kineticdevelopment.kineticanticheat.eventbuses.PlayerJoinChecker;
import net.kineticdevelopment.kineticanticheat.packets.MLMHandler;
import net.kineticdevelopment.kineticanticheat.packets.ModListMessage;
import net.kineticdevelopment.kineticanticheat.packets.RPLHandler;
import net.kineticdevelopment.kineticanticheat.packets.ResourcePackListMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(EmberAntiCheat.MODID)
public class Main {
  public static final String MODID = "emberanticheat";
  public static final String VERSION = "forge-1.20.1-1.0.0";
  private static final Logger LOGGER = LogUtils.getLogger();
  public static XMLHandler xmlHandler;
  public static ReportLogger reportLogger;
  public static SimpleNetworkWrapper checkerInstance = NetworkRegistry.INSTANCE.newSimpleChannel("emberanticheat");
  @Mod.Instance("emberanticheat")
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
  
  public boolean reloadList() {
	  xmlHandler.reload();
	  return true;
  }

  public EmberAntiCheat(FMLJavaModLoadingContext context) {
		IEventBus modEventBus = context.getModEventBus();

		modEventBus.addListener(this::commonSetup);

		MinecraftForge.EVENT_BUS.register(this);

		// Register the item to a creative tab
		modEventBus.addListener(PlayerJoinChecker::);

		// Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
		context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("Initializing Mod EmberAntiCheat (Version: " + VERSION + ") by EmberForge Development");
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
	}

	// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			// Some client setup code
		}
	}

//  @EventHandler
//  public void init(FMLInitializationEvent event) {
//	  MinecraftForge.EVENT_BUS.register(new PlayerJoinChecker());
//  }
}