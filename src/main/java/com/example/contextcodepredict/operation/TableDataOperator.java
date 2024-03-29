package com.example.contextcodepredict.operation;

import com.example.contextcodepredict.data.DataCenter;
import com.example.contextcodepredict.data.SuggestionData;
import com.example.contextcodepredict.plugin.listener.MarkFlag;
import com.example.contextcodepredict.vf3.algorithm.MainEntry;
import com.example.contextcodepredict.vf3.algorithm.MatchCouple;
import com.example.contextcodepredict.vf3.algorithm.Solution;
import com.example.contextcodepredict.vf3.graph.Graph;
import com.example.contextcodepredict.vf3.graph.Vertex;
import com.intellij.psi.PsiElement;

import java.util.*;

/**
 * 表格数据操作类,包括预测逻辑
 */
public class TableDataOperator {

  /**
   * 设置table加载状态
   */
  public static void setLoadingTable() {
    MarkFlag.tableStatus = 1;
  }

  /**
   * 设置table加载好了的状态
   */
  public static void setSuccessTable() {
    MarkFlag.tableStatus = 0;
  }

  /**
   * 设置table数据出错啦状态
   */
  public static void setErrorTable() {
    MarkFlag.tableStatus = -1;
  }

  /**
   * 根据suggestionData转换为table展示的数据类型
   *
   * @param suggestionData 需要转换的对象
   * @return 转换后的table行对象
   */
  public static Object[] convertSuggestionData(SuggestionData suggestionData) {
    Object[] raw = new Object[4];
    raw[0] = suggestionData.getElement();
    raw[1] = suggestionData.getPackagePath();
    raw[2] = suggestionData.getStereotype();
    raw[3] = suggestionData.getConfidence();
    return raw;
  }

  /**
   * 过滤TimeInterval内的建议数据项
   */
  public static void filterSuggestionDataInTimeInterval() {
    for (int i = 0; i < DataCenter.SUGGESTION_LIST.size(); i++) {
      SuggestionData suggestionData = DataCenter.SUGGESTION_LIST.get(i);
      long time = suggestionData.getGenerateTime().getTime();
      long nowTime = new Date().getTime();
      if (time < (nowTime - DataCenter.TIME_INTERVAL * 1000L)) {
        DataCenter.SUGGESTION_LIST.remove(i);
        i--;
      }
    }
  }

  /**
   * 判断是否建议列表中已经存在该元素，如果存在，则更新置信值，取最大值
   *
   * @param psiElement    目标元素
   * @param newConfidence 新的置信值
   * @return 是否存在
   */
  public static boolean existInSuggestionList(PsiElement psiElement, String newConfidence) {
    for (SuggestionData s : DataCenter.SUGGESTION_LIST) {
      if (s.getElement().equals(psiElement)) {
        if (Double.parseDouble(newConfidence) > Double.parseDouble(s.getConfidence())) {
          s.setConfidence(newConfidence);
        }
        return true;
      }
    }
    return false;
  }

  /**
   * 更新SUGGESTION_LIST，直接替换为新的列表
   *
   * @param list 新的列表项
   */
  public static void updateSuggestionList(List<SuggestionData> list) {
    DataCenter.SUGGESTION_LIST.clear();
    DataCenter.SUGGESTION_LIST.addAll(list);
  }

  /**
   * 清空建议列表
   */
  public static void clearSuggestionList() {
    DataCenter.SUGGESTION_LIST.clear();
  }

  /**
   * 更新表格模型，将SUGGESTION_LIST的数据转换为table数据，并更新表格
   */
  public static void updateTableModel() {
    int c = DataCenter.TABLE_MODEL.getRowCount();
    for (int i = c - 1; i >= 0; i--) {
      DataCenter.TABLE_MODEL.removeRow(i);
    }
    // 对数据根据置信值进行排序
    DataCenter.SUGGESTION_LIST.sort(Comparator.comparing(SuggestionData::getConfidence, Comparator.reverseOrder()));
    for (int i = 0; i < DataCenter.SUGGESTION_LIST.size(); i++) {
      DataCenter.TABLE_MODEL.addRow(TableDataOperator.convertSuggestionData(DataCenter.SUGGESTION_LIST.get(i)));
    }
  }

