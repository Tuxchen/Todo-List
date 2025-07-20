package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utilities.LM;
import utilities.Priority;

public class ToDo extends JPanel {
	private int id;
	private String name;
	private Date start;
	private Date end;
	private boolean isDone;
	private Priority priority;
	
	// GUI-Elemente
	private JButton update;
	private JButton watch;
	private JButton done;
	private JButton delete;
	
	public ToDo(int id, String name, Date start, Date end, boolean isDone, Priority priority) {
		this.id = id;
		this.name = name;
		this.start = start;
		this.end = end;
		this.isDone = isDone;
		this.priority = priority;
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 0, 0, 0));
		
		setBackground(new Color(188, 210, 238));
		
		JLabel title = new JLabel(name);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		
		if(priority == Priority.LOW) {
			title.setForeground(Color.blue);
		} else if(priority == Priority.MIDDLE) {
			title.setForeground(Color.yellow);
		} else {
			title.setForeground(Color.red);
		}
				
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		update = new JButton(LM.getValue("update"));
		
		watch = new JButton(LM.getValue("watch"));
		
		done = new JButton(LM.getValue("done"));
		
		delete = new JButton(LM.getValue("delete"));
		
		controls.add(update);
		controls.add(watch);
		controls.add(done);
		controls.add(delete);
		
		add(BorderLayout.CENTER, title);
		add(BorderLayout.SOUTH, controls);
	}
	
	public void setUpdateText(String update) {
		this.update.setText(update);
	}
	
	public void setWatchText(String watch) {
		this.watch.setText(watch);
	}
	
	public void setDoneText(String done) {
		this.done.setText(done);
	}
	
	public void setDeleteText(String delete) {
		this.delete.setText(delete);
	}
}
