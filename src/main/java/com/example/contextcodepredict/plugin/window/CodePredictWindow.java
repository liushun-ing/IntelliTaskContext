package com.example.contextcodepredict.plugin.window;

import com.example.contextcodepredict.data.Constants;
import com.example.contextcodepredict.data.ContextTaskData;
import com.example.contextcodepredict.data.DataCenter;
import com.example.contextcodepredict.data.SuggestionData;
import com.example.contextcodepredict.operation.TableDataOperator;
import com.example.contextcodepredict.operation.TreeDataOperator;
import com.example.contextcodepredict.plugin.listener.MarkFlag;
import com.example.contextcodepredict.plugin.render.MyPositionCellRender;
import com.example.contextcodepredict.plugin.render.MyTableCellRender;
import com.example.contextcodepredict.plugin.render.MyTreeNodeRenderer;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiElement;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;

public class CodePredictWindow {
  private JPanel CodePredict;
  private JComboBox TimeInterval;
  private JComboBox PredictionStep;
  private JTree ContextTree;
  private JTable SuggestionTable;
  private JLabel PredictSuggestion;
  private JButton ResetButton;
  private JButton RemoveButton;
  private JButton RemoveAllButton;
  private JButton ClearButton;
  private JLabel TaskContext;
  private JButton PredictButton;
  private JPanel RightPanel;
  private JPanel LeftPanel;

  private static final Icon loadingIcon = IconLoader.getIcon("/img/loading.svg", CodePredictWindow.class);
  private static final Icon tipIcon = IconLoader.getIcon("/img/intentionBulbGrey.svg", CodePredictWindow.class);
  private static final Icon errorIcon = IconLoader.getIcon("/img/error.svg", CodePredictWindow.class);
  private void init() {
    // set tip icon
    TaskContext.setIcon(tipIcon);
    PredictSuggestion.setIcon(tipIcon);

    SuggestionTable.setModel(DataCenter.TABLE_MODEL);
    SuggestionTable.setRowSelectionAllowed(true);
    SuggestionTable.setFocusable(false);
    SuggestionTable.setDragEnabled(false);
    TableColumn column = null;
    for (int i = 0; i < 4; i++) {
      column = SuggestionTable.getColumnModel().getColumn(i);
      if (i == 0) {
        column.setPreferredWidth(300);
        column.setMinWidth(300);
        column.setCellRenderer(new MyTableCellRender());
      } else if (i == 1) {
        column.setPreferredWidth(200);
        column.setMinWidth(150);
        column.setCellRenderer(new MyPositionCellRender());
      } else if (i == 2) {
        column.setPreferredWidth(180);
        column.setMinWidth(130);
      } else {
        column.setPreferredWidth(90);
        column.setMinWidth(90);
      }
    }

    ContextTree.setRootVisible(false);
    ContextTree.setModel(DataCenter.TREE_MODEL);
    ContextTree.setCellRenderer(new MyTreeNodeRenderer());
    ContextTree.setExpandsSelectedPaths(true);
  }

  public CodePredictWindow(Project project, ToolWindow toolWindow) {
    init();

    ContextTree.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        TreePath selectionPath = ContextTree.getSelectionPath();
        if (selectionPath == null) {
          return;
        }
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        ContextTaskData userObject = (ContextTaskData) lastPathComponent.getUserObject();
        // 导航定位元素
        NavigationUtil.activateFileWithPsiElement((PsiElement) userObject.getCaptureElement());
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {
        MarkFlag.isMouseInEditor = 2;
        // 防止点击改变caret也进行新的捕捉和预测
        MarkFlag.isMouseInToolWindow = true;
      }

      @Override
      public void mouseExited(MouseEvent e) {
        MarkFlag.isMouseInEditor = 0;
        MarkFlag.isMouseInToolWindow = false;
      }
    });

    SuggestionTable.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int i = SuggestionTable.rowAtPoint(e.getPoint());
        SuggestionData suggestionData = DataCenter.SUGGESTION_LIST.get(i);
        PsiElement element = suggestionData.getElement();
        NavigationUtil.activateFileWithPsiElement(element);
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {
        MarkFlag.isMouseInEditor = 2;
        MarkFlag.isMouseInToolWindow = true;
      }

      @Override
      public void mouseExited(MouseEvent e) {
        MarkFlag.isMouseInEditor = 0;
        MarkFlag.isMouseInToolWindow = false;
      }
    });

    SuggestionTable.getModel().addTableModelListener(
        new TableModelListener() {
          @Override
          public void tableChanged(TableModelEvent e) {
            if (MarkFlag.tableStatus == 1) {
              setLoadingTableText();
            } else if (MarkFlag.tableStatus == 0){
              setSuccessTableText();
            } else if (MarkFlag.tableStatus == -1) {
              setErrorTableText();
            }
          }
        });

    // 事件间隔的选择事件
    TimeInterval.addActionListener(e -> DataCenter.TIME_INTERVAL = Constants.TIME_INTERVAL_ENUM[TimeInterval.getSelectedIndex()]);

    // 预测步长的选择事件
    PredictionStep.addActionListener(e -> DataCenter.PREDICTION_STEP = PredictionStep.getSelectedIndex() + 1);

    ResetButton.addActionListener(e -> {
      TimeInterval.setSelectedIndex(0);
      PredictionStep.setSelectedIndex(0);
      DataCenter.TIME_INTERVAL = Constants.TIME_INTERVAL_ENUM[TimeInterval.getSelectedIndex()];
      DataCenter.PREDICTION_STEP = PredictionStep.getSelectedIndex() + 1;
    });

    ClearButton.addActionListener(e -> {
      TableDataOperator.clearSuggestionList();
      TableDataOperator.updateTableModel();
    });

    RemoveAllButton.addActionListener(e -> TreeDataOperator.removeAll());

    RemoveButton.addActionListener(e -> {
      TreePath selectionPath = ContextTree.getSelectionPath();
      if (selectionPath == null) {
        return;
      }
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
      TreeDataOperator.removeOne(node);
    });

    PredictButton.addActionListener(e -> TableDataOperator.executePrediction());


  }

  /**
   * 获取代码窗口的主panel
   *
   * @return panel
   */
  public JPanel getCodePredict() {
    return CodePredict;
  }

  public void setLoadingTableText() {
    PredictSuggestion.setIcon(loadingIcon);
    PredictSuggestion.setText("Predicting......");
  }

  public void setSuccessTableText() {
    PredictSuggestion.setIcon(tipIcon);
    PredictSuggestion.setText("Suggestions");
  }

  public void setErrorTableText() {
    PredictSuggestion.setIcon(errorIcon);
    PredictSuggestion.setText("Error occurred, please retry!");
  }
}
