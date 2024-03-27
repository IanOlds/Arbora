package net.fyreday.arbora.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fyreday.arbora.Arbora;
import net.fyreday.arbora.recipe.EssenceInfusionRecipe;
import net.fyreday.arbora.util.ArboraEnums;
import net.fyreday.arbora.util.BezierCurve;
import net.fyreday.arbora.util.Location;
import net.fyreday.arbora.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

public class EssenceBrewingScreen extends AbstractContainerScreen<EssenceBrewingMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Arbora.MOD_ID, "textures/gui/brewing_gui.png");
    private static final ResourceLocation SYMBOLS =
            new ResourceLocation(Arbora.MOD_ID, "textures/gui/infusion_symbols.png");
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
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        for(int k = 0; k < 2; ++k) {
            double d0 = pMouseX - (double)(286 + 42*k);
            double d1 = pMouseY - (double)(230);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 32.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, k)) {
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, k);
                return true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
//        guiGraphics.hLine((x + (156 / 2)), (x + (156 / 2)) + 60, (y + (146 / 2)), 0x404040);
//        guiGraphics.hLine((x + (156 / 2)), (x + (156 / 2)) + 60, (y + (146 / 2))+1, 0x404040);
//        guiGraphics.hLine((x + (156 / 2)), (x + (156 / 2)) + 60, (y + (146 / 2))+2, 0x404040);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - 256) / 2;
        int y = 0;

        guiGraphics.blit(TEXTURE, x, 0, 0, 0, 256, 256);


        renderLocation(guiGraphics, x+5, y+5, 246, 165);
        for(EssenceInfusionRecipe recipe : menu.getAllRecipes()){
            renderRecipe(guiGraphics, x+5, y+5, recipe);
        }
        renderProgressArrow(guiGraphics, x, y);

        if(menu.drawCurve()) {
            drawBezier(guiGraphics, x + (247 / 2), y + (165 / 2), menu.getConsumptionBezier(), menu.getStirProgress());
        }
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(SYMBOLS, 0 + 85, 0 + 30, 176, 0, 8, menu.getScaledProgress());
        }
    }

    private void renderLocation(GuiGraphics guiGraphics, int x,int y, int width, int height){
        Location loc = menu.getLocation();
        guiGraphics.blit(WORLD_TEXTURE, x , y, loc.getX() - 256/2 , loc.getY() - (256 / 2), width, height, 1500,5000);
    }

    private void drawBezier(GuiGraphics guiGraphics, int x, int y, BezierCurve bezierCurve, int progress){
        for (double i = progress; i <= 100; i++) {
            Point2D point = bezierCurve.interpolate(i/100);
            Point2D progressPoint = bezierCurve.interpolate(progress/100);
            guiGraphics.hLine(
                    (int)point.getX() + x - (int)progressPoint.getX(),
                    (int)point.getX()+x - (int)progressPoint.getX(),
                    (int)point.getY() + y - (int)progressPoint.getY(),
                    0xFF000000);
        }
//        for(Point2D point : bezierCurve.getPoints()){
//            guiGraphics.hLine((int)point.getX() + x, (int)point.getX()+x, (int)point.getY() + y, 0xFFFF0000);
//        }
    }
    private void renderRecipe(GuiGraphics guiGraphics, int x, int y, EssenceInfusionRecipe recipe){
        Location loc = recipe.getLocation();
        Location menuLoc = menu.getLocation();

        int pwidth = 30;
        int pheight = 29;
        int xdiff = 0;
        int ydiff = 0;
        int drawx = (x + (247 / 2)) + (loc.getX() - menuLoc.getX())-5;
        int drawy = (y + (165 / 2)) + (loc.getY() - menuLoc.getY())-40;
        int widthbound = x+246 - pwidth;
        int heightbound = y + 165 - pheight;
        if(drawx < x){
            xdiff = drawx - x;
            if(xdiff < -pwidth ){
                return;
            }

            drawx-=xdiff;
            pwidth+=xdiff;
            //System.out.println("width: " + pwidth);

        }
        if(drawx > widthbound){
            xdiff = drawx - widthbound;

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
            //System.out.println("height: " + pheight);
        }
        if(drawy > heightbound){
            ydiff = drawy - heightbound;

            if(ydiff > pheight ){
                return;
            }
            pheight = pheight - ydiff;
            ydiff = 0;
        }
        guiGraphics.blit(SYMBOLS, drawx, drawy, 9 - xdiff, 0- ydiff, pwidth, pheight);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
