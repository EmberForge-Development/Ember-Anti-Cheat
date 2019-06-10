package xyz.sleepingtea.modchecker;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;


public class PlayerJoinChecker
{
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPlayerJoinClient(EntityJoinWorldEvent e)
  {
    if ((e.getEntity() instanceof EntityPlayer))
    {  
    	EntityPlayer player = (EntityPlayer) e.getEntity();
		if(player.world.isRemote)
		{
			if(!Minecraft.getMinecraft().isIntegratedServerRunning())
			{
				List<ModContainer> mods = Loader.instance().getModList();
			      
			      String modListString = "";
			      for (int i = 0; i < mods.size(); i++) {
			        modListString = modListString + ((ModContainer)mods.get(i)).getModId() + ",";
			      }
			      for (int j = 0; j < ModChecker.modInstance.strangeFiles.size(); j++) {
			        modListString = modListString + ((File)ModChecker.modInstance.strangeFiles.get(j)).getName() + ",";
			      }
			      System.out.println("Sending modlist to server: total mods = " + mods.size() + ", " + modListString);
			      
			      ModChecker.checkerInstance.sendToServer(new MyMessage(modListString));
			      
	}
    }
 }
 }
}

