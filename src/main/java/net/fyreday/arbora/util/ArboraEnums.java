package net.fyreday.arbora.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class ArboraEnums {

    public enum SapType implements StringRepresentable {
        PURE("pure"),
        TERRA("terra"),
        AERO("aero"),
        PYRO("pyro"),
        CRYRO("cryro"),
        AQUA("aqua");

        private final String name;
        SapType(String name){this.name = name;}

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
