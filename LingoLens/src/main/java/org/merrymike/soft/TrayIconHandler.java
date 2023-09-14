package org.merrymike.soft;

import java.awt.*;
import javax.swing.*;

public class TrayIconHandler {
    private static TrayIconHandler trayIconHandler;
    private final PopupTimer popupTimer = PopupTimer.getPopupTimer();
    private Timer timer;

    private TrayIconHandler() {
    }

    public static synchronized TrayIconHandler getTreyIconHandler() {
        if (trayIconHandler == null) {
            trayIconHandler = new TrayIconHandler();
        }
        return trayIconHandler;
    }

    public TrayIcon getTrayIcon() {
        Image image = Toolkit.getDefaultToolkit().getImage("icon.ico");
        PopupMenu popupMenu = new PopupMenu();
        MenuItem showSentence = getShowSentence();
        MenuItem exit = getExit();
        MenuItem remainingTime = getRemainingTime();
        popupMenu.add(showSentence);
        popupMenu.add(remainingTime);
        popupMenu.addSeparator();
        popupMenu.add(exit);
        return new TrayIcon(image, "LingoLens", popupMenu);
    }

    private synchronized MenuItem getRemainingTime() {
        MenuItem remainingTime = new MenuItem(popupTimer.showRemainingTime());
        timer = new Timer(1000, a -> {
            remainingTime.setLabel(popupTimer.showRemainingTime());
        });
        timer.start();
        return remainingTime;
    }

    private MenuItem getShowSentence() {
        MenuItem showSentence = new MenuItem("Show sentence");
        showSentence.addActionListener(e -> {
            PopupWindow popupWindow = new PopupWindow();
            popupWindow.showPopupWindow();
        });
        return showSentence;
    }

    private MenuItem getExit() {
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        return exit;
    }

}
