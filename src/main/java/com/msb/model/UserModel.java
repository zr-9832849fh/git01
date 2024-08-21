package com.msb.model;

// UserModel 封装类（在做用户登录时，因为要返回加密后的id，就封装了这个）
public class UserModel {
//    private Integer userId;
    private String userIdStr;   // 加密后的id
    private String userName;
    private String trueName;  // 真实名字


    public UserModel() {
    }

    public UserModel(String userIdStr, String userName, String trueName) {
        this.userIdStr = userIdStr;
        this.userName = userName;
        this.trueName = trueName;
    }

    /**
     * 获取
     * @return userIdStr
     */
    public String getUserIdStr() {
        return userIdStr;
    }

    /**
     * 设置
     * @param userIdStr
     */
    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    /**
     * 获取
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     * @return trueName
     */
    public String getTrueName() {
        return trueName;
    }

    /**
     * 设置
     * @param trueName
     */
    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String toString() {
        return "UserModel{userIdStr = " + userIdStr + ", userName = " + userName + ", trueName = " + trueName + "}";
    }
}
