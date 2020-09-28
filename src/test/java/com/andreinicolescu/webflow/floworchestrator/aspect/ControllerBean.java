package com.andreinicolescu.webflow.floworchestrator.aspect;

import com.andreinicolescu.webflow.floworchestrator.Node;
import com.andreinicolescu.webflow.floworchestrator.annotation.CheckAccess;
import com.andreinicolescu.webflow.floworchestrator.annotation.MarkNextNodeAsAccessible;
import com.andreinicolescu.webflow.floworchestrator.annotation.ResetFlow;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Node.A)
public class ControllerBean {

    @ResetFlow
    @GetMapping
    public String displayLandingPage() {

        return "start";
    }

    @MarkNextNodeAsAccessible
    @PostMapping
    public String submitLandingPage() {
        String nextNode = Node.A;

        return "redirect:" + nextNode;
    }

    @CheckAccess
    @GetMapping
    public String displayInnerPage() {

        return "a";
    }

    @CheckAccess
    @PostMapping
    public String submitInnerPage() {
        String nextNode = Node.B;

        return "redirect:" + nextNode;
    }
}
