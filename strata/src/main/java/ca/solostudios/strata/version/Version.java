/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Version.java is part of Strata
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
        return coreVersion.getMajor();
    }
    
    /**
     * The minor version.
     *
     * @return The minor version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getMinor() {
        return coreVersion.getMinor();
    }
    
    /**
     * The patch version.
     *
     * @return The patch version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getPatch() {
        return coreVersion.getPatch();
    }
    
    /**
     * The core version.
     *
     * @return The core version.
     */
    @NotNull
    @Contract(pure = true)
    public CoreVersion getCoreVersion() {
        return coreVersion;
    }
    
    /**
     * The pre-release version.
     *
     * @return The pre-release version.
     */
    @NotNull
    @Contract(pure = true)
    public PreRelease getPreRelease() {
        return preRelease;
    }
    
    /**
     * The build metadata.
     *
     * @return The build metadata.
     */
    @NotNull
    @Contract(pure = true)
    public BuildMetadata getBuildMetadata() {
        return buildMetadata;
    }
    
    @Override
    @Contract(pure = true)
    public String toString() {
        return String.format("Version{normalVersion=%s, preRelease=%s, buildMetadata=%s}", coreVersion, preRelease, buildMetadata);
    }
    
    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(@Nullable Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        Version version = (Version) o;
        
        
        if (!coreVersion.equals(version.coreVersion))
            return false;
        if (!preRelease.equals(version.preRelease))
            return false;
        return buildMetadata.equals(version.buildMetadata);
    }
    
    @Override
    public int hashCode() {
        int result = coreVersion.hashCode();
        result = 31 * result + preRelease.hashCode();
        result = 31 * result + buildMetadata.hashCode();
        return result;
    }
    
    @Override
    public int compareTo(@NotNull Version o) {
        int normalVersionComparison = coreVersion.compareTo(o.coreVersion);
        return normalVersionComparison != 0 ? normalVersionComparison : preRelease.compareTo(o.preRelease);
    }
    
    @NotNull
    @Override
    public String getFormatted() {
        return String.format("%s%s%s", coreVersion.getFormatted(), preRelease.getFormatted(), buildMetadata.getFormatted());
    }
}
