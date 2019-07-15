package com.esansoft.oasis.value_object;

import java.io.Serializable;

public class WorkStateVO implements Serializable {
    private static final long serialVersionUID = 4635771219693349629L;

    public String LED_ID;
    public String LED_01;
    public String LED_02;
    public String LED_03;       // 날짜
    public String LED_03_NM;    // 요일
    public String LED_04;
    public String LED_04_NM;    // 이름
    public String LED_07;       // 출근
    public String LED_08;       // 퇴근
    public String STAT;         // 상태
    public String WORKTIME;     // 근무시간
}
