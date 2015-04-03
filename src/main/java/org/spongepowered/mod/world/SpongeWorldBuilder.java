/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.world;

import org.spongepowered.api.world.DimensionTypes;

import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.mod.service.persistence.NbtTranslator;
import com.google.common.base.Optional;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBuilder;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.mod.interfaces.IMixinWorldSettings;

public class SpongeWorldBuilder implements WorldBuilder {

    private String name;
    private long seed;
    private GameMode gameMode;
    private GeneratorType generatorType;
    private DimensionType dimensionType;
    private boolean mapFeaturesEnabled;
    private boolean hardcore;
    private boolean isWorldEnabled;
    private boolean loadOnStartup;
    private boolean keepSpawnLoaded;
    private DataContainer generatorSettings;

    public SpongeWorldBuilder() {
        reset();
    }

    public SpongeWorldBuilder(WorldCreationSettings settings) {
        this.name = settings.getWorldName();
        this.seed = settings.getSeed();
        this.gameMode = settings.getGameMode();
        this.generatorType = settings.getGeneratorType();
        this.dimensionType = settings.getDimensionType();
        this.mapFeaturesEnabled = settings.usesMapFeatures();
        this.hardcore = settings.isHardcore();
        this.isWorldEnabled = settings.isEnabled();
        this.loadOnStartup = settings.loadOnStartup();
        this.keepSpawnLoaded = settings.doesKeepSpawnLoaded();
    }

    public SpongeWorldBuilder(WorldProperties properties) {
        this.name = properties.getWorldName();
        this.seed = properties.getSeed();
        this.gameMode = properties.getGameMode();
        this.generatorType = properties.getGeneratorType();
        this.dimensionType = properties.getDimensionType();
        this.mapFeaturesEnabled = properties.usesMapFeatures();
        this.hardcore = properties.isHardcore();
        this.isWorldEnabled = properties.isEnabled();
        this.loadOnStartup = properties.loadOnStartup();
        this.keepSpawnLoaded = properties.doesKeepSpawnLoaded();
    }

    @Override
    public WorldBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public WorldBuilder seed(long seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public WorldBuilder gameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    @Override
    public WorldBuilder generator(GeneratorType type) {
        this.generatorType = type;
        return this;
    }

    @Override
    public WorldBuilder dimensionType(DimensionType type) {
        this.dimensionType = type;
        return this;
    }

    @Override
    public WorldBuilder usesMapFeatures(boolean enabled) {
        this.mapFeaturesEnabled = enabled;
        return this;
    }

    @Override
    public WorldBuilder hardcore(boolean enabled) {
        this.hardcore = enabled;
        return this;
    }

    @Override
    public WorldBuilder enabled(boolean state) {
        this.isWorldEnabled = state;
        return this;
    }

    @Override
    public WorldBuilder loadsOnStartup(boolean state) {
        this.loadOnStartup = state;
        return this;
    }

    @Override
    public WorldBuilder keepsSpawnLoaded(boolean state) {
        this.keepSpawnLoaded = state;
        return this;
    }

    @Override
    public WorldBuilder generatorSettings(DataContainer settings) {
        this.generatorSettings = settings;
        return this;
    }

    @Override
    public Optional<World> build() throws IllegalStateException {
        WorldCreationSettings settings = buildSettings();
        ((Server) MinecraftServer.getServer()).createWorld(buildSettings());
        return ((Server) MinecraftServer.getServer()).loadWorld(settings.getWorldName());
    }

    @Override
    public WorldCreationSettings buildSettings() throws IllegalStateException {
        WorldSettings settings =
                new WorldSettings(this.seed, GameType.valueOf(this.gameMode.getTranslation().get()), this.mapFeaturesEnabled, this.hardcore,
                        (WorldType) this.generatorType);
        settings.setWorldName(this.name);
        ((IMixinWorldSettings) (Object) settings).setDimensionType(this.dimensionType);
        ((IMixinWorldSettings) (Object) settings).setGeneratorSettings(this.generatorSettings);
        ((IMixinWorldSettings) (Object) settings).setEnabled(this.isWorldEnabled);
        ((IMixinWorldSettings) (Object) settings).setKeepSpawnLoaded(this.keepSpawnLoaded);
        ((IMixinWorldSettings) (Object) settings).setLoadOnStartup(this.loadOnStartup);
        return (WorldCreationSettings) (Object) settings;
    }

    @Override
    public WorldBuilder reset() {
        this.name = "spongeworld";
        this.seed = 1234567890;
        this.gameMode = GameModes.SURVIVAL;
        this.generatorType = GeneratorTypes.DEFAULT;
        this.dimensionType = DimensionTypes.OVERWORLD;
        this.mapFeaturesEnabled = true;
        this.hardcore = false;
        this.isWorldEnabled = true;
        this.loadOnStartup = true;
        this.keepSpawnLoaded = false;
        this.generatorSettings = (DataContainer) NbtTranslator.getInstance().translateFrom(new NBTTagCompound());
        return this;
    }

}
