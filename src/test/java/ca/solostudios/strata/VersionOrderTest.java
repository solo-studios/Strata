/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionOrderTest.java is part of Strata
 * Last modified on 24-09-2021 07:47 p.m.
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
import ca.solostudios.strata.version.Version;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static ca.solostudios.strata.Versions.parseVersion;


public class VersionOrderTest {
    Comparator<Version> versionComparator = Comparator.naturalOrder();
    
    @Test
    void testMajor() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("3.0.0"),
                                               parseVersion("2.0.0"),
                                               parseVersion("1.0.0"));
        
        List<Version> sorted = Arrays.asList(parseVersion("1.0.0"),
                                             parseVersion("2.0.0"),
                                             parseVersion("3.0.0"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testMajorMinor() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("3.1.0"),
                                               parseVersion("1.5.0"),
                                               parseVersion("2.3.0"));
        
        List<Version> sorted = Arrays.asList(parseVersion("1.5.0"),
                                             parseVersion("2.3.0"),
                                             parseVersion("3.1.0"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testMinor() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("0.2.0"),
                                               parseVersion("0.3.0"),
                                               parseVersion("0.1.0"));
        
        List<Version> sorted = Arrays.asList(parseVersion("0.1.0"),
                                             parseVersion("0.2.0"),
                                             parseVersion("0.3.0"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testMinorPatch() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("0.2.5"),
                                               parseVersion("0.3.0"),
                                               parseVersion("0.1.3"));
        
        List<Version> sorted = Arrays.asList(parseVersion("0.1.3"),
                                             parseVersion("0.2.5"),
                                             parseVersion("0.3.0"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testPatch() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("1.0.3"),
                                               parseVersion("1.0.2"),
                                               parseVersion("1.0.1"));
        
        List<Version> sorted = Arrays.asList(parseVersion("1.0.1"),
                                             parseVersion("1.0.2"),
                                             parseVersion("1.0.3"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testPatchPre() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("0.0.3-abcd"),
                                               parseVersion("0.0.2-123"),
                                               parseVersion("0.0.4"),
                                               parseVersion("0.0.3"),
                                               parseVersion("0.0.1-8aw3-21312"));
        
        List<Version> sorted = Arrays.asList(parseVersion("0.0.1-8aw3-21312"),
                                             parseVersion("0.0.2-123"),
                                             parseVersion("0.0.3-abcd"),
                                             parseVersion("0.0.3"),
                                             parseVersion("0.0.4"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testPre() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("0.0.0-abcd"),
                                               parseVersion("0.0.0-123.123"),
                                               parseVersion("0.0.0-123"),
                                               parseVersion("0.0.0-123.456"),
                                               parseVersion("0.0.0"),
                                               parseVersion("0.0.0-abcde"),
                                               parseVersion("0.0.0-3121"));
        
        List<Version> sorted = Arrays.asList(parseVersion("0.0.0-123"),
                                             parseVersion("0.0.0-123.123"),
                                             parseVersion("0.0.0-123.456"),
                                             parseVersion("0.0.0-3121"),
                                             parseVersion("0.0.0-abcd"),
                                             parseVersion("0.0.0-abcde"),
                                             parseVersion("0.0.0"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testPreBuild() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("0.0.0-abcd+7123"),
                                               parseVersion("0.0.0-123.123+bhkjawe9a"),
                                               parseVersion("0.0.0-123+79913"),
                                               parseVersion("0.0.0-123.456+nawe9.0"),
                                               parseVersion("0.0.0-abcde+0yu9awe3"),
                                               parseVersion("0.0.0-3121+nn9312.g812"));
        
        List<Version> sorted = Arrays.asList(parseVersion("0.0.0-123+79913"),
                                             parseVersion("0.0.0-123.123+bhkjawe9a"),
                                             parseVersion("0.0.0-123.456+nawe9.0"),
                                             parseVersion("0.0.0-3121+nn9312.g812"),
                                             parseVersion("0.0.0-abcd+7123"),
                                             parseVersion("0.0.0-abcde+0yu9awe3"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
    
    @Test
    void testBuild() throws ParseException {
        List<Version> unsorted = Arrays.asList(parseVersion("0.0.0+7123"),
                                               parseVersion("0.0.0+bhkjawe9a"),
                                               parseVersion("0.0.0"),
                                               parseVersion("0.0.0+nawe9.0"),
                                               parseVersion("0.0.0+0yu9awe3"),
                                               parseVersion("0.0.0+nn9312.g812"));
        
        List<Version> sorted = Arrays.asList(parseVersion("0.0.0+7123"),
                                             parseVersion("0.0.0+bhkjawe9a"),
                                             parseVersion("0.0.0"),
                                             parseVersion("0.0.0+nawe9.0"),
                                             parseVersion("0.0.0+0yu9awe3"),
                                             parseVersion("0.0.0+nn9312.g812"));
        
        unsorted.sort(versionComparator);
        
        assertIterableEquals(sorted, unsorted);
    }
}
