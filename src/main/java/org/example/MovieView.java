package org.example;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MovieView {

    public void displayMenu() {
        System.out.println("Movie Recommendation System");
        System.out.println("1. Add Movie");
        System.out.println("2. Add User");
        System.out.println("3. Add Rating");
        System.out.println("4. Get Movie Recommendations");
        System.out.println("5. Delete Movie");
        System.out.println("6. Delete User");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }


    public void displayRecommendations(ArrayList<Movie> recommendations) {
        System.out.println("Recommendations:");
        for (Movie movie : recommendations) {
            System.out.println(movie.getTitle());
        }
    }

    public int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                int choice = scanner.nextInt();
                return choice;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Clear the invalid input
            }
        }
    }
    public int getUserIdForRecommendations() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user ID for recommendations: ");
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a valid user ID: ");
                scanner.next(); // Clear the invalid input
            }
        }
    }


    public Movie getMovieDetailsFromUser() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter movie ID: ");
                int movieId = scanner.nextInt();
                System.out.print("Enter movie title: ");
                String title = scanner.next();
                System.out.print("Enter movie genre: ");
                String genre = scanner.next();
                return new Movie(movieId, title, genre);
            }
            catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Clear the invalid input
            }
        }

    }


    public User getUserDetailsFromUser() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try{
                System.out.print("Enter user ID:");
                int userId = scanner.nextInt();
                System.out.print("Enter user username: ");
                String username = scanner.next();
                return new User(userId,username);
            }
            catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Clear the invalid input
            }
        }

    }

    public Rating getRatingDetailsFromUser() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try{
                System.out.print("Enter user ID: ");
                int userId = scanner.nextInt();
                System.out.print("Enter movie ID: ");
                int movieId = scanner.nextInt();
                System.out.print("Enter rating (1-5): ");
                int rating = scanner.nextInt();
                return new Rating(userId, movieId, rating);
            }
            catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Clear the invalid input
            }
        }

    }
}
