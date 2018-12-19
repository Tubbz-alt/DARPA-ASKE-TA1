/*
 * generated by Xtext 2.14.0.RC1
 */
grammar DebugInternalDialog;

// Rule DialogModel
ruleDialogModel:
	ruleSadlModel
;

// Rule SadlModelElement
ruleSadlModelElement:
	(
		'CM:'
		(
			ruleSadlStatement
			    |
			ruleEquationStatement
			    |
			ruleExternalEquationStatement
		)
		ruleEOS
		    |
		'CM:'
		ruleStringResponse
		ruleEOS
		    |
		ruleModifiedAskStatement
		ruleEOS
		    |
		ruleWhatStatement
		ruleEOS
		    |
		ruleHowManyValuesStatement
		ruleEOS
	)
;

// Rule StringResponse
ruleStringResponse:
	RULE_STRING
;

// Rule ModifiedAskStatement
ruleModifiedAskStatement:
	(
		'Ask'
		    |
		'ask'
	)
	(
		ruleConstructExpression
		    |ruleAskExpression
		    |ruleExpression
	)
;

// Rule WhatStatement
ruleWhatStatement:
	(
		'What'
		    |
		'what'
	)
	(
		ruleWhatIsStatement
		    |ruleWhatValuesStatement
	)
;

// Rule WhatIsStatement
ruleWhatIsStatement:
	'is'
	ruleAnArticle
	?
	ruleSadlResource
;

// Rule WhatValuesStatement
ruleWhatValuesStatement:
	(
		'value'
		    |
		'values'
	)
	(
		'can'
		    |
		'must'
	)
	ruleSadlResource
	'of'
	ruleAnArticle
	?
	ruleSadlPrimaryTypeReference
	'have'
;

// Rule HowManyValuesStatement
ruleHowManyValuesStatement:
	(
		'How'
		    |
		'how'
	)
	'many'
	'values'
	'of'
	ruleSadlResource
	(
		'of'
		'type'
		ruleSadlPrimaryTypeReference
	)?
	'can'
	ruleAnArticle
	?
	ruleSadlPrimaryTypeReference
	'have'
;

// Rule EOS
ruleEOS:
	(
		superEOS
		    |
		'?'
	)
;

// Rule SadlModel
ruleSadlModel:
	'uri'
	RULE_STRING
	(
		'alias'
		RULE_ID
	)?
	(
		'version'
		RULE_STRING
	)?
	ruleSadlAnnotation
	*
	ruleEOS
	ruleSadlImport
	*
	ruleSadlModelElement
	*
;

// Rule SadlAnnotation
ruleSadlAnnotation:
	','?
	'('
	(
		'alias'
		    |
		'note'
	)
	RULE_STRING
	(
		','
		RULE_STRING
	)*
	')'
;

// Rule SadlImport
ruleSadlImport:
	'import'
	RULE_STRING
	(
		'as'
		RULE_ID
	)?
	ruleEOS
;

// Rule EquationStatement
ruleEquationStatement:
	'Equation'
	ruleEquationSignature
	ruleExpression
	?
	(
		'return'
		ruleExpression
	)?
	(
		'where'
		ruleExpression
	)?
;

// Rule ExternalEquationStatement
ruleExternalEquationStatement:
	'External'
	ruleEquationSignature
	RULE_STRING
	(
		'located'
		'at'
		RULE_STRING
	)?
	(
		'where'
		ruleExpression
	)?
;

// Rule EquationSignature
ruleEquationSignature:
	ruleSadlResource
	'('
	(
		ruleSadlParameterDeclaration
		(
			','
			ruleSadlParameterDeclaration
		)*
	)?
	')'
	'returns'
	ruleSadlReturnDeclaration
	(
		','
		ruleSadlReturnDeclaration
	)*
	':'
;

// Rule SadlParameterDeclaration
ruleSadlParameterDeclaration:
	(
		ruleSadlPrimaryTypeReference
		ruleSadlResource
		    |
		'--'
		    |
		'...'
	)
;

// Rule SadlReturnDeclaration
ruleSadlReturnDeclaration:
	(
		ruleSadlPrimaryTypeReference
		    |
		'None'
		    |
		'--'
	)
;

