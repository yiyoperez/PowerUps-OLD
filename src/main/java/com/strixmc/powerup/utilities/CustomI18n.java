package com.strixmc.powerup.utilities;

import com.google.inject.Singleton;
import me.fixeddev.ebcm.NamespaceAccesor;
import me.fixeddev.ebcm.i18n.I18n;
import me.fixeddev.ebcm.i18n.Message;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class CustomI18n implements I18n {

    private final Map<String, String> messageMap;

    public CustomI18n() {
        this.messageMap = new HashMap<>();

        this.messageMap.put(Message.INVALID_SUBCOMMAND.getId(), "Invalid sub-command, valid values are: %s");
        this.messageMap.put(Message.MISSING_ARGUMENT.getId(), "Missing arguments for required part %s minimum arguments required are: %s");
        this.messageMap.put(Message.MISSING_SUBCOMMAND.getId(), "Missing argument for required part %s, available values are: %s");
    }

    @Override
    public String getMessage(String messageId, NamespaceAccesor namespaceAccesor) {

        if (messageId.equals(Message.COMMAND_NO_PERMISSIONS.getId())) {
            return "You can't perform that action.";
        }
        if (messageId.startsWith(Message.COMMAND_USAGE.getId())) {
            return "Command usage is: /%s";
        }

        return this.messageMap.get(messageId);
    }
}
