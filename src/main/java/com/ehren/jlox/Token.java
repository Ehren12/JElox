package com.ehren.jlox;

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    final int col;

    Token(TokenType type, String lexeme, Object literal, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.col = col;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
