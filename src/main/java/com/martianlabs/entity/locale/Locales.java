package com.martianlabs.entity.locale;

import com.martianlabs.config.annotation.ConfigName;
import com.martianlabs.entity.BetterEntities;
import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;

import java.io.File;

@Getter
public enum Locales {
    RU_RU, EN_US, ES_ES, FR_FR, DE_DE, ZH_CN;

    private final Locale config;

    Locales() {
        this.config = BetterEntities.getRegistry().register(generateClass(this), new File(BetterEntities.getInstance().getDataFolder(), "locale"));
    }

    private static Class<? extends Locale> generateClass(Locales locale) {
        return new ByteBuddy()
                .makeInterface(Locale.class)
                .annotateType(
                        AnnotationDescription.Builder
                                .ofType(ConfigName.class)
                                .define("value", locale.name().toLowerCase())
                                .build()
                )
                .make()
                .load(Locale.class.getClassLoader())
                .getLoaded();
    }
}
