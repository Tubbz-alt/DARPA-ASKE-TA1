/*
 * generated by Xtext 2.14.0.RC1
 */
lexer grammar InternalDialogLexer;

@header {
package com.ge.research.sadl.darpa.aske.parser.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

NonNegativeInteger : 'nonNegativeInteger';

NonPositiveInteger : 'nonPositiveInteger';

NegativeInteger : 'negativeInteger';

PositiveInteger : 'positiveInteger';

AnySimpleType : 'anySimpleType';

Base64Binary : 'base64Binary';

Relationship : 'relationship';

UnsignedByte : 'unsignedByte';

Symmetrical : 'symmetrical';

UnsignedInt : 'unsignedInt';

Deductions : 'Deductions';

Annotation : 'annotation';

GYearMonth : 'gYearMonth';

Transitive : 'transitive';

Construct : 'construct';

Described : 'described';

Describes : 'describes';

GMonthDay : 'gMonthDay';

HexBinary : 'hexBinary';

Instances : 'instances';

TopLevel : 'top-level';

Equation : 'Equation';

Explain : 'Explain:';

External : 'External';

Contains : 'contains';

DateTime : 'dateTime';

Disjoint : 'disjoint';

Distinct : 'distinct';

Duration : 'duration';

Matching : 'matching';

Property : 'property';

Another : 'another';

Boolean : 'boolean';

Classes : 'classes';

Contain : 'contain';

Decimal : 'decimal';

Default : 'default';

Element : 'element';

Exactly : 'exactly';

Integer : 'integer';

Inverse : 'inverse';

Located : 'located';

Returns : 'returns';

Seventh : 'seventh';

Subject : 'subject';

Sublist : 'sublist';

Version : 'version';

Print : 'Print:';

Update : 'Update';

Write : 'Write:';

Always : 'always';

AnyURI : 'anyURI';

Before : 'before';

Delete : 'delete';

Double : 'double';

Eighth : 'eighth';

Exists : 'exists';

Fourth : 'fourth';

GMonth : 'gMonth';

Import : 'import';

Insert : 'insert';

Length : 'length';

Return : 'return';

Second : 'second';

Select : 'select';

Single : 'single';

String : 'string';

Unique : 'unique';

Values : 'values';

Expr : 'Expr:';

Graph : 'Graph';

Model : 'Model';

Read : 'Read:';

Stage : 'Stage';

Test : 'Test:';

After : 'after';

Alias : 'alias';

Class : 'class';

Count : 'count';

False : 'false';

Fifth : 'fifth';

First : 'first';

Float : 'float';

GYear : 'gYear';

Given : 'given';

Index : 'index';

Known : 'known';

Least : 'least';

Level : 'level';

Ninth : 'ninth';

Order : 'order';

Other : 'other';

Sixth : 'sixth';

Tenth : 'tenth';

There : 'there';

Third : 'third';

Types : 'types';

Using : 'using';

Value : 'value';

Where : 'where';

List : 'List';

None : 'None';

Rule : 'Rule';

What : 'What';

Byte : 'byte';

Data : 'data';

Date : 'date';

Desc : 'desc';

Does : 'does';

From : 'from';

GDay : 'gDay';

Last : 'last';

Long : 'long';

Most : 'most';

Must : 'must';

Note : 'note';

Only : 'only';

Same : 'same';

Some : 'some';

Then : 'then';

Time : 'time';

True : 'true';

Type : 'type';

With : 'with';

FullStopFullStopFullStop : '...';

Ask : 'Ask';

The : 'The';

And : 'and';

Any : 'any';

Are : 'are';

Asc : 'asc';

Ask_1 : 'ask';

Can : 'can';

For : 'for';

Has : 'has';

Int : 'int';

Not : 'not';

One : 'one';

The_1 : 'the';

Uri : 'uri';

ExclamationMarkEqualsSign : '!=';

AmpersandAmpersand : '&&';

HyphenMinusHyphenMinus : '--';

LessThanSignEqualsSign : '<=';

EqualsSignEqualsSign : '==';

EqualsSignGreaterThanSign : '=>';

GreaterThanSignEqualsSign : '>=';

An : 'An';

PI : 'PI';

An_1 : 'an';

As : 'as';

At : 'at';

Be : 'be';

By : 'by';

If : 'if';

In : 'in';

Is : 'is';

Of : 'of';

Or : 'or';

To : 'to';

VerticalLineVerticalLine : '||';

ExclamationMark : '!';

PercentSign : '%';

LeftParenthesis : '(';

RightParenthesis : ')';

Asterisk : '*';

PlusSign : '+';

Comma : ',';

HyphenMinus : '-';

FullStop : '.';

Solidus : '/';

Colon : ':';

LessThanSign : '<';

EqualsSign : '=';

GreaterThanSign : '>';

QuestionMark : '?';

A : 'A';

E : 'E';

LeftSquareBracket : '[';

RightSquareBracket : ']';

CircumflexAccent : '^';

A_1 : 'a';

E_1 : 'e';

LeftCurlyBracket : '{';

RightCurlyBracket : '}';

RULE_NUMBER : ('0'..'9')+;

RULE_WS : ('\u00A0'|' '|'\t'|'\r'|'\n')+;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'-'|'%'|'~')*;

RULE_QNAME_TERMINAL : RULE_ID ':' RULE_ID;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_ANY_OTHER : .;
