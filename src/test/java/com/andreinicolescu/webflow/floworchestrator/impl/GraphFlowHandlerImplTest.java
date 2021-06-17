package com.andreinicolescu.webflow.floworchestrator.impl;

import com.andreinicolescu.webflow.floworchestrator.Node;
import com.andreinicolescu.webflow.floworchestrator.SessionContextFlow;
import com.andreinicolescu.webflow.floworchestrator.configuration.FlowOrchestratorConfigurationProperties;
import com.andreinicolescu.webflow.floworchestrator.exception.NodeAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GraphFlowHandlerImplTest {

    @Mock
    private SessionContextFlow sessionContextFlow;

    @Mock
    private FlowOrchestratorConfigurationProperties properties;

    @InjectMocks
    private GraphFlowHandlerImpl graphFlowHandler;

    @Test
    public void checkAccess_accessibleNode_doesNotThrowAnyException() {
        // given
        List<String> graphFlowNodes = new LinkedList<>();
        graphFlowNodes.add(Node.A);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(graphFlowNodes);

        // when
        graphFlowHandler.checkAccess(Node.A);
    }

    @Test
    public void checkAccess_inaccessibleNode_throwsException() {
        // given
        List<String> graphFlowNodes = new LinkedList<>();
        graphFlowNodes.add(Node.A);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(graphFlowNodes);

        // when
        Executable e = () -> graphFlowHandler.checkAccess(Node.B);

        // then
        assertThrows(NodeAccessException.class, e);
    }


    @Test
    public void markAsAccessible_newNode_nodeIsPushedToTheStack() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        when(properties.getMaxNodes()).thenReturn(5);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        // when
        graphFlowHandler.markAsAccessible(Node.A);

        // then
        assertEquals(1, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
    }

    @Test
    public void markAsAccessible_sameAsPrevious_nodeIsNotPushedToTheStack() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        when(properties.getMaxNodes()).thenReturn(5);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        // when
        graphFlowHandler.markAsAccessible(Node.A);
        graphFlowHandler.markAsAccessible(Node.A);

        // then
        assertEquals(1, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
    }

    @Test
    public void markAsAccessible_existingNodeCircularGraph_nodeIsPushedToTheStack() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        when(properties.getMaxNodes()).thenReturn(5);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        // when
        graphFlowHandler.markAsAccessible(Node.A);
        graphFlowHandler.markAsAccessible(Node.B);
        graphFlowHandler.markAsAccessible(Node.A);

        // then
        assertEquals(3, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
        assertEquals(Node.B, spyGraphFlowNodes.get(1));
        assertEquals(Node.A, spyGraphFlowNodes.get(2));
    }

    @Test
    public void markAsAccessible_stackLimitExceeded_tailNodeIsRemoved() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        when(properties.getMaxNodes()).thenReturn(2);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        // when
        graphFlowHandler.markAsAccessible(Node.C);

        // then
        assertEquals(2, spyGraphFlowNodes.size());
        assertEquals(Node.B, spyGraphFlowNodes.get(0));
        assertEquals(Node.C, spyGraphFlowNodes.get(1));
    }

    @Test
    public void popAllAboveOf_tailNode_allOfTheAboveNodesAreRemovedFromStack() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        spyGraphFlowNodes.add(Node.C);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        //when
        graphFlowHandler.popAllAboveOf(Node.A);

        // then
        assertEquals(1, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
    }

    @Test
    public void popAllAboveOf_nonexistentNode_noActionIsPerformed() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        spyGraphFlowNodes.add(Node.C);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        // when
        graphFlowHandler.popAllAboveOf(Node.D);

        // then
        assertEquals(3, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
        assertEquals(Node.B, spyGraphFlowNodes.get(1));
        assertEquals(Node.C, spyGraphFlowNodes.get(2));
    }

    @Test
    public void popAllAboveOf_headNode_noActionIsPerformed() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        spyGraphFlowNodes.add(Node.C);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);

        // when
        graphFlowHandler.popAllAboveOf(Node.C);

        // then
        assertEquals(3, spyGraphFlowNodes.size());
        assertEquals(Node.A, spyGraphFlowNodes.get(0));
        assertEquals(Node.B, spyGraphFlowNodes.get(1));
        assertEquals(Node.C, spyGraphFlowNodes.get(2));
    }

    @Test
    public void reset_resetFlow_allNodesAreRemoved() {
        // given
        List<String> spyGraphFlowNodes = spy(new LinkedList<>());
        spyGraphFlowNodes.add(Node.A);
        spyGraphFlowNodes.add(Node.B);
        when(sessionContextFlow.getGraphFlowNodes()).thenReturn(spyGraphFlowNodes);
        // when
        graphFlowHandler.reset();

        // then
        assertTrue(spyGraphFlowNodes.isEmpty());
    }
}