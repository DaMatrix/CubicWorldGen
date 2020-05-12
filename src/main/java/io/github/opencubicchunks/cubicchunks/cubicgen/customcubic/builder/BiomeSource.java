/*
 *  This file is part of Cubic World Generation, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2015-2020 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.builder;

import com.google.common.collect.ImmutableList;
import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.cubicgen.ConversionUtils;
import io.github.opencubicchunks.cubicchunks.cubicgen.cache.HashCache;
import io.github.opencubicchunks.cubicchunks.cubicgen.cache.HashCacheLongKeys;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.CubicBiome;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.BiomeBlockReplacerConfig;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.IBiomeBlockReplacer;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.IBiomeBlockReplacerProvider;
import io.github.opencubicchunks.cubicchunks.cubicgen.falling.Falling;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import javax.annotation.ParametersAreNonnullByDefault;

// a small hack to get biome generation working with the new system
// todo: make it not hacky
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BiomeSource {

    private static final int SECTION_SIZE = 4;

    public static final int CHUNKS_CACHE_RADIUS = 3;
    private static final int CHUNKS_CACHE_SIZE = CHUNKS_CACHE_RADIUS * CHUNKS_CACHE_RADIUS;

    private static final int SECTIONS_CACHE_RADIUS = 16;
    private static final int SECTIONS_CACHE_SIZE = SECTIONS_CACHE_RADIUS * SECTIONS_CACHE_RADIUS;

    public static final double HEIGHT = ConversionUtils.biomeHeightVanilla(Biomes.FOREST.getBaseHeight());
    public static final double VARIATION = ConversionUtils.biomeHeightVanilla(Biomes.FOREST.getHeightVariation());

    private final Map<Biome, IBiomeBlockReplacer[]> replacers = new IdentityHashMap<>();

    public BiomeSource(World world, BiomeBlockReplacerConfig conf, BiomeProvider biomeGen, int smoothRadius) {
        for (Biome biome : Falling.DEBUG ? Biome.REGISTRY : Arrays.asList(Falling.BIOMES)) {
            Iterable<IBiomeBlockReplacerProvider> providers = CubicBiome.getCubic(biome).getReplacerProviders();
            List<IBiomeBlockReplacer> list = new ArrayList<>();
            for (IBiomeBlockReplacerProvider prov : providers) {
                list.add(prov.create(world, CubicBiome.getCubic(biome), conf));
            }
            this.replacers.put(biome, list.toArray(new IBiomeBlockReplacer[list.size()]));
        }
    }

    public double getHeight(int x, int y, int z) {
        return HEIGHT;
    }

    public double getVolatility(int x, int y, int z) {
        return VARIATION;
    }

    public CubicBiome getBiome(int blockX, int blockY, int blockZ) {
        return CubicBiome.getCubic(Falling.biomeFor(blockY >> 4));
    }

    public IBiomeBlockReplacer[] getReplacers(int blockX, int blockY, int blockZ) {
        return this.replacers.get(Falling.biomeFor(blockY >> 4));
    }
}
