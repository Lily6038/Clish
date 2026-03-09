package net.clish.ast;

import net.clish.lexer.Token;

/**
 * Base interface for all AST nodes.
 */
public interface ASTNode {
    /**
     * Get the token associated with this node (for source location).
     */
    Token getToken();

    /**
     * Get the type name of this node for debugging.
     */
    String getType();
}
