package com.caliber.shwaasdemo.Utils;

/**
 * Created by Caliber on 20-03-2018.
 */

public class URLs {
   private static final String ROOT_URL = "http://shwaascloudsolution-env.us-east-2.elasticbeanstalk.com";
   // private static final String ROOT_URL = "http://192.168.1.20:5001";

    public static final String URL_LOGIN = ROOT_URL + "/staff/email/";
    public static final String URL_FORGOTPASSWORD = ROOT_URL + "/staff/password?email=";

    public static final String URL_MULTI_REPORT = ROOT_URL + "/multi/report/";
    public static final String URL_MONO_REPORT = ROOT_URL + "/mono/report/";
}
