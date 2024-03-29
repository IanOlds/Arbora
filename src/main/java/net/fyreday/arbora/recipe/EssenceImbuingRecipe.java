package net.fyreday.arbora.recipe;

import com.google.gson.JsonObject;
import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffect;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffects;
import net.fyreday.arbora.util.InternalLocationContainer;
import net.fyreday.arbora.util.Location;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EssenceImbuingRecipe extends LocationRecipe implements Recipe<SimpleContainer> {

    private final Ingredient base;
    private final ImbuingEffect imbuingEffect;

    private final int range = 10;
    private final ResourceLocation id;

    public EssenceImbuingRecipe(Ingredient base, ImbuingEffect imbuingEffect, Location location, ResourceLocation id) {
            super(location);
            this.base = base;
            this.imbuingEffect = imbuingEffect;
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
            //System.out.println("check: " + base.test(inv.getItem(0)) + " is in range: " + inv.getLocation().inRectRange(this.getLocation().getX(), this.getLocation().getY(), range));
            return base.test(inv.getItem(0)) && inv.getLocation().inRectRange(this.getLocation().getX(), this.getLocation().getY(), range);
            }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        System.out.println("assemble");
        //copied frm
        if(!(pContainer instanceof InternalLocationContainer)) {
            System.out.println("Wrong Inventory!");
            return ItemStack.EMPTY;
        }
        InternalLocationContainer inv = (InternalLocationContainer) pContainer;
        ItemStack itemstack = inv.getItem(0);
        System.out.println("item at 0: " + itemstack.getItem().getDescription().getString());
        if (this.base.test(itemstack)) {
            System.out.println("Valid Imbue target");
            if (inv.getLocation().inRectRange(this.getLocation().getX(), this.getLocation().getY(), range)) {

                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(1);

                itemstack1.getOrCreateTag().putString("imbued_effect", ImbuingEffects.getREGISTRY().get().getKey(imbuingEffect).getPath());
                try {
                    System.out.println("Item tag: "+ itemstack1.getTag().getString("imbued_effect"));
                }catch (Exception e){
                    System.out.println("Failed");
                }

                return itemstack1;
            }
        }
        //System.out.println("Failed to imbue");
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
            return true;
            }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        ItemStack itemstack = new ItemStack(Items.IRON_CHESTPLATE);
        return itemstack;
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

    public static class Type implements RecipeType<EssenceImbuingRecipe> {
        public static final EssenceImbuingRecipe.Type INSTANCE = new EssenceImbuingRecipe.Type();
        public static final String ID = "essence_brewing";
    }

    public static class Serializer implements RecipeSerializer<EssenceImbuingRecipe> {
        public static final EssenceImbuingRecipe.Serializer INSTANCE = new EssenceImbuingRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Arbora.MOD_ID, "essence_imbuing");
        @Override
        public EssenceImbuingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            JsonObject locObject = GsonHelper.getAsJsonObject(pSerializedRecipe, "location");
            Location loc = new Location(GsonHelper.getAsInt(locObject, "x"), GsonHelper.getAsInt(locObject, "y"));
            Ingredient base = Ingredient.fromJson(GsonHelper.getNonNull(pSerializedRecipe, "base"));
            String imbuedEffect = GsonHelper.getAsString(pSerializedRecipe, "imbued_effect");
            ImbuingEffect imbuingEffect = ImbuingEffects.getREGISTRY().get().getValue(new ResourceLocation(Arbora.MOD_ID, imbuedEffect));

            return new EssenceImbuingRecipe(base, imbuingEffect, loc, pRecipeId);
        }

        @Override
        public @Nullable EssenceImbuingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            UUID uuid = pBuffer.readUUID();
            String string = pBuffer.readUtf();
            ImbuingEffect imbuingEffect = new ImbuingEffect(string,uuid);
            Ingredient base = Ingredient.fromNetwork(pBuffer);
            int x = pBuffer.readInt();
            int y = pBuffer.readInt();
            return new EssenceImbuingRecipe(base, imbuingEffect,new Location(x,y), pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EssenceImbuingRecipe pRecipe) {

            pBuffer.writeUUID(pRecipe.imbuingEffect.getAttributeModifierID());

            pBuffer.writeUtf(pRecipe.imbuingEffect.getAttributeId());

            pRecipe.base.toNetwork(pBuffer);

            pBuffer.writeInt(pRecipe.getLocation().getX());
            pBuffer.writeInt(pRecipe.getLocation().getY());
        }
    }
}
