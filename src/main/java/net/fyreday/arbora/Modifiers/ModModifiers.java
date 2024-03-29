package net.fyreday.arbora.Modifiers;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class ModModifiers {
    public static AttributeModifier imbuedSpeed = new AttributeModifier(UUID.fromString("1b85957f-dbfa-4611-b709-eb40386fb5b7"),"Imbued Speed", 0.2,AttributeModifier.Operation.MULTIPLY_TOTAL);
}
