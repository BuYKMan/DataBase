package ui;

import DAO.GameDAO;
import DAO.ProjectDAO;
import Entity.Game;
import model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GamePanel extends JPanel {
    private GameDAO gameDAO;
    private JTable gamesTable;
    private ProjectDAO projectDAO;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public GamePanel() {
        gameDAO = new GameDAO();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create table
        String[] columns = {"ID", "Title", "Genre", "Release Date", "Status", "Version"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gamesTable = new JTable(tableModel);
        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Game");
        editButton = new JButton("Edit Game");
        deleteButton = new JButton("Delete Game");
        JButton refreshButton = new JButton("Refresh");

        // Add button listeners
        addButton.addActionListener(e -> showAddGameDialog());
        editButton.addActionListener(e -> showEditGameDialog());
        deleteButton.addActionListener(e -> deleteSelectedGame());
        refreshButton.addActionListener(e -> refreshTable());

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Add components to panel
        add(new JScrollPane(gamesTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add selection listener
        gamesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        // Initial setup
        updateButtonStates();
        refreshTable();
    }

    private void updateButtonStates() {
        boolean rowSelected = gamesTable.getSelectedRow() != -1;
        editButton.setEnabled(rowSelected);
        deleteButton.setEnabled(rowSelected);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Game> games = gameDAO.findAll();
        for (Game game : games) {
            Object[] row = {
                    game.getGameId(),
                    game.getTitle(),
                    game.getGenre(),
                    game.getReleaseDate(),
                    game.getStatus(),
                    game.getVersion()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddGameDialog() {
        JDialog dialog = createGameDialog(null);
        dialog.setVisible(true);
    }

    private void showEditGameDialog() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) return;

        int gameId = (int) tableModel.getValueAt(selectedRow, 0);
        Game game = gameDAO.findById(gameId);

        JDialog dialog = createGameDialog(game);
        dialog.setVisible(true);
    }

    private JDialog createGameDialog(Game game) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                game == null ? "Add Game" : "Edit Game", true);
        dialog.setSize(400, 300);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form fields
        JTextField titleField = new JTextField(game != null ? game.getTitle() : "");
        JTextField genreField = new JTextField(game != null ? game.getGenre() : "");
        JTextField releaseDateField = new JTextField(
                game != null ? game.getReleaseDate().toString() : LocalDate.now().toString());
        JTextField statusField = new JTextField(game != null ? game.getStatus() : "");
        JTextField versionField = new JTextField(
                game != null ? String.valueOf(game.getVersion()) : "1.0");

        // Add form fields to panel
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Genre:"));
        formPanel.add(genreField);
        formPanel.add(new JLabel("Release Date (YYYY-MM-DD):"));
        formPanel.add(releaseDateField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Version:"));
        formPanel.add(versionField);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Game gameToSave = game != null ? game : new Game();
                gameToSave.setTitle(titleField.getText());
                gameToSave.setGenre(genreField.getText());
                gameToSave.setReleaseDate(LocalDate.parse(releaseDateField.getText()));
                gameToSave.setStatus(statusField.getText());
                gameToSave.setVersion(Float.parseFloat(versionField.getText()));

                if (game == null) {
                    gameDAO.save(gameToSave);
                } else {
                    gameDAO.update(gameToSave);
                }

                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Please check your input: " + ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }

    private void deleteSelectedGame() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) return;

        int gameId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this game?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            gameDAO.delete(gameId);
            refreshTable();
            updateButtonStates();
        }
    }


    // Complete the ProjectManagementPanel createProjectDialog method
    private JDialog createProjectDialog(Project project) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                project == null ? "Add Project" : "Edit Project", true);
        dialog.setSize(400, 300);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form fields
        JTextField nameField = new JTextField(project != null ? project.getProjectName() : "");
        JTextField startDateField = new JTextField(
                project != null ? project.getStartDate().toString() : LocalDate.now().toString());
        JTextField endDateField = new JTextField(
                project != null && project.getEndDate() != null ? project.getEndDate().toString() : "");
        JTextField statusField = new JTextField(project != null ? project.getStatus() : "");
        JTextField budgetField = new JTextField(
                project != null ? project.getBudget().toString() : "0.00");
        JTextField managerIdField = new JTextField(
                project != null ? String.valueOf(project.getManagerId()) : "");
        JTextField gameIdField = new JTextField(
                project != null ? String.valueOf(project.getGameId()) : "");

        // Add form fields to panel
        formPanel.add(new JLabel("Project Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(endDateField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Budget:"));
        formPanel.add(budgetField);
        formPanel.add(new JLabel("Manager ID:"));
        formPanel.add(managerIdField);
        formPanel.add(new JLabel("Game ID:"));
        formPanel.add(gameIdField);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Project projectToSave = project != null ? project : new Project();
                projectToSave.setProjectName(nameField.getText());
                projectToSave.setStartDate(LocalDate.parse(startDateField.getText()));
                projectToSave.setEndDate(endDateField.getText().isEmpty() ? null :
                        LocalDate.parse(endDateField.getText()));
                projectToSave.setStatus(statusField.getText());
                projectToSave.setBudget(new BigDecimal(budgetField.getText()));
                projectToSave.setManagerId(Integer.parseInt(managerIdField.getText()));
                projectToSave.setGameId(Integer.parseInt(gameIdField.getText()));

                if (project == null) {
                    projectDAO.save(projectToSave);
                } else {
                    projectDAO.update(projectToSave);
                }

                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Please check your input: " + ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }
}


