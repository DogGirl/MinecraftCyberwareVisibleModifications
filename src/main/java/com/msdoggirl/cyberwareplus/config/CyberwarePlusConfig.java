package com.msdoggirl.cyberwareplus.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CyberwarePlusConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        CLIENT = new Client(builder);

        CLIENT_SPEC = builder.build();
    }

    public static class Client {

        // General category


        public final ForgeConfigSpec.BooleanValue enableAllModifications;

        // Per-modification toggles
        public final ForgeConfigSpec.BooleanValue showCyberEye;
        public final ForgeConfigSpec.BooleanValue showCyberHeart;
        public final ForgeConfigSpec.BooleanValue showCyberArms;
        public final ForgeConfigSpec.BooleanValue showCyberLegs;
        public final ForgeConfigSpec.ConfigValue<Integer> eyeCount;
        public final ForgeConfigSpec.ConfigValue<Integer> eyeHeight;
        // Glow toggle (affects only client-side glow layer)
        public final ForgeConfigSpec.BooleanValue enableGlow;

        Client(ForgeConfigSpec.Builder builder) {


            builder.comment("Cyberware+ Visual Settings (client-side only)").push("visuals");

            enableAllModifications = builder
                    .comment("If false, overrides all other settings and hides ALL cyberware visuals.")
                    .define("enableAllModifications", true);


            showCyberEye = builder
                    .comment("Show cyber eye overlay")
                    .define("showCyberEye", true);

            showCyberHeart = builder
                    .comment("Show cyber heart overlay")
                    .define("showCyberHeart", true);

            showCyberArms = builder
                    .comment("Show cyber arms")
                    .define("showCyberArms", true);

            showCyberLegs = builder
                    .comment("Show cyber legs")
                    .define("showCyberLegs", true);

            enableGlow = builder
                    .comment("Enable glowing cyberware parts")
                    .define("enableGlow", true);

            eyeHeight =  builder
                    .comment("Enable glowing cyberware parts")
                    .define("eyeHeight", 2);

            eyeCount = builder
                    .comment("Enable glowing cyberware parts")
                    .define("eyeCount", 2);

            builder.pop();

        }

    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, "cyberwareplus-client.toml");
    }
}