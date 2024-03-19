import javax.swing.*;
import java.awt.*;

public class WW3_prewiew {
    private JFrame frame;
    private JPanel panel;
    private BattleshipGame game;

    public WW3_prewiew() {
        game = new BattleshipGame(); // Создаем новую игру

        frame = new JFrame("WW3 Preview");
        panel = new JPanel();

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            try {
                game.startGame(); // Здесь добавьте код для начала игры
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        panel.setLayout(new BorderLayout());
        panel.add(startButton, BorderLayout.NORTH);
        panel.add(exitButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(640, 360);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new WW3_prewiew();
    }
}