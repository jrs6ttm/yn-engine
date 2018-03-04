package com.sso.redis.bean;

import java.io.Serializable;

public class SensorThreshold implements Serializable{
    /**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	Long sensorId;
    Long detectParamId;
    Integer valType;
    String regularList;
    Double minVal;
    Double maxVal;
    Long mayJob;
    Long existJob;

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Long getDetectParamId() {
        return detectParamId;
    }

    public void setDetectParamId(Long detectParamId) {
        this.detectParamId = detectParamId;
    }

    public Integer getValType() {
        return valType;
    }

    public void setValType(Integer valType) {
        this.valType = valType;
    }

    public String getRegularList() {
        return regularList;
    }

    public void setRegularList(String regularList) {
        this.regularList = regularList;
    }

    public Double getMinVal() {
        return minVal;
    }

    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }

    public Double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }

    public Long getMayJob() {
        return mayJob;
    }

    public void setMayJob(Long mayJob) {
        this.mayJob = mayJob;
    }

    public Long getExistJob() {
        return existJob;
    }

    public void setExistJob(Long existJob) {
        this.existJob = existJob;
    }
}
