package org.merrymike.soft;

import java.awt.*;

public class Controller {

    private static final PopupTimer popupTimer = PopupTimer.getPopupTimer();

    public static void main(String[] args) throws AWTException {
        TrayIconHandler trayIconHandler = new TrayIconHandler();
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIconHandler.getTrayIcon());
            new PopupWindow();
            //popupWindow.showPopupWindow();
            popupTimer.startPopupTimer();
        }
    }
}