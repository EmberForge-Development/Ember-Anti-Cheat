package net.kineticdevelopment.kineticanticheat.packets;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class RPLHandler implements IMessageHandler<ResourcePackListMessage, IMessage> {
	public IMessage onMessage(ResourcePackListMessage message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		
		String modList = message.toSend;
		ArrayList<String> items = new ArrayList<String>(Arrays.asList(modList.split(",")));
		String rps = serverPlayer.getName()+"'s Resource Packs: \n";
		for (int i = 0; i < items.size(); i++) {
			rps = rps + items.get(i)+"\n";
		}
		
		System.out.println(rps);
		return null;
	}
}
