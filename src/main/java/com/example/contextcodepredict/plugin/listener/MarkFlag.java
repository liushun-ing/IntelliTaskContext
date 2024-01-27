package com.example.contextcodepredict.plugin.listener;

/**
 * 捕获元素所用到的一些标志
 */
public class MarkFlag {
  /**
   * 鼠标是否在编辑器中
   * 0 表示在structure中
   * 1 表示在编辑器中
   * 2 表示在toolWindow中
   */
  public static int isMouseInEditor = 1;

  /**
   * 鼠标是否在窗口中
   */
  public static boolean isMouseInToolWindow = false;

  /**
   * toolWindow是否打开了
   */
  public static boolean isToolWindowActive = false;

  /**
   * 表格的状态，true表示加载预测中，false表示预测完成
   */
  public static int tableStatus = 0;

  /**
   * caret是否在编辑器中
   */
  public static boolean isPluginCaretActive() {
    return isMouseInEditor == 1;
  }

  /**
   * 鼠标是否在toolWindow中
   * @return
   */
  public static boolean isPluginCaretWindow() {
    return isMouseInEditor == 2;
  }

  public static boolean isWindowActive() {
    return isToolWindowActive;
  }

  /**
   * 文件编辑器是否活跃，isToolWindowActive && !isMouseInEditor
   *
   * @return 是否活跃
   */
  public static boolean isPluginEditorActive() {
    return isToolWindowActive && !isMouseInToolWindow;
  }
}
