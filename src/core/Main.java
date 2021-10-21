package core;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.imageio.stream.FileImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gif.Buffer;
import gif.Capturer;
import gif.GifWriter;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import ui.MenuListener;

public class Main {

	protected static BlockingQueue<BufferedImage> kuvatque = new ArrayBlockingQueue<BufferedImage>(300);

	protected static GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
	protected static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	protected static Robot robot;
	protected static JFrame frame = new JFrame("AK-Capture");
	protected static JLabel label = new JLabel();
	protected static JPanel pane = new JPanel();
	protected static File endFile;
	protected static File giff = null;
	protected static File res = new File("res/");
	protected static File output = new File("output/");
	protected static int CompressionAmount = 50; // 30 min - 200 max



	protected static String format = ".png";
	protected static String finalformat = ".gif";
	protected static String imageName = "image";

	protected static GifWriter writer = null;
	protected static FileImageOutputStream stream;
	protected static Rectangle mouseRect;
	protected static MenuItem loopp;
	protected static MenuItem circle;
	protected static MenuItem comp;
	protected static MenuItem fpsslider;
	protected static TrayIcon trayIcon;
	protected static BufferedImage image;
	protected static BufferedImage image2;
	
	protected static boolean capturing = false; // kuvaus
	protected static boolean loop = true; // kuvauslol
	protected static boolean valintamode = false; // valinta
	protected static boolean valmis = true;
	protected static boolean done = false;
	protected static boolean ympyr�=false;
	
	protected static int mouseX, mouseY, mouseX2, mouseY2;
	protected static int kuvaindex = 0;
	protected static int delay = 33;
	protected static int kierros = 0;
	protected static int nameindex = 0;
	protected static int korkeus = 720;
	protected static int leveys = 1280;

	protected static int INTERVAL = 33; // time between screenshots & default targetFPS

	protected static double value = 30; // for fps label and event

	public static void capture(Rectangle rectangle) throws Exception {

		valmis = false;
		capturing = true;
		frame.setVisible(false);

		Capturer cap = new Capturer(rectangle);
		Thread rec = new Thread(cap);

		Buffer buf = new Buffer();
		Thread ThreadBuffer = new Thread(buf);

		fileFoundation(rectangle);

		rec.start();
		ThreadBuffer.start();

	}

	public static void stopCapture() throws Exception {
		trayIcon.setToolTip("Ready");
		valmis = true;
		kuvaindex = 0;
		capturing = false;
		stream.close();
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("res/idle.gif"));

		System.out.println("GIF created at: " + output.getAbsolutePath() + "\\" + endFile);
		alustus();

