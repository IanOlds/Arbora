package net.fyreday.arbora.ImbuingEffects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fyreday.arbora.registries.ModRegistries;
import net.fyreday.arbora.util.ArboraEnums;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public record ImbuingEffect(String assetName, Holder<MobEffect> effect, Map<ArboraEnums.ValidImbuingToolType, Boolean> validTools, Map<ArboraEnums.ValidImbuingArmor, Boolean> validArmor, Component description) {
    public static final Codec<ImbuingEffect> DIRECT_CODEC = RecordCodecBuilder.create((imbuingEffectInstance) -> {
        return imbuingEffectInstance.group(Codec.STRING.fieldOf("asset_name").forGetter(ImbuingEffect::assetName),
                RegistryFixedCodec.create(Registries.MOB_EFFECT).fieldOf("mobEffect").forGetter(ImbuingEffect::effect),
                Codec.unboundedMap(ArboraEnums.ValidImbuingToolType.CODEC, Codec.BOOL).optionalFieldOf("valid_imbuing_tools", Map.of()).forGetter(ImbuingEffect::validTools),
                Codec.unboundedMap(ArboraEnums.ValidImbuingArmor.CODEC, Codec.BOOL).optionalFieldOf("valid_imbuing_armor", Map.of()).forGetter(ImbuingEffect::validArmor),
                ExtraCodecs.COMPONENT.fieldOf("description").forGetter(ImbuingEffect::description)).apply(imbuingEffectInstance, ImbuingEffect::new);
    });
    public static final Codec<Holder<ImbuingEffect>> CODEC = RegistryFileCodec.create(ModRegistries.Keys.IMBUINGEFFECTS, DIRECT_CODEC);

    public static ImbuingEffect create(String pAssetName, MobEffect mobEffect, Component pDescription,Map<ArboraEnums.ValidImbuingToolType, Boolean> validTools, Map<ArboraEnums.ValidImbuingArmor, Boolean> validArmor) {
        return new ImbuingEffect(pAssetName, BuiltInRegistries.MOB_EFFECT.wrapAsHolder(mobEffect),validTools,validArmor , pDescription);
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    public static Optional<ImbuingEffect> getImbuingEffect(RegistryAccess pRegistryAccess, ItemStack pItem) {
        if (pItem.getTag() != null && pItem.getTag().contains("Imbued")) {
            CompoundTag compoundtag = pItem.getTagElement("Imbued");
            Holder<ImbuingEffect> imbuingEffect = CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, pRegistryAccess), compoundtag).resultOrPartial(LOGGER::error).orElse(null);
            if(imbuingEffect != null) {
                return Optional.of(imbuingEffect.get());
            }
        }
        return Optional.empty();

    }

    public static ImbuingEffect fromJson(@Nullable JsonElement pJson) {
        System.out.println(pJson);
        return fromJson(pJson, true);
    }
    public static ImbuingEffect fromJson(@Nullable JsonElement pJson, boolean pCanBeEmpty) {
        if (pJson != null && !pJson.isJsonNull()) {
            JsonObject obj = (JsonObject)pJson;
            String type = GsonHelper.getAsString(obj, "type", "arbora:imbuingEffect");
            if (type.isEmpty()) {
                throw new JsonSyntaxException("Ingredient type can not be an empty string");
            }

            if(obj.has("imbuingEffect")){
                String s = GsonHelper.getAsString(obj, "imbuingEffect");
                ImbuingEffect ret = ModRegistries.IMBUINGEFFECTS.getValue(ResourceLocation.tryParse(s));
                return ret;
            } else {
                throw new JsonSyntaxException("Expected imbuingEffect");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }
}