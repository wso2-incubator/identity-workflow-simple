package org.wso2.carbon.identity.workflow.engine.internal;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.workflow.engine.DefaultApprovalWorkflow;
import org.wso2.carbon.identity.workflow.engine.DefaultWorkflowExecutor;
import org.wso2.carbon.identity.workflow.mgt.WorkflowExecutorManagerService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.workflow.AbstractWorkflow;

@Component(
        name = "org.wso2.carbon.identity.workflow.engine",
        immediate = true)
public class WorkflowEngineServiceComponent {
    @Activate
    protected void activate(ComponentContext context) {

        BundleContext bundleContext = context.getBundleContext();
        bundleContext.registerService(AbstractWorkflow.class, new DefaultApprovalWorkflow(DefaultWorkflowExecutor.class,
                getMetaDataXML()), null);
    }

    private String getMetaDataXML() {

        return "<met:MetaData xmlns:met=\"http://metadata.bean.mgt.workflow.identity.carbon.wso2.org\">\n" +
                "<met:WorkflowImpl>\n" +
                "    <met:WorkflowImplId>newWorkflowImpl</met:WorkflowImplId>\n" +
                "    <met:WorkflowImplName>ApprovalWorkflow</met:WorkflowImplName>\n" +
                "    <met:WorkflowImplDescription>Approval Workflow</met:WorkflowImplDescription>\n" +
                "    <met:TemplateId>MultiStepApprovalTemplate</met:TemplateId>\n" +
                "    <met:ParametersMetaData>\n" +
                "        <met:ParameterMetaData Name=\"BPSProfile\" InputType=\"Select\" isRequired=\"true\" isInputDataRequired=\"true\">\n" +
                "            <met:DisplayName>BPS Profile(Server Profile Name)</met:DisplayName>\n" +
                "        </met:ParameterMetaData>\n" +
                "        <met:ParameterMetaData Name=\"HTSubject\" DataType=\"String\" InputType=\"Text\" isRequired=\"true\">\n" +
                "            <met:DisplayName>Task Subject(Approval task subject to display)</met:DisplayName>\n" +
                "        </met:ParameterMetaData>\n" +
                "        <met:ParameterMetaData Name=\"HTDescription\" DataType=\"String\" InputType=\"TextArea\">\n" +
                "            <met:DisplayName>Task Detail(Approval task description)</met:DisplayName>\n" +
                "        </met:ParameterMetaData>\n" +
                "    </met:ParametersMetaData>\n" +
                "</met:WorkflowImpl>\n" +
                "</met:MetaData>";
    }

    @Reference(
            name = "org.wso2.carbon.identity.workflow.mgt",
            service = org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetWorkflowManagementService")
    protected void setWorkflowManagementService(WorkflowManagementService workflowManagementService) {

        WorkflowEngineServiceDataHolder.getInstance().setWorkflowManagementService(workflowManagementService);
    }

    protected void unsetWorkflowManagementService(WorkflowManagementService workflowManagementService) {
        WorkflowEngineServiceDataHolder.getInstance().setWorkflowManagementService(null);
    }

    @Reference(
            name = "org.wso2.carbon.identity.workflow.mgt.executor",
            service = org.wso2.carbon.identity.workflow.mgt.WorkflowExecutorManagerService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetWorkflowExecutorManagerService")
    protected void setWorkflowExecutorManagerService(WorkflowExecutorManagerService workflowExecutorManagerService) {
        WorkflowEngineServiceDataHolder.getInstance().setWorkflowExecutorManagerService(workflowExecutorManagerService);
    }

    protected void unsetWorkflowExecutorManagerService(WorkflowExecutorManagerService workflowExecutorManagerService) {
        WorkflowEngineServiceDataHolder.getInstance().setWorkflowExecutorManagerService(workflowExecutorManagerService);
    }
}
