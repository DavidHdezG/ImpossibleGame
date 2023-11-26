package com.reales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Population {
    private static final RouletteWheelSelection geneticAlgorithm=new RouletteWheelSelection();
    private static final List<Player> creatures= new ArrayList<>();
    private final int noOfPlayers = 20;

    public List<Player> firstPopulation() {

        for (int i = 0; i < noOfPlayers; i++) {
            creatures.add(new Player());
        }
        //creatures = Stream.generate(Dino::new).limit(noOfPlayers).collect(Collectors.toList());
        return creatures;
    }

    public static List<Player> nextPopulation() {
        List<Player> deadPlayers = new ArrayList<>(creatures);
        creatures.clear();

        for (int i = 0; i < deadPlayers.size()/2; i ++) {
            List<Player> parents = geneticAlgorithm.select(deadPlayers, true, 2, new Random());

            Player[] children = parents.get(0).crossover(parents.get(1));

            children[0].mutate((float) 0.05);
            children[1].mutate((float) 0.05);

            creatures.addAll(Arrays.asList(children));
        }
        return creatures;
    }
}
