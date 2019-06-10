package xyz.sleepingtea.modchecker;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MyMessage
  implements IMessage
{
  public String toSend;
  
  public MyMessage() {}
  
  public MyMessage(String toSend)
  {
    this.toSend = toSend;
  }
  
  public void toBytes(ByteBuf buf)
  {
    ByteBufUtils.writeUTF8String(buf, this.toSend);
  }
  
  public void fromBytes(ByteBuf buf)
  {
    this.toSend = ByteBufUtils.readUTF8String(buf);
  }
}

