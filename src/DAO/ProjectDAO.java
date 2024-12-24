package DAO;

import Config.DatabaseConfig;
import Entity.Game;
import model.Project;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // FIND BY ID: Project için
    public Project findById(int id) {
        Project project = null;
        String sql = "SELECT * FROM Project WHERE project_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setStartDate(rs.getDate("start_date").toLocalDate());
                project.setEndDate(rs.getDate("end_date") != null ?
                        rs.getDate("end_date").toLocalDate() : null);
                project.setStatus(rs.getString("status"));
                project.setBudget(rs.getBigDecimal("budget"));
                project.setManagerId(rs.getInt("manager_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    // SAVE: Project için
    public void save(Project project) {
        String sql = "INSERT INTO Project (project_name, start_date, end_date, status, budget, manager_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, project.getProjectName());
            pstmt.setDate(2, Date.valueOf(project.getStartDate()));
            pstmt.setDate(3, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setString(4, project.getStatus());
            pstmt.setBigDecimal(5, project.getBudget());
            pstmt.setInt(6, project.getManagerId());

            // Debugging
            System.out.println("Executing SQL: " + pstmt);

            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Project saved successfully.");
            } else {
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving project: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // UPDATE: Project için
    public void update(Project project) {
        String sql = "UPDATE Project SET project_name = ?, start_date = ?, end_date = ?, status = ?, budget = ?, manager_id = ? " +
                "WHERE project_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, project.getProjectName());
            pstmt.setDate(2, Date.valueOf(project.getStartDate()));
            pstmt.setDate(3, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setString(4, project.getStatus());
            pstmt.setBigDecimal(5, project.getBudget());
            pstmt.setInt(6, project.getManagerId());
            pstmt.setInt(7, project.getProjectId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE: Project için
    public void delete(int id) {
        String sql = "DELETE FROM Project WHERE project_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // FIND ALL: Project için
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Project project = new Project();
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setStartDate(rs.getDate("start_date").toLocalDate());
                project.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
                project.setStatus(rs.getString("status"));
                project.setBudget(rs.getBigDecimal("budget"));
                project.setManagerId(rs.getInt("manager_id"));
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    // Game CRUD İşlemleri
    // SAVE: Game için
    public void saveGame(Game game) {
        String sql = "INSERT INTO Game (title, genre, release_date, status, version) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getGenre());
            pstmt.setDate(3, Date.valueOf(game.getReleaseDate()));
            pstmt.setString(4, game.getStatus());
            pstmt.setFloat(5, game.getVersion());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE: Game için
    public void deleteGame(int id) {
        String sql = "DELETE FROM Game WHERE game_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE: Game için
    public void updateGame(Game game) {
        String sql = "UPDATE Game SET title = ?, genre = ?, release_date = ?, status = ?, version = ? WHERE game_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getGenre());
            pstmt.setDate(3, Date.valueOf(game.getReleaseDate()));
            pstmt.setString(4, game.getStatus());
            pstmt.setFloat(5, game.getVersion());
            pstmt.setInt(6, game.getGameId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
