package com.ge.research.sadl.darpa.aske.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File
import com.ge.research.sadl.reasoner.ConfigurationManagerFactory
import org.eclipse.xtext.diagnostics.Severity
import com.ge.research.sadl.darpa.aske.processing.JenaBasedDialogModelProcessor
import com.ge.research.sadl.darpa.aske.processing.WhatIsContent
import com.ge.research.sadl.builder.ConfigurationManagerForIdeFactory
import java.io.IOException
import java.io.BufferedReader
import java.io.FileReader
import org.junit.Ignore

public class P2Tests extends AbstractDialogTest {
	private String kbRoot;	
	private String projectModelFolder;

	@Before
	def void setUp() throws Exception {
		val projectRoot = new File("resources/ASKE_P2");
		setKbRoot(projectRoot.getCanonicalPath());
		val prjFolder = new File(getKbRoot());
		assertTrue(prjFolder.exists());
		setProjectModelFolder(getKbRoot() + "/OwlModels");
	}
	
	def setKbRoot(String string) {
		kbRoot = string
	}
	
	def String getKbRoot() {
		return kbRoot
	}

	def String getProjectModelFolder() {
		return projectModelFolder;
	}


	def void setProjectModelFolder(String projectModelFolder) {
		this.projectModelFolder = projectModelFolder;
	}

	@Ignore("this test is obsolete")
	@Test
	def void test() {
		reusableScienceKnowledge
		reusableCompGraphModel
		reusableModelMeta
		reusableHypersonics_v2
		reusableDBN
		reusableTableAndEquation
		reusableTurbo
		'''
		uri "http://sadl.org/test4.dialog" alias test4dialog.
		 
		import "http://aske.ge.com/metamodel".
		import "http://aske.ge.com/hypersonicsV2".
		import "http://aske.ge.com/dbnnodes".
		import "http://sadl.org/TableAndEquation.sadl".
		import "http://aske.ge.com/turbo".
		
		what is the machSpeed of a CF6 when the speed of the CF6 is 400 mph and the altitude of the CF6 is 20000 ft?
		
		what is the machSpeed of a CF6 when the speed is 400 mph and the altitude is 20000 ft?
		'''.assertValidatesTo [ ontModel, issues, processor |
			assertNotNull(ontModel)
//			val stmtitr = ontModel.listStatements()
//			while (stmtitr.hasNext) {
//				println(stmtitr.nextStatement)
//			}
			val errors = issues.filter[severity === Severity.ERROR]
			assertEquals(0, errors.size)
			if (processor instanceof JenaBasedDialogModelProcessor) {
				val jbdp = (processor as JenaBasedDialogModelProcessor)
				val resource = jbdp.currentResource
				val acm = jbdp.getAnswerCurationManager(resource)
				val conversation = acm.conversation
				assertNotNull(conversation)
				assertNotNull(conversation.statements);
				assertEquals(2, conversation.statements.size)
				assertTrue(conversation.statements.get(0).statement instanceof WhatIsContent)
				assertTrue(conversation.statements.get(1).statement instanceof WhatIsContent)
				val cc1 = conversation.statements.get(0).statement as WhatIsContent
				val cc2 = conversation.statements.get(1).statement as WhatIsContent
				assertNotNull(cc1)
				assertNotNull(cc2)
				val rules1 = cc1.getComputationalGraphRules
				val rules2 = cc2.getComputationalGraphRules
				assertNotNull(rules1)
				assertNotNull(rules2)
				assertEquals(rules1.size, rules2.size)
				for (var i = 0 ; i < rules1.size; i++) {
					val r1 = rules1.get(i)
					val r2 = rules2.get(i)
					println(r1.toFullyQualifiedString)
					println(r2.toFullyQualifiedString)
				}
//				assertEquals(4, rules.size)
//				for (var i = 0; i < 4; i++) {
//					assertEquals(grd.get(i), rules.get(i).toFullyQualifiedString)
//				}
			}
		]

	}

	def reusableModelMeta() {
		val filepath = getKbRoot + "/MetaModel.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
		model.assertNoErrors
	}
	
	def reusableHypersonics_v2() {
		val filepath = getKbRoot + "/Hypersonics_v2.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
		model.assertNoErrors
	}
	
	def reusableDBN() {
		val filepath = getKbRoot + "/DBN.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
		model.assertNoErrors
	}
	
	def reusableTableAndEquation() {
		val filepath = getKbRoot + "/TableAndEquation.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
		model.assertNoErrors
	}
	
	def reusableTurbo() {
		val filepath = getKbRoot + "/Turbo.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
//		model.assertNoErrors
	}
	
	def reusableCompGraphModel() {
		val filepath = getKbRoot + "/CompGraphModel.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
		model.assertNoErrors
	}

	def reusableScienceKnowledge() {
		val filepath = getKbRoot + "/ScienceKnowledge.sadl"
		val modelcontent = readFile(new File(filepath))
		val model = modelcontent.sadl
		model.assertNoErrors
	}

	def String readFile(File file) throws IOException {
	    val reader = new BufferedReader(new FileReader (file));
	    var         line = reader.readLine;
	    val  stringBuilder = new StringBuilder();
	    val         ls = System.getProperty("line.separator");

	    try {
	        while(line !== null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	            line = reader.readLine
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}

}
