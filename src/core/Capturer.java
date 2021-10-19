package core;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Capturer extends Main implements Runnable {
	Rectangle rectangle;

	Capturer(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	@Override
	public void run() {
		while (capturing) {
			kierros++;
			long alkuaika = System.nanoTime();

			BufferedImage image = robot.createScreenCapture(rectangle);
			image.setAccelerationPriority(0);

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
			kuvatque.add(image);

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
