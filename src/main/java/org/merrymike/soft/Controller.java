package org.merrymike.soft;

import java.awt.*;

public class Controller {

    private static final PopupTimer popupTimer = PopupTimer.getPopupTimer();

    public static void main(String[] args) throws AWTException {
        TrayIconHandler trayIconHandler = TrayIconHandler.getTreyIconHandler();
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIconHandler.getTrayIcon());
            popupTimer.startPopupTimer();
        }
    }
}