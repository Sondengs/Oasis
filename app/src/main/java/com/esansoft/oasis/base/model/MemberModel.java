package com.esansoft.oasis.base.model;


import java.io.Serializable;
import java.util.ArrayList;

public class MemberModel implements Serializable {

    public Boolean IsSuccess;
    public Data Data;
    public String ErrorMsg;
    public String Token;

    public class Data implements Serializable {

        public UserInfo UserInfo;
        public ArrayList<Quest> QuestList;
        public ChallengeState ChallengeState;
        public SettingInfo SettingInfo;
        public ArrayList<ChallengeResult> ChallengeResult;
        public PromotionInfo PromotionInfo;

    }

    public class UserInfo implements Serializable {

        public String UID;
        public String Email;
        public String Name;
        public String NickName;
        public String Pwd;
        public String ChangePWD;
        public String IsTempPWD;
        public String ProfileImage;
        public String Image;
        public String Promise;
        public String Gender;
        public String Age;
        public String BirthDay;
        public String Height;
        public String Weight;
        public String TelHp;
        public String Type;
        public String SMM;
        public String PBF;
        public String PBF_MIN;
        public String PBF_MAX;
        public String BMR;
        public String Level;
        public String Key;
        public String EXP;
        public String Ranking;
        public String RankingChange;
        public String Steps;
        public String StepsGoal;
        public String StepsRanking;
        public String SleepTime;
        public String Year;
        public String Month;
        public String Day;
        public String Time;
        public String ExeKcal;
        public String ExeKcalGoal;
        public String FoodKcal;
        public String FoodKcalGoal;
        public String NoticeCount;
        public String BodyFatLost;
        public String BodyFatLostGoal;
        public String BodyFatLostRanking;
        public String InBodyCount;
        public String ActCount;
        public String CaloriesBurnt;
        public String TutorialState;
        public String Country;
        public String MasterCode;
        public String ABONo;
        public String WinCount;
        public String IsNewUser;
        public String ModifyKind;
        public String IsLikeUser;
        public String WT_Change;
        public String WT_Ranking;
        public String SMM_Change;
        public String SMM_Ranking;
        public String ExpireDate;
        public String ConversionDate;
        public String State;
        public String TimeZone;
        public String IsNewMission;
        public String IsSurvey;
        public String MentoringCount;
        public String MentoringState;
        public String MentoringMessage;

    }

    public class ChallengeState implements Serializable {

        public String SingleChallengeState;
        public String TeamChallengeState;
        public String CommunityState;
        public String OnetoOneState;
        public String GrouptoGroupState;

    }

    public class SettingInfo implements Serializable {

        public String UID;
        public String Kind;
        public String Language;
        public String FoodDB;
        public String HeightUnit;
        public String WeightUnit;
        public String CalorieUnit;
        public String BMI_ST;
        public Boolean IsPrivacy;

    }

    public class Quest implements Serializable {

        public String UID;
        public String ChallengeID;
        public String QuestID;
        public String QuestName;
        public String QuestType;
        public String Description;
        public String AchieveRate;
        public String IconType;
        public Boolean IsAchieved;
        public String Rewards;

    }

    public class ChallengeResult implements Serializable {

    }

    public class PromotionInfo implements Serializable {

        public String MyKeyList;                    //
        public String PromotionID;                  // 프로모션 - 아이디
        public String PromotionTitle;               // 프로모션 - 타이틀
        public String PromotionState;               // OPEN: 프로모션 등록기간일 경우, ONGOING: 프로모션이 진행중, OFF: 프로모션 기간이 아닐 경우
        public String RegStartDate;
        public String RegEndDate;
        public String StartDate;                    // 프로모션 - 시작일
        public String EndDate;                      // 프로모션 - 종료일
        public Boolean IsSignUp;                    // 프로모션 - 가입여부
        public String GainKeyDay;                   // 프로모션 - 타이
        public String PromotionPeriod;              // 프로모션 - 진행 기간
        public String IsInstagramAuth;              // 인스타그램 인증여부
        public String TodayExeCode;                 // 챌린지 오늘의 운동(이지트레이닝에서 운동횟수 500회 넘을경우 팝업)
        public String LimitExeCount;                // 이지트레이닝 카운팅 제한 개수

    }


}
