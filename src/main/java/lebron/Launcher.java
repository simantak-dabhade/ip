package lebron;

import lebron.gui.MainWindow;

/**
 * A launcher class to workaround classpath issues when using JavaFX.
 * 
 * This class serves as the entry point for the JavaFX application.
 * It's needed because of how the module system works with JavaFX -
 * the main class shouldn't directly extend Application.
 */
public class Launcher {
    public static void main(String[] args) {
        MainWindow.main(args);
    }
}