/**
 * generated by Xtext 2.14.0.RC1
 */
package com.ge.research.sadl.darpa.aske;

import com.ge.research.sadl.ValueConverterService;
import com.ge.research.sadl.darpa.aske.AbstractDialogRuntimeModule;
import com.ge.research.sadl.darpa.aske.processing.DialogModelProcessorProvider;
import com.ge.research.sadl.darpa.aske.scoping.DialogErrorAddingLinkingService;
import com.ge.research.sadl.generator.SADLOutputConfigurationProvider;
import com.ge.research.sadl.processing.IModelProcessorProvider;
import com.ge.research.sadl.scoping.SadlQualifiedNameConverter;
import com.ge.research.sadl.scoping.SadlQualifiedNameProvider;
import com.ge.research.sadl.scoping.SilencedImportedNamesAdapter;
import com.ge.research.sadl.validation.ResourceValidator;
import com.ge.research.sadl.validation.SoftLinkingMessageProvider;
import com.google.inject.Binder;
import com.google.inject.Singleton;
import java.io.IOException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.linking.impl.ImportedNamesAdapter;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.parsetree.reconstr.IParseTreeConstructor;
import org.eclipse.xtext.parsetree.reconstr.ITokenStream;
import org.eclipse.xtext.validation.ResourceValidatorImpl;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings("all")
public class DialogRuntimeModule extends AbstractDialogRuntimeModule {
  public static class NoImplParseTreeConstructor implements IParseTreeConstructor {
    @Override
    public IParseTreeConstructor.TreeConstructionReport serializeSubtree(final EObject object, final ITokenStream out) throws IOException {
      throw new UnsupportedOperationException("TODO: auto-generated method stub");
    }
  }
  
  @Override
  public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
    return SadlQualifiedNameProvider.class;
  }
  
  @Override
  public void configure(final Binder binder) {
    super.configure(binder);
    binder.<IOutputConfigurationProvider>bind(IOutputConfigurationProvider.class).to(SADLOutputConfigurationProvider.class).in(Singleton.class);
  }
  
  public Class<? extends IQualifiedNameConverter> bindIQualifiedNameCoverter() {
    return SadlQualifiedNameConverter.class;
  }
  
  public Class<? extends LinkingDiagnosticMessageProvider> bindILinkingDiagnosticMessageProvider() {
    return SoftLinkingMessageProvider.class;
  }
  
  @Override
  public Class<? extends IValueConverterService> bindIValueConverterService() {
    return ValueConverterService.class;
  }
  
  public Class<? extends ResourceValidatorImpl> bindResourceValidatorImpl() {
    return ResourceValidator.class;
  }
  
  public Class<? extends IModelProcessorProvider> bindIModelProcessorProvider() {
    return DialogModelProcessorProvider.class;
  }
  
  public Class<? extends DefaultLinkingService> bindDefaultLinkingService() {
    return DialogErrorAddingLinkingService.class;
  }
  
  public Class<? extends IParseTreeConstructor> bindIParseTreeConstructor() {
    return DialogRuntimeModule.NoImplParseTreeConstructor.class;
  }
  
  public Class<? extends ImportedNamesAdapter> bindImportedNamesAdapter() {
    return SilencedImportedNamesAdapter.class;
  }
}