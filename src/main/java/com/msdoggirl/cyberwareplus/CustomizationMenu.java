package com.msdoggirl.cyberwareplus;

import com.msdoggirl.dglib.api.ColorPickerAPI;
import com.msdoggirl.dglib.gui.ColorPickerScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = "cyberwareplus", bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class CustomizationMenu {

    public static final KeyMapping CUSTOMIZATION_MENU_KEY = new KeyMapping(
            "key.cyberwareplus.customizationmenu",
            GLFW.GLFW_KEY_O,
            "key.categories.cyberwareplus"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CustomizationMenu.CUSTOMIZATION_MENU_KEY);
    }

}
