package com.reales;

import processing.core.PApplet;

public class Spike {
    float x;
    float x2;
    float y;
    String id;

    Spike(int x, int y){
        this.x =  PApplet.round( x /40)*40;
        this.x2 =  PApplet.round( x /40)*40;
        this.id = "spi";
        this.y =  PApplet.round( y /40)*40 + 29;

    }

    void display(){
        boolean dead = true;
        for (Player player : Game.players
                ) {
            dead = dead && player.dead;
        }

        if(!dead && Game.home==0){
            this.x-= (float) (10/1.4);
        }
        if(Game.home==1||dead){
            this.x=this.x2;

        }
        Game.processing.fill(0,0,0);
        Game.processing.rectMode( Game.processing.CENTER);
        Game.processing.stroke(255);
        Game.processing.strokeWeight(1);
        Game.processing.triangle(this.x-20, this.y+20, this.x+20, this.y+20, this.x, this.y-20);
        Game.processing.triangle(this.x-20, this.y+20, this.x+20, this.y+20, this.x, this.y-20);
        Game.processing.noStroke();

        /*if(Game.player.y > this.y-39 && Game.player.y < this.y + 39&&Game.player.x > this.x-25 && Game.player.x < this.x + 25){
            Game.player.dead=true;
        }*/

        for (Player player:Game.players
             ) {
            if(player.y > this.y-39 && player.y < this.y + 39&&player.x > this.x-25 && player.x < this.x + 25){
                player.dead=true;
            }
        }

    }
}
