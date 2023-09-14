package org.merrymike.soft;

import javax.swing.*;

public class PopupTimer {
    private static PopupTimer popupTimer;
    private Timer timer;
    private int countdown;

    public PopupTimer() {
    }

    public static synchronized PopupTimer getPopupTimer() {
        if (popupTimer == null) {
            popupTimer = new PopupTimer();
        }
        return popupTimer;
    }

    public synchronized void startPopupTimer(int minutes) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        countdown = minutes * 60;
        timer = new Timer(1000, e -> {
            countdown--;
            if (countdown <= 0) {
                timer.stop();
                PopupWindow popupWindow = new PopupWindow();
                popupWindow.showPopupWindow();
            }
            //System.out.println(showRemainingTime());
        });
        timer.start();
    }

    public synchronized void stopPopupTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public synchronized String showRemainingTime() {
        int minutes = countdown / 60;
        int seconds = countdown % 60;
        return minutes + ":" + seconds;
    }

}
