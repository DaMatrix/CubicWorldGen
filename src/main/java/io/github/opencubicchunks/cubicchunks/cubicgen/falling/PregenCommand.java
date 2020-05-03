package io.github.opencubicchunks.cubicchunks.cubicgen.falling;

import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.world.ICubeProviderServer;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.WorldWorkerManager;

/**
 * @author DaPorkchop_
 */
public class PregenCommand extends CommandBase {
    @Override
    public String getName() {
        return "pregen";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "pregen <dim> <minX> <minY> <minZ> <maxX> <maxY> <maxZ>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WorldWorkerManager.addWorker(new PregenTask(
                sender,
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                Integer.parseInt(args[6])));
        sender.sendMessage(new TextComponentString("Generation started."));
    }

    private static class PregenTask implements WorldWorkerManager.IWorker {
        public final ICommandSender sender;
        public final int dim;
        public final int minX;
        public final int minY;
        public final int minZ;
        public final int maxX;
        public final int maxY;
        public final int maxZ;
        public int y;
        public int x;
        public int z;
        public long remaining;
        public final long total;
        public long lastMsg = System.currentTimeMillis();
        public Boolean keepingLoaded;

        public PregenTask(ICommandSender sender, int dim, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
            this.sender = sender;
            this.dim = dim;
            this.minX = (minX >> 4) - 1;
            this.minY = (minY >> 4) - 1;
            this.minZ = (minZ >> 4) - 1;
            this.maxX = (maxX >> 4) + 1;
            this.maxY = (maxY >> 4) + 1;
            this.maxZ = (maxZ >> 4) + 1;
            this.y = this.maxY;
            this.x = this.minX;
            this.z = this.maxZ;
            this.remaining = this.total = (long) (this.maxX - this.minX) * (long) (this.maxY - this.minY) * (long) (this.maxZ - this.minZ);
        }

        @Override
        public boolean hasWork() {
            return this.remaining > 0L;
        }

        @Override
        public boolean doWork() {
            WorldServer world = DimensionManager.getWorld(this.dim);
            if (world == null) {
                DimensionManager.initDimension(this.dim);
                world = DimensionManager.getWorld(this.dim);
                if (world == null) {
                    this.sender.sendMessage(new TextComponentString("Unable to load dimension " + this.dim));
                    this.remaining = 0L;
                    return false;
                }
            }

            if (this.keepingLoaded == null) {
                this.keepingLoaded = DimensionManager.keepDimensionLoaded(this.dim, true);
            }

            if (this.lastMsg + 5000L < System.currentTimeMillis()) {
                this.lastMsg = System.currentTimeMillis();
                this.sender.sendMessage(new TextComponentString(String.format(
                        "Generated %d/%d cubes, current block Y: %d",
                        this.total - this.remaining, this.total, this.y << 4
                )));
            }

            if (this.hasWork()) {
                //generate the chunk at the current position
                ICubeProviderServer provider = ((ICubicWorldServer) world).getCubeCache();
                ICube cube = provider.getCube(this.x, this.y, this.z, ICubeProviderServer.Requirement.POPULATE);
                if (!cube.isFullyPopulated())   {
                    throw new IllegalStateException("Cube isn't fully populated!");
                }
                if (++this.z > this.maxZ) {
                    if (++this.x > this.maxX) {
                        if (--this.y < this.minY) {
                            if (this.remaining > 1L) {
                                throw new IllegalStateException(this.remaining + " chunks remaining when we were finished!");
                            }
                        }
                        this.x = this.minX;
                    }
                    this.z = this.minZ;
                }
            }

            boolean hasWork = --this.remaining > 0L;
            if (!hasWork) {
                this.sender.sendMessage(new TextComponentString("Generation complete."));
                if (this.keepingLoaded != null && this.keepingLoaded) {
                    DimensionManager.keepDimensionLoaded(this.dim, false);
                }
            }
            return hasWork;
        }
    }
}
