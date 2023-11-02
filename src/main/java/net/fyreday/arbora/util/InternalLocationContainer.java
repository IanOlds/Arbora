package net.fyreday.arbora.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class InternalLocationContainer extends SimpleContainer {
    private Location location;

    public InternalLocationContainer(Location location,int pSize) {
        super(pSize);
        this.location = location;

    }

    public InternalLocationContainer(Location location,ItemStack... pItems) {
        super(pItems);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
