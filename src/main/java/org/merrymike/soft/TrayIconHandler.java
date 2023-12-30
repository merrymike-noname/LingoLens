package org.merrymike.soft;

import java.awt.*;

public class TrayIconHandler {
    private final PopupTimer popupTimer = PopupTimer.getPopupTimer();
    private final Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png"));
    private SettingsWindow settingsWindow = null;

    public TrayIcon getTrayIcon() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem showSentence = getShowSentence();
        MenuItem remainingTime = getRemainingTime();
        MenuItem settings = getSettings();
        MenuItem exit = getExit();
        popupMenu.add(showSentence);
        popupMenu.add(remainingTime);
        popupMenu.add(settings);
        popupMenu.addSeparator();
        popupMenu.add(exit);
        TrayIcon trayIcon = new TrayIcon(image, "LingoLens", popupMenu);
        trayIcon.addActionListener(e -> {
            PopupWindow popupWindow = new PopupWindow();
            popupWindow.showPopupWindow();
        });
        trayIcon.setImageAutoSize(true);
        return trayIcon;
    }

    private MenuItem getSettings() {
        MenuItem settings = new MenuItem("Settings");
        settings.addActionListener(e -> {
            if (settingsWindow != null) {
                settingsWindow.dispose();
            }
            settingsWindow = new SettingsWindow();
        });
        return settings;
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
