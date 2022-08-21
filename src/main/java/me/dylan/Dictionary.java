package me.dylan;// Java Program Illustrating Reading a File to a String
// Using next() and hasNext() method of Scanner class

// Importing required classes
import java.io.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

// Main class
public class Dictionary {
    private ArrayList<String> swearWords = new ArrayList<>();

    // Main driver method
    public Dictionary()
            throws IOException
    {

        // Creating object of Path class where custom local
        // directory path is passed as arguments using .of()
        // method
        Path fileName
                = Path.of("src/main/resources/SwearWords.txt");

        // Creating an object of Scanner class
        Scanner sc = new Scanner(fileName);

        // It holds true till there is single element left
        // via hasNext() method

        while (sc.hasNext()) {
            // Iterating over elements in object
            swearWords.add(sc.next());
        }


        // Closing scanner class object to avoid errors and
        //  free up memory space
        sc.close();
    }

    public ArrayList<String> getSwearWords() {
        return swearWords;
    }
}