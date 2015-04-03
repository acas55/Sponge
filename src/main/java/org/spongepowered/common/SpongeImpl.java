package org.spongepowered.common;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public interface SpongeImpl {

    WorldServer[] getWorlds();

    @Nullable
    String getSaveFolder(WorldProvider provider);

}
