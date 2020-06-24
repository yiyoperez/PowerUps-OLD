package com.strixmc.powerup.utilities.lang;

import com.google.inject.Inject;
import com.strixmc.powerup.PowerUps;
import lombok.Getter;

@Getter
public class LangUtilityManager implements LangUtility {

    @Inject
    private PowerUps plugin;

    private String noPermission;
    private String noAvailablePowerUp;

    @Override
    public void updateMessages() {
        this.noPermission = plugin.getLang().getString("NoPermission");

        this.noAvailablePowerUp = plugin.getLang().getString("No-Available");
    }
}
