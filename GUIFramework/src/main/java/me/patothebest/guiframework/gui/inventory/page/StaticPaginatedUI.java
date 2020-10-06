package me.patothebest.guiframework.gui.inventory.page;

import com.google.common.collect.Iterators;
import me.patothebest.guiframework.gui.inventory.GUIButton;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public abstract class StaticPaginatedUI<T> extends GUIMultiPage {

    private final Supplier<Collection<? extends T>> listProvider;

    protected StaticPaginatedUI(Plugin plugin, Player player, String rawName, Supplier<Collection<? extends T>> listProvider) {
        super(plugin, player, rawName, 54);
        this.listProvider = listProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void buildContent() {
        int slot = 0;
        Collection<? extends T> results = listProvider.get();
        final int start = pageSize * currentPage;
        final int end = Math.min(pageSize * (currentPage + 1), results.size());

        if (results instanceof List) {
            List<? extends T> list = (List<? extends T>) results;
            for (int index = start; index < end; index++) {
                addButton(createButton(list.get(index)), slot++);
            }
        } else {
            final Iterator<? extends T> iterator = results.iterator();
            for (int index = Iterators.advance(iterator, start); index < end; index++) {
                addButton(createButton(iterator.next()), slot++);
            }
        }

        buildFooter();
    }

    protected abstract GUIButton createButton(T item);

    protected void buildFooter() {};

    @Override
    protected int getListCount() {
        return listProvider.get().size();
    }
}
