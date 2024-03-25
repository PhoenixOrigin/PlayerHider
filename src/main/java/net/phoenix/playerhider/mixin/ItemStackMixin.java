package net.phoenix.playerhider.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.phoenix.playerhider.PlayerHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void getTooltipMixin(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> tooltip = cir.getReturnValue();
        for (int i = 0; i < tooltip.size(); i++) {
            Text text = tooltip.get(i);
            if (PlayerHider.blockedPlayerChecked(text.getString())) {
                tooltip.set(i, replace(text, PlayerHider.getBlockedPlayer(text.getString()), "Player"));
            }
        }
        cir.setReturnValue(tooltip);
    }
    @Unique
    private static MutableText replace(Text text, String target, String replacement) {
        MutableText mutableText = Text.empty();
        for (Text sibling : text.getSiblings()) {
            String siblingString = sibling.getString();
            if (siblingString.contains(target)) {
                String[] parts = siblingString.split(target, -1);
                MutableText replacedText = Text.empty();
                for (int i = 0; i < parts.length; i++) {
                    replacedText.append(Text.of(parts[i]));
                    if (i < parts.length - 1) {
                        MutableText replacementText = Text.empty();
                        for (Text replacementPart : Text.of(replacement).getWithStyle(sibling.getStyle())) {
                            replacementText.append(replacementPart);
                        }
                        replacedText.append(replacementText);
                    }
                }
                mutableText.append(replacedText);
            } else {
                mutableText.append(sibling);
            }
        }
        return mutableText;
    }

}