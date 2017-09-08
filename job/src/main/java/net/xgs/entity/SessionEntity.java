package net.xgs.entity;

import net.xgs.model.BaseTaskPlanJob;
import net.xgs.model.ViewMachineParams;

/**
 * Created by duai on 2017-08-18.
 */
public class SessionEntity {
    private final BaseTaskPlanJob baseTaskPlanJob;
    private final String operation;
    private final ViewMachineParams viewMachineParams;

    public ViewMachineParams getViewMachineParams() {
        return viewMachineParams;
    }

    public BaseTaskPlanJob getBaseTaskPlanJob() {
        return baseTaskPlanJob;
    }


    public String getOperation() {
        return operation;
    }


    public SessionEntity(BaseTaskPlanJob baseTaskPlanJob, String operation, ViewMachineParams viewMachineParams) {
        this.baseTaskPlanJob = baseTaskPlanJob;
        this.operation = operation;
        this.viewMachineParams = viewMachineParams;
    }
}
