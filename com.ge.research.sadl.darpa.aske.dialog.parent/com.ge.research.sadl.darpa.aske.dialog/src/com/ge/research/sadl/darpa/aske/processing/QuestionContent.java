package com.ge.research.sadl.darpa.aske.processing;

import org.eclipse.emf.ecore.EObject;

import com.ge.research.sadl.darpa.aske.curation.AnswerCurationManager.Agent;

public class QuestionContent extends ExpectsAnswerContent {
	public QuestionContent(EObject host) {
		super(host);
	}
	
	public QuestionContent(EObject host, Agent agnt) {
		super(host, agnt);
	}

	public QuestionContent(EObject host, Agent agnt, String uptxt) {
		super(host, agnt, uptxt);
	}
	

}
