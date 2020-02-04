/********************************************************************** 
 * Note: This license has also been called the "New BSD License" or 
 * "Modified BSD License". See also the 2-clause BSD License.
 *
 * Copyright � 2018-2019 - General Electric Company, All Rights Reserved
 * 
 * Projects: ANSWER and KApEESH, developed with the support of the Defense 
 * Advanced Research Projects Agency (DARPA) under Agreement  No.  
 * HR00111990006 and Agreement No. HR00111990007, respectively. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 ***********************************************************************/
package com.ge.research.sadl.darpa.aske.processing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.preferences.IPreferenceValuesProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextSyntaxDiagnostic;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.CheckType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ge.research.sadl.darpa.aske.curation.AnswerCurationManager;
import com.ge.research.sadl.darpa.aske.curation.AnswerCurationManager.Agent;
import com.ge.research.sadl.darpa.aske.dialog.AnswerCMStatement;
import com.ge.research.sadl.darpa.aske.dialog.CompareStatement;
import com.ge.research.sadl.darpa.aske.dialog.ExtractStatement;
import com.ge.research.sadl.darpa.aske.dialog.HowManyValuesStatement;
import com.ge.research.sadl.darpa.aske.dialog.ModifiedAskStatement;
import com.ge.research.sadl.darpa.aske.dialog.MyNameIsStatement;
import com.ge.research.sadl.darpa.aske.dialog.SadlEquationInvocation;
import com.ge.research.sadl.darpa.aske.dialog.SaveStatement;
import com.ge.research.sadl.darpa.aske.dialog.SuitabilityStatement;
import com.ge.research.sadl.darpa.aske.dialog.TargetModelName;
import com.ge.research.sadl.darpa.aske.dialog.WhatIsStatement;
import com.ge.research.sadl.darpa.aske.dialog.WhatStatement;
import com.ge.research.sadl.darpa.aske.dialog.WhatValuesStatement;
import com.ge.research.sadl.darpa.aske.dialog.YesNoAnswerStatement;
import com.ge.research.sadl.darpa.aske.preferences.DialogPreferences;
import com.ge.research.sadl.errorgenerator.generator.SadlErrorMessages;
import com.ge.research.sadl.jena.IntermediateFormTranslator;
import com.ge.research.sadl.jena.JenaBasedSadlModelProcessor;
import com.ge.research.sadl.jena.JenaProcessorException;
import com.ge.research.sadl.jena.MetricsProcessor;
import com.ge.research.sadl.jena.UtilsForJena;
import com.ge.research.sadl.model.CircularDefinitionException;
import com.ge.research.sadl.model.ModelError;
import com.ge.research.sadl.model.PrefixNotFoundException;
import com.ge.research.sadl.model.gp.BuiltinElement;
import com.ge.research.sadl.model.gp.Equation;
import com.ge.research.sadl.model.gp.GraphPatternElement;
import com.ge.research.sadl.model.gp.Junction;
import com.ge.research.sadl.model.gp.Literal;
import com.ge.research.sadl.model.gp.NamedNode;
import com.ge.research.sadl.model.gp.NamedNode.NodeType;
import com.ge.research.sadl.model.gp.Node;
import com.ge.research.sadl.model.gp.ProxyNode;
import com.ge.research.sadl.model.gp.Query;
import com.ge.research.sadl.model.gp.RDFTypeNode;
import com.ge.research.sadl.model.gp.Rule;
import com.ge.research.sadl.model.gp.TripleElement;
import com.ge.research.sadl.model.gp.VariableNode;
import com.ge.research.sadl.processing.OntModelProvider;
import com.ge.research.sadl.processing.SadlConstants;
import com.ge.research.sadl.processing.ValidationAcceptor;
import com.ge.research.sadl.reasoner.ConfigurationException;
import com.ge.research.sadl.reasoner.InvalidNameException;
import com.ge.research.sadl.reasoner.InvalidTypeException;
import com.ge.research.sadl.reasoner.QueryCancelledException;
import com.ge.research.sadl.reasoner.QueryParseException;
import com.ge.research.sadl.reasoner.ReasonerNotFoundException;
import com.ge.research.sadl.reasoner.TranslationException;
import com.ge.research.sadl.reasoner.utils.SadlUtils;
import com.ge.research.sadl.sADL.Declaration;
import com.ge.research.sadl.sADL.EquationStatement;
import com.ge.research.sadl.sADL.Expression;
import com.ge.research.sadl.sADL.ExternalEquationStatement;
import com.ge.research.sadl.sADL.NamedStructureAnnotation;
import com.ge.research.sadl.sADL.QueryStatement;
import com.ge.research.sadl.sADL.SadlAnnotation;
import com.ge.research.sadl.sADL.SadlInstance;
import com.ge.research.sadl.sADL.SadlModel;
import com.ge.research.sadl.sADL.SadlModelElement;
import com.ge.research.sadl.sADL.SadlParameterDeclaration;
import com.ge.research.sadl.sADL.SadlResource;
import com.ge.research.sadl.sADL.SadlReturnDeclaration;
import com.ge.research.sadl.sADL.SadlSimpleTypeReference;
import com.ge.research.sadl.sADL.SadlStatement;
import com.ge.research.sadl.sADL.SadlTypeReference;
import com.ge.research.sadl.utils.ResourceManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class JenaBasedDialogModelProcessor extends JenaBasedSadlModelProcessor {
	private static final Logger logger = LoggerFactory.getLogger(JenaBasedDialogModelProcessor.class);
	private boolean modelChanged;
	
	private String textServiceUrl = null;
	private String dbnCgServiceUrl = null;
	private String dbninputjsongenerationserviceurl = null;
	private boolean useDbn = true;
	private String kchainCgServiceUrl = null;
	private boolean useKchain = false;
	private AnswerCurationManager answerCurationManager = null;

	@Inject IPreferenceValuesProvider preferenceProvider;

	@Override
	public void onValidate(Resource resource, ValidationAcceptor issueAcceptor, CheckMode mode, ProcessorContext context) {
		if (!isSupported(resource)) {
			return;
		}
		resetProcessor();
		logger.debug("JenaBasedDialogModelProcessor.onValidate called for Resource '" + resource.getURI() + "'"); 
		CancelIndicator cancelIndicator = context.getCancelIndicator();
		if (resource.getContents().size() < 1) {
			return;
		}
	
		logger.debug("onValidate called for Resource '" + resource.getURI() + "'");
		if (mode.shouldCheck(CheckType.EXPENSIVE)) {
			// do expensive validation, i.e. those that should only be done when 'validate'
			// action was invoked.
		}
		setIssueAcceptor(issueAcceptor);
		setProcessorContext(context);
		setCancelIndicator(cancelIndicator);
		setCurrentResource(resource);
		SadlModel model = (SadlModel) resource.getContents().get(0);
		String modelActualUrl = resource.getURI().lastSegment();
		validateResourcePathAndName(resource, model, modelActualUrl);
		String modelName = model.getBaseUri();
		setModelName(modelName);
		setModelNamespace(assureNamespaceEndsWithHash(modelName));
		setModelAlias(model.getAlias());
		if (getModelAlias() == null) {
			setModelAlias("");
		}

		try {
			theJenaModel = prepareEmptyOntModel(resource);
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			addError(SadlErrorMessages.CONFIGURATION_ERROR.get(e1.getMessage()), model);
			addError(e1.getMessage(), model);
			return; // this is a fatal error
		}
		getTheJenaModel().setNsPrefix(getModelAlias(), getModelNamespace());
		Ontology modelOntology = getTheJenaModel().createOntology(modelName);
		logger.debug("Ontology '" + modelName + "' created");
		modelOntology.addComment("This ontology was created from a DIALOG file '" + modelActualUrl
				+ "' and should not be directly edited.", "en");

		String modelVersion = model.getVersion();
		if (modelVersion != null) {
			modelOntology.addVersionInfo(modelVersion);
		}

		EList<SadlAnnotation> anns = model.getAnnotations();
		addAnnotationsToResource(modelOntology, anns);

		OntModelProvider.registerResource(resource);
		// clear any pre-existing content
		List<Object> oc = OntModelProvider.getOtherContent(resource);
		if (oc != null) {
			Iterator<Object> itr = oc.iterator();
			List<Equation> eqs = new ArrayList<Equation>();
			while (itr.hasNext()) {
				Object nxt = itr.next();
				if (nxt instanceof Equation) {
					eqs.add((Equation)nxt);
				}
			}
			if (eqs.size() > 0) {
				oc.removeAll(eqs);
			}
		}

		try {
			// Add SadlBaseModel to everything except the SadlImplicitModel
			if (!resource.getURI().lastSegment().equals(SadlConstants.SADL_IMPLICIT_MODEL_FILENAME)) {
				addSadlBaseModelImportToJenaModel(resource);
			}
			// Add the SadlImplicitModel to everything except itself and the
			// SadlBuilinFunctions
			if (!resource.getURI().lastSegment().equals(SadlConstants.SADL_IMPLICIT_MODEL_FILENAME)
					&& !resource.getURI().lastSegment().equals(SadlConstants.SADL_BUILTIN_FUNCTIONS_FILENAME)) {
				addImplicitSadlModelImportToJenaModel(resource, context);
				addImplicitBuiltinFunctionModelImportToJenaModel(resource, context);

			}
			if (modelActualUrl.equals(ResourceManager.ServicesConf_SFN)) {
				try {
					importSadlServicesConfigConceptsModel(resource);
				} catch (JenaProcessorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			checkCodeExtractionSadlModelExistence(resource, context);
			importSadlListModel(resource);		// an import could happen at any time and require a list model
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (JenaProcessorException e1) {
			e1.printStackTrace();
		}
		
		if (model.eContents().size() < 1) {
			// there are no imports
			MixedInitiativeTextualResponse mir = new MixedInitiativeTextualResponse("Please replace this reminder with at least one import of a domain namespace");
			int endOffset = NodeModelUtils.findActualNodeFor(model).getEndOffset();
			mir.setInsertionPoint(endOffset);
			OntModelProvider.addPrivateKeyValuePair(resource, DialogConstants.LAST_DIALOG_COMMAND, mir);
		}

		if(!processModelImports(modelOntology, resource.getURI(), model)) {
			return;
		}

		boolean enableMetricsCollection = true; // no longer a preference
		try {
			if (enableMetricsCollection) {
				if (!isSyntheticUri(null, resource)) {
					setMetricsProcessor(new MetricsProcessor(modelName, resource,
							getConfigMgr(resource, getOwlModelFormat(context)), this));
				}
			}
		} catch (JenaProcessorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initializePreferences(context);

		// Check for a syntactically valid AST; if it isn't then don't process so that conversations will only be valid ones
	    boolean validAST = isAstSyntaxValid(model);	
	    if (!validAST) {
	    	return;
	    }

//		try {
//			if (!getAnswerCurationManager().dialogAnserProviderInitialized()) {
//				System.err.println("DialogAnswerProvider not yet initialized. Touch window.");
////				return;
//			}
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}

		// create validator for expressions
		initializeModelValidator();
		initializeAllImpliedPropertyClasses();
		initializeAllExpandedPropertyClasses();
		try {
			initializeDialogContent();
		} catch (ConversationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// process rest of parse tree
		List<SadlModelElement> elements = model.getElements();
		if (elements != null) {
			Iterator<SadlModelElement> elitr = elements.iterator();
			Object lastElement = null;
			while (elitr.hasNext()) {
				// check for cancelation from time to time
				if (cancelIndicator.isCanceled()) {
					throw new OperationCanceledException();
				}
				SadlModelElement element = elitr.next();
				boolean stmtComplete = statementIsComplete(element);
				String eos = getEos(element);
        		if (eos != null && (!(eos.endsWith(".") || eos.endsWith("?")))) {	// I don't think this is needed anymore awc 9/4/19
               		continue;
         		}
				logger.debug("   Model element of type '" + element.getClass().getCanonicalName() + "' being processed.");
				// reset state for a new model element
				try {
					resetProcessorState(element);
				} catch (InvalidTypeException e) {
					// TODO Auto-generated catch block
					logger.error("Error:", e);
				}
				try {
					StatementContent sc = processDialogModelElement(element);
					if (sc != null) {
						ConversationElement ce = new ConversationElement(getAnswerCurationManager().getConversation(), sc, sc.getAgent());
						getAnswerCurationManager().addToConversation(ce);
					}
				} catch (JenaProcessorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidNameException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidTypeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TranslationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (QueryParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (QueryCancelledException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ReasonerNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (PrefixNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				try {
//					if (!(element instanceof AnswerCMStatement)) {
//						// this is user input
//						lastElement = processDialogModelElement(resource, element);
//					lastACMQuestion = null;
//					}
//					else if (eos != null && eos.equals("?")) {
//						// this is a question from the backend
//						lastACMQuestion = (AnswerCMStatement) element;
//					}
//					else {
//						// this is a response from CM
//						//	clear last command
//						OntModelProvider.clearPrivateKeyValuePair(resource, DialogConstants.LAST_DIALOG_COMMAND);
//						lastElement = null;
//						processAnswerCMStatement(resource, (AnswerCMStatement)element);
//					}
//				} catch (IOException | TranslationException | InvalidNameException | InvalidTypeException | ConfigurationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			if (lastElement != null) {
				// this is the one to which the CM should respond; keep it for the DialogAnswerProvider
//				processUserInputElement(lastElement);
			}
			
			File saveFile = getModelFile(resource);
			try {
				if (saveFile != null && (isModelChanged() || !saveFile.exists() || 
						getConfigMgr().getAltUrlFromPublicUri(getModelName()) == null ||
						getConfigMgr().getAltUrlFromPublicUri(getModelName()) == getModelName())) {
					autoSaveModel(resource, getConfigMgr().getModelFolder(), saveFile, context);
					// refresh resource ?
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (getSadlCommands() != null && getSadlCommands().size() > 0) {
				OntModelProvider.attach(model.eResource(), getTheJenaModel(), getModelName(), getModelAlias(),
						getSadlCommands());
			} else {
				OntModelProvider.attach(model.eResource(), getTheJenaModel(), getModelName(), getModelAlias());
			}
			
			// Do this **after** setting the resource information in the OntModelProvider
			try {
				getAnswerCurationManager().processConversation(getCurrentResource(), getTheJenaModel(), getModelName());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			logger.debug("At end of model processing, conversation is:");
			try {
				logger.debug(getAnswerCurationManager().getConversation().toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

		}
	}

	private StatementContent processDialogModelElement(SadlModelElement element) throws JenaProcessorException, InvalidNameException, InvalidTypeException, TranslationException, IOException, ConfigurationException, QueryParseException, QueryCancelledException, ReasonerNotFoundException, PrefixNotFoundException {
		clearCruleVariables();
		if (element instanceof AnswerCMStatement) {
			return processAnswerCMStatement((AnswerCMStatement)element);
		}
		else if (element instanceof ExtractStatement) {
			return processStatement((ExtractStatement)element);
		}
		else if (element instanceof CompareStatement) {
			return processStatement((CompareStatement)element);
		}
		else if (element instanceof SuitabilityStatement) {
			return processStatement((SuitabilityStatement)element);
		}
		else if (element instanceof ModifiedAskStatement) {
			return processStatement((ModifiedAskStatement)element);
		}
		else if (element instanceof WhatStatement) {
			return processStatement((WhatStatement)element);
		}
		else if (element instanceof HowManyValuesStatement) {
			return processStatement((HowManyValuesStatement)element);
		}
		else if (element instanceof SaveStatement) {
			return processStatement((SaveStatement)element);
		}
		else if (element instanceof SadlEquationInvocation) {
			return processStatement((SadlEquationInvocation)element);
		}
		else if (element instanceof YesNoAnswerStatement) {
			return processStatement((YesNoAnswerStatement)element);
		}
		else if (element instanceof MyNameIsStatement) {
			return processStatement((MyNameIsStatement)element);
		}
		else if (element instanceof TargetModelName) {
			return processStatement((TargetModelName)element);
		}
		else if (element instanceof ExternalEquationStatement) {
			super.processStatement((ExternalEquationStatement)element);
			List<Object> oc = OntModelProvider.getOtherContent(getCurrentResource());
			String eqUri  = null;
			if (oc != null && oc.size() > 0) {
				Object obj = oc.get(oc.size() - 1);
				if (obj instanceof Equation) {
					Equation eq = (Equation)obj;
					validateEquationAugmentedTypes((ExternalEquationStatement) element, eq);
					eqUri = eq.getUri();
				}
			}
			if (eqUri != null) {
				getAnswerCurationManager().addEquationInformation(eqUri, NodeModelUtils.findActualNodeFor(element).getText());
			}
			else {
				addError("Unable to find URI of External equation", element);
			}
			return null;
		}
		else if (element instanceof EquationStatement) {
			super.processStatement((EquationStatement)element);
			List<Object> oc = OntModelProvider.getOtherContent(getCurrentResource());
			String eqUri  = null;
			if (oc != null && oc.size() > 0) {
				Object obj = oc.get(oc.size() - 1);
				if (obj instanceof Equation) {
					Equation eq = (Equation)obj;
					validateEquationAugmentedTypes((EquationStatement) element, eq);
					eqUri = eq.getUri();
				}
			}
			if (eqUri != null) {
				getAnswerCurationManager().addEquationInformation(eqUri, NodeModelUtils.findActualNodeFor(element).getText());
			}
			else {
				addError("Unable to find URI of External equation", element);
			}
			return null;
		}
		else if (element instanceof SadlStatement) {
			SadlStatementContent ssc = new SadlStatementContent(element, Agent.USER);
			super.processModelElement((SadlStatement)element);
			if (element instanceof SadlInstance) {
				String srUri = getDeclarationExtensions().getConceptUri(sadlResourceFromSadlInstance((SadlInstance)element));
				if (getAnswerCurationManager().getEquationInformation(srUri) != null) {
					getAnswerCurationManager().addEquationInformation(srUri, NodeModelUtils.findActualNodeFor(element).getText());
				}
			}
			return ssc;
		}
		else {
			throw new TranslationException("Model element of type '" + element.getClass().getCanonicalName() + "' not handled.");
		}	
	}
	
	private void validateEquationAugmentedTypes(ExternalEquationStatement element, Equation eq) {
		Iterator<SadlParameterDeclaration> spitr = element.getParameter().iterator();
		while (spitr.hasNext()) {
			SadlParameterDeclaration spd = spitr.next();
			if (spd.getAugtype() == null) {
				addWarning("Missing augmented type information", spd.getName());
			}
		}
		Iterator<SadlReturnDeclaration> srtitr = element.getReturnType().iterator();
		while (srtitr.hasNext()) {
			SadlReturnDeclaration srd = srtitr.next();
			if (srd.getAugtype() == null) {
				addWarning("Missing augmented return type information", srd.getType());
			}
		}
	}

	private void validateEquationAugmentedTypes(EquationStatement element, Equation eq) {
		Iterator<SadlParameterDeclaration> spitr = element.getParameter().iterator();
		while (spitr.hasNext()) {
			SadlParameterDeclaration spd = spitr.next();
			if (spd.getAugtype() == null) {
				addWarning("Missing augmented type information", spd.getName());
			}
		}
		Iterator<SadlReturnDeclaration> srtitr = element.getReturnType().iterator();
		while (srtitr.hasNext()) {
			SadlReturnDeclaration srd = srtitr.next();
			if (srd.getAugtype() == null) {
				addWarning("Missing augmented return type information", srd.getType());
			}
		}
	}

	private StatementContent processStatement(ExtractStatement element) {
		EList<String> srcUris = element.getSourceURIs();
		String str = projectHelper.toString();
		for (String srcUri : srcUris) {
			try {
				String scheme = getUriScheme(srcUri);
				String source;
				if (scheme != null && scheme.equals("file")) {
					SadlUtils su = new SadlUtils();
					File srcFile = new File(su.fileUrlToFileName(srcUri));
					if (!srcFile.exists()) {
						addError("File '" + srcFile.getCanonicalPath() + "' does not exist.", element);
					}
					else if (!srcFile.isFile()) {
						addError("'" + srcFile.getCanonicalPath() + "' is not a file.", element);
					}
					source = srcFile.getCanonicalPath();
				}
				else {
					source = srcUri;
				}
				return new ExtractContent(element, Agent.USER, scheme, source, srcUri);
			} catch (URISyntaxException e) {
				addError("'" + srcUri + "' does not appear to be a valid URL: " + e.getMessage(), element);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!validURI(srcUri)) {
				addError("'" + srcUri + "' does not appear to be a valid URL", element);
			}
		}
		return null;
	}

	private StatementContent processStatement(CompareStatement element) throws InvalidNameException, InvalidTypeException, TranslationException, IOException, PrefixNotFoundException, ConfigurationException {
		Expression thenExpr = element.getToCompare();
		Expression whenExpr = element.getWhenExpr();

		List<Rule> comparisonRules = whenAndThenToCookedRules(whenExpr, thenExpr);
		
		return new CompareContent(element, Agent.USER, comparisonRules);
	}

	private StatementContent processStatement(SuitabilityStatement element) throws InvalidNameException, InvalidTypeException, TranslationException {
		Expression whenExpr = element.getSuitableExpression();
		Expression thenExpr = element.getWhat();
		
		List<Rule> comparisonRules = whenAndThenToCookedRules(whenExpr, thenExpr);

		return new CompareContent(element, Agent.USER, comparisonRules);
	}

	private List<Rule> whenAndThenToCookedRules(EObject whenExpr, EObject thenExpr)
			throws InvalidNameException, InvalidTypeException, TranslationException {
		Object thenObj = processExpression(thenExpr);
		Object wh = processExpression(whenExpr);
		if (wh instanceof Object[]) {
			if (((Object[])wh).length == 2 && ((Object[])wh)[1] instanceof GraphPatternElement) {
				// expected
				wh = ((Object[])wh)[1];
			}
		}
		DialogIntermediateFormTranslator dift = new DialogIntermediateFormTranslator(this, getTheJenaModel());
		dift.setStartingVariableNumber(getVariableNumber());
		if (wh instanceof GraphPatternElement) {
			wh = dift.addImpliedAndExpandedProperties((GraphPatternElement)wh);
			setVariableNumber(dift.getVariableNumber());
			List<GraphPatternElement> gpes = new ArrayList<GraphPatternElement>();
			gpes = unitSpecialConsiderations(gpes, wh, thenExpr);
			wh = dift.listToAnd(gpes);
			if (wh instanceof List<?> && ((List<?>)wh).size() == 1) {
				wh = ((List<?>)wh).get(0);
			}
			else {
				throw new TranslationException("Unexpected array of size > 1");
			}
		}
		Node whenObj = nodeCheck(wh);
		List<Node> thenObjects = new ArrayList<Node>();
		NamedNode specifiedPropertyNN = null;
		if (thenObj instanceof Junction) {
			List<Node> compareList = IntermediateFormTranslator.conjunctionToList((Junction)thenObj);
			for (Node n : compareList) {
				if (n instanceof VariableNode) {
					thenObjects.add(((VariableNode)n).getType());
				}
				else {
					thenObjects.add(n);
				}
			}
		}
		else if (thenObj instanceof NamedNode) {
			thenObjects.add((Node) thenObj);
		}
		else if (thenObj instanceof TripleElement) {
			if (((TripleElement)thenObj).getSubject() instanceof VariableNode) {
				((TripleElement)thenObj).setSubject(((VariableNode)((TripleElement)thenObj).getSubject()).getType());
			}
			thenObjects.add(nodeCheck(thenObj));
		}
		else if (thenObj instanceof BuiltinElement) {
			addError("BuiltinElements not yet handled.", thenExpr);
		}
		List<Node> augmentedComparisonObjects = new ArrayList<Node>();
		for (int idx = 0; idx < thenObjects.size(); idx++) {
			Node cn = thenObjects.get(idx);
			if (cn instanceof NamedNode) {
				NamedNode nn = (NamedNode)cn;
				if (isProperty(nn.getNodeType())) {
					Property prop = getTheJenaModel().getProperty(nn.getURI());
					if (prop == null) {
						addError("Unexpected error finding property '" + nn.getURI() + "'", thenExpr);
						getTheJenaModel().write(System.err);
						ExtendedIterator<OntModel> smitr = getTheJenaModel().listSubModels();
						while (smitr.hasNext()) {
							smitr.next().write(System.err);
						}
						return null;
					}
					else {
						specifiedPropertyNN = nn;
					}
					StmtIterator stmtitr = getTheJenaModel().listStatements(prop, RDFS.domain, (RDFNode)null);
					while (stmtitr.hasNext()) {
						com.hp.hpl.jena.rdf.model.Resource dmn = stmtitr.nextStatement().getObject().asResource();
						if (dmn.isURIResource()) {
							nn = new NamedNode(dmn.getURI());
							nn.setNodeType(NodeType.ClassNode);
						}
						else {
							addError("Blank node domain not yet handled", thenExpr);
							return null;
						}
						if (stmtitr.hasNext()) {
							addError("Multiple domain classes not yet handled", thenExpr);
							return null;
						}
					}
				}
				if (nn instanceof VariableNode) {
					// this will happen when there is an article in front of a class name (a Declaration)
					Node tn = ((VariableNode)nn).getType();
					if (tn instanceof NamedNode) {
						nn = (NamedNode) tn;
					}
				}
				if (nn.getNodeType().equals(NodeType.ClassNode)) {
					NodeType ntype = null;
					boolean comparisonsFound = false;
					OntClass theClass = getTheJenaModel().getOntClass(nn.getURI());
					List<com.hp.hpl.jena.rdf.model.Resource> instances = new ArrayList<com.hp.hpl.jena.rdf.model.Resource>();
					// look for at least two instances of the class 
					instances = getInstancesOfClass(theClass, instances);
					if (instances.size() > 1) {
						comparisonsFound = true;
						ntype = NodeType.InstanceNode;
					}
					if (!comparisonsFound) {
						// if not resolved, look for leaf subclasses of the class, and create a variable of each leaf subclass type
						instances = getLeafSubclasses(theClass, instances);
						if (instances.size() > 1) {
							ntype = NodeType.ClassNode;
						}
					}
					if (instances.size() > 1) {
						for (int i = 0; i < instances.size(); i++) {
							Node compNode;
							if (specifiedPropertyNN != null) {
								Node subj;
								if (ntype.equals(NodeType.ClassNode)) {
									NamedNode varType = new NamedNode(instances.get(i).getURI());
									varType.setNodeType(ntype);
									subj = varType;	// don't create variable here--will be done in missing pattern processing
								}
								else {
									NamedNode instNN = new NamedNode(instances.get(i).getURI());
									instNN.setNodeType(ntype);
									subj = instNN;
								}
								compNode = nodeCheck(new TripleElement(subj, specifiedPropertyNN, null));
								augmentedComparisonObjects.add(compNode);
							}
							else {
								NamedNode instNN = new NamedNode(instances.get(i).getURI());
								instNN.setNodeType(ntype);
								instNN.setLocalizedType(nn);
								// we don't have any property specified so we need to generate a list of relevant properties
								List<NamedNode> relevantProperties;
								if (instNN.getNodeType().equals(NodeType.InstanceNode)) {
									relevantProperties = getRelevantPropertiesOfClass(nn);									
								}
								else {
									relevantProperties = getRelevantPropertiesOfClass(instNN);
								}
								if (relevantProperties != null) {
									for (NamedNode prop : relevantProperties) {
										if (!whensContainProperty(whenObj, prop)) {
											compNode = nodeCheck(new TripleElement(instNN, prop, null));
											augmentedComparisonObjects.add(compNode);
										}
									}
								}
								else {
									addError("No properties found for target", thenExpr);
									return null;
								}
							}
						}
					}
					else {
						// need to find the important properties for the given subject class and create multiple triples, one for each property
						augmentedComparisonObjects.add(new ProxyNode(new TripleElement(nn, specifiedPropertyNN, null)));
						
					}
				}
				else {
					addError("Failed to establish an anchor node", thenExpr);
				}
			}
			else {
				augmentedComparisonObjects.add(cn);
			}
		}
		
		dift.setStartingVariableNumber(getVariableNumber());
		List<Rule> comparisonRules = new ArrayList<Rule>();
		for (int i = 0; i < augmentedComparisonObjects.size(); i++) {
			Node cobj = augmentedComparisonObjects.get(i);
			Node newWhen = dift.newCopyOfProxyNode(whenObj);
			Rule pseudoRule = new Rule("ComparePseudoRule" + i, null, nodeToGPEList(newWhen), nodeToGPEList(cobj));
			populateRuleVariables(pseudoRule);
			dift.setTarget(pseudoRule);
			Rule modifiedRule = dift.cook(pseudoRule);
			comparisonRules.add(modifiedRule);
			logger.debug(modifiedRule.toDescriptiveString());
			System.out.println(modifiedRule.toFullyQualifiedString());
//			addInfo(modifiedRule.toFullyQualifiedString(), thenExpr.eContainer());
		}
		return comparisonRules;
	}

	private boolean whensContainProperty(Node whenObj, NamedNode prop) {
		if (whenObj instanceof ProxyNode) {
			GraphPatternElement gpe = ((ProxyNode)whenObj).getProxyFor();
			return whensContainProperty(gpe, prop);
		}
		return false;
	}

	private boolean whensContainProperty(GraphPatternElement gpe, NamedNode prop) {
		if (gpe instanceof Junction) {
			if (whensContainProperty((Node) ((Junction)gpe).getLhs(), prop)) {
				return true;
			}
			if (whensContainProperty((Node) ((Junction)gpe).getRhs(), prop)) {
				return true;
			}
		}
		else if (gpe instanceof BuiltinElement) {
			for (Node arg : ((BuiltinElement)gpe).getArguments()) {
				if (arg instanceof NamedNode && ((NamedNode)arg).getURI().equals(prop.getURI())) {
					return true;
				}
				else if (arg instanceof ProxyNode) {
					if (whensContainProperty((ProxyNode)arg, prop)) {
						return true;
					}
				}
			}
		}
		else if (gpe instanceof TripleElement) {
			if (((TripleElement)gpe).getPredicate().equals(prop)) {
				return true;
			}
		}
		return false;
	}

	private List<NamedNode> getRelevantPropertiesOfClass(NamedNode clsNode) {
		if (clsNode.getNodeType().equals(NodeType.ClassNode)) {
			OntClass cls = getTheJenaModel().getOntClass(clsNode.getURI());
			return getReleventPropertiesOfClass(cls);
		}
		return null;
	}
	
	private List<NamedNode> getReleventPropertiesOfClass(OntClass cls) {
		List<NamedNode> relevantProperties = new ArrayList<NamedNode>();
		StmtIterator dmnitr = getTheJenaModel().listStatements(null, RDFS.domain, cls);
		while (dmnitr.hasNext()) {
			com.hp.hpl.jena.rdf.model.Resource propnode = dmnitr.nextStatement().getSubject();
			if (propnode.isURIResource()) {
				NamedNode objNN = new NamedNode(propnode.asResource().getURI());
				if (objNN != null  ) {
					if (propnode.canAs(Property.class)) {
						objNN.setNodeType(NodeType.PropertyNode);
						relevantProperties.add(objNN);
					}
				}
			}
		}
		StmtIterator scitr = getTheJenaModel().listStatements(cls, RDFS.subClassOf, (RDFNode)null);
		while (scitr.hasNext()) {
			RDFNode sc = scitr.nextStatement().getObject();
			if (sc.isResource() && sc.asResource().canAs(OntClass.class)) {
				List<NamedNode> moreProps = getReleventPropertiesOfClass(sc.asResource().as(OntClass.class));
				if (moreProps != null) {
					relevantProperties.addAll(moreProps);
				}
			}
		}
		return relevantProperties;
	}

	private void populateRuleVariables(Rule rule) {
		if (rule.getGivens() != null) {
			for (GraphPatternElement gpe : rule.getGivens()) {
				populateRuleVariables(rule, gpe);
			}
		}
		if (rule.getIfs() != null) {
			for (GraphPatternElement gpe : rule.getIfs()) {
				populateRuleVariables(rule, gpe);
			}
		}
		if (rule.getThens() != null) {
			for (GraphPatternElement gpe : rule.getThens()) {
				populateRuleVariables(rule, gpe);
			}
		}
		
	}

	private void populateRuleVariables(Rule rule, GraphPatternElement gpe) {
		if (gpe instanceof TripleElement) {
			if (((TripleElement)gpe).getSubject() instanceof VariableNode) {
				rule.addRuleVariable((VariableNode) ((TripleElement)gpe).getSubject());
			}
			if (((TripleElement)gpe).getPredicate() instanceof VariableNode) {
				rule.addRuleVariable((VariableNode) ((TripleElement)gpe).getPredicate());
			}
			if (((TripleElement)gpe).getObject() instanceof VariableNode) {
				rule.addRuleVariable((VariableNode) ((TripleElement)gpe).getObject());
			}
		}
		else if (gpe instanceof BuiltinElement) {
			if (((BuiltinElement)gpe).getArguments() != null) {
				for (Node arg : ((BuiltinElement)gpe).getArguments()) {
					if (arg instanceof VariableNode) {
						rule.addRuleVariable((VariableNode)arg);
					}
					else if (arg instanceof ProxyNode) {
						populateRuleVariables(rule, ((ProxyNode)arg).getProxyFor());
					}
				}
			}
		}
		else if (gpe instanceof Junction) {
			populateRuleVariables(rule, ((ProxyNode)((Junction)gpe).getLhs()).getProxyFor());
			populateRuleVariables(rule, ((ProxyNode)((Junction)gpe).getRhs()).getProxyFor());
		}
	}

	private List<GraphPatternElement> nodeToGPEList(Node node) {
		List<GraphPatternElement> gpelist = new ArrayList<GraphPatternElement>();
		if (node instanceof ProxyNode) {
			GraphPatternElement gpe = ((ProxyNode)node).getProxyFor();
			if (gpe instanceof Junction) {
				List<Node> nodes = DialogIntermediateFormTranslator.conjunctionToList((Junction) gpe);
				for (Node n : nodes) {
					if (n instanceof ProxyNode) {
						gpelist.add(((ProxyNode)n).getProxyFor());
					}
				}
			}
			else {
				gpelist.add(gpe);
			}
		}
		return gpelist;
	}

	private VariableNode createComparisonTypedVariable(NamedNode varType, EObject context) throws IOException, PrefixNotFoundException, InvalidNameException, InvalidTypeException, TranslationException, ConfigurationException {
		String nvar = getNewVar(context);
		VariableNode var = createVariable(getModelNamespace() +nvar, context);	
		var.setType(varType);
		return var;
	}

	/**
	 * Method to find all of the instances of a given class
	 * @param theClass
	 * @param instances
	 * @return
	 */
	private List<com.hp.hpl.jena.rdf.model.Resource> getInstancesOfClass(OntClass theClass, List<com.hp.hpl.jena.rdf.model.Resource> instances) {
		StmtIterator stmtitr = getTheJenaModel().listStatements(null, RDF.type, theClass);
		if (stmtitr.hasNext()) {
			while (stmtitr.hasNext()) {
				com.hp.hpl.jena.rdf.model.Resource inst = stmtitr.nextStatement().getSubject();
				if (inst.isURIResource()) {
					// don't include unnamed instances, at least for the time being (awc 1/29/2020)
					instances.add(inst);					
				}
			}
		}
		ExtendedIterator<OntClass> scitr = theClass.listSubClasses();
		while (scitr.hasNext()) {
			instances = getInstancesOfClass(scitr.next(), instances);
		}
		return instances;
	}

	/**
	 * Method to get all of the leaf subclasses of a given class
	 * @param theClass
	 * @param instances
	 * @return
	 */
	private List<com.hp.hpl.jena.rdf.model.Resource> getLeafSubclasses(OntClass theClass,
			List<com.hp.hpl.jena.rdf.model.Resource> instances) {
		ExtendedIterator<OntClass> scitr = theClass.listSubClasses();
		while (scitr.hasNext()) {
			OntClass sc = scitr.next();
			int cnt = instances.size();
			instances = getLeafSubclasses(sc, instances);
			if (instances.size() == cnt) {
				instances.add(sc);
			}
		}
		return instances;
	}

	private StatementContent processStatement(MyNameIsStatement element) throws IOException {
		getAnswerCurationManager().setUserName(element.getAnswer());
		AnswerContent ac = new AnswerContent(element, Agent.USER);
		ac.setAnswer(element.getAnswer());
		return ac;
	}

	private StatementContent processStatement(YesNoAnswerStatement element) throws IOException {
		AnswerContent ac = new AnswerContent(element, Agent.USER);
		ac.setAnswer(element.getAnswer());
		return ac;
	}

	private String getEos(SadlModelElement element) {
		if (element instanceof EObject) {
			ICompositeNode nd = NodeModelUtils.findActualNodeFor((EObject) element);
			INode nsnd = nd.getNextSibling();
			String elementText = nd.getText();
			String nsndTxt = nsnd != null ? nsnd.getText() : null;
			String trimmed = elementText.trim();
			if (trimmed.endsWith(".")) {
				return ".";
			}
			else if (trimmed.endsWith("?")) {	// I don't think this is needed anymore awc 9/4/19
				return "?";
			}
		}
		return null;
	}

//	private Object processDialogModelElement(Resource resource, EObject stmt) throws IOException, TranslationException, InvalidNameException, InvalidTypeException, ConfigurationException {
//		ConversationElement ce = null;
//		Object toBeReturned = null;
//		if (stmt instanceof MyNameIsStatement) {
//			System.out.println("User name is " + ((MyNameIsStatement)stmt).getAnswer());
//		}
//		else if (stmt instanceof ModifiedAskStatement ||
//				stmt instanceof WhatStatement ||
//				stmt instanceof HowManyValuesStatement ||
//				stmt instanceof SaveStatement) {
////			ce = processUserInputElement(stmt);
//		}
//		else {
//			boolean treatAsAnswerToBackend = false;
//			AnswerCMStatement lastACMQuestion = getAnswerCurationManager().getLastACMQuestion();
//			if (lastACMQuestion  != null) {
//				// this could be the answer to a preceding question
//				if (stmt instanceof SadlStatement || stmt instanceof YesNoAnswerStatement) {
//					try {
//						IDialogAnswerProvider dap = getDialogAnswerProvider(resource);
//						String question = lastACMQuestion.getStr();
////						if (question != null) {
////							MixedInitiativeElement mie = dap.getMixedInitiativeElement(question);
//////										dap.removeMixedInitiativeElement(question);
////							if (mie != null) {
////					            // construct response
////								String answer = getResponseFromSadlStatement(stmt);
////								mie.addArgument(answer);
////								dap.provideResponse(mie);
//////								            MixedInitiativeElement response = new MixedInitiativeElement(answer, null);
//////								            response.setContent(new MixedInitiativeTextualResponse(answer));
//////								            // make call identified in element
//////								            mie.getRespondTo().accept(response);
////								dap.removeMixedInitiativeElement(question);	// question has been answered
////								treatAsAnswerToBackend = true;
////								ce = new ConversationElement(getAnswerCurationManager().getConversation(), mie, Agent.USER);
////								toBeReturned = mie;
////							}
////							else {
////								treatAsAnswerToBackend = true;
////								String answer = getResponseFromSadlStatement(stmt);
////								ce = new ConversationElement(getAnswerCurationManager().getConversation(), answer, Agent.USER);
////								toBeReturned = answer;
////							}
////						}
//					} catch (ConfigurationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			if (!treatAsAnswerToBackend && !(stmt instanceof YesNoAnswerStatement)) {
//				if (stmt instanceof TargetModelName) {
//					processModelElement((TargetModelName)stmt);
//				}
//				else if (stmt instanceof SadlEquationInvocation) {
//					processModelElement((SadlEquationInvocation)stmt);
//				}
//				// This is some kind of SADL statement to add to the model
//				else if (stmt instanceof SadlModelElement) {
//					processModelElement((SadlModelElement) stmt);
////					ce = new ConversationElement(getAnswerCurationManager().getConversation(), stmt, Agent.USER);
//					toBeReturned = stmt;
//				}
//				else {
//					try {
//						throw new JenaProcessorException("statement wasn't of expected type");
//					} catch (JenaProcessorException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				setModelChanged(true);
//			}
//		}
//		if (ce != null) {
//    		if (stmt instanceof EObject) {
//    			ICompositeNode node = NodeModelUtils.findActualNodeFor((EObject) stmt);
//     			ce.setText(node.getText());
//    			ce.setStartingLocation(node.getTotalOffset());
//    			ce.setLength(node.getTotalLength());
//    		}
//			getAnswerCurationManager().addToConversation(ce);
//		}
//		return toBeReturned;
//	}

//	@Inject
//	private SADLGrammarAccess grammarAccess;
	
	private StatementContent processStatement(TargetModelName element) throws ConfigurationException, IOException {
		boolean returnVal = true;
		SadlModel targetResource = element.getTargetResource();
		String aliasToUse = element.getAlias();
		if (targetResource != null) {
			// URI importingResourceUri = resource.getURI();
			String targetUri = targetResource.getBaseUri();
			String targetPrefix = targetResource.getAlias();
			Resource eResource = targetResource.eResource();
			if (eResource instanceof XtextResource) {
				XtextResource xtrsrc = (XtextResource) eResource;
				URI targetResourceUri = xtrsrc.getURI();
				OntModel targetOntModel = OntModelProvider.find(xtrsrc);
				if (targetOntModel == null) {
					addError("Model not found", element);
					returnVal = false;
				}
				else if (aliasToUse == null) {
					if (targetPrefix != null) {
						aliasToUse = targetPrefix;
					}
					else {
						String gprefix = getConfigMgr().getGlobalPrefix(targetUri);
						if (gprefix == null) {
							addError("No global prefix found for model so a local alias is required", element);
							returnVal = false;
						}
						else {
							aliasToUse = gprefix;
						}
					}
				}
				if (returnVal) {
					String[] uris = new String[2];
					uris[0] = targetUri;
					uris[1] = null;
					getAnswerCurationManager().addTargetModelToMap(aliasToUse, uris);
				}
			}
		}
		return null;
	}
	
	private StatementContent processStatement(SadlEquationInvocation element) throws TranslationException, InvalidNameException, InvalidTypeException, IOException {
		SadlResource name = element.getName();
		Node srobj = processExpression(name);
		EvalContent ec = new EvalContent(element, Agent.USER);
		ec.setEquationName(srobj);
		
		EList<Expression> params = element.getParameter();
		if (! params.isEmpty()) {
			List<Node> args = new ArrayList<Node>();
			for (Expression param : params) {
				Object paramObj = processExpression(param);
				args.add(nodeCheck(paramObj));
			}
			ec.setParameters(args);
		}
		EList<String> units = element.getUnits();
		if (!units.isEmpty()) {
			List<String> untlst = new ArrayList<String>();
			for (String unit : units) {
				untlst.add(unit);
			}
			ec.setUnits(untlst);
		}
		return ec;
	}
//
	private boolean statementIsComplete(SadlModelElement element) {
	    Iterable<XtextSyntaxDiagnostic> syntaxErrors = Iterables.<XtextSyntaxDiagnostic>filter(element.eResource().getErrors(), XtextSyntaxDiagnostic.class);
		if (syntaxErrors.iterator().hasNext()) {
			ICompositeNode node = NodeModelUtils.findActualNodeFor(element);
			if (node.getSyntaxErrorMessage() == null) {
				return true;
			}
			return false;
		}
//	    final ICompositeNode node = NodeModelUtils.findActualNodeFor(element);
//        if ((node != null)) {
//          final INode lastChild = node.getLastChild();
//          if ((lastChild != null)) {
//            final EObject grammarElement = lastChild.getGrammarElement();
//            if ((grammarElement instanceof RuleCall)) {
//              AbstractRule _rule = ((RuleCall)grammarElement).getRule();
//              final ParserRule EOS = this.grammarAccess.getEOSRule();
//              boolean _tripleEquals = (_rule == EOS);  //_rule.getName().equals(EOS.getName());
//              String _text = node.getText();
//              if (_tripleEquals) {
//                String _plus = ("SADL statement is complete: " + _text.trim());
//                InputOutput.<String>println(_plus);
//                return true;
//              }
//              else {
//                  String _plus = ("SADL statement is NOT complete: " + _text.trim());
//                  InputOutput.<String>println(_plus);
//              }
//            }
//          }
//        }
		return true;
	}

	private void initializeDialogContent() throws ConversationException, IOException {
		Resource resource = getCurrentResource();
		AnswerCurationManager cm = getAnswerCurationManager();
		DialogContent dc = new DialogContent(resource, cm);
		cm.setConversation(dc);
	}

	public AnswerCurationManager getAnswerCurationManager() throws IOException {
		if (answerCurationManager == null) {
			Object cm = getConfigMgr().getPrivateKeyValuePair(DialogConstants.ANSWER_CURATION_MANAGER);
			if (cm != null) {
				if (cm instanceof AnswerCurationManager) {
					answerCurationManager  = (AnswerCurationManager) cm;
				}
			}
			else {
				Map<String, String> pmap = null;
				Resource resource = Preconditions.checkNotNull(getCurrentResource(), "resource");
				try {
					IDialogAnswerProvider dap = getDialogAnswerProvider(resource);
					if (dap != null) {
						pmap = dap.getPreferences(resource.getURI());
					}
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				answerCurationManager = new AnswerCurationManager(getConfigMgr().getModelFolder(), getConfigMgr(),
						(XtextResource) resource, pmap);
				getConfigMgr().addPrivateKeyValuePair(DialogConstants.ANSWER_CURATION_MANAGER, answerCurationManager);
			}
		}
		return answerCurationManager;
	}
	
	private String getResponseFromSadlStatement(EObject stmt) {
		// TODO Auto-generated method stub
		String ans = "no";
		if (stmt instanceof SadlInstance) {
			SadlResource sr = ((SadlInstance)stmt).getInstance();
			ans = NodeModelUtils.getTokenText(NodeModelUtils.getNode(stmt));
			int i = 0;
		}
		else if (stmt instanceof YesNoAnswerStatement) {
			ans = ((YesNoAnswerStatement)stmt).getAnswer();
		}
		if (ans.substring(0, 1).equalsIgnoreCase("y")) {
			return "yes";
		}
		else {
			return "no";
		}
	}

	private IDialogAnswerProvider getDialogAnswerProvider(Resource resource) throws ConfigurationException {
		Object dap = getConfigMgr(resource, null).getPrivateKeyValuePair(DialogConstants.DIALOG_ANSWER_PROVIDER);
		if (dap instanceof IDialogAnswerProvider) {
			return (IDialogAnswerProvider)dap;
		}
		return null;
	}

	private void autoSaveModel(Resource resource, String modelFolder, File saveFile, ProcessorContext context) throws IOException, URISyntaxException {
		String format = getOwlModelFormat(context);
		RDFWriter w = getTheJenaModel().getWriter(format);
		w.setProperty("xmlbase", getModelName());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(getTheJenaModel().getBaseModel(), out, getModelName());
		Charset charset = Charset.forName("UTF-8");
		CharSequence seq = new String(out.toByteArray(), charset);
		(new SadlUtils()).stringToFile(saveFile, String.valueOf(seq), false);

	
		List<String[]> newMappings = new ArrayList<String[]>();
		String[] mapping = new String[3];
		SadlUtils su = new SadlUtils();
		mapping[0] = su.fileNameToFileUrl(saveFile.getCanonicalPath());
		mapping[1] = getModelName();
		mapping[2] = getModelAlias();

		newMappings.add(mapping);

		// Output the Rules and any other knowledge structures via the specified
		// translator
		List<Object> otherContent = OntModelProvider.getOtherContent(resource);
		if (otherContent != null) {
			for (int i = 0; i < otherContent.size(); i++) {
				Object oc = otherContent.get(i);
				if (oc instanceof List<?>) {
					if (((List<?>) oc).get(0) instanceof Rule) {
						setRules((List<Rule>) oc);
					}
				}
			}
		}
		List<ModelError> results = translateAndSaveModel(resource, saveFile.getName(), format, newMappings, "Dialog");
		if (results != null) {
			generationInProgress = false; // we need these errors to show up
			modelErrorsToOutput(resource, results);
		}
}

	private File getModelFile(Resource resource) {
		if (getMetricsProcessor() instanceof MetricsProcessor) {
			String mfp = ((MetricsProcessor) getMetricsProcessor()).getModelFolderPath(resource);
			String name = resource.getURI().lastSegment();
			String saveFN = mfp + "/" + name + ".owl";
			File saveFile = new File(saveFN);
			return saveFile;
		}
		return null;
	}

	
	private void autoSaveModel(Resource rsrc) {
		URI uri = rsrc.getURI();
		Path trgtpath;
		if (uri.isFile()) {
			trgtpath = new File(rsrc.getURI().toFileString()).toPath();
		}
		else {
			IFile trgtfile = getFile(rsrc);
			trgtpath = trgtfile.getLocation().toFile().toPath();
		}
		try {
			URI absuri = URI.createURI(trgtpath.toUri().toString());
			String modelFolderUri = ResourceManager.findModelFolderPath(absuri);
			getTheJenaModel();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {

		}
	}

	public static IFile getFile(Resource resource) {
		ResourceSet rset = resource.getResourceSet();
// TODO how to get the IFile from the Resource??
		IFile retFile = null;
//		return getFile(resource.getURI(), (rset != null) ? rset.getURIConverter() : null, false);
		return retFile;
	}

	@Override
	public void onGenerate(Resource resource, IFileSystemAccess2 fsa, ProcessorContext context) {
		if (!resource.getURI().toString().endsWith(".dialog")) {
			return;
		}
		super.onGenerate(resource, fsa, context);
		try {
			getAnswerCurationManager().saveQuestionsAndAnswersToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getOwlFilename(URI lastSeg, String format) {
		String owlFN = lastSeg.appendFileExtension(ResourceManager.getOwlFileExtension(format))
				.lastSegment().toString();
		return owlFN;
	}

	private void resetProcessor() {
		// TODO Auto-generated method stub
		
	}
	
//	private ConversationElement processUserInputElement(EObject element) {
//		ConversationElement ce = null;
//		try {
//			if (element instanceof ModifiedAskStatement) {
////				ce = processStatement((ModifiedAskStatement)element);
//			}
//			else if (element instanceof WhatStatement) {
//				ce = processStatement((WhatStatement)element);
//			}
//			else if (element instanceof HowManyValuesStatement) {
//				ce = processStatement((HowManyValuesStatement)element);
//			}
//			else if (element instanceof SaveStatement) {
//				ce = processStatement((SaveStatement)element);
//			}
//			else {
//				throw new JenaProcessorException("onValidate for element of type '"
//						+ element.getClass().getCanonicalName() + "' not implemented");
//			}
//		} catch (JenaProcessorException e) {
//			addError(e.getMessage(), element);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ce;
//	}	
	
	private StatementContent  processAnswerCMStatement( AnswerCMStatement element) throws IOException, TranslationException, InvalidNameException, InvalidTypeException, ConfigurationException, JenaProcessorException, QueryParseException, QueryCancelledException, ReasonerNotFoundException, PrefixNotFoundException {
		EObject stmt = element.getSstmt();
		if (stmt != null) {
			StatementContent sc = processDialogModelElement((SadlModelElement) stmt);
			AnswerContent ac = new AnswerContent(element, Agent.CM);
			ac.setAnswer(sc);
			return ac;
		}
		else {
			String str = element.getStr();
			if (str != null) {
				String eos = getEos(element);
				if (eos.equals(".")) {
					AnswerContent ac = new AnswerContent(element, Agent.CM);
					ac.setAnswer(str);
					return ac;
				}
				else if (eos.equals("?")) {
					QuestionWithCallbackContent qwcc = new QuestionWithCallbackContent(element, Agent.CM, null, null, str);
					return qwcc;
				}
				else {
					throw new IOException("Statement has unexpected ending character.");
				}
			}
		}
		return null;
	}

	private StatementContent processStatement(SaveStatement element) throws IOException, ConfigurationException, QueryParseException, QueryCancelledException, ReasonerNotFoundException {
		SadlResource equationSR = ((SaveStatement)element).getTarget();
		String targetModelUri = null;
		String targetModelUrl = null;
		String targetModelAlias = ((SaveStatement)element).getSaveTarget();
		Map<String, String[]> targetModelMap = getAnswerCurationManager().getTargetModelMap();
		if (targetModelMap == null || targetModelMap.size() < 1) {
			addError("No target models have been identified. Cannot identify a model into which to save.", element);
		}
		else {
			if (targetModelAlias != null) {
				String[] uris = targetModelMap.get(targetModelAlias);
				if (uris == null) {
					addError("Model with alias '" + targetModelAlias + "' not found in target models.", element);
				}
				else {
					targetModelUri = uris[0];
					targetModelUrl = uris[1];
				}
			}
			else {
				if (targetModelMap.size() > 1){
					addError("There are multiple target models identified; please specify which one to save to.", element);
				}
				else {
					String[] uris = targetModelMap.get(targetModelMap.keySet().iterator().next());
					targetModelUri = uris[0];
					targetModelUrl = uris[1];
				}
			}
			if (targetModelUri != null) {
				SaveContent sc = new SaveContent(element, Agent.USER);
				sc.setTargetModelAlias(targetModelAlias);
				if (element.getAll() != null && element.getAll().equals("all")) {
					sc.setSaveAll(true);
				}
				else {
					String equationUri = getDeclarationExtensions().getConceptUri(equationSR);
					Individual extractedModelInstance = getTheJenaModel().getIndividual(equationUri);
					if (extractedModelInstance == null) {
						addError("No equation with URI '" + equationUri + "' is found in current model.", equationSR);
						return null;
					}
					else if (extractedModelInstance.getNameSpace().equals(targetModelAlias)) {
						getAnswerCurationManager().notifyUser(getConfigMgr().getModelFolder(), "The equation with URI '" + equationUri + "' is already in the target model '" + targetModelAlias + "'", true);
					}
					sc.setSourceEquationUri(extractedModelInstance.getURI());
				}
				return sc;
			}
		}
		return null;
	}

	private ModifiedAskContent processStatement(ModifiedAskStatement stmt) {
		ConversationElement ce = null;
		try {
			SadlResource elementName = null; // element.getName();
			EList<NamedStructureAnnotation> annotations = null; // element.getAnnotations();
			boolean isGraph = stmt.getStart().equals("Graph");
			Query query = processQueryExpression(stmt, stmt.getExpr(), elementName, annotations, isGraph);
			if (stmt.getParameterizedValues() != null) {
				EList<Expression> rowvals = stmt.getParameterizedValues().getExplicitValues();
				List<Object> rowObjects = new ArrayList<Object>();
				for (Expression val : rowvals) {
					Object valObj = processExpression(val);
					rowObjects.add(valObj);
				}
				query.setParameterizedValues(rowObjects);
				query.setContext(stmt);
			}
			if (query.getKeyword() == null && (stmt.getStart().equalsIgnoreCase("ask") || stmt.getStart().equalsIgnoreCase("find"))) {
				query.setKeyword("select");
			}
//			System.out.println("ModifiedAskStatement: " + query.toDescriptiveString());
			ModifiedAskContent mac = new ModifiedAskContent(stmt, Agent.USER);
			mac.setQuery(query);
			return mac;
		} catch (CircularDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JenaProcessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return null;
	}

	private StatementContent processStatement(WhatStatement stmt) {
		EObject substmt = stmt.getStmt();
		if (substmt instanceof WhatIsStatement) {
			return processStatement((WhatIsStatement)substmt);
		}
		else if (substmt instanceof WhatValuesStatement) {
			return processStatement((WhatValuesStatement)substmt);
		}
		return null;
	}
	
	private StatementContent processStatement(WhatIsStatement stmt) {
		EObject whatIsTarget = stmt.getTarget();
		EObject when = stmt.getWhen();
		if (whatIsTarget == null) {
			// this is a request for user name
			
		}
//		if (whatIsTarget instanceof Declaration) {
//			whatIsTarget = ((Declaration)whatIsTarget).getType();
//			if (whatIsTarget instanceof SadlSimpleTypeReference) {
//				whatIsTarget = ((SadlSimpleTypeReference)whatIsTarget).getType();
//			}
//		}
//		Object trgtObj;
//		try {
//			trgtObj = processExpression(whatIsTarget);
////				System.out.println("WhatIsStatement target: " + trgtObj.toString());
//			if (trgtObj instanceof NamedNode) {
//				((NamedNode)trgtObj).setContext(stmt);
//			}
//			else if (trgtObj instanceof Junction) {
//				if (stmt.eContainer() instanceof WhatIsStatement) {
//					setGraphPatternContext((WhatStatement) stmt.eContainer(), whatIsTarget, trgtObj);
//				}
//			}
//			else if (trgtObj instanceof TripleElement) {
//				((TripleElement)trgtObj).setContext(stmt);
//			}
//			else if (trgtObj instanceof Object[]) {
//				for (int i = 0; i < ((Object[])trgtObj).length; i++) {
//					Object obj = ((Object[])trgtObj)[i];
//					if (stmt.eContainer() instanceof WhatIsStatement) {
//						setGraphPatternContext((WhatStatement) stmt, whatIsTarget, obj);
//					}
//				}
//			}
//			else {
//				// TODO
//				addInfo(trgtObj.getClass().getCanonicalName() + " not yet handled by dialog processor", whatIsTarget);
//			}
//			Object whenObj = when != null ? processExpression(when) : null;
//			
//			// apply implied/expanded properties
//			DialogIntermediateFormTranslator dift = new DialogIntermediateFormTranslator(this, getTheJenaModel());
//			if (trgtObj instanceof GraphPatternElement) {
//				trgtObj = dift.addImpliedAndExpandedProperties((GraphPatternElement)trgtObj);
//			}
//			else if (trgtObj instanceof List<?>) {
//				dift.addImpliedAndExpandedProperties((List<GraphPatternElement>) trgtObj);
//			}
//			if (whenObj instanceof GraphPatternElement) {
//				whenObj = dift.addImpliedAndExpandedProperties((GraphPatternElement)whenObj);
//				List<GraphPatternElement> gpes = new ArrayList<GraphPatternElement>();
//				gpes = unitSpecialConsiderations(gpes, whenObj, whatIsTarget);
//				Object temp = dift.cook(gpes, false);
//				if (temp instanceof List<?>) {
//					if (((List<?>)temp).size() == 1) {
//						whenObj = ((List<?>)temp).get(0);
//					}
//					else {
//						// ?
//					}
//				}
//			}
//			else if (whenObj instanceof List<?>) {
//				dift.addImpliedAndExpandedProperties((List<GraphPatternElement>) whenObj);
//				// does this ever happen? More to do...
//			}
		List<Rule> comparisonRules;
		try {
			comparisonRules = whenAndThenToCookedRules(when, whatIsTarget);
			if (comparisonRules != null) {
				WhatIsContent wic = new WhatIsContent(stmt.eContainer(), Agent.USER, comparisonRules);
				return wic;
			}
		} catch (InvalidNameException | InvalidTypeException | TranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		} catch (TranslationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidNameException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidTypeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}

	private List<GraphPatternElement> unitSpecialConsiderations(List<GraphPatternElement> gpes, Object whenObj, EObject whatIsTarget)
			throws TranslationException, InvalidNameException {
		if (!ignoreUnittedQuantities) {
			if (whenObj instanceof Junction) {
				Object lhs = ((Junction)whenObj).getLhs();
				if (lhs instanceof ProxyNode && ((ProxyNode)lhs).getProxyFor() instanceof GraphPatternElement) {
					gpes = unitSpecialConsiderations(gpes, ((ProxyNode) lhs).getProxyFor(), whatIsTarget);
				}
				Object rhs = ((Junction)whenObj).getRhs();
				if (rhs instanceof ProxyNode && ((ProxyNode)rhs).getProxyFor() instanceof GraphPatternElement) {
					gpes = unitSpecialConsiderations(gpes, ((ProxyNode) rhs).getProxyFor(), whatIsTarget);
				}
			}
			else if (whenObj instanceof BuiltinElement) {
				List<Node> args = ((BuiltinElement)whenObj).getArguments();
				for (Node arg : args) {
					if (arg instanceof Literal && ((Literal)arg).getUnits() != null) { 		// this operand to the || is for when the unit of a UnittedQuanity is given
						OntClass unittedQuantitySubclass = getTheJenaModel().getOntClass(SadlConstants.SADL_IMPLICIT_MODEL_UNITTEDQUANTITY_URI);
						VariableNode var = new VariableNode(getNewVar(whatIsTarget));
						NamedNode type = new NamedNode(unittedQuantitySubclass.getURI());
						type.setNodeType(NodeType.ClassNode);
						var.setType(validateNode(type));
						Literal valueLiteral = (Literal) arg;
						int idx = args.indexOf(arg);
						args.set(idx, var);
						String units = valueLiteral.getUnits();
						TripleElement varTypeTriple = new TripleElement(var, new RDFTypeNode(), type);
						NamedNode valueProp = new NamedNode(SadlConstants.SADL_IMPLICIT_MODEL_VALUE_URI);
						valueProp.setNodeType(NodeType.DataTypeProperty);
						TripleElement valueTriple = new TripleElement(var, valueProp, valueLiteral);
						gpes.add(varTypeTriple);
						gpes.add(valueTriple);
						if (units != null) {
							Literal unitsLiteral = new Literal();
							unitsLiteral.setValue(units);
							valueLiteral.setUnits(null);
							NamedNode unitProp = new NamedNode(SadlConstants.SADL_IMPLICIT_MODEL_UNIT_URI);
							unitProp.setNodeType(NodeType.DataTypeProperty);
							TripleElement unitTriple = new TripleElement(var, unitProp, unitsLiteral);
							gpes.add(unitTriple);
						}	
					}
					else if ((arg instanceof ProxyNode &&									// this operand to the || is for when no unit is given but there is an implied property and 
							// value of the UnittedQuantity is given
							((ProxyNode)arg).getProxyFor() instanceof TripleElement &&
							((TripleElement)((ProxyNode)arg).getProxyFor()).getPredicate() instanceof NamedNode &&
							((NamedNode)((TripleElement)((ProxyNode)arg).getProxyFor()).getPredicate()).getURI().equals(SadlConstants.SADL_IMPLICIT_MODEL_VALUE_URI))) {
						
					}
				}
				gpes.add((GraphPatternElement) whenObj);
			}
			else if (whenObj instanceof TripleElement && 
				((((TripleElement)whenObj).getObject() instanceof Literal &&
				((Literal)(((TripleElement)whenObj).getObject())).getUnits() != null) ||			// this operand to the || is for when the unit of a UnittedQuanity is given
				(((TripleElement)whenObj).getObject() instanceof ProxyNode &&		// this operand to the || is for when no unit is given but there is an implied property and 
																					// value of the UnittedQuantity is given
						((ProxyNode)((TripleElement)whenObj).getObject()).getProxyFor() instanceof TripleElement &&
						((TripleElement)((ProxyNode)((TripleElement)whenObj).getObject()).getProxyFor()).getPredicate() instanceof NamedNode &&
				((NamedNode)((TripleElement)((ProxyNode)((TripleElement)whenObj).getObject()).getProxyFor()).getPredicate()).getURI().equals(SadlConstants.SADL_IMPLICIT_MODEL_VALUE_URI)))) {
				String propUri = ((TripleElement)whenObj).getPredicate().getURI();
				// create a typed variable for the UnittedQuantity blank node, actual type range of prop
				Property prop = getTheJenaModel().getProperty(propUri);
				StmtIterator rngItr = getTheJenaModel().listStatements(prop.asResource(), RDFS.range, (RDFNode)null);
				OntClass unittedQuantitySubclass = null;
				if (rngItr.hasNext()) {
					RDFNode rng = rngItr.nextStatement().getObject();
					if (!rngItr.hasNext()) {
						if (rng.isURIResource() && rng.canAs(OntClass.class)) {
							unittedQuantitySubclass = rng.as(OntClass.class);
						}
					}
					if (unittedQuantitySubclass == null) {
						// apparently has more than 1 range, use UnittedQuantity
						unittedQuantitySubclass = getTheJenaModel().getOntClass(SadlConstants.SADL_IMPLICIT_MODEL_UNITTEDQUANTITY_URI);
					}
				}
				VariableNode var = new VariableNode(getNewVar(whatIsTarget));
				NamedNode type = new NamedNode(unittedQuantitySubclass.getURI());
				type.setNodeType(NodeType.ClassNode);
				var.setType(validateNode(type));
				Literal valueLiteral;
				if (((TripleElement)whenObj).getObject() instanceof Literal) {
					valueLiteral = (Literal) ((TripleElement)whenObj).getObject();
				}
				else {
					valueLiteral = (Literal) ((TripleElement)((ProxyNode)((TripleElement)whenObj).getObject()).getProxyFor()).getObject();
				}
				((TripleElement)whenObj).setObject(var);
				String units = valueLiteral.getUnits();
				TripleElement varTypeTriple = new TripleElement(var, new RDFTypeNode(), type);
				TripleElement valueTriple = new TripleElement(var, 
						new NamedNode(SadlConstants.SADL_IMPLICIT_MODEL_VALUE_URI), valueLiteral);
				gpes.add((TripleElement)whenObj);
				gpes.add(varTypeTriple);
				gpes.add(valueTriple);
				if (units != null) {
					Literal unitsLiteral = new Literal();
					unitsLiteral.setValue(units);
					valueLiteral.setUnits(null);
					TripleElement unitTriple = new TripleElement(var, 
							new NamedNode(SadlConstants.SADL_IMPLICIT_MODEL_UNIT_URI), unitsLiteral);
					gpes.add(unitTriple);
				}
			}
			//				else if (!ignoreUnittedQuantities && whenObj instanceof BuiltinElement &&
			//						((BuiltinElement)whenObj).getFuncType().equals(BuiltinType.Equal) &&
			//						((BuiltinElement)whenObj).getArguments().get(1) instanceof Literal &&
			//						((Literal)((BuiltinElement)whenObj).getArguments().get(1)).getUnits() != null) {
			//					Literal val = (Literal)((BuiltinElement)whenObj).getArguments().get(1);
			//					String units = val.getUnits();
			//					val.setUnits(null);
			//					Literal unitsLiteral = new Literal();
			//					unitsLiteral.setValue(units);
			//					TripleElement valueTriple = new TripleElement(((BuiltinElement)whenObj).getArguments().get(0), 
			//							new NamedNode(SadlConstants.SADL_IMPLICIT_MODEL_VALUE_URI), val);
			//					TripleElement unitTriple = new TripleElement(((BuiltinElement)whenObj).getArguments().get(0), 
			//							new NamedNode(SadlConstants.SADL_IMPLICIT_MODEL_UNIT_URI), unitsLiteral);
			//					gpes.add(valueTriple);
			//					gpes.add(unitTriple);
			//				}
			else {
				gpes.add((GraphPatternElement) whenObj);
			}
		}
		else {
			gpes.add((GraphPatternElement) whenObj);
		}
		return gpes;
	}
	
	private StatementContent processStatement(WhatValuesStatement stmt) {
		String article = stmt.getArticle();
		SadlTypeReference cls = stmt.getCls();
		SadlResource prop = stmt.getProp();
		String typ = stmt.getTyp(); 		// "can" or "must"
		try {
			Object clsObj = processExpression(cls);
			Object propObj = processExpression(prop);
//				System.out.println("WhatValuesStatement(" + typ + "): cls=" + (article!= null ? article : "") + 
//						" '" + clsObj.toString() + "'; prop='" + propObj.toString() + "'");
			WhatValuesContent wvc = new WhatValuesContent(stmt.eContainer(), Agent.USER);
			wvc.setArticle(article);
			wvc.setCls(nodeCheck(clsObj));
			wvc.setProp(nodeCheck(propObj));
			wvc.setTypeof(typ);
			return wvc;
		} catch (TranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to set the contextual EObject of a GraphPatternElement
	 * @param stmt
	 * @param whatIsTarget
	 * @param obj
	 */
	private void setGraphPatternContext(WhatStatement stmt, EObject whatIsTarget, Object obj) {
		if (obj instanceof TripleElement) {
			((TripleElement)obj).setContext(stmt.getStmt());
		}
		else if (obj instanceof Junction) {
			setGraphPatternContext(stmt, whatIsTarget, ((ProxyNode)((Junction)obj).getLhs()).getProxyFor());
			setGraphPatternContext(stmt, whatIsTarget, ((ProxyNode)((Junction)obj).getRhs()).getProxyFor());;
		}
		else {
			addInfo(obj.getClass().getCanonicalName() + " in array not yet handled by dialog processor", whatIsTarget);
		}
	}

	private StatementContent processStatement(HowManyValuesStatement stmt) throws IOException {
		String article = stmt.getArticle();
		SadlTypeReference cls = stmt.getCls();
		SadlResource prop = stmt.getProp();
		SadlTypeReference typ = stmt.getTyp(); 
		try {
			Object clsObj = processExpression(cls);
			Object propObj = processExpression(prop);
			Object typObj = null;
			if (typ != null) {
				typObj = processExpression(typ);
			}
//			System.out.println("HowManyValuesStatement: cls=" + (article!= null ? article : "") + " '" + 
//					clsObj.toString() + "'; prop='" + propObj.toString() + 
//					"'" + (typObj != null ? ("; type='" + typObj.toString() + "'") : ""));
			HowManyValuesContent hmvc = new HowManyValuesContent(stmt, Agent.USER);
			hmvc.setProp(nodeCheck(propObj));
			hmvc.setTyp(nodeCheck(typObj));
			hmvc.setArticle(article);
			hmvc.setCls(nodeCheck(clsObj));
			return hmvc;
		} catch (TranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected boolean isContainedByQuery(Expression expr) {
		if (EcoreUtil2.getContainerOfType(expr, QueryStatement.class) != null) {
			return true;
		}
		else if (EcoreUtil2.getContainerOfType(expr, ModifiedAskStatement.class)!= null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isSupported(String fileExtension) {
		return "dialog".equals(fileExtension);
	}

	private boolean isModelChanged() {
		return modelChanged;
	}

	private void setModelChanged(boolean modelChanged) {
		this.modelChanged = modelChanged;
	}

	@Override
	public void initializePreferences(ProcessorContext context) {
		super.initializePreferences(context);
		setTypeCheckingWarningsOnly(true);
		setUseArticlesInValidation(true);
		setIncludeImpliedPropertiesInTranslation(true);
		String textServiceUrl = context.getPreferenceValues().getPreference(DialogPreferences.ANSWER_TEXT_SERVICE_BASE_URI);
		if (textServiceUrl != null) {
			setTextServiceUrl(textServiceUrl);
		}
		String useDbn = context.getPreferenceValues().getPreference(DialogPreferences.USE_DBN_CG_SERVICE);
		if (useDbn != null) {
			setUseDbn(Boolean.parseBoolean(useDbn.trim()));
		}
		String dbncgserviceurl = context.getPreferenceValues().getPreference(DialogPreferences.ANSWER_DBN_CG_SERVICE_BASE_URI);
		if (dbncgserviceurl != null) {
			setDbnCgServiceUrl(dbncgserviceurl);
		}
		String dbninputjsongenerationserviceurl = context.getPreferenceValues().getPreference(DialogPreferences.DBN_INPUT_JSON_GENERATION_SERVICE_BASE_URI);
		if (dbninputjsongenerationserviceurl != null) {
			setDbnInputJsonGenerationServiceUrl(dbninputjsongenerationserviceurl);
		}
		String kchaincgserviceurl = context.getPreferenceValues().getPreference(DialogPreferences.ANSWER_KCHAIN_CG_SERVICE_BASE_URI);
		if (kchaincgserviceurl != null) {
			setKchainCgServiceUrl(kchaincgserviceurl);
		}
		String useKchain = context.getPreferenceValues().getPreference(DialogPreferences.USE_ANSWER_KCHAIN_CG_SERVICE);
		if (useKchain != null) {
			setUseKchain(Boolean.parseBoolean(useKchain.trim()));
		}
//		System.out.println(textserviceurl);
//		System.out.println(cgserviceurl);
	}

	public String getTextServiceUrl() {
		return textServiceUrl;
	}

	private void setTextServiceUrl(String textServiceUrl) {
		this.textServiceUrl = textServiceUrl;
	}

	public String getDbnCgServiceUrl() {
		return dbnCgServiceUrl;
	}

	private void setDbnCgServiceUrl(String cgServiceUrl) {
		this.dbnCgServiceUrl = cgServiceUrl;
	}

	private void setDbnInputJsonGenerationServiceUrl(String dbninputjsongenerationserviceurl) {
		this.dbninputjsongenerationserviceurl = dbninputjsongenerationserviceurl;
		
	}
	
	public String getDbnInputJsonGenerationServiceUrl() {
		return dbninputjsongenerationserviceurl;
	}

	public String getKchainCgServiceUrl() {
		return kchainCgServiceUrl;
	}

	private void setKchainCgServiceUrl(String cgServiceUrl) {
		this.kchainCgServiceUrl = cgServiceUrl;
	}

	private java.nio.file.Path checkCodeExtractionSadlModelExistence(Resource resource, ProcessorContext context)
			throws IOException, ConfigurationException, URISyntaxException, JenaProcessorException {
		UtilsForJena ufj = new UtilsForJena();
		String policyFileUrl = ufj.getPolicyFilename(resource);
		String policyFilename = policyFileUrl != null ? ufj.fileUrlToFileName(policyFileUrl) : null;
		if (policyFilename != null) {
			File projectFolder = new File(policyFilename).getParentFile().getParentFile();
			String relPath = DialogConstants.EXTRACTED_MODELS_FOLDER_PATH_FRAGMENT + "/" + DialogConstants.CODE_EXTRACTION_MODEL_FILENAME;
			String platformPath = projectFolder.getName() + "/" + relPath;
			String codeExtractionSadlModelFN = projectFolder + "/" + relPath;
			File codeExtractionSadlModelFile = new File(codeExtractionSadlModelFN);
			if (!codeExtractionSadlModelFile.exists()) {
				createCodeExtractionSadlModel(codeExtractionSadlModelFile);
				try {
					Resource newRsrc = resource.getResourceSet()
							.getResource(URI.createPlatformResourceURI(platformPath, false), true);
					if (EMFPlugin.IS_ECLIPSE_RUNNING) {
						Job.create("Refreshing " + newRsrc.getURI().lastSegment(), (ICoreRunnable) monitor -> {
							IPath path = org.eclipse.core.runtime.Path.fromPortableString(newRsrc.getURI().toPlatformString(true));
							IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
							if (file.isAccessible()) {
								file.getProject().build(IncrementalProjectBuilder.FULL_BUILD, monitor);
							} else {
								System.err.println("File " + file + " is not accessible.");
							}
							
						}).schedule();
					}
				} catch (Throwable t) {
				}
			}
			return codeExtractionSadlModelFile.getAbsoluteFile().toPath();
		}
		return null;
	}

	private void createCodeExtractionSadlModel(File cemf) throws IOException {
		String content = getCodeExtractionModel();
		if (!cemf.exists()) {
			cemf.getParentFile().mkdirs();
			new SadlUtils().stringToFile(cemf, content, false);
		}
	}

	private String getCodeExtractionModel() {
		String content = "uri \"http://sadl.org/CodeExtractionModel.sadl\" alias cem.\r\n" + 
				" \r\n" + 
				"// This is the code extraction meta-model\r\n" + 
				"CodeElement is a class described by beginsAt with a single value of type int,\r\n" + 
				"	described by endsAt with a single value of type int.\r\n" + 
				"\r\n" + 
				"CodeBlock is a type of CodeElement,\r\n" + 
				"	described by serialization with a single value of type string,\r\n" + 
				"	described by comment with values of type Comment,\r\n" + 
				"	described by containedIn with values of type CodeBlock.\r\n" + 
				"\r\n" + 
				"{Class, Method, ConditionalBlock, LoopBlock} are types of CodeBlock.\r\n" + 
				"\r\n" + 
				"cmArguments describes Method with a single value of type CodeVariable List.\r\n" + 
				"cmReturnTypes describes Method with a single value of type string List.\r\n" + 
				"cmSemanticReturnTypes describes Method with a single value of type string List.\r\n" + 
				"doesComputation describes Method with a single value of type boolean.\r\n" + 
				"incompleteInformation describes Method with a single value of type boolean.\r\n" + 
				"calls describes Method with values of type MethodCall.\r\n" + 
				"ExternalMethod is a type of Method.\r\n" + 
				"\r\n" + 
				"// The reference to a CodeVariable can be its definition (Defined),\r\n" + 
				"//	an assignment or reassignment (Reassigned), or just a reference\r\n" + 
				"//	in the right-hand side of an assignment or a conditional (Used)\r\n" + 
				"Usage is a class, must be one of {Defined, Used, Reassigned}.\r\n" + 
				"\r\n" + 
				"Reference  is a type of CodeElement\r\n" + 
				"	described by firstRef (note \"first reference in this CodeBlock\") \r\n" + 
				"		with a single value of type boolean\r\n" + 
				"	described by codeBlock with a single value of type CodeBlock\r\n" + 
				"	described by usage with values of type Usage\r\n" + 
				" 	described by cem:input (note \"CodeVariable is an input to codeBlock CodeBlock\") \r\n" + 
				" 		with a single value of type boolean\r\n" + 
				" 	described by output (note \"CodeVariable is an output of codeBlock CodeBlock\") \r\n" + 
				" 		with a single value of type boolean\r\n" + 
				" 	described by isImplicit (note \"the input or output of this reference is implicit (inferred), not explicit\")\r\n" + 
				" 		with a single value of type boolean\r\n" + 
				" 	described by setterArgument (note \"is this variable input to a setter?\") with a single value of type boolean\r\n" + 
				" 	described by comment with values of type Comment.\r\n" + 
				" 	\r\n" + 
				"MethodCall is a type of CodeElement\r\n" + 
				"	described by codeBlock with a single value of type CodeBlock\r\n" + 
				"	described by inputMapping with values of type InputMapping,\r\n" + 
				"	described by returnedMapping with values of type OutputMapping.\r\n" + 
				"MethodCallMapping is a class,\r\n" + 
				"	described by callingVariable with a single value of type CodeVariable,\r\n" + 
				"	described by calledVariable with a single value of type CodeVariable.\r\n" + 
				"{InputMapping, OutputMapping} are types of MethodCallMapping.		\r\n" + 
				"	\r\n" + 
				"Comment (note \"CodeBlock and Reference can have a Comment\") is a type of CodeElement\r\n" + 
				" 	described by commentContent with a single value of type string.	\r\n" + 
				"\r\n" + 
				"// what about Constant also? Note something maybe an input and then gets reassigned\r\n" + 
				"// Constant could be defined in terms of being set by equations that only involve Constants\r\n" + 
				"// Constants could also relate variables used in different equations as being same\r\n" + 
				"CodeVariable  is a type of CodeElement, \r\n" + 
				"	described by varName with a single value of type string,\r\n" + 
				"	described by varType with a single value of type string,\r\n" + 
				"	described by semanticVarType with a single value of type string,\r\n" + 
				"	described by quantityKind (note \"this should be qudt:QuantityKind\") with a single value of type ScientificConcept,\r\n" + 
				"	described by reference with values of type Reference.   \r\n" + 
				"\r\n" + 
				"{ClassField, MethodArgument, MethodVariable, ConstantVariable} are types of CodeVariable. 	\r\n" + 
				"\r\n" +
				"constantValue describes ConstantVariable with values of type UnittedQuantity.\r\n" + 
				"//External findFirstLocation (CodeVariable cv) returns int: \"http://ToBeImplemented\".\r\n" + 
				"\r\n" + 
				"Rule Transitive  \r\n" + 
				"if inst is a cls and \r\n" + 
				"   cls is a type of CodeVariable\r\n" + 
				"then inst is a CodeVariable. \r\n" + 
				"\r\n" + 
				"Rule Transitive2  \r\n" + 
				"if inst is a cls and \r\n" + 
				"   cls is a type of CodeBlock\r\n" + 
				"then inst is a CodeBlock. \r\n" + 
				"\r\n" + 
				"Rule FindFirstRef\r\n" + 
				"if c is a CodeVariable and\r\n" + 
				"   ref is reference of c and\r\n" + 
				"   ref has codeBlock cb and\r\n" + 
				"   l is beginsAt of ref and\r\n" + 
				"   minLoc = min(c, reference, r, r, codeBlock, cb, r, beginsAt) and\r\n" + 
				"   l = minLoc\r\n" + 
				"then firstRef of ref is true\r\n" + 
				"//	and print(c, \" at \", minLoc, \" is first reference.\")\r\n" + 
				".\r\n" + 
				"\r\n" + 
				"Rule ImplicitInput\r\n" + 
				"if cb is a CodeBlock and\r\n" + 
				"   ref has codeBlock cb and\r\n" + 
				"   ref has firstRef true and\r\n" + 
				"   ref has usage Used\r\n" + 
				"   and cv has reference ref\r\n" + 
				"//   and ref has beginsAt loc\r\n" + 
				"then input of ref is true and isImplicit of ref is true\r\n" + 
				"//	and print(cb, cv, loc, \" implicit input\")\r\n" + 
				".\r\n" + 
				"\r\n" + 
				"Rule ImplicitOutput\r\n" + 
				"if cb is a CodeBlock and\r\n" + 
				"   ref has codeBlock cb and\r\n" + 
				"   ref has firstRef true and\r\n" + 
				"   ref has usage Reassigned\r\n" + 
				"   and cv has reference ref\r\n" + 
				"   and noValue(cv, reference, ref2, ref2, codeBlock, cb, ref2, usage, Defined)\r\n" + 
				"//   and ref has beginsAt loc\r\n" + 
				"then output of ref is true and isImplicit of ref is true\r\n" + 
				"//	and print(cb, cv, loc, \" implicit output\")\r\n" + 
				"." + 
				"\r\n" + 
				"ClassesToIgnore is a type of Class.\r\n" + 
				"{Canvas, CardLayout, Graphics, Insets, Panel, Image, cem:Event, Choice, Button,\r\n" + 
				"	Viewer, GridLayout\r\n" + 
				"} are types of ClassesToIgnore.\r\n" + 
				"\r\n" + 
				"Ask ImplicitMethodInputs: \"select distinct ?m ?cv ?vt ?vn where {?r <isImplicit> true . ?r <http://sadl.org/CodeExtractionModel.sadl#input> true . \r\n" + 
				"	?r <codeBlock> ?m . ?cv <reference> ?r . ?cv <varType> ?vt . ?cv <varName> ?vn} order by ?m ?vn\".\r\n" + 
				"Ask ImplicitMethodOutputs: \"select distinct ?m ?cv ?vt ?vn where {?r <isImplicit> true . ?r <http://sadl.org/CodeExtractionModel.sadl#output> true . \r\n" + 
				"	?r <codeBlock> ?m . ?cv <reference> ?r . ?cv <varType> ?vt. ?cv <varName> ?vn} order by ?m ?vn\".\r\n" + 
				"Ask MethodsDoingComputation: \"select ?m where {?m <doesComputation> true}\".\r\n" + 
				"Ask MethodCalls: \"select ?m ?mcalled where {?m <calls> ?mc . ?mc <codeBlock> ?mcalled} order by ?m ?mcalled\".";
		return content;
	}

	private boolean isUseDbn() {
		return useDbn;
	}

	private void setUseDbn(boolean useDbn) {
		this.useDbn = useDbn;
	}

	private boolean isUseKchain() {
		return useKchain;
	}

	private void setUseKchain(boolean useKchain) {
		this.useKchain = useKchain;
	}

}
