package com.example.contextcodepredict.data;

/**
 * 数据常量
 * Data constant
 */
public class Constants {
  /**
   * 表格数据的head
   * head of table data
   */
  public static String[] HEAD = {"Element", "Location", "Stereotype", "Confidence"};
  /**
   * 事件间隔数组
   * Time Window array
   */
  public static int[] TIME_INTERVAL_ENUM = {10, 30, 60, 300, 600, 1800};
  /**
   * 处理超时时间
   * process timeout
   */
  public static long TIME_OUT = 5000L;
  /**
   * 超时提示的文案
   * text of process timeout tip
   */
  public static String TIME_OUT_TIP = "Time Out! Please adjust the Prediction Step or reduce Task_Context.";
  /**
   * 超时提示的标题
   * title of process timeout tip
   */
  public static String TIME_OUT_TITLE = "Warning";
}