// Rule SadlStatement
ruleSadlStatement:
	(
		ruleSadlResource
		(
			'is'
			'a'
			(
				'top-level'?
				'class'
				    |
				'type'
				'of'
				ruleSadlPrimaryTypeReference
				ruleSadlDataTypeFacet
				?
			)
			(
				ruleSadlPropertyDeclarationInClass
				+
				    |
				(
					','?
					ruleSadlPropertyRestriction
				)+
			)?
			    |
			'is'
			'a'
			'property'
			(
				','?
				ruleSadlPropertyRestriction
			)*
			    |
			(
				','?
				ruleSadlPropertyRestriction
			)+
			    |
			'is'
			'the'
			'same'
			'as'
			'not'
			?
			ruleSadlTypeReference
			    |
			'is'
			'not'
			'the'
			'same'
			'as'
			ruleSadlTypeReference
			    |
			(
				'is'
				ruleAnArticle
				ruleSadlTypeReference
			)?
			(
				ruleSadlValueList
				    |
				ruleSadlPropertyInitializer
				+
			)?
			    |
			(
				'and'
				ruleSadlResource
			)+
			'are'
			'disjoint'
		)
		    |
		'{'
		ruleSadlResource
		(
			','
			ruleSadlResource
		)*
		'}'
		'are'
		(
			(
				'top-level'?
				'classes'
				    |
				(
					'types'
					    |
					'instances'
				)
				'of'
				ruleSadlPrimaryTypeReference
			)
			ruleSadlPropertyDeclarationInClass
			*
			    |
			'disjoint'
			    |
			'not'
			?
			'the'
			'same'
		)
		    |
		ruleAnArticle?
		'relationship'
		'of'
		ruleSadlTypeReference
		'to'
		ruleSadlTypeReference
		'is'
		ruleSadlResource
		    |
		ruleAnArticle
		ruleSadlTypeReference
		(
			ruleSadlResource
			?
			ruleSadlPropertyInitializer
			*
			    |
			'is'
			ruleAnArticle
			ruleSadlResource
			'only'
			'if'
			ruleSadlPropertyCondition
			(
				'and'
				ruleSadlPropertyCondition
			)*
		)
	)
;

// Rule SadlPropertyCondition
ruleSadlPropertyCondition:
	ruleQNAME
	ruleSadlCondition
;

// Rule SadlPropertyInitializer
ruleSadlPropertyInitializer:
	(
		','?
		(
			'with'
			    |
			'has'
		)?
		ruleQNAME
		(
			ruleSadlExplicitValue
			    |
			'('
			ruleSadlNestedInstance
			')'
		)
		    |
		','?
		'is'
		ruleQNAME
		'of'
		ruleQNAME
		    |
		','?
		'of'
		ruleQNAME
		'is'
		(
			ruleSadlExplicitValue
			    |
			'('
			ruleSadlNestedInstance
			')'
		)
	)
;

// Rule SadlNestedInstance
ruleSadlNestedInstance:
	(
		ruleSadlResource
		'is'
		ruleAnArticle
		ruleSadlTypeReference
		ruleSadlPropertyInitializer
		*
		    |
		ruleAnArticle
		ruleSadlTypeReference
		ruleSadlResource
		?
		ruleSadlPropertyInitializer
		*
	)
;

// Rule SadlResource
ruleSadlResource:
	ruleQNAME
	ruleSadlAnnotation
	*
;

// Rule SadlDataTypeFacet
ruleSadlDataTypeFacet:
	(
		(
			'('
			    |
			'['
		)
		ruleFacetNumber
		?
		','
		ruleFacetNumber
		?
		(
			']'
			    |
			')'
		)
		    |
		RULE_STRING
		    |
		'length'
		(
			ruleFacetNumber
			    |
			ruleFacetNumber
			'-'
			(
				ruleFacetNumber
				    |'*'
			)
		)
		    |
		'{'
		ruleFacetValue
		(
			','?
			ruleFacetValue
		)*
		'}'
	)
;

// Rule FacetNumber
ruleFacetNumber:
	'-'?
	ruleAnyNumber
;

// Rule FacetValue
ruleFacetValue:
	(
		RULE_STRING
		    |
		ruleFacetNumber
	)
;

// Rule SadlTypeReference
ruleSadlTypeReference:
	ruleSadlUnionType
;

// Rule SadlUnionType
ruleSadlUnionType:
	ruleSadlIntersectionType
	(
		'or'
		ruleSadlIntersectionType
	)*
;

// Rule SadlIntersectionType
ruleSadlIntersectionType:
	ruleSadlPrimaryTypeReference
	(
		'and'
		ruleSadlPrimaryTypeReference
	)*
;

// Rule SadlPrimaryTypeReference
ruleSadlPrimaryTypeReference:
	(
		ruleQNAME
		'List'
		?
		    |
		ruleSadlDataType
		'List'
		?
		    |
		'('
		ruleSadlPropertyCondition
		')'
		    |
		'{'
		ruleSadlTypeReference
		'}'
	)
