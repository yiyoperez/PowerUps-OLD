package me.patothebest.guiframework.itemstack;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Field;
import java.util.*;

public class ItemStackBuilder extends ItemStack {

    public ItemStackBuilder() {
        this(Material.QUARTZ);
    }

    public ItemStackBuilder(Material material) {
        super(material);
        lore(new ArrayList<>());
    }

    public ItemStackBuilder(ItemStack itemStack) {
        setData(itemStack.getData());
        data(itemStack.getDurability());
        setAmount(itemStack.getAmount());
        setType(itemStack.getType());
        setItemMeta(itemStack.getItemMeta());
    }

    public ItemStackBuilder(ItemStack itemStack, Object n) {
        setData(itemStack.getData());
        data(itemStack.getDurability());
        setAmount(itemStack.getAmount());
        setType(itemStack.getType());
    }

    public ItemStackBuilder material(Material material) {
        setType(material);
        return this;
    }

    public ItemStackBuilder addAmount(int change) {
        setAmount(getAmount() + change);
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        setAmount(amount);
        return this;
    }

    public ItemStackBuilder data(short data) {
        setDurability(data);
        return this;
    }

    public ItemStackBuilder data(MaterialData data) {
        setData(data);
        return this;
    }

    public ItemStackBuilder enchantments(HashMap<Enchantment, Integer> enchantments) {
        getEnchantments().keySet().forEach(this::removeEnchantment);
        addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder name(String name) {
        if (name == null) {
            return this;
        }

        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(name.equals("") ? " " : format(name));
        setItemMeta(itemMeta);
        return this;
    }

    @Override
    public void setData(MaterialData data) {
        Field dataField;
        try {
            dataField = ItemStack.class.getDeclaredField("data");
            dataField.setAccessible(true);
            dataField.set(this, data);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ItemStackBuilder enchantedBook(Enchantment ench, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)getItemMeta();
        meta.addEnchant(ench, level, true);
        return this;
    }

    public ItemStackBuilder color(int red, int green, int blue) {
        return color(Color.fromRGB(red, green, blue));
    }

    public ItemStackBuilder color(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getItemMeta();
        leatherArmorMeta.setColor(color);
        setItemMeta(leatherArmorMeta);
        return this;
    }

    public ItemStackBuilder addBlankLineLore() {
        addLore(" ");
        return this;
    }

    public ItemStackBuilder addLineLore() {
        return addLineLore(20);
    }

    public ItemStackBuilder addLineLore(int length) {
        addLore("&8&m&l" + Strings.repeat("-", length));
        return this;
    }

    public ItemStackBuilder addLore(String... lore) {
        if (lore == null) {
            return this;
        }
        ItemMeta itemMeta = getItemMeta();
        List<String> original = itemMeta.getLore();
        if (original == null)
            original = new ArrayList<>();
        Collections.addAll(original, format(lore));
        itemMeta.setLore(original);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder addLore(List<String> lore) {
        if (lore == null) {
            return this;
        }
        ItemMeta itemMeta = getItemMeta();
        List<String> original = itemMeta.getLore();
        if (original == null)
            original = new ArrayList<>();
        original.addAll(format(lore));
        itemMeta.setLore(original);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        if (lore == null) {
            return this;
        }
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(format(Lists.newArrayList(lore)));
        setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        if (lore == null) {
            return this;
        }
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(format(lore));
        setItemMeta(itemMeta);
        return this;
    }

    public String format(String string) {
        return (string == null ? null : string.replace("&", "§"));
    }

    public String[] format(String[] strings) {
        return format(Arrays.asList(strings)).toArray(new String[strings.length]);
    }

    public List<String> format(List<String> strings) {
        List<String> collection = new ArrayList<>();
        for (String string : strings) {
            if (string == null || string.isEmpty()) {
                continue;
            }
            collection.add(format(string));
        }
        return collection;
    }
}