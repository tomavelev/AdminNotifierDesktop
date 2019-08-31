package com.tomata.adminnotifier.ui.ext;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import com.tomata.adminnotifier.dao.DB;
import com.tomata.adminnotifier.dao.ObservedItemDao;
import com.tomata.adminnotifier.model.ObservedItem;

public class AdminNotifierService {

	protected static final long Z30_minutes = 1000 * 60 * 30;
	private static Thread t;

	public static void restart() {
		if (t != null && t.isAlive()) {
			t.interrupt();
			try {
				t.join(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			t = null;
		}

		t = new Thread() {
			@Override
			public void run() {
				while (true) {
					List<ObservedItem> result = getItems();
					for (ObservedItem task : result) {
						if (!task.isActive()) {
							continue;
						}
						try {
							final String newValue = get(task.getUrl());
							boolean firstPass = false;

							String message = "Old Value";
							if (!task.isChangeAfterMeetingCondition()) {
								message = "Flag value";
							}
							try {
								final BigDecimal decimal = new BigDecimal(newValue);
								firstPass = true;
								BigDecimal oldDecimal = null;
								if (task.getValue() == null || task.getValue().equals("")) {
									oldDecimal = BigDecimal.ZERO;
								} else {
									oldDecimal = new BigDecimal(task.getValue());
								}
								task.setTimeOfTempValue(new Date());
								task.setTempValue(newValue);

								switch (task.getComparationActivation()) {
								case Equal:
									if (newValue != null && newValue.equals(task.getValue())) {
										postNotification(task.getName(), " ( " + newValue + " ) ", task);
									}
									break;
								case Greater:
									if (decimal.doubleValue() > oldDecimal.doubleValue()) {
										postNotification(task.getName(), " ( " + message + ": " + task.getValue()
												+ " New Value: " + newValue + " ) ", task);
									}
									break;
								case Less:
									if (decimal.doubleValue() < oldDecimal.doubleValue()) {
										postNotification(task.getName(), " ( " + message + ": " + task.getValue()
												+ " New Value: " + newValue + " ) ", task);
									}
									break;
								case LessEqual:
									if (decimal.doubleValue() <= oldDecimal.doubleValue()) {
										postNotification(task.getName(), " ( " + message + ": " + task.getValue()
												+ " New Value: " + newValue + " ) ", task);
									}
									break;
								case GreaterEqual:
									if (decimal.doubleValue() >= oldDecimal.doubleValue()) {
										postNotification(task.getName(), " ( " + message + ": " + task.getValue()
												+ " New Value: " + newValue + " ) ", task);
									}
									break;
								}
								if (task.isChangeAfterMeetingCondition()) {
									task.setValue(newValue);
								}
							} catch (NumberFormatException e) {
								postNotification(task.getName(),
										(firstPass ? message : "New Value") + " : Not A Number", task);
							}

						} catch (Exception e) {
							e.printStackTrace();
							postNotification(task.getName(), e.getMessage(), task);
						}

						try (Connection conn = DB.init()) {
							new ObservedItemDao(conn).saveOrUpdate(task);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
					Main.reload();
					try {
						Thread.sleep(timeToSleep());
					} catch (InterruptedException e) {
						break;
					}
				}
			}

			private List<ObservedItem> getItems() {
				try (Connection conn = DB.init()) {
					return new ObservedItemDao(conn).select(Integer.MAX_VALUE, 0, "").getList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return Collections.emptyList();
			}

			private long timeToSleep() {
				File f = new File("timeToSleep.txt");
				if (f.exists()) {
					try {
						String content = new String(Files.readAllBytes(f.toPath()));
						return Long.parseLong(content);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return Z30_minutes;
			}

			private void postNotification(String name, String message, ObservedItem task) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						Main.trayIcon.displayMessage(name, message, MessageType.INFO);
					}
				});
			}
		};
		t.start();

	}

	public static final String get(String urlAddr) throws IOException {
		StringBuilder sb = new StringBuilder();
		try {

			URL url = new URL(urlAddr);
			URLConnection httpCon = url.openConnection();
			httpCon.setReadTimeout(5000);
			BufferedReader r = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));

			String line = null;

			while ((line = r.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
