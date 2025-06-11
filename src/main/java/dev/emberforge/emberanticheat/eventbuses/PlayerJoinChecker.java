package dev.emberforge.emberanticheat.eventbuses;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dev.emberforge.emberanticheat.EmberAntiCheat;
import dev.emberforge.emberanticheat.ModList;
import dev.emberforge.emberanticheat.packets.ModListMessage;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.io.FileUtils;


import net.minecraft.client.Minecraft;


public class PlayerJoinChecker {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onPlayerJoinClient(EntityJoinLevelEvent e) {
		if ((e.getEntity() instanceof Player)) {
	    	Player player = (Player) e.getEntity();
			if(!player.isLocalPlayer()) {

				if(!Minecraft.getInstance().hasSingleplayerServer()) {
					List<String> currentlyLoadedMods = ModList.getLoadedMods();
				      
					String modListString = "";
					for (int i = 0; i < currentlyLoadedMods.size(); i++) {
						modListString = modListString + currentlyLoadedMods.get(i) + ",";
					}
					for (int j = 0; j < EmberAntiCheat.modInstance.strangeFiles.size(); j++) {
						modListString = modListString + ((File)EmberAntiCheat.modInstance.strangeFiles.get(j)).getName() + ",";
					}
					System.out.println("Sending modlist to server: total mods = " + currentlyLoadedMods.size() + ", " + modListString);
			      
					EmberAntiCheat.checkerInstance.sendToServer(new ModListMessage(modListString));
					
					String rps = "";
					
					Iterator<File> i;
					Collection<File> files = FileUtils.listFiles(new File(Minecraft.getInstance().gameDirectory + "/resourcepacks"), null, false);
					  
					for (i = files.iterator(); i.hasNext();) {
						File file = (File)i.next();
						rps = rps + file.getName()+",";
					}
				}
			}
		}
	}
}

