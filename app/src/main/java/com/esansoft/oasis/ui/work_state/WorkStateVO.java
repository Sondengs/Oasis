package com.esansoft.oasis.ui.work_state;

public class WorkStateVO {
    public String UserPhoto;
    public String WorkerName;
    public String WorkType;
    public String WorkTime;
    public String WorkState;

    public WorkStateVO(String userPhoto, String workerName, String workType, String workTime, String workState) {
        UserPhoto = userPhoto;
        WorkerName = workerName;
        WorkType = workType;
        WorkTime = workTime;
        WorkState = workState;
    }
}
