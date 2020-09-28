package com.andreinicolescu.webflow.floworchestrator.annotation;

import com.andreinicolescu.webflow.floworchestrator.configuration.FlowOrchestratorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = FlowOrchestratorConfiguration.class)
public @interface EnableFlowOrchestrator {
}
