package it.polimi.se2019.util;

import it.polimi.se2019.network.server.GameThread;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class JarPath {
    public static String getJarPath () {
        String jarPath = GameThread.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath;
        try {
            decodedPath = URLDecoder.decode(jarPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decodedPath = "../";
        }

        return decodedPath;
    }

    private JarPath() {
    }
}
