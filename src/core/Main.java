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
	
	static boolean capturing = false; // kuvaus boolean

	
	static int x1 = 0; // valittu alue coordit?? placeholder voidaa miettii myöhemmi
	static int x2 = 0;
	static int y1 = 0;
	static int y2 = 0;
	static int interval = 50;
	static int kuvaindex = 0;
	static int delay = 50;
	// test

	static Timer timer = new Timer();

	public static void capture() {

		if (capturing == false) {
			capturing = true;
			
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					kuvaindex++;
					try {
						Robot robot = new Robot();

						Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

						BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
						File file = new File("tmp/" + kuvaindex + ".png");
						kuvat.add(file);
						boolean status = ImageIO.write(bufferedImage, "png", file);
						System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());

						if(kuvaindex >= 25)
							try {
								stopCapture();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
					} catch (AWTException | IOException ex) {
						System.err.println(ex);
					}

				}
			}, 0, interval

			);

		}
	}

	public static void stopCapture() throws Exception {
		timer.cancel();
		kuvaindex = 0;
		capturing = false;

		BufferedImage first = ImageIO.read(new File("tmp/1.png"));
		ImageOutputStream gif = new FileImageOutputStream(new File("tmp/gifisi.gif"));

		GifWriter writer = new GifWriter(gif, first.getType(), delay, true);
		writer.writeToSequence(first);

		
		for (File i : kuvat) {
			BufferedImage next = ImageIO.read(i);
			writer.writeToSequence(next);
		}

		writer.close();
		gif.close();
		
		// tähän sit vaikka se gif luonti tai uus metodi
	}

	public static void main(String[] args) {
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