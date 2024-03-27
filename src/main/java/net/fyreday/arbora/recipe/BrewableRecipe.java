package net.fyreday.arbora.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.util.BezierCurve;
import net.fyreday.arbora.util.InternalLocationContainer;
import net.fyreday.arbora.util.Vector2D;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class BrewableRecipe implements Recipe<SimpleContainer> {

    private final Ingredient input;
    private final BezierCurve brewPath;
    private final ResourceLocation id;

    public BrewableRecipe(Ingredient input, ArrayList<Vector2D> brewPath, ResourceLocation id) {
        this.input = input;
        this.brewPath = new BezierCurve(brewPath);
        this.id = id;
    }

    public BrewableRecipe(Ingredient input, BezierCurve brewPath, ResourceLocation id) {
        this.input = input;
        this.brewPath = brewPath;
        this.id = id;
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
        //System.out.println("I'm " + input.getItems()[0].getItem() + " Inv is: " + inv.getItem(2).getItem()  + " match?: " + input.test(inv.getItem(2)));

        return input.test(inv.getItem(2));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
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

    public BezierCurve getBrewPath(){
        return brewPath;
    }
    @Override
    public boolean isSpecial() {
        return true;
    }
    public static class Type implements RecipeType<BrewableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "essence_brewable";
    }

    public static class Serializer implements RecipeSerializer<BrewableRecipe>{
        public static final BrewableRecipe.Serializer INSTANCE = new BrewableRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Arbora.MOD_ID, "essence_brewable");
        @Override
        public BrewableRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient"));

            JsonArray pathObject = GsonHelper.getAsJsonArray(pSerializedRecipe, "vectors");

            ArrayList<Vector2D> vectors = new ArrayList<>();
            for (int i = 0; i < pathObject.size(); i++) {
                vectors.add(
                    new Vector2D(
                        GsonHelper.getAsFloat(pathObject.get(i).getAsJsonObject(), "x"),
                        GsonHelper.getAsFloat(pathObject.get(i).getAsJsonObject(), "y")
                    )
                );
            }

            return new BrewableRecipe(ingredient, vectors , pRecipeId);
        }

        @Override
        public @Nullable BrewableRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            int size = pBuffer.readInt();
            ArrayList<Vector2D> vectors = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                double x = pBuffer.readDouble();
                double y = pBuffer.readDouble();
                vectors.add(new Vector2D(x, y));
            }
            return new BrewableRecipe(ingredient, vectors, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BrewableRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeInt(4);
            for(Point2D vector : pRecipe.brewPath.getPoints()){
                pBuffer.writeDouble(vector.getX());
                pBuffer.writeDouble(vector.getY());
            }
        }
    }
}
