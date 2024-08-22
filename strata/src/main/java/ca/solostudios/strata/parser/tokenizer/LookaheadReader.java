/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LookaheadReader.java is part of Strata.
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
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;


/**
 * An efficient reader of character streams, reading character by character and supporting lookaheads.
 * <p>
 * Helps to read characters from a {@link Reader} one after another. Using {@link #next()}, upcoming characters can be inspected without
 * consuming (removing) the current one.
 */
public class LookaheadReader extends Lookahead<Char> {
    @NotNull
    private final Reader input;

    private int pos = 0;

    /**
     * Creates a new LookaheadReader for the given Reader.
     * <p>
     * Internally a {@link BufferedReader} is used to efficiently read single characters. The given reader will not be closed by this
     * class.
     *
     * @param input the reader to draw the input from
     */
    @Contract(pure = true)
    public LookaheadReader(@NotNull Reader input) {
        super();
        this.input = new BufferedReader(input);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    protected Char endOfInput() {
        return new Char('\0', this.pos);
    }

    @Nullable
    @Override
    protected Char fetch() throws ParseException {
        try {
            int character = this.input.read();
            if (character == -1) {
                return null;
            }
            return new Char((char) character, this.pos++);
        } catch (IOException e) {
            throw new ParseException(e, new Char('\0', this.pos));
        }
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString() {
        if (this.itemBuffer.isEmpty()) {
            return String.format("%1d: Buffer empty", this.pos);
        }
        if (this.itemBuffer.size() < 2) {
            try {
                return String.format("%1d: %s", this.pos, current());
            } catch (ParseException e) {
                return String.format("%1d: Exception while fetching current.", this.pos);
            }
        }
        try {
            return String.format("%1d: %s, %s", this.pos, current(), next());
        } catch (ParseException e) {
            return String.format("%1d: Exception while fetching current or next.", this.pos);
        }
    }
}
