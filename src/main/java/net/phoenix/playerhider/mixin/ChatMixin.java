package net.phoenix.playerhider.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.phoenix.playerhider.PlayerHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void addMessage(Text message, CallbackInfo ci) {
        String raw = message.getString();
        String clean = raw.replaceAll("(§[0-9a-fklmnor])", "{$1}");
        if (PlayerHider.blockedPlayerChecked(clean)) {
            ci.cancel();
        }
    }

}