;

// Rule SadlPropertyDeclarationInClass
ruleSadlPropertyDeclarationInClass:
	','?
	'described'
	'by'
	ruleSadlResource
	ruleSadlPropertyRestriction
	*
;

// Rule SadlPropertyRestriction
ruleSadlPropertyRestriction:
	(
		ruleSadlCondition
		    |
		(
			'describes'
			    |
			'of'
		)
		ruleSadlTypeReference
		    |
		(
			'has'
			    |
			'with'
		)
		(
			'a'
			'single'
			'value'
			    |
			'values'
		)
		'of'
		'type'
		(
			(
				'class'
				    |
				'data'
			)
			    |
			ruleSadlPrimaryTypeReference
			ruleSadlDataTypeFacet
			?
		)
		    |
		'is'
		'the'
		'inverse'
		'of'
		ruleQNAME
		    |
		'is'
		'transitive'
		    |
		'is'
		'symmetrical'
		    |
		'is'
		'a'
		'type'
		'of'
		'annotation'
		    |
		'has'
		(
			'level'
			RULE_NUMBER
		)?
		'default'
		ruleSadlExplicitValue
		    |
		'has'
		'a'
		'single'
		(
			'subject'
			    |
			'value'
		)?
		    |
		'must'
		'be'
		'one'
		'of'
		'{'
		ruleSadlExplicitValue
		(
			','
			ruleSadlExplicitValue
		)*
		'}'
		    |
		'can'
		'only'
		'be'
		'one'
		'of'
		'{'
		ruleSadlExplicitValue
		(
			','
			ruleSadlExplicitValue
		)*
		'}'
	)
;

// Rule SadlCondition
ruleSadlCondition:
	(
		ruleSadlAllValuesCondition
		    |
		ruleSadlHasValueCondition
		    |
		ruleSadlCardinalityCondition
	)
;

// Rule SadlAllValuesCondition
ruleSadlAllValuesCondition:
	'only'
	(
		'has'
		    |
		'with'
	)
	'values'
	'of'
	'type'
	ruleSadlPrimaryTypeReference
	ruleSadlDataTypeFacet
	?
;

// Rule SadlHasValueCondition
ruleSadlHasValueCondition:
	'always'
	(
		'has'
		    |
		'with'
	)
	'value'
	(
		ruleSadlExplicitValue
		    |
		'('
		ruleSadlNestedInstance
		')'
	)
;

// Rule SadlCardinalityCondition
ruleSadlCardinalityCondition:
	(
		'has'
		    |
		'with'
	)
	(
		'at'
		(
			'least'
			    |
			'most'
		)
		    |
		'exactly'
	)?
	ruleCardinalityValue
	(
		'value'
		    |
		'values'
	)
	(
		'of'
		'type'
		ruleSadlPrimaryTypeReference
		ruleSadlDataTypeFacet
		?
	)?
;

// Rule CardinalityValue
ruleCardinalityValue:
	(
		RULE_NUMBER
		    |
		'one'
	)
;

// Rule SadlExplicitValue
ruleSadlExplicitValue:
	(
		ruleSadlExplicitValueLiteral
		    |
		(
			((
				'-'
				    |
				'not'
			)
			ruleSadlExplicitValueLiteral
			)=>
			(
				'-'
				    |
				'not'
			)
			ruleSadlExplicitValueLiteral
		)
	)
;

// Rule SadlExplicitValueLiteral
ruleSadlExplicitValueLiteral:
	(
		ruleSadlResource
		    |
		ruleAnyNumber
		(
			(RULE_STRING | RULE_ID)=>
			ruleUNIT
		)?
		    |
		RULE_STRING
		    |
		(
			'true'
			    |
			'false'
		)
		    |
		ruleSadlValueList
		    |
		(
			'PI'
			    |
			'e'
			    |
			'known'
		)
	)
;

// Rule UNIT
ruleUNIT:
	(
		RULE_STRING
		    |
		RULE_ID
	)
;

// Rule SadlValueList
ruleSadlValueList:
	'['
	(
		ruleSadlExplicitValue
		(
			','
			ruleSadlExplicitValue
		)*
	)?
	']'
;

// Rule AnArticle
ruleAnArticle:
	(
		ruleIndefiniteArticle
		    |
		ruleDefiniteArticle
	)
;

// Rule IndefiniteArticle
ruleIndefiniteArticle:
	(
		'A'
		    |
		'a'
		    |
		'An'
		    |
		'an'
		    |
		'any'
		    |
		'some'
		    |
		'another'
	)
;

