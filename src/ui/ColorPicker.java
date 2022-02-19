package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import core.Main;

public class ColorPicker extends JFrame implements ActionListener {    

ColorPicker(){    
   
    Color initialcolor=Main.väri;    
 Main.väri=JColorChooser.showDialog(this,"Select a color",initialcolor);    
  this.dispose();
}
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}    

}