package com.esansoft.oasis.base.model;

import java.io.Serializable;


public class BaseModel implements Serializable {

    public Boolean IsSuccess;
    public String ErrorMsg;
    public Data Data;

    public class Data{
        public String Type;
        public String Message;
    }

}
