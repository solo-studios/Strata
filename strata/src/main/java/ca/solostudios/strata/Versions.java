/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Versions.java is part of Strata.
 * Last modified on 22-08-2024 07:13 p.m.
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


/**
 * Helper class for constructing {@link Version} and {@link VersionRange} literals.
 *
 * @author solonovamax
 */
public final class Versions {
    private Versions() {
    }

    /**
     * Constructs a new version from the provided values.
     *
     * @param major The major version.
     * @param minor The minor version.
     * @param patch The patch version.
     *
     * @return The new version.
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Version getVersion(int major, int minor, int patch) {
        return new Version(new CoreVersion(BigInteger.valueOf(major),
                                           BigInteger.valueOf(minor),
                                           BigInteger.valueOf(patch)),
                           PreRelease.NULL,
                           BuildMetadata.NULL);
    }

    /**
     * Constructs a new version from the provided values.
     *
     * @param major The major version.
     * @param minor The minor version.
     * @param patch The patch version.
     *
     * @return The new version.
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch) {
        return new Version(new CoreVersion(major, minor, patch), PreRelease.NULL, BuildMetadata.NULL);
    }

    /**
     * Constructs a new version from the provided values.
     *
     * @param major      The major version.
     * @param minor      The minor version.
     * @param patch      The patch version.
     * @param preRelease The pre-release version.
     *
     * @return The new version.
     */
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch,
                                     @NotNull PreRelease preRelease) {
        return new Version(new CoreVersion(major, minor, patch), preRelease, BuildMetadata.NULL);
    }

    /**
     * Constructs a new version from the provided values.
     *
     * @param major         The major version.
     * @param minor         The minor version.
     * @param patch         The patch version.
     * @param buildMetadata The build metadata.
     *
     * @return The new version.
     */
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch,
                                     @NotNull BuildMetadata buildMetadata) {
        return new Version(new CoreVersion(major, minor, patch), PreRelease.NULL, buildMetadata);
    }

    /**
     * Constructs a new version from the provided values.
     *
     * @param major         The major version.
     * @param minor         The minor version.
     * @param patch         The patch version.
     * @param preRelease    The pre-release version.
     * @param buildMetadata The build metadata.
     *
     * @return The new version.
     */
    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static Version getVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch,
                                     @NotNull PreRelease preRelease, @NotNull BuildMetadata buildMetadata) {
        return new Version(new CoreVersion(major, minor, patch), preRelease, buildMetadata);
    }

    /**
     * Parses a version from the provided string, according to the semver spec as listed in {@link VersionParser}
     *
     * @param versionString The version string to parse.
     *
     * @return The parsed version.
     *
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.@see Versions#parseVersion(String)
     * @see VersionParser
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Version parseVersion(@NotNull String versionString) throws ParseException {
        return new VersionParser(versionString).parse();
    }

    /**
     * Parses a version from the provided {@code coreVersion}, {@code preReleaseVersion}, and {@code buildMetadataVersion}, according to the
     * semver spec as listed in {@link VersionParser}.
     *
     * @param coreVersion          The core version.
     * @param preReleaseVersion    The pre-release version.
     * @param buildMetadataVersion The build metadata.
     *
     * @return The parsed version.
     *
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.@see Versions#parseVersion(String)
     * @see VersionParser
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static Version parseVersion(@NotNull String coreVersion, @Nullable String preReleaseVersion,
                                       @Nullable String buildMetadataVersion) throws ParseException {
        StringBuilder builder = new StringBuilder(coreVersion.length() +
                                                  (preReleaseVersion == null ? 0 : preReleaseVersion.length()) +
                                                  (buildMetadataVersion == null ? 0 : buildMetadataVersion.length()));
        builder.append(coreVersion);

        if (preReleaseVersion != null)
            builder.append('-')
                   .append(preReleaseVersion);
        if (buildMetadataVersion != null)
            builder.append('+')
                   .append(buildMetadataVersion);

        return new VersionParser(builder.toString()).parse();
    }

    /**
     * Parses a {@link VersionRange} from the provided string, according to the specification described in {@link VersionRangeParser}.
     *
     * @param versionString The version range string to parse.
     *
     * @return The parsed version range.
     *
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.@see Versions#parseVersion(String)
     * @see VersionRangeParser
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static VersionRange parseVersionRange(@NotNull String versionString) throws ParseException {
        return new VersionRangeParser(versionString).parse();
    }

    /**
     * Constructs a new version range from the provided values.
     *
     * @param startVersion   The starting version. Use {@code null} if this has no lower bound.
     * @param startInclusive {@code true} if the starting version is inclusive, {@code false} if the starting version is exclusive.
     * @param endVersion     The ending version. Use {@code null} if this has no upper bound.
     * @param endInclusive   {@code true} if the ending version is inclusive, {@code false} if the ending version is exclusive.
     *
     * @return The version range from the provided values.
     *
     * @see VersionRange#VersionRange
     */
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static VersionRange getVersionRange(@Nullable Version startVersion, boolean startInclusive,
                                               @Nullable Version endVersion, boolean endInclusive) {
        return new VersionRange(startVersion, startInclusive, endVersion, endInclusive);
    }
}
