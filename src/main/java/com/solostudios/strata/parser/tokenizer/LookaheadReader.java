/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package com.solostudios.strata.parser.tokenizer;


import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;


/**
 * An efficient reader of character streams, reading character by character and supporting lookaheads.
 * <p>
 * Helps to read characters from a {@link Reader} one after another. Using <tt>next</tt>, upcoming characters can be inspected without
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
    public LookaheadReader(@NotNull Reader input) {
        super();
        this.input = new BufferedReader(input);
    }
    
    @Override
    public String toString() {
        if (itemBuffer.isEmpty()) {
            return String.format("%1d: Buffer empty", pos);
        }
        if (itemBuffer.size() < 2) {
            try {
                return String.format("%1d: %s", pos, current());
            } catch (ParseException e) {
                return String.format("%1d: Exception while fetching current.", pos);
            }
        }
        try {
            return String.format("%1d: %s, %s", pos, current(), next());
        } catch (ParseException e) {
            return String.format("%1d: Exception while fetching current or next.", pos);
        }
    }
    
    @Override
    protected Char fetch() throws ParseException {
        try {
            int character = input.read();
            if (character == -1) {
                return null;
            }
            return new Char((char) character, pos++);
        } catch (IOException e) {
            throw new ParseException(e, new Char('\0', pos));
        }
    }
    
    @Override
    protected Char endOfInput() {
        return new Char('\0', pos);
    }
}