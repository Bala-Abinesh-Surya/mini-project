package com.surya.miniproject.models;

public class Request {
    // attributes of a request
    private String requestSender;
    private String requestReceiver;
    private String requestType;
    private String className;
    private String requestedDate;
    private boolean accepted;
    private String requestPushId;

    // Constructor
    public Request(String requestSender, String requestReceiver, String requestType, String className, String requestedDate, boolean accepted) {
        this.requestSender = requestSender;
        this.requestReceiver = requestReceiver;
        this.requestType = requestType;
        this.className = className;
        this.requestedDate = requestedDate;
        this.accepted = accepted;
    }

    // getter and setter methods
    public String getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(String requestSender) {
        this.requestSender = requestSender;
    }

    public String getRequestReceiver() {
        return requestReceiver;
    }

    public void setRequestReceiver(String requestReceiver) {
        this.requestReceiver = requestReceiver;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getRequestPushId() {
        return requestPushId;
    }

    public void setRequestPushId(String requestPushId) {
        this.requestPushId = requestPushId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
