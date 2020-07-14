package com.yong.Life;

import java.util.Arrays;

public class Universe {
    public final int dimension;
    private final Cell[][] cells;

    public Universe(int dimension) {
        this.dimension = dimension;
        this.cells = new Cell[dimension][dimension];
        initiates();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public long activeCells() {
        return Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .filter(Cell::isLive)
                .count();
    }

    public void initiates() {
        for (int i = 0; i < dimension; i ++) {
            for (int j = 0; j < dimension; j++) {
                cells[i][j] = new Cell(this, i, j);
                cells[i][j].setLive(false);
            }
        }
    }
}
