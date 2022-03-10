/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CoreVersionTest.java is part of Strata
 * Last modified on 10-03-2022 12:04 p.m.
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

package ca.solostudios.strata.version;


import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;


class CoreVersionTest {
    
    private static BigInteger bigInteger(long integer) {
        return BigInteger.valueOf(integer);
    }
    
    @Test
    void testGetComponents() {
        CoreVersion coreVersion = new CoreVersion(bigInteger(1), bigInteger(2), bigInteger(3));
        
        assertEquals(coreVersion.getMajor(), bigInteger(1));
        assertEquals(coreVersion.getMinor(), bigInteger(2));
        assertEquals(coreVersion.getPatch(), bigInteger(3));
    }
    
    @Test
    void testCompareTo() {
        CoreVersion coreVersion = new CoreVersion(bigInteger(1), bigInteger(2), bigInteger(3));
        CoreVersion otherCoreVersion = new CoreVersion(bigInteger(2), bigInteger(3), bigInteger(4));
        
        assertEquals(-1, coreVersion.compareTo(otherCoreVersion));
    }
    
    @Test
    void testGetFormatted() {
        CoreVersion coreVersion = new CoreVersion(bigInteger(1), bigInteger(2), bigInteger(3));
        
        assertEquals("1.2.3", coreVersion.getFormatted());
    }
}