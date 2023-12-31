package net.fyreday.arbora.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.util.InternalLocationContainer;
import net.fyreday.arbora.util.Location;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EssenceBrewingRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final Location location;
    private final int range = 3;
    private final ResourceLocation id;

    public EssenceBrewingRecipe(NonNullList<Ingredient> inputItems, ItemStack output, Location location, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.location = location;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }
        if(!(pContainer instanceof InternalLocationContainer)) {
            return false;
        }
        InternalLocationContainer inv = (InternalLocationContainer) pContainer;
        return inputItems.get(0).test(inv.getItem(0)) && inv.getLocation().inRectRange(this.location.getX(), this.location.getY(), range);
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public Location getLocation(){
        return location;
    }
    public static class Type implements RecipeType<EssenceBrewingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "essence_brewing";
    }

    public static class Serializer implements RecipeSerializer<EssenceBrewingRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Arbora.MOD_ID, "essence_brewing");
        @Override
        public EssenceBrewingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            JsonObject locObject = GsonHelper.getAsJsonObject(pSerializedRecipe, "location");
            Location loc = new Location(GsonHelper.getAsInt(locObject, "x"), GsonHelper.getAsInt(locObject, "x"));
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new EssenceBrewingRecipe(inputs, output, loc, pRecipeId);
        }

        @Override
        public @Nullable EssenceBrewingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            ItemStack output = pBuffer.readItem();
            int x = pBuffer.readInt();
            int y = pBuffer.readInt();
            return new EssenceBrewingRecipe(inputs, output,new Location(x,y), pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EssenceBrewingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for(Ingredient ingredient : pRecipe.getIngredients()){
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
            pBuffer.writeInt(pRecipe.location.getX());
            pBuffer.writeInt(pRecipe.location.getY());
        }
    }
}
