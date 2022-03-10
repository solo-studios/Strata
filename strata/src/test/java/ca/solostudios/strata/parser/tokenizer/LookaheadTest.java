/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LookaheadTest.java is part of Strata
 * Last modified on 10-03-2022 11:48 a.m.
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


import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class LookaheadTest {
    
    private static LookaheadReader stringToLookahead(String stringToRead) {
        return new LookaheadReader(new StringReader(stringToRead));
    }
    
    @Test
    void testNext() throws ParseException {
        String testString = "testing 123";
        
        LookaheadReader reader = stringToLookahead(testString);
        
        char[] charsMinusFirst = Arrays.copyOfRange(testString.toCharArray(), 1, testString.length());
        for (char realChar : charsMinusFirst) {
            assertEquals(realChar, reader.next().getValue());
            
            reader.consume();
        }
        
        assertTrue(reader.next().isEndOfInput());
    }
    
    @Test
    void testCurrent() throws ParseException {
        String testString = "testing 123";
        
        LookaheadReader reader = stringToLookahead(testString);
        
        for (char realChar : testString.toCharArray()) {
            assertEquals(realChar, reader.current().getValue());
            
            reader.consume(); // consume character
        }
        
        // reader.consume(); // consume last character
        
        assertTrue(reader.current().isEndOfInput());
    }
    
    @Test
    void testConsume() throws ParseException {
        String testString = "testing 123";
        
        LookaheadReader reader = stringToLookahead(testString);
        
        for (char realChar : testString.toCharArray()) {
            assertEquals(realChar, reader.consume().getValue());
        }
        
        assertTrue(reader.consume().isEndOfInput());
    }
    
    @Test
    void testFetch() throws ParseException {
        String testString = "testing 123";
        
        LookaheadReader reader = stringToLookahead(testString);
        
        for (char realChar : testString.toCharArray()) {
            Char fetched = reader.fetch();
            assertNotNull(fetched);
            assertEquals(realChar, fetched.getValue());
        }
        
        assertNull(reader.fetch());
    }
    
    @Test
    void testEndOfInput() throws ParseException {
        LookaheadReader reader = stringToLookahead("");
        assertEquals(reader.endOfInput(), reader.current());
    }
}