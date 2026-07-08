package com.msdoggirl.cyberwareplus.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilityHandler {
    public static Capability<IEyeData> CUSTOMIZATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
