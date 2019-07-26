package com.esansoft.oasis.model;

import com.esansoft.oasis.value_object.CommuteVO;

import java.io.Serializable;
import java.util.ArrayList;

public class ATD_INPUT_Model implements Serializable {

    public ArrayList<CommuteVO> Data;
    public int Total;
    public String AggregateResults;
    public String Errors;
}
