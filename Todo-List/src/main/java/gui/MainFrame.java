package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatLightLaf;

import db.ToDoDAO;
import utilities.LM;
import utilities.Priority;
import utilities.ToDoChangeListener;

public class MainFrame extends JFrame implements ActionListener, ItemListener, ToDoChangeListener {
	private Container c;
	private JMenuBar menuBar;
	private JMenu options;
	private JMenuItem newTodo;
	private PlaceholderTextField searchfield;
	private JComboBox<Locale> languages;
	private JPanel todoList;

	private Locale[] supportedLanguages = { Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH, Locale.ITALY };

	public MainFrame() {
		c = getContentPane();
		c.setLayout(new BorderLayout());

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ToDoDAO.init();
		LM.setLanguage(Locale.GERMAN);

		menuBar = new JMenuBar();

		options = new JMenu(LM.getValue("options"));
		options.setMnemonic(KeyEvent.VK_O);

		newTodo = new JMenuItem(LM.getValue("options.new"));
		newTodo.setMnemonic(KeyEvent.VK_N);
		newTodo.addActionListener(this);

		options.add(newTodo);

		menuBar.add(options);

		setJMenuBar(menuBar);

		searchfield = new PlaceholderTextField(LM.getValue("main.searchfield"));
		searchfield.addActionListener(this);

		languages = new JComboBox<>(supportedLanguages);
		languages.addItemListener(this);

		JPanel above = new JPanel(new GridLayout(1, 3, 5, 5));

		above.add(searchfield);
		above.add(languages);

		todoList = new JPanel();
		todoList.setLayout(new BoxLayout(todoList, BoxLayout.Y_AXIS));
		todoList.setBackground(Color.lightGray);
		JScrollPane sp = new JScrollPane(todoList);

		loadTodos();

		c.add(BorderLayout.NORTH, above);
		c.add(BorderLayout.CENTER, sp);

		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			SwingUtilities.invokeLater(() -> checkForUpcomingDeadLines());
		}, 0, 1, TimeUnit.MINUTES);
	}

	private void checkForUpcomingDeadLines() {
		for (int i = 0; i < todoList.getComponentCount(); i++) {
			Component comp = todoList.getComponent(i);

			if (comp instanceof ToDo) {
				ToDo todo = (ToDo) comp;

				String endDateStr = todo.getEnd();
				try {
					Date endDate = new SimpleDateFormat("dd.MM.yyyy").parse(endDateStr);
					Date now = new Date();
					long diff = endDate.getTime() - now.getTime();

					long hoursLeft = TimeUnit.MILLISECONDS.toHours(diff);

					if (hoursLeft >= 0 && hoursLeft <= 20) {
						todo.setBackground(Color.magenta);
						todo.setToolTipText(LM.getValue("todo.reminder"));

						if (todo.getClientProperty("warned") == null) {
							JOptionPane.showMessageDialog(this, todo.getName() + " " + LM.getValue("todo.onereminder"),
									LM.getValue("todo.oneremindertitle"), JOptionPane.WARNING_MESSAGE);
							todo.putClientProperty("warned", true);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void loadTodos() {
		ArrayList<ArrayList<Object>> results = ToDoDAO.selectAllTodos();

		for (var row : results) {
			ToDo todo = new ToDo((Integer) row.get(0), // Die ID
					(String) row.get(1), // Der Name bzw. die Bezeichnung
					(String) row.get(2), // Das Startdatum
					(String) row.get(3), // Das Enddatum
					(String) row.get(4), // Die Beschreibung
					(Boolean) row.get(5), // Ob erledigt oder nicht (done)
					Priority.valueOf((String) row.get(6)), // Die Priorität
					this); // Der Listener der darauf reagiert, wenn an der Todo-Liste sich etwas verändert
							// hat
			todo.setBorder(new LineBorder(Color.black));

			todoList.add(todo);
			todoList.add(Box.createVerticalStrut(10));
		}

		todoList.revalidate();
		todoList.repaint();
	}

	@Override
	public void onToDoChanged() {
		todoList.removeAll();
		loadTodos();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newTodo) {
			CreateDialog dialog = new CreateDialog(this, LM.getValue("title.dialog"), true, this);
			dialog.setSize(500, 550);
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		} else if (e.getSource() == searchfield) {
			todoList.removeAll();
			searchForTodo(searchfield.getRealText());
		}
	}
	
	public void searchForTodo(String text) {
		ArrayList<ArrayList<Object>> results = ToDoDAO.searchTodos(text);

		for (var row : results) {
			ToDo todo = new ToDo((Integer) row.get(0), // Die ID
								(String) row.get(1), // Der Name bzw. die Bezeichnung
								(String) row.get(2), // Das Startdatum
								(String) row.get(3), // Das Enddatum
								(String) row.get(4), // Die Beschreibung
								(Boolean) row.get(5), // Ob erledigt oder nicht (done)
								Priority.valueOf((String) row.get(6)), // Die Priorität
								this); // Der Listener der darauf reagiert wenn sich etwas an der Todo-Liste geändert hat
			todo.setBorder(new LineBorder(Color.black));

			todoList.add(todo);
			todoList.add(Box.createVerticalStrut(10));
		}

		todoList.revalidate();
		todoList.repaint();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == languages && e.getStateChange() == ItemEvent.SELECTED) {
			LM.setLanguage((Locale) languages.getSelectedItem());

			options.setText(LM.getValue("options"));
			newTodo.setText(LM.getValue("options.new"));
			searchfield.setPlaceholder(LM.getValue("main.searchfield"));

			for (int i = 0; i < todoList.getComponentCount(); i++) {
				var comp = todoList.getComponent(i);
				if (comp instanceof ToDo) {
					ToDo todo = (ToDo) comp;
					todo.setUpdateText(LM.getValue("todo.update"));
					todo.setWatchText(LM.getValue("todo.watch"));
					todo.setDoneText(LM.getValue("todo.done"));
					todo.setDeleteText(LM.getValue("todo.delete"));
				}
			}

			this.revalidate();
			this.repaint();
		}
	}

	public static void main(String[] args) {
		MainFrame window = new MainFrame();
		window.setTitle("ToDo-Liste");
		window.setSize(550, 600);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}