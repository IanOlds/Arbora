package net.fyreday.arbora.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BezierCurve {
    private Point2D.Float[] points;

    public BezierCurve(Point2D.Float[] points)  {
        if (points.length == 4){
            this.points = points;

        }else{
            this.points = new Point2D.Float[4];
            for(int i = 0; i < 4; i++){
                if(i > points.length){
                    this.points[i] = new Point2D.Float(0,0);
                    continue;
                }
                this.points[i] = points[i];

            }
        }
    }
    public BezierCurve(ArrayList<Vector2D> points)  {
        this.points = new Point2D.Float[4];
        for(int i = 0; i < 4; i++){
            if(i > points.size()){
                this.points[i] = new Point2D.Float(0,0);
                continue;
            }
            this.points[i] = new Point2D.Float(((float) points.get(i).getX()),(float)points.get(i).getY());;

        }
    }

    public BezierCurve(Point2D.Float point0,Point2D.Float point1,Point2D.Float point2,Point2D.Float point3) {
        this.points = new Point2D.Float[4];
        this.points[0] = point0;
        this.points[1] = point1;
        this.points[2] = point2;
        this.points[3] = point3;
    }
    public Point2D.Float interpolate(double t){
        Point2D.Float point = new Point2D.Float();
        point.setLocation(
        Math.pow(1 - t, 3) * points[0].x + 3 * t * Math.pow(1 - t, 2) * points[1].x + 3 * t * t * (1 - t) * points[2].x + Math.pow(t, 3) * points[3].x,
        Math.pow(1 - t, 3) * points[0].y + 3 * t * Math.pow(1 - t, 2) * points[1].y + 3 * t * t * (1 - t) * points[2].y + Math.pow(t, 3) * points[3].y
        );
        return point;
    }

    public Point2D.Float[] getPoints(){
        return points;
    }

    public static CompoundTag getSerializedBezier(BezierCurve bezierCurve){
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < 4; i++)
        {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putFloat("X", bezierCurve.getPoints()[i].x);
            itemTag.putFloat("Y", bezierCurve.getPoints()[i].y);
            nbtTagList.add(itemTag);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Beziers", nbtTagList);
        nbt.putInt("Size", 1);
        return nbt;
    }
    public static CompoundTag getEmptySerializedBezier(){
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Size", 0);
        return nbt;
    }

    public static BezierCurve deserializeNBT(CompoundTag nbt)
    {
        int size = nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : 0;
        if(size == 0){
            return null;
        }
        Point2D.Float[] points = new Point2D.Float[4];
        ListTag tagList = nbt.getList("Beziers", Tag.TAG_COMPOUND);
        for (int i = 0; i < 4; i++)
        {
            CompoundTag pointTags = tagList.getCompound(i);
            float x = pointTags.getFloat("X");
            float y = pointTags.getFloat("Y");
            points[i] = new Point2D.Float();
            points[i].setLocation(x,y);
        }
        return new BezierCurve(points);
    }
}
