/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CharTest.java is part of Strata
 * Last modified on 10-03-2022 12:07 p.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * STRATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.strata.parser.tokenizer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


class CharTest {
    private List<Pair<Character, Char>> charList;

    @Test
    void testCharIs() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;
            if (realChar != '\0')
                assertTrue(actualChar.is(realChar));
            else // If the char is \0 (used to represent End of Input), then it should always fail
                assertFalse(actualChar.is(realChar));
        }
    }

    @Test
    void testCharGetValue() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            assertEquals(realChar, actualChar.getValue());
        }
    }

    @Test
    void testCharGetPos() {
        for (int i = 0; i < this.charList.size(); i++) {
            Char actualChar = this.charList.get(i).second;

            assertEquals(i, actualChar.getPos());
        }
    }

    @Test
    void testCharIsAlphaNumeric() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            assertEquals(Character.isLetterOrDigit(realChar) || realChar == '-', actualChar.isAlphaNumeric());
        }
    }

    @Test
    void testCharIsDigit() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            assertEquals(Character.isDigit(realChar), actualChar.isDigit());
        }
    }

    @Test
    void testCharIsLetter() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            assertEquals(Character.isLetter(realChar), actualChar.isLetter());
        }
    }

    @Test
    void testCharIsEndOfInput() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            assertEquals(realChar == '\0', actualChar.isEndOfInput());
        }
    }

    @Test
    void testCharGetStringValue() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            if (realChar != '\0')
                assertEquals(Character.toString(realChar), actualChar.getStringValue());
            else // If the char is \0 (used to represent End of Input), then it should be equal to the empty string
                assertEquals("", actualChar.getStringValue());
        }
    }

    @Test
    void testCharToString() {
        for (Pair<Character, Char> charPair : this.charList) {
            char realChar = charPair.first;
            Char actualChar = charPair.second;

            if (realChar != '\0')
                assertEquals(Character.toString(realChar), actualChar.toString());
            else // If the char is \0 (used to represent End of Input), then it should be equal to the empty string
                assertEquals("<EOI>", actualChar.toString());
        }
    }

    @BeforeEach
    void setUp() {
        char[] chars = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '-', '_', '\0'
        };

        List<Pair<Character, Char>> tempList = new ArrayList<>(chars.length);

        for (int i = 0; i < chars.length; i++) {
            tempList.add(i, new Pair<>(chars[i], new Char(chars[i], i)));
        }

        this.charList = tempList;
    }

    /**
     * Utility pair class
     *
     * @param <A>
     * @param <B>
     */
    public static class Pair<A, B> {

        public final A first;

        public final B second;

        public Pair(A fst, B snd) {
            this.first = fst;
            this.second = snd;
        }

        @Override
        public String toString() {
            return String.format("Pair{first=%s, second=%s}", this.first, this.second);
        }

        @Override
        public boolean equals(Object o) {
            return this == o ||
                   o != null &&
                   getClass() == o.getClass() &&
                   Objects.equals(this.first, ((Pair<?, ?>) o).first) &&
                   Objects.equals(this.second, ((Pair<?, ?>) o).second);

        }

        @Override
        public int hashCode() {
            return 31 * (this.first != null ? this.first.hashCode() : 0) + (this.second != null ? this.second.hashCode() : 0);
        }
    }
}