// Rule DefiniteArticle
ruleDefiniteArticle:
	(
		'The'
		    |
		'the'
	)
;

// Rule Ordinal
ruleOrdinal:
	(
		'first'
		    |
		'second'
		    |
		'other'
		    |
		'third'
		    |
		'fourth'
		    |
		'fifth'
		    |
		'sixth'
		    |
		'seventh'
		    |
		'eighth'
		    |
		'ninth'
		    |
		'tenth'
	)
;

// Rule NamedStructureAnnotation
ruleNamedStructureAnnotation:
	','?
	'('
	ruleSadlResource
	ruleSadlExplicitValue
	(
		','
		ruleSadlExplicitValue
	)*
	')'
;

// Rule ConstructExpression
ruleConstructExpression:
	'construct'
	ruleSadlResource
	','?
	ruleSadlResource
	','?
	ruleSadlResource
	'where'
	ruleExpression
;

// Rule AskExpression
ruleAskExpression:
	'ask'
	'where'
	ruleExpression
;

// Rule UpdateExpression
ruleUpdateExpression:
	(
		'delete'
		'data'
		?
		ruleExpression
	)?
	(
		'insert'
		'data'
		?
		ruleExpression
	)?
	(
		'where'
		ruleExpression
	)?
;

// Rule Expression
ruleExpression:
	norm3_SelectExpression
;

// Rule NestedExpression
ruleNestedExpression:
	norm1_SelectExpression
;

// Rule SelectExpression
ruleSelectExpression:
	(
		(
			('select')=>
			'select'
			'distinct'
			?
			(
				'*'
				    |
				ruleSadlResource
				(
					','?
					ruleSadlResource
				)*
			)
			'where'
			ruleExpressionParameterized
			(
				'order'
				'by'
				ruleOrderElement
				(
					(',' | 'asc' | 'desc' | RULE_QNAME_TERMINAL | RULE_ID)=>
					','?
					ruleOrderElement
				)*
			)?
		)
		    |
		ruleExpressionParameterized
	)
;

// Rule SelectExpression
norm1_SelectExpression:
	(
		(
			('select')=>
			'select'
			'distinct'
			?
			(
				'*'
				    |
				ruleSadlResource
				(
					','?
					ruleSadlResource
				)*
			)
			'where'
			norm1_ExpressionParameterized
			(
				'order'
				'by'
				ruleOrderElement
				(
					(',' | 'asc' | 'desc' | RULE_QNAME_TERMINAL | RULE_ID)=>
					','?
					ruleOrderElement
				)*
			)?
		)
		    |
		norm1_ExpressionParameterized
	)
;

// Rule SelectExpression
norm2_SelectExpression:
	(
		(
			('select')=>
			'select'
			'distinct'
			?
			(
				'*'
				    |
				ruleSadlResource
				(
					','?
					ruleSadlResource
				)*
			)
			'where'
			norm2_ExpressionParameterized
			(
				'order'
				'by'
				ruleOrderElement
				(
					(',' | 'asc' | 'desc' | RULE_QNAME_TERMINAL | RULE_ID)=>
					','?
					ruleOrderElement
				)*
			)?
		)
		    |
		norm2_ExpressionParameterized
	)
;

// Rule SelectExpression
norm3_SelectExpression:
	(
		(
			('select')=>
			'select'
			'distinct'
			?
			(
				'*'
				    |
				ruleSadlResource
				(
					','?
					ruleSadlResource
				)*
			)
			'where'
			norm3_ExpressionParameterized
			(
				'order'
				'by'
				ruleOrderElement
				(
					(',' | 'asc' | 'desc' | RULE_QNAME_TERMINAL | RULE_ID)=>
					','?
					ruleOrderElement
				)*
			)?
		)
		    |
		norm3_ExpressionParameterized
	)
;

// Rule OrderElement
ruleOrderElement:
	(
		'asc'
		    |
		'desc'
	)?
	ruleSadlResource
;

// Rule ExpressionParameterized
ruleExpressionParameterized:
	(
		ruleAnArticle?
		'sublist'
		'of'
		ruleOrExpression
		'matching'
		ruleOrExpression
		    |
		ruleOrExpression
	)
;

// Rule ExpressionParameterized
norm1_ExpressionParameterized:
	(
		ruleAnArticle?
		'sublist'
		'of'
		norm1_OrExpression
		'matching'
		norm1_OrExpression
		    |
		norm1_OrExpression
	)
;

// Rule ExpressionParameterized
norm2_ExpressionParameterized:
	(
		ruleAnArticle?
		'sublist'
		'of'
		norm2_OrExpression
		'matching'
		norm2_OrExpression
		    |
		norm2_OrExpression
	)
