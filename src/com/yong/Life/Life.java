package com.yong.Life;

import java.util.Random;

public class Life {
    private final Universe universe;
    private final Cell[][] preGen;
    private int genNumber;

    public Life(Universe universe) {
        this.genNumber = 0;
        this.universe = universe;
        this.preGen = new Cell[universe.dimension][universe.dimension];
    }

    public Universe getUniverse() {
        return universe;
    }

    public int getGenNumber() {
        return genNumber;
    }

    private void replicateCells(Cell[][] cellsA, Cell[][] cellsB) {
        for (int i = 0; i < cellsA.length; i++) {
            for (int j = 0; j < cellsA[i].length; j++) {
                cellsB[i][j] = new Cell(universe, i, j);
                cellsB[i][j].setLive(cellsA[i][j].isLive());
            }
        }
    }

    public void begins() {
        for (int i = 0; i < universe.dimension; i ++) {
            for (int j = 0; j < universe.dimension; j++) {
                universe.getCells()[i][j].setLive((new Random()).nextBoolean());
            }
        }
        genNumber++;
    }

    public void evolve() {
        Cell[][] curGen = universe.getCells();
        replicateCells(curGen, preGen);
        for (int i = 0; i < universe.dimension; i++) {
            for (int j = 0; j < universe.dimension; j++) {
                Cell preCell = preGen[i][j];
                Cell curCell = curGen[i][j];
                int lnc = preCell.liveNeighbors();
                if (preCell.isLive() && lnc != 2 && lnc != 3) {
                    curCell.setLive(false);
                }
                if (!preCell.isLive() && lnc == 3) {
                    curCell.setLive(true);
                }
            }
        }
        genNumber++;
    }
}
