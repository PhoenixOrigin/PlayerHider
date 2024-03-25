package net.phoenix.playerhider.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.phoenix.playerhider.PlayerHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "collectPlayerEntries", at = @At("HEAD"), cancellable = true)
    private void collectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
        List<PlayerListEntry> entries = cir.getReturnValue();
        for (PlayerListEntry entry : entries) {
            if (PlayerHider.blockedPlayerChecked(entry.getDisplayName().getString())) {
                entries.remove(entry);
            }
        }
        cir.setReturnValue(entries);
    }

}
