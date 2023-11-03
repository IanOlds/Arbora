package net.fyreday.arbora.block.entity;

import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.block.custom.MagicalLog;
import net.fyreday.arbora.util.ArboraEnums;
import net.fyreday.arbora.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;

public class MagicalLogBlockEntity extends BlockEntity {
    private boolean dying = false;
    private int counter = 0;
    public MagicalLogBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MAGICAL_LOG_BE.get(), pPos, pBlockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("magical_log.dying", dying);
        pTag.putInt("magical_log.counter", counter);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        pTag.getBoolean("magical_log.dying");
        pTag.getInt("magical_log.counter");
    }

    public ArboraEnums.SapType getSapType(){
        return ArboraEnums.SapType.getSapByBiome(this.level, worldPosition);
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(dying){
            counter++;
            if(counter > 10) {
                setAllNeighborsToWither(pLevel, pPos);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.MAGICAL_LOG_DEAD.get().defaultBlockState().setValue(BlockStateProperties.AXIS, pState.getValue(BlockStateProperties.AXIS)));
            }
            return;
        }
        if (pLevel.getRandom().nextDouble() < 0.01D) {
            int sapvalue = pState.getValue(MagicalLog.SAP_LEVEL);
            if (sapvalue < 5) {
                sapvalue++;

            }else {
                return;
            }
            pLevel.setBlockAndUpdate(this.getBlockPos(), pState.setValue(MagicalLog.SAP_LEVEL, sapvalue));
            if(sapvalue == 5) {
                double d0 = (double) pPos.getX() + 0.5D;
                double d1 = (double) pPos.getY();
                double d2 = (double) pPos.getZ() + 0.5D;
                pLevel.playSound(null, d0, d1, d2, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
    public void setAllNeighborsToWither(Level pLevel, BlockPos pPos){
        for (BlockPos b : Util.getAllNeighboringPos(pPos)) {
            BlockEntity blockEntity = pLevel.getBlockEntity(b);
            if (blockEntity instanceof MagicalLogBlockEntity && ((MagicalLogBlockEntity) blockEntity).isNotDying()) {
                ((MagicalLogBlockEntity) blockEntity).setDying();
            }
        }
    }
    private boolean isNotDying() {
        return !dying;
    }

    public void setDying(){
        counter = 0;
        dying = true;
    }
}
