/*
 * generated by Xtext
 */
grammar PsiInternalPartialSerializationTestLanguage;

options {
	superClass=AbstractPsiAntlrParser;
}

@lexer::header {
package org.eclipse.xtext.parsetree.reconstr.idea.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package org.eclipse.xtext.parsetree.reconstr.idea.parser.antlr.internal;

import org.eclipse.xtext.idea.parser.AbstractPsiAntlrParser;
import org.eclipse.xtext.parsetree.reconstr.idea.lang.PartialSerializationTestLanguageElementTypeProvider;
import org.eclipse.xtext.idea.parser.TokenTypeProvider;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parsetree.reconstr.services.PartialSerializationTestLanguageGrammarAccess;

import com.intellij.lang.PsiBuilder;
}

@parser::members {

	protected PartialSerializationTestLanguageGrammarAccess grammarAccess;

	protected PartialSerializationTestLanguageElementTypeProvider elementTypeProvider;

	public PsiInternalPartialSerializationTestLanguageParser(PsiBuilder builder, TokenStream input, PartialSerializationTestLanguageElementTypeProvider elementTypeProvider, PartialSerializationTestLanguageGrammarAccess grammarAccess) {
		this(input);
		setPsiBuilder(builder);
    	this.grammarAccess = grammarAccess;
		this.elementTypeProvider = elementTypeProvider;
	}

	@Override
	protected String getFirstRuleName() {
		return "Model";
	}

}

//Entry rule entryRuleModel
entryRuleModel returns [Boolean current=false]:
	{ markComposite(elementTypeProvider.getModelElementType()); }
	iv_ruleModel=ruleModel
	{ $current=$iv_ruleModel.current; }
	EOF;

// Rule Model
ruleModel returns [Boolean current=false]
:
	{
		markComposite(elementTypeProvider.getModel_NodeRootParserRuleCallElementType());
	}
	this_NodeRoot_0=ruleNodeRoot
	{
		$current = $this_NodeRoot_0.current;
		doneComposite();
	}
;

//Entry rule entryRuleNodeRoot
entryRuleNodeRoot returns [Boolean current=false]:
	{ markComposite(elementTypeProvider.getNodeRootElementType()); }
	iv_ruleNodeRoot=ruleNodeRoot
	{ $current=$iv_ruleNodeRoot.current; }
	EOF;

// Rule NodeRoot
ruleNodeRoot returns [Boolean current=false]
:
	(
		{
			markLeaf(elementTypeProvider.getNodeRoot_NumberSignDigitOneKeyword_0ElementType());
		}
		otherlv_0='#1'
		{
			doneLeaf(otherlv_0);
		}
		(
			(
				{
					markComposite(elementTypeProvider.getNodeRoot_NodeNodeParserRuleCall_1_0ElementType());
				}
				lv_node_1_0=ruleNode
				{
					doneComposite();
					if(!$current) {
						associateWithSemanticElement();
						$current = true;
					}
				}
			)
		)
	)
;

//Entry rule entryRuleNode
entryRuleNode returns [Boolean current=false]:
	{ markComposite(elementTypeProvider.getNodeElementType()); }
	iv_ruleNode=ruleNode
	{ $current=$iv_ruleNode.current; }
	EOF;

// Rule Node
ruleNode returns [Boolean current=false]
:
	(
		{
			markLeaf(elementTypeProvider.getNode_NodeKeyword_0ElementType());
		}
		otherlv_0='node'
		{
			doneLeaf(otherlv_0);
		}
		(
			(
				{
					markLeaf(elementTypeProvider.getNode_NameIDTerminalRuleCall_1_0ElementType());
				}
				lv_name_1_0=RULE_ID
				{
					if(!$current) {
						associateWithSemanticElement();
						$current = true;
					}
				}
				{
					doneLeaf(lv_name_1_0);
				}
			)
		)
		(
			{
				markLeaf(elementTypeProvider.getNode_LeftParenthesisKeyword_2_0ElementType());
			}
			otherlv_2='('
			{
				doneLeaf(otherlv_2);
			}
			(
				(
					{
						markComposite(elementTypeProvider.getNode_ChildrenNodeParserRuleCall_2_1_0ElementType());
					}
					lv_children_3_0=ruleNode
					{
						doneComposite();
						if(!$current) {
							associateWithSemanticElement();
							$current = true;
						}
					}
				)
			)+
			{
				markLeaf(elementTypeProvider.getNode_RightParenthesisKeyword_2_2ElementType());
			}
			otherlv_4=')'
			{
				doneLeaf(otherlv_4);
			}
		)?
	)
;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' .|~(('\\'|'"')))* '"'|'\'' ('\\' .|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;
