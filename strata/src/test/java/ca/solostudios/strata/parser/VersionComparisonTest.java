/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionComparisonTest.java is part of Strata.
 * Last modified on 22-08-2024 07:14 p.m.
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
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import org.junit.jupiter.api.Test;

import static ca.solostudios.strata.Versions.getVersionRange;
import static ca.solostudios.strata.Versions.parseVersion;
import static org.junit.jupiter.api.Assertions.*;


public class VersionComparisonTest {
    @Test
    void testSimpleInside() throws ParseException {
        Version ver1 = parseVersion("1.0.0");
        Version ver2 = parseVersion("2.0.0");
        Version ver3 = parseVersion("1.5.0");

        VersionRange range = getVersionRange(ver1, false, ver2, false);

        assertTrue(range.isSatisfiedBy(ver3));
    }

    @Test
    void testSimpleOutside() throws ParseException {
        Version ver1 = parseVersion("1.0.0");
        Version ver2 = parseVersion("2.0.0");
        Version ver3 = parseVersion("3.0.0");

        VersionRange range = getVersionRange(ver1, false, ver2, false);

        assertFalse(range.isSatisfiedBy(ver3));
    }

    @Test
    void testInclusiveStart() throws ParseException {
        Version ver1 = parseVersion("1.0.0");
        Version ver2 = parseVersion("2.0.0");
        Version ver3 = parseVersion("1.0.0");

        VersionRange range = getVersionRange(ver1, true, ver2, false);

        assertTrue(range.isSatisfiedBy(ver3));
    }

    @Test
    void testExclusiveStart() throws ParseException {
        Version ver1 = parseVersion("1.0.0");
        Version ver2 = parseVersion("2.0.0");
        Version ver3 = parseVersion("1.0.0");

        VersionRange range = getVersionRange(ver1, false, ver2, false);

        assertFalse(range.isSatisfiedBy(ver3));
    }

    @Test
    void testInclusiveEnd() throws ParseException {
        Version ver1 = parseVersion("1.0.0");
        Version ver2 = parseVersion("2.0.0");
        Version ver3 = parseVersion("2.0.0");

        VersionRange range = getVersionRange(ver1, false, ver2, true);

        assertTrue(range.isSatisfiedBy(ver3));
    }

    @Test
    void testExclusiveEnd() throws ParseException {
        Version ver1 = parseVersion("1.0.0");
        Version ver2 = parseVersion("2.0.0");
        Version ver3 = parseVersion("2.0.0");

        VersionRange range = getVersionRange(ver1, false, ver2, false);

        assertFalse(range.isSatisfiedBy(ver3));
    }
}
