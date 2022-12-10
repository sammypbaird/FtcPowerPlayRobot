package org.firstinspires.ftc.teamcode.opencv;

public enum Signal {
    ONE(1, "Signal 1", "red"),
    TWO(2, "Signal 2", "yellow"),
    THREE(3, "Signal 3", "blue");

    private final int id;
    private final String name;
    private final String color;

    Signal(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
}
