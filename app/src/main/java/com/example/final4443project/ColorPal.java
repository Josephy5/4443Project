package com.example.final4443project;
/**
 * One of the classes/code within the colorpicker package that the painting app's main activity uses
 *
 * Reason why it is here as a java class instead being imported in gradle is because the gradle system
 * couldn't import it from the gradle setting, stating that it is not there. Thus, we had to import the
 * whole package here as project files
 *
 * Code below is based on the github repo for the Kristiyan Petrov's colorpicker package
 * https://github.com/kristiyanP/colorpicker
 */
public class ColorPal {
    private int color;
    private boolean check;

    public ColorPal(int color, boolean check) {
        this.color = color;
        this.check = check;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ColorPal && ((ColorPal) o).color == color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
