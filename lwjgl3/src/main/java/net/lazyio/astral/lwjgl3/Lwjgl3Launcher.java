package net.lazyio.astral.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.lazyio.astral.Astral;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        boolean dg = false;
        for (String arg : args) {
            if (arg.equals("-debug")) {
                System.out.println(true);
                dg = true;
                break;
            }
        }
        createApplication(dg);
    }

    private static Lwjgl3Application createApplication(boolean debug) {
        return new Lwjgl3Application(new Astral(debug), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Astral");
        configuration.setWindowedMode(1280, 720);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}