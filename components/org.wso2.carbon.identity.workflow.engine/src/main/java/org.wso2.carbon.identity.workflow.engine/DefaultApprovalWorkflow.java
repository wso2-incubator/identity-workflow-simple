package org.wso2.carbon.identity.workflow.engine;

import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.metadata.InputData;
import org.wso2.carbon.identity.workflow.mgt.bean.metadata.ParameterMetaData;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowRuntimeException;
import org.wso2.carbon.identity.workflow.mgt.workflow.AbstractWorkflow;
import org.wso2.carbon.identity.workflow.mgt.workflow.TemplateInitializer;
import org.wso2.carbon.identity.workflow.mgt.workflow.WorkFlowExecutor;

import java.util.List;

/**
 * The class that extends the AbstractWorkflow class.
 */
public class DefaultApprovalWorkflow extends AbstractWorkflow {

    public DefaultApprovalWorkflow(Class<? extends TemplateInitializer> templateInitializerClass, Class<? extends WorkFlowExecutor> workFlowExecutorClass, String metaDataXML)
            throws WorkflowRuntimeException {

        super(templateInitializerClass, workFlowExecutorClass, metaDataXML);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    protected InputData getInputData(ParameterMetaData parameterMetaData) {

        return new InputData();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void deploy(List<Parameter> parameterList) throws WorkflowException {

        super.deploy(parameterList);
    }

}
