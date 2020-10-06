package me.patothebest.guiframework.gui.inventory.button;

import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.gui.anvil.AnvilGUI;
import me.patothebest.guiframework.gui.anvil.AnvilSlot;
import me.patothebest.guiframework.gui.inventory.GUIButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class AnvilButton implements GUIButton {

    private final ItemStack item;
    private final Map<AnvilSlot, ItemStack> items;
    private AnvilButtonConfirmAction confirmAction;
    private AnvilButtonCancelAction cancelAction;

    public AnvilButton(ItemStack item) {
        this.item = item;
        this.items = new HashMap<>();
    }

    public AnvilButton(ItemStack item, AnvilButtonConfirmAction confirmAction, AnvilButtonCancelAction cancelAction) {
        this.item = item;
        this.confirmAction = confirmAction;
        this.cancelAction = cancelAction;
        this.items = new HashMap<>();
    }

    @Override
    public void click(ClickType clickType, GUIPage page) {
        AnvilGUI gui = new AnvilGUI(page.getPlugin(), page.getPlayer(), event -> {
            event.setWillDestroy(true);

            if(event.getSlot() == AnvilSlot.OUTPUT){
                confirmAction.onConfirm(event);
            } else {
                cancelAction.onCancel();
            }
        });
        page.destroy();
        page.destroyInternal();
        gui.getItems().putAll(items);
        items.clear();
        gui.open();
    }

    public AnvilButton onConfirm(AnvilButtonConfirmAction confirmAction) {
        this.confirmAction = confirmAction;
        return this;
    }

    public AnvilButton onCancel(AnvilButtonCancelAction cancelAction) {
        this.cancelAction = cancelAction;
        return this;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    public AnvilButton useSlot(final AnvilSlot slot, final ItemStack item) {
        this.items.put(slot, item);
        return this;
    }

    @Override
    public void destroy() {

    }
}
