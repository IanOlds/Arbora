package net.fyreday.arbora.ImbuingEffects;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;


import java.util.UUID;


public class ImbuingEffect{
    private UUID attributeModifierID;
    private String attributeId;

    public ImbuingEffect(String attributeId, UUID attributeID){
        this.attributeModifierID = attributeID;
        this.attributeId = attributeId;
    }

    public UUID getAttributeModifierID() {
        return attributeModifierID;
    }

    public String getAttributeId() {
        System.out.println(attributeId);
        return attributeId.substring(15);
    }
}