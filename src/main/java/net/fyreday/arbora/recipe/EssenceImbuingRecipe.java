package net.fyreday.arbora.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffect;
import net.fyreday.arbora.ImbuingEffects.ImbuingEffects;
import net.fyreday.arbora.registries.ModRegistries;
import net.fyreday.arbora.util.ArboraEnums;
import net.fyreday.arbora.util.InternalLocationContainer;
import net.fyreday.arbora.util.Location;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

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
            return base.test(inv.getItem(0)) && inv.getLocation().inRectRange(this.getLocation().getX(), this.getLocation().getY(), range);
            }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        //copied frm
        if(!(pContainer instanceof InternalLocationContainer)) {
            return ItemStack.EMPTY;
        }
        InternalLocationContainer inv = (InternalLocationContainer) pContainer;
        ItemStack itemstack = pContainer.getItem(0);
        if (this.base.test(itemstack)) {
            if (inv.getLocation().inRectRange(this.getLocation().getX(), this.getLocation().getY(), range)) {

                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(1);
                ArboraEnums.ValidImbuingArmor type = ArboraEnums.ValidImbuingArmor.getArmorType((ArmorItem)itemstack1.getItem());
                if(type == null){
                    return ItemStack.EMPTY;
                }

                if(itemstack1.getItem() instanceof ArmorItem && imbuingEffect.validArmor().containsKey(type)){

                    Holder<ImbuingEffect> imbuingEffectHolder = ModRegistries.IMBUINGEFFECTS.getHolder(imbuingEffect).get();
                    itemstack1.getOrCreateTag().put("Imbued", ImbuingEffect.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, pRegistryAccess), imbuingEffectHolder).result().orElseThrow());
                }
            }
        }

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
            ImbuingEffect imbuingEffect = ModRegistries.IMBUINGEFFECTS.getValue(new ResourceLocation(Arbora.MOD_ID, "imbuing_speed"));
            ImbuingEffect.fromJson(GsonHelper.getNonNull(pSerializedRecipe, "effect"));

            return new EssenceImbuingRecipe(base, imbuingEffect, loc, pRecipeId);
        }

        @Override
        public @Nullable EssenceImbuingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            ImbuingEffect imbuingEffect = ModRegistries.IMBUINGEFFECTS.getHolder(pBuffer.readResourceLocation()).get().get();
            Ingredient base = Ingredient.fromNetwork(pBuffer);
            int x = pBuffer.readInt();
            int y = pBuffer.readInt();
            return new EssenceImbuingRecipe(base, imbuingEffect,new Location(x,y), pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EssenceImbuingRecipe pRecipe) {

            pBuffer.writeResourceLocation(ModRegistries.IMBUINGEFFECTS.getKey(pRecipe.imbuingEffect));
            pRecipe.base.toNetwork(pBuffer);

            pBuffer.writeInt(pRecipe.getLocation().getX());
            pBuffer.writeInt(pRecipe.getLocation().getY());
        }
    }
}
