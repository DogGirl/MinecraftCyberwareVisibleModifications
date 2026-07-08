package com.msdoggirl.cyberwareplus.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEyeData extends INBTSerializable<CompoundTag> {
    int getEyeHeight();
    int getEyeCount();
}