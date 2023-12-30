package org.merrymike.soft;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteElectricLookAndFeel;

public class PopupWindow {

    private final SentenceGenerator sentenceGenerator = new SentenceGenerator();
    private List<String> sentencesList;
    private final JDialog popup;
    private final PopupTimer popupTimer = PopupTimer.getPopupTimer();

    public PopupWindow() {
        JFrame frame = new JFrame();
        popup = new JDialog(frame, "LingoLens", Dialog.ModalityType.APPLICATION_MODAL);
        Image image = Toolkit.getDefaultToolkit().getImage("icon.ico");
        popup.setIconImage(image);
        popup.setResizable(false);

        int windowWidth = 350;
        int windowHeight = 150;
        popup.setPreferredSize(new Dimension(windowWidth, windowHeight));
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int windowX = screenWidth - windowWidth - 10;
        int windowY = screenHeight - windowHeight - 55;
        popup.setBounds(windowX, windowY, windowWidth, windowHeight);
        popup.setUndecorated(true);

        showPopupWindow();
    }

    public void showPopupWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteElectricLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }

            sentencesList = sentenceGenerator.getSentences();
            JPanel panel = getjPanel();

            popup.getContentPane().removeAll();
            popup.add(panel);
            popup.revalidate();
            popup.repaint();
            popup.setVisible(true);
        });
    }

    private JPanel getjPanel() {
        popupTimer.stopPopupTimer();
        JLabel sentenceLabel = new JLabel(getSentence());
        sentenceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel translationLabel = new JLabel(getTranslations());
        translationLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton closeButton = new JButton("x");
        closeButton.setFocusable(false);
        closeButton.addActionListener(e -> {
            popup.dispose();
            System.gc();
            popupTimer.startPopupTimer();
        });

        JButton nextButton = new JButton(">");
        nextButton.setFocusable(false);
        nextButton.addActionListener(e -> {
            popup.dispose();
            System.gc();
            showPopupWindow();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
        //labelPanel.setBorder(new LineBorder(Color.BLACK, 2));
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sentenceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        labelPanel.add(sentenceLabel);

        translationLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        labelPanel.add(translationLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        //buttonPanel.setBorder(new LineBorder(Color.BLACK, 2));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(nextButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(closeButton);

        panel.add(labelPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        return panel;
    }

    private String getSentence() {
        return "<html>" + sentencesList.get(0) + "</html>";
    }

    private String getTranslations() {
        return "<html>" + sentencesList.get(1) + "</html>";
    }

}
