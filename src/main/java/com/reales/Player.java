package com.reales;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import processing.core.PApplet;

import java.util.*;

public class Player {
    private MultiLayerNetwork brain;
    private final int noInputs = 14;
    private final int noOutputs = 2;
    float x = 75;
    float y = Game.processing.height - 100;
    float yVel = 0;
    boolean dead = false;
    boolean air = false;
    boolean jumping = false;
    float rot = 0;
    float xR = 0;
    float jumpst = 0;
    float c = 0;
    float speed = 10;

    int score;

    Player() {
        restart();
        this.createBrain();
        this.brain.init();
    }

    Player(INDArray weights) {
        restart();
        this.createBrain();
        this.brain.init(weights, true);
    }

    private void createBrain() {
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .list()
                .layer(0, new DenseLayer.Builder().nIn(this.noInputs).nOut(this.noInputs*2).activation(Activation.RELU).build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY).activation(Activation.SOFTMAX).nIn(this.noInputs*2).nOut(this.noOutputs).build())
                .build();
        this.brain = new MultiLayerNetwork(config);
    }

    void move() {
        if (!dead) {
            this.xR += (this.speed);
            this.score = (int) (this.xR / 40);
            //System.out.println(this.score);
        }
        this.y += this.yVel;
        Game.processing.stroke(0);

        if (this.y > Game.processing.height - 140) {
            //this.y -= this.yVel;
            this.y = Game.processing.height - 140;
            this.yVel = 0;
            this.air = false;
            this.jumping = false;
            this.rot = 0;
        } else if (this.air) {
            this.yVel += 1.3;
            this.y= Math.min(this.y + this.yVel, Game.processing.height - 140);
            //while (this.y > Game.processing.height - 140) {
            //    this.y -= 0.1;
           // }
        }

        if (this.dead) {
            //Game.home = 1;
            boolean dead = true;
            for (Player player : Game.players
            ) {
                dead = dead && player.dead;
            }
            if(dead){
                Game.home = 1;

                this.dead = false;
                this.y = Game.processing.height - 100;
                this.rot = 0;
                Game.placeOY = PApplet.round(Game.placeOY / 40);
                Game.placeO = PApplet.round(Game.placeO / 40);
                this.xR = 0;
                this.yVel = 0;
                this.c += (float) (60) / 30;
                if (this.c > 60) {
                    this.dead = false;
                }
            }


        } else {
            this.c = 0;
        }
    }

    public int getFitness() {
        return score;
    }

    public void setFitness(int score) {
        this.score = score;
    }

    public void act(float[] inputs) {
        float[] outputs = this.brain.output(Nd4j.create(new float[][] { inputs })).toFloatVector();
        if (outputs[1] > outputs[0]) {
            this.jump();
        }
    }

    public Player[] crossover(Player parent2){
        Player player1=this;

        long numOfWeights = player1.brain.numParams();

        INDArray weights1 = Nd4j.create(1,numOfWeights);
        INDArray weights2 = Nd4j.create(1,numOfWeights);

        for(int i=0;i<numOfWeights;i++){
            if(i<Math.floor(numOfWeights/2)){
                weights1.putScalar(0, i, player1.brain.params().getScalar(i).getFloat(0));
                weights2.putScalar(0, i, parent2.brain.params().getScalar(i).getFloat(0));
            }else{
                weights1.putScalar(0, i, parent2.brain.params().getScalar(i).getFloat(0));
                weights2.putScalar(0, i, player1.brain.params().getScalar(i).getFloat(0));
            }
        }
        return new Player[]{new Player(weights1), new Player(weights2)};
    }

    public void mutate(float v){
        /*INDArray weights = this.brain.params();
        for(int i=0;i<weights.length();i++){
            float w = weights.getFloat(i);
            if(Math.random()<v){
                w += Math.random()*0.1-0.05;
            }
            weights.putScalar(i, w);
        }
        this.brain.setParams(weights);*/
        for (int i = 0; i < this.brain.numParams(); i ++) {
            if (Math.random() < v) {
                this.brain.params().putScalar(i, Math.random() * 2 - 1);
            }
        }
    }

    ArrayList<Float> getInputs() {
        //Float[] closerObstacles = new Float[12];
        Map<Float,Object> closerObstacles = new HashMap<>();
        for (Spike spike:Game.spikes)
              {
                if (spike.x > this.x) {
                    float distance = spike.x - this.x;
                    closerObstacles.put(distance,spike);
                }
        }
        for (Block block:Game.blocks)
        {
            if (block.x > this.x) {
                float distance = block.x - this.x;
                closerObstacles.put(distance,block);
            }
        }
        /*Map<Float,Block> closerBlocks = new HashMap<>();
        for (Block block:Game.blocks)
        {
            if (block.x > this.x) {
                float distance = block.x - this.x;
                closerBlocks.put(distance,block);
            }
        }*/
        List<Float> sortedObstacles = new ArrayList<>(closerObstacles.keySet());

        Collections.sort(sortedObstacles);
        int maxObstacles = Math.min(sortedObstacles.size(), 6);
        ArrayList<Float> inputs = new ArrayList<>();
        for (int i = 0; i < maxObstacles; i ++) {
            float distance = sortedObstacles.get(i);
            Object obstacle = closerObstacles.get(distance);
            if(obstacle instanceof Spike){
                Spike spike = (Spike) obstacle;
                inputs.add((float) spike.x-this.x);
                inputs.add((float) spike.y-this.y);
            }else if(obstacle instanceof Block){
                Block block = (Block) obstacle;
                inputs.add((float) block.x-this.x);
                inputs.add((float) block.y-this.y);
            }
        }
        inputs.add((float) this.y);
        inputs.add((float) this.yVel);
        return inputs;
    }
    void display() {
        if (!this.dead) {
            Game.processing.translate(75, this.y);
            ArrayList<Float> temp = this.getInputs();
            //angleMode("DEGREES");
            float[] inputs = new float[this.noInputs];
            for (int i = 0; i < inputs.length; i ++) {
                inputs[i] = temp.get(i) ;
            }
            if (Game.processing.keyPressed || Game.processing.mousePressed) {
                jump();

            }
            this.act(inputs);
            this.air = false;
            //Esta linea hace que gire siempre

            Game.processing.rectMode(Game.processing.CENTER);
            if (this.jumping) {
                this.rot += 0.07F;
                Game.processing.rotate(this.rot);
            }
            Game.processing.fill(0, 0, 125);
            Game.processing.noStroke();
            Game.processing.rect(0, 0, 40, 40);
        }
    }

    void restart() {
        this.x = 75;
        this.y = Game.processing.height - 140;
        this.yVel = 0;
        this.dead = false;
        this.air = true;
        this.rot = 0;
        this.xR = 0;
        this.jumpst = 0;
        this.c = 0;
        this.speed = 10;
        this.jumping = false;
    }

    void jump() {
        if (!this.air) {
            this.yVel = -13;
            this.air = true;

        }
        jumpst = Game.processing.frameCount;
        jumping = true;
    }
}
