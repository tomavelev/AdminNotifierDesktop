package com.tomata.adminnotifier.ui.ext;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
	public static final ObservedItemViewExt COMP = new ObservedItemViewExt();
	final static TrayIcon trayIcon = new TrayIcon(new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR));
	static JFrame f = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				f = new JFrame("Admin Notifier");

				if (SystemTray.isSupported()) {

					for (MouseListener ml : trayIcon.getMouseListeners()) {
						trayIcon.removeMouseListener(ml);
					}
					trayIcon.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							if (e.getButton() == MouseEvent.BUTTON1) {
								if (!f.isVisible()) {
									reload();
									f.setVisible(true);
								}
							} else {
								int showConfirmDialog = JOptionPane.showConfirmDialog(f, "Exit?", "Admin Notifier",
										JOptionPane.YES_NO_OPTION);
								if (showConfirmDialog == JOptionPane.OK_OPTION) {
									System.exit(0);
								}
							}
						}
					});
					try {
						SystemTray.getSystemTray().add(trayIcon);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
				int defaultCloseOperation = SystemTray.isSupported() ? JFrame.HIDE_ON_CLOSE : JFrame.EXIT_ON_CLOSE;
				f.setDefaultCloseOperation(defaultCloseOperation);
				f.add(COMP);
				f.setSize(800, 500);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

				f.setLocation((screenSize.width - 800) / 2, (screenSize.height - 500) / 2);
				f.setVisible(true);
			}
		});
		AdminNotifierService.restart();
	}

	public static void reload() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				COMP.reload();
			}
		});
	}

}

