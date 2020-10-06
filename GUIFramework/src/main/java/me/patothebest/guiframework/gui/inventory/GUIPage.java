package me.patothebest.guiframework.gui.inventory;

import me.patothebest.guiframework.gui.inventory.button.NullButton;
import me.patothebest.guiframework.gui.inventory.page.FailedPage;
import me.patothebest.guiframework.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public abstract class GUIPage implements Listener {

    protected final HashMap<Integer, GUIButton> buttons;
    protected final Plugin plugin;
    protected final Player player;
    protected final int size;
    protected boolean overrideClose = false;
    protected boolean blockInventoryMovement = true;

    private String name;
    private Inventory menu;

    public GUIPage(Plugin plugin, Player player, String rawName, int size, boolean override) {
        this(plugin, player, rawName, size);
        this.overrideClose = true;
    }

    public GUIPage(Plugin plugin, Player player, String rawName, int size) {
        this.player = player;
        Utils.invokeStaticMethod(Utils.getCBSClass("event.CraftEventFactory"), "handleInventoryCloseEvent", new Class[] {Utils.getNMSClass("EntityHuman")}, Utils.invokeMethod(player, "getHandle", new Class[] {}, null));

        this.plugin = plugin;
        this.size = size;
        this.name = (rawName.length() > 32 ? rawName.substring(0, 32) : rawName);
        this.buttons = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.menu = Bukkit.getServer().createInventory(null, size, name);

        Utils.setFieldValue(Utils.getNMSClass("EntityHuman"), "activeContainer", Utils.invokeMethod(player, "getHandle", new Class[] {}, null), Utils.getFieldValue(Utils.getNMSClass("EntityHuman"), "defaultContainer", Utils.invokeMethod(player, "getHandle", new Class[] {}, null)));
        this.player.openInventory(menu);
    }

    public void build() {
        if(!getPlayer().isOnline()) {
            destroy();
        }

        try {
            buildPage();
            getPlayer().updateInventory();
        } catch (Exception e) {
            e.printStackTrace();
            new FailedPage(plugin, getPlayer(), "Error building GUI");
        }
    }

    public abstract void buildPage();

    public Plugin getPlugin() {
        return plugin;
    }

    public Player getPlayer() {
        return player;
    }

    public void addButton(GUIButton button, int slot) {
        if(slot >= size) {
            return;
        }

        if(!(button instanceof NullButton)) {
            menu.setItem(slot, button.getItem());
        }

        buttons.put(slot, button);
    }

    public void addButton(GUIButton button) {
        int slot = 0;
        while(!isFree(slot) && slot < size) {
            slot++;
        }

        if(slot > size || !isFree(slot)) {
            System.err.println("Could not find empty slot");
            System.err.println("Button= " + button.toString());
            System.err.println("Slot= " + slot);
            System.err.println("Menu= " + toString());
            return;
        }

        if(!(button instanceof NullButton)) {
            menu.setItem(slot, button.getItem());
        }

        buttons.put(slot, button);
    }

    public void removeButton(int slot) {
        menu.setItem(slot, null);

        if (buttons.get(slot) != null) {
            buttons.get(slot).destroy();
        }

        buttons.remove(slot);
    }

    public void removeAll() {
        for (int i = 0; i <= size - 1; i++) {
            removeButton(i);
        }

        buttons.clear();
    }

    public void refresh() {
        removeAll();
        build();
    }

    public void setTitle(String title) {
        removeAll();
        name = (title.length() > 32 ? title.substring(0, 32) : title);
        this.menu = Bukkit.getServer().createInventory(null, size, name);
        Utils.setFieldValue(Utils.getNMSClass("EntityHuman"), "activeContainer", Utils.invokeMethod(player, "getHandle", new Class[] {}, null), Utils.getFieldValue(Utils.getNMSClass("EntityHuman"), "defaultContainer", Utils.invokeMethod(player, "getHandle", new Class[] {}, null)));
        player.openInventory(menu);
        build();
    }

    public boolean isFree(int slot) {
        return !buttons.keySet().contains(slot);
    }

    public void onInventoryCloseOverride() {

    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (overrideClose) {
            onInventoryCloseOverride();
            return;
        }

        Player player = (Player) event.getPlayer();

        if (!this.player.getOpenInventory().getTitle().equalsIgnoreCase(name)) {
            return;
        }

        if (this.player.getName().equalsIgnoreCase(player.getName())) {
            destroy();
            destroyInternal();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.player.getName().equalsIgnoreCase(player.getName())) {
            return;
        }

        if (!this.player.getOpenInventory().getTitle().equalsIgnoreCase(name)) {
            return;
        }

        event.setCancelled(blockInventoryMovement);

        if (!buttons.containsKey(event.getRawSlot())) {
            return;
        }

        event.setCancelled(true);

        buttons.get(event.getRawSlot()).click(event.getClick(), this);
    }

    public void destroy() {};

    public void destroyInternal() {
        HandlerList.unregisterAll(this);
        this.buttons.values().forEach(GUIButton::destroy);
        this.buttons.clear();
    }
}