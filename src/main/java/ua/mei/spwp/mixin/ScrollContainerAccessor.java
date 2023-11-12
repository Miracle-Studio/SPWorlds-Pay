package ua.mei.spwp.mixin;

import io.wispforest.owo.ui.container.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ScrollContainer.class)
public interface ScrollContainerAccessor {
    @Accessor("scrollbaring")
    void setScrollbaring(boolean scrollbaring);
}
