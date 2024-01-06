package com.ehren.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = ErrorReporter.hadError;
    private static boolean repl;
    private static String filename = "";
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            repl = false;
            filename = args[0];
            runFile(args[0]);
        }
        else{
            repl = true;
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print(">> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) throws IOException {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        // For now, just print the tokens.
        System.out.println(tokens);
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
    static void error(int line, int col, String message) throws IOException {
        ErrorReporter.report(line, col, message, repl, filename);
    }
}
