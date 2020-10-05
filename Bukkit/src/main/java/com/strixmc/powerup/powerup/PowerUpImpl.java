package com.strixmc.powerup.powerup;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PowerUpImpl implements PowerUp {

  @Getter private String ID;
  @Getter @Setter private String name;
  @Getter @Setter private String material;
  @Getter @Setter private short data;
  @Getter @Setter private boolean enabled;
  @Getter @Setter private int chance;
  @Getter private List<String> hologram;
  @Getter private List<String> actions;

  public PowerUpImpl(String ID, String name, int chance, List<String> hologram, List<String> actions, boolean enabled, String material, short data) {
    this.ID = ID;
    this.name = name;
    this.chance = chance;
    this.hologram = hologram;
    this.actions = actions;
    this.enabled = enabled;
    this.material = material;
    this.data = data;
  }

  public PowerUpImpl(String ID, String name, String material, short data, int chance, List<String> hologram, List<String> actions) {
    this(ID, name, chance, hologram, actions, false, material, data);
  }

  public PowerUpImpl(String ID, String name) {
    this(ID, name, 100, Arrays.asList("&eDefault PowerUp", "&eHologram text."), Arrays.asList("[MESSAGE] Hey, this action works!", "[SOUND] ARROW_HIT;1.0;1.0"), false, "STONE", (short) 0);
  }

  @Override
  public ItemStack getItem() {
    try {
      return new ItemStack(Material.matchMaterial(material), 1, data);
    } catch (NullPointerException e) {
      for (int i = 0; i < 3; i++) {
        Bukkit.getLogger().warning(ID + " powerup have a wrong named material (\"" + ID + "\")");
      }
      return new ItemStack(Material.COAL_BLOCK);
    }
  }

}
