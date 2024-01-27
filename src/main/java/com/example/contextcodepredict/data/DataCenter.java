package com.example.contextcodepredict.data;

import com.example.contextcodepredict.myjstereocode.entry.StereotypeAssigner;
import com.example.contextcodepredict.myjstereocode.info.ProjectInformation;
import com.example.contextcodepredict.plugin.model.MyTableModel;
import com.intellij.openapi.project.Project;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据中心
 * Data center
 */
public class DataCenter {
  /**
   * 当前打开的项目对象
   * current project opened
   */
  public static Project PROJECT;

  /**
   * 项目信息
   * project information
   */
  public static ProjectInformation PROJECT_INFORMATION;

  /**
   * 原型分配器
   * the stereotype assigner object
   */
  public static StereotypeAssigner STEREOTYPE_ASSIGNER;

  /**
   * 时间间隔
   * Time Window chosen
   */
  public static int TIME_INTERVAL = Constants.TIME_INTERVAL_ENUM[0];

  /**
   * 预测步长
   * prediction step chosen
   */
  public static int PREDICTION_STEP = 1;

  /**
   * 预测的建议数据对象,中转数据
   * own suggestion table data
   */
  public static List<SuggestionData> SUGGESTION_LIST = new ArrayList<>();

  /**
   * 表格模型，用于ToolWindow右侧的列表展示
   * table model for suggestion view
   */
  public static MyTableModel TABLE_MODEL = new MyTableModel(null, Constants.HEAD);

  /**
   * 树模型，用于ToolWindow的左侧树结构展示
   * tree model for task context view
   */
  public static DefaultTreeModel TREE_MODEL = new DefaultTreeModel(new DefaultMutableTreeNode(new ContextTaskData(), true));

  /**
   * 记录游标开始位置，解决selection事件触发时机不对的 idea bug，
   * 但是又有新的bug: 如果重复选择同样的位置，将不会出发元素捕获
   * caret last start position in the editor
   */
  public static int LAST_START = 0;

  /**
   * 记录游标结束位置
   * caret last end position in the editor
   */
  public static int LAST_END = 0;

  /**
   * 重置插件数据结构
   * reset plugin data structure
   *
   * @param project 当前项目对象 current project object
   */
  public static void reset(Project project) {
    LAST_START = 0;
    LAST_END = 0;
    PROJECT = project;
    PROJECT_INFORMATION = new ProjectInformation(project);
    STEREOTYPE_ASSIGNER = new StereotypeAssigner();
    STEREOTYPE_ASSIGNER.setParameters(PROJECT_INFORMATION.getMethodsMean(), PROJECT_INFORMATION.getMethodsStdDev());
    TIME_INTERVAL = 5;
    PREDICTION_STEP = 1;
    SUGGESTION_LIST.clear();
    // 不能直接重置vector
    // cannot reset vector, must iterate row
    int c = DataCenter.TABLE_MODEL.getRowCount();
    for (int i = c - 1; i >= 0; i--) {
      DataCenter.TABLE_MODEL.removeRow(i);
    }
    TREE_MODEL.setRoot(new DefaultMutableTreeNode(new ContextTaskData(), true));
    TREE_MODEL.reload();
  }

}