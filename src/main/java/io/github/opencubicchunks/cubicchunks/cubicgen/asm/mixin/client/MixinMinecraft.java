package io.github.opencubicchunks.cubicchunks.cubicgen.asm.mixin.client;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    /*@Redirect(
            method = "Lnet/minecraft/client/Minecraft;createDisplay()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"
            )
    )
    private void redirectSetTitle(String text) {
        Display.setTitle("CubicChunks 1.12.2");
    }*/

    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    private boolean fullscreen;

    @Shadow
    protected abstract void updateDisplayMode() throws LWJGLException;

    /**
     * @author daporkchop
     */
    @Overwrite
    private void createDisplay() throws LWJGLException {
        Display.setResizable(true);
        Display.setTitle("CubicChunks 1.12.2");
        System.out.println("jeff!");

        try {
            Display.create((new PixelFormat()).withDepthBits(24));
        } catch (LWJGLException lwjglexception) {
            LOGGER.error("Couldn't set pixel format", (Throwable) lwjglexception);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var3) {
                ;
            }

            if (this.fullscreen) {
                this.updateDisplayMode();
            }

            Display.create();
        }
    }
}
