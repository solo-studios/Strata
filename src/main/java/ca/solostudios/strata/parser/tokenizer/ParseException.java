/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ParseException.java is part of Strata
 * Last modified on 24-09-2021 11:44 p.m.
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


/**
 * Represents an error or a warning, which occurred when parsing an input.
 */
public final class ParseException extends RuntimeException {
    private static final long serialVersionUID = -2935358064424839548L;
    
    @NotNull
    private final Position position;
    
    /**
     * Constructs a new parse exception with the provided message, parse string, and position.
     *
     * @param message     The message explaining why the parsing exception occurred.
     * @param parseString The string that was being parsed.
     * @param position    The position at which the error occurred.
     */
    public ParseException(@NotNull String message, @NotNull String parseString, @NotNull Position position) {
        // mmmmm, doubly nested String#format, my *favourite*
        super(String.format(String.format("%%s\n%%s\n%%%ss", position.getPos() == 0 ? "" : position.getPos()), message, parseString, '^'),
              null, false, false);
        this.position = position;
    }
    
    /**
     * Constructs a new parse exception with the provided message and position.
     *
     * @param message  The message explaining why the parsing exception occurred.
     * @param position The position at which the error occurred.
     */
    public ParseException(@NotNull String message, @NotNull Position position) {
        super(message, null, true, false);
        this.position = position;
    }
    
    /**
     * Constructs a new parse exception with the provided exception as a cause, parse string, and position.
     *
     * @param exception   The exception, which caused this.
     * @param parseString The string that was being parsed.
     * @param position    The position at which the error occurred.
     */
    public ParseException(@NotNull Exception exception, @NotNull String parseString, @NotNull Position position) {
        this(exception.getMessage(), parseString, position);
        addSuppressed(exception);
    }
    
    /**
     * Constructs a new parse exception with the provided exception as a cause and position.
     *
     * @param exception The exception, which caused this.
     * @param position  The position at which the error occurred.
     */
    public ParseException(@NotNull Exception exception, @NotNull Position position) {
        this(exception.getMessage(), position);
        addSuppressed(exception);
    }
    
    /**
     * Formats this parse exception as a string.
     *
     * @return The parse exception formatted as a string
     */
    @Override
    public String toString() {
        return String.format("%s", getMessage());
    }
    
    /**
     * The position where the error or warning occurred.
     *
     * @return the position of this error or warning.
     */
    @NotNull
    public Position getPosition() {
        return position;
    }
}