package me.patothebest.guiframework.gui.inventory.page;

import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import me.patothebest.guiframework.gui.inventory.button.ConfirmationPageButton;
import me.patothebest.guiframework.gui.inventory.button.PlaceHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ConfirmationPage extends GUIPage {

    private ItemStack infoTop;
    private ItemStack infoMiddle;

    public ConfirmationPage(Plugin plugin, Player player, ItemStack infoTop, ItemStack infoMiddle) {
        super(plugin, player, "Confirm?", 54);
        this.infoTop = infoTop;
        this.infoMiddle = infoMiddle;
        build();
    }

    public void buildPage() {
        ItemStack confirm = new ItemStackBuilder().material(Material.EMERALD_BLOCK).name(ChatColor.GREEN + "CONFIRM");
        ItemStack cancel = new ItemStackBuilder().material(Material.REDSTONE_BLOCK).name(ChatColor.RED + "CANCEL");

        addButton(new PlaceHolder(infoTop), 4);
        addButton(new PlaceHolder(infoMiddle), 22);

        addButton(new ConfirmationPageButton(true, confirm), 27);
        addButton(new ConfirmationPageButton(true, confirm), 28);
        addButton(new ConfirmationPageButton(true, confirm), 29);

        addButton(new ConfirmationPageButton(true, confirm), 36);
        addButton(new ConfirmationPageButton(true, confirm), 37);
        addButton(new ConfirmationPageButton(true, confirm), 38);

        addButton(new ConfirmationPageButton(true, confirm), 45);
        addButton(new ConfirmationPageButton(true, confirm), 46);
        addButton(new ConfirmationPageButton(true, confirm), 47);

        addButton(new ConfirmationPageButton(false, cancel), 33);
        addButton(new ConfirmationPageButton(false, cancel), 34);
        addButton(new ConfirmationPageButton(false, cancel), 35);

        addButton(new ConfirmationPageButton(false, cancel), 42);
        addButton(new ConfirmationPageButton(false, cancel), 43);
        addButton(new ConfirmationPageButton(false, cancel), 44);

        addButton(new ConfirmationPageButton(false, cancel), 51);
        addButton(new ConfirmationPageButton(false, cancel), 52);
        addButton(new ConfirmationPageButton(false, cancel), 53);
    }

    public void destroy() {
        infoMiddle = null;
        infoTop = null;
    }

    public abstract void onConfirm();

    public abstract void onCancel();

}
