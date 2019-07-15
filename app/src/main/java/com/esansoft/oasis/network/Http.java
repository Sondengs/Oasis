package com.esansoft.oasis.network;

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
         * @param host
         * @param GUBUN
         * @param CDO_ID
         * @param CDO_02
         * @param CDO_04
         * @param CDO_12
         * @param CDO_17
         * @param CDO_18
         * @param CDO_19
         * @param CDO_20
         * @param CDO_23
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

        @FormUrlEncoded
        @POST(BaseConst.URL_ATDVIEW)
        Call<ATD_LIST_Model> getWorkStateData(
                @Path(value = "host", encoded = true) String host,
                @Field(value = "GUBUN") String GUBUN,
                @Field(value = "LED_ID") String CDO_ID,
                @Field(value = "LED_03") String CDO_12,
                @Field(value = "LED_04") String CDO_20
        );
    }
}