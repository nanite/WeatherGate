package com.unrealdinnerbone.weathergate.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.unrealdinnerbone.weathergate.attrubutes.Injector;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @ModifyReturnValue(method = "addEnvironmentAttributeLayers", at = @At("RETURN"))
    public EnvironmentAttributeSystem.Builder injectAttributes(EnvironmentAttributeSystem.Builder environmentAttributes) {
        return Injector.inject(getThis(), environmentAttributes);
    }

    @Unique
    private ClientLevel getThis() {
        return (ClientLevel) (Object) this;
    }

//    @Shadow
//    @Final
//    private Minecraft minecraft;
//
//    @ModifyExpressionValue(method = "getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getTimeOfDay(F)F")
//    )
//    public float modifySkyColor(float original) {
//        ClientLevel level = (ClientLevel) (Object) this;
//        BlockPos position = minecraft.gameRenderer.getMainCamera().getBlockPosition();
//        if(WeatherGateClient.SUN_IN_BOX_LOCATIONS.containsKey(level.dimension())) {
//            for (BlockPos blockPos : WeatherGateClient.SUN_IN_BOX_LOCATIONS.get(level.dimension())) {
//                if(RangeUtils.isWithinRange(blockPos, position, 64)) {
//                    return 0;
//                }
//            }
//        }
//        return original;
//    }
//
//    @ModifyExpressionValue(method = "getSkyDarken(F)F",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getTimeOfDay(F)F")
//    )
//    public float modifySkyDarken(float original) {
//        ClientLevel level = (ClientLevel) (Object) this;
//        BlockPos position = minecraft.gameRenderer.getMainCamera().getBlockPosition();
//        if(WeatherGateClient.SUN_IN_BOX_LOCATIONS.containsKey(level.dimension())) {
//            for (BlockPos blockPos : WeatherGateClient.SUN_IN_BOX_LOCATIONS.get(level.dimension())) {
//                if(RangeUtils.isWithinRange(blockPos, position, 64)) {
//                    return 0;
//                }
//            }
//        }
//        return original;
//    }
//
//    @ModifyExpressionValue(method = "getCloudColor",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getTimeOfDay(F)F")
//    )
//    public float modifyCouldColor(float original) {
//        ClientLevel level = (ClientLevel) (Object) this;
//        BlockPos position = minecraft.gameRenderer.getMainCamera().getBlockPosition();
//        if(WeatherGateClient.SUN_IN_BOX_LOCATIONS.containsKey(level.dimension())) {
//            for (BlockPos blockPos : WeatherGateClient.SUN_IN_BOX_LOCATIONS.get(level.dimension())) {
//                if(RangeUtils.isWithinRange(blockPos, position, 64)) {
//                    return 0;
//                }
//            }
//        }
//        return original;
//    }
}