;

// Rule ExpressionParameterized
norm3_ExpressionParameterized:
	(
		ruleAnArticle?
		'sublist'
		'of'
		norm3_OrExpression
		'matching'
		norm3_OrExpression
		    |
		norm3_OrExpression
	)
;

// Rule OrExpression
ruleOrExpression:
	ruleAndExpression
	(
		ruleOpOr
		ruleAndExpression
	)*
;

// Rule OrExpression
norm1_OrExpression:
	norm1_AndExpression
	(
		ruleOpOr
		norm1_AndExpression
	)*
;

// Rule OrExpression
norm2_OrExpression:
	norm2_AndExpression
	(
		ruleOpOr
		norm2_AndExpression
	)*
;

// Rule OrExpression
norm3_OrExpression:
	norm3_AndExpression
	(
		ruleOpOr
		norm3_AndExpression
	)*
;

// Rule OpOr
ruleOpOr:
	(
		'or'
		    |
		'||'
	)
;

// Rule AndExpression
ruleAndExpression:
	ruleEqualityExpression
	(
		ruleOpAnd
		ruleEqualityExpression
	)*
;

// Rule AndExpression
norm1_AndExpression:
	norm1_EqualityExpression
	(
		ruleOpAnd
		norm1_EqualityExpression
	)*
;

// Rule AndExpression
norm2_AndExpression:
	norm2_EqualityExpression
	(
		ruleOpAnd
		norm2_EqualityExpression
	)*
;

// Rule AndExpression
norm3_AndExpression:
	norm3_EqualityExpression
	(
		ruleOpAnd
		norm3_EqualityExpression
	)*
;

// Rule OpAnd
ruleOpAnd:
	(
		'and'
		    |
		'&&'
	)
;

// Rule EqualityExpression
ruleEqualityExpression:
	ruleRelationalExpression
	(
		ruleInfixOperator
		ruleRelationalExpression
	)*
;

// Rule EqualityExpression
norm1_EqualityExpression:
	norm1_RelationalExpression
	(
		ruleInfixOperator
		norm1_RelationalExpression
	)*
;

// Rule EqualityExpression
norm2_EqualityExpression:
	norm2_RelationalExpression
	(
		ruleInfixOperator
		norm2_RelationalExpression
	)*
;

// Rule EqualityExpression
norm3_EqualityExpression:
	norm3_RelationalExpression
	(
		ruleInfixOperator
		norm3_RelationalExpression
	)*
;

// Rule InfixOperator
ruleInfixOperator:
	(
		'=='
		    |
		'!='
		    |
		'='
		    |
		'is'
		(
			'not'?
			'unique'
			'in'
		)?
		    |
		'contains'
		    |
		'does'
		'not'
		'contain'
	)
;

// Rule RelationalExpression
ruleRelationalExpression:
	ruleAddition
	(
		('>=' | '<=' | '>' | '<')=>
		(
			(ruleOpCompare
			)=>
			ruleOpCompare
		)
		ruleAddition
	)*
;

// Rule RelationalExpression
norm1_RelationalExpression:
	norm1_Addition
	(
		('>=' | '<=' | '>' | '<')=>
		(
			(ruleOpCompare
			)=>
			ruleOpCompare
		)
		norm1_Addition
	)*
;

// Rule RelationalExpression
norm2_RelationalExpression:
	norm2_Addition
	(
		('>=' | '<=' | '>' | '<')=>
		(
			(ruleOpCompare
			)=>
			ruleOpCompare
		)
		norm2_Addition
	)*
;

// Rule RelationalExpression
norm3_RelationalExpression:
	norm3_Addition
	(
		('>=' | '<=' | '>' | '<')=>
		(
			(ruleOpCompare
			)=>
			ruleOpCompare
		)
		norm3_Addition
	)*
;

// Rule OpCompare
ruleOpCompare:
	(
		'>='
		    |
		'<='
		    |
		'>'
		    |
		'<'
	)
;

// Rule Addition
ruleAddition:
	ruleMultiplication
	(
		ruleAddOp
		ruleMultiplication
	)*
;

// Rule Addition
norm1_Addition:
	norm1_Multiplication
	(
		ruleAddOp
		norm1_Multiplication
	)*
;

// Rule Addition
norm2_Addition:
	norm2_Multiplication
	(
		ruleAddOp
		norm2_Multiplication
	)*
;

// Rule Addition
norm3_Addition:
	norm3_Multiplication
	(
		ruleAddOp
		norm3_Multiplication
	)*
;

