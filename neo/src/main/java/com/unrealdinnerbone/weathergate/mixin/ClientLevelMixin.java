package com.unrealdinnerbone.weathergate.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.unrealdinnerbone.weathergate.attrubutes.Injector;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
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
}
