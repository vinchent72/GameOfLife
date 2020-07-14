package com.yong.Life;

import javax.swing.*;
import java.awt.*;

public class UniversePanel extends JPanel {

    // N * N JPanels in GridLayout, background color to show cells live (BLACK) or dead (WHITE)
    private JPanel[][] cellPanels;

    public UniversePanel() {
        super();
    }

    public void initialize(int dimension) {
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.setLayout(new GridLayout(dimension, dimension));
        cellPanels = new JPanel[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                cellPanels[i][j] = new JPanel();
                cellPanels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                this.add(cellPanels[i][j]);
            }
        }
    }

    public void update(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cellPanels[i][j].setBackground(cells[i][j].isLive() ? Color.BLACK : Color.WHITE);
                cellPanels[i][j].repaint();
            }
        }
    }
}
