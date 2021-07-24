/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Versions.java is part of Strata
 * Last modified on 23-07-2021 11:21 p.m.
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
import com.solostudios.strata.parser.tokenizer.ParseException;
import com.solostudios.strata.version.BuildMetadata;
import com.solostudios.strata.version.CoreVersion;
import com.solostudios.strata.version.PreRelease;
import com.solostudios.strata.version.Version;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;


public final class Versions {
    private Versions() {
    }
    
    public static Version getVersion(int major, int minor, int patch) {
        return new Version(new CoreVersion(BigInteger.valueOf(major),
                                           BigInteger.valueOf(minor),
                                           BigInteger.valueOf(patch)),
                           PreRelease.NULL,
                           BuildMetadata.NULL);
    }
    
    public static Version getVersion(BigInteger major, BigInteger minor, BigInteger patch, PreRelease preRelease) {
        return new Version(new CoreVersion(major, minor, patch), preRelease, BuildMetadata.NULL);
    }
    
    public static Version getVersion(BigInteger major, BigInteger minor, BigInteger patch, BuildMetadata buildMetadata) {
        return new Version(new CoreVersion(major, minor, patch), PreRelease.NULL, buildMetadata);
    }
    
    public static Version getVersion(BigInteger major, BigInteger minor, BigInteger patch, PreRelease preRelease,
                                     BuildMetadata buildMetadata) {
        return new Version(new CoreVersion(major, minor, patch), preRelease, buildMetadata);
    }
    
    @NotNull
    @Contract(pure = true)
    public static Version parseVersion(String versionString) throws ParseException {
        return new VersionParser(versionString).parse();
    }
    
    @NotNull
    @Contract(pure = true)
    public static Version parseVersion(String coreVersion, String preReleaseVersion, String buildMetadataVersion) throws ParseException {
        StringBuilder builder = new StringBuilder();
        builder.append(coreVersion);
        
        if (preReleaseVersion != null)
            builder.append('-')
                   .append(preReleaseVersion);
        if (buildMetadataVersion != null)
            builder.append('+')
                   .append(buildMetadataVersion);
        
        return new VersionParser(builder.toString()).parse();
    }
}
