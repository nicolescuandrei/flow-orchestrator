package com.andreinicolescu.webflow.floworchestrator.configuration;

import com.andreinicolescu.webflow.floworchestrator.GraphFlowHandler;
import com.andreinicolescu.webflow.floworchestrator.SessionContextFlow;
import com.andreinicolescu.webflow.floworchestrator.aspect.GraphFlowHandlerAspect;
import com.andreinicolescu.webflow.floworchestrator.impl.GraphFlowHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedList;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(FlowOrchestratorConfigurationProperties.class)
public class FlowOrchestratorConfiguration {
    private final FlowOrchestratorConfigurationProperties properties;

    @Autowired
    public FlowOrchestratorConfiguration(FlowOrchestratorConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    @SessionScope
    public SessionContextFlow sessionContextFlow() {
        SessionContextFlow sessionContextFlow = new SessionContextFlow();
        sessionContextFlow.setGraphFlowNodes(new LinkedList<>());

        return sessionContextFlow;
    }

    @Bean
    public GraphFlowHandler graphFlowHandler(SessionContextFlow sessionContextFlow) {
        return new GraphFlowHandlerImpl(sessionContextFlow, properties);
    }

    @Bean
    public GraphFlowHandlerAspect graphFlowHandlerAspect(GraphFlowHandler graphFlowHandler) {
        return new GraphFlowHandlerAspect(graphFlowHandler);
    }
}
