/*
 * VERSION 1 SCANNER TEST:
 *   Class Coverage: 100% (1/1),
 *   Method Coverage: 100% (18/18),
 *   Line Coverage: 97% (114/117)
 *
 * NOTE: CODE COVERAGE IS NOT A MEASURE OF HOW GOOD THESE TESTS ARE
 * THERE CAN ALWAYS BE ROOM FOR IMPROVEMENT.
 */

package com.ehren.jlox;


import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.ehren.jlox.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("Scanner")
@DisplayName("Scanner Test")
class ScannerTest {

    // Reserved Keywords
    @Tag("ReservedKeywords")
    @ParameterizedTest
    @MethodSource("keywordTestCases")
    @DisplayName("Check that reserved keywords are identified correctly")
    void itShouldContainReservedKeyword(String sourceUnderTest, TokenType tokenType) throws IOException {
        // given
        Scanner scannerUnderTest = new Scanner(sourceUnderTest);

        // when
        List<Token> testTokens = scannerUnderTest.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();

        expectedTokens.add(new Token(tokenType, sourceUnderTest, null, 1, sourceUnderTest.length()));
        expectedTokens.add(new Token(EOF, "", null, 1, sourceUnderTest.length()));

        // then
        assertThat(testTokens).usingRecursiveComparison().isEqualTo(expectedTokens);
    }

