package com.example.contextcodepredict.myjstereocode.stereotype;

/**
 * 类原型
 * Class Stereotype
 */
public enum TypeStereotype implements CodeStereotype {
  ENTITY,
  MINIMAL_ENTITY,
  DATA_PROVIDER,
  COMMANDER,
  BOUNDARY,
  FACTORY,
  CONTROLLER,
  PURE_CONTROLLER,
  LARGE_CLASS,
  LAZY_CLASS,
  DEGENERATE,
  DATA_CLASS,
  POOL,
  INTERFACE;

  private TypeStereotype() {
  }
}
