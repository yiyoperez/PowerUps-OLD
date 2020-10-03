package com.strixmc.powerup.powerup;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PowerUp {

  String getID();

  String getMaterial();

  void setMaterial(String material);

  short getData();

  void setData(short data);

  String getName();

  void setName(@NotNull String name);

  boolean isEnabled();

  void setEnabled(boolean value);

  double getChance();

  void setChance(double chance);

  List<String> getHologram();

  List<String> getActions();

  ItemStack getItem();

}
