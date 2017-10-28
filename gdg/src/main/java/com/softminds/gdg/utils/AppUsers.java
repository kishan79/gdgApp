package com.softminds.gdg.utils;


import com.google.firebase.auth.FirebaseAuth;

//This suppress is required because we will not use any setters for this class
// but we need them because fire base internally use these setters
@SuppressWarnings("unused")
public class AppUsers {

    public static final int ADMIN_ACCESS = 122;
    public static final int MEMBER_ACCESS = 120;
    public static final int PUBLIC_ACCESS = 121;

    //data-fields this will be used in a key value pairs in decomposition of this class
    private String name;
    private String email;
    private String authUid;
    private String picUrl;
    private String fcmToken;
    private int AccessLevel;
    private long MemberSince;
    private long LastSeen;



    public AppUsers(){
        //requires empty constructor for real time database
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public long getLastSeen() {
        return LastSeen;
    }

    public int getAccessLevel() {
        return AccessLevel;
    }

    public String getAuthUid() {
        return authUid;
    }

    public long getMemberSince() {
        return MemberSince;
    }

    public String getPicUrl() {
        return picUrl;
    }


    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemberSince(long memberSince) {
        MemberSince = memberSince;
    }

    public void setAccessLevel(int Access) {
        this.AccessLevel = Access;
    }

    public void setLastSeen(long lastSeen) {
        LastSeen = lastSeen;
    }

    /*
        We use this token to generate the unique user node under database
        this token will be unique for all users
        spaces are not allowed in emails but spaces can be used as a key so
        we replace all chars that does not support to be a qualified key in database
        with spaces and result is a unique key,
        we may use uid of auth as key but i have noticed it changing as a new account
        with old email again after deletion, so we stick to this method
          */
    @SuppressWarnings("ConstantConditions")
    public static String generateAppUserToken(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null
                && FirebaseAuth.getInstance().getCurrentUser().getEmail() !=null){

            return FirebaseAuth.getInstance().getCurrentUser().getEmail()
                    .replace('.', ' ')
                    .replace('@', ' ');
        }
        return null;
    }

    public static String generateAppUserTokenWithEmail(String Email) {
         if(Email.contains("@") && Email.contains("."))
            return Email.replace('.',' ').replace('@',' ');
        else
            return null;
    }
}
