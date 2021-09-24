/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Char.java is part of Strata
 * Last modified on 24-09-2021 02:21 p.m.
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


/**
 * Represents a single character read from a {@link LookaheadReader}.
 * <p>
 * Provides the value as well as an exact position of the character in the stream. Also some test methods are provided to determine the
 * character class of the internal value.
 *
 * @see LookaheadReader
 */
public class Char implements Position {
    private final char value;
    
    private final int pos;
    
    public Char(char value, int pos) {
        this.value = value;
        this.pos = pos;
    }
    
    @Override
    public String toString() {
        if (isEndOfInput()) {
            return "<EOI>";
        } else {
            return String.valueOf(value);
        }
    }
    
    public boolean is(char test) {
        return test == value && test != '\0';
    }
    
    /**
     * Checks if the internal value is one of the given characters
     *
     * @param tests the characters to check against
     *
     * @return <tt>true</tt> if the value equals to one of the give characters, <tt>false</tt> otherwise
     */
    public boolean is(char... tests) {
        for (char test : tests) {
            if (test == value && test != '\0') {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the value of this char.
     *
     * @return the internal value read from the stream
     */
    public char getValue() {
        return value;
    }
    
    @Override
    public int getPos() {
        return pos;
    }
    
    /**
     * Determines if the value is an alphanumeric identifier (0..9, a..z, A..Z, -)
     *
     * @return <tt>true</tt> if the internal value is a letter, <tt>false</tt> otherwise
     */
    public boolean isAlphaNumeric() {
        return isLetter() || isDigit() || is('-');
    }
    
    /**
     * Determines if the value is a digit (0..9)
     *
     * @return <tt>true</tt> if the internal value is a digit, <tt>false</tt> otherwise
     */
    public boolean isDigit() {
        return value >= '0' && value <= '9';
    }
    
    /**
     * Determines if the value is a letter (a..z, A..Z)
     *
     * @return <tt>true</tt> if the internal value is a letter, <tt>false</tt> otherwise
     */
    public boolean isLetter() {
        return (value >= 'a' && value <= 'z') || (value >= 'A' && value <= 'Z');
    }
    
    /**
     * Determines if this instance represents the end of input indicator
     *
     * @return <tt>true</tt> if this instance represents the end of the underlying input,
     *         <tt>false</tt> otherwise
     */
    public boolean isEndOfInput() {
        return value == '\0';
    }
    
    /**
     * Returns the internal value as string.
     *
     * @return the internal character as string or "" if this is the end of input indicator
     */
    public String getStringValue() {
        if (isEndOfInput()) {
            return "";
        }
        return String.valueOf(value);
    }
}
