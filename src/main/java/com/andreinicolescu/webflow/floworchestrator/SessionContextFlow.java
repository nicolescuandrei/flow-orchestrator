package com.andreinicolescu.webflow.floworchestrator;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SessionContextFlow implements Serializable {

    // stack
    private List<String> graphFlowNodes;
}
