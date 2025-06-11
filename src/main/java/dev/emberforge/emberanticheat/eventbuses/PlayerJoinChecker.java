package dev.emberforge.emberanticheat.eventbuses;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.kineticdevelopment.kineticanticheat.Main;
import net.kineticdevelopment.kineticanticheat.packets.ModListMessage;
import net.kineticdevelopment.kineticanticheat.packets.ResourcePackListMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class PlayerJoinChecker
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerJoinClient(EntityJoinWorldEvent e) {
		if ((e.getEntity() instanceof EntityPlayer)) {
	    	EntityPlayer player = (EntityPlayer) e.getEntity();
			if(player.world.isRemote) {

				if(!Minecraft.getMinecraft().isIntegratedServerRunning()) {
					List<ModContainer> mods = Loader.instance().getModList();
				      
					String modListString = "";
					for (int i = 0; i < mods.size(); i++) {
						modListString = modListString + ((ModContainer)mods.get(i)).getModId() + ",";
					}
					for (int j = 0; j < Main.modInstance.strangeFiles.size(); j++) {
						modListString = modListString + ((File)Main.modInstance.strangeFiles.get(j)).getName() + ",";
					}
					System.out.println("Sending modlist to server: total mods = " + mods.size() + ", " + modListString);
			      
					Main.checkerInstance.sendToServer(new ModListMessage(modListString));
					
					String rps = "";
					
					Iterator<File> i;
					Collection<File> files = FileUtils.listFiles(new File(Minecraft.getMinecraft().mcDataDir + "/resourcepacks"), null, false);
					  
					for (i = files.iterator(); i.hasNext();) {
						File file = (File)i.next();
						rps = rps + file.getName()+",";
					}
					
					Main.checkerInstance.sendToServer(new ResourcePackListMessage(rps));
				}
			}
		}
	}
}

