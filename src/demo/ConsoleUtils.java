package demo;

import java.util.Scanner;

public class ConsoleUtils {
    public static final Scanner sc = new Scanner(System.in);

    private ConsoleUtils() {
    }

    public static String askText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static int askInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid");
            }
        }
    }

    public static void pause() {

        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

}
