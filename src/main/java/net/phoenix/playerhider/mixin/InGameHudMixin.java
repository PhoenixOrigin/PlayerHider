package net.phoenix.playerhider.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.phoenix.playerhider.PlayerHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @ModifyArg(method = "renderScoreboardSidebar", at = @At("HEAD"), index = 1)
    private ScoreboardObjective renderScoreboardSidebar(ScoreboardObjective objective) {
        for(String player : objective.getScoreboard().getKnownPlayers()) {
            if(PlayerHider.blockedPlayerChecked(player)) {
                objective.getScoreboard().getPlayerObjectives(player).forEach((e, obj) -> objective.getScoreboard().removeObjective(e));
            }
        }
        return objective;
    }

}
