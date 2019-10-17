package io.github.opencubicchunks.cubicchunks.cubicgen.preset.wrapper;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * BlockState descriptor. Allows to keep blockstates from mods, that don't exist currently.
 */
public final class BlockStateDesc {

    private final String blockId;
    private final Map<String, String> properties;

    private final IBlockState blockState;

    public BlockStateDesc(String blockId, Map<String, String> properties) {
        this.blockId = blockId;
        this.properties = properties;

        ResourceLocation id = new ResourceLocation(blockId);
        if (!ForgeRegistries.BLOCKS.containsKey(id)) {
            blockState = null;
            return;
        }
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        IBlockState state = block.getDefaultState();

        BlockStateContainer container = block.getBlockState();

        for (String s : properties.keySet()) {
            IProperty<?> prop = container.getProperty(s);

            if (prop != null) {
                state = setProperty(state, prop, properties.get(s));
            }
        }
        this.blockState = state;
    }

    public BlockStateDesc(IBlockState value) {
        this.blockState = value;

        this.blockId = ForgeRegistries.BLOCKS.getKey(value.getBlock()).toString();
        this.properties = new HashMap<>();
        for (Map.Entry<IProperty<?>, Comparable<?>> entry : value.getProperties().entrySet()) {
            properties.put(entry.getKey().getName(), getPropertyString(entry.getKey(), entry.getValue()));
        }
    }


    public String getBlockId() {
        return blockId;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    private static <T extends Comparable<T>> IBlockState setProperty(IBlockState state, IProperty<T> propName, String value) {
        return propName.parseValue(value).transform(property -> state.withProperty(propName, property)).or(state);
    }


    @SuppressWarnings("unchecked") private static <T extends Comparable<T>> String getPropertyString(IProperty<T> prop, Comparable<?> value) {
        return prop.getName((T) value);
    }

    public IBlockState getOrDefault(IBlockState defaultState) {
        return blockState == null ? defaultState : blockState;
    }

    public BlockDesc getBlock() {
        return new BlockDesc(blockId);
    }
}
