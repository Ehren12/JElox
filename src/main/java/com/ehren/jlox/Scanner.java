package com.ehren.jlox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ehren.jlox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private int col = 0;

    Scanner(String source) {
        this.source = source;
    }

    // Keyword map
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    // 'The heart of the scanner'
    List<Token> scanTokens() throws IOException {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line, col));
        return tokens;
    }

    // Scans every token classifying them appropriately
    private void scanToken() throws IOException {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else if (match('*')) {
                    // A comment goes until the ending */
                    multiLineComment();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                col = 0;
                line++;
                break;
            case '"':
                string();
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, col, "Unexpected character: " + '"' + c + '"');
                }
                break;

        }
    }

    private void multiLineComment() throws IOException {
        // Keep track of opening and opening comment tags
        int openingTag = 1;
        int closingTag = 0;
        // Attain number of closing and opening tags
        for (int i = current; i < source.length() - 1; i++) {

            if (!isAtEnd() && source.charAt(i) == '/' && source.charAt(i + 1) == '*') openingTag++;
            if (!isAtEnd() && source.charAt(i) == '*' && source.charAt(i + 1) == '/') closingTag++;
        }

        // Used while loop to make it more readable
        int consumedClosingTags = 0;
        while (consumedClosingTags < closingTag && !isAtEnd()) {
            if (peek() == '\n') {
                col = 0;
                line++;
            }
            if (peek() == '*' && peekNext() == '/' && !isAtEnd()) {
                consumedClosingTags++;
                advance();
            }
            if (!isAtEnd()) advance();
        }

        if (openingTag != closingTag) {
            Lox.error(line, col, "Unterminated Comment!");
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    private void string() throws IOException {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, col, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    // Helper functions
    // Checks if next character matches expected value
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    // Looks ahead one step
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // Looks ahead two steps
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    // Checks if a character is an alphabet or underscore
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    // Checks if a character is a digit or not
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // Checks for either alphabetic or numeral characters
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    // Checks if we are at the end of the file
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // 'consumes' the next character
    private char advance() {
        col++;
        return source.charAt(current++);
    }

    // Adds token to token ArrayList; used for non-literals
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // OVERLOADING FUNC: Adds token to token ArrayList; used for literals

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line, col));
    }
}