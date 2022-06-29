package org.wso2.carbon.identity.workflow.engine;

import org.apache.commons.collections.CollectionUtils;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.workflow.engine.dto.PropertyDTO;
import org.wso2.carbon.identity.workflow.engine.dto.StateDTO;
import org.wso2.carbon.identity.workflow.engine.dto.TaskDataDTO;
import org.wso2.carbon.identity.workflow.engine.dto.TaskSummaryDTO;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineException;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineRuntimeException;
import org.wso2.carbon.identity.workflow.engine.internal.dao.WorkflowEventRequestDAO;
import org.wso2.carbon.identity.workflow.engine.internal.dao.impl.WorkflowEventRequestDAOImpl;
import org.wso2.carbon.identity.workflow.engine.model.PagePagination;
import org.wso2.carbon.identity.workflow.engine.model.TStatus;
import org.wso2.carbon.identity.workflow.engine.model.TaskDetails;
import org.wso2.carbon.identity.workflow.engine.model.TaskModel;
import org.wso2.carbon.identity.workflow.engine.model.TaskParam;
import org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants;
import org.wso2.carbon.identity.workflow.mgt.WorkflowExecutorManagerService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowExecutorManagerServiceImpl;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementServiceImpl;
import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.RequestParameter;
import org.wso2.carbon.identity.workflow.mgt.bean.WorkflowAssociation;
import org.wso2.carbon.identity.workflow.mgt.callback.WSWorkflowCallBackService;
import org.wso2.carbon.identity.workflow.mgt.callback.WSWorkflowResponse;
import org.wso2.carbon.identity.workflow.mgt.dto.WorkflowRequest;
import org.wso2.carbon.identity.workflow.mgt.exception.InternalWorkflowException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Call internal osgi services to perform user's approval task related operations.
 */
public class ApprovalEventService {

    private static final String PENDING = "PENDING";
    private static final String APPROVED = "APPROVED";
    private static final String REJECTED = "REJECTED";
    private static final String RELEASED = "RELEASED";
    private static final String CLAIMED = "CLAIMED";
    private static final Integer LIMIT = 20;
    private static final Integer OFFSET = 0;

    /**
     * Search available approval tasks for the current authenticated user.
     *
     * @param limit  number of records to be returned.
     * @param offset start page.
     * @param status state of the tasks [RESERVED, READY or COMPLETED].
     * @return taskSummaryDTO list.
     */
    public List<TaskSummaryDTO> listTasks(Integer limit, Integer offset, List<String> status) {

        PagePagination pagePagination = new PagePagination();
        if (limit == null || offset == null) {
            pagePagination.setPageSize(LIMIT);
            pagePagination.setPageNumber(OFFSET);
        }

        if (limit != null && limit > 0) {
            pagePagination.setPageSize(limit);
        }
        if (offset != null && offset > 0) {
            pagePagination.setPageNumber(offset);
        }

        Set<TaskSummaryDTO> taskSummaryDTOs = null;
        List<TaskSummaryDTO> tasks = listTasksOfApprovers(status);
        int taskListSize = tasks.size();
        for (int i = 0; i < taskListSize; ++i) {
            taskSummaryDTOs = new HashSet<>(tasks);
        }
        if (taskSummaryDTOs == null) {
            return new ArrayList<>(0);
        }
        return new ArrayList<>(taskSummaryDTOs);
    }

