package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

private boolean[] keys = new boolean[65536];

public static final int f1 = KeyEvent.VK_F1; // key code of f1 key

public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
}

public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
  
}

public void keyTyped(KeyEvent e) {
}

public boolean isKeyPressed(int key) {
    return keys[key];
}
}
