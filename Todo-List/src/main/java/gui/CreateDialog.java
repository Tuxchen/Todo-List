package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import db.ToDoDAO;
import utilities.DateLabelFormatter;
import utilities.LM;
import utilities.Priority;
import utilities.ToDoChangeListener;

public class CreateDialog extends JDialog implements ActionListener {
	private Container c;
	private PlaceholderTextField name;
	private JDatePickerImpl start;
	private JDatePickerImpl end;
	private PlaceholderTextArea description;
	private JRadioButton low, middle, high;
	private JButton submit;
	
	private ToDoChangeListener listener;
	
	public CreateDialog(JFrame owner, String title, boolean modal, ToDoChangeListener listener) {
		super(owner, title, modal);
		this.listener = listener;
		c = getContentPane();
		c.setLayout(new GridLayout(6, 1, 5, 5));
		
		name = new PlaceholderTextField(LM.getValue("name.dialog"));
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", LM.getValue("date.today"));
		p.put("text.month", LM.getValue("date.month"));
		p.put("text.year", LM.getValue("date.year"));
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		start = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		model = new UtilDateModel();
		datePanel = new JDatePanelImpl(model, p);
		end = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		description = new PlaceholderTextArea(LM.getValue("description.dialog"));
		
		JPanel priorityBoxes = new JPanel(new GridLayout(1, 3, 5, 5));
		
		low = new JRadioButton(LM.getValue("low.dialog"));
		middle = new JRadioButton(LM.getValue("middle.dialog"));
		high = new JRadioButton(LM.getValue("high.dialog"));
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(low);
		bg.add(middle);
		bg.add(high);
		
		priorityBoxes.add(low);
		priorityBoxes.add(middle);
		priorityBoxes.add(high);
		
		submit = new JButton(LM.getValue("submit.dialog"));
		submit.addActionListener(this);
		
		c.add(name);
		c.add(start);
		c.add(end);
		c.add(description);
		c.add(priorityBoxes);
		c.add(submit);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!name.getRealText().equals("") &&
			start.getModel().getValue() != null &&
			end.getModel().getValue() != null &&
			(!low.isSelected() || !middle.isSelected() || !high.isSelected())) {
			
			Date startDate = (Date) this.start.getModel().getValue();
			Date endDate = (Date) this.end.getModel().getValue();
			
			if(!endDate.after(startDate)) {
				JOptionPane.showMessageDialog(this, LM.getValue("date.invalid"));
				return;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			
			String name = this.name.getRealText();
			String start = sdf.format(startDate);
			String end = sdf.format(endDate);
			String description = this.description.getRealText();
			Priority priority;
			
			if(low.isSelected())
				priority = Priority.LOW;
			else if(middle.isSelected())
				priority = Priority.MIDDLE;
			else
				priority = Priority.HIGH;
			
			boolean result = ToDoDAO.insertNewTodo(name, start, end, description, false, priority);
			
			if(listener != null) {
				listener.onToDoChanged();
			}
			dispose();
		}
		else {
			JOptionPane.showMessageDialog(this, LM.getValue("info.box"));
		}
	}
}
