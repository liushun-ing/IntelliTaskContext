package com.example.contextcodepredict.data;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;

import java.util.Date;

/**
 * 建议结果数据的对象
 * suggestion data object
 */
public class SuggestionData {
  /**
   * Psi元素
   * psi element
   */
  private PsiElement element;
  /**
   * 包路径
   * position: file and package
   */
  private String packagePath;
  /**
   * 原型
   * stereotype
   */
  private String stereotype;
  /**
   * 置信值
   * confidence
   */
  private String confidence;
  /**
   * 生成的时间
   * time generate this object
   */
  private Date generateTime;

  public SuggestionData() {
  }

  public SuggestionData(PsiElement element, String stereotype, String confidence) {
    PsiJavaFile javaFile = (PsiJavaFile) element.getContainingFile();
    String p = javaFile.getPackageName();
    String f = javaFile.getName();
    this.packagePath = !"".equals(p) ? f + "~" + p : f;
    this.element = element;
    this.stereotype = stereotype;
    this.confidence = confidence;
    this.generateTime = new Date();
  }

  public Date getGenerateTime() {
    return generateTime;
  }

  public void setGenerateTime(Date generateTime) {
    this.generateTime = generateTime;
  }

  public String getPackagePath() {
    return packagePath;
  }

  public void setPackagePath(String packagePath) {
    this.packagePath = packagePath;
  }

  public PsiElement getElement() {
    return element;
  }

  public void setElement(PsiElement element) {
    this.element = element;
  }

  public String getStereotype() {
    return stereotype;
  }

  public void setStereotype(String stereotype) {
    this.stereotype = stereotype;
  }

  public String getConfidence() {
    return confidence;
  }

  public void setConfidence(String confidence) {
    this.confidence = confidence;
  }

  @Override
  public String toString() {
    return "SuggestionData{" +
        "element=" + element +
        ", packagePath='" + packagePath + '\'' +
        ", stereotype='" + stereotype + '\'' +
        ", confidence='" + confidence + '\'' +
        '}';
  }
}
