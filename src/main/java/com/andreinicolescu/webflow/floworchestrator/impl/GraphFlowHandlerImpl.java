package com.andreinicolescu.webflow.floworchestrator.impl;

import com.andreinicolescu.webflow.floworchestrator.GraphFlowHandler;
import com.andreinicolescu.webflow.floworchestrator.SessionContextFlow;
import com.andreinicolescu.webflow.floworchestrator.configuration.FlowOrchestratorConfigurationProperties;
import com.andreinicolescu.webflow.floworchestrator.exception.NodeAccessException;

import java.util.List;
import java.util.ListIterator;

public class GraphFlowHandlerImpl implements GraphFlowHandler {

    private final SessionContextFlow sessionContextFlow;

    private final FlowOrchestratorConfigurationProperties orchestratorConfigurationProperties;

    public GraphFlowHandlerImpl(SessionContextFlow sessionContextFlow, FlowOrchestratorConfigurationProperties orchestratorConfigurationProperties) {
        this.sessionContextFlow = sessionContextFlow;
        this.orchestratorConfigurationProperties = orchestratorConfigurationProperties;
    }

    @Override
    public void checkAccess(String currentNode) {
        List<String> graphFlowNodes = this.sessionContextFlow.getGraphFlowNodes();

        if (!graphFlowNodes.contains(currentNode)) {
            String headNode = !graphFlowNodes.isEmpty() ?
                    graphFlowNodes.get(graphFlowNodes.size() - 1) : null;

            throw new NodeAccessException("Node [" + currentNode + "] is inaccessible", headNode);
        }
    }

    @Override
    public void markAsAccessible(String nextNode) {
        List<String> graphFlowNodes = sessionContextFlow.getGraphFlowNodes();

        // push the next node only if it is different than the last node
        if (graphFlowNodes.isEmpty() || !nextNode.equals(graphFlowNodes.get(graphFlowNodes.size() - 1))) {
            if (this.sessionContextFlow.getGraphFlowNodes().size() == this.orchestratorConfigurationProperties.getMaxNodes()) {
                // keep only the latest x nodes, x = maxNodes
                this.sessionContextFlow.getGraphFlowNodes().remove(0);
            }
            /* grant access to the next node */
            graphFlowNodes.add(nextNode);
        }
    }

    @Override
    public void popAllAboveOf(String node) {
        List<String> graphFlowNodes = this.sessionContextFlow.getGraphFlowNodes();

        if (graphFlowNodes.contains(node)) {
            ListIterator<String> li = graphFlowNodes.listIterator(graphFlowNodes.size());
            while (li.hasPrevious() && !li.previous().equals(node)) {
                li.remove();
            }
        }
    }

    @Override
    public void reset() {
        this.sessionContextFlow.getGraphFlowNodes().clear();
    }

}
