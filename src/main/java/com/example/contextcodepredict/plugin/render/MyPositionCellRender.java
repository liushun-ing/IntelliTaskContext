package com.example.contextcodepredict.plugin.render;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyPositionCellRender extends DefaultTableCellRenderer {
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    System.out.println(value);
    System.out.println(value.toString());
    setToolTipText(value.toString());
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }
}
