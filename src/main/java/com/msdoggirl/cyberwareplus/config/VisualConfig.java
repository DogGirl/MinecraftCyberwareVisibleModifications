package com.msdoggirl.cyberwareplus.config;

import com.msdoggirl.cyberwareplus.config.CyberwarePlusConfig;

public class VisualConfig {


    public static boolean anyVisualsEnabled() {
        return CyberwarePlusConfig.CLIENT.enableAllModifications.get();
    }

    public static boolean showCyberEye() {
        return anyVisualsEnabled() && CyberwarePlusConfig.CLIENT.showCyberEye.get();
    }

    public static boolean showCyberHeart() {
        return anyVisualsEnabled() && CyberwarePlusConfig.CLIENT.showCyberHeart.get();
    }

    public static boolean showCyberArms() {
        return anyVisualsEnabled() && CyberwarePlusConfig.CLIENT.showCyberArms.get();
    }

    public static String path(int height, int count) {
        return "textures/skins/default_" + height + "_" + count + ".png";
    }

    public static String glowPath(int height, int count) {
        return "textures/skins/default_glow_" + height + "_" + count + ".png";
    }


    public static boolean showCyberLegs() {
        return anyVisualsEnabled() && CyberwarePlusConfig.CLIENT.showCyberLegs.get();
    }


    public static boolean glowEnabled() {
        return anyVisualsEnabled() && CyberwarePlusConfig.CLIENT.enableGlow.get();
    }
}