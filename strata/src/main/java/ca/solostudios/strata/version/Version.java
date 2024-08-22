/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Version.java is part of Strata.
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

package ca.solostudios.strata.version;


import ca.solostudios.strata.Versions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;


/**
 * Class representing a version.
 *
 * @author solonovamax
 * @see Versions#getVersion
 * @see Versions#parseVersion
 */
public final class Version implements Comparable<Version>, Formattable {
    @NotNull
    private final CoreVersion coreVersion;

    @NotNull
    private final PreRelease preRelease;

    @NotNull
    private final BuildMetadata buildMetadata;

    /**
     * Constructs a new version with the provided core, pre-release, and build data, values.
     *
     * @param coreVersion   The core version.
     * @param preRelease    The pre-release version.
     * @param buildMetadata The build data.
     */
    @Contract(pure = true)
    public Version(@NotNull CoreVersion coreVersion, @NotNull PreRelease preRelease, @NotNull BuildMetadata buildMetadata) {
        this.coreVersion = coreVersion;
        this.preRelease = preRelease;
        this.buildMetadata = buildMetadata;
    }

    /**
     * The major version.
     *
     * @return The major version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getMajor() {
        return this.coreVersion.getMajor();
    }

    /**
     * The minor version.
     *
     * @return The minor version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getMinor() {
        return this.coreVersion.getMinor();
    }

    /**
     * The patch version.
     *
     * @return The patch version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getPatch() {
        return this.coreVersion.getPatch();
    }

    /**
     * The core version.
     *
     * @return The core version.
     */
    @NotNull
    @Contract(pure = true)
    public CoreVersion getCoreVersion() {
        return this.coreVersion;
    }

    /**
     * The pre-release version.
     *
     * @return The pre-release version.
     */
    @NotNull
    @Contract(pure = true)
    public PreRelease getPreRelease() {
        return this.preRelease;
    }

    /**
     * The build metadata.
     *
     * @return The build metadata.
     */
    @NotNull
    @Contract(pure = true)
    public BuildMetadata getBuildMetadata() {
        return this.buildMetadata;
    }

    @Override
    @Contract(pure = true)
    public int hashCode() {
        int result = this.coreVersion.hashCode();
        result = 31 * result + this.preRelease.hashCode();
        result = 31 * result + this.buildMetadata.hashCode();
        return result;
    }

    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(@Nullable Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Version version = (Version) o;


        return this.coreVersion.equals(version.coreVersion) &&
               this.preRelease.equals(version.preRelease) &&
               this.buildMetadata.equals(version.buildMetadata);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        return String.format("Version{normalVersion=%s, preRelease=%s, buildMetadata=%s}", this.coreVersion, this.preRelease, this.buildMetadata);
    }

    @Override
    @Contract(pure = true)
    public int compareTo(@NotNull Version o) {
        int coreVersionComparison = this.coreVersion.compareTo(o.coreVersion);
        return coreVersionComparison != 0 ? coreVersionComparison : this.preRelease.compareTo(o.preRelease);
    }

    @NotNull
    @Override
    public String getFormatted() {
        return String.format("%s%s%s", this.coreVersion.getFormatted(), this.preRelease.getFormatted(), this.buildMetadata.getFormatted());
    }
}
