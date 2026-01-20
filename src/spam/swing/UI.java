package spam.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class UI {

    static class AnimatedBar extends JComponent {
        private int value = 0;

        public AnimatedBar(int target) {
            setPreferredSize(new Dimension(300, 20));
            Timer timer = new Timer(10, e -> {
                if (value < target) {
                    value++;
                    repaint();
                }
            });
            timer.start();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(60, 60, 60));
            g.fillRect(0, 0, getWidth(), 20);

            g.setColor(new Color(0, 255, 255));
            g.fillRect(0, 0, (int)(getWidth() * (value / 100.0)), 20);
        }
    }

    static class ParticlePanel extends JPanel implements Runnable {
        class Particle {
            int x, y, size, speedX, speedY;
            Color color;
            Particle() {
                Random r = new Random();
                x = r.nextInt(400);
                y = r.nextInt(400);
                size = r.nextInt(5) + 3;
                speedX = r.nextInt(3) - 1;
                speedY = r.nextInt(3) - 1;
                color = new Color(0, 255, 255, r.nextInt(100) + 155);
            }

            void move() {
                x += speedX;
                y += speedY;
                if (x < 0 || x > getWidth()) speedX = -speedX;
                if (y < 0 || y > getHeight()) speedY = -speedY;
            }
        }

        Particle[] particles = new Particle[60];

        public ParticlePanel() {
            for (int i = 0; i < particles.length; i++) {
                particles[i] = new Particle();
            }
            setBackground(Color.BLACK);
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                for (Particle p : particles) p.move();
                repaint();
                try { Thread.sleep(30); }
                catch (InterruptedException e) { System.out.println(e.getMessage());}
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Particle p : particles) {
                g.setColor(p.color);
                g.fillOval(p.x, p.y, p.size, p.size);
            }
        }
    }

    public static void showStatisticsWindow(int total, int spam, int toxic, int both) {

        JFrame frame = new JFrame("Message Statistics");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));  
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Message Statistics");
        title.setForeground(new Color(255, 100, 255));
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel t1 = makeLabel("Total Messages: " + total);
        JLabel t2 = makeLabel("Spam Messages: " + spam);
        JLabel t3 = makeLabel("Toxic Messages: " + toxic);
        JLabel t4 = makeLabel("Spam & Toxic Messages: " + both);
        double spamRate = (spam * 100.0) / total;
        double toxicRate = (toxic * 100.0) / total;

        JLabel t5 = makeLabel("Spam Rate: " + String.format("%.2f", spamRate) + "%");
        JLabel t6 = makeLabel("Toxicity Rate: " + String.format("%.2f", toxicRate) + "%");

        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        panel.add(t1);
        panel.add(t2);
        panel.add(t3);
        panel.add(t4);
        panel.add(t5);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new AnimatedBar((int) spamRate));
        panel.add(t6);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new AnimatedBar((int) toxicRate));

        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private static JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 255, 255));
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    public static void topWordsWindow(List<Map.Entry<String, Integer>> list){
        JFrame frame = new JFrame("Top Words");
        frame.setSize(400, 430);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent bg = new ParticlePanel();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);

        JLabel title = new JLabel("Top Words");
        title.setForeground(new Color(255, 100, 255));
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(15));

        for (Map.Entry<String, Integer> entry : list) {
            JLabel t = makeLabel(entry.getKey() + " = " + entry.getValue());
            panel.add(t);
        }

        frame.setContentPane(bg);
        bg.setLayout(new BorderLayout());
        bg.add(panel, BorderLayout.CENTER);
        //frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
