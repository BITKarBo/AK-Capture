package core;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class Main{

	static ArrayList<File> kuvat = new ArrayList<File>();
	static File tmp = new File("tmp/");
	static File output = new File("output/");
	static String format = ".png";
	static String finalformat = ".gif";
	static String imageName = "image";
	
	static boolean capturing = false; // kuvaus boolean
	
	
	
	static int x1 = 0; // valittu alue coordit?? placeholder voidaa miettii myöhemmi
	static int x2 = 0;
	static int y1 = 0;
	static int y2 = 0;
	static int interval = 50;
	static int kuvamaara = 5;
	static int kuvaindex = 0;
	static int delay = 50;

	static Timer timer = new Timer();

	public static void capture() throws AWTException {

		if (!capturing) {
			
			capturing = true;
			
			Robot robot = new Robot();
			Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

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
	
	public static void alustus() {
		//Alustaa kansion jos ei löydy
		kuvaindex = 0;
		if(!tmp.exists())
			tmp.mkdir();
		if(!output.exists())
			output.mkdir();
	}

	public static void main(String[] args) throws AWTException {
		alustus();
		
		
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
		
		capture();

	}


}