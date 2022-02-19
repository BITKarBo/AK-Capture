package core;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
/**
 * Ikkuna joka jää näkyviin, mikäli alue on maalattu
 * BUG !!! Ei seuraa kuin yhdellä näytöllä, koitin lisätä alueen kokoa mutta performance kärsii rajusti. Olisi hyvä kun koko frame olisi alueen kokoinen ja se liikkuisi mukana.
 */
public class Näkyväikkuna extends JFrame {
int x,y,w,h;

boolean ymp=false;
  public Näkyväikkuna(int x,int y,int w,int h,boolean ymp) {
      this.setUndecorated(true);
      this.w=w+1;
      this.x=x-1;
      this.y=y-1;
      this.h=h+1;
      this.ymp=ymp;
      final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(screenSize.width, screenSize.height);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.setBackground(new Color(0, 0, 0, 0));
      this.setOpacity(1f);
      this.setAlwaysOnTop(true);
      this.setVisible(false);
      enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
      repaint();
      
  }

public void movee(int x,int y) {
      this.x=x-1;
      this.y=y-1;
  
      repaint();
      this.setBackground(new Color(0, 0, 0, 0));
}


  @Override
  public void paint(Graphics g) {
	 
    Graphics gfx=(Graphics)g;

    gfx.setColor(Main.väri);

    if(ymp)
    	 gfx.drawOval(x,y,w,h);
    else 
    	  gfx.drawRect(x,y,w,h);
  


  }
}
