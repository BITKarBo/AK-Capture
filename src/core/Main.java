package core;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayDeque;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Main {

	static private final long INTERVAL = 33; // time between screenshots

	static ArrayDeque<BufferedImage> kuvatque = new ArrayDeque<BufferedImage>();
	static GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	static Robot robot;

	static JFrame frame = new JFrame("AK-Capture");
	static JLabel label = new JLabel();
	static JPanel pane = new JPanel();
	static File tmp = new File("tmp/");
	static File output = new File("output/");
	static String format = ".png";
	static String finalformat = ".gif";
	static String imageName = "image";
	static File giff = null;

	static Rectangle mouseRect;

	static MenuItem loopp;
	static TrayIcon trayIcon;

	static boolean capturing = false; // kuvaus
	static boolean loop = true; // kuvaus
	static boolean valintamode = false; // valinta
	static boolean valmis = true;

	static int mouseX, mouseY, mouseX2, mouseY2;
	static int kuvaindex = 0;
	static int delay = 16;

	public static void capture(Rectangle rectangle) throws Exception {
		valmis = false;
		capturing = true;
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("rec.jpg"));
		trayIcon.setToolTip("Capturing...");
		frame.setVisible(false);

		Thread ThreadKuva = new Thread(new Runnable() {

			@Override
			public void run() {

				while (capturing) {
					long alkuaika = System.nanoTime();
					System.out.println("Capturing: " + kuvaindex + "QUESIZE: " + kuvatque.size());

					kuvaindex++;
					kuvatque.add(robot.createScreenCapture(rectangle));
					long loppuaika = System.nanoTime();
					System.out.println("Lis��ntynyt aika : "+(loppuaika-alkuaika)/1000000.0 + "ms");
					try {
						int aika = (int) (INTERVAL - (loppuaika-alkuaika)/1000000.0);
						if(aika > 0) {
							Thread.sleep(aika);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

		});
		Thread ThreadBuffer = new Thread(new Runnable() {
			@Override
			public void run() {

				GifWriter writer = null;
				boolean eka = true;
				while (capturing || kuvatque.peek() != null) {
					long alkuaika = System.nanoTime();
					if (kuvatque.peek() != null) {
						System.out.println("BUFFER QUESIZE: " + kuvatque.size());
						BufferedImage kuva = kuvatque.poll();
						if (eka) {
							eka = false;
							int nameindex = 0;
							while (new File(output, imageName + nameindex + finalformat).exists()) {

								nameindex++;
							}
							giff = new File(output, (imageName + nameindex + finalformat).toString());
							try {
								writer = new GifWriter(new FileImageOutputStream(giff), kuva.getType(), delay, loop);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						try {
							writer.writeToSequence(kuva);
						} catch (IOException e) {
							e.printStackTrace();
						}
						//System.out.println("BUFFER Time: "+(System.nanoTime()-alkuaika)/1000000.0 + "ms");
					} else {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
				try {
					writer.close();
					stopCapture();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		ThreadKuva.start();
		ThreadBuffer.start();

	}

	public static void stopCapture() throws Exception {
		trayIcon.setToolTip("Ready");
		valmis = true;
		kuvaindex = 0;
		capturing = false;
		// writer.close();

		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("catjam.gif"));

		System.out.println("GIF created at: " + output.getAbsolutePath() + ":" + imageName + finalformat);
		alustus();
		// t�h�n sit vaikka se gif luonti tai uus metodi
	}

	public static void valintamode() {
		label.setIcon(
				new ImageIcon(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()))));

		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		// todo
		// t�h�n se ikkunan valinta tila jonka j�lkeen suoritetaan capture metodi
	}

	public static void alustus() {
		// Alustaa kansion jos ei l�ydy
		valintamode = false;
		kuvaindex = 0;
		if (!output.exists())
			output.mkdir();
	}

	public static void iconMenu(TrayIcon icon) {

		PopupMenu popup = new PopupMenu();
		MenuItem item = new MenuItem("High");
		popup.add(item);
		ActionListener listen = new PopupActionListener();

		item.addActionListener(listen);
		MenuItem item2 = new MenuItem("Medium");
		item2.addActionListener(listen);
		popup.add(item2);
		MenuItem item3 = new MenuItem("Low");
		item3.addActionListener(listen);
		popup.add(item3);
		popup.addSeparator();
		loopp = new MenuItem("Loop: ON");

		loopp.addActionListener(listen);
		popup.add(loopp);
		popup.addSeparator();
		MenuItem close = new MenuItem("Close");
		close.addActionListener(listen);
		popup.add(close);
		icon.setPopupMenu(popup);

		icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				/*
				 * gui t�h�n vaikka sit my�hemmin frame.setAlwaysOnTop(true);
				 * frame.setVisible(true); JOptionPane.showMessageDialog(null, "Clicked");
				 */
			}
		});

		try {
			SystemTray.getSystemTray().add(icon);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void Window() throws AWTException {
		robot = new Robot();

		dim.setSize(dim.width, dim.height + 5);
		pane.setSize(dim);

		pane.setLocation(0, -5);
		pane.add(label);
		frame.add(pane);
		frame.setLayout(new BorderLayout());
		frame.setSize(dim.width, dim.height);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);

		frame.setResizable(false);
		frame.setVisible(false);

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {

			@Override
			public void keyReleased(GlobalKeyEvent e) {

			}

			@Override
			public void keyPressed(GlobalKeyEvent e) {
				if (e.getVirtualKeyCode() == GlobalKeyEvent.VK_F9 && !capturing && valmis) {
					try {
						capture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if (e.getVirtualKeyCode() == GlobalKeyEvent.VK_F8 && !capturing && !valintamode && valmis) {
					valintamode = true;

					valintamode();

				} else if (e.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE && valintamode) {
					frame.setVisible(false);
					valintamode = false;
				} else if ((e.getVirtualKeyCode() == GlobalKeyEvent.VK_F9 && capturing)
						|| (e.getVirtualKeyCode() == GlobalKeyEvent.VK_F8 && capturing)) {
					trayIcon.setToolTip("Buffering...");
					capturing = false;

				}
			}
		});

		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (valintamode == true) {
					mouseX2 = e.getXOnScreen();
					mouseY2 = e.getYOnScreen();
					try {
						if (mouseX != mouseX2 || mouseY != mouseY2) {
							if (mouseX2 - mouseX > 0 && mouseY2 - mouseY > 0)
								capture(new Rectangle(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY)); // 0,0 ->
																											// 1,1
							else if (mouseX2 - mouseX > 0 && mouseY2 - mouseY < 0)
								capture(new Rectangle(mouseX, mouseY2, mouseX2 - mouseX, mouseY - mouseY2)); // 0,1 ->
																												// 1,0
							else if (mouseX2 - mouseX < 0 && mouseY2 - mouseY > 0)
								capture(new Rectangle(mouseX2, mouseY, mouseX - mouseX2, mouseY2 - mouseY)); // 1,0 ->
																												// 0,1
							else
								capture(new Rectangle(mouseX2, mouseY2, mouseX - mouseX2, mouseY - mouseY2)); // 1,1 ->
																												// 0,0
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (valintamode == true) {
					mouseX = e.getXOnScreen();
					mouseY = e.getYOnScreen();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});

		trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("catjam.gif"), "running");
		iconMenu(trayIcon);

	}

	public static void main(String[] args) throws Exception {

		alustus();
		Window();
	}
}