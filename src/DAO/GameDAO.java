package DAO;

import Config.DatabaseConfig;
import Entity.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    public Game findById(int id) {
        Game game = null;
        String sql = "SELECT * FROM Game WHERE game_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setTitle(rs.getString("title"));
                game.setGenre(rs.getString("genre"));
                game.setReleaseDate(rs.getDate("release_date").toLocalDate());
                game.setStatus(rs.getString("status"));
                game.setVersion(rs.getFloat("version"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }

public void save(Game game) {
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

public void update(Game game) {
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

public void delete(int id) {
    String sql = "DELETE FROM Game WHERE game_id = ?";

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<Game> findAll() {
    List<Game> games = new ArrayList<>();
    String sql = "SELECT * FROM Game";

    try (Connection conn = DatabaseConfig.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Game game = new Game();
            game.setGameId(rs.getInt("game_id"));
            game.setTitle(rs.getString("title"));
            game.setGenre(rs.getString("genre"));
            game.setReleaseDate(rs.getDate("release_date").toLocalDate());
            game.setStatus(rs.getString("status"));
            game.setVersion(rs.getFloat("version"));
            games.add(game);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return games;
}
}
