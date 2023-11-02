package net.fyreday.arbora.util;

import net.minecraft.core.BlockPos;

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
}
