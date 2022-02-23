/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRangeParserTest.java is part of Strata
 * Last modified on 23-02-2022 12:23 p.m.
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

package ca.solostudios.strata;


import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.VersionRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static ca.solostudios.strata.Versions.parseVersionRange;


class VersionRangeParserTest {
    @Test
    void testUnboundedRange() throws ParseException {
        VersionRange globalRange = parseVersionRange("(,)");
        assertTrue(globalRange.isSatisfiedBy("0.0.0"));
        assertTrue(globalRange.isSatisfiedBy("1.1.1"));
        assertTrue(globalRange.isSatisfiedBy("918273.1872693.89"));
        assertTrue(globalRange.isSatisfiedBy("999999999999999999999999999.999999999999999999999999999.999999999999999999999999999"));
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
    }
    
    @Test
    void testExclusiveUpperBoundRange() throws ParseException {
        VersionRange exclusiveUpperBound = parseVersionRange("(,4.5.6)");
        assertTrue(exclusiveUpperBound.isSatisfiedBy("4.5.5"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("4.5.0"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("4.0.0"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("0.0.0"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("4.4.219083712"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("4.4.99999999999999999999999999999999999999999999999999999999"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("1.2.4"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("1.2.7819239182"));
        assertTrue(exclusiveUpperBound.isSatisfiedBy("1.98712318972.90842"));
        assertFalse(exclusiveUpperBound.isSatisfiedBy("4.5.6"));
        assertFalse(exclusiveUpperBound.isSatisfiedBy("4.5.8907123"));
        assertFalse(exclusiveUpperBound.isSatisfiedBy("4.6.0"));
        assertFalse(exclusiveUpperBound.isSatisfiedBy("4.9071231.0"));
        assertFalse(exclusiveUpperBound.isSatisfiedBy("5.0.0"));
        assertFalse(exclusiveUpperBound.isSatisfiedBy("840438590432.87921312.98721341"));
    }
    
    @Test
    void testInclusiveUpperBoundRange() throws ParseException {
        VersionRange inclusiveUpperBound = parseVersionRange("(,4.5.6]");
        assertTrue(inclusiveUpperBound.isSatisfiedBy("4.5.5"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("4.5.0"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("4.0.0"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("0.0.0"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("4.4.219083712"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("4.4.99999999999999999999999999999999999999999999999999999999"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("1.2.4"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("1.2.7819239182"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("1.98712318972.90842"));
        assertTrue(inclusiveUpperBound.isSatisfiedBy("4.5.6"));
        assertFalse(inclusiveUpperBound.isSatisfiedBy("4.5.8907123"));
        assertFalse(inclusiveUpperBound.isSatisfiedBy("4.6.0"));
        assertFalse(inclusiveUpperBound.isSatisfiedBy("4.9071231.0"));
        assertFalse(inclusiveUpperBound.isSatisfiedBy("5.0.0"));
        assertFalse(inclusiveUpperBound.isSatisfiedBy("840438590432.87921312.98721341"));
    }
    
    @Test
    void testNoRangeGlob() throws ParseException {
        VersionRange noRange = parseVersionRange("1.2.3");
        assertTrue(noRange.isSatisfiedBy("1.2.3"));
        assertFalse(noRange.isSatisfiedBy("1.2.4"));
        assertFalse(noRange.isSatisfiedBy("1.2.2"));
    }
    
    @Test
    void testPatchGlobRange() throws ParseException {
        VersionRange patchRange = parseVersionRange("1.2.+");
        assertTrue(patchRange.isSatisfiedBy("1.2.0"));
        assertTrue(patchRange.isSatisfiedBy("1.2.9999"));
        assertTrue(patchRange.isSatisfiedBy("1.2.91231"));
        assertFalse(patchRange.isSatisfiedBy("1.3.0"));
        assertFalse(patchRange.isSatisfiedBy("1.4.0"));
    }
    
    @Test
    void testMinorGlobRange() throws ParseException {
        VersionRange minorRange = parseVersionRange("1.+");
        assertTrue(minorRange.isSatisfiedBy("1.0.0"));
        assertTrue(minorRange.isSatisfiedBy("1.9999.0"));
        assertTrue(minorRange.isSatisfiedBy("1.7182.0"));
        assertFalse(minorRange.isSatisfiedBy("2.0.0"));
        assertFalse(minorRange.isSatisfiedBy("0.0.0"));
        assertFalse(minorRange.isSatisfiedBy("0.99.99"));
    }
    
    @Test
    void testMajorGlobRange() throws ParseException {
        VersionRange majorRange = parseVersionRange("+");
        assertTrue(majorRange.isSatisfiedBy("0.0.0"));
        assertTrue(majorRange.isSatisfiedBy("1.2.3"));
        assertTrue(majorRange.isSatisfiedBy("9999.9999.9999"));
        assertTrue(majorRange.isSatisfiedBy("999999999999999999999999999.999999999999999999999999999.999999999999999999999999999"));
    }
    
    @Test
    void testMetadata() throws ParseException {
        VersionRange range = parseVersionRange("0.1.+");
        assertTrue(range.isSatisfiedBy("0.1.0-BETA"));
    }
}