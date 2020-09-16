package com.strixmc.powerup.powerup;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PowerUpBuilder implements PowerUp {

    @Getter private String ID;
    @Getter @Setter private String name;
    @Getter @Setter private boolean enabled;
    @Getter @Setter private double chance;
    @Getter private List<String> hologram;
    @Getter private List<String> actions;
    @Getter @Setter private ItemStack item;

    public PowerUpBuilder(String ID, String name, double chance, List<String> hologram, List<String> actions) {
        this.ID = ID;
        this.name = name;
        this.chance = chance;
        this.hologram = hologram;
        this.actions = actions;
        this.enabled = false;
    }

    @Override
    public boolean setItem(@NotNull String materialName, short data) {
        try {
            this.item = new ItemStack(Material.matchMaterial(materialName), 1, data);
            return true;
        } catch (NullPointerException e) {
            for (int i = 0; i < 3; i++) {
                Bukkit.getLogger().warning(ID + " powerup have a wrong named material (\"" + materialName + "\")");
            }
            this.item = new ItemStack(Material.COAL_BLOCK);
            return false;
        }
    }

}
