package com.arch.ihcd.gateway.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= TableName.IHCD_SMS_OUTBOX_TRANS)
public class SmsOutboxTrans {
    private String to;
    private String from;
    private String subject;
    private String message;
    private String messageType; //resetpasword/userreg/forgotpass
    private boolean smssent = false;
    private long msglastattempttime;
    private int attemtcount=0;
    private String username;

    //Record fields
    private String createdby;
    private long createddtm;
    private String modifiedby;
    private long modifieddtm;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getMsglastattempttime() {
        return msglastattempttime;
    }

    public void setMsglastattempttime(long msglastattempttime) {
        this.msglastattempttime = msglastattempttime;
    }

    public int getAttemtcount() {
        return attemtcount;
    }

    public void setAttemtcount(int attemtcount) {
        this.attemtcount = attemtcount;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public long getCreateddtm() {
        return createddtm;
    }

    public void setCreateddtm(long createddtm) {
        this.createddtm = createddtm;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public long getModifieddtm() {
        return modifieddtm;
    }

    public void setModifieddtm(long modifieddtm) {
        this.modifieddtm = modifieddtm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSmssent() {
        return smssent;
    }

    public void setSmssent(boolean smssent) {
        this.smssent = smssent;
    }

}
