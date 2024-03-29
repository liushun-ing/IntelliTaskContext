package com.example.contextcodepredict.data;

import java.util.Date;

/**
 * 上下文任务数据体
 * code context task data entity
 */
public class ContextTaskData {
  /**
   * 捕获时间
   * capture time
   */
  Date captureTime;
  /**
   * 捕获元素
   * the element captured
   */
  Object captureElement;

  public ContextTaskData() {
    this.captureTime = new Date();
    this.captureElement = "rootOfTree";
  }

  public ContextTaskData(Object captureElement) {
    this.captureTime = new Date();
    this.captureElement = captureElement;
  }

  public ContextTaskData(Date captureTime, Object captureElement) {
    this.captureTime = captureTime;
    this.captureElement = captureElement;
  }

  public Date getCaptureTime() {
    return captureTime;
  }

  public void setCaptureTime(Date captureTime) {
    this.captureTime = captureTime;
  }

  public Object getCaptureElement() {
    return captureElement;
  }

  public void setCaptureElement(Object captureElement) {
    this.captureElement = captureElement;
  }

  @Override
  public String toString() {
    return "ContextTaskData{" +
        "captureTime=" + captureTime +
        ", captureElement=" + captureElement +
        '}';
  }
}
