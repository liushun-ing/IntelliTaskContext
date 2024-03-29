package com.example.contextcodepredict.plugin.listener;

import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import org.jetbrains.annotations.NotNull;

/**
 * 编辑器鼠标事件
 */
public class MyEditorMouseListener implements EditorMouseListener {
  @Override
  public void mouseEntered(@NotNull EditorMouseEvent event) {
    EditorMouseListener.super.mouseEntered(event);
    MarkFlag.isMouseInEditor = 1;
  }

  @Override
  public void mouseExited(@NotNull EditorMouseEvent event) {
    EditorMouseListener.super.mouseExited(event);
    MarkFlag.isMouseInEditor = 0;
  }
}
