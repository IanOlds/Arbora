package net.fyreday.arbora.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.recipe.EssenceBrewingRecipe;
import net.fyreday.arbora.util.Location;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EssenceBrewingScreen extends AbstractContainerScreen<EssenceBrewingMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Arbora.MOD_ID, "textures/gui/gem_polishing_station_gui.png");
    private static final ResourceLocation WORLD_TEXTURE =
            new ResourceLocation(Arbora.MOD_ID, "textures/gui/parchement.png");
    public EssenceBrewingScreen(EssenceBrewingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);


        renderLocation(guiGraphics, x, y, imageWidth, (imageHeight/2));
        for(EssenceBrewingRecipe recipe : menu.getAllRecipes()){
            renderRecipe(guiGraphics, x, y, recipe);
        }
        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
        }
    }

    private void renderLocation(GuiGraphics guiGraphics, int x,int y, int width, int height){
        Location loc = menu.getLocation();
        guiGraphics.blit(WORLD_TEXTURE, x , y, loc.getX() - (156 / 2) , loc.getY() - (146 / 2), width, height);


    }

    private void renderRecipe(GuiGraphics guiGraphics, int x, int y,EssenceBrewingRecipe recipe){
        Location loc = recipe.getLocation();
        Location menuLoc = menu.getLocation();

        int pwidth = 30;
        int pheight = 29;
        int xdiff = 0;
        int ydiff = 0;
        int drawx = (x + + (156 / 2)) + (loc.getX() - menuLoc.getX())-5;
        int drawy = (y + (146 / 2)) + (loc.getY() - menuLoc.getY())-40;
        int widthbound = x+imageWidth - pwidth;
        int heightbound = y + (imageHeight/2) - pheight;
        if(drawx < x){
            xdiff = drawx - x;
            if(xdiff < -pwidth ){
                return;
            }

            drawx-=xdiff;
            pwidth+=xdiff;
            System.out.println("width: " + pwidth);

        }
        if(drawx > widthbound){
            xdiff = drawx - widthbound;
            System.out.println(xdiff);
            if(xdiff > pwidth ){
                return;
            }
            pwidth = pwidth - xdiff;
            xdiff = 0;
        }

        if(drawy < y){
            ydiff = drawy - y;
            if(ydiff < -pheight ){
                return;
            }
            drawy-=ydiff;
            pheight+=ydiff;
            System.out.println("height: " + pheight);
        }
        if(drawy > heightbound){
            ydiff = drawy - heightbound;

            if(ydiff > pheight ){
                return;
            }
            pheight = pheight - ydiff;
            ydiff = 0;
        }
        guiGraphics.blit(TEXTURE, drawx, drawy, 184 - xdiff, 0- ydiff, pwidth, pheight);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
