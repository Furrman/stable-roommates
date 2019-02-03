package Controller;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

/**
 * @class Main
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Startup class which invoke start of application.
 */
public class Main {
	
	/**
	 * @fn main
	 * @brief Startup method handle input parameters and invoke start application.
	 * @param args - standard input given from user. 
	 */
	public static void main(String[] args) {
		ViewController controller = new ViewController();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                switch (ke.getID()) {
                    case KeyEvent.KEY_RELEASED:
                        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                        	controller.makeNextStep();
                        }
                        break;
                }
                return false;
            }
        });
	}

}
