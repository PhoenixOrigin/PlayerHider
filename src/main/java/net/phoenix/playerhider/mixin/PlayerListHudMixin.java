package net.phoenix.playerhider.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.phoenix.playerhider.PlayerHider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayerListHudMixin {
    @Shadow @Final private Set<PlayerListEntry> listedPlayerListEntries;
    @Inject(method = "handlePlayerListAction", at = @At("RETURN"))
    private void getListedPlayerListEntries(PlayerListS2CPacket.Action action, PlayerListS2CPacket.Entry receivedEntry, PlayerListEntry currentEntry, CallbackInfo ci) {
        if(PlayerHider.blockedPlayerChecked(receivedEntry.displayName().getString())) {
            listedPlayerListEntries.remove(currentEntry);
        }
    }

}
