package com.yong.Life;

public class GameController extends Thread {

    private final GameOfLife gof;
    private Life life;

    private boolean suspended = true;

    public boolean isSuspended() {
        return suspended;
    }

    public void pause() {
        suspended = true;
    }

    public void goOn() {
        suspended = !suspended;
    }

    public Life getLife() {
        return life;
    }

    public void setLife(Life life) {
        this.life = life;
    }

    public GameController(GameOfLife gof) {
        this.gof = gof;
    }

    @Override
    public void run() {

        while (true) {

            synchronized (this) {
                while (suspended) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            if (life.getGenNumber() == 0) {
                life.begins();
            } else {
                life.evolve();
            }

            Thread t = new Thread(() -> {
                gof.getGenLabel().setText("Generation #" + life.getGenNumber());
                gof.getGenLabel().repaint();
                gof.getAliveLabel().setText("Alive: " + life.getUniverse().activeCells());
                gof.getAliveLabel().repaint();
                gof.getUniversePanel().update(life.getUniverse().getCells());
                gof.getUniversePanel().repaint();
            });

            t.start();

            if (isInterrupted()) {
                return;
            }

            try {
                t.join();
                Thread.sleep(gof.getSleepInterval());
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
