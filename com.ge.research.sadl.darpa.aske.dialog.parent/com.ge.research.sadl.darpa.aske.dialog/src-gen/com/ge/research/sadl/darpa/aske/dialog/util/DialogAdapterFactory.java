/**
 * generated by Xtext 2.14.0.RC1
 */
package com.ge.research.sadl.darpa.aske.dialog.util;

import com.ge.research.sadl.darpa.aske.dialog.*;

import com.ge.research.sadl.sADL.ExpressionScope;
import com.ge.research.sadl.sADL.SadlModelElement;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.ge.research.sadl.darpa.aske.dialog.DialogPackage
 * @generated
 */
public class DialogAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static DialogPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DialogAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = DialogPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DialogSwitch<Adapter> modelSwitch =
    new DialogSwitch<Adapter>()
    {
      @Override
      public Adapter caseStringResponse(StringResponse object)
      {
        return createStringResponseAdapter();
      }
      @Override
      public Adapter caseWhatStatement(WhatStatement object)
      {
        return createWhatStatementAdapter();
      }
      @Override
      public Adapter caseWhatIsStatement(WhatIsStatement object)
      {
        return createWhatIsStatementAdapter();
      }
      @Override
      public Adapter caseWhatValuesStatement(WhatValuesStatement object)
      {
        return createWhatValuesStatementAdapter();
      }
      @Override
      public Adapter caseHowManyValuesStatement(HowManyValuesStatement object)
      {
        return createHowManyValuesStatementAdapter();
      }
      @Override
      public Adapter caseModifiedAskStatement(ModifiedAskStatement object)
      {
        return createModifiedAskStatementAdapter();
      }
      @Override
      public Adapter caseSadlModelElement(SadlModelElement object)
      {
        return createSadlModelElementAdapter();
      }
      @Override
      public Adapter caseExpressionScope(ExpressionScope object)
      {
        return createExpressionScopeAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.darpa.aske.dialog.StringResponse <em>String Response</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.darpa.aske.dialog.StringResponse
   * @generated
   */
  public Adapter createStringResponseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.darpa.aske.dialog.WhatStatement <em>What Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.darpa.aske.dialog.WhatStatement
   * @generated
   */
  public Adapter createWhatStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.darpa.aske.dialog.WhatIsStatement <em>What Is Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.darpa.aske.dialog.WhatIsStatement
   * @generated
   */
  public Adapter createWhatIsStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.darpa.aske.dialog.WhatValuesStatement <em>What Values Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.darpa.aske.dialog.WhatValuesStatement
   * @generated
   */
  public Adapter createWhatValuesStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.darpa.aske.dialog.HowManyValuesStatement <em>How Many Values Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.darpa.aske.dialog.HowManyValuesStatement
   * @generated
   */
  public Adapter createHowManyValuesStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.darpa.aske.dialog.ModifiedAskStatement <em>Modified Ask Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.darpa.aske.dialog.ModifiedAskStatement
   * @generated
   */
  public Adapter createModifiedAskStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.sADL.SadlModelElement <em>Sadl Model Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.sADL.SadlModelElement
   * @generated
   */
  public Adapter createSadlModelElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ge.research.sadl.sADL.ExpressionScope <em>Expression Scope</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ge.research.sadl.sADL.ExpressionScope
   * @generated
   */
  public Adapter createExpressionScopeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //DialogAdapterFactory
