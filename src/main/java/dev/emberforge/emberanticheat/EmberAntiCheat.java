package dev.emberforge.emberanticheat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import com.mojang.logging.LogUtils;
import dev.emberforge.emberanticheat.eventbuses.PlayerJoinChecker;
import dev.emberforge.emberanticheat.packets.MLMHandler;
import dev.emberforge.emberanticheat.packets.ModListMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

@Mod(EmberAntiCheat.MODID)
public class EmberAntiCheat {
    public static final String MODID = "emberanticheat";
    public static final String VERSION = "forge-1.20.1-1.0.0";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static XMLHandler xmlHandler;
    public static ReportLogger reportLogger;
	private static final String PROTOCOL_VERSION = "1";
    public static NetworkRegistry checkerInstance = NetworkRegistry.newSimpleChannel(ResourceLocation.parse("emberanticheat"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    public static EmberAntiCheat modInstance;
    private File modDirectory;
    public ArrayList<File> strangeFiles;
    private ArrayList<String> badWords;

    public EmberAntiCheat(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(PlayerJoinChecker::onPlayerJoinClient);

    }

    public boolean reloadList() {
        xmlHandler.reload();
        return true;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Initializing Mod EmberAntiCheat (Version: " + VERSION + ") by EmberForge Development");

		this.strangeFiles = new ArrayList<File>();
		this.badWords = new ArrayList<String>();
		this.badWords.add("liteloader");
		this.badWords.add("ray");
		this.badWords.add("vape");
		this.badWords.add("radar");
		this.badWords.add("wurst");

		checkerInstance.registerMessage(1, MLMHandler.class, ModListMessage.class, 0, MixinEnvironment.Side.SERVER);

		xmlHandler = new XMLHandler(event.getModConfigurationDirectory().getPath());
		if (event.getSide() == MixinEnvironment.Side.CLIENT) {
			reportLogger = new ReportLogger(Minecraft.getInstance().gameDirectory.getPath());
		} else {
			reportLogger = new ReportLogger(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory().getPath());
		}
		System.out.println("Loaded accepted mod list!");
		Iterator<?> i;
		if (event.getSide() == MixinEnvironment.Side.CLIENT) {
			if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
				System.out.println("Loading in a windows environment!");
				this.modDirectory = new File(Minecraft.getInstance().gameDirectory + "\\mods\\");
			} else {
				System.out.println("Loading in a non-windows environment!");
				this.modDirectory = new File(Minecraft.getInstance().gameDirectory + "/mods/");
			}
			Collection<File> files = FileUtils.listFiles(this.modDirectory, null, true);

			for (i = files.iterator(); i.hasNext(); ) {
				File file = (File) i.next();
				if (ArrayUtils.nameContains(badWords, file.getName())) {
					this.strangeFiles.add(file);
				}
			}
		}
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

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