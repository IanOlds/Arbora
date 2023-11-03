package net.fyreday.arbora.block.custom;

import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.block.entity.MagicalLogBlockEntity;
import net.fyreday.arbora.block.entity.ModBlockEntities;
import net.fyreday.arbora.item.ModItems;
import net.fyreday.arbora.util.ArboraBlockProperties;
import net.fyreday.arbora.util.ArboraEnums;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

public class MagicalLog extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(0,0,0, 16,16,16);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final IntegerProperty SAP_LEVEL = ArboraBlockProperties.SAP_LEVEL;
    public static final int MAX_SAP_LEVELS = 5;

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }



    public MagicalLog(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SAP_LEVEL, Integer.valueOf(0)).setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem){

            BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            ((MagicalLogBlockEntity) blockEntity).setAllNeighborsToWither(context.getLevel(),context.getClickedPos());
            if(state.is(ModBlocks.MAGICAL_LOG.get())){
                return ModBlocks.STRIPPED_MAGICAL_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }

            if(state.is(ModBlocks.MAGICAL_WOOD.get())){
                return ModBlocks.STRIPPED_MAGICAL_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MagicalLogBlockEntity) {
                ((MagicalLogBlockEntity) blockEntity).setAllNeighborsToWither(pLevel,pPos);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    public BlockState rotate(BlockState pState, Rotation pRot) {
        return rotatePillar(pState, pRot);
    }

    public static BlockState rotatePillar(BlockState pState, Rotation pRotation) {
        switch (pRotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)pState.getValue(AXIS)) {
                    case X:
                        return pState.setValue(AXIS, Direction.Axis.Z);
                    case Z:
                        return pState.setValue(AXIS, Direction.Axis.X);
                    default:
                        return pState;
                }
            default:
                return pState;
        }
    }
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(AXIS, pContext.getClickedFace().getAxis());
    }
    public void resetSapLevel(Level pLevel, BlockState pState, BlockPos pPos) {
        pLevel.setBlock(pPos, pState.setValue(SAP_LEVEL, Integer.valueOf(0)), 3);
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        int i = pState.getValue(SAP_LEVEL);
        boolean flag = false;
        if (i >= 5) {
            Item item = itemstack.getItem();
            if (itemstack.is(Items.GLASS_BOTTLE)) {
                itemstack.shrink(1);
                pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (itemstack.isEmpty()) {
                    pPlayer.setItemInHand(pHand, new ItemStack(getSapByBiome(pLevel, pPos)));
                } else if (!pPlayer.getInventory().add(new ItemStack(getSapByBiome(pLevel, pPos)))) {
                    pPlayer.drop(new ItemStack(getSapByBiome(pLevel, pPos)), false);
                }

                flag = true;
                pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, pPos);
            }
            if (!pLevel.isClientSide() && flag) {
                pPlayer.awardStat(Stats.ITEM_USED.get(item));
            }
        }
        if(flag){
            this.resetSapLevel(pLevel, pState, pPos);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {

            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }

    public Item getSapByBiome(Level pLevel, BlockPos pPos){
        switch (ArboraEnums.SapType.getSapByBiome(pLevel, pPos)){
            case AERO: {
                return ModItems.AERO_ICHOR.get();
            }
            case AQUA:{
                return ModItems.AQUA_ICHOR.get();
            }
            case MIND:{
                return ModItems.MIND_ICHOR.get();
            }
            case PURE:{
                return ModItems.TERRA_ICHOR.get();
            }
            case PYRO:{
                return ModItems.PYRO_ICHOR.get();
            }
            case CHAOS:{
                return ModItems.CHAOS_ICHOR.get();
            }
            case CRYRO:{
                return ModItems.CRYRO_ICHOR.get();
            }
            case ORDER:{
                return ModItems.ORDER_ICHOR.get();
            }
            case TERRA:{
                return ModItems.TERRA_ICHOR.get();
            }
            case SPIRIT:{
                return ModItems.SPIRIT_ICHOR.get();
            }
            default:{
                return ModItems.AQUA_ICHOR.get();
            }
        }
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MagicalLogBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.MAGICAL_LOG_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));

    }

    @javax.annotation.Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(SAP_LEVEL);
        pBuilder.add(AXIS);
    }
}
