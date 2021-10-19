package gif;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import core.Main;

public class Capturer extends Main implements Runnable {
	Rectangle rectangle;

	public Capturer(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	@Override
	public void run() {
		while (capturing) {
			/*if(kuvatque.size()>10) {
				Buffer buf= new Buffer();
				Thread ThreadBuffer = new Thread(buf);
				ThreadBuffer.start();
			}*/
			System.out.println(Thread.activeCount());
			kierros++;
			long alkuaika = System.nanoTime();
			
			BufferedImage image = robot.createScreenCapture(rectangle);
		

			if (image.getHeight() > 720 || image.getWidth() > 1280) { // jos image on liian iso niin pienennetään resoa
				try {
					image = resizeImage(image, (int) (image.getWidth() / 1.5), (int) (image.getHeight() / 1.5));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			kuvaindex++;
			// if(kierros==1)
		
			try {
				kuvatque.put(image);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*
			 * else if(kierros==2) kuvatque2.add(image); else { kierros=0;
			 * kuvatque3.add(image); }
			 */
			long loppuaika = System.nanoTime();
			System.out.println("Lisääntynyt aika : " + (loppuaika - alkuaika) / 1000000.0 + "ms");

			try {
				int aika = (int) (INTERVAL - (loppuaika - alkuaika) / 1000000.0);
				if (aika > 0) {
					Thread.sleep(aika);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
