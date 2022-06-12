package com.surya.miniproject.models;

import static com.surya.miniproject.constants.Strings.REQUEST_ONE_TIME;

import java.util.Locale;

public class Request {
    // attributes of a request
    private String requestSender;
    private String requestReceiver;
    private String requestType;
    private String className;
    private String requestedDate;
    private boolean accepted;
    private String requestPushId;
    private boolean requestTimedOut;

    // Constructor
    public Request() {

    }

    public Request(String requestSender, String requestReceiver, String requestType, String className, String requestedDate, boolean accepted) {
        this.requestSender = requestSender;
        this.requestReceiver = requestReceiver;
        this.requestType = requestType;
        this.className = className;
        this.requestedDate = requestedDate;
        this.accepted = accepted;
    }

    public String requestWarnText(){
        return "* It's up to " + requestReceiver + " to accept the request or not";
    }

    // for the request list recycler view
    public String requestText(){
        return requestSender +
                " requesting you the access to edit the " + className + " attendance";
    }

    public String requestAcceptedText(){
        return "You gave access for " + requestSender + " to edit the " + className + " attendance";
    }

    // status message for the recipient
    public String requestStatusText(){
        if(isAccepted()){
            return "Your request has been accepted by " + requestReceiver;
        }
        else{
            return "Your request has not been accepted yet by " + requestReceiver;
        }
    }

    public String timelineText(){
        if(requestType.equals(REQUEST_ONE_TIME)){
            return "Timeline : Only One Time";
        }
        else{
            return "Timeline : Unlimited Access";
        }
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

    public boolean isRequestTimedOut() {
        return requestTimedOut;
    }

    public void setRequestTimedOut(boolean requestTimedOut) {
        this.requestTimedOut = requestTimedOut;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestSender='" + requestSender + '\'' +
                ", requestReceiver='" + requestReceiver + '\'' +
                ", requestType='" + requestType + '\'' +
                ", className='" + className + '\'' +
                ", requestedDate='" + requestedDate + '\'' +
                ", accepted=" + accepted +
                ", requestPushId='" + requestPushId + '\'' +
                ", requestTimedOut=" + requestTimedOut +
                '}';
    }
}
