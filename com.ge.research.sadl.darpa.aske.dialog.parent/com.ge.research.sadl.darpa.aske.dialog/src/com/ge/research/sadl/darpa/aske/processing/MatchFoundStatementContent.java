package com.ge.research.sadl.darpa.aske.processing;

import org.eclipse.emf.ecore.EObject;

import com.ge.research.sadl.darpa.aske.curation.AnswerCurationManager.Agent;

public class MatchFoundStatementContent extends InformationContent {
	private String type;

	public MatchFoundStatementContent(EObject host, String type, Agent agnt, String msg) {
		super(host, agnt, msg);
		setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
