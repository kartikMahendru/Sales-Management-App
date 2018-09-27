package project.avishkar.salesmanagement.Chat;

/**
 * Created by user on 9/24/18.
 */

public class BaseMessage {
    String message, senderName, msgRole, createdAt;

    public String getMsgRole() {
        return msgRole;
    }

    public void setMsgRole(String msgRole) {
        this.msgRole = msgRole;
    }

    public BaseMessage(){

    }
    public BaseMessage(String message, String createdAt, String senderName, String msgRole) {
        this.message = message;
        this.senderName = senderName;
        this.createdAt = createdAt;
        this.msgRole = msgRole;

    }

    public BaseMessage(String message, String createdAt){
        this.message = message;
        this.createdAt = createdAt;
        this.senderName = "current";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
