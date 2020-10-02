package com.strixmc.powerup.utilities;

import com.google.inject.Singleton;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class Utils {

  public String translate(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  public List<String> translate(List<String> lines) {
    List<String> toReturn = new ArrayList<>();

    for (String line : lines) {
      toReturn.add(translate(line));
    }

    return toReturn;
  }

  public String stripColor(String text) {
    return ChatColor.stripColor(translate(text));
  }
}
