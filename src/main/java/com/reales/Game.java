package com.reales;
import processing.core.PApplet;

import java.lang.reflect.Array;
import java.util.*;

public class Game extends PApplet{

    public static Game processing;


    public static Object[] lCode = {};
    public static float time=0;
    public static int pst=0;
    public static int placeO=0;
    public static int placeOY=0;
    public static String mode="block";
    public static int home = 0;
    public static float camY = 0;

    public static int randY = 0;
    public static int randX = 0;
    public static int randObstacle = 0;
    public static Set<Integer> scores = new HashSet<>();
    public static ArrayList<Spike> spikes;
    public static ArrayList<Block> blocks;
    public static  Player player;
    public static List<Player> players = new ArrayList<Player>();
    public static ArrayList<String> sMoves;

    public static String[] modes={"block", "spike", "del"};
    public static  String obstacle = "del";
    public static int alivePlayers = 0;
    Population population = new Population();
    static ArrayList<Obstacle> obstacles = new ArrayList<>();
    int generation = 1;
    public void settings(){
        size(1080, 450);
        processing = this;
    }
    public void setup(){
        background(255);
        spikes= new ArrayList<Spike>();
        blocks = new ArrayList<Block>();
        player = new Player();
        sMoves = new ArrayList<String>();


        int tipoAleatorio = 0;


        ArrayList<Structure> estructuras = new ArrayList<Structure>();

        // Agregar elementos existentes
        //obstacles.add(new Obstacle("block", 960, 249));
        //obstacles.add(new Obstacle("block", 1000, 249));
        //obstacles.add(new Obstacle("spike", 1040, 229));



        for (int i=0; i<10000; i+=480) {
            tipoAleatorio = (int) processing.random(0, 5);
            estructuras.add(new Structure(tipoAleatorio, i));
        }




        for (Structure est : estructuras) {
            obstacles = est.obstacles;

            for (Obstacle obs : obstacles) {
                if (obs.type.equals("spike")) {
                    spikes.add(new Spike(obs.x, obs.y));

                    if (obs.y < 259)
                        blocks.add(new Block(obs.x, obs.y+40));
                    //blocks.add(new Block(obs.x-50, obs.y+50));
                    //blocks.add(new Block(obs.x-100, obs.y+50));
                } else if (obs.type.equals("block")) {
                    blocks.add(new Block(obs.x, obs.y));
                }
            }
        }

        players= population.firstPopulation();
        for (Player value : players) {
            value.air = true;
        }
//        player.air=true;
        ground();
    }
    public void draw(){
        if (home==10) {
            background(0);
            fill(255);
            noStroke();
            textAlign(CENTER, CENTER);
            textSize(50);
            text("You beat the level!",  width /2,  height /2);
        }

        if (home==0) {
            push();
            /*if (player.xR>10000) {
                home=10;
            }*/
            Player alivePlayer = getRandomAlivePlayer();
            if(alivePlayer != null){
                if (-alivePlayer.y+height-100>height-100) {
                    if (camY>-alivePlayer.y+height-100+3) {
                        camY-=3;
                    } else {
                        if (camY<-alivePlayer.y+height-100-3) {
                            camY+=3;
                        } else {
                            camY=-alivePlayer.y+height-100;
                        }
                    }
                } else {
                    if (camY>-alivePlayer.y+height-100+3) {
                        camY-=alivePlayer.yVel+30;
                    }
                }
            }
            translate(0, camY);
            background(142, 222, 248);
            ground();
            //pa que flote se pone en false
           // player.air=true;
            for (Player player: players) {
                player.air=true;
            }
            noStroke();
           // player.move();
            for (Player player: players) {
                player.move();
            }
            noStroke();

            for (Block block : blocks) {
                block.display();
            }
            for (Spike spike : spikes) {
                spike.display();
            }

            //player.display();
            for (Player player: players) {
                push();
                player.display();
                pop();

            }
            pop();
            ArrayList<Integer> scoresTemp = new ArrayList<>(scores);
            textSize(20);
            fill(0);
            text("High Score: " + Collections.max(scoresTemp), 5, 20);
            text("Generation: " + generation, 5, 40);
            text("Alive: " + alivePlayers, 5, 60);
            //time=0;
        }
        if (home == 1) {
            for (Player player: players
                 ) {
                player.restart();
            }
            alivePlayers = 0;
            generation++;
            Game.players = Population.nextPopulation();
            time=0;
            home=0;
        }
    }

    public static void addScore(int score){
        scores.add(score);
    }
    Player getRandomAlivePlayer(){
        ArrayList<Player> alivePlayers = new ArrayList<Player>();

        for (Player player: players
             ) {
            if (!player.dead) {
                alivePlayers.add(player);
            }

        }

        if(!alivePlayers.isEmpty()){
            Random rand = new Random();
            return alivePlayers.get(rand.nextInt(alivePlayers.size()));
        }
        return null;
    }
    void ground() {
        fill(0, 0, 0);
        rectMode(CORNER);
        stroke(255);
        strokeWeight(0);
        if (home==1) {
            rect(-10, height-120, width+10, placeOY+150);
            rect(-10, height-120, width+20, placeOY+150);
        } else {
            rect(-10, height-120, width+10, 150);
            rect(-10, height-120, width+20, 150);
        }
    }

}
