/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Position.java is part of Strata.
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

package ca.solostudios.strata.parser.tokenizer;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a position in a file or a stream based on lines and the character position within the line.
 */
public interface Position {

    /**
     * Returns the character position within the line of this position
     *
     * @return the one-based character position of this
     */
    @Contract(pure = true)
    int getPos();

    @NotNull
    @Contract(pure = true)
    default Position increment() {
        return increment(1);
    }

    @NotNull
    @Contract(pure = true)
    Position increment(int by);

    /**
     * Represents an unknown position for warnings and errors which cannot be associated with a defined position.
     */
    Position UNKNOWN = new Position() {
        @Override
        public int getPos() {
            return 0;
        }

        @NotNull
        @Override
        @Contract(pure = true)
        public Position increment(int by) {
            return UNKNOWN;
        }
    };
}

