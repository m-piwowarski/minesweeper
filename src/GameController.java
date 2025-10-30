import javax.swing.*;

public class GameController extends JFrame implements LevelTransporter {
    Visuals vis;
    public GameController(Visuals vis){
        this.vis = vis;


    }

    @Override
    public int getDifficulty() {
        return 0;
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public int getCols() {
        return 0;
    }

    @Override
    public int getRevealOnStart() {
        return 0;
    }
}

interface LevelTransporter {
    int getDifficulty();
    int getRows();
    int getCols();
    int getRevealOnStart();
}