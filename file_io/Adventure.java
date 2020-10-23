/*
A program to run a choose your own adventure game
Tiffany Ferderer
1/16/2020
Adventure.java
 */

package file_io;

import java.io.*;
import java.util.Scanner;

/**
 * This class represents a Choose Your Own Adventure Game.
 * Lets user choose a game from a file and play.
 *
 * @author Tiffany Ferderer
 * @version 1.0
 */
public class Adventure {

    private static File storyFile;
    private static String userGame;
    private static String[] storeTheGame;
    private static Scanner console = new Scanner(System.in);
    private static Scanner reader;
    private static PrintWriter writer = null;

    /**
     * The entry point to run game program and print results to a new file
     * @param args command line arguments
     */
    public static void main(String[] args) {

        userPrompt();
        userGame = console.nextLine();

         storyFile = new File("./stories/incomplete/" + userGame + ".txt");

         //read the file chosen by user
        try (Scanner reader = new Scanner(new FileInputStream(storyFile))) {
            convertFile();

        } catch (FileNotFoundException ex) {
            System.out.println("Sorry, the file doesn't exist!");
        }

        //write to the file
        if (storyFile.exists())
        try ( PrintWriter writer = new PrintWriter(new FileOutputStream("stories/complete/" + userGame + ".txt"))) {
            game();
        } catch (FileNotFoundException ex) {
            System.out.println("Problem writing file" + ex.getMessage());
        }
    }

    private static void userPrompt() {
        System.out.println("Welcome to my Choose Your Own Adventure program!");
        System.out.println("Please choose your story");
        System.out.println("zombies");
        System.out.println("island");
    }

    //Read file and convert into an array
    private static void convertFile() throws FileNotFoundException {
        if (storyFile.exists()) {
            reader = new Scanner(new FileInputStream(storyFile));

            storeTheGame = new String[(int) storyFile.length()];
            for (int i = 0; i < storyFile.length(); i++) {
                if(reader.hasNextLine())
                storeTheGame[i] = reader.nextLine();
            }
        } else {
            throw new FileNotFoundException();
        }
    }

    //Plays the game and writes the receipt to its own file
    private static void game() throws FileNotFoundException  {
        int userChoice = 0;
        int[] choiceOptions = new int[3];
        String[] descriptChoices;

        writer = new PrintWriter(new FileOutputStream("stories/complete/" + userGame + ".txt"));

        //Split the status with the options
        while (userChoice >= 0) {

            descriptChoices = storeTheGame[userChoice].split(" : ");
            String begin = descriptChoices[0];
            System.out.println(begin);
            writer.println(begin);

            //Split the options
            String[] choices = descriptChoices[1].split(" \\| ");

            //Put choices in the right layout
            for (int i = 0; i < choices.length; i++) {
                String[] choiceSentence = choices[i].split(" = ");
                String tryout = choiceSentence[1] + ": " + choiceSentence[0];
                choiceOptions[i] = Integer.parseInt(choiceSentence[1]);
                System.out.println(tryout);
                writer.println(tryout);
            }

            //A negative result needs to end the game
            if (choiceOptions[0] <= -1) {
                endGame();
                break;
            }

            //Add User input
            userChoice = console.nextInt();
            System.out.println();
            writer.println(userChoice);
            writer.println();

            //make sure the user is playing nicely
            while (!(userChoice == choiceOptions[0] || userChoice == choiceOptions[1] || userChoice == choiceOptions[2])) {
                System.out.println("Invalid option.");
                writer.println("Invalid option.");
                userChoice = console.nextInt();
            }
            userChoice--;
        }
    }

    private static void endGame() {
        System.out.println();
        System.out.println("Thanks for playing!");
        writer.println();
        writer.println("Thanks for playing!");
        writer.close();
    }
}
