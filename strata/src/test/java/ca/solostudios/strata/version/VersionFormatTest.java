/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionFormatTest.java is part of Strata
 * Last modified on 19-06-2022 03:51 p.m.
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


import ca.solostudios.strata.parser.tokenizer.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static ca.solostudios.strata.Versions.parseVersion;
import static ca.solostudios.strata.Versions.parseVersionRange;


public class VersionFormatTest {
    
    @Test
    void testVersionRangeFormat() throws ParseException {
        VersionRange range1 = parseVersionRange("1.2.+");
        VersionRange range2 = parseVersionRange("+");
        VersionRange range3 = parseVersionRange("[,]");
        VersionRange range4 = parseVersionRange("(,)");
        
        assertEquals("[1.2.0,1.3.0)", range1.getFormatted());
        assertEquals("[0.0.0,)", range2.getFormatted());
        assertEquals("[,]", range3.getFormatted());
        assertEquals("(,)", range4.getFormatted());
    }
    
    @Test
    void testVersionFormat() throws ParseException {
        Version version1 = parseVersion("1.2.3");
        Version version2 = parseVersion("1.2.3-stuff+abcd");
        
        assertEquals("1.2.3", version1.getFormatted());
        assertEquals("1.2.3-stuff+abcd", version2.getFormatted());
    }
}