// Rule AddOp
ruleAddOp:
	(
		'+'
		    |
		'-'
	)
;

// Rule Multiplication
ruleMultiplication:
	rulePower
	(
		ruleMultiOp
		rulePower
	)*
;

// Rule Multiplication
norm1_Multiplication:
	norm1_Power
	(
		ruleMultiOp
		norm1_Power
	)*
;

// Rule Multiplication
norm2_Multiplication:
	norm2_Power
	(
		ruleMultiOp
		norm2_Power
	)*
;

// Rule Multiplication
norm3_Multiplication:
	norm3_Power
	(
		ruleMultiOp
		norm3_Power
	)*
;

// Rule MultiOp
ruleMultiOp:
	(
		'*'
		    |
		'/'
		    |
		'%'
	)
;

// Rule Power
rulePower:
	rulePropOfSubject
	(
		'^'
		rulePropOfSubject
	)*
;

// Rule Power
norm1_Power:
	norm1_PropOfSubject
	(
		'^'
		norm1_PropOfSubject
	)*
;

// Rule Power
norm2_Power:
	norm2_PropOfSubject
	(
		'^'
		norm2_PropOfSubject
	)*
;

// Rule Power
norm3_Power:
	norm3_PropOfSubject
	(
		'^'
		norm3_PropOfSubject
	)*
;

