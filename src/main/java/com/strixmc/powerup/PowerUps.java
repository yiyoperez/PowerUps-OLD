package com.strixmc.powerup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.strixmc.powerup.commands.PowerUpCommand;
import com.strixmc.powerup.guice.SimpleBinderModule;
import com.strixmc.powerup.powerup.manager.PowerUpManager;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.powerup.utilities.lang.LangUtility;
import lombok.Getter;
import me.fixeddev.ebcm.*;
import me.fixeddev.ebcm.bukkit.BukkitAuthorizer;
import me.fixeddev.ebcm.bukkit.BukkitCommandManager;
import me.fixeddev.ebcm.bukkit.BukkitMessager;
import me.fixeddev.ebcm.bukkit.parameter.provider.BukkitModule;
import me.fixeddev.ebcm.parameter.provider.ParameterProviderRegistry;
import me.fixeddev.ebcm.parametric.ParametricCommandBuilder;
import me.fixeddev.ebcm.parametric.ReflectionParametricCommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowerUps extends JavaPlugin {

    @Getter
    private FileConfiguration lang;

    @Inject
    private PowerUpCommand powerUpCommand;
    @Inject
    private LangUtility langUtility;
    @Inject
    private PowerUpManager powerUpManager;

    @Override
    public void onEnable() {

        createConfig();
        createLang();

        initInstances();

        registerListeners();
        registerCommands();

    }


    @Override
    public void onDisable() {
        powerUpManager.savePowerUps();
    }


    private void initInstances() {

        if (!getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("This plugins does need HolographicDisplays as dependency to work.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        SimpleBinderModule module = new SimpleBinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        powerUpManager.loadPowerUps();
        langUtility.updateMessages();

    }

    private void registerListeners() {

        PluginManager pm = getServer().getPluginManager();

    }

    private void registerCommands() {

        ParametricCommandBuilder builder = new ReflectionParametricCommandBuilder();

        Authorizer authorizer = new BukkitAuthorizer();
        ParameterProviderRegistry providerRegistry = ParameterProviderRegistry.createRegistry();
        Messager messager = new BukkitMessager();
        CommandManager commandManager = new SimpleCommandManager(authorizer, messager, providerRegistry);
        providerRegistry.installModule(new BukkitModule());

        BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(commandManager, this.getName());

        List<Command> commands = new ArrayList<>();
        commands.addAll(builder.fromClass(powerUpCommand));

        bukkitCommandManager.registerCommands(commands);

    }

    private void createConfig() {

        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile, Arrays.asList("PowerUps"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();

    }

    private void createLang() {
        File langFile = new File(getDataFolder(), "lang.yml");
        this.lang = YamlConfiguration.loadConfiguration(langFile);

        if (!langFile.exists()) {
            this.saveResource("lang.yml", false);
        }

        try {
            ConfigUpdater.update(this, "lang.yml", langFile, Arrays.asList("none"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            lang.load(langFile);
        } catch (IOException var2) {
            Bukkit.getLogger().warning("Could not save config to " + lang.getName());
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
