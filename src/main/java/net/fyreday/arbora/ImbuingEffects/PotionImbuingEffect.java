package net.fyreday.arbora.ImbuingEffects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;

public class PotionImbuingEffect implements ImbuingEffectFunction{
    private MobEffectInstance mobEffectInstance;
    public PotionImbuingEffect(MobEffectInstance effectInstance){
        this.mobEffectInstance = effectInstance;
    }
    @Override
    public void runEffect(Player player, Level level) {
        player.addEffect(mobEffectInstance);
    }
}
