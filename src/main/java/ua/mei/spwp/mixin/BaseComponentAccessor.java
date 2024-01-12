package ua.mei.spwp.mixin;

import io.wispforest.owo.ui.base.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(BaseComponent.class)
public interface BaseComponentAccessor {
    @Accessor
    boolean getHovered();
}
