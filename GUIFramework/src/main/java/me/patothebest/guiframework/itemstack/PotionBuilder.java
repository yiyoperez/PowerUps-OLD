package me.patothebest.guiframework.itemstack;

import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@SuppressWarnings("deprecation")
public class PotionBuilder extends Potion {

    public PotionBuilder() {
        super(PotionType.SPEED);
    }

    public PotionBuilder setEffectType(PotionType type) {
        setType(type);
        return this;
    }

    public PotionBuilder setSplashPotion(boolean splash) {
        setSplash(splash);
        return this;
    }

    public PotionBuilder setPotionLevel(int level) {
        setLevel(level);
        return this;
    }

    public PotionBuilder setExtendedDuration(boolean isExtended) {
        setHasExtendedDuration(isExtended);
        return this;
    }

}
