/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Versions.java is part of Strata
 * Last modified on 24-09-2021 07:19 p.m.
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


import ca.solostudios.strata.parser.VersionParser;
import ca.solostudios.strata.parser.VersionRangeParser;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.BuildMetadata;
import ca.solostudios.strata.version.CoreVersion;
import ca.solostudios.strata.version.PreRelease;
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;


public final class Versions {
    private Versions() {
    }
    
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Version getVersion(int major, int minor, int patch) {
        return new Version(new CoreVersion(BigInteger.valueOf(major),
                                           BigInteger.valueOf(minor),
                                           BigInteger.valueOf(patch)),
                           PreRelease.NULL,
                           BuildMetadata.NULL);
    }
    
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch) {
        return new Version(new CoreVersion(major, minor, patch), PreRelease.NULL, BuildMetadata.NULL);
    }
    
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch,
                                     @NotNull PreRelease preRelease) {
        return new Version(new CoreVersion(major, minor, patch), preRelease, BuildMetadata.NULL);
    }
    
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch,
                                     @NotNull BuildMetadata buildMetadata) {
        return new Version(new CoreVersion(major, minor, patch), PreRelease.NULL, buildMetadata);
    }
    
    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch,
                                     @NotNull PreRelease preRelease, @NotNull BuildMetadata buildMetadata) {
        return new Version(new CoreVersion(major, minor, patch), preRelease, buildMetadata);
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Version parseVersion(@NotNull String versionString) throws ParseException {
        return new VersionParser(versionString).parse();
    }
    
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Version parseVersion(@NotNull String coreVersion, @Nullable String preReleaseVersion,
                                       @Nullable String buildMetadataVersion) throws ParseException {
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
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static VersionRange parseVersionRange(@NotNull String versionString) throws ParseException {
        return new VersionRangeParser(versionString).parse();
    }
    
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static VersionRange getVersionRange(@Nullable Version startVersion, boolean startInclusive,
                                               @Nullable Version endVersion, boolean endInclusive) {
        return new VersionRange(startVersion, startInclusive, endVersion, endInclusive);
    }
}
