/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package com.solostudios.strata.parser.tokenizer;


import org.apache.commons.text.TextStringBuilder;
import org.jetbrains.annotations.NotNull;


/**
 * Represents an error or a warning which occurred when parsing an input.
 */
public final class ParseException extends Exception {
    private static final long serialVersionUID = -2935358064424839548L;
    
    @NotNull
    private final Position position;
    
    public ParseException(@NotNull String message, @NotNull String parseString, @NotNull Position position) {
        super(new TextStringBuilder(message).appendNewLine()
                                            .append(parseString)
                                            .appendNewLine()
                                            .appendPadding(position.getPos(), ' ')
                                            .append('^')
                                            .toString(),
              null, false, false);
        this.position = position;
    }
    
    public ParseException(@NotNull String message, @NotNull Position position) {
        super(message, null, true, false);
        this.position = position;
    }
    
    public ParseException(@NotNull Exception exception, @NotNull String parseString, @NotNull Position position) {
        this(exception.getMessage(), parseString, position);
        addSuppressed(exception);
    }
    
    public ParseException(@NotNull Exception exception, @NotNull Position position) {
        this(exception.getMessage(), position);
        addSuppressed(exception);
    }
    
    @Override
    public String toString() {
        return String.format("%s", getMessage());
    }
    
    /**
     * Provides the position where the error or warning occurred.
     *
     * @return the position of this error or warning
     */
    @NotNull
    public Position getPosition() {
        return position;
    }
}