package com.andreinicolescu.webflow.floworchestrator.aspect;

import com.andreinicolescu.webflow.floworchestrator.GraphFlowHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;

import static java.util.Objects.nonNull;

@Slf4j
@Aspect
public class GraphFlowHandlerAspect {

    private static final String REDIRECT = "redirect:";

    private final GraphFlowHandler graphFlowHandler;

    public GraphFlowHandlerAspect(GraphFlowHandler graphFlowHandler) {
        this.graphFlowHandler = graphFlowHandler;
    }

    @Before(value = "anyController() " +
            "&& anyPublicMethod() " +
            "&& @annotation(com.andreinicolescu.webflow.floworchestrator.annotation.CheckAccess) " +
            "&& anyGetMethod()")
    public void checkAccessToDisplay(JoinPoint joinPoint) {
        String targetNode = getTargetNode(joinPoint);

        this.graphFlowHandler.checkAccess(targetNode);
        this.graphFlowHandler.popAllAboveOf(targetNode);
    }

    @Before(value = "anyController() " +
            "&& anyPublicMethod() " +
            "&& @annotation(com.andreinicolescu.webflow.floworchestrator.annotation.CheckAccess)" +
            "&& anyPostMethod() ")
    public void checkAccessToSubmit(JoinPoint joinPoint) {
        String targetNode = getTargetNode(joinPoint);

        this.graphFlowHandler.checkAccess(targetNode);
    }

    @AfterReturning(value = "anyController() " +
            "&& anyPublicMethod() " +
            "&& @annotation(com.andreinicolescu.webflow.floworchestrator.annotation.MarkNextNodeAsAccessible)" +
            "&& (anyPostMethod() || anyGetMethod())",
            returning = "redirectionPath")
    public void markAsAccessible(String redirectionPath) {
        if (redirectionPath.startsWith(REDIRECT)) {
            String nextNode = redirectionPath.substring(redirectionPath.indexOf(':') + 1);
            this.graphFlowHandler.markAsAccessible(nextNode);
        } else {
            log.error("Redirection path [{}] is invalid", redirectionPath);
        }
    }

    @AfterReturning(value = "anyController() " +
            "&& anyPublicMethod() " +
            "&& @annotation(com.andreinicolescu.webflow.floworchestrator.annotation.ResetFlow)")
    public void resetFlow() {
        this.graphFlowHandler.reset();
    }

    @Pointcut(value = "execution(public * * (..))")
    public void anyPublicMethod() {
    }

    @Pointcut(value = "within(@org.springframework.stereotype.Controller *)")
    public void anyController() {
    }

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void anyGetMethod() {
    }

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void anyPostMethod() {
    }

    private String getTargetNode(JoinPoint joinPoint) {
        RequestMapping requestMapping = getClassAnnotation(joinPoint, RequestMapping.class);
        String targetNode = null;

        if (nonNull(requestMapping)) {
            String[] nodes = requestMapping.value();
            targetNode = nodes[0];
        } else {
            log.error("@RequestMapping annotation missing from the controller class");
        }

        return targetNode;
    }

    private <T extends Annotation> T getClassAnnotation(JoinPoint joinPoint, Class<T> annotation) {
        return joinPoint.getTarget().getClass().getAnnotation(annotation);
    }
}
