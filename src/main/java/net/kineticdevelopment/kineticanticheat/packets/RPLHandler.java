package net.kineticdevelopment.kineticanticheat.packets;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class RPLHandler implements IMessageHandler<ResourcePackListMessage, IMessage> {
	public IMessage onMessage(ResourcePackListMessage message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		
		String resources = message.toSend;
		ArrayList<String> items = new ArrayList<String>(Arrays.asList(resources.split(",")));
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).toLowerCase().contains("x-ray") || items.get(i).toLowerCase().contains("xray")) {
				System.out.println("Yeet");
				try {
					Thread.sleep(500l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				serverPlayer.connection.disconnect(new TextComponentString("You have been disconnected for having X-Ray Texture packs in your texture packs folders, please remove them"));
			}
		}
		return null;
	}
}
