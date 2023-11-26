package com.reales;

import processing.core.PApplet;

public class App 
{
    public static void main( String[] args )
    {
        String[] procArgs = {"Game"};
        Game app = new Game();
        PApplet.runSketch(procArgs, app);
    }
}
