package me.patothebest.guiframework.gui.anvil;

public enum AnvilSlot {

    INPUT_LEFT("INPUT_LEFT", 0),
    INPUT_RIGHT("INPUT_RIGHT", 1),
    OUTPUT("OUTPUT", 2);
    
    private final int slot;
    
    AnvilSlot(String s, int slot) {
        this.slot = slot;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public static AnvilSlot bySlot(int slot) {
        AnvilSlot[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final AnvilSlot anvilSlot = values[i];
            if (anvilSlot.getSlot() == slot) {
                return anvilSlot;
            }
        }
        return null;
    }
}
