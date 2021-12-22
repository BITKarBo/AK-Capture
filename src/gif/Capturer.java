package gif;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import core.Main;

public class Capturer extends Main implements Runnable {

	Rectangle rectangle;

	public Capturer(Rectangle rectangle) {
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("res/rec.gif"));
		trayIcon.setToolTip("Capturing...");
		this.rectangle = rectangle;
	}

	@Override
	synchronized public void run() {
		Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		while (capturing) {
			/*
			 * if(kuvatque.size()>10) { Buffer buf= new Buffer(); Thread ThreadBuffer = new
			 * Thread(buf); ThreadBuffer.start(); }
			 */

			// kierros++;
			long alkuaika = System.nanoTime();
			
			if(following) {
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();
				rectangle.setLocation(x-((int)rectangle.getWidth()/2), y-(int)(rectangle.getHeight()/2));
			}
			BufferedImage image =robot.createScreenCapture(rectangle);
			try {
				if (image.getHeight() > 720 || image.getWidth() > 1280) {
					image = BIResizeColor(image);
				}

				if (ympyrä)
					image = makeCircle(image);

				frames++;
				kuvaindex++;

				kuvatque.add(image);
				if (kuvatque.size() > 270) {
					trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("res/overbuffer.gif"));
					trayIcon.setToolTip("Cant keep up");
				}

				kuvanottoviive = ((System.nanoTime() - alkuaika) / 1000000.0);

				int aika = (int) (INTERVAL - kuvanottoviive);

				if (aika > 0) {
					Thread.sleep(aika);
				}

				Finalkuvanottoviive = kuvanottoviive + aika;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage("res/buffering16.gif"));
		trayIcon.setToolTip("Buffering...");

	}

}
