package com.example.contextcodepredict.myjstereocode.storage;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;

import java.util.ArrayList;

/**
 * 存储最近的一些历史原型记录
 * the latest history storage
 */
public class LatestHistory {
  /**
   * 最近的100个原型记录，只统计类和方法
   * the latest 100 stereotyped element, only for class and method element
   */
  public static ArrayList<HistoryItem> LATEST_TYPES = new ArrayList<>();

  /**
   * 添加历史项
   * add history item
   *
   * @param psiElement 元素 psiElement
   * @param type 原型 stereotype
   */
  public static void addTypeItem(PsiElement psiElement, String type) {
    if (psiElement == null || psiElement instanceof PsiField) {
      return;
    }
    LATEST_TYPES.add(new HistoryItem(psiElement, type));
    if (LATEST_TYPES.size() > 100) {
      LATEST_TYPES.remove(0);
    }
  }

  /**
   * 获取历史项
   * get history item
   *
   * @param psiElement 元素 psiElement
   * @return 原型，没有则为空字符串 stereotype string, empty string for null
   */
  public static String getHistory(PsiElement psiElement) {
    if (psiElement == null) {
      return "";
    }
    for (HistoryItem latestType : LATEST_TYPES) {
      if (latestType.getPsiElement().equals(psiElement)) {
        return latestType.getStereotype();
      }
    }
    return "";
  }

}
