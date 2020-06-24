package com.strixmc.powerup.powerup;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class PowerUpBuilder implements PowerUp {

    private String ID;
    private String name;
    private double chance;
    private List<String> hologram;
    private double hologramHeight;
    private List<String> actions;
    private ItemStack item;

    public PowerUpBuilder(String ID, String name, double chance, List<String> hologram, double hologramHeight, List<String> actions) {
        this.ID = ID;
        this.name = name;
        this.chance = chance;
        this.hologram = hologram;
        this.hologramHeight = hologramHeight;
        this.actions = actions;
    }

    @Override
    public boolean setItem(@NotNull String materialName, @NotNull short data) {
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
