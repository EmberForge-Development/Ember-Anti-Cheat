package xyz.sleepingtea.modchecker;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class MyMessageHandler
  implements IMessageHandler<MyMessage, IMessage>
{
  public IMessage onMessage(MyMessage message, MessageContext ctx)
  {
    EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
    
    String modList = message.toSend;
    if (ModChecker.xmlHandler.debugMode.intValue() == 1) {
      System.out.println("Player joined: " + serverPlayer.getName() + " - Comparing mods...");
    }
    ArrayList<String> items = new ArrayList(Arrays.asList(modList.split(",")));
    for (int i = 0; i < ModChecker.xmlHandler.defaultMods.size(); i++)
    {
      if (ModChecker.xmlHandler.debugMode.intValue() == 1) {
        System.out.println("checking for mod " + (String)ModChecker.xmlHandler.defaultMods.get(i));
      }
      for (int j = 0; j < items.size(); j++) {
        if (((String)ModChecker.xmlHandler.defaultMods.get(i)).equals(items.get(j)))
        {
          if (ModChecker.xmlHandler.debugMode.intValue() == 1) {
            System.out.println("Match found! Removing " + (String)items.get(j) + " from the list.");
          }
          items.remove(j);
          break;
        }
      }
    }
    if (items.size() > 0)
    {
      if (ModChecker.xmlHandler.debugMode.intValue() == 1)
      {
        System.out.println("There are " + items.size() + " leftover Mods!");
        for (int i = 0; i < items.size(); i++) {
          System.out.println("Mod leftover: " + (String)items.get(i));
        }
        System.out.println("Removing player's connection.");
      }
      serverPlayer.connection.disconnect(new TextComponentTranslation ("You have been disconnected due to an illegal client. \nIf you have added a mod to your client, please remove this mod before you join the server again.",  new Object[0]));
      
      ModChecker.reportLogger.generateReport(serverPlayer.getName(), items);
    }
    return null;
  }
}
