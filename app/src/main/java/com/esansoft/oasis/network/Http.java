package com.esansoft.oasis.network;

import com.esansoft.oasis.model.ATD_INPUT_Model;
import com.esansoft.oasis.model.ATD_LIST_Model;
import com.esansoft.oasis.model.LoginModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class Http extends HttpBaseService {


    //--------------------------------------------------
    // 회원
    //--------------------------------------------------
    public static IEMP member(TYPE type) {
        return (IEMP) retrofit(IEMP.class, type);
    }

    public interface IEMP {

        /**
         * 로그인
         *
         * @param host
         * @param GUBUN
         * @param CDO_ID
         * @param CDO_12 비밀번호
         * @param CDO_20 이메일
         * @return
         */
        @FormUrlEncoded
        @POST(BaseConst.URL_EMPVIEW)
        Call<LoginModel> login(
                @Path(value = "host", encoded = true) String host,
                @Field(value = "GUBUN") String GUBUN,
                @Field(value = "CDO_ID") String CDO_ID,
                @Field(value = "CDO_12") String CDO_12,
                @Field(value = "CDO_20") String CDO_20
        );

        /**
         * 가입
         *
         * @param host
         * @param GUBUN
         * @param CDO_ID
         * @param CDO_02
         * @param CDO_04 성명
         * @param CDO_12 비밀번호
         * @param CDO_17
         * @param CDO_18
         * @param CDO_19
         * @param CDO_20 이메일
         * @param CDO_23 휴대폰 번호
         * @return
         */
        @FormUrlEncoded
        @POST(BaseConst.URL_EMPINPUT)
        Call<LoginModel> signUp(
                @Path(value = "host", encoded = true) String host,
                @Field(value = "GUBUN") String GUBUN,
                @Field(value = "CDO_ID") String CDO_ID,
                @Field(value = "CDO_02") String CDO_02,
                @Field(value = "CDO_04") String CDO_04,
                @Field(value = "CDO_12") String CDO_12,
                @Field(value = "CDO_17") String CDO_17,
                @Field(value = "CDO_18") String CDO_18,
                @Field(value = "CDO_19") String CDO_19,
                @Field(value = "CDO_20") String CDO_20,
                @Field(value = "CDO_23") String CDO_23
        );
    }

    //--------------------------------------------------
    // 회원 - 나만의 목표
    //--------------------------------------------------
    public static IATD work(TYPE type) {
        return (IATD) retrofit(IATD.class, type);
    }

    public interface IATD {
        /**
         *
         * @param host
         * @param GUBUN LIST
         * @param LED_ID
         * @param LED_03 날짜
         * @param LED_04
         * @return
         */
        @FormUrlEncoded
        @POST(BaseConst.URL_ATDVIEW)
        Call<ATD_LIST_Model> getWorkStateData(
                @Path(value = "host", encoded = true) String host,
                @Field(value = "GUBUN") String GUBUN,
                @Field(value = "LED_ID") String LED_ID,
                @Field(value = "LED_03") String LED_03,
                @Field(value = "LED_04") String LED_04
        );

        /**
         *
         * @param host
         * @param GUBUN WORK 출근 END 퇴근
         * @param LED_ID
         * @param LED_02
         * @param LED_03 근무일 yyyyMMdd
         * @param LED_04 사원코드
         * @param LED_07 출근시간 HHmm
         * @param LED_08 퇴근시간 HHmm
         * @return
         */
        @FormUrlEncoded
        @POST(BaseConst.URL_ATDINPUT)
        Call<ATD_INPUT_Model> setCommute(
                @Path(value = "host", encoded = true) String host,
                @Field(value = "GUBUN") String GUBUN,
                @Field(value = "LED_ID") String LED_ID, // ID
                @Field(value = "LED_02") String LED_02,
                @Field(value = "LED_03") String LED_03, // 근무일 yyyyMMdd
                @Field(value = "LED_04") String LED_04, // 사원코드
                @Field(value = "LED_07") String LED_07, // 출근시간 HHmm
                @Field(value = "LED_08") String LED_08  // 퇴근시간 HHmm
        );
    }
}