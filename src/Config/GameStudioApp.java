package Config;

import ui.GamePanel;
import ui.ProjectManagementPanel;

import javax.swing.*;
import java.awt.*;

public class GameStudioApp extends JFrame {
    private JTabbedPane tabbedPane;
    private GamePanel gamePanel;

    public GameStudioApp() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Game Studio Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create and add panels
        GamePanel gamePanel = new GamePanel();
        ProjectManagementPanel projectPanel = new ProjectManagementPanel();

        tabbedPane.addTab("Games", createTabPanel(gamePanel, "Game Management"));
        tabbedPane.addTab("Projects", createTabPanel(projectPanel, "Project Management"));

        // Add tabbed pane to frame
        add(tabbedPane);

        // Center on screen
        setLocationRelativeTo(null);
    }

    private JPanel createTabPanel(JPanel contentPanel, String title) {
        JPanel tabPanel = new JPanel(new BorderLayout());

        // Create header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Add header and content
        tabPanel.add(headerPanel, BorderLayout.NORTH);
        tabPanel.add(contentPanel, BorderLayout.CENTER);

        return tabPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            GameStudioApp app = new GameStudioApp();
            app.setVisible(true);
        });
    }
}
