package net.fyreday.arbora.screen;

import net.fyreday.arbora.block.ModBlocks;
import net.fyreday.arbora.block.entity.EssenceBrewingStationBlockEntity;
import net.fyreday.arbora.recipe.EssenceImbuingRecipe;
import net.fyreday.arbora.recipe.EssenceInfusionRecipe;
import net.fyreday.arbora.recipe.LocationRecipe;
import net.fyreday.arbora.util.BezierCurve;
import net.fyreday.arbora.util.Location;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EssenceBrewingMenu extends AbstractContainerMenu {
    public final EssenceBrewingStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int COMSUMTION_SLOT = 2;
    protected EssenceBrewingMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(6));
    }

    public EssenceBrewingMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data){
        super(ModMenuTypes.ESSENCE_BREWING_MENU.get(), pContainerId);
        checkContainerSize(inv, 3);
        blockEntity = ((EssenceBrewingStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, INPUT_SLOT, 142, 130));
            this.addSlot(new SlotItemHandler(iItemHandler, OUTPUT_SLOT, 183, 130));
            this.addSlot(new SlotItemHandler(iItemHandler, COMSUMTION_SLOT, 142, 166));
        });
        addDataSlots(data);
    }
    public boolean isCrafting() {
        return data.get(INPUT_SLOT) > 0;
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if(pId == 1){
            this.blockEntity.stir();
        }
        if(pId ==0){
            this.blockEntity.grind();
        }

        return true;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public Location getLocation() {
        return new Location(this.data.get(2), this.data.get(3));
    }

    public List<LocationRecipe> getAllRecipes(){
        List<EssenceInfusionRecipe> infusionRecipes = this.level.getRecipeManager().getAllRecipesFor(EssenceInfusionRecipe.Type.INSTANCE);
        List<EssenceImbuingRecipe> imbuingRecipes = this.level.getRecipeManager().getAllRecipesFor(EssenceImbuingRecipe.Type.INSTANCE);

        List<LocationRecipe> recipes = new ArrayList<>();
        recipes.addAll(infusionRecipes);
        recipes.addAll(imbuingRecipes);
        return recipes;
    }
    public boolean drawCurve(){
        return getConsumptionBezier() != null;
    }
    public BezierCurve getConsumptionBezier(){
        return blockEntity.getBrewingCurve();
    }
    public int getStirProgress(){
        return this.blockEntity.getStiringProgress();
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.ESSENCE_BREWING_STATION.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, -32 + l * 18, 130 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, -32 + i * 18, 188));
        }
    }
}
