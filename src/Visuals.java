import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class Visuals extends JFrame {
    Board board;
    int timerTime = 0;
    Thread timer;
    JLabel timerLabel;
    public Visuals(Board board){
        this.board = board;
        setTitle("Minesweeper");
        setSize(new Dimension(board.getColsAmount() * 52, board.getRowsAmount() * 52));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel boardPanel = new JPanel(new GridLayout(board.getRowsAmount(), board.getColsAmount(), 1, 1));
        boardPanel.setBackground(Color.BLACK);

        for (int y = 0; y < board.getRowsAmount(); y++) {
            for (int x = 0; x < board.getColsAmount(); x++) {
                Pole pole = new Pole(y, x);
                pole.setHorizontalAlignment(SwingConstants.CENTER);
                boardPanel.add(pole);
            }
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.X_AXIS));
        controlPanel.setPreferredSize(new Dimension(600,35));

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(new Font("Arial",Font.BOLD,23));
        //timerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        timer = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    timerTime++;
                    timerLabel.setText(((timerTime / 60) >= 10? timerTime/60 : "0"+timerTime/60) + ":" + ((timerTime % 60) >= 10? timerTime%60 : "0"+timerTime%60));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timer.setDaemon(true);
        timer.start();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restart());

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGame());

        controlPanel.add(timerLabel);
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(restartButton);
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(newGameButton);

        getContentPane().add(boardPanel,BorderLayout.CENTER);
        getContentPane().add(controlPanel,BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        Pole.POLA.forEach(Pole::virtualRepaint);
        setVisible(true);
    }
    public void newGame(){
        JDialog restartWindow = new JDialog(this,"New Game");
        restartWindow.setSize(300,280);
        restartWindow.setLocationRelativeTo(this);

        Integer[] difficultyVals = {1, 2, 3, 4, 5, 6, 7, 8};
        JComboBox<Integer> difficulty = new JComboBox<>(difficultyVals);
        difficulty.setSelectedIndex(Main.DIFFICULTY - 1);
        difficulty.setEditable(false);

        JFormattedTextField heightTextField;
        JFormattedTextField widthTextField;
        JFormattedTextField revealField;

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        NumberFormatter nf = new NumberFormatter(format);
        nf.setValueClass(Integer.class);
        nf.setMinimum(5);
        nf.setMaximum(40);
        nf.setAllowsInvalid(true);
        nf.setCommitsOnValidEdit(true);

        NumberFormatter nf2 = new NumberFormatter(format);
        nf2.setValueClass(Integer.class);
        nf2.setMinimum(0);
        nf2.setMaximum(10);
        nf2.setAllowsInvalid(true);
        nf2.setCommitsOnValidEdit(true);

        heightTextField = new JFormattedTextField(nf);
        widthTextField = new JFormattedTextField(nf);
        revealField = new JFormattedTextField(nf);
        heightTextField.setValue(Main.ROWS);
        widthTextField.setValue(Main.COLS);
        revealField.setValue(Main.REVEAL_ON_START);

        JLabel lh = new JLabel("Height: (5-40)");
        JLabel lw = new JLabel("Width: (5-40)");
        JLabel ld = new JLabel("Difficulty:");
        JLabel lr = new JLabel("Reveal on start: (0-10)");
        JPanel mainPanel = new JPanel(new GridLayout(5,2,20,30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(lh);
        mainPanel.add(heightTextField);
        mainPanel.add(lw);
        mainPanel.add(widthTextField);
        mainPanel.add(ld);
        mainPanel.add(difficulty);
        mainPanel.add(lr);
        mainPanel.add(revealField);

        JButton okButton = new JButton("Start");
        JButton cancelButton = new JButton("Cancel");
        mainPanel.add(cancelButton);
        mainPanel.add(okButton);

        cancelButton.addActionListener(l -> restartWindow.dispose());
        okButton.addActionListener(l -> {
            timerLabel.setText("00:00");
            try {
                heightTextField.commitEdit();
                widthTextField.commitEdit();
                revealField.commitEdit();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            board = new Board((int) heightTextField.getValue(), (int) widthTextField.getValue(), (int) difficulty.getSelectedItem(), (int) revealField.getValue());
            timerTime = 0;
            Pole.POLA.forEach(Pole::virtualRepaint);
            restartWindow.dispose();
            timer = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        timerTime++;
                        timerLabel.setText(((timerTime / 60) >= 10? timerTime/60 : "0"+timerTime/60) + ":" + ((timerTime % 60) >= 10? timerTime%60 : "0"+timerTime%60));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });

        restartWindow.setContentPane(mainPanel);
        restartWindow.setVisible(true);
    }

    public void restart() {
        timerLabel.setText("00:00");
        board = new Board(board.getRowsAmount(), board.getColsAmount(), board.getDiff(), board.getRev());
        timerTime = 0;
        Pole.POLA.forEach(Pole::virtualRepaint);
        timer = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    timerTime++;
                    timerLabel.setText(((timerTime / 60) >= 10? timerTime/60 : "0"+timerTime/60) + ":" + ((timerTime % 60) >= 10? timerTime%60 : "0"+timerTime%60));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    class Pole extends JLabel {
        static ArrayList<Pole> POLA = new ArrayList<>();
        int y;
        int x;
        public Pole(int y, int x){
            this.y = y;
            this.x = x;

            Font montserrat = new Font("Montserrat",Font.BOLD,18);
            setText("");
            setFont(montserrat);
            setOpaque(true);
            setBackground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(new Color(205,205,205));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.getButton() == MouseEvent.BUTTON3){
                        setFlag();
                    } else if (e.getButton() == MouseEvent.BUTTON1){
                        board.revealArea(y,x);
                        repaintAll();
                    }
                }
            });

            POLA.add(this);
        }

        public void repaintAll() {
            for (Pole p : POLA) {
                if (!board.isFlagged(p.y,p.x)) p.virtualRepaint();
            }
        }

        public void virtualRepaint(){
            setForeground(switch (board.getObjectAt(y, x)){
                case 11 -> Color.RED;
                case 12 -> Color.BLUE;
                case 13 -> new Color(0,130,0);
                case 14 -> new Color(130, 130, 0);
                case 15 -> new Color(255,85,0);
                case 16 -> new Color(140,0,255);
                case 17 -> new Color(0,185,255);
                case 18 -> new Color(255,0,255);
                default -> Color.BLACK;
            });
            setBackground(switch (board.getObjectAt(y,x)){
                case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> new Color(205,205,205); // covered space
                case 19 -> Color.RED;
                default -> Color.WHITE; // revealed
            });
            setText(switch (board.getObjectAt(y, x)){
                case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 -> ""; //not revealed (0-9) or no mines nearby for 10
                case 19 -> "X"; //revealed mine
                default -> String.valueOf(board.getObjectAt(y, x) - 10); //number
            });
        }

        public void setFlag(){
            if (!board.isFlagged(y,x) && board.getObjectAt(y,x) < 10){
                board.setFlag(y,x);
                setForeground(Color.BLACK);
                setText("F");
                setBackground(new Color(120,120,120));
            } else {
                board.setFlag(y,x); //unset flag
                virtualRepaint();
            }
        }
    }
}

