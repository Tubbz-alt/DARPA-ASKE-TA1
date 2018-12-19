/*
 * generated by Xtext 2.14.0.RC1
 */
package com.ge.research.sadl.darpa.aske

import com.ge.research.sadl.ValueConverterService
import com.ge.research.sadl.darpa.aske.processing.DialogModelProcessorProvider
import com.ge.research.sadl.generator.SADLOutputConfigurationProvider
import com.ge.research.sadl.model.SadlEObjectDocumentationProvider
import com.ge.research.sadl.processing.IModelProcessorProvider
import com.ge.research.sadl.resource.SadlResourceDescriptionManager
import com.ge.research.sadl.resource.SadlResourceDescriptionStrategy
import com.ge.research.sadl.scoping.SadlQualifiedNameConverter
import com.ge.research.sadl.scoping.SadlQualifiedNameProvider
import com.ge.research.sadl.scoping.SilencedImportedNamesAdapter
import com.ge.research.sadl.validation.ResourceValidator
import com.ge.research.sadl.validation.SoftLinkingMessageProvider
import com.google.inject.Binder
import com.google.inject.Singleton
import java.io.IOException
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider
import org.eclipse.xtext.generator.IOutputConfigurationProvider
import org.eclipse.xtext.linking.impl.ImportedNamesAdapter
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.parsetree.reconstr.IParseTreeConstructor
import org.eclipse.xtext.parsetree.reconstr.ITokenStream
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionManager
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy
import org.eclipse.xtext.validation.ResourceValidatorImpl

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class DialogRuntimeModule extends AbstractDialogRuntimeModule {
		
	override bindIQualifiedNameProvider() {
		SadlQualifiedNameProvider
	}
	
	override configure (Binder binder) {
		super.configure(binder);
		binder.bind(IOutputConfigurationProvider).to(SADLOutputConfigurationProvider).in(Singleton);
	}
	
	def Class<? extends DefaultResourceDescriptionManager> bindDefaultResourceDescriptionManager() {
		return SadlResourceDescriptionManager;
	}
	
	def Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProvider() {
		return SadlEObjectDocumentationProvider;
	}
	
	def Class<? extends IQualifiedNameConverter> bindIQualifiedNameCoverter() {
		return SadlQualifiedNameConverter;
	}
	
	def Class<? extends LinkingDiagnosticMessageProvider> bindILinkingDiagnosticMessageProvider() {
		SoftLinkingMessageProvider
	}
	
	override bindIValueConverterService() {
		ValueConverterService
	}
	
	def Class<? extends ResourceValidatorImpl> bindResourceValidatorImpl() {
		return ResourceValidator
	}
	
	def Class<? extends IModelProcessorProvider> bindIModelProcessorProvider() {
		return DialogModelProcessorProvider
	}
	
// this is what's in SADL	
//	def Class<? extends DefaultLinkingService> bindDefaultLinkingService() {
//		return ErrorAddingLinkingService;
//	}
	
	def Class<? extends IParseTreeConstructor> bindIParseTreeConstructor() {
		NoImplParseTreeConstructor
	}
	
//	def Class<? extends SadlMarkerLocationProvider> bindSadlMarkerLocationProvider() {
//		RequirementsMarkerLocationProvider
//	}
	
	static class NoImplParseTreeConstructor implements IParseTreeConstructor {
		
		override serializeSubtree(EObject object, ITokenStream out) throws IOException {
			throw new UnsupportedOperationException("TODO: auto-generated method stub")
		}
		
	}
	
	def Class<? extends DefaultResourceDescriptionStrategy> bindResourceDescritpionStrategy() {
		return SadlResourceDescriptionStrategy;
	}
// This is what it is f
//	def Class<? extends DefaultResourceDescriptionStrategy> bindResourceDescritpionStrategy() {
//		return ResourceDescriptionStrategy
//	}
	
//	def Class<? extends IDeclarationExtensionsContribution> bindIDeclarationExtensionsContribution() {
//    	return RequirementsDeclarationExtensionsContribution;
//  	}
	
	def Class<? extends ImportedNamesAdapter> bindImportedNamesAdapter() {
		return SilencedImportedNamesAdapter; 
	}
	
}