    private List<TaskSummaryDTO> listTasksOfApprovers(List<String> status) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        DefaultWorkflowEventRequest defaultWorkflowEventRequest = new DefaultWorkflowEventRequestService();
        List<String> allRequestsList = getAllRequestRelatedUserAndRole();
        List<TaskSummaryDTO> taskSummaryDTOList = new ArrayList<>();
        for (String task : allRequestsList) {
            TaskSummaryDTO summeryDTO = new TaskSummaryDTO();
            String eventId = getTaskRelatedStatus(task, status);
            if (eventId != null) {
                WorkflowRequest request = getWorkflowRequest(eventId);
                TaskDetails taskDetails = getTaskDetails(request);
                String eventType = workflowEventRequestDAO.getEventType(request.getUuid());
                String taskId = defaultWorkflowEventRequest.getApprovalOfRequest(request.getUuid());
                String taskStatus = workflowEventRequestDAO.getTaskStatusOfRequest(taskId);
                String[] taskStatusValue = taskStatus.split(",", 0);
                Timestamp createdTime = workflowEventRequestDAO.getCreatedAtTimeInMill(request.getUuid());
                summeryDTO.setId(taskId);
                summeryDTO.setName(WorkflowEngineConstants.ParameterName.APPROVAL_TASK);
                summeryDTO.setTaskType(eventType);
                summeryDTO.setPresentationName(taskDetails.getTaskSubject());
                summeryDTO.setPresentationSubject(taskDetails.getTaskDescription());
                summeryDTO.setCreatedTimeInMillis(String.valueOf(createdTime));
                summeryDTO.setPriority(Integer.valueOf(WorkflowEngineConstants.ParameterName.PRIORITY));
                summeryDTO.setStatus(TaskSummaryDTO.StatusEnum.valueOf(taskStatusValue[0]));
                taskSummaryDTOList.add(summeryDTO);
            }
        }
        return taskSummaryDTOList;
    }

    private List<String> getAllRequestRelatedUserAndRole(){

        String userName = CarbonContext.getThreadLocalCarbonContext().getUsername();
        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        List<String> roleNames = getRoleNamesFromUser(userName);
        List<String> roleRequestsList;
        List<String> lst = new ArrayList<>();
        for (String roleName : roleNames) {
            String[] names = roleName.split("/", 0);
            String[] newName = roleName.split("_", 0);
            if (names[0].equals(WorkflowEngineConstants.ParameterName.APPLICATION_USER)) {
                roleRequestsList = workflowEventRequestDAO.getRequestsList(roleName);
                lst.addAll(roleRequestsList);
            } else if (newName[0].equals(WorkflowEngineConstants.ParameterName.SYSTEM_USER)) {
                String newRoleName = WorkflowEngineConstants.ParameterName.SYSTEM_PRIMARY_USER.concat(names[0]);
                roleRequestsList = workflowEventRequestDAO.getRequestsList(newRoleName);
                lst.addAll(roleRequestsList);
            } else {
                String newRoleName = WorkflowEngineConstants.ParameterName.INTERNAL_USER.concat(names[0]);
                roleRequestsList = workflowEventRequestDAO.getRequestsList(newRoleName);
                lst.addAll(roleRequestsList);
            }
        }

        List<String> userRequestList = workflowEventRequestDAO.getRequestIdFromApprover(userName);
        return Stream.concat(lst.stream(), userRequestList.stream()).collect(Collectors.toList());
    }

    private List<String> getRoleNamesFromUser(String approverName) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        List<Integer> roleIDList = workflowEventRequestDAO.getRolesID(approverName);
        List<String> roleNameList;
        List<String> lst = new ArrayList<>();
        for (Integer roleId : roleIDList) {
            roleNameList = workflowEventRequestDAO.getRoleNames(roleId);
            lst.addAll(roleNameList);
        }
        return new ArrayList<>(lst);
    }

    private String getTaskRelatedStatus(String requestId, List<String> status) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();

        TStatus[] tStatuses = getRequiredTStatuses(status);
        String taskStatus = workflowEventRequestDAO.getStatusOfTask(requestId);
        String[] taskStatusValue = taskStatus.split(",", 0);
        String eventId = null;
        String value;
        for (TStatus tStatus : tStatuses) {
            value = tStatus.getTStatus();
            if (value.equals(taskStatusValue[0])) {
                eventId = requestId;
                break;
            }
        }
        return eventId;
    }

    /**
     * Get details of a task identified by the taskId.
     *
     * @param taskId the unique ID.
     * @return TaskDataDto object.
     */
    public TaskDataDTO getTaskData(String taskId) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        String requestId = workflowEventRequestDAO.getRequestID(taskId);
        WorkflowRequest request = getWorkflowRequest(requestId);
        TaskDetails details = getTaskDetails(request);
        String initiator = workflowEventRequestDAO.getInitiatedUser(requestId);
        List<String> approvers = workflowEventRequestDAO.listApprovers(taskId);
        Map<String, String> assigneeMap = null;
        for (String assignee : approvers) {
            assigneeMap = new HashMap<>();
            assigneeMap.put(WorkflowEngineConstants.ParameterName.ASSIGNEE_TYPE, assignee);
        }
        String status = workflowEventRequestDAO.getTaskStatusOfRequest(taskId);
        String statusValue;
        if (status.equals(String.valueOf(WorkflowEngineConstants.TaskStatus.RESERVED))) {
            statusValue = PENDING;
        } else if (status.equals(String.valueOf(WorkflowEngineConstants.TaskStatus.READY))) {
            statusValue = PENDING;
        } else if (status.equals(String.valueOf(WorkflowEngineConstants.TaskStatus.COMPLETED))) {
            statusValue = APPROVED;
        } else {
            statusValue = REJECTED;
        }
        List<TaskParam> params = getRequestParameters(request);
        List<PropertyDTO> properties = getPropertyDTOs(params);
        TaskDataDTO taskDataDTO = new TaskDataDTO();
        taskDataDTO.setId(taskId);
        taskDataDTO.setSubject(details.getTaskSubject());
        taskDataDTO.setDescription(details.getTaskDescription());
        taskDataDTO.setApprovalStatus(TaskDataDTO.ApprovalStatusEnum.valueOf(statusValue));
        taskDataDTO.setInitiator(initiator);
        taskDataDTO.setPriority(Integer.valueOf(WorkflowEngineConstants.ParameterName.PRIORITY));
        TaskModel taskModel = new TaskModel();
        taskModel.setAssignees(assigneeMap);
        taskDataDTO.setAssignees(getPropertyDTOs(taskModel.getAssignees()));
        taskDataDTO.setProperties(properties);
        return taskDataDTO;
    }

    /**
     * Update the state of a task identified by the task id.
     * User can reserve the task by claiming, or release a reserved task to himself.
     * Or user can approve or reject a task.
     *
     * @param taskId    the unique ID to update the state.
     * @param nextState event status.
     */
    public void updateStatus(String taskId, StateDTO nextState) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        validateApprovers(taskId);
            switch (nextState.getAction()) {
                case APPROVE:
                    updateTaskStatusOfRequest(taskId, APPROVED);
                    updateStepDetailsOfRequest(taskId);
                    break;
                case REJECT:
                    String eventId = workflowEventRequestDAO.getRequestID(taskId);
                    updateTaskStatusOfRequest(taskId, REJECTED);
                    completeRequest(eventId, REJECTED);
                    break;
                case RELEASE:
                    updateTaskStatusOfRequest(taskId, RELEASED);
                    break;
                case CLAIM:
                    updateTaskStatusOfRequest(taskId, CLAIMED);
                    break;
                default:
                    break;
        }
    }

    private WorkflowRequest getWorkflowRequest(String requestId) {

        WorkflowExecutorManagerService workFlowExecutorManagerService = new WorkflowExecutorManagerServiceImpl();
        WorkflowRequest request;
        try {
            request = workFlowExecutorManagerService.retrieveWorkflow(requestId);
        } catch (InternalWorkflowException e) {
            throw new WorkflowEngineRuntimeException("Cannot get workflow request given the request Id");
        }
        return request;
    }

    private void updateStepDetailsOfRequest(String taskId) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        String eventId = workflowEventRequestDAO.getRequestID(taskId);
        WorkflowRequest request = getWorkflowRequest(eventId);
        DefaultWorkflowEventRequest defaultWorkflowEventRequest = new DefaultWorkflowEventRequestService();
        List<Parameter> parameterList = getParameterList(request);
        String workflowId = defaultWorkflowEventRequest.getWorkflowId(request);
        defaultWorkflowEventRequest.deleteApprovalOfRequest(taskId);
        int stepValue = defaultWorkflowEventRequest.getStateOfRequest(eventId, workflowId);
        if (stepValue < numOfStates(request)) {
            defaultWorkflowEventRequest.addApproversOfRequests(request, parameterList);
        } else {
            completeRequest(eventId, ApprovalEventService.APPROVED);
        }
    }

    private void validateApprovers(String taskId) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        List<String> eventList =getAllRequestRelatedUserAndRole();
        List<String> taskList;
        List<String> lst = new ArrayList<>();
        for (String event : eventList) {
            taskList = workflowEventRequestDAO.getTaskId(event);
            lst.addAll(taskList);
        }

        for (String task : lst) {
            if (taskId.equals(task)) {
                return;
            }
        }
    }

    private int numOfStates(WorkflowRequest request) {

        List<Parameter> parameterList = getParameterList(request);
        int count = 0;
        for (Parameter parameter : parameterList) {
            if (parameter.getParamName().equals(WorkflowEngineConstants.ParameterName.USER_AND_ROLE_STEP)
                    && !parameter.getParamValue().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private void updateTaskStatusOfRequest(String taskId, String status) {

        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        switch (status) {
            case APPROVED:
                status = WorkflowEngineConstants.TaskStatus.COMPLETED.toString();
                break;
            case REJECTED:
                status = WorkflowEngineConstants.TaskStatus.COMPLETED.toString().concat(",").concat(REJECTED);
                break;
            case RELEASED:
                status = WorkflowEngineConstants.TaskStatus.READY.toString();
                break;
            case CLAIMED:
                status = WorkflowEngineConstants.TaskStatus.RESERVED.toString();
                break;
            default:
                status = WorkflowEngineConstants.ParameterName.STATUS_ERROR;
                break;
        }
        workflowEventRequestDAO.updateStatusOfRequest(taskId, status);
    }

    private void completeRequest(String eventId, String status) {

        WSWorkflowResponse wsWorkflowResponse = new WSWorkflowResponse();
        WorkflowEventRequestDAO workflowEventRequestDAO = new WorkflowEventRequestDAOImpl();
        String relationshipId = workflowEventRequestDAO.getRelationshipId(eventId);
        wsWorkflowResponse.setUuid(relationshipId);
        wsWorkflowResponse.setStatus(status);
        WSWorkflowCallBackService wsWorkflowCallBackService = new WSWorkflowCallBackService();
        wsWorkflowCallBackService.onCallback(wsWorkflowResponse);
    }

    private TaskDetails getTaskDetails(WorkflowRequest workflowRequest) {

        List<Parameter> parameterList = getParameterList(workflowRequest);
        TaskDetails taskDetails = new TaskDetails();
        for (Parameter parameter : parameterList) {
            if (parameter.getParamName().equals(WorkflowEngineConstants.ParameterName.TASK_SUBJECT)) {
                taskDetails.setTaskSubject(parameter.getParamValue());
            }
            if (parameter.getParamName().equals(WorkflowEngineConstants.ParameterName.TASK_DESCRIPTION)) {
                taskDetails.setTaskDescription(parameter.getParamValue());
            }
        }
        return taskDetails;
    }

    private List<Parameter> getParameterList(WorkflowRequest request) {

        WorkflowManagementService workflowManagementService = new WorkflowManagementServiceImpl();
        DefaultWorkflowEventRequest defaultWorkflowEventRequest = new DefaultWorkflowEventRequestService();
        List<WorkflowAssociation> associations = defaultWorkflowEventRequest.getAssociations(request);
        List<Parameter> parameterList = null;
        for (WorkflowAssociation association : associations) {
            try {
                parameterList = workflowManagementService.getWorkflowParameters(association.getWorkflowId());
            } catch (WorkflowException e) {
                throw new WorkflowEngineException("The parameterList can't get given the associated workflowId");
            }
        }
        return parameterList;
    }

    private List<TaskParam> getRequestParameters(WorkflowRequest request) {

        List<RequestParameter> requestParameter;
        List<TaskParam> taskParamsList = new ArrayList<>();
        for (int i = 0; i < request.getRequestParameters().size(); i++) {
            requestParameter = request.getRequestParameters();
            TaskParam taskParam = new TaskParam();
            Object value = requestParameter.get(i).getValue();
            if (requestParameter.get(i).getName().equals(WorkflowEngineConstants.ParameterName.CREDENTIAL)) {
                continue;
            }
            if (value != null) {
                taskParam.setItemValue(requestParameter.get(i).getValue().toString());
                taskParam.setItemName(requestParameter.get(i).getName());
                taskParamsList.add(taskParam);
            }
        }
        return taskParamsList;
    }

    private List<PropertyDTO> getPropertyDTOs(Map<String, String> props) {

        return props.entrySet().stream().map(p -> getPropertyDTO(p.getKey(), p.getValue()))
                .collect(Collectors.toList());
    }

    private List<PropertyDTO> getPropertyDTOs(List<TaskParam> props) {

        return props.stream().map(p -> getPropertyDTO(p.getItemName(), p.getItemValue()))
                .collect(Collectors.toList());
    }

    private PropertyDTO getPropertyDTO(String key, String value) {

        PropertyDTO prop = new PropertyDTO();
        prop.setKey(key);
        prop.setValue(value);
        return prop;
    }

    private TStatus[] getRequiredTStatuses(List<String> status) {

        List<String> allStatuses = Arrays.asList(WorkflowEngineConstants.TaskStatus.RESERVED.toString(),
                WorkflowEngineConstants.TaskStatus.READY.toString(),
                WorkflowEngineConstants.TaskStatus.COMPLETED.toString());
        TStatus[] tStatuses = getTStatus(allStatuses);

        if (CollectionUtils.isNotEmpty(status)) {
            List<String> requestedStatus = status.stream().filter(allStatuses::contains).collect
                    (Collectors.toList());
            if (CollectionUtils.isNotEmpty(requestedStatus)) {
                tStatuses = getTStatus(requestedStatus);
            }
        }
        return tStatuses;
    }

    private TStatus[] getTStatus(List<String> statuses) {

        return statuses.stream().map(this::getTStatus).toArray(TStatus[]::new);
    }

    private TStatus getTStatus(String status) {

        TStatus tStatus = new TStatus();
        tStatus.setTStatus(status);
        return tStatus;
    }
}
