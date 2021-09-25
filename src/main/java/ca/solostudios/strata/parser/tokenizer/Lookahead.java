/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Lookahead.java is part of Strata
 * Last modified on 24-09-2021 08:15 p.m.
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
    protected List<T> itemBuffer = new ArrayList<>();
    
    /**
     * Determines if the end of the underlying data source has been reached.
     */
    protected boolean endReached = false;
    
    /**
     * Once the end of the underlying input was reached, an end of input indicator is created and constantly returned for all calls of
     * current and next.
     */
    protected T endOfInputIndicator = null;
    
    /**
     * Returns the next item after the current one in the stream.
     * <p>
     * This method does not change the internal state. Therefore it can be called several times and will always return the same result.
     *
     * @return the next item in the stream. This will be the current item, after a call to {@link #consume()}
     */
    @NotNull
    public T next() throws ParseException {
        return next(1);
    }
    
    /**
     * Returns the item the stream is currently pointing at.
     * <p>
     * This method does not change the internal state. Therefore it can be called several times and will always return the same result.
     *
     * @return the item the stream is currently pointing at.
     */
    @NotNull
    public T current() throws ParseException {
        return next(0);
    }
    
    /**
     * Returns the next n-th item in the stream.
     * <p>
     * Calling this method with 0 as parameter, will return the current item. Calling it with 1 will return the same item as a call to
     * <tt>next()</tt>.
     * <p>
     * This method does not change the internal state. Therefore it can be called several times and will always return the same result.
     *
     * @param offset the number of items to skip
     *
     * @return the n-th item in the stream
     */
    @NotNull
    public T next(int offset) throws ParseException {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        while (itemBuffer.size() <= offset && !endReached) {
            T item = fetch();
            if (item != null) {
                itemBuffer.add(item);
            } else {
                endReached = true;
            }
        }
        if (offset >= itemBuffer.size()) {
            if (endOfInputIndicator == null) {
                endOfInputIndicator = endOfInput();
            }
            return endOfInputIndicator;
        } else {
            return itemBuffer.get(offset);
        }
    }
    
    /**
     * Removes and returns the current item from the stream.
     * <p>After this method was called, all calls to {@link #current()} will then return the item, which
     * was previously returned by {@link #next()}
     *
     * @return the item which is being removed from the stream
     */
    @NotNull
    public T consume() throws ParseException {
        T result = current();
        consume(1);
        return result;
    }
    
    /**
     * Consumes (removes) <tt>numberOfItems</tt> at once.
     * <p>
     * Removes the given number of items from the stream.
     *
     * @param numberOfItems the number of items to remove
     */
    public void consume(int numberOfItems) throws ParseException {
        if (numberOfItems < 0) {
            throw new IllegalArgumentException("numberOfItems < 0");
        }
        while (numberOfItems-- > 0) {
            if (!itemBuffer.isEmpty()) {
                itemBuffer.remove(0);
            } else {
                if (endReached) {
                    return;
                }
                T item = fetch();
                if (item == null) {
                    endReached = true;
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
    protected abstract T endOfInput();
    
    /**
     * Fetches the next item from the stream.
     *
     * @return the next item in the stream or <tt>null</tt> to indicate that the end was reached
     */
    @Nullable
    protected abstract T fetch() throws ParseException;
}