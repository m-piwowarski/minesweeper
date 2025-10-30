import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JFrame implements LevelTransporter {
    private JComboBox difficulty;
    private JTextField heightTextField;
    private JTextField widthTextField;
    private JComboBox rev;
    private JLabel lh;
    private JLabel lw;
    private JLabel ld;
    private JLabel lr;
    private JPanel mainpanel;
    private JButton button1;
    private int diff;

    public Test(){
        setLayout(new GridLayout(2,2));
        setSize(250,250);
        setLocationRelativeTo(this);
        setContentPane(mainpanel);

        difficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diff = (int) difficulty.getSelectedItem();
            }
        });

        rev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        heightTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public int getDifficulty() {
        return diff;
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
