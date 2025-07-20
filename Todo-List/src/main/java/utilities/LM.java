package utilities;

import java.util.Locale;
import java.util.ResourceBundle;

public class LM {
	private static ResourceBundle bundle;
	
	public static void setLanguage(Locale locale) {
		bundle = ResourceBundle.getBundle("messages", locale);
	}
	
	public static String getValue(String key) {
		return bundle.getString(key);
	}
}
