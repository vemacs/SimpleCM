package com.nullblock.vemacs.simplecm;

import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ScmCommand
  implements CommandExecutor
{
  static HashSet<String> permissions = new HashSet();

  static HashSet<String> prefix = new HashSet();
  static HashSet<String> suffix = new HashSet();
  static HashSet<String> group = new HashSet();
  static HashSet<String> user = new HashSet();
  static HashSet<String> help = new HashSet();
  static HashSet<String> colours = new HashSet();
  static HashSet<String> world = new HashSet();
  static HashSet<String> update = new HashSet();
  static HashSet<String> reload = new HashSet();

  public ScmCommand(SimpleCM plugin) {  } 
  public static void initialise() { prefix.add("prefix");
    prefix.add("p");
    suffix.add("suffix");
    suffix.add("s");
    group.add("group");
    group.add("g");
    user.add("user");
    user.add("u");
    help.add("help");
    help.add("?");
    colours.add("colours");
    colours.add("colors");
    colours.add("colour");
    colours.add("color");
    colours.add("c");
    world.add("world");
    world.add("w");
    update.add("update");
    reload.add("reload");

    permissions.add("colors");
    permissions.add("editGroups");
    permissions.add("editIndividual");
    permissions.add("editOwn");
    permissions.add("editWorld");
    permissions.add("update");
    permissions.add("reload");
    permissions.add("*");
  }

  public boolean onCommand(CommandSender sender, Command comannd, String label, String[] args)
  {
    HashSet p = new HashSet();
    if ((sender instanceof ConsoleCommandSender)) {
      p.add("colors");
      p.add("editGroups");
      p.add("editIndividual");
      p.add("editWorld");
      p.add("update");
      p.add("reload");
    } else if ((sender instanceof Player)) {
      for (String s : permissions) {
        if (SimpleCM.hasPermission((Player)sender, "SimpleCM.command." + s)) {
          p.add(s);
        }
      }
    }

    if (args.length == 1)
    {
      if (help.contains(args[0])) {
        showHelp(sender, p);
        return true;
      }

      if (((p.contains("update")) || (p.contains("*"))) && (update.contains(args[0]))) {
        checkForUpdates(sender);
        return true;
      }

      if (((p.contains("reload")) || (p.contains("*"))) && (reload.contains(args[0]))) {
        Config.loadConfig();
        Config.saveConfig();
        sender.sendMessage(ChatColor.AQUA + "[" + SimpleCM.pluginName + "]" + ChatColor.WHITE + " Config reloaded.");
        return true;
      }

      if (((p.contains("colors")) || (p.contains("*"))) && (colours.contains(args[0]))) {
        showColours(sender);
        return true;
      }
      if ((sender instanceof Player))
      {
        if (((p.contains("editOwn")) || (p.contains("*"))) && (prefix.contains(args[0]))) {
          Config.setPrefix("user", sender.getName(), "");
          changeMessage(sender, "Prefix", "remove", "user", sender.getName(), "");
          return true;
        }

        if (((p.contains("editOwn")) || (p.contains("*"))) && (suffix.contains(args[0]))) {
          Config.setSuffix("user", sender.getName(), "");
          changeMessage(sender, "Suffix", "remove", "user", sender.getName(), "");
          return true;
        }
      }

    }

    if (args.length == 2) {
      if ((sender instanceof Player))
      {
        if (((p.contains("editOwn")) || (p.contains("*"))) && (prefix.contains(args[0]))) {
          Config.setPrefix("user", sender.getName(), args[1]);
          changeMessage(sender, "Prefix", "set", "user", sender.getName(), args[1]);
          return true;
        }

        if (((p.contains("editOwn")) || (p.contains("*"))) && (suffix.contains(args[0]))) {
          Config.setPrefix("user", sender.getName(), args[1]);
          changeMessage(sender, "Suffix", "set", "user", sender.getName(), args[1]);
          return true;
        }
      }

      if (((p.contains("editWorld")) || (p.contains("*"))) && (world.contains(args[0]))) {
        Config.setWorld(args[1], "");
        changeMessage(sender, "Nickname", "remove", "world", args[1], "");
        return true;
      }
    }

    if (args.length == 3)
    {
      if (((p.contains("editGroup")) || (p.contains("*"))) && (prefix.contains(args[0])) && (group.contains(args[1]))) {
        Config.setPrefix("group", args[2], "");
        changeMessage(sender, "Prefix", "remove", "group", args[2], "");
        return true;
      }

      if (((p.contains("editGroup")) || (p.contains("*"))) && (suffix.contains(args[0])) && (group.contains(args[1]))) {
        Config.setSuffix("group", args[2], "");
        changeMessage(sender, "Suffix", "remove", "group", args[2], "");
        return true;
      }

      if (((p.contains("editUser")) || (p.contains("*"))) && (prefix.contains(args[0])) && (user.contains(args[1]))) {
        Config.setPrefix("user", args[2], "");
        changeMessage(sender, "Prefix", "remove", "user", args[2], "");
        return true;
      }

      if (((p.contains("editUser")) || (p.contains("*"))) && (suffix.contains(args[0])) && (user.contains(args[1]))) {
        Config.setSuffix("user", args[2], "");
        changeMessage(sender, "Suffix", "remove", "user", args[2], "");
        return true;
      }

      if (((p.contains("editWorld")) || (p.contains("*"))) && (world.contains(args[0]))) {
        Config.setWorld(args[1], args[2]);
        changeMessage(sender, "Nickname", "set", "world", args[1], args[2]);
        return true;
      }
    }

    if (args.length >= 4)
    {
      if (((p.contains("editGroup")) || (p.contains("*"))) && (prefix.contains(args[0])) && (group.contains(args[1]))) {
        String prefix = args[3];
        if (args.length > 4) {
          for (int i = 4; i < args.length; i++) {
            prefix = prefix + " " + args[i];
          }
        }
        Config.setPrefix("group", args[2], prefix);
        changeMessage(sender, "Prefix", "set", "group", args[2], prefix);
        return true;
      }

      if (((p.contains("editGroup")) || (p.contains("*"))) && (suffix.contains(args[0])) && (group.contains(args[1]))) {
        String suffix = args[3];
        if (args.length > 4) {
          for (int i = 4; i < args.length; i++) {
            suffix = suffix + " " + args[i];
          }
        }
        Config.setSuffix("group", args[2], suffix);
        changeMessage(sender, "Suffix", "set", "group", args[2], suffix);
        return true;
      }

      if (((p.contains("editUser")) || (p.contains("*"))) && (prefix.contains(args[0])) && (user.contains(args[1]))) {
        String prefix = args[3];
        if (args.length > 4) {
          for (int i = 4; i < args.length; i++) {
            prefix = prefix + " " + args[i];
          }
        }
        Config.setPrefix("user", args[2], prefix);
        changeMessage(sender, "Prefix", "set", "user", args[2], prefix);
        return true;
      }

      if (((p.contains("editUser")) || (p.contains("*"))) && (suffix.contains(args[0])) && (user.contains(args[1]))) {
        String suffix = args[3];
        if (args.length > 4) {
          for (int i = 4; i < args.length; i++) {
            suffix = suffix + " " + args[i];
          }
        }
        Config.setSuffix("user", args[2], suffix);
        changeMessage(sender, "Suffix", "set", "user", args[2], suffix);
        return true;
      }
    }
    showHelp(sender, p);
    return true;
  }

  private void changeMessage(CommandSender sender, String item, String change, String entityType, String entity, String input) {
    String space = " ";
    sender.sendMessage(ChatColor.AQUA + "[" + SimpleCM.pluginName + "]");
    if (change.equalsIgnoreCase("remove"))
      sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + " > " + ChatColor.WHITE + item + space + "for" + space + entityType + space + ChatColor.AQUA + entity + ChatColor.WHITE + space + ChatColor.RED + "removed.");
    else if (change.equalsIgnoreCase("set"))
      sender.sendMessage((ChatColor.AQUA + "" + ChatColor.BOLD + " > " + ChatColor.WHITE + item + space + "for" + space + entityType + space + ChatColor.AQUA + entity + ChatColor.WHITE + space + ChatColor.GREEN + "set to" + ChatColor.WHITE + ":" + space + input).replaceAll("(&([A-Fa-f0-9L-Ol-okKrR]))", "§$2"));
  }

  private void showColours(CommandSender sender)
  {
    sender.sendMessage(ChatColor.AQUA + "[" + SimpleCM.pluginName + " Colours] ");
    if ((sender instanceof Player)) {
      sender.sendMessage("§0&0 §1&1 §2&2 §3&3 §4&4 §5&5 §6&6 §7&7 §8&8 §9&9 §A&A §B&B §C&C §D&D §E&E §F&F");
      sender.sendMessage("§r§L&L§r §M&M§r §N&N§r §O&O§r §k&K§R (§r&R§r Reset)");
    } else {
      sender.sendMessage("| &0 Black     | &1 Dark Blue | &2 Dark Green | &3 Dark Aqua |");
      sender.sendMessage("| &4 Dark Red  | &5 Purple    | &6 Gold       | &7 Gray      |");
      sender.sendMessage("| &8 Dark Gray | &9 Blue      | &a Green      | &b Aqua      |");
      sender.sendMessage("| &c Red       | &d Pink      | &e Yellow     | &f White     |");
      sender.sendMessage("| &l Bold      | &m Striked   | &n Underlined | &o Italic    |");
      sender.sendMessage("| &k Magic     | &r Reset     |");
    }
  }

  private void showHelp(CommandSender sender, HashSet<String> p) {
    sender.sendMessage(ChatColor.AQUA + "[" + SimpleCM.pluginName + " Commands] ");
    if ((p.contains("colors")) || (p.contains("*"))) sender.sendMessage(commandHelp("colors", ""));
    if ((p.contains("editGroup")) || (p.contains("*"))) sender.sendMessage(commandHelp("prefix/suffix group <group> [prefix/suffix]", ""));
    if ((p.contains("editUser")) || (p.contains("*"))) sender.sendMessage(commandHelp("prefix/suffix user <user> [prefix/suffix]", ""));
    if ((p.contains("editOwn")) || (p.contains("*"))) sender.sendMessage(commandHelp("prefix/suffix [prefix/suffix]", ""));
    if ((p.contains("editWorld")) || (p.contains("*"))) sender.sendMessage(commandHelp("world <world> [nick]", ""));

    if ((p.contains("reload")) || (p.contains("*"))) sender.sendMessage(commandHelp("reload", ""));
    sender.sendMessage(ChatColor.GRAY + "If you can't see the commands, check your permissions!");
    sender.sendMessage(ChatColor.GRAY + "More help: " + ChatColor.UNDERLINE + "http://dev.bukkit.org/server-mods/simple-prefix/");
  }

  private String commandHelp(String command, String function) {
    String message = ChatColor.AQUA + "" + ChatColor.BOLD + ">" + ChatColor.WHITE + "/spr " + command;
    return message;
  }

  private void checkForUpdates(CommandSender sender) {
    String curVer = SimpleCM.pluginVersion;
  }
}