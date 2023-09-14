package org.merrymike.soft;

import java.awt.*;

public class Controller {
    public static void main(String[] args) throws AWTException {
        TrayIconHandler trayIconHandler = TrayIconHandler.getTreyIconHandler();
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIconHandler.getTrayIcon());
        }
    }
}