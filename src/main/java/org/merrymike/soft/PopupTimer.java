package org.merrymike.soft;

import javax.swing.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PopupTimer {
    private static PopupTimer popupTimer;
    private final Properties properties = PropertiesManager.getProperties();
    private Timer timer;
    private final AtomicInteger countdown = new AtomicInteger();
    private Consumer<String> updateListener;

    private PopupTimer() {
    }

    public static synchronized PopupTimer getPopupTimer() {
        if (popupTimer == null) {
            popupTimer = new PopupTimer();
        }
        return popupTimer;
    }

    public synchronized void startPopupTimer() {
        int minutes = Integer.parseInt(properties.getProperty("time"));

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        countdown.set(minutes * 60);

        timer = new Timer(1000, e -> {
            int remainingTime = countdown.decrementAndGet();
            String formattedTime = showRemainingTime();
            if (updateListener != null) {
                updateListener.accept(formattedTime);
            }
            if (remainingTime <= 0) {
                timer.stop();
                new PopupWindow();
            }
            //System.out.println(formattedTime);
        });
        timer.start();
    }

    public synchronized void stopPopupTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public synchronized String showRemainingTime() {
        int minutes = countdown.get() / 60;
        int seconds = countdown.get() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void addUpdateListener(Consumer<String> listener) {
        updateListener = listener;
    }
}
