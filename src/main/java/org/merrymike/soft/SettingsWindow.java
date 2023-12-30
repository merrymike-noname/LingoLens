package org.merrymike.soft;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteElectricLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class SettingsWindow extends JFrame {
    private final Properties properties = PropertiesManager.getProperties();
    private final JSpinner minSpinner;
    private final JSpinner maxSpinner;
    private final JSpinner timeSpinner;
    private final JComboBox<String> languageComboBox;
    private final JComboBox<String> translationComboBox;

    private static final Map<String, String> LANGUAGE_MAP = new HashMap<>();
    static {
        LANGUAGE_MAP.put("English", "eng");
        LANGUAGE_MAP.put("Ukrainian", "ukr");
        LANGUAGE_MAP.put("Russian", "rus");
        LANGUAGE_MAP.put("German", "deu");
        LANGUAGE_MAP.put("French", "fra");
        LANGUAGE_MAP.put("Spanish", "spa");
        LANGUAGE_MAP.put("Bulgarian", "bul");
        LANGUAGE_MAP.put("Polish", "pol");
    }

    public SettingsWindow() {
        setTitle("Settings");
        setResizable(false);

        SwingUtilities.invokeLater(() -> {
            setDefaultLookAndFeelDecorated(true);
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteElectricLookAndFeel());
            } catch (Exception e) {
                System.out.println("Substance error");
            }
        });

        JPanel contentPanel = new JPanel();
        JPanel settingsPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        settingsPanel.setLayout(new GridLayout(5, 5, 20, 5));

        contentPanel.setLayout(new BorderLayout());

        Label languageLabel = new Label("From language:");
        Label translationLabel = new Label("Translate to:");
        Label minLabel = new Label("Minimum words:");
        Label maxLabel = new Label("Maximum words:");
        Label timeLabel = new Label("Popup cooldown (minutes):");

        languageComboBox = new JComboBox<>(LANGUAGE_MAP.keySet().toArray(new String[0]));
        String fromLanguage = properties.getProperty("from");
        if (fromLanguage != null && LANGUAGE_MAP.containsValue(fromLanguage)) {
            for (Map.Entry<String, String> entry : LANGUAGE_MAP.entrySet()) {
                if (entry.getValue().equals(fromLanguage)) {
                    languageComboBox.setSelectedItem(entry.getKey());
                    break;
                }
            }
        }
        translationComboBox = new JComboBox<>(LANGUAGE_MAP.keySet().toArray(new String[0]));
        fromLanguage = properties.getProperty("to");
        if (fromLanguage != null && LANGUAGE_MAP.containsValue(fromLanguage)) {
            for (Map.Entry<String, String> entry : LANGUAGE_MAP.entrySet()) {
                if (entry.getValue().equals(fromLanguage)) {
                    translationComboBox.setSelectedItem(entry.getKey());
                    break;
                }
            }
        }

        SpinnerModel minSpinnerModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("min")), 1, 10, 1);
        SpinnerModel maxSpinnerModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("max")), 1, 10, 1);
        SpinnerModel timeSpinnerModel = new SpinnerNumberModel(Integer.parseInt(properties.getProperty("time")), 1, 120, 1);
        minSpinner = new JSpinner(minSpinnerModel);
        maxSpinner = new JSpinner(maxSpinnerModel);
        timeSpinner = new JSpinner(timeSpinnerModel);


        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> updateProperties());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonPanel.add(closeButton);

        settingsPanel.add(languageLabel);
        settingsPanel.add(languageComboBox);
        settingsPanel.add(translationLabel);
        settingsPanel.add(translationComboBox);
        settingsPanel.add(minLabel);
        settingsPanel.add(minSpinner);
        settingsPanel.add(maxLabel);
        settingsPanel.add(maxSpinner);
        settingsPanel.add(timeLabel);
        settingsPanel.add(timeSpinner);

        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(settingsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(contentPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateProperties() {

        if ((int) minSpinner.getValue() > (int) maxSpinner.getValue()) {
            JOptionPane.showMessageDialog(null, "'Min' should not be greater, than 'Max'", "Incorrect settings", JOptionPane.ERROR_MESSAGE);
        } else if (Objects.equals(languageComboBox.getSelectedItem(), translationComboBox.getSelectedItem())) {
            JOptionPane.showMessageDialog(null, "Languages should be different", "Incorrect settings", JOptionPane.ERROR_MESSAGE);
        } else {
            properties.setProperty("from", LANGUAGE_MAP.get((String) languageComboBox.getSelectedItem()));
            properties.setProperty("to", LANGUAGE_MAP.get((String) translationComboBox.getSelectedItem()));
            properties.setProperty("min", String.valueOf(minSpinner.getValue()));
            properties.setProperty("max", String.valueOf(maxSpinner.getValue()));
            properties.setProperty("time", String.valueOf(timeSpinner.getValue()));
            PopupTimer.getPopupTimer().startPopupTimer();

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/application.properties")) {
                properties.store(fos, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            dispose();
        }
    }

}
