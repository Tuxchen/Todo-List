package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utilities.LM;
import utilities.Priority;

public class WatchDialog extends JDialog {
	private Container c;
	private JLabel name;
	private JLabel time;
	private JTextArea description;
	private JLabel doneDisplay;
	private JRadioButton low, middle, high;
	private ToDo todo;
	
	public WatchDialog(JFrame owner, String title, boolean modal, ToDo todo) {
		super(owner, title, modal);
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		this.todo = todo;
		
		JPanel above = new JPanel(new GridLayout(2, 1, 5, 5));
		
		name = new JLabel(todo.getName());
		name.setFont(new Font("Arial", Font.BOLD, 22));
		name.setHorizontalAlignment(JLabel.CENTER);
		
		if(todo.getPriority() == Priority.LOW)
			name.setForeground(Color.blue);
		else if(todo.getPriority() == Priority.MIDDLE)
			name.setForeground(Color.yellow);
		else
			name.setForeground(Color.red);
		
		time = new JLabel(todo.getStart() + " - " + todo.getEnd());
		time.setHorizontalAlignment(JLabel.CENTER);
		
		above.add(name);
		above.add(time);
		
		JPanel middle = new JPanel(new GridLayout(2, 1));
		
		description = new JTextArea(todo.getDescription());
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		JScrollPane sp = new JScrollPane(description);
		
		doneDisplay = new JLabel(todo.isDone() ? LM.getValue("done.watch") : LM.getValue("nodone.watch"));
		doneDisplay.setHorizontalAlignment(JLabel.CENTER);
		doneDisplay.setForeground(Color.darkGray);
		
		middle.add(sp);
		middle.add(doneDisplay);
		
		JPanel below = new JPanel(new GridLayout(1, 3, 5, 5));
		
		low = new JRadioButton(LM.getValue("low.dialog"));
		low.setSelected(todo.getPriority() == Priority.LOW ? true : false);
		low.setEnabled(false);
		
		this.middle = new JRadioButton(LM.getValue("middle.dialog"));
		this.middle.setSelected(todo.getPriority() == Priority.MIDDLE ? true : false);
		this.middle.setEnabled(false);
		
		high = new JRadioButton(LM.getValue("high.dialog"));
		high.setSelected(todo.getPriority() == Priority.HIGH ? true : false);
		high.setEnabled(false);
		
		below.add(low);
		below.add(this.middle);
		below.add(high);
		
		c.add(BorderLayout.NORTH, above);
		c.add(BorderLayout.CENTER, middle);
		c.add(BorderLayout.SOUTH, below);
	}
}
