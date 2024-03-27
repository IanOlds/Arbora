package net.fyreday.arbora.block.entity;

import net.fyreday.arbora.recipe.BrewableRecipe;
import net.fyreday.arbora.recipe.EssenceInfusionRecipe;
import net.fyreday.arbora.screen.EssenceBrewingMenu;
import net.fyreday.arbora.util.BezierCurve;
import net.fyreday.arbora.util.InternalLocationContainer;
import net.fyreday.arbora.util.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class EssenceBrewingStationBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3);
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int CONSUMPTION_SLOT = 2;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    private int stiringProgress = 0;
    private int stiringMaxProgress = 50;
    private final int stiringMax = 100;
    private BezierCurve brewingCurve;
    private int brewingX = 50;
    private int brewingY = 50;
    private int maxBrewingX = 1500 - (156/2);
    private int maxBrewingY = 1500 - (146/2);

    private int minBrewingX = (156/2);
    private int minBrewingY = (146/2);
    private boolean xIncrease = true;
    private boolean yIncrease = true;

    public EssenceBrewingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ESSENCE_BREWING_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                   case 0 -> EssenceBrewingStationBlockEntity.this.progress;
                   case 1 -> EssenceBrewingStationBlockEntity.this.maxProgress;
                   case 2 -> EssenceBrewingStationBlockEntity.this.brewingX + (int)getStiringPoint().getX();
                   case 3 -> EssenceBrewingStationBlockEntity.this.brewingY + (int)getStiringPoint().getY();
                   case 4 -> EssenceBrewingStationBlockEntity.this.stiringProgress;
                   case 5 -> EssenceBrewingStationBlockEntity.this.stiringMaxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex){
                    case 0 -> EssenceBrewingStationBlockEntity.this.progress = pValue;
                    case 1 -> EssenceBrewingStationBlockEntity.this.maxProgress = pValue;
                    case 2 -> EssenceBrewingStationBlockEntity.this.brewingX = pValue;
                    case 3 -> EssenceBrewingStationBlockEntity.this.brewingY = pValue;
                    case 4 -> EssenceBrewingStationBlockEntity.this.stiringProgress = pValue;
                    case 5 -> EssenceBrewingStationBlockEntity.this.stiringMaxProgress = pValue;
                };
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.arbora.essence_brewing_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new EssenceBrewingMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("essence_brewing_station.progress", progress);
        pTag.putInt("essence_brewing_station.brewing_x", brewingX);
        pTag.putInt("essence_brewing_station.brewing_y", brewingY);
        pTag.putInt("essence_brewing_station.stiringProgress", stiringProgress);
        if(brewingCurve != null) {
            pTag.put("essence_brewing_station.beziers", BezierCurve.getSerializedBezier(brewingCurve));
        }else {
            pTag.put("essence_brewing_station.beziers", BezierCurve.getEmptySerializedBezier());
        }
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("essence_brewing_station.progress");
        brewingX = pTag.getInt("essence_brewing_station.brewing_x");
        brewingY = pTag.getInt("essence_brewing_station.brewing_y");
        stiringProgress = pTag.getInt("essence_brewing_station.stiringProgress");
        brewingCurve = BezierCurve.deserializeNBT(pTag.getCompound("essence_brewing_station.beziers"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (stiringProgress > 0) {

            if (stiringProgress > stiringMaxProgress) {
                brewingX = brewingX + (int)getStiringPoint().getX();
                brewingY = brewingY + (int)getStiringPoint().getY();
                stiringProgress = 0;
                stiringMaxProgress = 50;
                brewingCurve = null;
                this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        } else {
            if (hasBrewable()) {
                BrewItem();
            }
        }
        if (hasRecipe()) {

            increaseCraftingProcess();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
                brewingX = 0;
                brewingY = 0;
                this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        } else {
            resetProgress();
            InternalLocationContainer inventory = new InternalLocationContainer(new Location(brewingX, brewingY), this.itemHandler.getSlots());
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, this.itemHandler.getStackInSlot(i));
            }
            if (inventory.isEmpty()) {

            }
        }
    }
    private void BrewItem() {
        BrewableRecipe recipe = getCurrentBrewable().get();
        this.itemHandler.extractItem(CONSUMPTION_SLOT, 1, false);
        stiringProgress =1;
        brewingCurve = recipe.getBrewPath();
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void stir(){
        if(brewingCurve != null) {
            stiringProgress++;
            System.out.println("x: " + getCurrentPos().getX() + " y: " + getCurrentPos().getY());
        }
    }

    public int getStiringProgress(){
        return stiringProgress;
    }
    public void grind(){
        if(stiringMaxProgress <= stiringMax) {
            stiringMaxProgress++;
        }
    }
    public BezierCurve getBrewingCurve(){
        if(brewingCurve != null){
            //System.out.println("Not null!");
            return brewingCurve;
        }
        return null;
    }
    public boolean hasBrewable() {
        Boolean temp = getCurrentBrewable().isPresent();
        //System.out.println("Has Brewable?: " + temp);
        return temp;

    }

    public Optional<BrewableRecipe> getCurrentBrewable() {

        InternalLocationContainer inventory = new InternalLocationContainer(new Location(brewingX, brewingY),this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
//        for (BrewableRecipe brewableRecipe : this.level.getRecipeManager().getAllRecipesFor(BrewableRecipe.Type.INSTANCE)){
//            System.out.println(Arrays.stream(brewableRecipe.getBezier().getPoints()).count());
//        }
        return this.level.getRecipeManager().getRecipeFor(BrewableRecipe.Type.INSTANCE, inventory, level);
    }

    private Point2D getStiringPoint(){
        if(brewingCurve == null){
            return new Point2D.Float(0,0);
        }
        return brewingCurve.interpolate((double)stiringProgress/stiringMax);
    }

    private Location getCurrentPos(){
        return new Location(brewingX + (int)getStiringPoint().getX(), brewingY + (int)getStiringPoint().getY());
    }
    private void craftItem() {
        Optional<EssenceInfusionRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProcess() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<EssenceInfusionRecipe> recipe = getCurrentRecipe();
        if(recipe.isEmpty()){
            return false;
        }
        ItemStack result = recipe.get().getResultItem(null);
        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<EssenceInfusionRecipe> getCurrentRecipe() {
        InternalLocationContainer inventory = new InternalLocationContainer(getCurrentPos(),this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
//        for (EssenceBrewingRecipe essenceBrewingRecipe : this.level.getRecipeManager().getAllRecipesFor(EssenceBrewingRecipe.Type.INSTANCE)){
//            System.out.println(essenceBrewingRecipe.getResultItem(null));
//        }

        return this.level.getRecipeManager().getRecipeFor(EssenceInfusionRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
}
