/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CoreVersion.java is part of Strata
 * Last modified on 10-03-2022 12:00 p.m.
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


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;


/**
 * A class representing the core {@code major}, {@code minor}, and {@code patch} versions for {@link Version}.
 *
 * @author solonovamax
 */
public final class CoreVersion implements Comparable<CoreVersion>, Formattable {
    @NotNull
    private final BigInteger major;

    @NotNull
    private final BigInteger minor;

    @NotNull
    private final BigInteger patch;

    /**
     * Constructs a new core version instance.
     *
     * @param major The major version.
     * @param minor The minor version.
     * @param patch The patch version.
     */
    @Contract(pure = true)
    public CoreVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * The major version.
     *
     * @return The major version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getMajor() {
        return this.major;
    }

    /**
     * The minor version.
     *
     * @return The minor version.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getMinor() {
        return this.minor;
    }

    /**
     * The patch version
     *
     * @return The patch version
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger getPatch() {
        return this.patch;
    }

    @Override
    @Contract(pure = true)
    public int hashCode() {
        int result = this.major.hashCode();
        result = 31 * result + this.minor.hashCode();
        result = 31 * result + this.patch.hashCode();
        return result;
    }

    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CoreVersion coreVersion = (CoreVersion) o;

        if (this.major.compareTo(coreVersion.major) != 0)
            return false;
        if (this.minor.compareTo(coreVersion.minor) != 0)
            return false;
        return this.patch.compareTo(coreVersion.patch) == 0;
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        return String.format("NormalVersion{major=%d, minor=%d, patch=%d}", this.major, this.minor, this.patch);
    }

    @Override
    @Contract(pure = true)
    public int compareTo(@NotNull CoreVersion o) {
        int majorComparison = this.major.compareTo(o.major);
        int minorComparison = this.minor.compareTo(o.minor);
        return (majorComparison != 0) ? majorComparison : ((minorComparison != 0) ? minorComparison : this.patch.compareTo(o.patch));
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String getFormatted() {
        return String.format("%s.%s.%s", this.major, this.minor, this.patch);
    }
}
