package ua.mei.spwp.mixin;

import net.minecraft.client.*;
import net.minecraft.client.gui.screen.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import ua.mei.spwp.client.gui.essential.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreen(Screen screen, CallbackInfo ci) {
        if (!(screen instanceof NewPage) && this.currentScreen instanceof NewPage newPage) {
            if (!newPage.closing) {
                MinecraftClient.getInstance().options.getGuiScale().setValue(newPage.oldGuiScale);
                MinecraftClient.getInstance().onResolutionChanged();
            }
        }
    }
}
