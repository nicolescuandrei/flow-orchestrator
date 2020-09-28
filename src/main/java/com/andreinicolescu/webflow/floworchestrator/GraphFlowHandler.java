package com.andreinicolescu.webflow.floworchestrator;

public interface GraphFlowHandler {

    /**
     * <p>Check access to the current node<p/>
     * <p>
     * If the stack does not contain the current node then
     * operation throws a {@link com.andreinicolescu.webflow.floworchestrator.exception.NodeAccessException}
     */
    void checkAccess(String currentNode);

    /**
     * Mark the next node as valid to be accessed
     */
    void markAsAccessible(String nextNode);

    /**
     * Pop all the nodes from the stack after a specific node
     */
    void popAllAboveOf(String node);

    /**
     * Pop all the nodes from the stack
     */
    void reset();
}
