package net.fyreday.arbora.block.entity;

import net.fyreday.arbora.recipe.DistillRecipe;
import net.fyreday.arbora.recipe.EssenceImbuingRecipe;
import net.fyreday.arbora.screen.DistillMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArboricDistillerBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(6);
    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT_1 = 2;
    private static final int OUTPUT_SLOT_2 = 3;
    private static final int OUTPUT_SLOT_3 = 4;
    private static final int OUTPUT_SLOT_4 = 5;

    protected final ContainerData data;

    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime = 100;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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
        lazyItemHandler = LazyOptional.of(() -> this.itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyItemHandler.invalidate();
    }

    public ArboricDistillerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ARBORIC_DISTILLER.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0:
                        return ArboricDistillerBlockEntity.this.litTime;
                    case 1:
                        return ArboricDistillerBlockEntity.this.litDuration;
                    case 2:
                        return ArboricDistillerBlockEntity.this.cookingProgress;
                    case 3:
                        return ArboricDistillerBlockEntity.this.cookingTotalTime;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        ArboricDistillerBlockEntity.this.litTime = pValue;
                        break;
                    case 1:
                        ArboricDistillerBlockEntity.this.litDuration = pValue;
                        break;
                    case 2:
                        ArboricDistillerBlockEntity.this.cookingProgress = pValue;
                        break;
                    case 3:
                        ArboricDistillerBlockEntity.this.cookingTotalTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(this.itemStackHandler.getSlots());
        for (int i = 0; i < this.itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.arbora.arboric_distiller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DistillMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }
    public void tick(Level pLevel, BlockPos pPos, BlockState pState){
        boolean wasOn = false;
        ItemStack fuelItemStack = this.itemStackHandler.getStackInSlot(FUEL_SLOT);
        if (isLit()) {
            --litTime;
            wasOn = true;
        }
        if(!isLit()){
            if(canBurn(fuelItemStack)){
                burnFuel(fuelItemStack);
                pLevel.setBlockAndUpdate(this.getBlockPos(), pState.setValue(AbstractFurnaceBlock.LIT, Boolean.TRUE));
            }else if(wasOn){
                pLevel.setBlockAndUpdate(this.getBlockPos(), pState.setValue(AbstractFurnaceBlock.LIT, Boolean.FALSE));
                resetProgress();
            }
        }
        if(isLit()){
            if (hasRecipe()) {
                increaseCraftingProgress();
                setChanged(pLevel, pPos, pState);

                if (hasProgressFinished()) {
                    craftItem();
                    resetProgress();
                }
            }
        }
    }

    private void craftItem() {
        Optional<DistillRecipe> recipe = getCurrentRecipe();

        NonNullList<ItemStack> result = getResultItem(recipe.get());

        this.itemStackHandler.extractItem(INPUT_SLOT, 1, false);

        System.out.println("craft: " + result.toString());
        for (ItemStack itemStack: result) {
            if(itemStack.isEmpty()){
                continue;
            }
            boolean hasinserted = false;
            //insert stackable in matching slots to not waste slots
            for (int i = OUTPUT_SLOT_1; i <= OUTPUT_SLOT_4; i++) {
                if(canInsertInExistingStackInSlot(itemStack, i)){
                    this.itemStackHandler.setStackInSlot(i, new ItemStack(itemStack.getItem(),
                    this.itemStackHandler.getStackInSlot(i).getCount() + itemStack.getCount()));
                    hasinserted = true;
                    System.out.println("insert: " + itemStack.getItem().toString());
                    break;
                }
            }
            if(hasinserted){
                continue;
            }

            //insert new items
            for (int i = OUTPUT_SLOT_1; i <= OUTPUT_SLOT_4; i++) {
                if(!this.itemStackHandler.getStackInSlot(i).isEmpty()){
                    continue;
                }
                if(canInsertItemIntoOutputSlot(itemStack.getItem(), i)){
                    this.itemStackHandler.setStackInSlot(i, new ItemStack(itemStack.getItem(),
                    this.itemStackHandler.getStackInSlot(i).getCount() + itemStack.getCount()));
                    System.out.println("add: " + itemStack.getItem().toString());
                    break;
                }
            }
        }
    }

    private Optional<DistillRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(this.itemStackHandler.getSlots());

        for (int i = 0; i < this.itemStackHandler.getSlots(); i++) {
            inv.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }

        Optional<DistillRecipe> distillRecipe = this.level.getRecipeManager().getRecipeFor(DistillRecipe.Type.INSTANCE, inv, level);
        if(distillRecipe.isPresent()){
            return Optional.of(distillRecipe.get());
        }
        return Optional.empty();
    }

    private boolean hasRecipe() {
        Optional<DistillRecipe> recipe = getCurrentRecipe();
        if(recipe.isEmpty()){
            return false;
        }
        NonNullList<ItemStack> result = getResultItem(recipe.get());
        int needHowManySlots = 0;
        for ( ItemStack itemStack: result) {
            if(itemStack.isEmpty()){
                continue;
            }
            needHowManySlots+=getRequiredSlots(itemStack);
        }
        int emptySlots = 0;
        for (int i = OUTPUT_SLOT_1; i < OUTPUT_SLOT_4; i++) {
            if(this.itemStackHandler.getStackInSlot(i).isEmpty()){
                emptySlots++;
            }
        }
        return needHowManySlots <= emptySlots;
    }

    private int getRequiredSlots(ItemStack itemStack) {
        int matches = 0;
        int insert=0;
        for (int i = OUTPUT_SLOT_1; i <= OUTPUT_SLOT_4; i++) {
            if(canInsertItemIntoOutputSlot(itemStack.getItem(), i) ){
                matches++;
                if(canInsertAmountIntoOutputSlot(itemStack.getCount(), i)){
                    insert++;
                }
            }
        }
        if(insert > 0){
            return 0;
        }
        return 1;
    }

    private boolean canInsertItemIntoOutputSlot(Item item, int slot) {
        return this.itemStackHandler.getStackInSlot(slot).isEmpty() || this.itemStackHandler.getStackInSlot(slot).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count, int slot) {
        return this.itemStackHandler.getStackInSlot(slot).getCount() + count <= this.itemStackHandler.getStackInSlot(slot).getMaxStackSize();
    }

    private boolean canInsertInExistingStackInSlot(ItemStack itemStack, int slot){
        if(!(this.itemStackHandler.getStackInSlot(slot).is(itemStack.getItem()) &&
                canInsertItemIntoOutputSlot(itemStack.getItem(), slot) &&
                (this.itemStackHandler.getStackInSlot(slot).getMaxStackSize() - this.itemStackHandler.getStackInSlot(slot).getCount() >= itemStack.getCount())
        )){
            return false;
        }
        return canInsertAmountIntoOutputSlot(itemStack.getCount(), slot);
    }

    private NonNullList<ItemStack> getResultItem(DistillRecipe recipe) {
        return recipe.getResultItems(null);
    }

    private boolean hasProgressFinished() {
        return cookingProgress > cookingTotalTime;
    }

    private void increaseCraftingProgress() {
        cookingProgress++;
    }

    private void resetProgress() {
        cookingProgress = 0;
    }

    private boolean canBurn(ItemStack fuelItemStack) {
        return getFuelTime(fuelItemStack) > 0;
    }

    private void burnFuel(ItemStack fuelItemStack) {
        litTime = getFuelTime(fuelItemStack);
        litDuration = litTime;

        if (fuelItemStack.hasCraftingRemainingItem()) {
            this.itemStackHandler.setStackInSlot(1, fuelItemStack.getCraftingRemainingItem());
        }else if (!fuelItemStack.isEmpty()) {
            Item item = fuelItemStack.getItem();
            fuelItemStack.shrink(1);
            if (fuelItemStack.isEmpty()) {
                this.itemStackHandler.setStackInSlot(1, fuelItemStack.getCraftingRemainingItem());
            }
        }

    }

    private int getFuelTime(ItemStack fuelItemStack) {
        return ForgeHooks.getBurnTime(fuelItemStack, RecipeType.SMELTING);
    }




    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", this.itemStackHandler.serializeNBT());
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.litDuration = ForgeHooks.getBurnTime(this.itemStackHandler.getStackInSlot(FUEL_SLOT), RecipeType.SMELTING);
    }

}
