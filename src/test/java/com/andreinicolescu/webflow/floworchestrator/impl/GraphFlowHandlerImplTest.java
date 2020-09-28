package com.andreinicolescu.webflow.floworchestrator.impl;

import com.andreinicolescu.webflow.floworchestrator.Node;
import com.andreinicolescu.webflow.floworchestrator.SessionContextFlow;
import com.andreinicolescu.webflow.floworchestrator.configuration.FlowOrchestratorConfigurationProperties;
import com.andreinicolescu.webflow.floworchestrator.exception.NodeAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GraphFlowHandlerImplTest {

    @Mock
    private SessionContextFlow sessionContextFlow;

    @Mock
    private FlowOrchestratorConfigurationProperties properties;

    @InjectMocks
    private GraphFlowHandlerImpl graphFlowHandler;

    @Test
    public void checkAccess_accessibleNode() {
        List<String> graphFlowNodes = new LinkedList<>();
        graphFlowNodes.add(Node.A);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(graphFlowNodes);

        graphFlowHandler.checkAccess(Node.A);

    }

    @Test
    public void checkAccess_inaccessibleNode() {
        List<String> graphFlowNodes = new LinkedList<>();
        graphFlowNodes.add(Node.A);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(graphFlowNodes);

        assertThrows(NodeAccessException.class, () ->
                graphFlowHandler.checkAccess(Node.B)
        );
    }


    @Test
    public void markAsAccessible() {
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());

        when(properties.getMaxNodes()).thenReturn(5);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        graphFlowHandler.markAsAccessible(Node.A);

        assertEquals(1, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
    }

    @Test
    public void markAsAccessible_NodesLimitExceeded() {
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);

        when(properties.getMaxNodes()).thenReturn(2);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        graphFlowHandler.markAsAccessible(Node.C);

        assertEquals(2, spyGraphFlowNodes.size());
        assertEquals(Node.B, spyGraphFlowNodes.get(0));
        assertEquals(Node.C, spyGraphFlowNodes.get(1));
    }

    @Test
    public void popAllAboveOf() {
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        spyGraphFlowNodes.add(Node.C);

        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        graphFlowHandler.popAllAboveOf(Node.A);

        assertEquals(1, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
    }

    @Test
    public void popAllAboveOf_outsideNode() {
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        spyGraphFlowNodes.add(Node.C);

        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        graphFlowHandler.popAllAboveOf(Node.D);

        assertEquals(3, spyGraphFlowNodes.size());
    }

    @Test
    public void popAllAboveOf_headNode() {
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        spyGraphFlowNodes.add(Node.C);

        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        graphFlowHandler.popAllAboveOf(Node.C);

        assertEquals(3, spyGraphFlowNodes.size());
        assertEquals(Node.C, spyGraphFlowNodes.get(2));
    }

    @Test
    public void reset() {
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);

        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        graphFlowHandler.reset();

        assertEquals(0, spyGraphFlowNodes.size());
    }
}