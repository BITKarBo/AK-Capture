package core;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Main{

	static Robot robot;
	static GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
	static ArrayList<BufferedImage> kuvat = new ArrayList<BufferedImage>();
	static JFrame frame;
	static JLabel label;
	static File tmp = new File("tmp/");
	static File output = new File("output/");
	static String format = ".png";
	static String finalformat = ".gif";
	static String imageName = "image";
	static boolean valintamode=false;
	static Rectangle mouseRect;
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	static boolean capturing = false; // kuvaus boolean
	static boolean loop = true; // kuvaus boolean
	static MenuItem loopp;
	static int interval = 100;
	//static int kuvamaara = 30;
	static int kuvaindex = 0;
	static int delay = 100;
	
	static int mouseX, mouseY, mouseX2, mouseY2;
	

	static Timer timer = new Timer();

	public static void capture(Rectangle rectangle) throws AWTException {

		if (!capturing) {
			frame.setVisible(false);
			
			capturing = true;
			
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if(kuvaindex>=100000) {
						try {
							stopCapture();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("Capturing");
					kuvaindex++;
					kuvat.add(robot.createScreenCapture(rectangle));
					//File file = new File(tmp, + kuvaindex + format);
					
					//boolean status = ImageIO.write(bufferedImage, "png", file);
					//System.out.println("Screen Captured: " + status + " File Path:- " + file.getAbsolutePath());

				

				}
			}, 0, interval);
		}
		
	}

	public static void stopCapture() throws Exception {
		timer.cancel();
		alustus();
		capturing = false;
		

		BufferedImage first = kuvat.get(0);
		while(new File(output, imageName + kuvaindex + finalformat).exists()) {
			
			kuvaindex++;
		}
		File giff =new File(output,(imageName + kuvaindex + finalformat).toString());
		ImageOutputStream gif = new FileImageOutputStream(giff);

		GifWriter writer = new GifWriter(gif, first.getType(), delay, loop);
		writer.writeToSequence(first);

		
		for (BufferedImage i : kuvat) {
		
			writer.writeToSequence(i);
		}

		writer.close();
		gif.close();
		
		//
		//	Kertoo gifin luonnin onnistumisen ja polun sekä tyhjentää tmp
		//
	
		 

		System.out.println("GIF created at: " + output.getAbsolutePath() + ":" + imageName + finalformat);
		valintamode=false;
		kuvaindex = 0;
		kuvat.clear();
		// tähän sit vaikka se gif luonti tai uus metodi
	}
	public static void valintamode() {
		label.setIcon(new ImageIcon(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()))));
		
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
	//todo
	//tähän se ikkunan valinta tila jonka jälkeen suoritetaan capture metodi
	}
	public static void alustus() {
		//Alustaa kansion jos ei löydy
		kuvaindex = 0;
		if(!tmp.exists())
			tmp.mkdir();
		if(!output.exists())
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
	                /* gui tähän vaikka sit myöhemmin
	                frame.setAlwaysOnTop(true);
	                frame.setVisible(true);
	                JOptionPane.showMessageDialog(null, "Clicked");
	                */
	            }
	        });

	        try {
	            SystemTray.getSystemTray().add(icon);
	        }catch (Exception e){
	            System.out.println(e);
	        }
	}
	public static void main(String[] args) throws Exception {
		
		
		alustus();
		
		robot = new Robot();
		frame = new JFrame("AK-Capture");
		label = new JLabel();
		
		JPanel pane = new JPanel();
		dim.setSize(dim.width, dim.height+5);
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(GlobalKeyEvent e) {
				if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_F9 && !capturing) {
					try {
						capture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_F8 && !capturing && !valintamode) {
				valintamode=true;
				valintamode();
				
				}
				else if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE && valintamode) {
				frame.setVisible(false);
				valintamode=false;
				}
				else if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_F9 && capturing) {
					try {
						stopCapture();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	
		frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(valintamode==true) {
				mouseX2 = e.getXOnScreen();
				mouseY2 = e.getYOnScreen();
				try {
					if(mouseX!=mouseX2||mouseY!=mouseY2) {
					if(mouseX2 - mouseX > 0 && mouseY2 - mouseY > 0) 				
						capture(new Rectangle(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY)); 	// 0,0 -> 1,1
					else if(mouseX2 - mouseX > 0 && mouseY2 - mouseY < 0)			
						capture(new Rectangle(mouseX, mouseY2, mouseX2 - mouseX, mouseY - mouseY2));	// 0,1 -> 1,0
					else if(mouseX2 - mouseX < 0 && mouseY2 - mouseY > 0)			
						capture(new Rectangle(mouseX2, mouseY, mouseX - mouseX2, mouseY2 - mouseY));	// 1,0 -> 0,1
					else
						capture(new Rectangle(mouseX2, mouseY2, mouseX - mouseX2, mouseY - mouseY2));	// 1,1 -> 0,0
					}
					
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(valintamode==true) {
				mouseX = e.getXOnScreen();
				mouseY = e.getYOnScreen();
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//lol
		TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("catjam.gif"),"running");
		iconMenu(trayIcon);


	}
	

}