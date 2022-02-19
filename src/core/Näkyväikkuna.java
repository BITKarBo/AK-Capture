package core;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
/**
 * Ikkuna joka j�� n�kyviin, mik�li alue on maalattu
 * BUG !!! Ei seuraa kuin yhdell� n�yt�ll�, koitin lis�t� alueen kokoa mutta performance k�rsii rajusti. Olisi hyv� kun koko frame olisi alueen kokoinen ja se liikkuisi mukana.
 */
public class N�kyv�ikkuna extends JFrame {
int x,y,w,h;

boolean ymp=false;
  public N�kyv�ikkuna(int x,int y,int w,int h,boolean ymp) {
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

    gfx.setColor(Main.v�ri);

    if(ymp)
    	 gfx.drawOval(x,y,w,h);
    else 
    	  gfx.drawRect(x,y,w,h);
  


  }
}
