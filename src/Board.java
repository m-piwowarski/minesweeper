//import java.util.EventObject;

public class Board {
    private int[][] board;
    private boolean[][] flagged;
    private final int diff;
    private final int rev;
    private GameState gameState;
    public enum GameState {
        RUNNING, FROZEN, GAMEOVER
    }
    public Board(int rows, int cols, int difficulty, int revealAmount) {

        board = new int[rows][cols];
        flagged = new boolean[rows][cols];

        diff = difficulty;
        rev = revealAmount;

        //generate mines
        int mines = (int)(rows * cols * (0.10 + difficulty * 0.04));
        while (mines > 0) {
            int y = (int)(Math.random() * rows);
            int x = (int)(Math.random() * cols);
            if (board[y][x] != 9){
                board[y][x] = 9;
                mines--;
            }
        }

        //generate numbers
        for (int x = 0; x < board[0].length; x++) {
            for (int y = 0; y < board.length; y++) {
                if (!isMine(y,x)) {
                    board[y][x] = minesAround(y, x);
                }
            }
        }

        revealOnStart(revealAmount);
    }
    public void showBoard(){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                switch (board[y][x]){
                    case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> System.out.print("[ ]");
                    case 10, 11, 12, 13, 14, 15, 16, 17, 18 -> System.out.print("[" + (board[y][x] - 10) + "]");
                    case 19 -> System.out.print("[X]");
                }
            }
            System.out.println();
        }
    }
    public void showCheatBoard(){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                switch (board[y][x]){
                    case 9, 19 -> System.out.print("[X]");
                    default -> System.out.print("[" + (board[y][x] - 10) + "]");
                }
            }
            System.out.println();
        }
    }
    public int minesAround(int y, int x){
        int counter = 0;
        for (int yc = -1; yc <= 1; yc++) {
            for (int xc = -1; xc <= 1; xc++) {
                if (!(yc == 0 && xc == 0)){
                    try {
                        if (isMine(y + yc, x + xc)){
                            counter++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored){
                    }
                }
            }
        }
        return counter;
    }
    public boolean isMine(int y, int x){
        return board[y][x] == 9;
    }
    public void revealArea(int y, int x){
        if (board[y][x] == 0) {
            revealSpace(y, x);
            for (int yc = -1; yc <= 1; yc++) {
                for (int xc = -1; xc <= 1; xc++) {
                    if (!(yc == 0 && xc == 0)) {
                        try {
                            if (board[y + yc][x + xc] == 0) {
                                revealArea(y + yc, x + xc);
                            }
                            if (board[y + yc][x + xc] < 9) {
                                revealSpace(y + yc, x + xc);
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                        }
                    }
                }
            }
        } else {
            revealSpace(y, x);
        }
    }
    public int getColsAmount() {
        return board[0].length;
    }

    public int getRowsAmount() {
        return board.length;
    }

    public int getObjectAt(int y, int x) {
        return board[y][x];
    }
    public boolean isFlagged(int y, int x){
        return flagged[y][x];
    }
    public void revealSpace(int y, int x) {
        if (isMine(y,x)){
            gameOver();
        }
        if (board[y][x] < 10) {
            board[y][x] += 10;
        }
    }
    public void revealOnStart(int amount){
        int toReveal = amount;
        while (toReveal > 0) {
            int y = (int)(Math.random() * board.length);
            int x = (int)(Math.random() * board[0].length);
            if (board[y][x] != 9 && board[y][x] < 10){
                revealArea(y, x);
                toReveal--;
            }
        }
    }
    public void setFlag(int y, int x){
        flagged[y][x] = !flagged[y][x];
    }
    public void gameOver(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isMine(i,j)){
                    board[i][j] = 19;
                }
            }
        }
    }
    public int getDiff() {
        return diff;
    }
    public int getRev() {
        return rev;
    }
}

//interface Transporter {
//    int getColsAmount();
//    int getRowsAmount();
//    int getObjectAt(int y, int x);
//}

//class GameOverEvent extends EventObject {
//
//    /**
//     * Constructs a prototypical Event.
//     *
//     * @param source the object on which the Event initially occurred
//     * @throws IllegalArgumentException if source is null
//     */
//    public GameOverEvent(Object source) {
//        super(source);
//    }
//}
//interface GameOverListener {
//    void gameOver(GameOverEvent evt);
//}
//class GameTickEvent extends EventObject {
//
//    /**
//     * Constructs a prototypical Event.
//     *
//     * @param source the object on which the Event initially occurred
//     * @throws IllegalArgumentException if source is null
//     */
//    public GameTickEvent(Object source) {
//        super(source);
//    }
//}
//interface GameTickListener {
//    void gameTick(GameTickEvent evt);
//}