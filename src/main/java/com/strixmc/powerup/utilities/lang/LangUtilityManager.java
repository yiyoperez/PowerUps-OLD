package com.strixmc.powerup.utilities.lang;

import com.google.inject.Inject;
import com.strixmc.powerup.PowerUps;
import com.strixmc.powerup.utilities.Utils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class LangUtilityManager implements LangUtility {

    @Inject
    private PowerUps plugin;

    private List<String> help;
    private String noPermission;
    private String noAvailablePowerUp;
    private String availablePowerUp;
    private String alreadyExist;
    private String alreadyEnabled;
    private String enabled;
    private String alreadyDisabled;
    private String disabled;
    private String itemUpdate;
    private String nameUpdate;
    private String chanceUpdate;
    private String noExist;
    private String created;
    private String createHelp;
    private String deleted;

    @Override
    public void updateMessages() {
        this.noPermission = Utils.color(plugin.getLang().getString("NoPermission"));

        this.help = Utils.color(plugin.getLang().getStringList("Help.Plugin"));
        this.availablePowerUp = Utils.color(plugin.getLang().getString("Available-Powerups"));
        this.noAvailablePowerUp = Utils.color(plugin.getLang().getString("No-Available"));
        this.alreadyExist = Utils.color(plugin.getLang().getString("Already-Exist"));
        this.noExist = Utils.color(plugin.getLang().getString("No-Exist"));
        this.created = Utils.color(plugin.getLang().getString("Created"));
        this.createHelp = Utils.color(plugin.getLang().getString("Help.Create"));
        this.deleted = Utils.color(plugin.getLang().getString("Deleted"));
        this.enabled = Utils.color(plugin.getLang().getString("Enabled"));
        this.alreadyEnabled = Utils.color(plugin.getLang().getString("Already-Enabled"));
        this.disabled = Utils.color(plugin.getLang().getString("Disabled"));
        this.alreadyDisabled = Utils.color(plugin.getLang().getString("Already-Disabled"));
    }

    @Override
    public String getAlreadyEnabled(@NotNull String name) {
        return this.alreadyEnabled.replace("%powerup_id%", name);
    }

    @Override
    public String getEnabled(@NotNull String name) {
        return this.enabled.replace("%powerup_id%", name);
    }

    @Override
    public String getAlreadyDisabled(@NotNull String name) {
        return this.alreadyDisabled.replace("%powerup_id%", name);
    }

    @Override
    public String getDisabled(@NotNull String name) {
        return this.disabled.replace("%powerup_id%", name);
    }

    @Override
    public String getDeleted(@NotNull String name) {
        return this.deleted.replace("%powerup_id%", name);
    }

    @Override
    public String getCreated(@NotNull String name, @NotNull String id) {
        return this.created.replace("%powerup_name%", name).replace("%powerup_id%", id);
    }

    @Override
    public String getAlreadyExist(@NotNull String name) {
        return this.alreadyExist.replace("%powerup_id%", name);
    }

    @Override
    public String getNoExist(@NotNull String name) {
        return this.noExist.replace("%powerup_id%", name);
    }

}
