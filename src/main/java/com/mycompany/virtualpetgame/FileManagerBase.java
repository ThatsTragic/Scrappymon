/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
public abstract class FileManagerBase{
    private static final String DIRECTORY = "resources";
    private final String fileName;

    protected FileManagerBase(String file) {
        this.fileName = DIRECTORY + "/" + file;
    }

    //Creates the resources folder and the data file if they do not exist yet. 
    private void ensureFile() throws IOException {
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    //Reads every non-blank line of the file. Returns an empty list on failure. 
    protected List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try {
            ensureFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not read " + fileName + " (" + e.getMessage() + ")");
        }
        return lines;
    }

    // Overwrites the file with the given lines. 
    protected void writeLines(List<String> lines) {
        try {
            ensureFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save " + fileName + " (" + e.getMessage() + ")");
        }
    }
}
