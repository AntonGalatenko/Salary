package count;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


public class MyPanel extends Thread implements Serializable{
    private static final long serialVersionUID = 1L;

    JPanel panel = new JPanel();
    long startTime;
    long currentTime;
    long pauseTime;
    Date date = new Date();
    double salaryPerHour, salary;
    boolean isButton;
    final JLabel labelSalary = new JLabel("0");
    final JLabel labelTime = new JLabel("00:00:00");
    final JTextField salaryTextField = new JTextField(3);
    final JButton startButton = new JButton(new ImageIcon(Main.class.getResource("/start03.jpg")));
    final JButton removeButton = new JButton(new ImageIcon(Main.class.getResource("/delete.gif")));
    final JButton infoButton = new JButton(new ImageIcon(Main.class.getResource("/info01.jpg")));
    JTextArea nameOfProject;
    ThreadClass th = new ThreadClass();
    Map<Date, Long> timeOfDays = new HashMap<Date, Long>();
    long tempPauseTime;
    Date thisDay;
    String nameOfProjectString;
    InfoFrame infoFrame = new InfoFrame();

    public void run() {
        infoFrame.panel = null;
        labelSalary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                BorderFactory.createEmptyBorder(5,0,5,5)));

        startButton.setPreferredSize(new Dimension(55, 18));
        startButton.setBorderPainted(false);
        startButton.addActionListener(new StartButtonListener());

        nameOfProject = new JTextArea();
        nameOfProject.setColumns(20);
        nameOfProject.setRows(1);
        nameOfProject.setFont(nameOfProject.getFont().deriveFont(15f));
        nameOfProject.addKeyListener(new NameOfProjectKeyListener());

        salaryTextField.setBorder(BorderFactory.createEmptyBorder());
        salaryTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    salaryPerHour = Double.parseDouble(salaryTextField.getText().replaceAll(",", "."));
                    startButton.doClick();
                    salaryTextField.setVisible(false);
                } catch (NumberFormatException e2) {}
            }
        });

        removeButton.setPreferredSize(new Dimension(24, 24));
        removeButton.setBorderPainted(false);
        removeButton.addActionListener(new RemoveButtonListener());

        infoButton.setPreferredSize(new Dimension(20 ,25));
        infoButton.setBorderPainted(false);
        infoButton.addActionListener(new InfoButtonListener());

        JLabel emptyLabel1 = new JLabel();
        emptyLabel1.setPreferredSize(new Dimension(20, 5));
        JLabel emptyLabel2 = new JLabel();
        emptyLabel2.setPreferredSize(new Dimension(6, 5));

        panel.add(removeButton);
        panel.add(emptyLabel1);
        panel.add(infoButton);
        panel.add(emptyLabel2);
        panel.add(labelSalary);
        panel.add(labelTime);
        panel.add(nameOfProject);
        panel.add(startButton);
        panel.add(salaryTextField);
    }

    public String getTime() {
        currentTime = System.currentTimeMillis() - startTime;
        date = new Date(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }

    public String getSalary() {
        double salary = currentTime / 1000.0 / 60 / 60;
        return String.format("%8.2f", salary * salaryPerHour);
    }

    public class StartButtonListener implements ActionListener, Serializable {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(nameOfProjectString);
            try {
                salaryPerHour = Double.parseDouble(salaryTextField.getText().replaceAll(",", "."));
            } catch (NumberFormatException e2) {}

            if (salaryPerHour != 0){
                if(startTime == 0){
                    salaryTextField.setVisible(false);
                    startTime = System.currentTimeMillis();
                    th.start();
                    startButton.setIcon(new ImageIcon(Main.class.getResource("/pause02.jpg")));
                    isButton = true;
                } else if ( ! isButton){
                    tempPauseTime = System.currentTimeMillis();
                    startTime = System.currentTimeMillis() - pauseTime;
                    if( ! th.isAlive())
                        th.start();
                    else
                        th.resume();
                    startButton.setIcon(new ImageIcon(Main.class.getResource("/pause02.jpg")));
                    isButton = true;
                } else if (isButton){
                    addTimeOfThisDay();
                    pauseTime = System.currentTimeMillis() - startTime;
                    th.suspend();
                    startButton.setIcon(new ImageIcon(Main.class.getResource("/start03.jpg")));
                    isButton = false;
                }
            }
        }
    }

    public class RemoveButtonListener implements ActionListener, Serializable {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            Main.go.removeP(returnThis());
        }
    }

    public class NameOfProjectKeyListener extends KeyAdapter implements Serializable {
        private static final long serialVersionUID = 1L;
        @Override
        public void keyReleased(KeyEvent e) {
            nameOfProjectString = nameOfProject.getText();
            Main.go.rere();
        }
    }

    public class InfoButtonListener implements ActionListener, Serializable {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            if(infoFrame.panel == null){
                infoFrame = new InfoFrame(timeOfDays, salaryPerHour);
                infoFrame.setVisible(true);
                Main.go.setInfoFrame(infoFrame);
                if(Main.go.isAlwaysOnTop())
                    infoFrame.setAlwaysOnTop(true);
            } else return;
        }
    }

    public class ThreadClass extends Thread implements Serializable {
        private static final long serialVersionUID = 1L;

        public void run() {
            while ( ! isInterrupted()) {
                labelTime.setText(getTime());
                labelSalary.setText(getSalary());
                Thread.yield();
            }
        }
    }

    public MyPanel returnThis(){
        return this;
    }

    public boolean isThisDay(){
        Iterator<Map.Entry<Date, Long>> key = timeOfDays.entrySet().iterator();

        if( ! key.hasNext()){
            thisDay = new Date();
            return false;
        }

        while (key.hasNext()){
            thisDay = key.next().getKey();
            Date currentDate = new Date();

            if ((thisDay.getYear() == currentDate.getYear()) && (thisDay.getMonth() == currentDate.getMonth())
                    && (thisDay.getDate() == currentDate.getDate()))
                return true;
        }
        thisDay = new Date();
        timeOfDays.put(thisDay, new Long(0));
        return true;
    }

    public void addTimeOfThisDay(){
        if( ! isThisDay())
            timeOfDays.put(thisDay, currentTime);
        else
            timeOfDays.put(thisDay, timeOfDays.get(thisDay) + System.currentTimeMillis() - tempPauseTime);
    }

    public void minMaxRepaint(){
        panel.removeAll();
        JLabel emptyLabel1 = new JLabel();
        emptyLabel1.setPreferredSize(new Dimension(20, 5));
        JLabel emptyLabel2 = new JLabel();
        emptyLabel2.setPreferredSize(new Dimension(6, 5));

        panel.add(removeButton);
        panel.add(emptyLabel1);
        panel.add(infoButton);
        panel.add(emptyLabel2);
        panel.add(labelSalary);
        panel.add(labelTime);
        panel.add(nameOfProject);
        panel.add(startButton);
        panel.add(salaryTextField);

        Main.go.rere();
    }
}
