package count;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MinimizePanel extends JPanel{
    List<MyPanel> listMinimize;
    JTextArea nameLabel;
    JPanel panel;

    public MinimizePanel(List<MyPanel> listMinimize) {
        this.listMinimize = listMinimize;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int i = 0;
        for(MyPanel p : listMinimize){
            i++;
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            nameLabel = new JTextArea(p.nameOfProjectString);
            nameLabel.setEditable(false);
            nameLabel.setBackground(panel.getBackground());
            nameLabel.setForeground(Color.BLACK);
            nameLabel.setFont(nameLabel.getFont().deriveFont(15f));
            panel.add(p.labelSalary);
            panel.add(p.labelTime);
            panel.add(nameLabel);
            add(panel);
            if(i < listMinimize.size())
                add(Box.createVerticalStrut(5));
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(screen.getWidth() / 4 - getWidth() / 2), (int)(screen.getHeight() / 3 - getHeight() / 2));
    }
}
