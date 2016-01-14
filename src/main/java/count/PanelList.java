package count;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PanelList extends JPanel implements Serializable{
    private static final long serialVersionUID = 1L;

    List<MyPanel> list = new ArrayList<MyPanel>();
    Point pointMax;
    Point pointMin;

    public PanelList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addNewPanel(MyPanel panel) {
        list.add(panel);
        add(panel.panel);
        if(list.size() < 10)
            Main.go.setMinimumSize(new Dimension(565, (67 + 40 * list.size())));
        else if(list.size() > 10)
            Main.go.setPreferredSize(new Dimension(565, (67 + 40 * 10)));
    }

    public void stopAll(){
        for(MyPanel p : list)
            if(p.isButton)
                p.startButton.doClick();
    }

    public void removePanel (MyPanel panel){
        panel.th.interrupt();
        panel.interrupt();
        list.remove(panel);
        remove(panel.panel);
        if(list.size() == 0)
            Main.go.setMinimumSize(new Dimension(230, 67));
        else if(list.size() < 10){
            Main.go.setPreferredSize(null);
            Main.go.setMinimumSize(new Dimension(565, (67 + 40 * list.size())));
        }

        Main.go.rere();
    }

    public void save(Point pointMin, Point pointMax) {
        try {
            File file = new File("save.f");
            if( ! file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            try{
                oos.writeObject(list);
                oos.writeObject(pointMin);
                oos.writeObject(pointMax);
                oos.flush();
            } finally{
                fos.close();
                oos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            File file = new File("save.f");
            if( ! file.exists())
                return;
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fin);
            try{
                List<MyPanel> newList;
                newList = (List<MyPanel>) ois.readObject();
                pointMin = (Point) ois.readObject();
                pointMax = (Point) ois.readObject();
                list.addAll(newList);
                for(MyPanel p : newList){
                    add(p.panel);
                }
                revalidate();
            } finally{
                fin.close();
                ois.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MyPanel> getMinimizeList() {
        List<MyPanel> result = new ArrayList<MyPanel>();
        for (MyPanel p : list)
            if(p.th.isAlive() && p.isButton == true)
                result.add(p);
        return  result;
    }

    public void refresh(){
        removeAll();

        for(MyPanel p : list){
            p.minMaxRepaint();
            add(p.panel);
        }

    }
}
