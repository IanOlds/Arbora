package net.fyreday.arbora.event;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.Modifiers.ModModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = Arbora.MOD_ID)
    public static class ForgeEvents{
        @SubscribeEvent
        public static void LivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
            if(event.getEntity() instanceof Player player){
                //player.sendSystemMessage(Component.literal("Equipped: ").append(event.getTo().getItem().getDescription()));
                if(event.getTo().is(Items.DIAMOND_BOOTS) && event.getSlot() == EquipmentSlot.FEET){
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(ModModifiers.imbuedSpeed);
                }else if(event.getFrom().is(Items.DIAMOND_BOOTS)){
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removePermanentModifier(ModModifiers.imbuedSpeed.getId());
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Arbora.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
    }
}