  /**
   * 执行field元素的代码预测，包括构建目标图，分配原型，VF3子图匹配，计算置信值，更新table列表
   */
  public static void executePrediction() {
    try {
      System.out.println("psiElement prediction start");
      setLoadingTable();
      clearSuggestionList(); // 执行前先清理一次
      updateTableModel();
      commonExecute(TargetGraphOperator.buildTargetGraph());
      // 在 table 数据改变之前将标志复位
      setSuccessTable();
      // 更新tableModel
      updateTableModel();
    } catch (Exception e) {
      setErrorTable();
      clearSuggestionList();
      updateTableModel();
      e.printStackTrace();
      System.out.println("Error occurred" + e.getMessage());
    } finally {
      System.out.println("psiElement prediction complete");
    }

  }

  /**
   * 相同的执行逻辑，根据目标图执行一系列预测操作
   *
   * @param targetGraph 目标图
   */
  public static void commonExecute(Graph targetGraph) {
    if (targetGraph == null) {
      return;
    }
    // 分配原型
    TargetGraphOperator.assignStereotypeRole(targetGraph);
    // 分配原型之后需要重新统计图的节点类型
    targetGraph.countLabelQuantity();
    // 执行VF3子图匹配算法
    MainEntry mainEntry = new MainEntry();
    ArrayList<ArrayList<Solution>> executeResult = mainEntry.execute(targetGraph);
    // 计算置信度
    HashMap<Integer, Double> confidenceMap = calculateConfidence(executeResult, targetGraph);
    // 更新SUGGESTION_LIST,需要过滤掉时间过期的 不用过滤了
//    filterSuggestionDataInTimeInterval();
    // 数据转换
    ArrayList<SuggestionData> newSuggestionDataList = convertSuggestionList(confidenceMap, targetGraph);
    // 将新得到的节点加入到SUGGESTION_LIST中
    updateSuggestionList(newSuggestionDataList);
  }

  /**
   * 根据置信值和目标图，将匹配到的节点，转换为table数据
   *
   * @param confidenceMap 置信度映射map
   * @param targetGraph   目标图
   * @return table数据列表
   */
  public static ArrayList<SuggestionData> convertSuggestionList(HashMap<Integer, Double> confidenceMap, Graph targetGraph) {
    ArrayList<SuggestionData> suggestionList = new ArrayList<>();
    for (Integer i : confidenceMap.keySet()) {
      Vertex vertexById = targetGraph.getVertexById(i);
      if (confidenceMap.get(i) > 0) {
        String s = confidenceMap.get(i).toString();
        s = s.length() > 6 ? s.substring(0, 6) : s;
        suggestionList.add(new SuggestionData(vertexById.getPsiElement(), vertexById.getLabel(), s));
      }
    }
    return suggestionList;
  }

  /**
   * 计算置信度
   *
   * @param executeResult VF3子图匹配结果
   * @param targetGraph   目标图
   * @return 返回节点和对应的置信度的map
   */
  public static HashMap<Integer, Double> calculateConfidence(ArrayList<ArrayList<Solution>> executeResult, Graph targetGraph) {
    ArrayList<Vertex> vertices = targetGraph.getVertices();
    HashMap<Integer, Double> confidenceMap = new HashMap<>();
    // 先初始化
    for (Vertex v : vertices) {
      confidenceMap.put(v.getId(), 0.0);
    }
    // 统计所有匹配子图数目
    Double occurrenceSum = 0.0;
    // 不同的模式图
    for (ArrayList<Solution> solutions : executeResult) {
      // 不同的解决方案
      for (Solution solution : solutions) {
        occurrenceSum++;
        // 映射节点对
        for (MatchCouple mc : solution.getSolution()) {
          Vertex targetV = mc.getV(); // 拿到目标图映射的节点
          if (confidenceMap.containsKey(targetV.getId())) {
            confidenceMap.replace(targetV.getId(), confidenceMap.get(targetV.getId()) + 1);
          }
        }
      }
    }
    if (occurrenceSum != 0) {
      // 计算比例
      for (Integer i : confidenceMap.keySet()) {
        confidenceMap.replace(i, confidenceMap.get(i) / occurrenceSum);
      }
    }
    return confidenceMap;
  }
}