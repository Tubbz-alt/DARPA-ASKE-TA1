/**
 * generated by Xtext 2.14.0.RC1
 */
package com.ge.research.sadl.darpa.aske.dialog;

import com.ge.research.sadl.sADL.SadlModelElement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>What Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.ge.research.sadl.darpa.aske.dialog.WhatStatement#getStmt <em>Stmt</em>}</li>
 * </ul>
 *
 * @see com.ge.research.sadl.darpa.aske.dialog.DialogPackage#getWhatStatement()
 * @model
 * @generated
 */
public interface WhatStatement extends SadlModelElement
{
  /**
   * Returns the value of the '<em><b>Stmt</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Stmt</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Stmt</em>' containment reference.
   * @see #setStmt(EObject)
   * @see com.ge.research.sadl.darpa.aske.dialog.DialogPackage#getWhatStatement_Stmt()
   * @model containment="true"
   * @generated
   */
  EObject getStmt();

  /**
   * Sets the value of the '{@link com.ge.research.sadl.darpa.aske.dialog.WhatStatement#getStmt <em>Stmt</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Stmt</em>' containment reference.
   * @see #getStmt()
   * @generated
   */
  void setStmt(EObject value);

} // WhatStatement
