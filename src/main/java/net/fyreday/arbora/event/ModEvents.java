package net.fyreday.arbora.event;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffect;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffects;
import net.fyreday.arbora.Modifiers.ModModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = Arbora.MOD_ID)
    public static class ForgeEvents{
        @SubscribeEvent
        public static void LivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
            if(event.getEntity() instanceof Player player){
                if(event.getFrom().hasTag()){
                    try {
                        ImbuingEffect imbuingEffect = ImbuingEffects.getREGISTRY().get().getValue(new ResourceLocation(Arbora.MOD_ID,event.getFrom().getTag().getString("imbued_effect")));
                        player.sendSystemMessage(Component.literal("effect: ").append(imbuingEffect.getAttributeId()));
                        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("minecraft",imbuingEffect.getAttributeId()));
                        player.getAttribute(attribute).removePermanentModifier(imbuingEffect.getAttributeModifierID());
                    }catch (Exception e){
                       // System.out.println("No old effect");
                    }
                }

                if(event.getTo().hasTag()){
                    try {
                        ImbuingEffect imbuingEffect = ImbuingEffects.getREGISTRY().get().getValue(new ResourceLocation(Arbora.MOD_ID,event.getTo().getTag().getString("imbued_effect")));
                        AttributeModifier attributeModifier = ModModifiers.getModifierFromUUID(imbuingEffect.getAttributeModifierID()).get();
                        player.sendSystemMessage(Component.literal("effect: ").append(imbuingEffect.getAttributeId().toString()));
                        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("minecraft",imbuingEffect.getAttributeId()));

                        if(!player.getAttribute(attribute).hasModifier(attributeModifier)){
                            player.getAttribute(attribute).addPermanentModifier(attributeModifier);
                        }
                    }catch (Exception e){
                       // System.out.println("Failed new effect");
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Arbora.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
    }
}
