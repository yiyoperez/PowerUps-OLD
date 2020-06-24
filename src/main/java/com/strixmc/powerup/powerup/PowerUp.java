package com.strixmc.powerup.powerup;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PowerUp {

    String getID();

    String getName();

    void setName(@NotNull String name);

    double getChance();

    void setChance(@NotNull double chance);

    List<String> getHologram();

    double getHologramHeight();

    void setHologramHeight(@NotNull double hologramHeight);

    List<String> getActions();

    ItemStack getItem();

    boolean setItem(@NotNull String material, @NotNull short data);

}
