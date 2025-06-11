package dev.emberforge.emberanticheat.packets;

import java.util.ArrayList;
import java.util.Arrays;

import dev.emberforge.emberanticheat.EmberAntiCheat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class MLMHandler implements IMessageHandler<dev.emberforge.emberanticheat.packets.ModListMessage, IMessage> {
	public IMessage onMessage(dev.emberforge.emberanticheat.packets.ModListMessage message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		
		String modList = message.toSend;
		if (Main.xmlHandler.debugMode.intValue() == 1) {
			System.out.println("Player joined: " + serverPlayer.getName() + " - Comparing mods...");
		}
		ArrayList<String> items = new ArrayList<String>(Arrays.asList(modList.split(",")));
		for (int i = 0; i < Main.xmlHandler.defaultMods.size(); i++) {
			if (Main.xmlHandler.debugMode.intValue() == 1) {
				System.out.println("checking for mod " + (String)Main.xmlHandler.defaultMods.get(i));
			}
			for (int j = 0; j < items.size(); j++) {
				if (((String)Main.xmlHandler.defaultMods.get(i)).equals(items.get(j))) {
					if (Main.xmlHandler.debugMode.intValue() == 1) {
						System.out.println("Match found! Removing " + (String)items.get(j) + " from the list.");
					}
					items.remove(j);
					break;
				}
			}
		}
		String message2 = "You have been disconnected due to an illegal client.\nIf you have added a mod to your client, please remove this mod before you join the server again.\nIllegal Mods:\n";
		if (items.size() > 0) {
			if (Main.xmlHandler.debugMode.intValue() == 1) {
				System.out.println("There are " + items.size() + " leftover Mods!");
				for (int i = 0; i < items.size(); i++) {
					System.out.println("Mod leftover: " + (String)items.get(i));
				}
				System.out.println("Removing player's connection.");
			}
			for(int j =0; j < items.size(); j++) {
				message2 = message2+items.get(j)+"\n";
			}
			try {
				Thread.sleep(500l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			serverPlayer.connection.disconnect(new TextComponentString(message2));
      
			Main.reportLogger.generateReport(serverPlayer.getName(), items);
		}
		return null;
	}
}
