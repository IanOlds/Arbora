package net.fyreday.arbora.recipe;

import com.google.gson.Gson;
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

public class DistillRecipe implements Recipe<SimpleContainer> {

    private final Ingredient inputItem;
    private final NonNullList<ItemStack> outputItems;
    private final ResourceLocation id;

    public DistillRecipe(Ingredient inputItem, NonNullList<ItemStack> outputItems, ResourceLocation id){
        this.inputItem = inputItem;
        this.outputItems = outputItems;
        this.id = id;
    }
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }
        return inputItem.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return outputItems.get(0);
    }

    public NonNullList<ItemStack> assembleList(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return outputItems;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return outputItems.get(0);
    }
    public NonNullList<ItemStack> getResultItems(RegistryAccess pRegistryAccess) {
        return outputItems;
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

    public static class Type implements RecipeType<DistillRecipe> {
        public static final DistillRecipe.Type INSTANCE = new DistillRecipe.Type();
        public static final String ID = "distilling";
    }

    public static class Serializer implements RecipeSerializer<DistillRecipe>{
        public static final DistillRecipe.Serializer INSTANCE = new DistillRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Arbora.MOD_ID, "distilling");
        @Override
        public DistillRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient"));
            JsonArray outputjson = GsonHelper.getAsJsonArray(pSerializedRecipe, "outputs");

            NonNullList<ItemStack> outputs = NonNullList.withSize(4, ItemStack.EMPTY);

            for (int i = 0; i < outputjson.size(); i++) {
                outputs.set(i, ShapedRecipe.itemStackFromJson(outputjson.get(i).getAsJsonObject()));
            }

            return new DistillRecipe(ingredient, outputs, pRecipeId);
        }

        @Override
        public @Nullable DistillRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);

            NonNullList<ItemStack> outputs = NonNullList.withSize(pBuffer.readInt(), ItemStack.EMPTY);

            for (int i = 0; i < outputs.size(); i++) {
                outputs.set(i, pBuffer.readItem());
            }

            return new DistillRecipe(ingredient, outputs, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DistillRecipe pRecipe) {
            pRecipe.inputItem.toNetwork(pBuffer);

            pBuffer.writeInt(pRecipe.outputItems.size());

            for (int i = 0; i < pRecipe.outputItems.size(); i++) {
                pBuffer.writeItemStack(pRecipe.getResultItems(null).get(i), false);
            }
        }
    }
}
