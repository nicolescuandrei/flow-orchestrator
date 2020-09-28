package com.andreinicolescu.webflow.floworchestrator.exception;

import lombok.Getter;

public class NodeAccessException extends RuntimeException {

    @Getter
    private final String headNode;

    public NodeAccessException(String message, String headNode) {
        super(message);
        this.headNode = headNode;
    }
}
