package me.patothebest.guiframework.gui.anvil;

import org.bukkit.entity.Player;

public class AnvilClickEvent  {

    private AnvilSlot slot;
    private String output;
    private String playerName;
    private boolean close;
    private boolean destroy;
    
    public AnvilClickEvent(final AnvilSlot slot, final String output, final Player player) {
        super();
        this.close = false;
        this.destroy = true;
        this.slot = slot;
        this.output = output;
        this.playerName = player.getName();
    }
    
    public String getPlayerName() {
        return this.playerName;
    }

    public AnvilSlot getSlot() {
        return this.slot;
    }
    
    public String getOutput() {
        return this.output;
    }
    
    public boolean getWillClose() {
        return this.close;
    }
    
    public void setWillClose(final boolean close) {
        this.close = close;
    }
    
    public boolean getWillDestroy() {
        return this.destroy;
    }
    
    public void setWillDestroy(final boolean destroy) {
        this.destroy = destroy;
    }
    
    public void destroy() {
        this.output = null;
        this.playerName = null;
        this.slot = null;
    }
}
