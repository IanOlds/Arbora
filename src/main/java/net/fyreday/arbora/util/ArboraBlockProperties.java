package net.fyreday.arbora.util;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ArboraBlockProperties {
    public static final EnumProperty<ArboraEnums.SapType> SAP_TYPE_ENUM_PROPERTY = EnumProperty.create("saptype", ArboraEnums.SapType.class);
    public static final IntegerProperty SAP_LEVEL = IntegerProperty.create("sap_level", 0, 5);
}