// Rule PropOfSubject
rulePropOfSubject:
	ruleElementInList
	(
		('of' | 'for' | 'in' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
		(
			(
				'of'
				    |
				'for'
				    |
				'in'
			)
			rulePropOfSubject
			    |
			(
				('has' | RULE_QNAME_TERMINAL | RULE_ID)=>
				'has'?
				ruleSadlResource
				(
					('(' | 'A' | 'a' | 'An' | 'an' | 'any' | 'some' | 'another' | 'The' | 'the' | 'true' | 'false' | 'PI' | 'known' | 'e' | '--' | 'None' | 'type' | 'length' | 'count' | 'index' | 'first' | 'last' | 'value' | '[' | '{' | 'not' | '!' | 'only' | '-' | 'there' | 'element' | RULE_QNAME_TERMINAL | RULE_ID | RULE_STRING | RULE_NUMBER)=>
					ruleElementInList
				)?
			)+
		)
	)?
;

// Rule PropOfSubject
norm1_PropOfSubject:
	norm1_ElementInList
	(
		('of' | 'for' | 'in' | 'with' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
		(
			(
				'of'
				    |
				'for'
				    |
				'in'
			)
			norm1_PropOfSubject
			    |
			(
				('with' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
				(
					'with'
					    |
					'has'
				)?
				ruleSadlResource
				(
					('(' | 'A' | 'a' | 'An' | 'an' | 'any' | 'some' | 'another' | 'The' | 'the' | 'true' | 'false' | 'PI' | 'known' | 'e' | '--' | 'None' | 'type' | 'length' | 'count' | 'index' | 'first' | 'last' | 'value' | '[' | '{' | 'not' | '!' | 'only' | '-' | 'there' | 'element' | RULE_QNAME_TERMINAL | RULE_ID | RULE_STRING | RULE_NUMBER)=>
					norm1_ElementInList
				)?
			)+
		)
	)?
;

// Rule PropOfSubject
norm2_PropOfSubject:
	norm2_ElementInList
	(
		('of' | 'for' | 'in' | ',' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
		(
			(
				'of'
				    |
				'for'
				    |
				'in'
			)
			norm2_PropOfSubject
			    |
			(
				(',' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
				','
				?
				'has'?
				ruleSadlResource
				(
					('(' | 'A' | 'a' | 'An' | 'an' | 'any' | 'some' | 'another' | 'The' | 'the' | 'true' | 'false' | 'PI' | 'known' | 'e' | '--' | 'None' | 'type' | 'length' | 'count' | 'index' | 'first' | 'last' | 'value' | '[' | '{' | 'not' | '!' | 'only' | '-' | 'there' | 'element' | RULE_QNAME_TERMINAL | RULE_ID | RULE_STRING | RULE_NUMBER)=>
					norm2_ElementInList
				)?
			)+
		)
	)?
;

// Rule PropOfSubject
norm3_PropOfSubject:
	norm3_ElementInList
	(
		('of' | 'for' | 'in' | ',' | 'with' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
		(
			(
				'of'
				    |
				'for'
				    |
				'in'
			)
			norm3_PropOfSubject
			    |
			(
				(',' | 'with' | 'has' | RULE_QNAME_TERMINAL | RULE_ID)=>
				','
				?
				(
					'with'
					    |
					'has'
				)?
				ruleSadlResource
				(
					('(' | 'A' | 'a' | 'An' | 'an' | 'any' | 'some' | 'another' | 'The' | 'the' | 'true' | 'false' | 'PI' | 'known' | 'e' | '--' | 'None' | 'type' | 'length' | 'count' | 'index' | 'first' | 'last' | 'value' | '[' | '{' | 'not' | '!' | 'only' | '-' | 'there' | 'element' | RULE_QNAME_TERMINAL | RULE_ID | RULE_STRING | RULE_NUMBER)=>
					norm3_ElementInList
				)?
			)+
		)
	)?
;

// Rule ElementInList
ruleElementInList:
	(
		ruleUnitExpression
		    |
		'element'
		(
			'before'
			    |
			'after'
		)?
		ruleUnitExpression
	)
;

// Rule ElementInList
norm1_ElementInList:
	(
		norm1_UnitExpression
		    |
		'element'
		(
			'before'
			    |
			'after'
		)?
		norm1_UnitExpression
	)
;

// Rule ElementInList
norm2_ElementInList:
	(
		norm2_UnitExpression
		    |
		'element'
		(
			'before'
			    |
			'after'
		)?
		norm2_UnitExpression
	)
;

// Rule ElementInList
norm3_ElementInList:
	(
		norm3_UnitExpression
		    |
		'element'
		(
			'before'
			    |
			'after'
		)?
		norm3_UnitExpression
	)
;

// Rule UnitExpression
ruleUnitExpression:
	ruleUnaryExpression
	(
		RULE_STRING
	)?
;

// Rule UnitExpression
norm1_UnitExpression:
	norm1_UnaryExpression
	(
		RULE_STRING
	)?
;

// Rule UnitExpression
norm2_UnitExpression:
	norm2_UnaryExpression
	(
		RULE_STRING
	)?
;

// Rule UnitExpression
norm3_UnitExpression:
	norm3_UnaryExpression
	(
		RULE_STRING
	)?
;

// Rule UnaryExpression
ruleUnaryExpression:
	(
		rulePrimaryExpression
		    |
		(
			'not'
			    |
			'!'
			    |
			'only'
			    |
			'-'
			    |
			ruleThereExists
		)
		rulePrimaryExpression
	)
;

// Rule UnaryExpression
norm1_UnaryExpression:
	(
		norm1_PrimaryExpression
		    |
		(
			'not'
			    |
			'!'
			    |
			'only'
			    |
			'-'
			    |
			ruleThereExists
		)
		norm1_PrimaryExpression
	)
;

// Rule UnaryExpression
norm2_UnaryExpression:
	(
		norm2_PrimaryExpression
		    |
		(
			'not'
			    |
			'!'
			    |
			'only'
			    |
			'-'
			    |
			ruleThereExists
		)
		norm2_PrimaryExpression
	)
;

// Rule UnaryExpression
norm3_UnaryExpression:
	(
		norm3_PrimaryExpression
		    |
		(
			'not'
			    |
			'!'
			    |
			'only'
			    |
			'-'
			    |
			ruleThereExists
		)
		norm3_PrimaryExpression
	)
;

// Rule ThereExists
ruleThereExists:
	'there'
	'exists'
;

// Rule PrimaryExpression
rulePrimaryExpression:
	(
		'('
		ruleSelectExpression
		')'
		    |
		ruleName
		    |
		ruleAnArticle
		ruleOrdinal
		?
		ruleSadlPrimaryTypeReference
		(
			'['
			ruleNestedExpression
			?
			(
				','
				ruleNestedExpression
			)*
			']'
			    |
			'length'
			ruleFacetNumber
			(
				('-')=>
				'-'
				(
					ruleFacetNumber
					    |'*'
				)
			)?
		)?
		    |
		RULE_STRING
		    |
		ruleAnyNumber
		    |
		ruleBooleanValue
		    |
		ruleConstants
		    |
		ruleValueTable
	)
;

// Rule PrimaryExpression
norm1_PrimaryExpression:
	(
		'('
		norm1_SelectExpression
		')'
		    |
		ruleName
		    |
		ruleAnArticle
		ruleOrdinal
		?
		ruleSadlPrimaryTypeReference
		(
			'['
			ruleNestedExpression
			?
			(
				','
				ruleNestedExpression
			)*
			']'
			    |
			'length'
			ruleFacetNumber
			(
				('-')=>
				'-'
				(
					ruleFacetNumber
					    |'*'
				)
			)?
		)?
		    |
		RULE_STRING
		    |
		ruleAnyNumber
		    |
		ruleBooleanValue
		    |
		ruleConstants
		    |
		ruleValueTable
	)
;

// Rule PrimaryExpression
norm2_PrimaryExpression:
	(
		'('
		norm2_SelectExpression
		')'
		    |
		ruleName
		    |
		ruleAnArticle
		ruleOrdinal
		?
		ruleSadlPrimaryTypeReference
		(
			'['
			ruleNestedExpression
			?
			(
				','
				ruleNestedExpression
			)*
			']'
			    |
			'length'
			ruleFacetNumber
			(
				('-')=>
				'-'
				(
					ruleFacetNumber
					    |'*'
				)
			)?
		)?
		    |
		RULE_STRING
		    |
		ruleAnyNumber
		    |
		ruleBooleanValue
		    |
		ruleConstants
		    |
		ruleValueTable
	)
;

// Rule PrimaryExpression
norm3_PrimaryExpression:
	(
		'('
		norm3_SelectExpression
		')'
		    |
		ruleName
		    |
		ruleAnArticle
		ruleOrdinal
		?
		ruleSadlPrimaryTypeReference
		(
			'['
			ruleNestedExpression
			?
			(
				','
				ruleNestedExpression
			)*
			']'
			    |
			'length'
			ruleFacetNumber
			(
				('-')=>
				'-'
				(
					ruleFacetNumber
					    |'*'
				)
			)?
		)?
		    |
		RULE_STRING
		    |
		ruleAnyNumber
		    |
		ruleBooleanValue
		    |
		ruleConstants
		    |
		ruleValueTable
	)
;

// Rule Name
ruleName:
	ruleQNAME
	(
		('(')=>
		'('
		ruleNestedExpression
		?
		(
			','
			ruleNestedExpression
		)*
		')'
	)?
;

// Rule ValueTable
ruleValueTable:
	(
		'['
		ruleValueRow
		']'
		    |
		'{'
		'['
		ruleValueRow
		']'
		(
			','?
			'['
			ruleValueRow
			']'
		)*
		'}'
	)
;

// Rule BooleanValue
ruleBooleanValue:
	(
		'true'
		    |
		'false'
	)
;

// Rule Constants
ruleConstants:
	(
		'PI'
		    |
		'known'
		    |
		'e'
		    |
		'--'
		    |
		'None'
		    |
		'a'?
		'type'
		    |
		ruleDefiniteArticle?
		'length'
		    |
		'count'
		    |
		ruleDefiniteArticle?
		'index'
		    |
		(
			'first'
			    |
			'last'
			    |
			ruleAnArticle
			ruleOrdinal?
		)
		'element'
		    |
		'value'
	)
;

// Rule ValueRow
ruleValueRow:
	ruleNestedExpression
	(
		','
		ruleNestedExpression
	)*
;

// Rule AnyNumber
ruleAnyNumber:
	ruleDecimalNumber
	ruleEXPONENT?
;

// Rule DecimalNumber
ruleDecimalNumber:
	RULE_NUMBER
;

// Rule EXPONENT
ruleEXPONENT:
	(
		'e'
		    |
		'E'
	)
	(
		'-'
		    |
		'+'
	)?
	ruleDecimalNumber
;

// Rule EOS
superEOS:
	'.'
;

// Rule QNAME
ruleQNAME:
	(
		RULE_QNAME_TERMINAL
		    |
		RULE_ID
	)
;

// Rule SadlDataType
ruleSadlDataType:
	(
		'string'
		    |
		'boolean'
		    |
		'decimal'
		    |
		'int'
		    |
		'long'
		    |
		'float'
		    |
		'double'
		    |
		'duration'
		    |
		'dateTime'
		    |
		'time'
		    |
		'date'
		    |
		'gYearMonth'
		    |
		'gYear'
		    |
		'gMonthDay'
		    |
		'gDay'
		    |
		'gMonth'
		    |
		'hexBinary'
		    |
		'base64Binary'
		    |
		'anyURI'
		    |
		'integer'
		    |
		'negativeInteger'
		    |
		'nonNegativeInteger'
		    |
		'positiveInteger'
		    |
		'nonPositiveInteger'
		    |
		'byte'
		    |
		'unsignedByte'
		    |
		'unsignedInt'
		    |
		'anySimpleType'
	)
;

RULE_NUMBER : ('0'..'9')+;

RULE_WS : ('\u00A0'|' '|'\t'|'\r'|'\n')+ {skip();};

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'-'|'%'|'~')*;

RULE_QNAME_TERMINAL : RULE_ID ':' RULE_ID;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/' {skip();};

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')? {skip();};

RULE_ANY_OTHER : .;
