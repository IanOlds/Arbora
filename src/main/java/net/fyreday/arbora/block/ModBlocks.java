package net.fyreday.arbora.block;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.custom.EssenceBrewingStationBlock;
import net.fyreday.arbora.block.custom.MagicalLog;
import net.fyreday.arbora.block.custom.ModFlammableRotatedPillarBock;
import net.fyreday.arbora.fluid.ModFluids;
import net.fyreday.arbora.item.ModItems;
import net.fyreday.arbora.worldgen.tree.MagicalTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Arbora.MOD_ID);

    public static final RegistryObject<Block> MAGICAL_LOG = registerBlock("magical_log",
        () -> new MagicalLog(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MAGICAL_LOG_DEAD = registerBlock("magical_log_dead",
        () -> new ModFlammableRotatedPillarBock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MAGICAL_WOOD = registerBlock("magical_wood",
        () -> new ModFlammableRotatedPillarBock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STRIPPED_MAGICAL_LOG = registerBlock("stripped_magical_log",
        () -> new ModFlammableRotatedPillarBock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STRIPPED_MAGICAL_WOOD = registerBlock("stripped_magical_wood",
        () -> new ModFlammableRotatedPillarBock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MAGICAL_PLANKS = registerBlock("magical_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> MAGICAL_LEAVES = registerBlock("magical_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final RegistryObject<LiquidBlock> TERRA_ESSENCE_FLUID_BLOCK = BLOCKS.register("terra_essence_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_TERRA_ESSENCE, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final RegistryObject<Block> MAGICAL_SAPLING = registerBlock("magical_sapling",
            () -> new SaplingBlock(new MagicalTreeGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> ESSENCE_BREWING_STATION = registerBlock("essence_brewing_station",
            () -> new EssenceBrewingStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
