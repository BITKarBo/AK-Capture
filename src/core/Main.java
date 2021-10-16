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

	static private final double UPDATE_CAP = 1.0 / 60.0;
	static private final double INTERVAL = 1.25; // time between screenshots

	static ArrayDeque<BufferedImage> kuvatque = new ArrayDeque<BufferedImage>();
	static GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	static Robot robot;

	static JFrame frame = new JFrame("AK-Capture");;
	static JLabel label = new JLabel();;
	static JPanel pane = new JPanel();;
	static File tmp = new File("tmp/");
	static File output = new File("output/");
	static String format = ".png";
	static String finalformat = ".gif";
	static String imageName = "image";
	static File giff = null;
	static GifWriter writer = null;
	static Rectangle mouseRect;

	static MenuItem loopp;
	static TrayIcon trayIcon;

	static boolean capturing = false; // kuvaus
	static boolean loop = true; // kuvaus
	static boolean valintamode = false; // valinta

	static int mouseX, mouseY, mouseX2, mouseY2;
	static int kuvaindex = 0;
	static int delay = 100;
	
	public static void capture(Rectangle rectangle) throws Exception {
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				if (!capturing) {
					trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("rec.jpg"));
					frame.setVisible(false);
					capturing = true;
					double firstTime = 0;
					double lastTime = System.nanoTime()/1000000000.0;
					double passedTime = 0;
					double unprocessedTime = 0;
					double frameTime = 0;
					boolean fpslimitter = true;
					
					while (capturing || kuvatque.size() > 0) {
						
						firstTime = System.nanoTime()/1000000000.0;
						passedTime = firstTime - lastTime;
						lastTime = firstTime;

						unprocessedTime += passedTime;
						frameTime += passedTime;
						

						while (unprocessedTime >= UPDATE_CAP) {
							unprocessedTime -= UPDATE_CAP;
							
							
							if (frameTime >= INTERVAL) {
								fpslimitter = true;
								frameTime = 0;
							}
						}
						if (capturing && fpslimitter) {
							fpslimitter = false;
							System.out.println("Capturing: " + kuvaindex);
							kuvaindex++;
							kuvatque.add(robot.createScreenCapture(rectangle));
						}

						if(kuvatque.peek() != null) {
							BufferedImage kuva = kuvatque.poll();
							if(kuvaindex == 1) {
								int nameindex = 0;
								while (new File(output, imageName + nameindex + finalformat).exists()) {

									nameindex++;
								}
								giff = new File(output, (imageName + nameindex + finalformat).toString());
								try {
									writer = new GifWriter(new FileImageOutputStream(giff), kuva.getType(), delay, loop);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							try {
								writer.writeToSequence(kuva);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} 
					}
					
					try {
						stopCapture();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});
		
		t.start();
	}

	public static void stopCapture() throws Exception {
		kuvaindex = 0;
		capturing = false;
		//writer.close();
		
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("catjam.gif"));

		System.out.println("GIF created at: " + output.getAbsolutePath() + ":" + imageName + finalformat);
		alustus();
		// tähän sit vaikka se gif luonti tai uus metodi
	}

	public static void valintamode() {
		label.setIcon(
				new ImageIcon(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()))));

		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		// todo
		// tähän se ikkunan valinta tila jonka jälkeen suoritetaan capture metodi
	}

	public static void alustus() {
		// Alustaa kansion jos ei löydy
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
				 * gui tähän vaikka sit myöhemmin frame.setAlwaysOnTop(true);
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
				if (e.getVirtualKeyCode() == GlobalKeyEvent.VK_F9 && !capturing) {
					try {
						capture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if (e.getVirtualKeyCode() == GlobalKeyEvent.VK_F8 && !capturing && !valintamode) {
					valintamode = true;
					
					valintamode();

				} else if (e.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE && valintamode) {
					frame.setVisible(false);
					valintamode = false;
				} else if ((e.getVirtualKeyCode() == GlobalKeyEvent.VK_F9 && capturing)
						|| (e.getVirtualKeyCode() == GlobalKeyEvent.VK_F8 && capturing)) {

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