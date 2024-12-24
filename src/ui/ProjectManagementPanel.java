package ui;

import DAO.ProjectDAO;
import DAO.TaskDAO;
import model.Project;
import model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProjectManagementPanel extends JPanel {
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private JTable projectsTable;
    private JTable tasksTable;
    private DefaultTableModel projectTableModel;
    private DefaultTableModel taskTableModel;
    private JButton addProjectButton;
    private JButton editProjectButton;
    private JButton deleteProjectButton;
    private JButton addTaskButton;
    private JButton editTaskButton;
    private JButton deleteTaskButton;

    public ProjectManagementPanel() {
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Equal resize weight

        // Projects Panel
        JPanel projectsPanel = createProjectsPanel();

        // Tasks Panel
        JPanel tasksPanel = createTasksPanel();

        // Add panels to split pane
        splitPane.setTopComponent(projectsPanel);
        splitPane.setBottomComponent(tasksPanel);

        // Add split pane to main panel
        add(splitPane, BorderLayout.CENTER);

        // Initialize button states
        updateButtonStates();

        // Initial data load
        refreshProjectsTable();
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create projects table
        String[] projectColumns = {"ID", "Name", "Start Date", "End Date", "Status", "Budget"};
        projectTableModel = new DefaultTableModel(projectColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        projectsTable = new JTable(projectTableModel);
        projectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add selection listener
        projectsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleProjectSelection();
            }
        });

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addProjectButton = new JButton("Add Project");
        editProjectButton = new JButton("Edit Project");
        deleteProjectButton = new JButton("Delete Project");

        // Add button listeners
        addProjectButton.addActionListener(e -> showAddProjectDialog());
        editProjectButton.addActionListener(e -> showEditProjectDialog());
        deleteProjectButton.addActionListener(e -> deleteSelectedProject());

        buttonPanel.add(addProjectButton);
        buttonPanel.add(editProjectButton);
        buttonPanel.add(deleteProjectButton);

        // Add components to panel
        panel.add(new JLabel("Projects", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(projectsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create tasks table
        String[] taskColumns = {"ID", "Title", "Priority", "Status", "Due Date"};
        taskTableModel = new DefaultTableModel(taskColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tasksTable = new JTable(taskTableModel);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add selection listener
        tasksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addTaskButton = new JButton("Add Task");
        editTaskButton = new JButton("Edit Task");
        deleteTaskButton = new JButton("Delete Task");

        // Add button listeners
        addTaskButton.addActionListener(e -> showAddProjectDialog());
        editTaskButton.addActionListener(e -> showEditProjectDialog());
        deleteTaskButton.addActionListener(e -> deleteSelectedTask());

        buttonPanel.add(addTaskButton);
        buttonPanel.add(editTaskButton);
        buttonPanel.add(deleteTaskButton);

        // Add components to panel
        panel.add(new JLabel("Tasks", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(tasksTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleProjectSelection() {
        int selectedRow = projectsTable.getSelectedRow();
        if (selectedRow != -1) {
            int projectId = (int) projectTableModel.getValueAt(selectedRow, 0);
            refreshTasksTable(projectId);
        } else {
            taskTableModel.setRowCount(0);
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean projectSelected = projectsTable.getSelectedRow() != -1;
        boolean taskSelected = tasksTable.getSelectedRow() != -1;

        editProjectButton.setEnabled(projectSelected);
        deleteProjectButton.setEnabled(projectSelected);
        addTaskButton.setEnabled(projectSelected);
        editTaskButton.setEnabled(taskSelected);
        deleteTaskButton.setEnabled(taskSelected);
    }

    private void refreshProjectsTable() {
        projectTableModel.setRowCount(0);
        List<Project> projects = projectDAO.findAll();
        for (Project project : projects) {
            Object[] row = {
                    project.getProjectId(),
                    project.getProjectName(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getStatus(),
                    project.getBudget()
            };
            projectTableModel.addRow(row);
        }
    }

    private void refreshTasksTable(int projectId) {
        taskTableModel.setRowCount(0);
        List<Task> tasks = taskDAO.findByProjectId(projectId);
        for (Task task : tasks) {
            Object[] row = {
                    task.getTaskId(),
                    task.getTitle(),
                    task.getPriority(),
                    task.getStatus(),
                    task.getDueDate()
            };
            taskTableModel.addRow(row);
        }
    }

    // Project Dialog methods (Add/Edit)
    private void showAddProjectDialog() {
        JDialog dialog = createProjectDialog(null);
        dialog.setVisible(true);
    }

    private void showEditProjectDialog() {
        int selectedRow = projectsTable.getSelectedRow();
        if (selectedRow == -1) return;

        int projectId = (int) projectTableModel.getValueAt(selectedRow, 0);
        Project project = projectDAO.findById(projectId);

        JDialog dialog = createProjectDialog(project);
        dialog.setVisible(true);
    }

    private JDialog createProjectDialog(Project project) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                project == null ? "Add Project" : "Edit Project", true);
        dialog.setSize(400, 400);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form fields
        JLabel nameLabel = new JLabel("Project Name:");
        JTextField nameField = new JTextField(project != null ? project.getProjectName() : "");

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        JTextField startDateField = new JTextField(project != null ? project.getStartDate().toString() : "");

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        JTextField endDateField = new JTextField(project != null && project.getEndDate() != null
                ? project.getEndDate().toString()
                : "");

        JLabel statusLabel = new JLabel("Status:");
        JTextField statusField = new JTextField(project != null ? project.getStatus() : "");

        JLabel budgetLabel = new JLabel("Budget:");
        JTextField budgetField = new JTextField(project != null ? project.getBudget().toString() : "");

        JLabel managerIdLabel = new JLabel("Manager ID:");
        JTextField managerIdField = new JTextField(project != null ? String.valueOf(project.getManagerId()) : "");

        // Add fields to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(startDateLabel);
        formPanel.add(startDateField);
        formPanel.add(endDateLabel);
        formPanel.add(endDateField);
        formPanel.add(statusLabel);
        formPanel.add(statusField);
        formPanel.add(budgetLabel);
        formPanel.add(budgetField);
        formPanel.add(managerIdLabel);
        formPanel.add(managerIdField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners
        saveButton.addActionListener(e -> {
            try {
                String projectName = nameField.getText().trim();
                LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                LocalDate endDate = endDateField.getText().trim().isEmpty()
                        ? null
                        : LocalDate.parse(endDateField.getText().trim());
                String status = statusField.getText().trim();
                BigDecimal budget = new BigDecimal(budgetField.getText().trim());
                int managerId = Integer.parseInt(managerIdField.getText().trim());

                Project newProject = project != null ? project : new Project();
                newProject.setProjectName(projectName);
                newProject.setStartDate(startDate);
                newProject.setEndDate(endDate);
                newProject.setStatus(status);
                newProject.setBudget(budget);
                newProject.setManagerId(managerId);

                ProjectDAO projectDAO = new ProjectDAO();
                if (project == null) {
                    projectDAO.save(newProject);
                } else {
                    projectDAO.update(newProject);
                }

                JOptionPane.showMessageDialog(dialog, "Project saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving project: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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


    // Similar methods for Task dialogs
    // ... (Add your task dialog methods here)

    private void deleteSelectedProject() {
        int selectedRow = projectsTable.getSelectedRow();
        if (selectedRow == -1) return;

        int projectId = (int) projectTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this project and all its tasks?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            projectDAO.delete(projectId);
            refreshProjectsTable();
            taskTableModel.setRowCount(0);
            updateButtonStates();
        }
    }


    private void deleteSelectedTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow == -1) return;

        int taskId = (int) taskTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            taskDAO.delete(taskId);
            refreshTasksTable(getSelectedProjectId());
            updateButtonStates();
        }
    }

    private int getSelectedProjectId() {
        int selectedRow = projectsTable.getSelectedRow();
        return selectedRow != -1 ? (int) projectTableModel.getValueAt(selectedRow, 0) : -1;
    }
}