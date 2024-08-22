/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file BuildMetadata.java is part of Strata.
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


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * A class representing the build metadata of a {@link Version}
 *
 * @author solonovamax
 */
public final class BuildMetadata implements Formattable {
    /**
     * An empty build metadata instance used it no metadata is provided.
     */
    public static final BuildMetadata NULL = new BuildMetadata("");

    @NotNull
    private final String buildMetadata;

    /**
     * Constructs a new build metadata
     *
     * @param buildMetadata The build metadata string to store.
     */
    @Contract(pure = true)
    public BuildMetadata(@NotNull String buildMetadata) {
        this.buildMetadata = buildMetadata;
    }

    /**
     * The build metadata as a string.
     *
     * @return The build metadata
     */
    @NotNull
    @Contract(pure = true)
    public String getBuildMetadata() {
        return this.buildMetadata;
    }

    @Override
    @Contract(pure = true)
    public int hashCode() {
        return this.buildMetadata.hashCode();
    }

    @Override
    @Contract(pure = true)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildMetadata that = (BuildMetadata) o;

        return this.buildMetadata.equals(that.buildMetadata);
    }

    @Override
    @Contract(pure = true)
    public String toString() {
        return String.format("BuildMetadata{buildMetadata='%s'}", this.buildMetadata);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String getFormatted() {
        if (!this.buildMetadata.isEmpty())
            return String.format("+%s", this.buildMetadata);
        else
            return "";
    }
}
