package com.reales;

import processing.core.PApplet;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class Block {

    float x = 75;
    float y = Game.processing.height-100;
    float yVel =0;
    boolean dead = false;
    boolean air = false;
    float rot =0;
    float xR = 0;
    float jumpst = 0;
    float c = 0;
    float speed = 10;
    String id;
    float xh;

    Block(int x, int y){
        this.x =  PApplet.round((float) x /40)*40;
        this.y =  PApplet.round((float) y /40)*40 + 29;
        this.id="blo";
        this.xh= PApplet.round((float) x /40)*40;
    }

    void display(){
        boolean dead = true;
        Game.alivePlayers=0;
        for (Player player : Game.players
        ) {
            dead = dead && player.dead;
            Game.alivePlayers=player.dead?Game.alivePlayers:Game.alivePlayers+1;
            Game.addScore(player.score);
        }
        if(!dead && Game.home==0){
            this.x-= (10/1.4);
        }
        if(Game.home==1 || dead){
            this.x = this.xh;
        }

        Game.processing.fill(0,0,0);
        Game.processing.rectMode( Game.processing.CENTER);
        Game.processing.stroke(255);
        Game.processing.strokeWeight(1);
        Game.processing.rect(this.x, this.y, 40, 40);
        Game.processing.noStroke();

        /*if(Game.player.y > this.y-40 && Game.player.y < this.y && Game.player.x > this.x-40 && Game.player.x < this.x+40){
            Game.player.y -= Game.player.yVel;
            Game.player.yVel = 0;
            Game.player.air = false;
        }

        if(Game.player.y > this.y-25 && Game.player.y < this.y+35 && Game.player.x > this.x-39 && Game.player.x < this.x+39){
            Game.player.dead=true;
        }
        while(Game.player.y > this.y-40 && Game.player.y < this.y+20 && Game.player.x > this.x-39 && Game.player.x < this.x+39){
            Game.player.y-= 0.1F;
        }*/

        for (Player player:Game.players
        ) {
            if(player.y > this.y-40 && player.y < this.y && player.x > this.x-40 && player.x < this.x+40){
                player.y -= player.yVel;
                player.yVel = 0;
                player.air = false;
            }
            if(player.y > this.y-25 && player.y < this.y+35 && player.x > this.x-39 && player.x < this.x+39){
                player.dead=true;
            }

            while(player.y > this.y-40 && player.y < this.y+20 && player.x > this.x-39 && player.x < this.x+39){
                player.y-= 0.1F;

            }
        }
    }
}
