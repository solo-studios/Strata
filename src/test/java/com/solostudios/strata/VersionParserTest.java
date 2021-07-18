/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionParserTest.java is part of Strata
 * Last modified on 17-07-2021 10:40 p.m.
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

package com.solostudios.strata;


import com.solostudios.strata.parser.VersionParser;
import com.solostudios.strata.version.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class VersionParserTest {
    @Test
    void testCoreParsing() {
        String[] validVersions = {
                "0.0.4",
                "1.2.3",
                "10.20.30",
                "1.0.0",
                "2.0.0",
                "1.1.7",
                "99999999999999999999999.999999999999999999.99999999999999999",
                };
    
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = new VersionParser(version).parse();
                assertEquals(version, ver.getFormatted(), "Failed to parse the version properly.");
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testPreReleaseParsing() {
        String[] validVersions = {
                "1.0.0-alpha",
                "1.0.0-beta",
                "1.0.0-alpha.beta",
                "1.0.0-alpha.beta.1",
                "1.0.0-alpha.1",
                "1.0.0-alpha0.valid",
                "1.0.0-alpha.0valid",
                "1.2.3-beta",
                "10.2.3-DEV-SNAPSHOT",
                "1.2.3-SNAPSHOT-123",
                "2.0.1-alpha.1227",
                "1.2.3----RC-SNAPSHOT.12.9.1--.12",
                "1.0.0-0A.is.legal"
        };
        
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = new VersionParser(version).parse();
                assertEquals(version, ver.getFormatted(), "Failed to parse the version properly.");
                
                System.out.printf("version: %s, format: %s%n", version, ver.getFormatted());
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testBuildMetadataParsing() {
        String[] validVersions = {
                "1.1.2+meta",
                "1.1.2+meta-valid",
                "2.0.0+build.1848",
                "1.0.0+0.build.1-rc.10000aaa-kk-0.1",
                };
        
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = new VersionParser(version).parse();
                assertEquals(version, ver.getFormatted(), "Failed to parse the version properly.");
            }, String.format("Failed during parsing of version '%s'.", version));
    }
    
    @Test
    void testPreReleaseAndBuildMetadataParsing() {
        String[] validVersions = {
                "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay",
                "1.1.2-prerelease+meta",
                "1.0.0-rc.1+build.1",
                "2.0.0-rc.1+build.123",
                "1.0.0-alpha+beta",
                "1.2.3----RC-SNAPSHOT.12.9.1--.12+788",
                "1.2.3----R-S.12.9.1--.12+meta",
                };
        
        for (String version : validVersions)
            assertDoesNotThrow(() -> {
                Version ver = new VersionParser(version).parse();
                assertEquals(version, ver.getFormatted(), "Failed to parse the version properly.");
            }, String.format("Failed during parsing of version '%s'.", version));
    }
}