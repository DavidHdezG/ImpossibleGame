package com.reales;

import java.util.ArrayList;

public class Structure {
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();


    Structure(int tipoEstructura,int desplazamientoX) {
        if (tipoEstructura == 0) {
            this.obstacles.add(new Obstacle("block", 880+desplazamientoX, 289));
            this.obstacles.add(new Obstacle("block", 920+desplazamientoX, 289));
            this.obstacles.add(new Obstacle("block", 960+desplazamientoX, 249));
            this.obstacles.add(new Obstacle("block", 1000+desplazamientoX, 249));
            this.obstacles.add(new Obstacle("block", 1040+desplazamientoX, 249));
            this.obstacles.add(new Obstacle("spike", 1080+desplazamientoX, 229));
        }

        if (tipoEstructura == 1) {
            this.obstacles.add(new Obstacle("spike", 1040+desplazamientoX, 309));
            this.obstacles.add(new Obstacle("spike", 2020+desplazamientoX, 309));
        }

        if (tipoEstructura == 2) {
            this.obstacles.add(new Obstacle("block", 920+desplazamientoX, 289));

            this.obstacles.add(new Obstacle("block", 1080+desplazamientoX, 249));
        }

        if (tipoEstructura == 3) {
            this.obstacles.add(new Obstacle("block", 920+desplazamientoX, 249));

            this.obstacles.add(new Obstacle("block", 1080+desplazamientoX, 289));
        }

        if (tipoEstructura == 4) {
            this.obstacles.add(new Obstacle("spike", 920+desplazamientoX, 309));

            this.obstacles.add(new Obstacle("spike", 1080+desplazamientoX, 309));
        }
    }
}
