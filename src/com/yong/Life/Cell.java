package com.yong.Life;

class Cell {
    private final int xPos;
    private final int yPos;
    private boolean live;
    private final Universe universe;

    public void setLive(boolean live) {
        this.live = live;
    }

    public Cell(Universe universe, int xPos, int yPos) {
        this.universe = universe;
        this.xPos = xPos;
        this.yPos = yPos;
        this.live = false;
    }

    public int liveNeighbors() {
        int n = universe.dimension;
        int count = 0;
        Cell[][] cells = universe.getCells();
        if (cells[(n + xPos -1) % n][(n + yPos - 1) % n].isLive()) count++;
        if (cells[(n + xPos -1) % n][(n + yPos) % n].isLive()) count++;
        if (cells[(n + xPos -1) % n][(n + yPos + 1) % n].isLive()) count++;
        if (cells[(n + xPos) % n][(n + yPos - 1) % n].isLive()) count++;
        if (cells[(n + xPos) % n][(n + yPos + 1) % n].isLive()) count++;
        if (cells[(n + xPos +1) % n][(n + yPos - 1) % n].isLive()) count++;
        if (cells[(n + xPos +1) % n][(n + yPos) % n].isLive()) count++;
        if (cells[(n + xPos +1) % n][(n + yPos + 1) % n].isLive()) count++;
        return count;
    }

    public boolean isLive() {
        return this.live;
    }
}
