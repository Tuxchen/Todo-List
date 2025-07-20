package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import db.ToDoDAO;
import utilities.LM;
import utilities.Priority;

public class MainFrame extends JFrame implements ItemListener {
	private Container c;
	private JMenuBar menuBar;
	private JMenu options;
	private JMenuItem newTodo;
	private JComboBox<Locale> languages;
	private JPanel todoList;
	
	public MainFrame() {
		c = getContentPane();
		c.setLayout(new BorderLayout());
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
		
		options.add(newTodo);
		
		menuBar.add(options);
		
		setJMenuBar(menuBar);
		
		languages = new JComboBox<>(new Locale[] { Locale.GERMAN, Locale.ENGLISH });
		languages.addItemListener(this);
		
		todoList = new JPanel();
		todoList.setLayout(new BoxLayout(todoList, BoxLayout.Y_AXIS));
		todoList.setBackground(Color.lightGray);
		JScrollPane sp = new JScrollPane(todoList);
		
		ToDo test = new ToDo(1, "Test", new Date(), new Date(), false, Priority.LOW);
		
		todoList.add(test);
		
		c.add(BorderLayout.NORTH, languages);
		c.add(BorderLayout.CENTER, sp);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == languages && e.getStateChange() == ItemEvent.SELECTED) {
			LM.setLanguage( (Locale) languages.getSelectedItem());
			
			options.setText(LM.getValue("options"));
			newTodo.setText(LM.getValue("options.new"));
			
			for(int i = 0; i < todoList.getComponentCount(); i++) {
				ToDo elem = (ToDo) todoList.getComponent(i);
				elem.setUpdateText(LM.getValue("update"));
				elem.setWatchText(LM.getValue("watch"));
				elem.setDoneText(LM.getValue("done"));
				elem.setDeleteText(LM.getValue("delete"));
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