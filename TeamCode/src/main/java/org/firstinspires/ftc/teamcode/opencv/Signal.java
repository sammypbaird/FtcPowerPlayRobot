package org.firstinspires.ftc.teamcode.opencv;

public enum Signal {
    ONE(1, "Signal 1", "green",86),
    TWO(2, "Signal 2", "purple",125),
    THREE(3, "Signal 3", "orange",5);

    private final int id;
    private final String name;
    private final String color;
    private final int hue;

    Signal(int id, String name, String color, int hue) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.hue = hue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getHueMax(){
        return hue + 10;
    }
    public int getHueMin(){
        return hue - 10;
    }

}
