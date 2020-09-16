package com.strixmc.powerup.utilities.lang;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.powerup.utilities.Utils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Singleton
public class LangUtilityManager implements LangUtility {

    @Inject private PowerUpsPlugin main;
    @Getter private FileConfiguration lang;

    @Getter private List<String> help;
    @Getter private String noPermission;
    @Getter private String noAvailablePowerUp;
    @Getter private String availablePowerUp;
    @Getter private String alreadyExist;
    @Getter private String alreadyEnabled;
    @Getter private String enabled;
    @Getter private String alreadyDisabled;
    @Getter private String disabled;
    @Getter private String itemUpdate;
    @Getter private String nameUpdate;
    @Getter private String chanceUpdate;
    @Getter private String noExist;
    @Getter private String created;
    @Getter private String createHelp;
    @Getter private String deleted;

    @Override
    public void load() {

        this.noPermission = Utils.color(lang.getString("NoPermission"));

        this.help = Utils.color(lang.getStringList("Help.Plugin"));
        this.availablePowerUp = Utils.color(lang.getString("Available-Powerups"));
        this.noAvailablePowerUp = Utils.color(lang.getString("No-Available"));
        this.alreadyExist = Utils.color(lang.getString("Already-Exist"));
        this.noExist = Utils.color(lang.getString("No-Exist"));
        this.created = Utils.color(lang.getString("Created"));
        this.createHelp = Utils.color(lang.getString("Help.Create"));
        this.deleted = Utils.color(lang.getString("Deleted"));
        this.enabled = Utils.color(lang.getString("Enabled"));
        this.alreadyEnabled = Utils.color(lang.getString("Already-Enabled"));
        this.disabled = Utils.color(lang.getString("Disabled"));
        this.alreadyDisabled = Utils.color(lang.getString("Already-Disabled"));
    }

    @SneakyThrows @Override
    public void createLang() {

        File langFile = new File(main.getDataFolder(), "lang.yml");
        this.lang = YamlConfiguration.loadConfiguration(langFile);

        if (!langFile.exists()) {
            main.saveResource("lang.yml", false);
        }

        ConfigUpdater.update(main, "lang.yml", langFile, Collections.singletonList("none"));

        lang.load(langFile);

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
