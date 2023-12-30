package org.merrymike.soft;

import java.awt.*;

public class TrayIconHandler {
    private static TrayIconHandler trayIconHandler;
    private final PopupTimer popupTimer = PopupTimer.getPopupTimer();
    private final Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png"));

    private TrayIconHandler() {
    }

    public static synchronized TrayIconHandler getTreyIconHandler() {
        if (trayIconHandler == null) {
            trayIconHandler = new TrayIconHandler();
        }
        return trayIconHandler;
    }

    public TrayIcon getTrayIcon() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem showSentence = getShowSentence();
        MenuItem exit = getExit();
        MenuItem remainingTime = getRemainingTime();
        popupMenu.add(showSentence);
        popupMenu.add(remainingTime);
        popupMenu.addSeparator();
        popupMenu.add(exit);
        TrayIcon trayIcon = new TrayIcon(image, "LingoLens", popupMenu);
        trayIcon.setImageAutoSize(true);
        return trayIcon;
    }

    private synchronized MenuItem getRemainingTime() {
        MenuItem remainingTime = new MenuItem(popupTimer.showRemainingTime());
        popupTimer.addUpdateListener(remainingTime::setLabel);
        return remainingTime;
    }

    private MenuItem getShowSentence() {
        MenuItem showSentence = new MenuItem("Show sentence");
        showSentence.addActionListener(e -> {
            PopupWindow popupWindow = new PopupWindow();
            popupWindow.showPopupWindow();
            System.gc();
        });
        return showSentence;
    }

    private MenuItem getExit() {
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        return exit;
    }

}
