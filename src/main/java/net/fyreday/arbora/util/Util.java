package net.fyreday.arbora.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Util {


    public static List<BlockPos> getAllNeighboringPos(BlockPos blockPos){
        List<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(blockPos.above());
        blockPosList.add(blockPos.below());
        blockPosList.add(blockPos.north());
        blockPosList.add(blockPos.south());
        blockPosList.add(blockPos.east());
        blockPosList.add(blockPos.west());
        blockPosList.add(blockPos.north().above());
        blockPosList.add(blockPos.south().above());
        blockPosList.add(blockPos.east().above());
        blockPosList.add(blockPos.west().above());
        blockPosList.add(blockPos.north().below());
        blockPosList.add(blockPos.south().below());
        blockPosList.add(blockPos.east().below());
        blockPosList.add(blockPos.west().below());
        blockPosList.add(blockPos.north().above().east());
        blockPosList.add(blockPos.south().above().west());
        blockPosList.add(blockPos.east().above().south());
        blockPosList.add(blockPos.west().above().north());
        blockPosList.add(blockPos.north().below().east());
        blockPosList.add(blockPos.south().below().west());
        blockPosList.add(blockPos.east().below().south());
        blockPosList.add(blockPos.west().below().north());
        blockPosList.add(blockPos.north().east());
        blockPosList.add(blockPos.south().west());
        blockPosList.add(blockPos.east().south());
        blockPosList.add(blockPos.west().north());

        return blockPosList;
    }

    public static int getIntFromColor(Color c){
        int rbgByBytes;
        int a = c.getAlpha();
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        a <<= 24;
        r <<= 16; //shift 16 bits left
        g <<= 8;//shift 8 bits left
        rbgByBytes = a | r | g | b; // OR operator all 3 channels.
        return rbgByBytes;
    }
    public static Vector3f getVectorFromColor(Color c){
        return new Vector3f(c.getRed(),c.getGreen(),c.getGreen());
    }
}
