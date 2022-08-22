package me.dylan;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class writer {

    private final FileWriter fileWriter = new FileWriter("src/main/resources/SwearWords.txt", true);

    public writer() throws IOException {
    }


    public void WriteToFile(String text) throws IOException {
        System.out.println("Writing to file...");
        fileWriter.write(text);
        fileWriter.close();
    }
}
