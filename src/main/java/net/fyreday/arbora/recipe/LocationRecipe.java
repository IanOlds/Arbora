package net.fyreday.arbora.recipe;

import net.fyreday.arbora.util.Location;

public abstract class LocationRecipe {
    private final Location location;

    public LocationRecipe(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
