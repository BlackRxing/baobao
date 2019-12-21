package com.example.baoxiaojianapp.Utils;

import com.tencent.mm.opensdk.modelmsg.SendAuth;

public class NetInterface {
    public static final String ReleaseEnvironment="https://dev2.turingsenseai.com";
    public static final String DebugEnvironment="https://dev2.turingsenseai.com";
    public static final String TSloginSMSRequest="/account/sendSMS";
    public static final String TSloginRequest="/account/login";
    public static final String TSAppraisalPageReques="/appraisalIndex";
    public static final String TSCategoryPageRequest="/secondClass";
    public static final String TSPersonCenterPageRequest="/account/userCenter";
    public static final String TSPageBackwardRequest="/account/pageDown";
    public static final String TSSetUserInfoRequest="/account/setUserInfo";
    public static final String TSUserAdviceRequest="/account/userAdvice";
    public static final String TSAppraisalProcessV2Request="/appraisalProcessV2";
    public static final String TSSingleAppraisalRequestV2="/appraisal/appraisalSerial";
    public static final String TSAppraisalResultV4Request="/appraisal/genAppraisalResultV3";
    public static final String TSDailyPunchRequest="/account/sign";
    public static final String TSRenewalTokenRequest="/account/renewalToken";
    public static final String TSThirdPartyloginRequest="/account/check3rdStatus";
    public static final String TSThirdPartyBindPhoneRequest="/account/bind";
}
