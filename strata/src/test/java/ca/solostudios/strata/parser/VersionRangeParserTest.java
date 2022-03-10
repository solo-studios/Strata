/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRangeParserTest.java is part of Strata
 * Last modified on 10-03-2022 11:57 a.m.
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

package ca.solostudios.strata.parser;


import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.VersionRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static ca.solostudios.strata.Versions.parseVersion;
import static ca.solostudios.strata.Versions.parseVersionRange;


class VersionRangeParserTest {
    @Test
    void testUnboundedRange() throws ParseException {
        VersionRange range = parseVersionRange("(,)");
    
        assertTrue(range.isSatisfiedBy("0.0.0"));
        assertTrue(range.isSatisfiedBy("1.1.1"));
        assertTrue(range.isSatisfiedBy("918273.1872693.89"));
        assertTrue(range.isSatisfiedBy("999999999999999999999999999.999999999999999999999999999.999999999999999999999999999"));
    
        assertFalse(range.isEndInclusive()); // should be exclusive
        assertFalse(range.isStartInclusive());
    
        assertNull(range.getStartVersion()); // both start & end should be null
        assertNull(range.getEndVersion());
    }
    
    @Test
    void testExclusiveLowerBoundRange() throws ParseException {
        VersionRange range = parseVersionRange("(1.2.3,)");
    
        assertTrue(range.isSatisfiedBy("1.2.4"));
        assertTrue(range.isSatisfiedBy("1.2.7819239182"));
        assertTrue(range.isSatisfiedBy("1.98712318972.90842"));
        assertTrue(range.isSatisfiedBy("840438590432.87921312.98721341"));
        assertFalse(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.2"));
        assertFalse(range.isSatisfiedBy("1.2.0"));
        assertFalse(range.isSatisfiedBy("1.0.0"));
        assertFalse(range.isSatisfiedBy("0.0.0"));
    
        assertFalse(range.isEndInclusive()); // should be exclusive
        assertFalse(range.isStartInclusive());
    
        assertEquals(parseVersion("1.2.3"), range.getStartVersion());
        assertNull(range.getEndVersion());
    }
    
    @Test
    void testInclusiveLowerBoundRange() throws ParseException {
        VersionRange range = parseVersionRange("[1.2.3,]");
    
        assertTrue(range.isSatisfiedBy("1.2.4"));
        assertTrue(range.isSatisfiedBy("1.2.7819239182"));
        assertTrue(range.isSatisfiedBy("1.98712318972.90842"));
        assertTrue(range.isSatisfiedBy("840438590432.87921312.98721341"));
        assertTrue(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.2"));
        assertFalse(range.isSatisfiedBy("1.2.0"));
        assertFalse(range.isSatisfiedBy("1.0.0"));
        assertFalse(range.isSatisfiedBy("0.0.0"));
    
        assertTrue(range.isEndInclusive()); // should be inclusive
        assertTrue(range.isStartInclusive());
    
        assertEquals(parseVersion("1.2.3"), range.getStartVersion());
        assertNull(range.getEndVersion());
    }
    
    @Test
    void testExclusiveUpperBoundRange() throws ParseException {
        VersionRange range = parseVersionRange("(,4.5.6)");
    
        assertTrue(range.isSatisfiedBy("4.5.5"));
        assertTrue(range.isSatisfiedBy("4.5.0"));
        assertTrue(range.isSatisfiedBy("4.0.0"));
        assertTrue(range.isSatisfiedBy("0.0.0"));
        assertTrue(range.isSatisfiedBy("4.4.219083712"));
        assertTrue(range.isSatisfiedBy("4.4.99999999999999999999999999999999999999999999999999999999"));
        assertTrue(range.isSatisfiedBy("1.2.4"));
        assertTrue(range.isSatisfiedBy("1.2.7819239182"));
        assertTrue(range.isSatisfiedBy("1.98712318972.90842"));
        assertFalse(range.isSatisfiedBy("4.5.6"));
        assertFalse(range.isSatisfiedBy("4.5.8907123"));
        assertFalse(range.isSatisfiedBy("4.6.0"));
        assertFalse(range.isSatisfiedBy("4.9071231.0"));
        assertFalse(range.isSatisfiedBy("5.0.0"));
        assertFalse(range.isSatisfiedBy("840438590432.87921312.98721341"));
    
        assertFalse(range.isStartInclusive()); // should be exclusive
        assertFalse(range.isEndInclusive());
    
        assertNull(range.getStartVersion());
        assertEquals(parseVersion("4.5.6"), range.getEndVersion());
    }
    
    @Test
    void testInclusiveUpperBoundRange() throws ParseException {
        VersionRange range = parseVersionRange("(,4.5.6]");
    
        assertTrue(range.isSatisfiedBy("4.5.5"));
        assertTrue(range.isSatisfiedBy("4.5.0"));
        assertTrue(range.isSatisfiedBy("4.0.0"));
        assertTrue(range.isSatisfiedBy("0.0.0"));
        assertTrue(range.isSatisfiedBy("4.4.219083712"));
        assertTrue(range.isSatisfiedBy("4.4.99999999999999999999999999999999999999999999999999999999"));
        assertTrue(range.isSatisfiedBy("1.2.4"));
        assertTrue(range.isSatisfiedBy("1.2.7819239182"));
        assertTrue(range.isSatisfiedBy("1.98712318972.90842"));
        assertTrue(range.isSatisfiedBy("4.5.6"));
        assertFalse(range.isSatisfiedBy("4.5.8907123"));
        assertFalse(range.isSatisfiedBy("4.6.0"));
        assertFalse(range.isSatisfiedBy("4.9071231.0"));
        assertFalse(range.isSatisfiedBy("5.0.0"));
        assertFalse(range.isSatisfiedBy("840438590432.87921312.98721341"));
    
        assertFalse(range.isStartInclusive());
        assertTrue(range.isEndInclusive());
    
        assertNull(range.getStartVersion());
        assertEquals(parseVersion("4.5.6"), range.getEndVersion());
    }
    
    @Test
    void testNoRangeGlob() throws ParseException {
        VersionRange range = parseVersionRange("1.2.3");
        assertTrue(range.isSatisfiedBy("1.2.3"));
        assertFalse(range.isSatisfiedBy("1.2.4"));
        assertFalse(range.isSatisfiedBy("1.2.2"));
    
        assertTrue(range.isStartInclusive());
        assertFalse(range.isEndInclusive());
    
        assertEquals(parseVersion("1.2.3"), range.getStartVersion());
        assertEquals(parseVersion("1.2.4"), range.getEndVersion());
    }
    
    @Test
    void testPatchGlobRange() throws ParseException {
        VersionRange range = parseVersionRange("1.2.+");
    
        assertTrue(range.isSatisfiedBy("1.2.0"));
        assertTrue(range.isSatisfiedBy("1.2.9999"));
        assertTrue(range.isSatisfiedBy("1.2.91231"));
        assertFalse(range.isSatisfiedBy("1.3.0"));
        assertFalse(range.isSatisfiedBy("1.4.0"));
    
        assertTrue(range.isStartInclusive());
        assertFalse(range.isEndInclusive());
    
        assertEquals(parseVersion("1.2.0"), range.getStartVersion());
        assertEquals(parseVersion("1.3.0"), range.getEndVersion());
    }
    
    @Test
    void testMinorGlobRange() throws ParseException {
        VersionRange range = parseVersionRange("1.+");
    
        assertTrue(range.isSatisfiedBy("1.0.0"));
        assertTrue(range.isSatisfiedBy("1.9999.0"));
        assertTrue(range.isSatisfiedBy("1.7182.0"));
        assertFalse(range.isSatisfiedBy("2.0.0"));
        assertFalse(range.isSatisfiedBy("0.0.0"));
        assertFalse(range.isSatisfiedBy("0.99.99"));
    
        assertTrue(range.isStartInclusive());
        assertFalse(range.isEndInclusive());
    
        assertEquals(parseVersion("1.0.0"), range.getStartVersion());
        assertEquals(parseVersion("2.0.0"), range.getEndVersion());
    }
    
    @Test
    void testMajorGlobRange() throws ParseException {
        VersionRange range = parseVersionRange("+");
    
        assertTrue(range.isSatisfiedBy("0.0.0"));
        assertTrue(range.isSatisfiedBy("1.2.3"));
        assertTrue(range.isSatisfiedBy("9999.9999.9999"));
        assertTrue(range.isSatisfiedBy("999999999999999999999999999.999999999999999999999999999.999999999999999999999999999"));
    
        assertTrue(range.isStartInclusive());
        assertFalse(range.isEndInclusive());
    
        assertEquals(parseVersion("0.0.0"), range.getStartVersion());
        assertNull(range.getEndVersion());
    }
    
    @Test
    void testMetadata() throws ParseException {
        VersionRange range = parseVersionRange("0.1.+");
    
        assertTrue(range.isSatisfiedBy("0.1.0-BETA"));
        assertTrue(range.isSatisfiedBy("0.1.4-BETA"));
        assertTrue(range.isSatisfiedBy("0.1.9999999999999-BETA"));
        assertFalse(range.isSatisfiedBy("0.2.0-BETA"));
    }
}