    // Testing Token Classification
    @Tag("Tokens")
    @ParameterizedTest
    @MethodSource("tokenTestCases")
    @DisplayName("Check that tokens are properly recognized")
    void itShouldRecognizeTokensCorrectly(String sourceUnderTest, TokenType tokenType) throws IOException {
        // given
        Scanner scannerUnderTest = new Scanner(sourceUnderTest);

        // when
        List<Token> testTokens = scannerUnderTest.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token(tokenType, sourceUnderTest, null, 1, 1));
        expectedTokens.add(new Token(EOF, "", null, 1, 1));
        // then
        assertThat(testTokens).usingRecursiveComparison().isEqualTo(expectedTokens);
    }

    // Identifier
    @Tag("Identifiers")
    @ParameterizedTest
    @MethodSource("identifierTestCases")
    @DisplayName("Check that identifiers are properly recognized")
    void itShouldRecognizeIdentifiersCorrectly(String sourceUnderTest) throws IOException {
        // given
        Scanner scannerUnderTest = new Scanner(sourceUnderTest);

        // when
        List<Token> testTokens = scannerUnderTest.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token(IDENTIFIER, sourceUnderTest, null, 1, sourceUnderTest.length()));
        expectedTokens.add(new Token(EOF, "", null, 1, sourceUnderTest.length()));

        // then
        assertThat(testTokens).usingRecursiveComparison().isEqualTo(expectedTokens);
    }

    // String Test
    @Tag("Strings")
    @ParameterizedTest
    @MethodSource("stringTestCases")
    @DisplayName("Check that identifiers are properly recognized")
    void itShouldRecognizeStringsCorrectly(String sourceUnderTest, int line) throws IOException {
        // given
        Scanner scannerUnderTest = new Scanner(sourceUnderTest);

        // when
        List<Token> testTokens = scannerUnderTest.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token(TokenType.STRING, sourceUnderTest, stripQuotes(sourceUnderTest), line, sourceUnderTest.length()));
        expectedTokens.add(new Token(EOF, "", null, line, sourceUnderTest.length()));

        // then
        assertThat(testTokens).usingRecursiveComparison().isEqualTo(expectedTokens);
    }

    // Number/Integer test
    @Tag("Numbers")
    @ParameterizedTest
    @MethodSource("numberTestCase")
    @DisplayName("Check that numbers are properly recognized")
    void itShouldRecognizeNumbersCorrectly(String sourceUnderTest) throws IOException {
        // given
        Scanner scannerUnderTest = new Scanner(sourceUnderTest);
        // when
        List<Token> testTokens = scannerUnderTest.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token(NUMBER, sourceUnderTest, Double.parseDouble(sourceUnderTest), 1, sourceUnderTest.length()));
        expectedTokens.add(new Token(EOF, "", null, 1, sourceUnderTest.length()));
        // then
        assertThat(testTokens).usingRecursiveComparison().isEqualTo(expectedTokens);
    }

    // Useless keyword test
    @Tag("IrrelevantTokens")
    @ParameterizedTest
    @MethodSource("irrelevantTokensTestCase")
    @DisplayName("Checks to see if comments are properly ignored")
    void itShouldIgnoreIrrelevantTokens(String sourceUnderTest, int line, int col) throws IOException {
        // given
        Scanner scannerUnderTest = new Scanner(sourceUnderTest);
        // when
        List<Token> testTokens = scannerUnderTest.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token(EOF, "", null, line, col == 0 ? sourceUnderTest.length() - 1 : col));
        // then
        assertThat(testTokens).usingRecursiveComparison().isEqualTo(expectedTokens);
    }

    // Test Cases
    @org.jetbrains.annotations.NotNull
    private static Stream<Arguments> keywordTestCases() {
        return Stream.of(
                Arguments.of("and", AND),
                Arguments.of("class", CLASS),
                Arguments.of("else", ELSE),
                Arguments.of("false", FALSE),
                Arguments.of("for", FOR),
                Arguments.of("fun", FUN),
                Arguments.of("if", IF),
                Arguments.of("nil", NIL),
                Arguments.of("print", PRINT),
                Arguments.of("return", RETURN),
                Arguments.of("super", SUPER),
                Arguments.of("this", THIS),
                Arguments.of("true", TRUE),
                Arguments.of("var", VAR),
                Arguments.of("while", WHILE)
        );
    }

    @org.jetbrains.annotations.NotNull
    private static Stream<Arguments> tokenTestCases() {
        return Stream.of(
                Arguments.of("(", LEFT_PAREN),
                Arguments.of(")", RIGHT_PAREN),
                Arguments.of("{", LEFT_BRACE),
                Arguments.of("}", RIGHT_BRACE),
                Arguments.of(",", COMMA),
                Arguments.of(".", DOT),
                Arguments.of("+", PLUS),
                Arguments.of("-", MINUS),
                Arguments.of("*", STAR),
                Arguments.of(";", SEMICOLON),
                Arguments.of(">", GREATER),
                Arguments.of("<", LESS),
                Arguments.of("==", EQUAL_EQUAL),
                Arguments.of("<=", LESS_EQUAL),
                Arguments.of(">=", GREATER_EQUAL),
                Arguments.of("=", EQUAL),
                Arguments.of("!=", BANG_EQUAL),
                Arguments.of("!", BANG),
                Arguments.of("/", SLASH)
        );
    }

    @org.jetbrains.annotations.NotNull
    private static Stream<Arguments> identifierTestCases() {
        return Stream.of(
                Arguments.of("garfield"),
                Arguments.of("ehren"),
                Arguments.of("printer"),
                Arguments.of("truesy"),
                Arguments.of("orchid"),
                Arguments.of("_MALACHI"),
                Arguments.of("___weBareBears")
        );
    }

    @org.jetbrains.annotations.NotNull
    private static Stream<Arguments> stringTestCases() {
        return Stream.of(
                Arguments.of("\"moots\"", 1),
                Arguments.of("""
                        \"This is what I stand for: +
                        For the inclusion of everybody.
                        For men to be viewed as men +
                        And for children as children.\"""", 4),
                Arguments.of("\"I am a maple tree\"", 1),
                Arguments.of("""
                        \"Mr rich
                                Stickerish
                                    Lockerish
                                        Lickerish



                                        Gibberish\"""", 8)
        );
    }

    @org.jetbrains.annotations.NotNull
    private static Stream<Arguments> numberTestCase() {
        return Stream.of(
                Arguments.arguments("15"),
                Arguments.arguments("18.9392"),
                Arguments.arguments("1218139283922819219281928191921981939139121929182918291821.92192129129192189281928192189218")
        );
    }

    private static Stream<Arguments> irrelevantTokensTestCase() {
        return Stream.of(
                Arguments.arguments("// This is a comment! It doesn't make sense and shouldn't", 1, 0),
                Arguments.arguments("/* this is a multiline comment!\n See I am on a new line \n */", 3, 4),
                Arguments.arguments("/* /* /* /* I am a 'quadly' nested comment <3 */ */ */ */", 1, 0),
                Arguments.arguments("\n", 2, 0),
                Arguments.arguments(" ", 1, 1),
                Arguments.arguments("\t", 1, 1),
                Arguments.arguments("\r", 1, 1)
        );
    }


    // Helper Function
    private static String stripQuotes(@NotNull String input) {
        return input.substring(1, input.length() - 1);
    }

}
