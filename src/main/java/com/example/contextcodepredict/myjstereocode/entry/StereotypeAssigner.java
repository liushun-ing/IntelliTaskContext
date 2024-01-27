package com.example.contextcodepredict.myjstereocode.entry;

import com.example.contextcodepredict.myjstereocode.stereotypedresult.StereotypedElement;
import com.example.contextcodepredict.myjstereocode.stereotypedresult.StereotypedField;
import com.example.contextcodepredict.myjstereocode.stereotypedresult.StereotypedMethod;
import com.example.contextcodepredict.myjstereocode.stereotypedresult.StereotypedType;
import com.example.contextcodepredict.myjstereocode.storage.LatestHistory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

/**
 * 原型分配器
 * the assigner for stereotype: main entry
 */
public class StereotypeAssigner {
  /**
   * 每个类方法个数平均值，分布平均值
   * methods distribution mean of every class
   */
  double methodsMean;
  /**
   * 每个类方法个数分布的方差
   * methods distribution stdDev of every class
   */
  double methodsStdDev;

  public StereotypeAssigner() {
  }

  /**
   * 设置项目的方法方法参数
   * set project's method data
   *
   * @param methodsMean   方法个数平均值 mean of methods
   * @param methodsStdDev 方法分布的方差 stdDev of methods
   */
  public void setParameters(double methodsMean, double methodsStdDev) {
    this.methodsMean = methodsMean;
    this.methodsStdDev = methodsStdDev;
  }

  /**
   * 为节点分配原型
   * assign stereotype for psiElement
   *
   * @param psiElement 需要分配原型的节点 the element needs stereotype
   * @return 原型字符串 stereotype string
   */
  public String assignStereotypes(PsiElement psiElement) {
    try {
      StereotypedElement stereoElement = null;
      if (psiElement instanceof PsiField) {
        stereoElement = new StereotypedField((PsiField) psiElement);
      }
      if (psiElement instanceof PsiClass) {
        String history = LatestHistory.getHistory(psiElement);
        if (!"".equals(history)) {
          return history;
        }
        stereoElement = new StereotypedType((PsiClass) psiElement, this.methodsMean, this.methodsStdDev);
      } else if (psiElement instanceof PsiMethod) {
        String history = LatestHistory.getHistory(psiElement);
        if (!"".equals(history)) {
          return history;
        }
        stereoElement = new StereotypedMethod((PsiMethod) psiElement);
      }
      if (stereoElement != null) {
        stereoElement.findStereotypes();
        LatestHistory.addTypeItem(psiElement, stereoElement.getStereotypesName());
        return stereoElement.getStereotypesName();
      }
    } catch (NullPointerException var4) {
      System.out.println("error occurred！" + var4.getMessage());
    }
    return "NULL";
  }
}
