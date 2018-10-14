package io.github.opencubicchunks.cubicchunks.cubicgen.asm.mixin.common;

import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldServer;
import io.github.opencubicchunks.cubicchunks.core.world.CubeWorldEntitySpawner;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.Set;

@Mixin(CubeWorldEntitySpawner.class)
public class MixinCubeWorldEntitySpawner {
    /**
     * @author daporkchop
     */
    /*@Overwrite
    public int findChunksForSpawning(WorldServer world, boolean hostileEnable, boolean peacefulEnable, boolean spawnOnSetTickRate) {
        return 0;
    }*/

    /**
     * @author daporkchop
     */
    /*@Overwrite
    private int addEligibleChunks(WorldServer world, Set<CubePos> possibleChunks) {
        return 0;
    }*/

    /**
     * @author daporkchop
     */
    /*@Overwrite
    private int spawnCreatureTypeInAllChunks(EnumCreatureType mobType, WorldServer world, ArrayList<CubePos> chunkList){
        return 0;
    }*/
}
