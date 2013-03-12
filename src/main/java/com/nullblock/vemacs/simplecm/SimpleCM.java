package com.nullblock.vemacs.simplecm;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleCM extends JavaPlugin
  implements Listener
{
  static String pluginName;
  static File pluginFolder;
  static String pluginVersion;
  protected static final Logger log = Logger.getLogger("Minecraft");
  static List<String> permissions = new ArrayList();
  static String template;
  static String timeFormat;
  static Boolean multiPrefix;

  public void onEnable()
  {
    pluginName = getDescription().getName();
    pluginFolder = getDataFolder();
    pluginVersion = getDescription().getVersion();
    Config.firstRun(this);
    ScmCommand.initialise();
    getServer().getPluginManager().registerEvents(this, this);
    getCommand("scm").setExecutor(new ScmCommand(this));
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerChat(AsyncPlayerChatEvent event)
  {
    Player player = event.getPlayer();
    String prefix = Config.getPrefix(player);
    String suffix = Config.getSuffix(player);
    String world = Config.getWorld(player);
    String message = event.getMessage().replaceAll("%", "%%");
    if(player.hasPermission("simplecm.colors"))
    {
    	message = replaceColors(message);
    }
    if (template == null) template = "<[time] [world] [prefix][name][suffix]> ";
    if (timeFormat == null) timeFormat = "[h:mm aa]";
    String formattedName = template.replaceAll("\\[world\\]", world).replaceAll("\\[prefix\\]", prefix).replaceAll("\\[name\\]", player.getDisplayName()).replaceAll("\\[suffix\\]", suffix).replaceAll("(&([A-Fa-f0-9L-Ol-okKrR]))", "§$2");
    if ((timeFormat != null) && (!timeFormat.equalsIgnoreCase("")) && (formattedName.contains("[time]"))) {
      DateFormat dateFormat = new SimpleDateFormat(timeFormat);
      Date date = new Date();
      formattedName = formattedName.replaceAll("\\[time\\]", String.valueOf(dateFormat.format(date)));
    }
    formattedName = formattedName.replaceAll("\\s+", " ");
    event.setFormat(formattedName + message);
  }

  public static void message(String message, CommandSender sender) {
    sender.sendMessage(ChatColor.AQUA + "[" + pluginName + "] " + ChatColor.WHITE + message);
  }

  public static boolean hasPermission(Player player, String permission) {
    if (player.isPermissionSet(permission)) {
      return player.hasPermission(permission);
    }

    return false;
  }
  
  public static String replaceColors(String text) {
	        char[] chrarray = text.toCharArray();
	 
	        for (int index = 0; index < chrarray.length; index ++) {
	            char chr = chrarray[index];
	            if (chr != '&') {
	                continue;
	            }
	 
	            if ((index + 1) == chrarray.length) {
	                break;
	            }
	             char forward = chrarray[index + 1];
	            if ((forward >= '0' && forward <= '9') || (forward >= 'a' && forward <= 'f') || (forward >= 'k' && forward <= 'r')) {
	                chrarray[index] = '\u00A7';
	            }
	        }
	         return new String(chrarray);
  }
  
}