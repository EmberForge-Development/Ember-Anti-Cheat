package net.kineticdevelopment.kineticanticheat.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ModListMessage implements IMessage {
	public String toSend;
  
	public ModListMessage() {}
  
	public ModListMessage(String toSend) {
		this.toSend = toSend;
	}
  
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.toSend);
	}
  
	public void fromBytes(ByteBuf buf) {
		this.toSend = ByteBufUtils.readUTF8String(buf);
	}
}

