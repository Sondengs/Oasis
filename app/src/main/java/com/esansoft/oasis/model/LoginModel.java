package com.esansoft.oasis.model;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginModel implements Serializable {
    private static final long serialVersionUID = 1258235124358454790L;

    public ArrayList<EmployeeVO> Data;
    public int Total;
    public String AggregateResults;
    public String Errors;
}
