package net.fyreday.arbora.util;

import net.fyreday.arbora.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ArboraEnums {

    public enum SapType implements StringRepresentable {
        PURE("pure", new Color(12, 12, 24)),
        TERRA("terra",new Color(22, 75, 0)),
        AERO("aero", new Color(203, 255, 24, 255)),
        PYRO("pyro",new Color(255, 81, 0)),
        CRYRO("cryro",new Color(0, 255, 255)),
        AQUA("aqua",new Color(84, 149, 255)),
        CHAOS("chaos",new Color(45, 37, 37)),
        ORDER("order",new Color(162, 201, 197)),
        MIND("mind",new Color(200, 0, 255)),
        SPIRIT("spirit",new Color(242, 171, 255));


        private final String name;
        private final Color color;
        SapType(String name,Color color){
            this.name = name;
            this.color = color;}

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        public Color getColor(){
            return color;
        }

        public static SapType getSapByBiome(Level pLevel, BlockPos pPos){

            if(pLevel.getBiome(pPos).is(Tags.Biomes.IS_PEAK)){
                return TERRA;
            }
            if(pLevel.getBiome(pPos).is(Tags.Biomes.IS_WET_OVERWORLD)){
                return AQUA;
            }
            if(pLevel.getBiome(pPos).is(Tags.Biomes.IS_HOT_OVERWORLD)){
                return PYRO;
            }
            if(pLevel.getBiome(pPos).is(BiomeTags.IS_NETHER)){
                return SPIRIT;
            }
            if(pLevel.getBiome(pPos).is(BiomeTags.IS_END)){
                return MIND;
            }
            if(pLevel.getBiome(pPos).is(Tags.Biomes.IS_COLD_OVERWORLD)){
                return CRYRO;
            }
            if(pLevel.getBiome(pPos).is(Tags.Biomes.IS_SPARSE_OVERWORLD) || pLevel.getBiome(pPos).is(Tags.Biomes.IS_PLAINS)){
                return AERO;
            }
            if(pLevel.getBiome(pPos).is(Tags.Biomes.IS_DENSE_OVERWORLD)){
                return TERRA;
            }
            return AQUA;
        }
    }
}
