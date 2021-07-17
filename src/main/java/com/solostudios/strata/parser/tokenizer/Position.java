/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package com.solostudios.strata.parser.tokenizer;


/**
 * Describes a position in a file or a stream based on lines and the character position within the line.
 */
public interface Position {
    
    /**
     * Represents an unknown position for warnings and errors which cannot be associated with a defined position.
     */
    Position UNKNOWN = () -> 0;
    
    /**
     * Returns the character position within the line of this position
     *
     * @return the one-based character position of this
     */
    int getPos();
    
}