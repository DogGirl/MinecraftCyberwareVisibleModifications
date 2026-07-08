package com.msdoggirl.cyberwareplus.capability;

import com.msdoggirl.cyberwareplus.config.CyberwarePlusConfig;
import net.minecraft.nbt.CompoundTag;

public class EyeData implements IEyeData {
    private int eyeHeight = CyberwarePlusConfig.CLIENT.eyeHeight.get();
    private int eyeCount = CyberwarePlusConfig.CLIENT.eyeCount.get();

    @Override
    public int getEyeHeight() {return eyeHeight;}

    @Override
    public int getEyeCount() {return eyeCount;}

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("EyeCount", eyeCount);
        nbt.putInt("EyeHeight", eyeHeight);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        eyeCount = nbt.getInt("EyeCount");
        eyeHeight = nbt.getInt("EyeHeight");
    }
}
