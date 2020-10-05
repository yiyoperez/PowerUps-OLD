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

  @Getter private String noPermission;
  @Getter private List<String> help;

  @Getter private String createUsage;
  @Getter private String deleteUsage;
  @Getter private String enableUsage;
  @Getter private String disableUsage;

  @Getter private String noAvailablePowerUp;
  @Getter private String availablePowerUp;
  @Getter private String alreadyExist;
  @Getter private String alreadyEnabled;
  @Getter private String enabled;
  @Getter private String alreadyDisabled;
  @Getter private String disabled;
  @Getter private String noExist;
  @Getter private String created;
  @Getter private String deleted;

  @Inject private Utils utils;

  @Override
  public void load() {
    // COMMON
    this.noPermission = utils.translate(lang.getString("NO_PERMISSION"));

    // HELP MESSAGES
    this.help = utils.translate(lang.getStringList("HELP.MAIN"));

    // USAGES
    this.createUsage = utils.translate(lang.getString("HELP.USAGES.CREATE"));
    this.deleteUsage = utils.translate(lang.getString("HELP.USAGES.DELETE"));
    this.enableUsage = utils.translate(lang.getString("HELP.USAGES.ENABLE"));
    this.disableUsage = utils.translate(lang.getString("HELP.USAGES.DISABLE"));

    // OTHER MESSAGES
    this.created = utils.translate(lang.getString("CREATED"));
    this.deleted = utils.translate(lang.getString("DELETED"));
    this.enabled = utils.translate(lang.getString("ENABLED"));
    this.disabled = utils.translate(lang.getString("DISABLED"));

    this.noAvailablePowerUp = utils.translate(lang.getString("NO_AVAILABLE"));
    this.availablePowerUp = utils.translate(lang.getString("AVAILABLE_LIST"));

    this.alreadyExist = utils.translate(lang.getString("ALREADY_EXIST"));
    this.noExist = utils.translate(lang.getString("NO_EXIST"));
    this.alreadyEnabled = utils.translate(lang.getString("ALREADY_ENABLED"));
    this.alreadyDisabled = utils.translate(lang.getString("ALREADY_DISABLED"));
  }

  @SneakyThrows
  @Override
  public void createLang() {
    final File langFile = new File(main.getDataFolder().getAbsolutePath(), "lang.yml");
    this.lang = YamlConfiguration.loadConfiguration(langFile);

    if (!langFile.exists()) {
      main.saveResource("lang.yml", false);
    }
    ConfigUpdater.update(main, "lang.yml", langFile, Collections.singletonList("none"));
    lang.load(langFile);
  }

  @Override
  public String getAlreadyEnabled(@NotNull String name) {
    return this.alreadyEnabled.replace("$powerup_id", name);
  }

  @Override
  public String getEnabled(@NotNull String name) {
    return this.enabled.replace("$powerup_id", name);
  }

  @Override
  public String getAlreadyDisabled(@NotNull String name) {
    return this.alreadyDisabled.replace("$powerup_id", name);
  }

  @Override
  public String getDisabled(@NotNull String name) {
    return this.disabled.replace("$powerup_id", name);
  }

  @Override
  public String getDeleted(@NotNull String name) {
    return this.deleted.replace("$powerup_id", name);
  }

  @Override
  public String getCreated(@NotNull String name, @NotNull String id) {
    return this.created.replace("$powerup_name", name).replace("$powerup_id", id);
  }

  @Override
  public String getAlreadyExist(@NotNull String name) {
    return this.alreadyExist.replace("$powerup_id", name);
  }

  @Override
  public String getNoExist(@NotNull String name) {
    return this.noExist.replace("$powerup_id", name);
  }

}
