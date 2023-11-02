package net.fyreday.arbora.util;

public class Location {
    private final int x;
    private final int y;

    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean inRectRange(int testx, int testy, int range){
        return (testx >= x - range && testx <= x + range) && (testy >= y - range && testy <= y + range);
    }
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
