package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatLightLaf;

import db.ToDoDAO;
import utilities.LM;
import utilities.Priority;

public class MainFrame extends JFrame implements ActionListener, ItemListener {
	private Container c;
	private JMenuBar menuBar;
	private JMenu options;
	private JMenuItem newTodo;
	private PlaceholderTextField searchfield;
	private JComboBox<Locale> languages;
	private JButton refresh;
	private JPanel todoList;
	
	private Locale[] supportedLanguages = {
		Locale.GERMAN, 
		Locale.ENGLISH, 
		Locale.FRENCH, 
		Locale.ITALY
	};
	
	public MainFrame() {
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch(Exception e) {
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
		
		refresh = new JButton(LM.getValue("main.refresh"));
		refresh.addActionListener(this);
		
		JPanel above = new JPanel(new GridLayout(1, 3, 5, 5));
		
		above.add(searchfield);
		above.add(languages);
		above.add(refresh);
		
		todoList = new JPanel();
		todoList.setLayout(new BoxLayout(todoList, BoxLayout.Y_AXIS));
		todoList.setBackground(Color.lightGray);
		JScrollPane sp = new JScrollPane(todoList);
		
		loadTodos();
		
		c.add(BorderLayout.NORTH, above);
		c.add(BorderLayout.CENTER, sp);
	}
	
	public void loadTodos() {
		ArrayList<ArrayList<Object>> results = ToDoDAO.selectAllTodos();
		
		for(var row : results) {
			ToDo todo = new ToDo(
					(Integer)row.get(0), // Die ID
					(String)row.get(1),  // Der Name bzw. die Bezeichnung
					(String)row.get(2),  // Das Startdatum
					(String)row.get(3),  // Das Enddatum
					(String)row.get(4),  // Die Beschreibung
					(Boolean)row.get(5), // Ob erledigt oder nicht (done)
					Priority.valueOf((String)row.get(6))); // Die Priorität
			todo.setBorder(new LineBorder(Color.black));
			
			todoList.add(todo);
			todoList.add(Box.createVerticalStrut(10));
		}
		
		todoList.revalidate();
		todoList.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newTodo) {
			CreateDialog dialog = new CreateDialog(this, LM.getValue("title.dialog"), true);
			dialog.setSize(500, 550);
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		}
		else if(e.getSource() == refresh) {
			todoList.removeAll();
			
			loadTodos();
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == languages && e.getStateChange() == ItemEvent.SELECTED) {
			LM.setLanguage( (Locale) languages.getSelectedItem());
			
			options.setText(LM.getValue("options"));
			newTodo.setText(LM.getValue("options.new"));
			searchfield.setPlaceholder(LM.getValue("main.searchfield"));
			refresh.setText(LM.getValue("main.refresh"));
			
			for(int i = 0; i < todoList.getComponentCount(); i++) {
				var comp = todoList.getComponent(i);
				if(comp instanceof ToDo) {
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
	
	public static void main (String[] args) {
		MainFrame window = new MainFrame();
		window.setTitle("ToDo-Liste");
		window.setSize(550, 600);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}