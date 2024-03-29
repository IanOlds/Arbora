package net.fyreday.arbora.Modifiers;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ModModifiers {
    public static Optional<AttributeModifier> getModifierFromUUID(UUID uuid){
        ArrayList<AttributeModifier> list = new ArrayList<>();
        list.add(imbuedSpeed);

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(uuid)){
                return Optional.of(list.get(i));
            }
        }
        return Optional.empty();
    }
    public static AttributeModifier imbuedSpeed = new AttributeModifier(UUID.fromString("1b85957f-dbfa-4611-b709-eb40386fb5b7"),"Imbued Speed", 0.2,AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static AttributeModifier imbuedHealth = new AttributeModifier(UUID.fromString("1b85957f-dbfa-4611-b709-eb40386fb5b8"),"Imbued Speed", 1,AttributeModifier.Operation.MULTIPLY_TOTAL);
}
