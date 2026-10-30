/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */

import java.util.Scanner;
public class InputReader {
    private static final int MAX_NAME_LENGTH = 20;
    private final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "x";
    }

    public String readName(String prompt) {
        while (true) {
            System.out.println(prompt);
            String name = readLine();
            if (isValidName(name)) {
                return name;
            }
            System.out.println("Invalid name. Use 1-" + MAX_NAME_LENGTH
                    + " letters, numbers, spaces, '-' or '_' (no commas).");
        }
    }

    public static boolean isValidName(String name) {
        return name != null
                && !name.isEmpty()
                && name.length() <= MAX_NAME_LENGTH
                && name.matches("[A-Za-z0-9 _-]+");
    }
}
