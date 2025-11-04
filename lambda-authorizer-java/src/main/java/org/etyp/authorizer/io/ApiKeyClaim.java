package org.etyp.authorizer.io;

public class ApiKeyClaim {

    private String userId = "NULL";
    private String email = "NULL";
    private String expire = "NULL";

    public ApiKeyClaim() {
    }

    public ApiKeyClaim(String userId, String email, String expire) {
        this.userId = userId;
        this.email = email;
        this.expire = expire;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", expire='" + expire + '\'' +
                '}';
    }
}
