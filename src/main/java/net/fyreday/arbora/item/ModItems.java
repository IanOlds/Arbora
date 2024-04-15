package net.fyreday.arbora.item;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.fluid.ModFluids;
import net.fyreday.arbora.item.custom.MetalDetectorItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Arbora.MOD_ID);

    public static final RegistryObject<Item> AQUA_ICHOR = ITEMS.register("aqua_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PYRO_ICHOR = ITEMS.register("pyro_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AERO_ICHOR = ITEMS.register("aero_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TERRA_ICHOR = ITEMS.register("terra_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYRO_ICHOR = ITEMS.register("cryro_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHAOS_ICHOR = ITEMS.register("chaos_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPIRIT_ICHOR = ITEMS.register("spirit_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORDER_ICHOR = ITEMS.register("order_ichor_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIND_ICHOR = ITEMS.register("mind_ichor_bottle",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> AQUA_ESSENCE_BUCKET = ITEMS.register("aqua_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_AQUA_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> AERO_ESSENCE_BUCKET = ITEMS.register("aero_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_AERO_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> CRYRO_ESSENCE_BUCKET = ITEMS.register("cryro_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_CRYRO_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> PYRO_ESSENCE_BUCKET = ITEMS.register("pyro_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_PYRO_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> TERRA_ESSENCE_BUCKET = ITEMS.register("terra_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_TERRA_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> CHAOS_ESSENCE_BUCKET = ITEMS.register("chaos_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_CHAOS_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> MIND_ESSENCE_BUCKET = ITEMS.register("mind_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_MIND_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> ORDER_ESSENCE_BUCKET = ITEMS.register("order_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_ORDER_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> SPIRIT_ESSENCE_BUCKET = ITEMS.register("spirit_essence_bucket",
            () -> new BucketItem(ModFluids.SOURCE_SPIRIT_ESSENCE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));



    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(100)));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
