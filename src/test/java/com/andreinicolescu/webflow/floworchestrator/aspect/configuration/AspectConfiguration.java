package com.andreinicolescu.webflow.floworchestrator.aspect.configuration;

import com.andreinicolescu.webflow.floworchestrator.GraphFlowHandler;
import com.andreinicolescu.webflow.floworchestrator.SessionContextFlow;
import com.andreinicolescu.webflow.floworchestrator.aspect.ControllerBean;
import com.andreinicolescu.webflow.floworchestrator.aspect.GraphFlowHandlerAspect;
import com.andreinicolescu.webflow.floworchestrator.configuration.FlowOrchestratorConfigurationProperties;
import com.andreinicolescu.webflow.floworchestrator.impl.GraphFlowHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.LinkedList;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    public FlowOrchestratorConfigurationProperties flowOrchestratorConfigurationProperties() {
        return new FlowOrchestratorConfigurationProperties();
    }

    @Bean
    public SessionContextFlow sessionContextFlow() {
        SessionContextFlow sessionContextFlow = new SessionContextFlow();
        sessionContextFlow.setGraphFlowNodes(new LinkedList<>());

        return sessionContextFlow;
    }

    @Bean
    public GraphFlowHandler graphFlowHandler(SessionContextFlow sessionContextFlow,
                                             FlowOrchestratorConfigurationProperties properties) {
        return new GraphFlowHandlerImpl(sessionContextFlow, properties);
    }

    @Bean
    public GraphFlowHandlerAspect graphFlowHandlerAspect(GraphFlowHandler graphFlowHandler) {
        return new GraphFlowHandlerAspect(graphFlowHandler);
    }

    @Bean
    public ControllerBean controllerTest() {
        return new ControllerBean();
    }
}
