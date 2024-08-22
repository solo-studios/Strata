/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Lookahead.java is part of Strata
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

package ca.solostudios.strata.parser.tokenizer;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract data structure for providing streams of items with a lookahead.
 * <p>
 * Items provided by subclasses of this are processed one after each other. However, using {@link #next()} or {@link #next(int)} one can
 * peek at upcoming items without consuming the current one.
 *
 * @param <T> the type of the elements kept in the stream
 */
public abstract class Lookahead<T> {
    /**
     * Internal buffer containing items which where already created due to lookaheads.
     */
    @NotNull
    protected final List<T> itemBuffer = new ArrayList<>();

    /**
     * Determines if the end of the underlying data source has been reached.
     */
    protected boolean endReached = false;

    /**
     * Once the end of the underlying input was reached, an end of input indicator is created and constantly returned for all calls of
     * current and next.
     */
    @Nullable
    protected T endOfInputIndicator = null;

    /**
     * Returns the next item after the current one in the stream.
     * <p>
     * This method does not change the internal state. Therefore it can be called several times and will always return the same result.
     *
     * @return The next item in the stream. This will be the current item, after a call to {@link #consume()}you
     * @throws ParseException If there is an exception during parsing.
     */
    @NotNull
    @Contract(pure = true)
    public T next() throws ParseException {
        return next(1);
    }

    /**
     * Returns the item the stream is currently pointing at.
     * <p>
     * This method does not change the internal state. Therefore it can be called several times and will always return the same result.
     *
     * @return the item the stream is currently pointing at.
     * @throws ParseException If there is an exception during parsing.
     */
    @NotNull
    @Contract(pure = true)
    public T current() throws ParseException {
        return next(0);
    }

    /**
     * Returns the next n-th item in the stream.
     * <p>
     * Calling this method with 0 as parameter, will return the current item. Calling it with 1 will return the same item as a call to
     * {@link #next}.
     * <p>
     * This method does not change the internal state. Therefore it can be called several times and will always return the same result.
     *
     * @param offset the number of items to skip
     * @return the n-th item in the stream
     * @throws ParseException If there is an exception during parsing.
     */
    @NotNull
    @Contract(pure = true)
    public T next(int offset) throws ParseException {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        boolean endReached = false;
        while (this.itemBuffer.size() <= offset && !endReached) {
            T item = fetch();
            if (item != null) {
                this.itemBuffer.add(item);
            } else {
                endReached = true;
            }
        }
        if (offset >= this.itemBuffer.size()) {
            if (this.endOfInputIndicator == null) {
                this.endOfInputIndicator = endOfInput();
            }
            return this.endOfInputIndicator;
        } else {
            return this.itemBuffer.get(offset);
        }
    }

    /**
     * Removes and returns the current item from the stream.
     * <p>After this method was called, all calls to {@link #current()} will then return the item, which
     * was previously returned by {@link #next()}
     *
     * @return the item which is being removed from the stream
     * @throws ParseException If there is an exception during parsing.
     */
    @NotNull
    public T consume() throws ParseException {
        T result = current();
        consume(1);
        return result;
    }

    /**
     * Consumes (removes) {@code numberOfItems} at once.
     * <p>
     * Removes the given number of items from the stream.
     *
     * @param numberOfItems the number of items to remove
     * @throws ParseException If there is an exception during parsing.
     */
    public void consume(int numberOfItems) throws ParseException {
        int items = numberOfItems;
        if (items < 0) {
            throw new IllegalArgumentException("numberOfItems < 0");
        }
        while (items-- > 0) {
            if (!this.itemBuffer.isEmpty()) {
                this.itemBuffer.remove(0);
            } else {
                if (this.endReached) {
                    return;
                }
                T item = fetch();
                if (item == null) {
                    this.endReached = true;
                }
            }
        }
    }

    /**
     * Creates the end of input indicator item.
     * <p>
     * This method will be only called once, as the indicator is cached.
     *
     * @return a special item which marks the end of the input
     */
    @NotNull
    @Contract(pure = true)
    protected abstract T endOfInput();

    /**
     * Fetches the next item from the stream.
     *
     * @return the next item in the stream or {@code null} to indicate that the end was reached
     * @throws ParseException If there is an exception during parsing.
     */
    @Nullable
    protected abstract T fetch() throws ParseException;
}
