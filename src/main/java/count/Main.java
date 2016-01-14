package count;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main extends JFrame {
    PanelList pl = new PanelList();
    MyPanel panel;
    static JTextArea textArea = new JTextArea();
    static Main go = new Main();
    boolean isMinimize;
    MinimizePanel minimizePanel;
    JCheckBox alwaysOnTopCheckBox;
    JButton minimizeButton;
    JButton addButton;
    JPanel northPanel;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Point pointMin;
    Point pointMax;
    InfoFrame infoFrame;
    JScrollPane scrollPane;
    Dimension currentDimension;

    public static void main(String[] args) {
        go.start();
    }

    public void start() {
        setTitle("Salary");
        pl.load();
        pl.refresh();
        pointMin = pl.pointMin;
        pointMax = pl.pointMax;

        scrollPane = new JScrollPane(pl);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setPreferredSize(screen);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                pl.stopAll();
                if (isMinimize)
                    pointMin = getLocation();
                else
                    pointMax = getLocation();

                pl.save(pointMin, pointMax);
                System.exit(0);

            }
        });

        addButton = new JButton(new ImageIcon(Main.class.getResource("/add.gif")));
        addButton.setPreferredSize(new Dimension(37, 22));
        addButton.setBorderPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPanel();
                rere();
                //revalidate();
                //repaint();
                //pack();
            }
        });

        minimizeButton = new JButton(new ImageIcon(Main.class.getResource("/resize01.jpg")));
        minimizeButton.setMargin(new Insets(0, 0, 0, 0));
        minimizeButton.setPreferredSize(new Dimension(27, 28));
        minimizeButton.setBorderPainted(false);
        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if( ! isMinimize){
                    if(pl.getMinimizeList().size() <= 0)
                        return;

                    pointMax = getLocation();
                    minimizePanel = new MinimizePanel(pl.getMinimizeList());
                    minimizePanel.repaint();

                    setContentPane(minimizePanel);
                    minimizeButton.setPreferredSize(new Dimension(0, 10));
                    minimizeButton.setIcon(new ImageIcon(Main.class.getResource("/resize02.jpg")));

                    JPanel temp = new JPanel();
                    temp.add(minimizeButton);
                    getContentPane().add(BorderLayout.EAST, minimizeButton);
                    setMinimumSize(new Dimension(150, 89));
                    pack();
                    if(pointMin == null)
                        setLocation((int)(screen.getWidth() - getWidth()), (int)(screen.getHeight() - getHeight() - 40));
                    else
                    setLocation(pointMin);
                    isMinimize = true;
                } else{
                    pointMin = getLocation();
                    getContentPane().removeAll();
                    getContentPane().setLayout(new BorderLayout());
                    pl.refresh();
                    minMaxRepaint();
                    getContentPane().add(BorderLayout.NORTH, northPanel);
                    getContentPane().add(BorderLayout.CENTER, scrollPane);
                    update(getGraphics());

                    if(pointMax != null)
                        setLocation(pointMax);
                    setMinimumSize(new Dimension(548, (67 + 40 * pl.list.size())));

                    repaint();
                    pack();
                    isMinimize = false;
                }
            }
        });

        alwaysOnTopCheckBox = new JCheckBox("Always on top");
        alwaysOnTopCheckBox.setPreferredSize(new Dimension(150, 20));
        alwaysOnTopCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        alwaysOnTopCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println(getSize());
                if(alwaysOnTopCheckBox.isSelected()){
                    setAlwaysOnTop(true);
                    if(infoFrame != null)
                        infoFrame.setAlwaysOnTop(true);
                } else{
                    setAlwaysOnTop(false);
                    if(infoFrame != null)
                        infoFrame.setAlwaysOnTop(false);
                }

            }
        });

        northPanel = new JPanel();

        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(addButton);
        northPanel.add(alwaysOnTopCheckBox);
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(minimizeButton);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        if(pointMax != null)
            setLocation(pointMax);
        else
            setLocation((int)(screen.getWidth() / 4 - getWidth() / 2), (int)(screen.getHeight() / 3 - getHeight() / 2));

        add(BorderLayout.NORTH, northPanel);
        add(scrollPane);

        System.out.println(pl.list.size());

        if(pl.list.size() > 0)
            setMinimumSize(new Dimension(548, (67 + 40 * pl.list.size())));
        else
            setMinimumSize(new Dimension(230, 67));

        pack();
    }

    public void addPanel() {
        panel = new MyPanel();
        panel.run();
        pl.addNewPanel(panel);
    }

    public void removeP (MyPanel panel){
        pl.removePanel(panel);
    }

    public void rere(){
        revalidate();
        repaint();
        //pack();
    }

    public void minMaxRepaint(){
        northPanel.removeAll();
        minimizeButton.setPreferredSize(new Dimension(27, 28));
        minimizeButton.setIcon(new ImageIcon(Main.class.getResource("/resize01.jpg")));
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(addButton);
        northPanel.add(alwaysOnTopCheckBox);
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(minimizeButton);
        setLocation((int)(screen.getWidth() / 4 - getWidth() / 2), (int)(screen.getHeight() / 3 - getHeight() / 2));
    }

    public void setInfoFrame(InfoFrame infoFrame){
        this.infoFrame = infoFrame;
    }
}
