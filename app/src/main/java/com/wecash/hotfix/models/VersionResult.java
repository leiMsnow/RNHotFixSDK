package com.wecash.hotfix.models;

/**
 * Created by wecash on 2018/1/2.
 */

public class VersionResult {

    /**
     * remoteVersion : 1.0.0
     * remoteHotUpdateVersion : 1.0.0.0
     * remoteVersionUpdateDescription : Welcome to AILAW!
     * remoteCommand : notify
     * remoteHotUpdateCommand : none
     * remoteHotUpdateURL : http://172.12.10.234:8080/main.jsbundle
     * remoteConfigJSON : null
     * remoteVersionUpdateURL : https://www.baidu.com
     */
    public String remoteVersion;
    public String remoteHotUpdateVersion;
    public String remoteVersionUpdateDescription;
    public String remoteCommand;
    public String remoteHotUpdateCommand;
    public String remoteHotUpdateURL;
    public Object remoteConfigJSON;
    public String remoteVersionUpdateURL;

    @Override
    public String toString() {
        return "VersionResult{" +
                "remoteVersion='" + remoteVersion + '\'' +
                ", remoteHotUpdateVersion='" + remoteHotUpdateVersion + '\'' +
                ", remoteVersionUpdateDescription='" + remoteVersionUpdateDescription + '\'' +
                ", remoteCommand='" + remoteCommand + '\'' +
                ", remoteHotUpdateCommand='" + remoteHotUpdateCommand + '\'' +
                ", remoteHotUpdateURL='" + remoteHotUpdateURL + '\'' +
                ", remoteConfigJSON=" + remoteConfigJSON +
                ", remoteVersionUpdateURL='" + remoteVersionUpdateURL + '\'' +
                '}';
    }
}
