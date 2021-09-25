/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRange.java is part of Strata
 * Last modified on 24-09-2021 10:47 p.m.
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
import ca.solostudios.strata.parser.tokenizer.ParseException;
import org.jetbrains.annotations.Nullable;


/**
 * A class representing a range of versions.
 *
 * @author solonovamax
 */
public final class VersionRange {
    @Nullable
    private final Version startVersion;
    
    private final boolean startInclusive;
    
    @Nullable
    private final Version endVersion;
    
    private final boolean endInclusive;
    
    /**
     * Constructs a new version range.
     *
     * @param startVersion   The starting version. Use {@code null} if this has no lower bound.
     * @param startInclusive {@code true} if the starting version is inclusive, {@code false} if the starting version is exclusive.
     * @param endVersion     The ending version. Use {@code null} if this has no upper bound.
     * @param endInclusive   {@code true} if the ending version is inclusive, {@code false} if the ending version is exclusive.
     */
    public VersionRange(@Nullable Version startVersion, boolean startInclusive,
                        @Nullable Version endVersion, boolean endInclusive) {
        this.startVersion = startVersion;
        this.startInclusive = startInclusive;
        this.endVersion = endVersion;
        this.endInclusive = endInclusive;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (startInclusive)
            sb.append('[');
        else
            sb.append('(');
        
        if (startVersion != null)
            sb.append(startVersion);
        
        sb.append(",");
        
        if (endVersion != null)
            sb.append(endVersion);
        
        return sb.toString();
    }
    
    /**
     * The lower bound for the version range.
     * <p>
     * {@code null} is returned if this has no lower bound.
     *
     * @return The start version.
     *
     * @see #isStartInclusive()
     */
    @Nullable
    public Version getStartVersion() {
        return startVersion;
    }
    
    /**
     * Whether or not the lower version is inclusive.
     *
     * @return {@code true} if the start is inclusive, {@code false} if the start is exclusive.
     *
     * @see #getStartVersion()
     */
    public boolean isStartInclusive() {
        return startInclusive;
    }
    
    /**
     * The upper bound for the version range.
     * <p>
     * {@code null} is used if this has no upper bound.
     *
     * @return The end version.
     *
     * @see #isEndInclusive()
     */
    @Nullable
    public Version getEndVersion() {
        return endVersion;
    }
    
    /**
     * Wehther or not the upper version is inclusive.
     *
     * @return {@code true} if the end is inclusive, {@code false} if the end is exclusive.
     *
     * @see #getEndVersion()
     */
    public boolean isEndInclusive() {
        return endInclusive;
    }
    
    /**
     * Whether or not this range is satisfied by the provided version.
     *
     * @param version The version to check against.
     *
     * @return {@code true} if this range is satisfied by the provided version, {@code false} otherwise.
     *
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.@see Versions#parseVersion(String)
     * @see Versions#parseVersion(String)
     */
    public boolean isSatisfiedBy(String version) throws ParseException {
        return this.isSatisfiedBy(Versions.parseVersion(version));
    }
    
    /**
     * Whether or not this range is satisfied by the provided version.
     *
     * @param version The version to check against.
     *
     * @return {@code true} if this range is satisfied by the provided version, {@code false} otherwise.
     */
    public boolean isSatisfiedBy(Version version) {
        if (startVersion != null) {
            if (startInclusive) {
                if (0 < startVersion.compareTo(version))
                    return false;
            } else {
                if (0 <= startVersion.compareTo(version))
                    return false;
            }
        }
        if (endVersion != null) {
            if (endInclusive) {
                return 0 <= endVersion.compareTo(version);
            } else {
                return 0 < endVersion.compareTo(version);
            }
        }
        
        return true;
    }
}
