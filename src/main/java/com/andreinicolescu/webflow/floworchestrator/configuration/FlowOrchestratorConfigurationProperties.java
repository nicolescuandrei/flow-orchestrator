package com.andreinicolescu.webflow.floworchestrator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@Validated
@ConfigurationProperties(prefix = "flow-orchestrator")
public class FlowOrchestratorConfigurationProperties {

    /**
    * The maximum number of nodes that the flow execution can have. The default is 31, which is generally high
    * enough not to interfere with the user experience of normal users using the back button, but low enough to avoid
    * excessive resource usage or denial of service attacks.
    */
    @Min(1)
    private int maxNodes = 31;
}
