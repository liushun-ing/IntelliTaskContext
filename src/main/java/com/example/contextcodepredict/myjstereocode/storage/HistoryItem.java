package com.example.contextcodepredict.myjstereocode.storage;

import com.intellij.psi.PsiElement;

/**
 * 历史记录项
 * history stereotyped element item
 */
public class HistoryItem {
  private PsiElement psiElement;

  private String stereotype;

  public HistoryItem(PsiElement psiElement, String stereotype) {
    this.psiElement = psiElement;
    this.stereotype = stereotype;
  }

  public PsiElement getPsiElement() {
    return psiElement;
  }

  public void setPsiElement(PsiElement psiElement) {
    this.psiElement = psiElement;
  }

  public String getStereotype() {
    return stereotype;
  }

  public void setStereotype(String stereotype) {
    this.stereotype = stereotype;
  }
}
