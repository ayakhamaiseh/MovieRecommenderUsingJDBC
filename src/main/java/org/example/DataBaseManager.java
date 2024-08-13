package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    private String url = "jdbc:mysql://localhost:3306/MovieSystem";
    private String user = "root";
    private String password = "P@ssw0rd";
    private Connection conn;

    public void connect() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public boolean movieExists(int movieId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Movies WHERE movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean userExists(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean ratingExists(int userId, int movieId) throws SQLException {
        String query = "SELECT COUNT(*) FROM ratings WHERE userId = ? AND movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void insertMovie(Movie movie) throws SQLException {
        String query = "INSERT INTO Movies (movieId, title, genre) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movie.getMovieId());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getGenre());
            stmt.executeUpdate();
        }
    }

    public void updateMovies(Movie movie) throws SQLException {
        String query = "UPDATE Movies SET title = ?, genre = ? WHERE movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getMovieId());
            stmt.executeUpdate();
        }
    }

    public void deleteRatingsByMovie(int movieId) throws SQLException {
        String query = "DELETE FROM ratings WHERE movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }

    public void deleteMovie(int movieId) throws SQLException {
        String query = "DELETE FROM Movies WHERE movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }


    public void insertUser(User user) throws SQLException {
        String query = "INSERT INTO users (userId, userName) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET userName = ? WHERE userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteRatingsByUser(int userId) throws SQLException {
        String query = "DELETE FROM ratings WHERE userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void insertRating(Rating rating) throws SQLException {
        String query = "INSERT INTO ratings (userId, movieId, rating) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rating.getUserId());
            stmt.setInt(2, rating.getMovieId());
            stmt.setDouble(3, rating.getRating());
            stmt.executeUpdate();
        }
    }

    public void updateRating(Rating rating) throws SQLException {
        String query = "UPDATE ratings SET rating = ? WHERE userId = ? AND movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, rating.getRating());
            stmt.setInt(2, rating.getUserId());
            stmt.setInt(3, rating.getMovieId());
            stmt.executeUpdate();
        }
    }


    public Movie getMovie(int movieId) throws SQLException {
        String query = "SELECT * FROM Movies WHERE movieId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Movie(
                        rs.getInt("movieId"),
                        rs.getString("title"),
                        rs.getString("genre")
                );
            }
        }
        return null;
    }

    public List<Rating> getRatingsByUser(int userId) throws SQLException {
        String query = "SELECT * FROM ratings WHERE userId = ?";
        List<Rating> ratings = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ratings.add(new Rating(
                        rs.getInt("userId"),
                        rs.getInt("movieId"),
                        rs.getInt("rating")
                ));
            }
        }
        return ratings;
    }

    public List<Movie> getRecommendations(int userId) throws SQLException {
        List<Movie> recommendations = new ArrayList<>();

        String query = "SELECT m.movieId, m.title, m.genre FROM Movies m " +
                "JOIN (SELECT r.movieId, AVG(r.rating) AS avgRating FROM ratings r GROUP BY r.movieId) AS avgRatings " +
                "ON m.movieId = avgRatings.movieId " +
                "ORDER BY avgRating DESC LIMIT 5"; // order top 5 movies

        connect();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int movieId = rs.getInt("movieId");
                String title = rs.getString("title");
                String genre = rs.getString("genre");

                Movie movie = new Movie(movieId, title, genre); // Assuming Movie constructor exists
                recommendations.add(movie);
            }
        } finally {
            disconnect();
        }

        return recommendations;
    }
}