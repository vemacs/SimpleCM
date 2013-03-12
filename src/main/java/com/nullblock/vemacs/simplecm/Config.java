package com.nullblock.vemacs.simplecm;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Config
{
  private static File pluginFolder = SimpleCM.pluginFolder;
  private static File configFile = new File(pluginFolder, "config.yml");
  public static FileConfiguration config;
  static Plugin plugin;

  public static void firstRun(Plugin p)
  {
    plugin = p;
    config = new YamlConfiguration();
    createFolder();
    createFile();
    loadConfig();
  }

  public static void createFolder()
  {
    if (!pluginFolder.exists())
      try {
        pluginFolder.mkdir();
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  public static void createFile()
  {
    if (!configFile.exists()) {
      try {
        configFile.createNewFile();
      } catch (Exception e) {
        e.printStackTrace();
      }

      addDefaultKeys();
      saveConfig();
    }
  }

  public static void addDefaultKeys()
  {
    SimpleCM.log.info("[Simple Prefix] Generating Config File.");

    config.set("Template.format", "<[time] [world] [prefix][name][suffix]> ");
    config.set("Template.time", "[h:mm aa]");
    config.set("Template.multiPrefix", Boolean.valueOf(false));
    config.set("Worlds.world.nickname", "&e[World]&f");
    config.set("Group.example.prefix", "&a[Example]&f");
    config.set("Group.example.suffix", "");
    config.set("User.Flabaliki.prefix", "&c[Who?]&f");
    config.set("User.Flabaliki.suffix", "");
  }

  public static void loadConfig()
  {
    config = new YamlConfiguration();
    try {
      config.load(configFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    loadkeys();
  }

  public static void saveConfig()
  {
    try {
      config.save(configFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void loadkeys()
  {
    for (String s : config.getConfigurationSection("Group").getKeys(false)) {
      SimpleCM.permissions.add(s);
    }
    SimpleCM.template = config.getString("Template.format");
    SimpleCM.timeFormat = config.getString("Template.time");
    SimpleCM.multiPrefix = Boolean.valueOf(config.getBoolean("Template.multiPrefix"));
  }

  public static String getPrefix(Player player) {
    return getPrefixSuffix(player, "prefix").replaceAll("(&([A-Fa-f0-9L-Ol-okKrR]))", "§$2");
  }

  public static String getSuffix(Player player) {
    return getPrefixSuffix(player, "suffix").replaceAll("(&([A-Fa-f0-9L-Ol-okKrR]))", "§$2");
  }

  public static String getWorld(Player player) {
    String world = "";
    if (config.getString("Worlds." + player.getWorld().getName() + ".nickname") != null) {
      world = config.getString("Worlds." + player.getWorld().getName() + ".nickname").replaceAll("(&([A-Fa-f0-9L-Ol-okKrR]))", "§$2");
    }
    return world;
  }

  private static String getPrefixSuffix(Player player, String type) {
    String prefixSuffix = "";
    for (int i = SimpleCM.permissions.size() - 1; i > -1; i--) {
      if ((SimpleCM.hasPermission(player, "SimpleCM." + (String)SimpleCM.permissions.get(i))) && 
        (config.getString("Group." + (String)SimpleCM.permissions.get(i) + "." + type) != null)) {
        if (SimpleCM.multiPrefix.booleanValue())
          prefixSuffix = prefixSuffix + config.getString(new StringBuilder("Group.").append((String)SimpleCM.permissions.get(i)).append(".").append(type).toString());
        else {
          prefixSuffix = config.getString("Group." + (String)SimpleCM.permissions.get(i) + "." + type);
        }
      }
    }

    player.setMetadata(type, new FixedMetadataValue(plugin, prefixSuffix));

    if ((config.get("User") != null) && (config.getConfigurationSection("User").contains(player.getName()))) {
      String data = config.getString("User." + player.getName() + "." + type);
      if ((data != null) && (!data.equalsIgnoreCase(""))) {
        if (SimpleCM.multiPrefix.booleanValue())
          player.setMetadata(type, new FixedMetadataValue(plugin, prefixSuffix + config.getString(new StringBuilder("User.").append(player.getName()).append(".").append(type).toString())));
        else {
          player.setMetadata(type, new FixedMetadataValue(plugin, config.getString("User." + player.getName() + "." + type)));
        }
      }

    }

    if (player.hasMetadata(type)) {
      return ((MetadataValue)player.getMetadata(type).get(0)).asString();
    }

    return "";
  }

  public static void setPrefix(String type, String body, String prefix) {
    setPrefixSuffix("prefix", type, body, prefix);
  }

  public static void setSuffix(String type, String body, String suffix) {
    setPrefixSuffix("suffix", type, body, suffix);
  }

  public static void setWorld(String world, String input) {
    config.set("Worlds." + world + ".nickname", input);
    saveConfig();
    loadConfig();
  }

  private static void setPrefixSuffix(String type, String bodyType, String body, String input) {
    if (bodyType.equalsIgnoreCase("group"))
      config.set("Group." + body + "." + type, input);
    else if (bodyType.equalsIgnoreCase("user")) {
      config.set("User." + body + "." + type, input);
    }
    saveConfig();
    loadConfig();
  }
}