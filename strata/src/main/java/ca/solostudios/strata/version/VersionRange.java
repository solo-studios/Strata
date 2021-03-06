/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRange.java is part of Strata
 * Last modified on 19-06-2022 03:52 p.m.
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * A class representing a range of versions.
 *
 * @author solonovamax
 */
public final class VersionRange implements Formattable {
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
                if (0 < startVersion.getCoreVersion().compareTo(version.getCoreVersion()))
                    return false;
            } else {
                if (0 <= startVersion.getCoreVersion().compareTo(version.getCoreVersion()))
                    return false;
            }
        }
        if (endVersion != null) {
            if (endInclusive) {
                return 0 <= endVersion.getCoreVersion().compareTo(version.getCoreVersion());
            } else {
                return 0 < endVersion.getCoreVersion().compareTo(version.getCoreVersion());
            }
        }
        
        return true;
    }
    
    @Override
    @Contract(pure = true)
    public String toString() {
        return String.format("VersionRange{startVersion=%s, startInclusive=%b, endVersion=%s, endInclusive=%b}", startVersion,
                             startInclusive, endVersion, endInclusive);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        VersionRange range = (VersionRange) o;
        
        return startInclusive == range.startInclusive &&
               endInclusive == range.endInclusive &&
               Objects.equals(startVersion, range.startVersion) &&
               Objects.equals(endVersion, range.endVersion);
    }
    
    @Override
    public int hashCode() {
        int result = startVersion != null ? startVersion.hashCode() : 0;
        result = 31 * result + (startInclusive ? 1 : 0);
        result = 31 * result + (endVersion != null ? endVersion.hashCode() : 0);
        result = 31 * result + (endInclusive ? 1 : 0);
        return result;
    }
    
    @NotNull
    @Override
    public String getFormatted() {
        StringBuilder sb = new StringBuilder();
        if (startInclusive)
            sb.append('[');
        else
            sb.append('(');
    
        if (startVersion != null)
            sb.append(startVersion.getFormatted());
    
        sb.append(",");
    
        if (endVersion != null)
            sb.append(endVersion.getFormatted());
    
        if (endInclusive)
            sb.append(']');
        else
            sb.append(')');
    
        return sb.toString();
    }
}
