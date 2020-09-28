package com.andreinicolescu.webflow.floworchestrator.aspect;


import com.andreinicolescu.webflow.floworchestrator.Node;
import com.andreinicolescu.webflow.floworchestrator.SessionContextFlow;
import com.andreinicolescu.webflow.floworchestrator.aspect.configuration.AspectConfiguration;
import com.andreinicolescu.webflow.floworchestrator.exception.NodeAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AspectConfiguration.class})
public class GraphFlowHandlerAspectIT {

    @Autowired
    private SessionContextFlow sessionContextFlow;

    @Autowired
    private ControllerBean controller;

    @Test
    public void checkAccessToDisplay() {
        assertThrows(NodeAccessException.class, () ->
                controller.displayInnerPage()
        );
    }

    @Test
    public void checkAccessToSubmit() {
        assertThrows(NodeAccessException.class, () ->
                controller.submitInnerPage()
        );
    }

    @Test
    public void markAsAccessible() {
        controller.submitLandingPage();

        assertEquals(1, this.sessionContextFlow.getGraphFlowNodes().size());
        assertEquals(Node.A, this.sessionContextFlow.getGraphFlowNodes().get(0));
    }

    @Test
    public void resetFlow() {
        this.sessionContextFlow.getGraphFlowNodes().add(Node.A);
        this.sessionContextFlow.getGraphFlowNodes().add(Node.B);

        controller.displayLandingPage();

        assertEquals(0, this.sessionContextFlow.getGraphFlowNodes().size());
    }
}