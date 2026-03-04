package view;

import java.util.Scanner;

public class Validate {

    Scanner scanner = new Scanner(System.in);

    private static final String MENUREGEX =
            "(?i)^(N|P|F|L|G|W|R|U|D|S|SR|SA|UN|BA|RE|E)$";

    public static boolean isValidMenu(String input) {
        if (input == null) return false;
        return input.trim().matches(MENUREGEX);

    }

}