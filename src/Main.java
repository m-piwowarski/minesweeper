import javax.swing.*;

public class Main {
    static int COLS = 10;
    static int ROWS = 10;
    static int DIFFICULTY = 3;
    static int REVEAL_ON_START = 5;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
        Board board = new Board(ROWS, COLS, DIFFICULTY, REVEAL_ON_START);
        Visuals vis = new Visuals(board);
    }
}