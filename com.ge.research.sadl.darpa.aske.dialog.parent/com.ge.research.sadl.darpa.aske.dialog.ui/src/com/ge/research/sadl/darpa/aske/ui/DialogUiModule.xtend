/*
 * generated by Xtext 2.14.0.RC1
 */
package com.ge.research.sadl.darpa.aske.ui

import com.ge.research.sadl.darpa.aske.ui.answer.DialogAnswerProvider
import com.ge.research.sadl.darpa.aske.ui.contentassist.DialogOntologyContextProvider
import com.ge.research.sadl.darpa.aske.ui.preferences.DialogRootPreferencePage
import com.ge.research.sadl.darpa.aske.ui.syntaxcoloring.DialogHighlightingConfiguration
import com.ge.research.sadl.darpa.aske.ui.syntaxcoloring.DialogSemanticHighlightingCalculator
import com.ge.research.sadl.darpa.aske.ui.syntaxcoloring.DialogTokenToAttributeIdMapper
import com.ge.research.sadl.external.ExternalEmfResourcePredicate
import com.ge.research.sadl.ide.editor.contentassist.IOntologyContextProvider
import com.ge.research.sadl.ui.contentassist.SadlReferenceProposalCreator
import com.ge.research.sadl.ui.external.EclipseExternalEmfResourcePredicate
import com.ge.research.sadl.ui.utils.EclipseSadlProjectHelper
import com.ge.research.sadl.utils.SadlProjectHelper
import com.google.inject.Binder
import com.google.inject.name.Names
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.ide.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator
import org.eclipse.xtext.ui.editor.autoedit.DefaultAutoEditStrategyProvider
import org.eclipse.xtext.ui.editor.contentassist.AbstractJavaBasedContentProposalProvider.ReferenceProposalCreator
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreInitializer
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration
import com.ge.research.sadl.darpa.aske.ui.preferences.DialogPreferencesInitializer

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class DialogUiModule extends AbstractDialogUiModule {
		// Registers our own syntax coloring styles.
	def Class<? extends IHighlightingConfiguration> bindILexicalHighlightingConfiguration() {
		return DialogHighlightingConfiguration
	}

	// Maps our Ecore nodes to our syntax coloring styles.
	def Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
		return DialogSemanticHighlightingCalculator
	}

	// Maps our token names to our syntax coloring styles.
	def Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindTokenToAttributeIdMapper() {
		return DialogTokenToAttributeIdMapper
	}
	
	def Class<? extends ExternalEmfResourcePredicate> bindExternalEmfResourcePredicate() {
		return EclipseExternalEmfResourcePredicate;
	}
	
	def Class<? extends DefaultAutoEditStrategyProvider> bindDefaultAutoEditStrategyProvider() {
		return DialogAnswerProvider
	}

	def Class<? extends SadlProjectHelper> bindSadlProjectHelper() {
		return EclipseSadlProjectHelper;
	}
	
	def void configurePreferenceInitializer(Binder binder) {
		binder.bind(IPreferenceStoreInitializer).annotatedWith(Names.named("dialogPreferenceInitializer")).to(
			DialogPreferencesInitializer)
	}

	// registers our own root preference page.
	def Class<? extends LanguageRootPreferencePage> bindLanguageRootPreferencePage() {
		return DialogRootPreferencePage
	}

	def Class<? extends ReferenceProposalCreator> bindReferenceProposalCreator() {
		return SadlReferenceProposalCreator;
	}

	def Class<? extends IOntologyContextProvider> bindIOntologyContextProvider() {
		return DialogOntologyContextProvider;
	}
	
}
