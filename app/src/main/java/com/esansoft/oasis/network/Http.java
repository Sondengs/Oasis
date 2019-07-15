package com.esansoft.oasis.network;

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
    }

    //--------------------------------------------------
    // 회원 - 나만의 목표
    //--------------------------------------------------
    public static IATD myGoal(TYPE type) {
        return (IATD) retrofit(IATD.class, type);
    }

    public interface IATD {

    }
}