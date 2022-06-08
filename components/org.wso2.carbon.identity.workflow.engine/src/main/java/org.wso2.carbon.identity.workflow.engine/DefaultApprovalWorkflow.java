package org.wso2.carbon.identity.workflow.engine;

import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.metadata.InputData;
import org.wso2.carbon.identity.workflow.mgt.bean.metadata.ParameterMetaData;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowRuntimeException;
import org.wso2.carbon.identity.workflow.mgt.workflow.AbstractWorkflow;
import org.wso2.carbon.identity.workflow.mgt.workflow.WorkFlowExecutor;

import java.util.List;

public class DefaultApprovalWorkflow extends AbstractWorkflow {

    public DefaultApprovalWorkflow(Class<? extends WorkFlowExecutor> workFlowExecutorClass, String metaDataXML)
            throws WorkflowRuntimeException {

        super(null, workFlowExecutorClass, metaDataXML);
    }

    @Override
    protected InputData getInputData(ParameterMetaData parameterMetaData) {

        return null;
    }

    @Override
    public void deploy(List<Parameter> parameterList) throws WorkflowException {

        super.deploy(parameterList);
    }

}
