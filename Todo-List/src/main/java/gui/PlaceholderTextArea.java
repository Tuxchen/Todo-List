package gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextArea;

public class PlaceholderTextArea extends JTextArea implements FocusListener {
	private String placeholder;
	
	private Color placeholderColor;
	private Color normalColor;
	
	public PlaceholderTextArea(String placeholder) {
		super(placeholder);
		this.placeholder = placeholder;
		
		placeholderColor = Color.gray;
		normalColor = Color.black;
		
		setForeground(placeholderColor);
		addFocusListener(this);
	}
	
	public void setPlaceholderColor(Color color) {
		placeholderColor = color;
	}
	
	public void setNormalColor(Color color) {
		normalColor = color;
	}
	
	public String getRealText() {
		return getText().equals(placeholder) ? "" : getText();
	}
	
	public void setRealText(String text) {
		if(text.equals("")) {
			setText(placeholder);
			setForeground(placeholderColor);
		}
		else {
			setText(text);
			setForeground(normalColor);
		}
	}
	
	public void setPlaceholder(String placeholder) {
		if(getText().equals(this.placeholder)) {
			setText(placeholder);
		}
		this.placeholder = placeholder;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(getText().equals(placeholder)) {
			setText("");
			setForeground(normalColor);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(getText().equals("")) {
			setText(placeholder);
			setForeground(placeholderColor);
		}
	}
}
