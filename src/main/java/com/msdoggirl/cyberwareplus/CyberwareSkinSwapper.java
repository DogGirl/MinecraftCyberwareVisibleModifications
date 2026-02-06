package com.msdoggirl.cyberwareplus;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.init.ModItems;
import com.msdoggirl.cyberwareplus.SyncCyberwareC2S;
import com.msdoggirl.cyberwareplus.NetworkHandler;     // your custom one
import com.msdoggirl.dglib.api.SkinGlowLayerAPI;
import com.msdoggirl.dglib.api.SkinSwapperAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;

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

@Mod.EventBusSubscriber(modid = "cyberwareplus", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CyberwareSkinSwapper {

    private static final String TEXTURE_PATH = "textures/skins/test_skin.png";
    private static final String TEXTURE_PATH_LIMBLESS = "textures/skins/empty.png";
    private static final String TEXTURE_PATH_GLOW = "textures/skins/cyber_glow.png";

    private static ResourceLocation GLOW_TEXTURE = new ResourceLocation("cyberwareplus", TEXTURE_PATH_GLOW);

    private static boolean skinUpdated = false;
    private static boolean reChecked = false;
    private static boolean wasReloaded = false;

    private static int tickCounter = 0;
    private static int tickCounter2 = 0;
    private static final int CHECK_INTERVAL = 20;

    // Track the current client level for world change detection
    private static Level previousLevel = null;

    // Per-player previous states for change detection
    private static final Map<UUID, PlayerVisualState> previousStates = new HashMap<>();

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
                   cyberLeftLeg == that.cyberLeftLeg;
        }

        @Override
        public int hashCode() {
            // Simple hash for equality
            return Boolean.hashCode(syntheticSkin) ^ Boolean.hashCode(cyberEye) ^ // etc.
                   Boolean.hashCode(cyberHeart) ^ Boolean.hashCode(humanRightArm) ^
                   Boolean.hashCode(humanLeftArm) ^ Boolean.hashCode(humanRightLeg) ^
                   Boolean.hashCode(humanLeftLeg) ^ Boolean.hashCode(cyberRightArm) ^
                   Boolean.hashCode(cyberLeftArm) ^ Boolean.hashCode(cyberRightLeg) ^
                   Boolean.hashCode(cyberLeftLeg);
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

        boolean localUpdated = false;


        AbstractClientPlayer localPlayer = mc.player;

        for (AbstractClientPlayer player : mc.level.players()) {

            UUID uuid = player.getUUID();
            CyberwareSkinSwapper.PlayerVisualState newState = ClientCyberwareData.getState(uuid); // Changed: Use synced data or local capability


            CyberwareSkinSwapper.PlayerVisualState oldState = previousStates.get(uuid);
            if (oldState == null || !oldState.equals(newState)) {
                applyVisualState(uuid, newState);
                if(player == localPlayer) applyGlowVisualState(copyState(newState));
                previousStates.put(uuid, copyState(newState)); // Added: Use copy to avoid reference issues
                if (uuid.equals(mc.player.getUUID())) {
                    localUpdated = true;
                    // Added: Send packet to server if local state changed
                    NetworkHandler.INSTANCE.sendToServer(new SyncCyberwareC2S(
                            newState.syntheticSkin, newState.cyberEye, newState.cyberHeart,
                            newState.humanRightArm, newState.humanLeftArm, newState.humanRightLeg, newState.humanLeftLeg,
                            newState.cyberRightArm, newState.cyberLeftArm, newState.cyberRightLeg, newState.cyberLeftLeg
                    ));
                }
            }
        }
    }

    private static void applyVisualState(UUID uuid, PlayerVisualState state) {
        // Eye
        if (state.cyberEye && !state.syntheticSkin) {
            SkinSwapperAPI.enableHeadOverlay(uuid, TEXTURE_PATH);
        } else {
            SkinSwapperAPI.disableHeadOverlay(uuid);
        }

        // Heart
        if (state.cyberHeart && !state.syntheticSkin) {
            SkinSwapperAPI.enableBodyOverlay(uuid, TEXTURE_PATH);
        } else {
            SkinSwapperAPI.disableBodyOverlay(uuid);
        }

        // Right Arm
        if (state.humanRightArm || (state.cyberRightArm && state.syntheticSkin)) {
            SkinSwapperAPI.disableRightArm(uuid);
        } else if (state.cyberRightArm && !state.syntheticSkin) {
            SkinSwapperAPI.enableRightArm(uuid, TEXTURE_PATH);
        } else {
            SkinSwapperAPI.enableRightArm(uuid, TEXTURE_PATH_LIMBLESS);
        }

        // Left Arm
        if (state.humanLeftArm || (state.cyberLeftArm && state.syntheticSkin)) {
            SkinSwapperAPI.disableLeftArm(uuid);
        } else if (state.cyberLeftArm && !state.syntheticSkin) {
            SkinSwapperAPI.enableLeftArm(uuid, TEXTURE_PATH);
        } else {
            SkinSwapperAPI.enableLeftArm(uuid, TEXTURE_PATH_LIMBLESS);
        }

        // Right Leg
        if (state.humanRightLeg || (state.cyberRightLeg && state.syntheticSkin)) {
            SkinSwapperAPI.disableRightLeg(uuid);
        } else if (state.cyberRightLeg && !state.syntheticSkin) {
            SkinSwapperAPI.enableRightLeg(uuid, TEXTURE_PATH);
        } else {
            SkinSwapperAPI.enableRightLeg(uuid, TEXTURE_PATH_LIMBLESS);
        }

        // Left Leg
        if (state.humanLeftLeg || (state.cyberLeftLeg && state.syntheticSkin)) {
            SkinSwapperAPI.disableLeftLeg(uuid);
        } else if (state.cyberLeftLeg && !state.syntheticSkin) {
            SkinSwapperAPI.enableLeftLeg(uuid, TEXTURE_PATH);
        } else {
            SkinSwapperAPI.enableLeftLeg(uuid, TEXTURE_PATH_LIMBLESS);
        }
    }

    private static void applyGlowVisualState(PlayerVisualState state) {
        // Eye
        if (state.cyberEye && !state.syntheticSkin) {
            SkinGlowLayerAPI.setHeadGlow(GLOW_TEXTURE, true);
        } else {
            SkinGlowLayerAPI.setHeadGlow(GLOW_TEXTURE, false);
        }

        // Heart
        if (state.cyberHeart && !state.syntheticSkin) {
            SkinGlowLayerAPI.setBodyGlow(GLOW_TEXTURE, true);
        } else {
            SkinGlowLayerAPI.setBodyGlow(GLOW_TEXTURE, false);
        }

        // Right Arm
        if (state.humanRightArm || (state.cyberRightArm && state.syntheticSkin)) {
            SkinGlowLayerAPI.setRightArmGlow(GLOW_TEXTURE, false);
        } else if (state.cyberRightArm && !state.syntheticSkin) {
            SkinGlowLayerAPI.setRightArmGlow(GLOW_TEXTURE, true);
        } else {
            SkinGlowLayerAPI.setRightArmGlow(GLOW_TEXTURE, true);
        }

        // Left Arm
        if (state.humanLeftArm || (state.cyberLeftArm && state.syntheticSkin)) {
            SkinGlowLayerAPI.setLeftArmGlow(GLOW_TEXTURE, false);
        } else if (state.cyberLeftArm && !state.syntheticSkin) {
            SkinGlowLayerAPI.setLeftArmGlow(GLOW_TEXTURE, true);
        } else {
            SkinGlowLayerAPI.setLeftArmGlow(GLOW_TEXTURE, true);
        }

        // Right Leg
        if (state.humanRightLeg || (state.cyberRightLeg && state.syntheticSkin)) {
            SkinGlowLayerAPI.setRightLegGlow(GLOW_TEXTURE, false);
        } else if (state.cyberRightLeg && !state.syntheticSkin) {
            SkinGlowLayerAPI.setRightLegGlow(GLOW_TEXTURE, true);
        } else {
            SkinGlowLayerAPI.setRightLegGlow(GLOW_TEXTURE, true);
        }

        // Left Leg
        if (state.humanLeftLeg || (state.cyberLeftLeg && state.syntheticSkin)) {
            SkinGlowLayerAPI.setLeftLegGlow(GLOW_TEXTURE, false);
        } else if (state.cyberLeftLeg && !state.syntheticSkin) {
            SkinGlowLayerAPI.setLeftLegGlow(GLOW_TEXTURE, true);
        } else {
            SkinGlowLayerAPI.setLeftLegGlow(GLOW_TEXTURE, true);
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
        return copy;
    }
} 