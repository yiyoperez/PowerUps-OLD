package com.strixmc.powerup.loaders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.powerup.utilities.lang.LangUtility;
import com.strixmc.universal.loader.LoaderManager;

@Singleton
public class LangUtilityLoader implements LoaderManager {

  @Inject private LangUtility langUtility;

  @Override
  public void load() {
    langUtility.createLang();
    langUtility.load();
  }
}
