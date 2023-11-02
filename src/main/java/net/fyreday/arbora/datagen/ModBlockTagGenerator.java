package net.fyreday.arbora.datagen;

import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Arbora.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
               // .add(ModBlocks.BLOCKHERE)
                .addTags(Tags.Blocks.ORES);

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.MAGICAL_LOG.get(),
                        ModBlocks.MAGICAL_PLANKS.get(),
                        ModBlocks.MAGICAL_LOG_DEAD.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.MAGICAL_LOG.get());
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.MAGICAL_LOG.get(),
                        ModBlocks.MAGICAL_WOOD.get(),
                        ModBlocks.STRIPPED_MAGICAL_LOG.get(),
                        ModBlocks.STRIPPED_MAGICAL_WOOD.get());
        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.MAGICAL_PLANKS.get());
    }
}
