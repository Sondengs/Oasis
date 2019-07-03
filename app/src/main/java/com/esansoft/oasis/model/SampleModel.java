package com.esansoft.oasis.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SampleModel implements Serializable {
    public ArrayList<EmployeeModel> Data;
    public int Total;
    public String AggregateResults;
    public String Errors;
}
