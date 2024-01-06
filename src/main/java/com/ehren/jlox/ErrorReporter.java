package com.ehren.jlox;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ErrorReporter {
    public static boolean hadError = false;
    // TODO: Abstract ANSI colours to separate enum
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    public static void report(int line, int col, String message, boolean repl, String filename) throws IOException {
        System.err.println(
                ANSI_RED + "ERROR " + message + ANSI_RESET
        );
        if (!repl) {
            String lineCont = "";
            try (Stream<String> lines = Files.lines(Paths.get(filename))) {
                lineCont = lines.skip(line - 1).findFirst().get();
            }
            // TODO: Add support for token highlighting as opposed to character highlighting
            // FIX: Exception in thread "main" java.lang.StringIndexOutOfBoundsException: Index 6 out of bounds for length 6
            System.out.println(
                    "\t" + line + ":" + col + " | " + lineCont.substring(0, col-1) + ANSI_RED_BACKGROUND + lineCont.charAt(col-1) + ANSI_RESET
            );
        }
        hadError = true;
    }
}
