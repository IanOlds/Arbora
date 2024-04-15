package net.fyreday.arbora.datagen.loot;

import net.fyreday.arbora.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.MAGICAL_LOG.get());
        this.dropSelf(ModBlocks.MAGICAL_LOG_DEAD.get());
        this.dropSelf(ModBlocks.MAGICAL_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_MAGICAL_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_MAGICAL_WOOD.get());
        this.dropSelf(ModBlocks.MAGICAL_PLANKS.get());
        this.dropSelf(ModBlocks.MAGICAL_SAPLING.get());
        this.add(ModBlocks.MAGICAL_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.MAGICAL_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.ESSENCE_BREWING_STATION.get());
        this.dropSelf(ModBlocks.ARBORIC_DISTILLER.get());
        this.dropSelf(ModBlocks.AQUA_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.AERO_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.CRYRO_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.PYRO_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.TERRA_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.CHAOS_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.MIND_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.ORDER_ESSENCE_FLUID_BLOCK.get());
        this.dropSelf(ModBlocks.SPIRIT_ESSENCE_FLUID_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
