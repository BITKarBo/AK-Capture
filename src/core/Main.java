package core;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Main{

	
	static ArrayList<File> kuvat = new ArrayList<File>();
	static JFrame frame;
	static File tmp = new File("tmp/");
	static File output = new File("output/");
	static String format = ".png";
	static String finalformat = ".gif";
	static String imageName = "image";
	static boolean valintamode=false;
	static Rectangle mouseRect;
	static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	static boolean capturing = false; // kuvaus boolean
	

	static int interval = 100;
	static int kuvamaara = 100;
	static int kuvaindex = 0;
	static int delay = 100;
	
	static int mouseX, mouseY, mouseX2, mouseY2;
	

	static Timer timer = new Timer();

	public static void capture(Rectangle rectangle) throws AWTException {

		if (!capturing) {
			
			capturing = true;
			
			Robot robot = new Robot();
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					kuvaindex++;
					try {
						BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
						File file = new File(tmp, + kuvaindex + format);
						kuvat.add(file);
						boolean status = ImageIO.write(bufferedImage, "png", file);
						System.out.println("Screen Captured: " + status + " File Path:- " + file.getAbsolutePath());

						if(kuvaindex >= kuvamaara)
							try {
								stopCapture();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
					} catch (IOException ex) {
						System.err.println(ex);
					}

				}
			}, 0, interval);
		}
		
	}

	public static void stopCapture() throws Exception {
		timer.cancel();
		alustus();
		capturing = false;
		

		BufferedImage first = ImageIO.read(new File(tmp,("1"+format).toString()));
		while(new File(output, imageName + kuvaindex + finalformat).exists()) {
			
			kuvaindex++;
		}
		ImageOutputStream gif = new FileImageOutputStream(new File(output,(imageName + kuvaindex + finalformat).toString()));

		GifWriter writer = new GifWriter(gif, first.getType(), delay, true);
		writer.writeToSequence(first);

		
		for (File i : kuvat) {
			BufferedImage next = ImageIO.read(i);
			writer.writeToSequence(next);
		}

		writer.close();
		gif.close();
		
		//
		//	Kertoo gifin luonnin onnistumisen ja polun sekä tyhjentää tmp
		//
		
		System.out.println("GIF created at: " + output.getAbsolutePath() + ":" + imageName + finalformat);
		if(!kuvat.isEmpty()) {
			for (File file : kuvat) {
				file.delete();
			}
		}
		kuvaindex = 0;
		
		// tähän sit vaikka se gif luonti tai uus metodi
	}
	public static void valintamode() {
		frame.setVisible(true);
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
	public static void main(String[] args) throws AWTException {
		alustus();
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
		frame = new JFrame("AK-Capture");
		
		frame.setLayout(new BorderLayout());
		frame.setSize(dim.width, dim.height);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		//frame.setUndecorated(true);
		//frame.setLayout(null);
		Canvas canvas = new Canvas();
		canvas.setPreferredSize(dim);
		
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		//frame.setLocationRelativeTo(null);
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
			if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_F8 && !capturing) {
				valintamode=true;
				valintamode();
				
				}
			if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE && valintamode) {
				frame.setVisible(false);
				
				}
				if(e.getVirtualKeyCode() == GlobalKeyEvent.VK_F10 && capturing) {
					try {
						stopCapture();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	
		canvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(valintamode==true) {
				mouseX2 = e.getXOnScreen();
				mouseY2 = e.getYOnScreen();
				try {
					if(mouseX2 - mouseX > 0 && mouseY2 - mouseY > 0) {
						capture(new Rectangle(mouseX, mouseY, mouseX2 - mouseX, mouseY2 - mouseY));
					}
					else {
						capture(new Rectangle(mouseX2, mouseY2, mouseX, mouseY));
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
		
		
		TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("catjam.gif"),"running");
		iconMenu(trayIcon);

		
		
     

   
		
		// tänne sit vaikka ne key listenerit jotka toteuttaa metodit riippuen
		// keybindista ?
//
//		try {
//			Input i = new Input();
//			
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
		
		

	}
	

}