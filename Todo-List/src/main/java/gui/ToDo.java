package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import db.ToDoDAO;
import utilities.LM;
import utilities.Priority;
import utilities.ToDoChangeListener;

public class ToDo extends JPanel {
	private int id;
	private String name;
	private String start;
	private String end;
	private String description;
	private boolean isDone;
	private Priority priority;
	
	// GUI-Elemente
	private JButton update;
	private JButton watch;
	private JButton done;
	private JButton delete;
	
	private ToDoChangeListener listener;
	
	public ToDo(int id, String name, String start, String end, String description, boolean isDone, Priority priority, ToDoChangeListener listener) {
		this.id = id;
		this.name = name;
		this.start = start;
		this.end = end;
		this.description = description;
		this.isDone = isDone;
		this.priority = priority;
		
		this.listener = listener;
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 0, 0, 0));
		
		setBackground(new Color(188, 210, 238));
		
		JLabel title = new JLabel(name);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		
		if(isDone) {
			title.setForeground(Color.green);
		}
		else if(priority == Priority.LOW) {
			title.setForeground(Color.blue);
		} else if(priority == Priority.MIDDLE) {
			title.setForeground(Color.yellow);
		} else {
			title.setForeground(Color.red);
		}
				
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		update = new JButton(LM.getValue("todo.update"));
		update.addActionListener(e -> {
			UpdateDialog dialog = new UpdateDialog(null, LM.getValue("title.update"), true, this, listener);
			dialog.setSize(500, 550);
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		});
		
		watch = new JButton(LM.getValue("todo.watch"));
		watch.addActionListener(e -> {
			WatchDialog dialog = new WatchDialog(null, LM.getValue("title.watch"), true, this);
			dialog.setSize(500, 550);
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		});
		
		done = new JButton(LM.getValue("todo.done"));
		done.addActionListener(e -> {
			ToDoDAO.markAsDone(id);
			if(listener != null) {
				listener.onToDoChanged();
			}
		});
		
		delete = new JButton(LM.getValue("todo.delete"));
		delete.addActionListener(e -> {
			ToDoDAO.deleteTodo(id);
			if(listener != null) {
				listener.onToDoChanged();
			}
		});
		
		controls.add(update);
		controls.add(watch);
		controls.add(done);
		controls.add(delete);
		
		add(BorderLayout.CENTER, title);
		add(BorderLayout.SOUTH, controls);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public int getId() {
		return id;
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
