package count;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.*;

public class InfoFrame extends JFrame{
	JPanel panel;
	StringBuilder sb;
	Map<Date, Long> timeOfDays = new HashMap<Date, Long>();
	double salaryPerHour;

	public  InfoFrame(){}

	public InfoFrame(Map<Date, Long> timeOfDays, double salaryPerHour) {
		this.timeOfDays = timeOfDays;
		this.salaryPerHour = salaryPerHour;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				panel = null;
			}
		});

		panel = new JPanel();
		
		JLabel label = new JLabel(getText());

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				panel = null;
			}
		});

		panel.add(label);

		JPanel southPanel = new JPanel();
		southPanel.add(okButton);

		add(panel);
		add(BorderLayout.SOUTH, southPanel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
	}

	public String getText(){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Map.Entry<Date, Long> temp;
		sb = new StringBuilder();
		sb.append("<html> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Time " +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Salary<br>");

		Iterator<Map.Entry<Date, Long>> it = timeOfDays.entrySet().iterator();

		while (it.hasNext()){
			temp = (Map.Entry<Date, Long>) it.next();
			double salary = salaryPerHour * temp.getValue() / 1000.0 / 60 / 60;
			sb.append(new SimpleDateFormat("dd-MM-yyyy").format(temp.getKey())
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + df.format(temp.getValue())
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp" + String.format("%8.2f", salary) + "<br><br>");
		}
		sb.append("</html>");
		return sb.toString();
	}
}
