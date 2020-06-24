package com.strixmc.powerup.utilities.lang;

import com.google.inject.Inject;
import com.strixmc.powerup.PowerUps;
import lombok.Getter;

@Getter
public class LangUtilityManager implements LangUtility {

    @Inject
    private PowerUps plugin;

    private String noPermission;

    @Override
    public void updateMessages() {
        this.noPermission = plugin.getLang().getString("NoPermission");
    }
}
