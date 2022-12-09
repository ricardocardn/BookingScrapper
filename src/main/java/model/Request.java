package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Request {
    private Object objectType;
    private int id;
    private String requestInfo;
    private Map<String,String> queryParams;

    public Request() {
        queryParams = new HashMap<>();
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public void setObjectType(Object objectType) {
        this.objectType = objectType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public Object getObjectType() {
        return objectType;
    }

    public int getId() {
        return id;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public String toString() {
        return "Request{" +
                "objectType=" + objectType +
                ", id=" + id +
                ", requestInfo='" + requestInfo + '\'' +
                ", queryParams=" + queryParams +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return id == request.id && Objects.equals(objectType, request.objectType) && Objects.equals(requestInfo, request.requestInfo) && Objects.equals(queryParams, request.queryParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, id, requestInfo, queryParams);
    }
}
