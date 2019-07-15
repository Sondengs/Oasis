package com.esansoft.oasis.model;

import com.esansoft.oasis.value_object.WorkStateVO;

import java.io.Serializable;
import java.util.ArrayList;

public class ATD_LIST_Model implements Serializable {
    private static final long serialVersionUID = 3489758610344907488L;

    public ArrayList<WorkStateVO> Data;
    public int Total;
    public String AggregateResults;
    public String Errors;
}
