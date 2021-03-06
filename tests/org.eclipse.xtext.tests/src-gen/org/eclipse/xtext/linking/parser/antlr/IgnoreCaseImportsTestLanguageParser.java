/*
 * generated by Xtext
 */
package org.eclipse.xtext.linking.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.linking.services.IgnoreCaseImportsTestLanguageGrammarAccess;

public class IgnoreCaseImportsTestLanguageParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private IgnoreCaseImportsTestLanguageGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected org.eclipse.xtext.linking.parser.antlr.internal.InternalIgnoreCaseImportsTestLanguageParser createParser(XtextTokenStream stream) {
		return new org.eclipse.xtext.linking.parser.antlr.internal.InternalIgnoreCaseImportsTestLanguageParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}
	
	public IgnoreCaseImportsTestLanguageGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(IgnoreCaseImportsTestLanguageGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
