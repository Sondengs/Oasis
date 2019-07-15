package com.esansoft.oasis.value_object;

public class WorkPlaceVO {
    public String WorkPlaceName;
    public String Manager;
    public String Address;
    public String Phone;

    public WorkPlaceVO(String workPlaceName, String manager, String address, String phone) {
        WorkPlaceName = workPlaceName;
        Manager = manager;
        Address = address;
        Phone = phone;
    }
}
