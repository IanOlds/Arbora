package net.fyreday.arbora.util;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Vector2D plus(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }
    public Vector2D plus(Double value) {
        return new Vector2D(this.x + value, this.y + value);
    }

    public Vector2D minus(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }
    public Vector2D minus(Double value) {
        return new Vector2D(this.x - value, this.y - value);
    }
    public Vector2D times(Vector2D other) {
        return new Vector2D(this.x * other.x, this.y * other.y);
    }
    public Vector2D times(Double value) {
        return new Vector2D(this.x * value, this.y * value);
    }
    public Vector2D div(Vector2D other) {
        return new Vector2D(this.x / other.x, this.y / other.y);
    }
    public Vector2D div(Double value) {
        return new Vector2D(this.x / value, this.y / value);
    }
    public double distanceTo(Vector2D other){
        return Math.sqrt(Math.pow(this.x - other.x,2) + Math.pow(this.y - other.y,2));
    }

    public double magnitude(Vector2D other){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public boolean equals(Vector2D other) {
        return (x == other.x && y == other.y);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
