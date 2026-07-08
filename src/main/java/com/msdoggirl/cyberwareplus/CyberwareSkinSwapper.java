package com.msdoggirl.cyberwareplus;

import com.msdoggirl.cyberwareplus.config.CyberwarePlusConfig;
import com.msdoggirl.dglib.api.ColoredSkinGlowLayerAPI;
import com.msdoggirl.dglib.api.SkinSwapperAPI;
import com.msdoggirl.cyberwareplus.config.VisualConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.msdoggirl.cyberwareplus.CustomizationMenu.CUSTOMIZATION_MENU_KEY;

@Mod.EventBusSubscriber(modid = "cyberwareplus", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CyberwareSkinSwapper {

    private static final String TEXTURE_PATH_LIMBLESS = "textures/skins/empty_limbless.png";
    public static final ResourceLocation GLOW_1_2 = new ResourceLocation("dglib","textures/skins/default_glow_1_2.png");
    public static final ResourceLocation GLOW_1_3 = new ResourceLocation("dglib","textures/skins/default_glow_1_3.png");
    public static final ResourceLocation GLOW_1_4 = new ResourceLocation("dglib","textures/skins/default_glow_1_4.png");
    public static final ResourceLocation GLOW_1_5 = new ResourceLocation("dglib","textures/skins/default_glow_1_5.png");
    public static final ResourceLocation GLOW_2_2 = new ResourceLocation("dglib","textures/skins/default_glow_2_2.png");
    public static final ResourceLocation GLOW_2_3 = new ResourceLocation("dglib","textures/skins/default_glow_2_3.png");
    public static final ResourceLocation GLOW_2_4 = new ResourceLocation("dglib","textures/skins/default_glow_2_4.png");
    public static final ResourceLocation GLOW_2_5 = new ResourceLocation("dglib","textures/skins/default_glow_2_5.png");
    private static boolean reChecked = false;
    private static boolean wasReloaded = false;

    private static int tickCounter = 0;
    private static int tickCounter2 = 0;
    private static final int CHECK_INTERVAL = 20;

    // Track the current client level for world change detection
    private static Level previousLevel = null;

    public static boolean hadCyberEye = false;
    public static boolean hadCyberHeart = false;
    public static boolean hadCyberRightArm = false;
    public static boolean hadCyberLeftArm = false;
    public static boolean hadCyberRightLeg = false;
    public static boolean hadCyberLeftLeg = false;

    public static int headColor;
    public static int bodyColor;
    public static int rightArmColor;
    public static int leftArmColor;
    public static int rightLegColor;
    public static int leftLegColor;

    // Per-player previous states for change detection
    public static final Map<UUID, PlayerVisualState> previousStates = new HashMap<>();


    public static class PlayerVisualState {
        boolean syntheticSkin = false;
        boolean cyberEye = false;
        boolean cyberHeart = false;
        boolean humanRightArm = false;
        boolean humanLeftArm = false;
        boolean humanRightLeg = false;
        boolean humanLeftLeg = false;
        boolean cyberRightArm = false;
        boolean cyberLeftArm = false;
        boolean cyberRightLeg = false;
        boolean cyberLeftLeg = false;
        int eyeCount = 0;
        int eyeHeight = 0;



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerVisualState that = (PlayerVisualState) o;
            return syntheticSkin == that.syntheticSkin &&
                   cyberEye == that.cyberEye &&
                   cyberHeart == that.cyberHeart &&
                   humanRightArm == that.humanRightArm &&
                   humanLeftArm == that.humanLeftArm &&
                   humanRightLeg == that.humanRightLeg &&
                   humanLeftLeg == that.humanLeftLeg &&
                   cyberRightArm == that.cyberRightArm &&
                   cyberLeftArm == that.cyberLeftArm &&
                   cyberRightLeg == that.cyberRightLeg &&
                   cyberLeftLeg == that.cyberLeftLeg &&
                    eyeCount == that.eyeCount &&
                    eyeHeight == that.eyeHeight;
        }

        @Override
        public int hashCode() {
            // Simple hash for equality
            return Boolean.hashCode(syntheticSkin) ^ Boolean.hashCode(cyberEye) ^
                   Boolean.hashCode(cyberHeart) ^ Boolean.hashCode(humanRightArm) ^
                   Boolean.hashCode(humanLeftArm) ^ Boolean.hashCode(humanRightLeg) ^
                   Boolean.hashCode(humanLeftLeg) ^ Boolean.hashCode(cyberRightArm) ^
                   Boolean.hashCode(cyberLeftArm) ^ Boolean.hashCode(cyberRightLeg) ^
                   Boolean.hashCode(cyberLeftLeg) ^ Integer.hashCode(eyeCount) ^ Integer.hashCode(eyeHeight);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            previousStates.clear();
            previousLevel = null;
            reChecked = false;
            wasReloaded = false;
            ClientCyberwareData.clear();
            //System.out.println("[Cyberware+] World Unloaded.");
            return;
        } else if (mc.level != previousLevel) {
            //System.out.println("[Cyberware+] Level Changed. Reloading.");
            previousLevel = mc.level;
            previousStates.clear();
            wasReloaded = true;
        }

        if (!reChecked) {
            tickCounter2++;
            if (tickCounter2 > 400 && wasReloaded) {
                previousStates.clear();
                tickCounter2 = 0;
                reChecked = true;
                //System.out.println("[Cyberware+] Reloaded.");
            }
        }

        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) return;
        tickCounter = 0;


        UUID uuid = mc.player.getUUID();

        headColor = ColoredSkinGlowLayerAPI.getHeadColor(uuid);
        bodyColor = ColoredSkinGlowLayerAPI.getBodyColor(uuid);
        rightArmColor = ColoredSkinGlowLayerAPI.getRightArmColor(uuid);
        leftArmColor = ColoredSkinGlowLayerAPI.getLeftArmColor(uuid);
        rightLegColor = ColoredSkinGlowLayerAPI.getRightLegColor(uuid);
        leftLegColor = ColoredSkinGlowLayerAPI.getLeftLegColor(uuid);


        for (AbstractClientPlayer player : mc.level.players()) {

            uuid = player.getUUID();
            CyberwareSkinSwapper.PlayerVisualState newState = ClientCyberwareData.getState(uuid);
            CyberwareSkinSwapper.PlayerVisualState oldState = previousStates.get(uuid);

            if(oldState != null && player == mc.player) {
                if(oldState.cyberEye) hadCyberEye = true;
                if(oldState.cyberHeart) hadCyberHeart = true;
                if(oldState.cyberRightArm) hadCyberRightArm = true;
                if(oldState.cyberLeftArm) hadCyberLeftArm = true;
                if(oldState.cyberRightLeg) hadCyberRightLeg = true;
                if(oldState.cyberLeftLeg) hadCyberLeftLeg = true;
            }


            if (oldState == null || !oldState.equals(newState)) {


                //applyVisualState(uuid, newState);

                if(player == mc.player) {
                    hadCyberEye = false;
                    hadCyberHeart = false;
                    hadCyberRightArm = false;
                    hadCyberLeftArm = false;
                    hadCyberRightLeg = false;
                    hadCyberLeftLeg = false;
                }
                //System.out.println("[Cyberware+] Visual state applied");

                previousStates.put(uuid, copyState(newState));
                if (uuid.equals(mc.player.getUUID())) {
                    newState.eyeCount = CyberwarePlusConfig.CLIENT.eyeCount.get();
                    newState.eyeHeight = CyberwarePlusConfig.CLIENT.eyeHeight.get();
                    NetworkHandler.INSTANCE.sendToServer(new SyncCyberwareC2S(
                            newState.syntheticSkin, newState.cyberEye, newState.cyberHeart,
                            newState.humanRightArm, newState.humanLeftArm, newState.humanRightLeg, newState.humanLeftLeg,
                            newState.cyberRightArm, newState.cyberLeftArm, newState.cyberRightLeg, newState.cyberLeftLeg, newState.eyeCount, newState.eyeHeight
                    ));
                    System.out.println(uuid + " sent eye data. Height: " + newState.eyeHeight + " Count: " + newState.eyeCount);
                }


                applyVisualState(uuid, newState);
                CyberwareGlow.glow(false, newState);
            }

            if (CUSTOMIZATION_MENU_KEY.consumeClick()) {
                if (mc.player != null && mc.screen == null) {
                    newState.eyeCount = CyberwarePlusConfig.CLIENT.eyeCount.get();
                    newState.eyeHeight = CyberwarePlusConfig.CLIENT.eyeHeight.get();
                    NetworkHandler.INSTANCE.sendToServer(new SyncCyberwareC2S(
                            newState.syntheticSkin, newState.cyberEye, newState.cyberHeart,
                            newState.humanRightArm, newState.humanLeftArm, newState.humanRightLeg, newState.humanLeftLeg,
                            newState.cyberRightArm, newState.cyberLeftArm, newState.cyberRightLeg, newState.cyberLeftLeg, newState.eyeCount, newState.eyeHeight
                    ));
                    CyberwareGlow.glow(true, newState);
                }
            }
        }

    }

    public static void applyVisualState(UUID uuid, PlayerVisualState state) {


        if (!VisualConfig.anyVisualsEnabled()) {
            // Hide everything
            SkinSwapperAPI.disableHeadOverlay(uuid);
            SkinSwapperAPI.disableBodyOverlay(uuid);
            SkinSwapperAPI.disableRightArm(uuid);
            SkinSwapperAPI.disableLeftArm(uuid);
            SkinSwapperAPI.disableRightLeg(uuid);
            SkinSwapperAPI.disableLeftLeg(uuid);
            return;
        }
        applyCyberEye(state, uuid);
        applyCyberHeart(state, uuid);
        applyCyberRightArm(state, uuid);
        applyCyberLeftArm(state, uuid);
        applyCyberRightLeg(state, uuid);
        applyCyberLeftLeg(state, uuid);


    }
    public static void applyCyberEye(PlayerVisualState state, UUID uuid) {
        if (VisualConfig.showCyberEye() && state.cyberEye && !state.syntheticSkin) {
            SkinSwapperAPI.enableHeadOverlay(uuid, VisualConfig.path(state.eyeHeight, state.eyeCount));
            System.out.println("Fetched skin at "+ VisualConfig.path(state.eyeHeight, state.eyeCount) + " for " + uuid + ". Height: " + state.eyeHeight + " Count: " + state.eyeCount);
        } else {
            SkinSwapperAPI.disableHeadOverlay(uuid);
        }
    }

    public static void applyCyberHeart(PlayerVisualState state, UUID uuid) {
        if (VisualConfig.showCyberHeart() && state.cyberHeart && !state.syntheticSkin) {
            SkinSwapperAPI.enableBodyOverlay(uuid, VisualConfig.path(state.eyeHeight, state.eyeCount));
        } else {
            SkinSwapperAPI.disableBodyOverlay(uuid);
        }
    }

    public static void applyCyberRightArm(PlayerVisualState state, UUID uuid) {
        if (state.humanRightArm || (state.cyberRightArm && state.syntheticSkin)) {
            SkinSwapperAPI.disableRightArm(uuid);
        } else if (VisualConfig.showCyberArms() && state.cyberRightArm && !state.syntheticSkin) {
            SkinSwapperAPI.enableRightArm(uuid, VisualConfig.path(state.eyeHeight, state.eyeCount));
        } else if (!state.humanRightArm && !state.cyberRightArm) {
            SkinSwapperAPI.enableRightArm(uuid, TEXTURE_PATH_LIMBLESS);
        }
    }

    public static void applyCyberLeftArm(PlayerVisualState state, UUID uuid) {
        if (state.humanLeftArm || (state.cyberLeftArm && state.syntheticSkin)) {
            SkinSwapperAPI.disableLeftArm(uuid);
        } else if (VisualConfig.showCyberArms() && state.cyberLeftArm && !state.syntheticSkin) {
            SkinSwapperAPI.enableLeftArm(uuid, VisualConfig.path(state.eyeHeight, state.eyeCount));
        } else if (!state.humanLeftArm && !state.cyberLeftArm)  {
            SkinSwapperAPI.enableLeftArm(uuid, TEXTURE_PATH_LIMBLESS);
        }
    }

    public static void applyCyberRightLeg(PlayerVisualState state, UUID uuid) {
        // Right Leg
        if (state.humanRightLeg || (state.cyberRightLeg && state.syntheticSkin)) {
            SkinSwapperAPI.disableRightLeg(uuid);
        } else if (VisualConfig.showCyberLegs() && state.cyberRightLeg && !state.syntheticSkin) {
            SkinSwapperAPI.enableRightLeg(uuid, VisualConfig.path(state.eyeHeight, state.eyeCount));
        } else if (!state.humanRightLeg && !state.cyberRightLeg)  {
            SkinSwapperAPI.enableRightLeg(uuid, TEXTURE_PATH_LIMBLESS);
        }
    }

    public static void applyCyberLeftLeg(PlayerVisualState state, UUID uuid) {
        // Left Leg
        if (state.humanLeftLeg || (state.cyberLeftLeg && state.syntheticSkin)) {
            SkinSwapperAPI.disableLeftLeg(uuid);
        } else if (VisualConfig.showCyberLegs() &&  state.cyberLeftLeg && !state.syntheticSkin) {
            SkinSwapperAPI.enableLeftLeg(uuid, VisualConfig.path(state.eyeHeight, state.eyeCount));
        }  else if (!state.humanLeftLeg && !state.cyberLeftLeg) {
            SkinSwapperAPI.enableLeftLeg(uuid, TEXTURE_PATH_LIMBLESS);
        }
    }


    private static PlayerVisualState copyState(PlayerVisualState original) {
        PlayerVisualState copy = new PlayerVisualState();
        copy.syntheticSkin   = original.syntheticSkin;
        copy.cyberEye        = original.cyberEye;
        copy.cyberHeart      = original.cyberHeart;
        copy.humanRightArm   = original.humanRightArm;
        copy.humanLeftArm    = original.humanLeftArm;
        copy.humanRightLeg   = original.humanRightLeg;
        copy.humanLeftLeg    = original.humanLeftLeg;
        copy.cyberRightArm   = original.cyberRightArm;
        copy.cyberLeftArm    = original.cyberLeftArm;
        copy.cyberRightLeg   = original.cyberRightLeg;
        copy.cyberLeftLeg    = original.cyberLeftLeg;
        copy.eyeHeight = original.eyeHeight;
        copy.eyeCount = original.eyeCount;
        return copy;
    }
}