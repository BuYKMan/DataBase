package Entity;

import java.time.LocalDate;

public class Game {
    private int gameId;
    private String title;
    private String genre;

    private LocalDate releaseDate;
    private String status;
    private float version;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public Game(int gameId, String title, String genre, LocalDate releaseDate, String status, float version) {
        this.gameId = gameId;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.status = status;
        this.version = version;
    }

    public Game() {
    }
}
