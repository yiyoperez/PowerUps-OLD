package com.strixmc.powerup.utilities.lang;

import org.jetbrains.annotations.NotNull;

public interface LangUtility {

    String getNoPermission();


    void updateMessages();

    String getNoAvailablePowerUp();

    String getAvailablePowerUp();

    String getAlreadyExist(@NotNull String name);

    String getNoExist(@NotNull String name);

    String getAlreadyEnabled(@NotNull String name);

    String getEnabled(@NotNull String name);

    String getAlreadyDisabled(@NotNull String name);

    String getDisabled(@NotNull String name);

    String getCreated(@NotNull String name, @NotNull String id);

    String getCreateHelp();

    String getDeleted(@NotNull String name);
}
