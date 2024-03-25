package net.phoenix.playerhider.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.phoenix.playerhider.PlayerHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Unique
    private static String toString(OrderedText orderedText) {
        StringBuilder builder = new StringBuilder();

        orderedText.accept((index, style, codePoint) -> {
            builder.append(Character.toChars(codePoint));
            return true;
        });

        return builder.toString();
    }

    @ModifyArg(method = "drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;IIIZ)I"), index = 1)
    public OrderedText render(OrderedText text) {
        String raw = toString(text);

        if (PlayerHider.blockedPlayerChecked(raw)) {
            raw = PlayerHider.cleanString(raw);
        }

        return Text.of(raw).asOrderedText();
    }
}