		if ((CompressionAmount+30)>30) {
			String compress = "cmd /c gifsicle.exe --batch --optimize=O3 --colors 256 --lossy=" + CompressionAmount + " "
					+ ("../" + output + "/" + endFile).toString();
			System.out.println(compress);
			Runtime rt = Runtime.getRuntime();
			@SuppressWarnings("unused")
			Process b = rt.exec(compress, null, res.getAbsoluteFile());

		}

	}

	public static void fileFoundation(Rectangle rectangle) {
		BufferedImage kuva = robot.createScreenCapture(rectangle);
		try {
			if (kuva.getHeight() > 720 || kuva.getWidth() > 1280) {
				kuva = resizeImage(kuva, (int) (kuva.getWidth() / 1.5), (int) (kuva.getHeight() / 1.5));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(ympyr�) 
			kuva=makeCircle(kuva);
		
		
		while (new File(output, imageName + nameindex + finalformat).exists()) {

			nameindex++;
		}
		endFile = new File(imageName + nameindex + finalformat);
		giff = new File(output, (imageName + nameindex + finalformat).toString());
		try {
			stream = new FileImageOutputStream(giff);
			writer = new GifWriter(stream, kuva.getType(), delay, loop);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			
			writer.writeToSequence(kuva);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void valintamode() {

		
		image = robot.createScreenCapture(new Rectangle(dim.width*2,dim.height*2));
		
	
		label.setIcon(new ImageIcon(image));
		
		frame.add(label);
		frame.pack();
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.setColor(new Color(0,0,0,50));
		g.fillRect(0, 0,image.getWidth(),image.getHeight());
		
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		
	}

	public static void alustus() {

		// Alustaa kansion jos ei l�ydy
		valintamode = false;
		kuvaindex = 0;
		done = false;
		if (!output.exists())
			output.mkdir();
		if (!res.exists())
			res.mkdir();
	}

	public static void iconMenu(TrayIcon icon) {

		ActionListener listen = new MenuListener();
		PopupMenu popup = new PopupMenu();
	
		fpsslider = new MenuItem("FPS: " + value);
		popup.add(fpsslider);
		fpsslider.addActionListener(listen);
		MenuItem itemz = new MenuItem("Timelapse");
		popup.add(itemz);
		

		itemz.addActionListener(listen);
		MenuItem item = new MenuItem("High");
		popup.add(item);

		item.addActionListener(listen);
		MenuItem item2 = new MenuItem("Medium");
		item2.addActionListener(listen);
		item2.setFont(new Font("Arial", Font.BOLD, 13));
		popup.add(item2);
		MenuItem item3 = new MenuItem("Low");
		item3.addActionListener(listen);
		popup.add(item3);
		popup.addSeparator();
		loopp = new MenuItem("Loop: ON");

		loopp.addActionListener(listen);
		popup.add(loopp);
		circle = new MenuItem("Circle: OFF");

		circle.addActionListener(listen);
		popup.add(circle);
		
		comp = new MenuItem("Compression...");

		comp.addActionListener(listen);
		popup.add(comp);
		
		popup.addSeparator();
		MenuItem folder = new MenuItem("Folder");
		
		folder.addActionListener(listen);
		popup.add(folder);
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

	protected static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}

	public static void Window() throws AWTException {
		robot = new Robot();

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

		frame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (valintamode == true) {

					mouseX2 = e.getXOnScreen();
					mouseY2 = e.getYOnScreen();
					Graphics2D g = (Graphics2D) label.getGraphics();
					System.out.println();
					g.drawImage(image, 0, 0, label);
			
					g.setColor(Color.WHITE);

					g.setColor(Color.WHITE);
					g.drawString("Size: " + (mouseX2 - mouseX) + ", " + (mouseY2 - mouseY), mouseX2, mouseY2);
					g.setColor(Color.CYAN);

					if (mouseX != mouseX2 || mouseY != mouseY2) {
						
						if (mouseX2 - mouseX > 0 && mouseY2 - mouseY > 0) {
							if(ympyr�) {
								g.drawOval(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY);
								g.setColor(new Color(0,0,0,50));
								g.fillOval(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY);
								}
							if(!ympyr�) {
								
								g.drawRect(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY);
								g.setColor(new Color(0,0,0,50));
								g.fillRect(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY);

							}
							

							
							
						}
																								
						else if (mouseX2 - mouseX > 0 && mouseY2 - mouseY < 0) {
							if(ympyr�) {
								g.drawOval(mouseX, mouseY2, mouseX2 - mouseX, mouseY - mouseY2);
								g.setColor(new Color(0,0,0,50));
								g.fillOval(mouseX, mouseY2, mouseX2 - mouseX, mouseY - mouseY2);
							}
							if(!ympyr�) {
								g.drawRect(mouseX, mouseY2, mouseX2 - mouseX, mouseY - mouseY2);
								g.setColor(new Color(0,0,0,50));
								g.fillRect(mouseX, mouseY2, mouseX2 - mouseX, mouseY - mouseY2);
							}
							
							
						}
							
						else if (mouseX2 - mouseX < 0 && mouseY2 - mouseY > 0) {
							if(ympyr�) {
								g.drawOval(mouseX2, mouseY, mouseX - mouseX2, mouseY2 - mouseY); 
								g.setColor(new Color(0,0,0,50));;
								g.fillOval(mouseX2, mouseY, mouseX - mouseX2, mouseY2 - mouseY); 
							}
							if(!ympyr�) {
								g.drawRect(mouseX2, mouseY, mouseX - mouseX2, mouseY2 - mouseY); 
								g.setColor(new Color(0,0,0,50));
								g.fillRect(mouseX2, mouseY, mouseX - mouseX2, mouseY2 - mouseY); 
							}
							
						
						}
						else {
							if(ympyr�) {
								g.drawOval(mouseX2, mouseY2, mouseX - mouseX2, mouseY - mouseY2);
								g.setColor(new Color(0,0,0,50));
								g.fillOval(mouseX2, mouseY2, mouseX - mouseX2, mouseY - mouseY2);
							}
							if(!ympyr�) {
								g.drawRect(mouseX2, mouseY2, mouseX - mouseX2, mouseY - mouseY2); 
								g.setColor(new Color(0,0,0,50));
								g.fillRect(mouseX2, mouseY2, mouseX - mouseX2, mouseY - mouseY2); 
							}
							 
				            
						}
				
				}

			}}

			@Override
			public void mouseMoved(MouseEvent e) {

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

		trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("res/idle.gif"), "Ready");
		iconMenu(trayIcon);

	}
	   public static BufferedImage makeCircle(BufferedImage image) {
	        int w = image.getWidth();
	        int h = image.getHeight();
	        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	 
	        Graphics2D g2 = output.createGraphics();
	 
	        g2.setComposite(AlphaComposite.Src);
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setColor(Color.WHITE);
	        g2.fill(new Ellipse2D.Double(0, 0, image.getWidth(), image.getHeight()));
	        g2.setComposite(AlphaComposite.SrcAtop);
	        g2.drawImage(image, 0, 0, null);
	 
	        g2.dispose();
	 
	        return output;
	    }
	public static void main(String[] args) throws Exception {

		alustus();
		Window();
	}
}