package com.yong.Life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOfLife extends JFrame implements ActionListener {

    private final JLabel genLabel;
    private final JLabel aliveLabel;
    private  UniversePanel universePanel;
    private final JButton playToggleButton;
    private final JButton resetButton;
    private final JButton stopButton;
    private final JButton setDimButton;
    private final JScrollBar dimensionBar;
    private final ImageIcon playIcon = new ImageIcon(getClass().getResource("/rec/play.png"));
    private final ImageIcon pauseIcon = new ImageIcon(getClass().getResource("/rec/pause.png"));

    private final int defaultDimension = 30;
    private int sleepInterval = 1000;

    private GameController gameController;

    public int getSleepInterval() {
        return sleepInterval;
    }

    public JLabel getGenLabel() {
        return genLabel;
    }

    public JLabel getAliveLabel() {
        return aliveLabel;
    }

    public UniversePanel getUniversePanel() {
        return universePanel;
    }

    public GameOfLife() {
        super("Game of Life");
        this.setIconImage((new ImageIcon(getClass().getResource("/rec/gof.png"))).getImage());

        this.gameController = new GameController(this);

        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(800, 5));
        add(spacePanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 600));
        controlPanel.setLayout(null);
        add(controlPanel, BorderLayout.WEST);

        playToggleButton = makeButton(playIcon, "Play Toggle", "PlayToggleButton", controlPanel);
        playToggleButton.setBounds(10,5, 50, 50);
        playToggleButton.setEnabled(false);

        ImageIcon resetIcon = new ImageIcon(getClass().getResource("/rec/reset.png"));
        resetButton = makeButton(resetIcon, "Reset Game", "ResetButton", controlPanel);
        resetButton.setBounds(75, 5, 50, 50);
        resetButton.setEnabled(false);

        ImageIcon stopIcon = new ImageIcon(getClass().getResource("/rec/stop.png"));
        stopButton = makeButton(stopIcon, "Stop Game", "StopButton", controlPanel);
        stopButton.setBounds(140, 5, 50, 50);
        stopButton.setEnabled(false);

        JLabel dimensionLabel = new JLabel();
        dimensionLabel.setBounds(10, 70, 150, 40);
        dimensionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dimensionLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        dimensionLabel.setText("Universe Dimension:");
        controlPanel.add(dimensionLabel);

        JTextField dimensionField = new JTextField("30", 3);
        dimensionField.setBounds(150, 80, 40, 25);
        dimensionField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        dimensionField.setHorizontalAlignment(SwingConstants.RIGHT);
        dimensionField.setEditable(false);
        controlPanel.add(dimensionField);

        dimensionBar = new JScrollBar(Adjustable.HORIZONTAL, defaultDimension, 1, 3, 151);
        dimensionBar.setBounds(10, 110, 180, 15);
        dimensionBar.addAdjustmentListener(e -> {
            universePanel.setVisible(false);
            gameController.setLife(null);
            dimensionField.setText(Integer.toString(e.getValue()));
        });
        controlPanel.add(dimensionBar);

        setDimButton = new JButton("Initialize Universe");
        setDimButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        setDimButton.setName("Initialize");
        setDimButton.setBounds(10, 135, 180, 40);
        setDimButton.setActionCommand("Initialize");
        setDimButton.addActionListener(e -> {
            dimensionBar.setEnabled("Adjust Dimension".equals(setDimButton.getText()));
            playToggleButton.setEnabled("Initialize Universe".equals(setDimButton.getText()));
            resetButton.setEnabled("Initialize Universe".equals(setDimButton.getText()));
            stopButton.setEnabled("Initialize Universe".equals(setDimButton.getText()));
            setDimButton.setText("Initialize Universe".equals(setDimButton.getText()) ?
                    "Adjust Dimension" : "Initialize Universe");
            if (gameController.getLife() == null) {
                gameController.setLife(new Life(new Universe(dimensionBar.getValue())));
                initGame();
            }
        });
        controlPanel.add(setDimButton);

        genLabel = new JLabel();
        genLabel.setBounds(10, 190, 150, 40);
        genLabel.setHorizontalAlignment(SwingConstants.LEFT);
        genLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        genLabel.setName("GenerationLabel");
        genLabel.setText("Generation #0");
        aliveLabel = new JLabel();
        aliveLabel.setHorizontalAlignment(SwingConstants.LEFT);
        aliveLabel.setBounds(10, 220, 150, 40);
        aliveLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive: 0");
        controlPanel.add(genLabel);
        controlPanel.add(aliveLabel);

        JLabel speedLabel = new JLabel();
        speedLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        speedLabel.setText("Slow     Speed      Fast");
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 200, 3000, sleepInterval);
        speedSlider.setInverted(true);
        speedSlider.setMajorTickSpacing(400);
        speedSlider.setMinorTickSpacing(100);
        speedSlider.setPaintLabels(false);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(l -> {
            JSlider source = (JSlider) l.getSource();
            sleepInterval = source.getValue();
        });
        speedSlider.setBounds(5, 260, 190, 40);
        speedLabel.setBounds(10, 282, 190, 40);
        speedLabel.setHorizontalAlignment(SwingConstants.LEFT);
        controlPanel.add(speedSlider);
        controlPanel.add(speedLabel);

        universePanel = new UniversePanel();
        universePanel.setName("LifeEvolution");
        add(universePanel);
        universePanel.add(description());

        pack();
        setVisible(true);
    }

    private void initGame() {
        SwingWorker<Void, Void> paneWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                universePanel.setVisible(false);
                universePanel.removeAll();
                universePanel.initialize(gameController.getLife().getUniverse().dimension);
                universePanel.repaint();
                genLabel.setText("Generation #0");
                genLabel.repaint();
                aliveLabel.setText("Alive: 0");
                genLabel.repaint();
                return null;
            }

            @Override
            protected void done() {
                universePanel.setVisible(true);
            }
        };
        paneWorker.execute();
    }

    private JButton makeButton (ImageIcon icon, String caption, String name, JPanel jp) {
        JButton jb = new JButton(icon);
        jb.setName(name);
        jb.setMargin(new Insets(0, 0, 0, 0));
        jb.setActionCommand(caption);
        jb.addActionListener(this);
        jp.add(jb);
        return jb;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Play Toggle":
                setDimButton.setEnabled(false);
                if (gameController.getState() == Thread.State.NEW) {
                    gameController.start();
                } else if (gameController.getState() == Thread.State.TERMINATED) {
                    gameController = new GameController(this);
                }
                synchronized (gameController) {
                    gameController.notifyAll();
                    gameController.goOn();
                    playToggleButton.setIcon(gameController.isSuspended() ? playIcon : pauseIcon);
                }
                break;
            case "Reset Game" :
                if (gameController.getLife().getGenNumber() != 0) {
                    gameController.setLife(new Life(new Universe(dimensionBar.getValue())));
                    initGame();
                    playToggleButton.setIcon((playIcon));
                    gameController.pause();
                }
                break;
            case "Stop Game":
                gameController.interrupt();
                gameController = new GameController(this);
                playToggleButton.setIcon((playIcon));
                playToggleButton.setEnabled(false);
                resetButton.setEnabled(false);
                setDimButton.setEnabled(true);
            default:
        }
    }

    private JPanel description() {
        JPanel desPanel = new JPanel();
        desPanel.setLayout(new BoxLayout(desPanel,BoxLayout.Y_AXIS));
        JLabel title = new JLabel();
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        title.setAlignmentX(LEFT_ALIGNMENT);
        title.setText("Conway's Game of Life");
        desPanel.add(title);
        JTextArea description = new JTextArea();
        description.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
        description.setText("The Game of Life, also known simply as Life, is a cellular automaton devised by the\n" +
                "British mathematician John Horton Conway in 1970. It is a zero-player game,\nmeaning that its " +
                "evolution is determined by its initial state, requiring no further\ninput. One interacts with the " +
                "Game of Life by creating an initial configuration and\nobserving how it evolves. It is Turing " +
                "complete and can simulate a universal\nconstructor or any other Turing machine.\n(Source: Wikipedia)");
        description.setEnabled(false);
        description.setAlignmentX(LEFT_ALIGNMENT);
        desPanel.add(description);

        JLabel rules = new JLabel();
        rules.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        rules.setAlignmentX(LEFT_ALIGNMENT);
        rules.setText("Rules");
        desPanel.add(rules);
        JTextArea ruleDetails = new JTextArea();
        ruleDetails.setFont(new Font(Font.MONOSPACED, Font.BOLD, 8));
        ruleDetails.setText("The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of " +
                "square cells, each of \nwhich is in one of two possible states, live or dead, (or populated and " +
                "unpopulated, respectively). Every \ncell interacts with its eight neighbours, which are the cells " +
                "that are horizontally, vertically, or \ndiagonally adjacent. At each step in time, the following " +
                "transitions occur:\n\n" +
                "Any live cell with fewer than two live neighbours dies, as if by underpopulation.\n" +
                "Any live cell with two or three live neighbours lives on to the next generation.\n" +
                "Any live cell with more than three live neighbours dies, as if by overpopulation.\n" +
                "Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.\n\n" +
                "These rules which compare the behavior of the automaton to real life, can be condensed into the " +
                "following:\n\nAny live cell with two or three live neighbours survives.\nAny dead cell with three " +
                "live neighbours becomes a live cell.\nAll other live cells die in the next generation. Similarly, " +
                "all other dead cells stay dead.\nThe initial pattern constitutes the seed of the system. The first " +
                "generation is created by applying the\nabove rules simultaneously to every cell in the seed; births " +
                "and deaths occur simultaneously, and the\ndiscrete moment at which this happens is sometimes called " +
                "a tick. Each generation is a pure function of\nthe preceding one. The rules continue to be applied " +
                "repeatedly to create further generations.\n(Source: Wikipedia)");
        ruleDetails.setEnabled(false);
        ruleDetails.setAlignmentX(LEFT_ALIGNMENT);
        desPanel.add(ruleDetails);

        JLabel insTitle = new JLabel();
        insTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        insTitle.setAlignmentX(LEFT_ALIGNMENT);
        insTitle.setText("Game Instructions");
        desPanel.add(insTitle);
        JTextArea instructions = new JTextArea();
        instructions.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
        instructions.setText("1. Set the dimension of the universe (default to 30, adjustable between 3 and 150).\n" +
                "2. Click \"Initialize Universe\" to initiate game field after setting dimension.\n" +
                "3. The \"Go / Pause\" button start/continue and puse the game of life.\n" +
                "4. The \"Reset\" button stop and reset the game of life.\n" +
                "5. As life evolves, each generation's number and live cells count are displayed\n" +
                "6. The game speed can be adjusted using the Speed slide bar.\n");
        instructions.setEnabled(false);
        instructions.setAlignmentX(LEFT_ALIGNMENT);
        desPanel.add(instructions);
        return desPanel;
    }
}