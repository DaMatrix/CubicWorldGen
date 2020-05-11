package io.github.opencubicchunks.cubicchunks.cubicgen.falling;

import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import net.daporkchop.lib.common.pool.handle.DefaultThreadHandledPool;
import net.daporkchop.lib.common.pool.handle.Handle;
import net.daporkchop.lib.common.pool.handle.HandledPool;
import net.daporkchop.lib.common.util.PorkUtil;
import net.daporkchop.lib.unsafe.PUnsafe;

import java.util.Arrays;

/**
 * @author DaPorkchop_
 */
public final class PooledCubePrimer extends CubePrimer {
    private static final HandledPool<PooledCubePrimer> POOL = new DefaultThreadHandledPool<>(PooledCubePrimer::new, 1);

    protected static final long ACTIVE_OFFSET = PUnsafe.pork_getOffset(PorkUtil.classForName("net.daporkchop.lib.common.pool.handle.DefaultThreadHandledPool$HandleImpl"), "active");

    public static CubePrimer get() {
        Handle<PooledCubePrimer> handle = POOL.get();
        if (!PUnsafe.compareAndSwapInt(handle, ACTIVE_OFFSET, 0, 1)) { //oops it's broken
            System.out.println("Handle was already active...");
        }
        PooledCubePrimer primer = handle.value();
        primer.handle = handle;
        return primer;
    }

    private Handle<PooledCubePrimer> handle;

    private PooledCubePrimer() {
        super();
        System.out.println("Allocating new PooledCubePrimer");
    }

    @Override
    public synchronized void close() {
        if (this.handle != null) {
            //Arrays.fill(this.data, (char) 0);
            this.biome = null;
            this.handle.close();
            this.handle = null;
        } else {
            throw new IllegalStateException("Already released!");
        }
    }
}
