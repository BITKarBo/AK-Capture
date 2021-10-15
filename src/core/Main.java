package core;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class Main {
	
	
	
	static boolean capturing=false; //kuvaus boolean
	
	static int x1=0; // valittu alue coordit?? placeholder voidaa miettii myöhemmi
	static int x2=0;
	static int y1=0;
	static int y2=0;
	
	//test

	
	public void capture() {
		if(capturing=false) {
			capturing=true;
		
			while (capturing) {
				//lisätään kuvii kansioon
			}
		}
	}
	public void stopCapture() {
		capturing=false;
		//tähän sit vaikka se gif luonti tai uus metodi
	}
		

	public static void main(String[] args) throws Exception {
		//tänne sit vaikka ne key listenerit jotka toteuttaa metodit riippuen keybindista ?
		 
		BufferedImage first = ImageIO.read(new File("/tmp/duke.jpg"));
        ImageOutputStream output = new FileImageOutputStream(new File("/tmp/example.gif"));

        GifWriter writer = new GifWriter(output, first.getType(), 250, true);
        writer.writeToSequence(first);

        File[] images = new File[]{
                new File("/tmp/duke-image-watermarked.jpg"),
                new File("/tmp/duke.jpg"),
                new File("/tmp/duke-text-watermarked.jpg"),
        };

        for (File image : images) {
            BufferedImage next = ImageIO.read(image);
            writer.writeToSequence(next);
        }

        writer.close();
        output.close();
		
		try {
	            Robot robot = new Robot();
	 
	            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	           
	            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
	            File file = new File("screen-capture.png");
	            boolean status = ImageIO.write(bufferedImage, "png", file);
	            System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
	 
	        } catch (AWTException | IOException ex) {
	            System.err.println(ex);
	        }
		 
		 
		 
		 

	}

}