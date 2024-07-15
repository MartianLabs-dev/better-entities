package com.martianlabs.entity.locale;

public interface Locale {
    default String getRespawnHologram() {
        return "Hello, %player%!\n%boss% will spawn in\n%time";
    }

    default String getBossKill() {
        return "%boss% was killed!\nTop 3 damagers:\n%damager%\n%damager%\n%damager%";
    }
}
