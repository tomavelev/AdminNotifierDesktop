package com.tomata.adminnotifier.i18n;

import java.util.ResourceBundle;

public class I18n {

	private static ResourceBundle res;

	public static String s(String string) {
		try {
			if (res != null)
				res = ResourceBundle.getBundle("com/tomata/adminnotifier/i18n/messages");

			return res.getString(string);
		} catch (Exception e) {
		}
		return string;
	}

}
