package io.github.opencubicchunks.cubicchunks.cubicgen.asm.mixin.common;

import io.github.opencubicchunks.cubicchunks.api.worldgen.CubeGeneratorsRegistry;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.populator.AnimalsPopulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CubeGeneratorsRegistry.class)
public class MixinCubeGeneratorsRegistry {
    /*@Inject(
            method = "Lio/github/opencubicchunks/cubicchunks/api/worldgen/CubeGeneratorsRegistry;computeSortedGeneratorList()V",
            at = @At("HEAD")
    )
    private static void preComputeSortedGeneratorList(CallbackInfo callbackInfo)    {
        System.out.println("jeff v2");
    }

    @Redirect(
            method = "Lio/github/opencubicchunks/cubicchunks/api/worldgen/CubeGeneratorsRegistry;computeSortedGeneratorList()V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    @SuppressWarnings("unchecked")
    private static boolean redirectAdd(List list, Object o) {
        if (o instanceof AnimalsPopulator)  {
            System.out.println("Ignoring animals populator");
            return false;
        }
        System.out.printf("Adding populator %s\n", o.getClass().getCanonicalName());
        return list.add(o);
    }*/
}
