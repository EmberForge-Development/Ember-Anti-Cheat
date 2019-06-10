package xyz.sleepingtea.modchecker;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import net.minecraft.util.Session;

@Mod(modid="kinetic anti-cheat", version="12.2.2", acceptedMinecraftVersions="[1.12.2]")
public class ModChecker
{
  public static final String MODID = "kinetic anti-cheat";
  public static final String VERSION = "12.2.2";
  public static XMLHandler xmlHandler;
  public static ReportLogger reportLogger;
  public static SimpleNetworkWrapper checkerInstance = NetworkRegistry.INSTANCE.newSimpleChannel("kinetic anti-cheat");
  @Mod.Instance("kinetic anti-cheat")
  public static ModChecker modInstance;
  private File modDirectory;
  public ArrayList<File> strangeFiles;
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    this.strangeFiles = new ArrayList();
    
    checkerInstance.registerMessage(MyMessageHandler.class, MyMessage.class, 0, Side.SERVER);
    
    xmlHandler = new XMLHandler(event.getModConfigurationDirectory().getPath());
    if (event.getSide() == Side.CLIENT) {
      reportLogger = new ReportLogger(Minecraft.getMinecraft().mcDataDir.getPath());
    } else {
      reportLogger = new ReportLogger(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory().getPath());
    }
    System.out.println("Loaded accepted mod list!");
    Iterator i;
    if (event.getSide() == Side.CLIENT)
    {
      if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0)
      {
        System.out.println("Loading in a windows environment!");
        this.modDirectory = new File(Minecraft.getMinecraft().mcDataDir + "\\mods\\");
      }
      else
      {
        System.out.println("Loading in a non-windows environment!");
        this.modDirectory = new File(Minecraft.getMinecraft().mcDataDir + "/mods/");
      }
      Collection files = FileUtils.listFiles(this.modDirectory, null, true);
      for (i = files.iterator(); i.hasNext();)
      {
        File file = (File)i.next();
        if (file.getName().toLowerCase().contains("liteloader".toLowerCase())) {
          this.strangeFiles.add(file);
        }
        if (file.getName().toLowerCase().contains("ray".toLowerCase())) {
          this.strangeFiles.add(file);
        }
        if (file.getName().toLowerCase().contains("vape".toLowerCase())) {
          this.strangeFiles.add(file);
        }
        if (file.getName().toLowerCase().contains("radar".toLowerCase())) {
          this.strangeFiles.add(file);
        }
      }
    }
  }
  
  public boolean reloadList()
  {
    xmlHandler.reload();
    return true;
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    MinecraftForge.EVENT_BUS.register(new PlayerJoinChecker());
  }
}

