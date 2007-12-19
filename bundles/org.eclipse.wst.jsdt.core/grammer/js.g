--main options
%options ACTION, AN=JavaAction.java, GP=java, 
%options FILE-PREFIX=java, ESCAPE=$, PREFIX=TokenName, OUTPUT-SIZE=125 ,
%options NOGOTO-DEFAULT, SINGLE-PRODUCTIONS, LALR=1 , TABLE, 

--error recovering options.....
%options ERROR_MAPS 

--grammar understanding options
%options first follow
%options TRACE=FULL ,
%options VERBOSE

%options DEFERRED
%options NAMES=MAX
%options SCOPES

--Usefull macros helping reading/writing semantic actions
$Define 
$putCase 
/.    case $rule_number : if (DEBUG) { System.out.println("$rule_text"); }  //$NON-NLS-1$
		   ./

$break
/. 
			break;
./


$readableName 
/.1#$rule_number#./
$compliance
/.2#$rule_number#./
$recovery
/.2#$rule_number# recovery./
$recovery_template
/.3#$rule_number#./
$no_statements_recovery
/.4#$rule_number# 1./
$empty_statement
/.5#$rule_number# 1./
-- here it starts really ------------------------------------------
$Terminals

	Identifier

	abstract boolean break byte case catch char class 
	continue const default delete debugger do double else enum export extends false final finally float
	for function  goto if in infinity implements import instanceof int
	interface long native new null package private
	protected public return short static strictfp super switch
	synchronized this throw throws transient true try typeof undefined var void
	volatile with while

	IntegerLiteral
	LongLiteral
	FloatingPointLiteral
	DoubleLiteral
	CharacterLiteral
	StringLiteral
	RegExLiteral

	PLUS_PLUS
	MINUS_MINUS
	EQUAL_EQUAL
	EQUAL_EQUAL_EQUAL
	NOT_EQUAL_EQUAL
	LESS_EQUAL
	GREATER_EQUAL
	NOT_EQUAL
	LEFT_SHIFT
	RIGHT_SHIFT
	UNSIGNED_RIGHT_SHIFT
	PLUS_EQUAL
	MINUS_EQUAL
	MULTIPLY_EQUAL
	DIVIDE_EQUAL
	AND_EQUAL
	OR_EQUAL
	XOR_EQUAL
	REMAINDER_EQUAL
	LEFT_SHIFT_EQUAL
	RIGHT_SHIFT_EQUAL
	UNSIGNED_RIGHT_SHIFT_EQUAL
	OR_OR
	AND_AND
	PLUS
	MINUS
	NOT
	REMAINDER
	XOR
	AND
	MULTIPLY
	OR
	TWIDDLE
	DIVIDE
	GREATER
	LESS
	LPAREN
	RPAREN
	LBRACE
	RBRACE
	LBRACKET
	RBRACKET
	SEMICOLON
	QUESTION
	COLON
	COMMA
	DOT
	EQUAL
	AT

--    BodyMarker

$Alias

	'++'   ::= PLUS_PLUS
	'--'   ::= MINUS_MINUS
	'=='   ::= EQUAL_EQUAL
	'==='  ::= EQUAL_EQUAL_EQUAL
	'<='   ::= LESS_EQUAL
	'>='   ::= GREATER_EQUAL
	'!='   ::= NOT_EQUAL
	'!=='   ::= NOT_EQUAL_EQUAL
	'<<'   ::= LEFT_SHIFT
	'>>'   ::= RIGHT_SHIFT
	'>>>'  ::= UNSIGNED_RIGHT_SHIFT
	'+='   ::= PLUS_EQUAL
	'-='   ::= MINUS_EQUAL
	'*='   ::= MULTIPLY_EQUAL
	'/='   ::= DIVIDE_EQUAL
	'&='   ::= AND_EQUAL
	'|='   ::= OR_EQUAL
	'^='   ::= XOR_EQUAL
	'%='   ::= REMAINDER_EQUAL
	'<<='  ::= LEFT_SHIFT_EQUAL
	'>>='  ::= RIGHT_SHIFT_EQUAL
	'>>>=' ::= UNSIGNED_RIGHT_SHIFT_EQUAL
	'||'   ::= OR_OR
	'&&'   ::= AND_AND
	'+'    ::= PLUS
	'-'    ::= MINUS
	'!'    ::= NOT
	'%'    ::= REMAINDER
	'^'    ::= XOR
	'&'    ::= AND
	'*'    ::= MULTIPLY
	'|'    ::= OR
	'~'    ::= TWIDDLE
	'/'    ::= DIVIDE
	'>'    ::= GREATER
	'<'    ::= LESS
	'('    ::= LPAREN
	')'    ::= RPAREN
	'{'    ::= LBRACE
	'}'    ::= RBRACE
	'['    ::= LBRACKET
	']'    ::= RBRACKET
	';'    ::= SEMICOLON
	'?'    ::= QUESTION
	':'    ::= COLON
	','    ::= COMMA
	'.'    ::= DOT
	'='    ::= EQUAL
	'@'	   ::= AT
	
$Start
	Goal

$Rules

/.// This method is part of an automatic generation : do NOT edit-modify  
protected void consumeRule(int act) {
  switch ( act ) {
./



Goal ::= '++' CompilationUnit
Goal ::= '--' MethodBody
-- Initializer
-- Goal ::= '>>' StaticInitializer
-- Goal ::= '>>' Initializer
-- error recovery
-- Modifiersopt is used to properly consume a header and exit the rule reduction at the end of the parse() method
Goal ::= '>>>' Header1 Modifiersopt
-- Goal ::= '!' Header2 Modifiersopt
Goal ::= '*' BlockStatements
Goal ::= '*' CatchHeader
-- JDOM
Goal ::= '&&' LocalVariableDeclaration
-- Goal ::= '||' ImportDeclaration
-- Goal ::= '?' PackageDeclaration
-- Goal ::= '+' TypeDeclaration
-- Goal ::= '/' GenericMethodDeclaration
-- Goal ::= '&' ClassBodyDeclarations
-- code snippet
Goal ::= '%' Expression
-- Goal ::= '%' ArrayInitializer
-- completion parser
Goal ::= '~' BlockStatementsopt
Goal ::= '+' ProgramElement
-- source type converter
-- Goal ::= '||' MemberValue
-- syntax diagnosis
-- Goal ::= '?' AnnotationTypeMemberDeclaration
/:$readableName Goal:/

Literal -> IntegerLiteral
Literal -> LongLiteral
Literal -> FloatingPointLiteral
Literal -> DoubleLiteral
Literal -> CharacterLiteral
Literal -> StringLiteral
Literal -> RegExLiteral
Literal -> null
Literal -> undefined
Literal -> BooleanLiteral
/:$readableName Literal:/
BooleanLiteral -> true
BooleanLiteral -> false
/:$readableName BooleanLiteral:/

-- Type ::= PrimitiveType
-- /.$putCase consumePrimitiveType(); $break ./
-- Type -> ReferenceType
-- /:$readableName Type:/

-- PrimitiveType -> NumericType
-- /:$readableName PrimitiveType:/
-- NumericType -> IntegralType
-- NumericType -> FloatingPointType
-- /:$readableName NumericType:/

-- PrimitiveType -> 'boolean'
-- PrimitiveType -> 'void'
-- IntegralType -> 'byte'
-- IntegralType -> 'short'
-- IntegralType -> 'int'
-- IntegralType -> 'long'
-- IntegralType -> 'char'
-- /:$readableName IntegralType:/
-- FloatingPointType -> 'float'
-- FloatingPointType -> 'double'
-- /:$readableName FloatingPointType:/

-- ReferenceType ::= ClassOrInterfaceType
-- /.$putCase consumeReferenceType();  $break ./
-- ReferenceType -> ArrayType
-- /:$readableName ReferenceType:/

---------------------------------------------------------------
-- 1.5 feature
---------------------------------------------------------------
-- ClassOrInterfaceType -> ClassOrInterface
-- ClassOrInterfaceType -> GenericType
-- /:$readableName Type:/

-- ClassOrInterface ::= Name
-- /.$putCase consumeClassOrInterfaceName();  $break ./
-- ClassOrInterface ::= GenericType '.' Name
-- /.$putCase consumeClassOrInterface();  $break ./
-- /:$readableName Type:/

-- GenericType ::= ClassOrInterface TypeArguments
-- /.$putCase consumeGenericType();  $break ./
-- /:$readableName GenericType:/

--
-- These rules have been rewritten to avoid some conflicts introduced
-- by adding the 1.1 features
--
-- ArrayType ::= PrimitiveType '[' ']'
-- ArrayType ::= Name '[' ']'
-- ArrayType ::= ArrayType '[' ']'
--

-- ArrayTypeWithTypeArgumentsName ::= GenericType '.' Name
-- /.$putCase consumeArrayTypeWithTypeArgumentsName();  $break ./
-- /:$readableName ArrayTypeWithTypeArgumentsName:/

-- ArrayType ::= PrimitiveType Dims
-- /.$putCase consumePrimitiveArrayType();  $break ./
-- ArrayType ::= Name Dims
-- /.$putCase consumeNameArrayType();  $break ./
-- ArrayType ::= ArrayTypeWithTypeArgumentsName Dims
-- /.$putCase consumeGenericTypeNameArrayType();  $break ./
-- ArrayType ::= GenericType Dims
-- /.$putCase consumeGenericTypeArrayType();  $break ./
-- /:$readableName ArrayType:/

-- ClassType -> ClassOrInterfaceType
-- /:$readableName ClassType:/

--------------------------------------------------------------
--------------------------------------------------------------

Name -> SimpleName
Name -> QualifiedName
/:$readableName Name:/

SimpleName -> 'Identifier'
/:$readableName SimpleName:/

QualifiedName ::= Name '.' SimpleName 
/.$putCase consumeQualifiedName(); $break ./
/:$readableName QualifiedName:/

CompilationUnit ::= EnterCompilationUnit InternalCompilationUnit
/.$putCase consumeCompilationUnit(); $break ./
/:$readableName CompilationUnit:/

InternalCompilationUnit ::= ProgramElements
/.$putCase consumeInternalCompilationUnitWithTypes(); $break ./
InternalCompilationUnit ::= $empty
/.$putCase consumeEmptyInternalCompilationUnit(); $break ./
/:$readableName CompilationUnit:/

-- ReduceImports ::= $empty
-- /.$putCase consumeReduceImports(); $break ./
-- /:$readableName ReduceImports:/

EnterCompilationUnit ::= $empty
/.$putCase consumeEnterCompilationUnit(); $break ./
/:$readableName EnterCompilationUnit:/

-- Header -> ImportDeclaration
-- Header -> PackageDeclaration
-- Header -> ClassHeader
-- Header -> InterfaceHeader
-- Header -> EnumHeader
-- Header -> AnnotationTypeDeclarationHeader
-- Header -> StaticInitializer
Header -> RecoveryMethodHeader
Header -> LocalVariableDeclaration
-- Header -> AllocationHeader
-- Header -> ArrayCreationHeader
/:$readableName Header:/

Header1 -> Header
-- Header1 -> ConstructorHeader
/:$readableName Header1:/

-- Header2 -> Header
-- Header2 -> EnumConstantHeader
-- /:$readableName Header2:/

CatchHeader ::= 'catch' '(' FormalParameter ')' '{'
/.$putCase consumeCatchHeader(); $break ./
/:$readableName CatchHeader:/

-- ImportDeclarations -> ImportDeclaration
-- ImportDeclarations ::= ImportDeclarations ImportDeclaration 
-- /.$putCase consumeImportDeclarations(); $break ./
-- /:$readableName ImportDeclarations:/

-- TypeDeclarations -> TypeDeclaration
-- TypeDeclarations ::= TypeDeclarations TypeDeclaration
-- /.$putCase consumeTypeDeclarations(); $break ./
-- /:$readableName TypeDeclarations:/

-- PackageDeclaration ::= PackageDeclarationName ';'
-- /.$putCase  consumePackageDeclaration(); $break ./
-- /:$readableName PackageDeclaration:/

-- PackageDeclarationName ::= Modifiers 'package' PushRealModifiers Name
-- /.$putCase  consumePackageDeclarationNameWithModifiers(); $break ./
-- /:$readableName PackageDeclarationName:/
-- /:$compliance 1.5:/

-- PackageDeclarationName ::= PackageComment 'package' Name
-- /.$putCase  consumePackageDeclarationName(); $break ./
-- /:$readableName PackageDeclarationName:/

-- PackageComment ::= $empty
-- /.$putCase  consumePackageComment(); $break ./
-- /:$readableName PackageComment:/

-- ImportDeclaration -> SingleTypeImportDeclaration
-- ImportDeclaration -> TypeImportOnDemandDeclaration
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
-- ImportDeclaration -> SingleStaticImportDeclaration
-- ImportDeclaration -> StaticImportOnDemandDeclaration
-- /:$readableName ImportDeclaration:/

-- SingleTypeImportDeclaration ::= SingleTypeImportDeclarationName ';'
-- /.$putCase consumeImportDeclaration(); $break ./
-- /:$readableName SingleTypeImportDeclaration:/
			  
-- SingleTypeImportDeclarationName ::= 'import' Name
-- /.$putCase consumeSingleTypeImportDeclarationName(); $break ./
-- /:$readableName SingleTypeImportDeclarationName:/
			  
-- TypeImportOnDemandDeclaration ::= TypeImportOnDemandDeclarationName ';'
-- /.$putCase consumeImportDeclaration(); $break ./
-- /:$readableName TypeImportOnDemandDeclaration:/

-- TypeImportOnDemandDeclarationName ::= 'import' Name '.' '*'
-- /.$putCase consumeTypeImportOnDemandDeclarationName(); $break ./
-- /:$readableName TypeImportOnDemandDeclarationName:/

-- TypeDeclaration -> ClassDeclaration
-- TypeDeclaration -> InterfaceDeclaration
-- -- this declaration in part of a list od declaration and we will
-- -- use and optimized list length calculation process 
-- -- thus we decrement the number while it will be incremend.....
-- TypeDeclaration ::= ';' 
-- /. $putCase consumeEmptyTypeDeclaration(); $break ./
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
-- TypeDeclaration -> EnumDeclaration
-- -- TypeDeclaration -> AnnotationTypeDeclaration
-- /:$readableName TypeDeclaration:/

--18.7 Only in the LALR(1) Grammar

Modifiers -> Modifier
Modifiers ::= Modifiers Modifier
/.$putCase consumeModifiers2(); $break ./
/:$readableName Modifiers:/

Modifier -> 'public' 
Modifier -> 'protected'
Modifier -> 'private'
Modifier -> 'static'
Modifier -> 'abstract'
Modifier -> 'final'
Modifier -> 'native'
Modifier -> 'synchronized'
Modifier -> 'transient'
Modifier -> 'volatile'
Modifier -> 'strictfp'
-- Modifier ::= Annotation
/.$putCase consumeAnnotationAsModifier(); $break ./
/:$readableName Modifier:/

--18.8 Productions from 8: Class Declarations
--ClassModifier ::=
--      'abstract'
--    | 'final'
--    | 'public'
--18.8.1 Productions from 8.1: Class Declarations

-- ClassDeclaration ::= ClassHeader ClassBody
-- /.$putCase consumeClassDeclaration(); $break ./
-- /:$readableName ClassDeclaration:/

-- ClassHeader ::= ClassHeaderName ClassHeaderExtendsopt ClassHeaderImplementsopt
-- /.$putCase consumeClassHeader(); $break ./
-- /:$readableName ClassHeader:/

-----------------------------------------------
-- 1.5 features : generics
-----------------------------------------------
-- ClassHeaderName ::= ClassHeaderName1 TypeParameters
-- /.$putCase consumeTypeHeaderNameWithTypeParameters(); $break ./
-- 
-- ClassHeaderName -> ClassHeaderName1
-- /:$readableName ClassHeaderName:/
-- 
-- ClassHeaderName1 ::= Modifiersopt 'class' 'Identifier'
-- /.$putCase consumeClassHeaderName1(); $break ./
-- /:$readableName ClassHeaderName:/
-- 
-- ClassHeaderExtends ::= 'extends' ClassType
-- /.$putCase consumeClassHeaderExtends(); $break ./
-- /:$readableName ClassHeaderExtends:/

-- ClassHeaderImplements ::= 'implements' InterfaceTypeList
-- /.$putCase consumeClassHeaderImplements(); $break ./
-- /:$readableName ClassHeaderImplements:/

-- InterfaceTypeList -> InterfaceType
-- InterfaceTypeList ::= InterfaceTypeList ',' InterfaceType
-- /.$putCase consumeInterfaceTypeList(); $break ./
-- /:$readableName InterfaceTypeList:/

-- InterfaceType ::= ClassOrInterfaceType
-- /.$putCase consumeInterfaceType(); $break ./
-- /:$readableName InterfaceType:/

-- ClassBody ::= '{' ClassBodyDeclarationsopt '}'
-- /:$readableName ClassBody:/
-- /:$no_statements_recovery:/

-- ClassBodyDeclarations ::= ClassBodyDeclaration
-- ClassBodyDeclarations ::= ClassBodyDeclarations ClassBodyDeclaration
-- /.$putCase consumeClassBodyDeclarations(); $break ./
-- /:$readableName ClassBodyDeclarations:/

-- ClassBodyDeclaration -> ClassMemberDeclaration
-- ClassBodyDeclaration -> StaticInitializer
-- ClassBodyDeclaration -> ConstructorDeclaration
--1.1 feature
-- ClassBodyDeclaration ::= Diet NestedMethod Block
-- /.$putCase consumeClassBodyDeclaration(); $break ./
-- /:$readableName ClassBodyDeclaration:/

Diet ::= $empty
/.$putCase consumeDiet(); $break./
/:$readableName Diet:/

-- Initializer ::= Diet NestedMethod Block
-- /.$putCase consumeClassBodyDeclaration(); $break ./
-- /:$readableName Initializer:/

-- ClassMemberDeclaration -> FieldDeclaration
-- ClassMemberDeclaration -> MethodDeclaration
--1.1 feature
-- ClassMemberDeclaration -> ClassDeclaration
--1.1 feature
-- ClassMemberDeclaration -> InterfaceDeclaration
-- 1.5 feature
-- ClassMemberDeclaration -> EnumDeclaration
-- ClassMemberDeclaration -> AnnotationTypeDeclaration
-- /:$readableName ClassMemberDeclaration:/

-- Empty declarations are not valid Java ClassMemberDeclarations.
-- However, since the current (2/14/97) Java compiler accepts them 
-- (in fact, some of the official tests contain this erroneous
-- syntax)
-- ClassMemberDeclaration ::= ';'
-- /.$putCase consumeEmptyClassMemberDeclaration(); $break./

-- GenericMethodDeclaration -> MethodDeclaration
-- -- GenericMethodDeclaration -> ConstructorDeclaration
-- /:$readableName GenericMethodDeclaration:/

--18.8.2 Productions from 8.3: Field Declarations
--VariableModifier ::=
--      'public'
--    | 'protected'
--    | 'private'
--    | 'static'
--    | 'final'
--    | 'transient'
--    | 'volatile'

-- FieldDeclaration ::= Modifiersopt 'var' VariableDeclarators ';'
-- /.$putCase consumeFieldDeclaration(); $break ./
-- /:$readableName FieldDeclaration:/

VariableDeclarators -> VariableDeclarator 
VariableDeclarators ::= VariableDeclarators ',' VariableDeclarator
/.$putCase consumeVariableDeclarators(); $break ./
/:$readableName VariableDeclarators:/

VariableDeclaratorsNoIn -> VariableDeclaratorNoIn
VariableDeclaratorsNoIn ::= VariableDeclaratorsNoIn ',' VariableDeclaratorNoIn
/.$putCase consumeVariableDeclarators(); $break ./
/:$readableName VariableDeclarators:/

VariableDeclarator ::= VariableDeclaratorId EnterVariable ExitVariableWithoutInitialization
VariableDeclarator ::= VariableDeclaratorId EnterVariable '=' ForceNoDiet VariableInitializer RestoreDiet ExitVariableWithInitialization
/:$readableName VariableDeclarator:/
/:$recovery_template Identifier:/

VariableDeclaratorNoIn ::= VariableDeclaratorId EnterVariable ExitVariableWithoutInitialization
VariableDeclaratorNoIn ::= VariableDeclaratorId EnterVariable '=' ForceNoDiet VariableInitializerNoIn RestoreDiet ExitVariableWithInitialization
/:$readableName VariableDeclarator:/
/:$recovery_template Identifier:/

EnterVariable ::= $empty
/.$putCase consumeEnterVariable(); $break ./
/:$readableName EnterVariable:/

ExitVariableWithInitialization ::= $empty
/.$putCase consumeExitVariableWithInitialization(); $break ./
/:$readableName ExitVariableWithInitialization:/

ExitVariableWithoutInitialization ::= $empty
/.$putCase consumeExitVariableWithoutInitialization(); $break ./
/:$readableName ExitVariableWithoutInitialization:/

ForceNoDiet ::= $empty
/.$putCase consumeForceNoDiet(); $break ./
/:$readableName ForceNoDiet:/
RestoreDiet ::= $empty
/.$putCase consumeRestoreDiet(); $break ./
/:$readableName RestoreDiet:/

VariableDeclaratorId ::= 'Identifier' 
/:$readableName VariableDeclaratorId:/
/:$recovery_template Identifier:/

VariableInitializer -> AssignmentExpression
-- VariableInitializer -> ArrayInitializer
/:$readableName VariableInitializer:/
/:$recovery_template Identifier:/

VariableInitializerNoIn -> AssignmentExpressionNoIn
/:$readableName VariableInitializer:/
/:$recovery_template Identifier:/

--18.8.3 Productions from 8.4: Method Declarations
--MethodModifier ::=
--      'public'
--    | 'protected'
--    | 'private'
--    | 'static'
--    | 'abstract'
--    | 'final'
--    | 'native'
--    | 'synchronized'
--

FunctionExpression ::= FunctionExpressionHeader MethodBody 
/.$putCase // set to true to consume a method with a body
  consumeFunctionExpression();  $break ./
/:$readableName FunctionExpression:/

FunctionExpressionHeader ::= FunctionExpressionHeaderName FormalParameterListopt MethodHeaderRightParen  
/.$putCase consumeMethodHeader(); $break ./
/:$readableName FunctionExpressionHeader :/

FunctionExpressionHeaderName ::= Modifiersopt 'function' 'Identifier' '('
/.$putCase consumeMethodHeaderName(false); $break ./
FunctionExpressionHeaderName ::= Modifiersopt 'function'   '('
/.$putCase consumeMethodHeaderName(true); $break ./
/:$readableName FunctionExpressionHeaderName :/


MethodDeclaration -> AbstractMethodDeclaration
MethodDeclaration ::= MethodHeader MethodBody 
/.$putCase // set to true to consume a method with a body
  consumeMethodDeclaration(true);  $break ./
/:$readableName MethodDeclaration:/

AbstractMethodDeclaration ::= MethodHeader ';'
/.$putCase // set to false to consume a method without body
  consumeMethodDeclaration(false); $break ./
/:$readableName MethodDeclaration:/

MethodHeader ::= MethodHeaderName FormalParameterListopt MethodHeaderRightParen  
/.$putCase consumeMethodHeader(); $break ./
/:$readableName MethodDeclaration:/

-- MethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
-- /.$putCase consumeMethodHeaderNameWithTypeParameters(false); $break ./
MethodHeaderName ::= Modifiersopt 'function' 'Identifier' '('
/.$putCase consumeMethodHeaderName(false); $break ./
/:$readableName MethodHeaderName:/

MethodHeaderRightParen ::= ')'
/.$putCase consumeMethodHeaderRightParen(); $break ./
/:$readableName ):/
/:$recovery_template ):/

-- MethodHeaderExtendedDims ::= Dimsopt
-- /.$putCase consumeMethodHeaderExtendedDims(); $break ./
-- /:$readableName MethodHeaderExtendedDims:/

-- MethodHeaderThrowsClause ::= 'throws' ClassTypeList
-- /.$putCase consumeMethodHeaderThrowsClause(); $break ./
-- /:$readableName MethodHeaderThrowsClause:/

-- ConstructorHeader ::= ConstructorHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderThrowsClauseopt
-- /.$putCase consumeConstructorHeader(); $break ./
-- /:$readableName ConstructorDeclaration:/

-- ConstructorHeaderName ::=  Modifiersopt TypeParameters 'Identifier' '('
-- /.$putCase consumeConstructorHeaderNameWithTypeParameters(); $break ./
-- ConstructorHeaderName ::=  Modifiersopt 'Identifier' '('
-- /.$putCase consumeConstructorHeaderName(); $break ./
-- /:$readableName ConstructorHeaderName:/

FormalParameterList -> FormalParameter
FormalParameterList ::= FormalParameterList ',' FormalParameter
/.$putCase consumeFormalParameterList(); $break ./
/:$readableName FormalParameterList:/

--1.1 feature
FormalParameter ::= VariableDeclaratorId
/.$putCase consumeFormalParameter(false); $break ./

-- ClassTypeList -> ClassTypeElt
-- ClassTypeList ::= ClassTypeList ',' ClassTypeElt
-- /.$putCase consumeClassTypeList(); $break ./
-- /:$readableName ClassTypeList:/

-- ClassTypeElt ::= ClassType
-- /.$putCase consumeClassTypeElt(); $break ./
-- /:$readableName ClassType:/

MethodBody ::= NestedMethod '{' PostDoc BlockStatementsopt '}' 
/.$putCase consumeMethodBody(); $break ./
/:$readableName MethodBody:/
-- /:$no_statements_recovery:/

NestedMethod ::= $empty
/.$putCase consumeNestedMethod(); $break ./
/:$readableName NestedMethod:/

PostDoc ::= $empty
/.$putCase consumePostDoc(); $break ./
/:$readableName PostDoc:/

--18.8.4 Productions from 8.5: Static Initializers

-- StaticInitializer ::=  StaticOnly Block
-- /.$putCase consumeStaticInitializer(); $break./
-- /:$readableName StaticInitializer:/

-- StaticOnly ::= 'static'
-- /.$putCase consumeStaticOnly(); $break ./
-- /:$readableName StaticOnly:/

--18.8.5 Productions from 8.6: Constructor Declarations
--ConstructorModifier ::=
--      'public'
--    | 'protected'
--    | 'private'
--
--
-- ConstructorDeclaration ::= ConstructorHeader MethodBody
-- /.$putCase consumeConstructorDeclaration() ; $break ./ 
-- These rules are added to be able to parse constructors with no body
-- ConstructorDeclaration ::= ConstructorHeader ';'
-- /.$putCase consumeInvalidConstructorDeclaration() ; $break ./ 
-- /:$readableName ConstructorDeclaration:/

-- the rules ExplicitConstructorInvocationopt has been expanded
-- in the rule below in order to make the grammar lalr(1).

-- ExplicitConstructorInvocation ::= 'this' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocation(0, THIS_CALL); $break ./

-- ExplicitConstructorInvocation ::= OnlyTypeArguments 'this' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocationWithTypeArguments(0,THIS_CALL); $break ./

-- ExplicitConstructorInvocation ::= 'super' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocation(0,SUPER_CALL); $break ./

-- ExplicitConstructorInvocation ::= OnlyTypeArguments 'super' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocationWithTypeArguments(0,SUPER_CALL); $break ./

--1.1 feature
-- ExplicitConstructorInvocation ::= Primary '.' 'super' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocation(1, SUPER_CALL); $break ./

-- ExplicitConstructorInvocation ::= Primary '.' OnlyTypeArguments 'super' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocationWithTypeArguments(1, SUPER_CALL); $break ./

--1.1 feature
-- ExplicitConstructorInvocation ::= Name '.' 'super' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocation(2, SUPER_CALL); $break ./

-- ExplicitConstructorInvocation ::= Name '.' OnlyTypeArguments 'super' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocationWithTypeArguments(2, SUPER_CALL); $break ./

--1.1 feature
-- ExplicitConstructorInvocation ::= Primary '.' 'this' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocation(1, THIS_CALL); $break ./

-- ExplicitConstructorInvocation ::= Primary '.' OnlyTypeArguments 'this' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocationWithTypeArguments(1, THIS_CALL); $break ./

--1.1 feature
-- ExplicitConstructorInvocation ::= Name '.' 'this' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocation(2, THIS_CALL); $break ./

-- ExplicitConstructorInvocation ::= Name '.' OnlyTypeArguments 'this' '(' ArgumentListopt ')' ';'
-- /.$putCase consumeExplicitConstructorInvocationWithTypeArguments(2, THIS_CALL); $break ./
-- /:$readableName ExplicitConstructorInvocation:/

--18.9 Productions from 9: Interface Declarations

--18.9.1 Productions from 9.1: Interface Declarations
--InterfaceModifier ::=
--      'public'
--    | 'abstract'
--
-- InterfaceDeclaration ::= InterfaceHeader InterfaceBody
-- /.$putCase consumeInterfaceDeclaration(); $break ./
-- /:$readableName InterfaceDeclaration:/

-- InterfaceHeader ::= InterfaceHeaderName InterfaceHeaderExtendsopt
-- /.$putCase consumeInterfaceHeader(); $break ./
-- /:$readableName InterfaceHeader:/

-----------------------------------------------
-- 1.5 features : generics
-----------------------------------------------
-- InterfaceHeaderName ::= InterfaceHeaderName1 TypeParameters
-- /.$putCase consumeTypeHeaderNameWithTypeParameters(); $break ./

-- InterfaceHeaderName -> InterfaceHeaderName1
-- /:$readableName InterfaceHeaderName:/

-- InterfaceHeaderName1 ::= Modifiersopt interface Identifier
-- /.$putCase consumeInterfaceHeaderName1(); $break ./
-- /:$readableName InterfaceHeaderName:/

-- InterfaceHeaderExtends ::= 'extends' InterfaceTypeList
-- /.$putCase consumeInterfaceHeaderExtends(); $break ./
-- /:$readableName InterfaceHeaderExtends:/

-- InterfaceBody ::= '{' InterfaceMemberDeclarationsopt '}' 
-- /:$readableName InterfaceBody:/

-- InterfaceMemberDeclarations -> InterfaceMemberDeclaration
-- InterfaceMemberDeclarations ::= InterfaceMemberDeclarations InterfaceMemberDeclaration
-- /.$putCase consumeInterfaceMemberDeclarations(); $break ./
-- /:$readableName InterfaceMemberDeclarations:/

--same as for class members
-- InterfaceMemberDeclaration ::= ';'
-- /.$putCase consumeEmptyInterfaceMemberDeclaration(); $break ./
-- /:$readableName InterfaceMemberDeclaration:/

-- InterfaceMemberDeclaration -> ConstantDeclaration
-- InterfaceMemberDeclaration ::= MethodHeader MethodBody
-- /.$putCase consumeInvalidMethodDeclaration(); $break ./
-- /:$readableName InterfaceMemberDeclaration:/

-- These rules are added to be able to parse constructors inside interface and then report a relevent error message
-- InvalidConstructorDeclaration ::= ConstructorHeader MethodBody
-- /.$putCase consumeInvalidConstructorDeclaration(true);  $break ./
-- InvalidConstructorDeclaration ::= ConstructorHeader ';'
-- /.$putCase consumeInvalidConstructorDeclaration(false);  $break ./
-- /:$readableName InvalidConstructorDeclaration:/

-- InterfaceMemberDeclaration -> AbstractMethodDeclaration
-- InterfaceMemberDeclaration -> InvalidConstructorDeclaration
--1.1 feature
-- InterfaceMemberDeclaration -> ClassDeclaration
--1.1 feature
-- InterfaceMemberDeclaration -> InterfaceDeclaration
-- InterfaceMemberDeclaration -> EnumDeclaration
-- InterfaceMemberDeclaration -> AnnotationTypeDeclaration
-- /:$readableName InterfaceMemberDeclaration:/

-- ConstantDeclaration -> FieldDeclaration
-- /:$readableName ConstantDeclaration:/

--- PushLeftBrace ::= $empty
-- /.$putCase consumePushLeftBrace(); $break ./
-- /:$readableName PushLeftBrace:/

PushLeftBraceObjLit ::= $empty
/.$putCase consumePushLeftBrace(); $break ./
/:$readableName PushLeftBrace:/

-- ArrayInitializer ::= '{' PushLeftBrace ,opt '}'
-- /.$putCase consumeEmptyArrayInitializer(); $break ./
-- ArrayInitializer ::= '{' PushLeftBrace VariableInitializers '}'
-- /.$putCase consumeArrayInitializer(); $break ./
-- ArrayInitializer ::= '{' PushLeftBrace VariableInitializers , '}'
-- /.$putCase consumeArrayInitializer(); $break ./
-- /:$readableName ArrayInitializer:/
-- /:$recovery_template Identifier:/

VariableInitializers ::= VariableInitializer
VariableInitializers ::= VariableInitializers ',' VariableInitializer
/.$putCase consumeVariableInitializers(); $break ./
/:$readableName VariableInitializers:/

Block ::= OpenBlock '{' BlockStatementsopt '}'
/.$putCase consumeBlock(); $break ./
/:$readableName Block:/

OpenBlock ::= $empty
/.$putCase consumeOpenBlock() ; $break ./
/:$readableName OpenBlock:/

ProgramElements -> ProgramElement
ProgramElements ::= ProgramElements ProgramElement
/.$putCase consumeProgramElements() ; $break ./
/:$readableName ProgramElements:/

-- ProgramElement -> MethodDeclaration
ProgramElement -> BlockStatement
/:$readableName ProgramElement:/


BlockStatements -> BlockStatement
BlockStatements ::= BlockStatements BlockStatement
/.$putCase consumeBlockStatements() ; $break ./
/:$readableName BlockStatements:/

BlockStatement -> LocalVariableDeclarationStatement
BlockStatement -> MethodDeclaration
BlockStatement -> Statement
--1.1 feature
-- BlockStatement -> ClassDeclaration
-- BlockStatement ::= InterfaceDeclaration
-- /.$putCase consumeInvalidInterfaceDeclaration(); $break ./
-- /:$readableName BlockStatement:/
-- BlockStatement ::= AnnotationTypeDeclaration
-- /.$putCase consumeInvalidAnnotationTypeDeclaration(); $break ./
-- /:$readableName BlockStatement:/
-- BlockStatement ::= EnumDeclaration
-- /.$putCase consumeInvalidEnumDeclaration(); $break ./
/:$readableName BlockStatement:/

LocalVariableDeclarationStatement ::= LocalVariableDeclaration ';'
/.$putCase consumeLocalVariableDeclarationStatement(); $break ./
/:$readableName LocalVariableDeclarationStatement:/

LocalVariableDeclaration ::= 'var' PushModifiers VariableDeclarators
/.$putCase consumeLocalVariableDeclaration(); $break ./
-- 1.1 feature
-- The modifiers part of this rule makes the grammar more permissive. 
-- The only modifier here is final. We put Modifiers to allow multiple modifiers
-- This will require to check the validity of the modifier
-- LocalVariableDeclaration ::= Modifiers Type PushRealModifiers VariableDeclarators
-- /.$putCase consumeLocalVariableDeclaration(); $break ./
/:$readableName LocalVariableDeclaration:/

LocalVariableDeclarationNoIn ::= 'var' PushModifiers VariableDeclaratorsNoIn
/.$putCase consumeLocalVariableDeclaration(); $break ./
/:$readableName LocalVariableDeclaration:/

PushModifiers ::= $empty
/.$putCase consumePushModifiers(); $break ./
/:$readableName PushModifiers:/

-- PushModifiersForHeader ::= $empty
-- /.$putCase consumePushModifiersForHeader(); $break ./
-- /:$readableName PushModifiersForHeader:/

-- PushRealModifiers ::= $empty
-- /.$putCase consumePushRealModifiers(); $break ./
-- /:$readableName PushRealModifiers:/

Statement -> StatementWithoutTrailingSubstatement
Statement -> LabeledStatement
Statement -> IfThenStatement
Statement -> IfThenElseStatement
Statement -> WhileStatement
Statement -> WithStatement
Statement -> ForStatement
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
-- Statement -> EnhancedForStatement
/:$readableName Statement:/
/:$recovery_template ;:/

StatementNoShortIf -> StatementWithoutTrailingSubstatement
StatementNoShortIf -> LabeledStatementNoShortIf
StatementNoShortIf -> IfThenElseStatementNoShortIf
StatementNoShortIf -> WhileStatementNoShortIf
StatementNoShortIf -> WithStatementNoShortIf
StatementNoShortIf -> ForStatementNoShortIf
-----------------------------------------------
-- 1.5 feature
-----------------------------------------------
-- StatementNoShortIf -> EnhancedForStatementNoShortIf
/:$readableName Statement:/

StatementWithoutTrailingSubstatement -> Block
StatementWithoutTrailingSubstatement -> EmptyStatement
StatementWithoutTrailingSubstatement -> ExpressionStatement
StatementWithoutTrailingSubstatement -> SwitchStatement
StatementWithoutTrailingSubstatement -> DoStatement
StatementWithoutTrailingSubstatement -> BreakStatement
StatementWithoutTrailingSubstatement -> ContinueStatement
StatementWithoutTrailingSubstatement -> ReturnStatement
-- StatementWithoutTrailingSubstatement -> SynchronizedStatement
StatementWithoutTrailingSubstatement -> ThrowStatement
StatementWithoutTrailingSubstatement -> TryStatement
/:$readableName Statement:/

EmptyStatement ::= ';'
/.$putCase consumeEmptyStatement(); $break ./
/:$readableName EmptyStatement:/
/:$empty_statement:/

LabeledStatement ::= Label ':' Statement
/.$putCase consumeStatementLabel() ; $break ./
/:$readableName LabeledStatement:/

LabeledStatementNoShortIf ::= Label ':' StatementNoShortIf
/.$putCase consumeStatementLabel() ; $break ./
/:$readableName LabeledStatement:/

Label ::= 'Identifier'
/.$putCase consumeLabel() ; $break ./
/:$readableName Label:/

ExpressionStatement ::= StatementExpression ';'
/. $putCase consumeExpressionStatement(); $break ./
-- ExpressionStatement ::= ExplicitConstructorInvocation
-- /:$readableName Statement:/

--StatementExpression ::= AssignmentStmt
StatementExpression ::= ListExpressionStmt
--StatementExpression ::= PreIncrementExpression
--StatementExpression ::= PreDecrementExpression
--StatementExpression ::= PostIncrementExpressionStmt
--StatementExpression ::= PostDecrementExpressionStmt
--StatementExpression ::= MethodInvocationStmt
-- StatementExpression ::= ClassInstanceCreationExpression
/:$readableName Expression:/


StatementExpressionNoIn ::= ListExpressionStmtNoIn
StatementExpressionNoIn ::= AssignmentStmtNoIn
StatementExpressionNoIn ::= PreIncrementExpression
StatementExpressionNoIn ::= PreDecrementExpression
StatementExpressionNoIn ::= PostIncrementExpressionStmt
StatementExpressionNoIn ::= PostDecrementExpressionStmt
StatementExpressionNoIn ::= MethodInvocationStmt
/:$readableName Expression:/

IfThenStatement ::=  'if' '(' Expression ')' BlockStatement
/.$putCase consumeStatementIfNoElse(); $break ./
/:$readableName IfStatement:/

IfThenElseStatement ::=  'if' '(' Expression ')' StatementNoShortIf 'else' BlockStatement
/.$putCase consumeStatementIfWithElse(); $break ./
/:$readableName IfStatement:/

IfThenElseStatementNoShortIf ::=  'if' '(' Expression ')' StatementNoShortIf 'else' StatementNoShortIf
/.$putCase consumeStatementIfWithElse(); $break ./
/:$readableName IfStatement:/

IfThenElseStatement ::=  'if' '(' Expression ')' LocalVariableDeclarationStatement 'else' BlockStatement
/.$putCase consumeStatementIfWithElse(); $break ./
/:$readableName IfStatement:/

IfThenElseStatementNoShortIf ::=  'if' '(' Expression ')' LocalVariableDeclarationStatement 'else' LocalVariableDeclarationStatement
/.$putCase consumeStatementIfWithElse(); $break ./
/:$readableName IfStatement:/

SwitchStatement ::= 'switch' '(' Expression ')' OpenBlock SwitchBlock
/.$putCase consumeStatementSwitch() ; $break ./
/:$readableName SwitchStatement:/

SwitchBlock ::= '{' '}'
/.$putCase consumeEmptySwitchBlock() ; $break ./

SwitchBlock ::= '{' SwitchBlockStatements '}'
SwitchBlock ::= '{' SwitchLabels '}'
SwitchBlock ::= '{' SwitchBlockStatements SwitchLabels '}'
/.$putCase consumeSwitchBlock() ; $break ./
/:$readableName SwitchBlock:/

SwitchBlockStatements -> SwitchBlockStatement
SwitchBlockStatements ::= SwitchBlockStatements SwitchBlockStatement
/.$putCase consumeSwitchBlockStatements() ; $break ./
/:$readableName SwitchBlockStatements:/

SwitchBlockStatement ::= SwitchLabels BlockStatements
/.$putCase consumeSwitchBlockStatement() ; $break ./
/:$readableName SwitchBlockStatement:/

SwitchLabels -> SwitchLabel
SwitchLabels ::= SwitchLabels SwitchLabel
/.$putCase consumeSwitchLabels() ; $break ./
/:$readableName SwitchLabels:/

SwitchLabel ::= 'case' ConstantExpression ':'
/. $putCase consumeCaseLabel(); $break ./

SwitchLabel ::= 'default' ':'
/. $putCase consumeDefaultLabel(); $break ./
/:$readableName SwitchLabel:/

WhileStatement ::= 'while' '(' Expression ')' Statement
/.$putCase consumeStatementWhile() ; $break ./
/:$readableName WhileStatement:/

WhileStatementNoShortIf ::= 'while' '(' Expression ')' StatementNoShortIf
/.$putCase consumeStatementWhile() ; $break ./
/:$readableName WhileStatement:/

WithStatement ::= 'with' '(' Expression ')' Statement
/.$putCase consumeStatementWith() ; $break ./
/:$readableName WithStatement:/

WithStatementNoShortIf ::= 'with' '(' Expression ')' StatementNoShortIf
/.$putCase consumeStatementWith() ; $break ./
/:$readableName WithStatementNoShortIf:/

DoStatement ::= 'do' Statement 'while' '(' Expression ')' ';'
/.$putCase consumeStatementDo() ; $break ./
/:$readableName DoStatement:/

ForStatement ::= 'for' '(' ForInitopt ';' Expressionopt ';' ForUpdateopt ')' Statement
/.$putCase consumeStatementFor() ; $break ./
/:$readableName ForStatement:/

ForStatement ::= 'for' '(' ForInInit 'in' Expression  ')' Statement
/.$putCase consumeStatementForIn() ; $break ./
/:$readableName ForStatement:/

ForStatementNoShortIf ::= 'for' '(' ForInitopt ';' Expressionopt ';' ForUpdateopt ')' StatementNoShortIf
/.$putCase consumeStatementFor() ; $break ./
/:$readableName ForStatement:/

ForStatementNoShortIf ::= 'for' '(' ForInInit 'in' Expression  ')' StatementNoShortIf
/.$putCase consumeStatementForIn() ; $break ./
/:$readableName ForStatement:/


-- SHOULD BE 'FullPostfixExpression', but that causes shift/reduce conflict
ForInInit ::= SimpleName
/.$putCase consumeForInInit() ; $break ./
ForInInit -> LocalVariableDeclarationNoIn
/:$readableName ForInit:/

--the minus one allows to avoid a stack-to-stack transfer
ForInit ::= StatementExpressionListNoIn
/.$putCase consumeForInit() ; $break ./
ForInit -> LocalVariableDeclarationNoIn
/:$readableName ForInit:/

ForUpdate -> StatementExpressionList
/:$readableName ForUpdate:/

StatementExpressionList -> AssignmentExpression
StatementExpressionList ::= StatementExpressionList ',' AssignmentExpression
/.$putCase consumeStatementExpressionList() ; $break ./
/:$readableName StatementExpressionList:/

StatementExpressionListNoIn -> AssignmentExpressionNoIn
StatementExpressionListNoIn ::= StatementExpressionListNoIn ',' AssignmentExpressionNoIn
/.$putCase consumeStatementExpressionList() ; $break ./
/:$readableName StatementExpressionList:/

BreakStatement ::= 'break' ';'
/.$putCase consumeStatementBreak() ; $break ./

BreakStatement ::= 'break' Identifier ';'
/.$putCase consumeStatementBreakWithLabel() ; $break ./
/:$readableName BreakStatement:/

ContinueStatement ::= 'continue' ';'
/.$putCase consumeStatementContinue() ; $break ./

ContinueStatement ::= 'continue' Identifier ';'
/.$putCase consumeStatementContinueWithLabel() ; $break ./
/:$readableName ContinueStatement:/

ReturnStatement ::= 'return' Expressionopt ';'
/.$putCase consumeStatementReturn() ; $break ./
/:$readableName ReturnStatement:/

ThrowStatement ::= 'throw' Expression ';'
/.$putCase consumeStatementThrow(); $break ./
/:$readableName ThrowStatement:/

SynchronizedStatement ::= OnlySynchronized '(' Expression ')'    Block
/.$putCase consumeStatementSynchronized(); $break ./
/:$readableName SynchronizedStatement:/

OnlySynchronized ::= 'synchronized'
/.$putCase consumeOnlySynchronized(); $break ./
/:$readableName OnlySynchronized:/

TryStatement ::= 'try' TryBlock Catches
/.$putCase consumeStatementTry(false); $break ./
TryStatement ::= 'try' TryBlock Catchesopt Finally
/.$putCase consumeStatementTry(true); $break ./
/:$readableName TryStatement:/

TryBlock ::= Block ExitTryBlock
/:$readableName Block:/

ExitTryBlock ::= $empty
/.$putCase consumeExitTryBlock(); $break ./
/:$readableName ExitTryBlock:/

Catches -> CatchClause
Catches ::= Catches CatchClause
/.$putCase consumeCatches(); $break ./
/:$readableName Catches:/

CatchClause ::= 'catch' '(' FormalParameter ')'    Block
/.$putCase consumeStatementCatch() ; $break ./
/:$readableName CatchClause:/

Finally ::= 'finally'    Block
/:$readableName Finally:/

--18.12 Productions from 14: Expressions

--for source positionning purpose
PushLPAREN ::= '('
/.$putCase consumeLeftParen(); $break ./
/:$readableName (:/
/:$recovery_template (:/
PushRPAREN ::= ')'
/.$putCase consumeRightParen(); $break ./
/:$readableName ):/
/:$recovery_template ):/

Primary -> PrimaryNoNewArray
Primary -> ArrayLiteral
Primary -> ObjectLiteral
Primary	-> 	FunctionExpression
/:$readableName Primary:/

PrimaryNoNewArray -> Literal
PrimaryNoNewArray ::= 'this'
/.$putCase consumePrimaryNoNewArrayThis(); $break ./

PrimaryNoNewArray ::=  PushLPAREN AssignmentExpression PushRPAREN 
/.$putCase consumePrimaryNoNewArray(); $break ./

-- PrimaryNoNewArray ::=  PushLPAREN Name PushRPAREN 
-- /.$putCase consumePrimaryNoNewArrayWithName(); $break ./

-- PrimaryNoNewArray -> ClassInstanceCreationExpression
-- PrimaryNoNewArray -> FieldAccess
--1.1 feature
-- PrimaryNoNewArray ::= Name '.' 'this'
-- /.$putCase consumePrimaryNoNewArrayNameThis(); $break ./
-- PrimaryNoNewArray ::= Name '.' 'super'
-- /.$putCase consumePrimaryNoNewArrayNameSuper(); $break ./

--1.1 feature
--PrimaryNoNewArray ::= Type '.' 'class'   
--inline Type in the previous rule in order to make the grammar LL1 instead 
-- of LL2. The result is the 3 next rules.

-- PrimaryNoNewArray ::= Name '.' 'class'
-- /.$putCase consumePrimaryNoNewArrayName(); $break ./

-- PrimaryNoNewArray ::= Name Dims '.' 'class'
-- /.$putCase consumePrimaryNoNewArrayArrayType(); $break ./

-- PrimaryNoNewArray ::= PrimitiveType Dims '.' 'class'
-- /.$putCase consumePrimaryNoNewArrayPrimitiveArrayType(); $break ./

-- PrimaryNoNewArray ::= PrimitiveType '.' 'class'
-- /.$putCase consumePrimaryNoNewArrayPrimitiveType(); $break ./

-- PrimaryNoNewArray -> MethodInvocation
-- PrimaryNoNewArray -> ArrayAccess
/:$readableName Expression:/
--1.1 feature
--
-- In Java 1.0 a ClassBody could not appear at all in a
-- ClassInstanceCreationExpression.
--

ObjectLiteral ::=	'{'PushLeftBraceObjLit FieldList '}'
/.$putCase consumeObjectLiteral(); $break ./
/:$readableName ObjectLiteral:/
	
FieldList	::= $empty
/.$putCase consumeEmptyFieldList(); $break ./
FieldList	-> NonemptyFieldList
/:$readableName FieldList:/
	
NonemptyFieldList	-> LiteralField
NonemptyFieldList	::= NonemptyFieldList	',' LiteralField 
/.$putCase consumeFieldList(); $break ./
/:$readableName NonemptyFieldList:/
	
LiteralField ::=	FieldName ':' AssignmentExpression
/.$putCase consumeLiteralField(); $break ./
/:$readableName LiteralField:/
	
FieldName	::= SimpleName
/.$putCase consumeFieldNameSimple(); $break ./
FieldName	-> StringLiteral
FieldName	-> CharacterLiteral
FieldName	-> IntegerLiteral
/:$readableName FieldName:/
	
ArrayLiteral ::= 	ArrayLiteralHeader  ElisionOpt ']'
/.$putCase comsumeArrayLiteral(false); $break ./
	
ArrayLiteral ::= 	ArrayLiteralHeader  ArrayLiteralElementList ']'
/.$putCase comsumeArrayLiteral(false); $break ./
/:$readableName ArrayLiteral:/
	
ArrayLiteral ::= 	ArrayLiteralHeader  ArrayLiteralElementList ',' ElisionOpt  ']'
/.$putCase comsumeArrayLiteral(true); $break ./
/:$readableName ArrayLiteral:/

ArrayLiteralHeader ::= 	'[' 
/.$putCase comsumeArrayLiteralHeader(); $break ./

ElisionOpt ::= $empty 
/.$putCase comsumeElisionEmpty(); $break ./
ElisionOpt -> Elision

Elision ::= ','
/.$putCase comsumeElisionOne(); $break ./
Elision ::= Elision ','
/.$putCase comsumeElisionList(); $break ./
 
ArrayLiteralElementList ::= ElisionOpt ArrayLiteralElement
/.$putCase comsumeArrayLiteralListOne(); $break ./
ArrayLiteralElementList ::=	ArrayLiteralElementList ',' ElisionOpt ArrayLiteralElement
/.$putCase comsumeArrayLiteralList(); $break ./
	
-- ArrayLiteralElement	::=	$empty
-- /.$putCase comsumeElementListEmpty(); $break ./
ArrayLiteralElement ::=	AssignmentExpression
/.$putCase comsumeArrayLiteralElement(); $break ./
/:$readableName ArrayLiteralElement:/

FullNewExpression	::= 'new' FullNewSubexpression '(' ArgumentListopt ')'
/.$putCase consumeFullNewExpression(); $break ./
/:$readableName FullNewExpression:/
	
FullNewSubexpression ->	Primary
FullNewSubexpression ::=	SimpleName
/.$putCase consumeFullNewSubexpressionSimpleName (); $break ./
FullNewSubexpression ->	FullNewExpression
FullNewSubexpression ::=	FullNewSubexpression PropertyOperator
/.$putCase consumeFullNewSubexpressionPropertyOperator(); $break ./
/:$readableName FullNewSubexpression:/
	
ShortNewExpression ::= 'new' ShortNewSubexpression
/.$putCase comsumeShortNewSubexpression(); $break ./
/:$readableName ShortNewExpression:/
	
	
ShortNewSubexpression -> FullNewSubexpression
ShortNewSubexpression -> ShortNewExpression
/:$readableName ShortNewSubexpression:/
	
PropertyOperator ::= '.' SimpleName
/.$putCase consumePropertyOperator() ; $break ./
PropertyOperator -> Brackets 
/.$putCase consumePropertyOperatorBrackets() ; $break ./
/:$readableName PropertyOperator:/

Brackets ::= '[' Expression ']'
/.$putCase consumeListExpressionBrackets() ; $break ./
/:$readableName Brackets:/


ListExpression -> AssignmentExpression
ListExpression ::= ListExpression ',' AssignmentExpression
/.$putCase consumeListExpression(); $break ./
/:$readableName ListExpression:/

ListExpressionNoIn -> AssignmentExpressionNoIn
ListExpressionNoIn ::= ListExpressionNoIn ',' AssignmentExpressionNoIn
/.$putCase consumeListExpression(); $break ./
/:$readableName ListExpression:/

ListExpressionStmt -> AssignmentExpressionStmt
ListExpressionStmt ::= ListExpressionStmt ',' AssignmentExpressionStmt
/.$putCase consumeListExpression(); $break ./
/:$readableName ListExpression:/


ListExpressionStmtNoIn -> AssignmentStmtNoIn
ListExpressionStmtNoIn ::= ListExpressionStmtNoIn ',' AssignmentStmtNoIn
/.$putCase consumeListExpression(); $break ./
/:$readableName ListExpression:/



-- AllocationHeader ::= 'new' ClassType '(' ArgumentListopt ')'
-- /.$putCase consumeAllocationHeader(); $break ./
-- /:$readableName AllocationHeader:/

-- ClassInstanceCreationExpression ::= 'new' OnlyTypeArguments ClassType '(' ArgumentListopt ')' ClassBodyopt
-- /.$putCase consumeClassInstanceCreationExpressionWithTypeArguments(); $break ./

-- ClassInstanceCreationExpression ::= 'new' ClassType '(' ArgumentListopt ')' ClassBodyopt
-- /.$putCase consumeClassInstanceCreationExpression(); $break ./
--1.1 feature

-- ClassInstanceCreationExpression ::= Primary '.' 'new' OnlyTypeArguments ClassType '(' ArgumentListopt ')' ClassBodyopt
-- /.$putCase consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() ; $break ./

-- ClassInstanceCreationExpression ::= Primary '.' 'new' ClassType '(' ArgumentListopt ')' ClassBodyopt
-- /.$putCase consumeClassInstanceCreationExpressionQualified() ; $break ./

--1.1 feature
-- ClassInstanceCreationExpression ::= ClassInstanceCreationExpressionName 'new' ClassType '(' ArgumentListopt ')' ClassBodyopt
-- /.$putCase consumeClassInstanceCreationExpressionQualified() ; $break ./
-- /:$readableName ClassInstanceCreationExpression:/

-- ClassInstanceCreationExpression ::= ClassInstanceCreationExpressionName 'new' OnlyTypeArguments ClassType '(' ArgumentListopt ')' ClassBodyopt
-- /.$putCase consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() ; $break ./
-- /:$readableName ClassInstanceCreationExpression:/

-- ClassInstanceCreationExpressionName ::= Name '.'
-- /.$putCase consumeClassInstanceCreationExpressionName() ; $break ./
-- /:$readableName ClassInstanceCreationExpressionName:/

-- ClassBodyopt ::= $empty --test made using null as contents
-- /.$putCase consumeClassBodyopt(); $break ./
-- ClassBodyopt ::= EnterAnonymousClassBody ClassBody
-- /:$readableName ClassBody:/
-- /:$no_statements_recovery:/

-- EnterAnonymousClassBody ::= $empty
-- /.$putCase consumeEnterAnonymousClassBody(); $break ./
-- /:$readableName EnterAnonymousClassBody:/

ArgumentList ::= AssignmentExpression
ArgumentList ::= ArgumentList ',' AssignmentExpression
/.$putCase consumeArgumentList(); $break ./
/:$readableName ArgumentList:/

-- ArrayCreationHeader ::= 'new' PrimitiveType DimWithOrWithOutExprs
-- /.$putCase consumeArrayCreationHeader(); $break ./

-- ArrayCreationHeader ::= 'new' ClassOrInterfaceType DimWithOrWithOutExprs
-- /.$putCase consumeArrayCreationHeader(); $break ./
-- /:$readableName ArrayCreationHeader:/

-- ArrayCreationWithoutArrayInitializer ::= 'new' PrimitiveType DimWithOrWithOutExprs
-- /.$putCase consumeArrayCreationExpressionWithoutInitializer(); $break ./
-- /:$readableName ArrayCreationWithoutArrayInitializer:/

-- ArrayCreationWithArrayInitializer ::= 'new' PrimitiveType DimWithOrWithOutExprs ArrayInitializer
-- /.$putCase consumeArrayCreationExpressionWithInitializer(); $break ./
-- /:$readableName ArrayCreationWithArrayInitializer:/

-- ArrayCreationWithoutArrayInitializer ::= 'new' ClassOrInterfaceType DimWithOrWithOutExprs
-- /.$putCase consumeArrayCreationExpressionWithoutInitializer(); $break ./

-- ArrayCreationWithArrayInitializer ::= 'new' ClassOrInterfaceType DimWithOrWithOutExprs ArrayInitializer
-- /.$putCase consumeArrayCreationExpressionWithInitializer(); $break ./

-- DimWithOrWithOutExprs ::= DimWithOrWithOutExpr
-- DimWithOrWithOutExprs ::= DimWithOrWithOutExprs DimWithOrWithOutExpr
-- /.$putCase consumeDimWithOrWithOutExprs(); $break ./
-- /:$readableName Dimensions:/

-- DimWithOrWithOutExpr ::= '[' Expression ']'
-- DimWithOrWithOutExpr ::= '[' ']'
-- /. $putCase consumeDimWithOrWithOutExpr(); $break ./
-- /:$readableName Dimension:/
-- -----------------------------------------------

-- Dims ::= DimsLoop
-- /. $putCase consumeDims(); $break ./
-- /:$readableName Dimensions:/
-- DimsLoop -> OneDimLoop
-- DimsLoop ::= DimsLoop OneDimLoop
-- /:$readableName Dimensions:/
-- OneDimLoop ::= '[' ']'
-- /. $putCase consumeOneDimLoop(); $break ./
-- /:$readableName Dimension:/

FieldAccess ::= Primary '.' 'Identifier'
/.$putCase consumeFieldAccess(false); $break ./

-- FieldAccess ::= 'super' '.' 'Identifier'
-- /.$putCase consumeFieldAccess(true); $break ./
-- /:$readableName FieldAccess:/

-- MethodInvocation ::= Name '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationName(); $break ./

-- MethodInvocation ::= Name '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationNameWithTypeArguments(); $break ./

-- MethodInvocation ::= Primary '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationPrimaryWithTypeArguments(); $break ./

-- MethodInvocation ::= Primary '.' 'Identifier' '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationPrimary(); $break ./

-- MethodInvocation ::= 'super' '.' OnlyTypeArguments 'Identifier' '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationSuperWithTypeArguments(); $break ./

-- MethodInvocation ::= 'super' '.' 'Identifier' '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationSuper(); $break ./

-- MethodInvocation ::= SimpleName '(' ArgumentListopt ')'
-- /.$putCase consumeMethodInvocationName(); $break ./

MethodInvocation ::= FullPostfixExpression '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationPrimary(); $break ./
/:$readableName MethodInvocation:/

-- ArrayAccess ::= Name '[' Expression ']'
-- /.$putCase consumeArrayAccess(true); $break ./
-- ArrayAccess ::= PrimaryNoNewArray '[' Expression ']'
-- /.$putCase consumeArrayAccess(false); $break ./
-- ArrayAccess ::= ArrayCreationWithArrayInitializer '[' Expression ']'
-- /.$putCase consumeArrayAccess(false); $break ./
-- /:$readableName ArrayAccess:/



PostfixExpression	->	FullPostfixExpression
PostfixExpression	->	ShortNewExpression

-- PostfixExpression -> Primary
-- PostfixExpression ::= Name
-- /.$putCase consumePostfixExpression(); $break ./
-- PostfixExpression -> PostIncrementExpression
-- PostfixExpression -> PostDecrementExpression
-- /:$readableName Expression:/

FullPostfixExpression ->	Primary
-- used identifier instead FullPostfixExpression ->	ExpressionQualifiedIdentifier
FullPostfixExpression ::=	SimpleName
/.$putCase consumePostfixExpression(); $break ./
FullPostfixExpression ->	FullNewExpression
FullPostfixExpression ::=	FullPostfixExpression PropertyOperator
/.$putCase consumeFullPropertyOperator(); $break ./
FullPostfixExpression ::=	MethodInvocation 
/.$putCase consumeMethodInvocation(); $break ./
FullPostfixExpression -> PostIncrementExpression
FullPostfixExpression -> PostDecrementExpression
-- /:$readableName FullPostfixExpression:/



PostIncrementExpression ::= PostfixExpression '++'
/.$putCase consumeUnaryExpression(OperatorIds.PLUS,true); $break ./
/:$readableName PostIncrementExpression:/

PostDecrementExpression ::= PostfixExpression '--'
/.$putCase consumeUnaryExpression(OperatorIds.MINUS,true); $break ./
/:$readableName PostDecrementExpression:/

--for source managment purpose
PushPosition ::= $empty
 /.$putCase consumePushPosition(); $break ./
/:$readableName PushPosition:/

UnaryExpression -> PreIncrementExpression
UnaryExpression -> PreDecrementExpression
UnaryExpression ::= '+' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.PLUS); $break ./
UnaryExpression ::= '-' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.MINUS); $break ./
UnaryExpression -> UnaryExpressionNotPlusMinus
/:$readableName Expression:/

PreIncrementExpression ::= '++' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.PLUS,false); $break ./
/:$readableName PreIncrementExpression:/

PreDecrementExpression ::= '--' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.MINUS,false); $break ./
/:$readableName PreDecrementExpression:/

UnaryExpressionNotPlusMinus -> PostfixExpression
UnaryExpressionNotPlusMinus ::= '~' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.TWIDDLE); $break ./
UnaryExpressionNotPlusMinus ::= '!' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.NOT); $break ./
UnaryExpressionNotPlusMinus ::= 'delete' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.DELETE); $break ./
UnaryExpressionNotPlusMinus ::= 'void' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.VOID); $break ./
UnaryExpressionNotPlusMinus ::= 'typeof' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.TYPEOF); $break ./
-- UnaryExpressionNotPlusMinus -> CastExpression
/:$readableName Expression:/

-- CastExpression ::= PushLPAREN PrimitiveType Dimsopt PushRPAREN InsideCastExpression UnaryExpression
-- /.$putCase consumeCastExpressionWithPrimitiveType(); $break ./
-- CastExpression ::= PushLPAREN Name OnlyTypeArgumentsForCastExpression Dimsopt PushRPAREN InsideCastExpression UnaryExpressionNotPlusMinus
-- /.$putCase consumeCastExpressionWithGenericsArray(); $break ./
-- CastExpression ::= PushLPAREN Name OnlyTypeArgumentsForCastExpression '.' ClassOrInterfaceType Dimsopt PushRPAREN InsideCastExpressionWithQualifiedGenerics UnaryExpressionNotPlusMinus
-- /.$putCase consumeCastExpressionWithQualifiedGenericsArray(); $break ./
-- CastExpression ::= PushLPAREN Name PushRPAREN InsideCastExpressionLL1 UnaryExpressionNotPlusMinus
-- /.$putCase consumeCastExpressionLL1(); $break ./
-- CastExpression ::= PushLPAREN Name Dims PushRPAREN InsideCastExpression UnaryExpressionNotPlusMinus
-- /.$putCase consumeCastExpressionWithNameArray(); $break ./
-- /:$readableName CastExpression:/

-- OnlyTypeArgumentsForCastExpression ::= OnlyTypeArguments
-- /.$putCase consumeOnlyTypeArgumentsForCastExpression(); $break ./
-- /:$readableName TypeArguments:/

-- InsideCastExpression ::= $empty
-- /.$putCase consumeInsideCastExpression(); $break ./
-- /:$readableName InsideCastExpression:/
-- InsideCastExpressionLL1 ::= $empty
-- /.$putCase consumeInsideCastExpressionLL1(); $break ./
-- /:$readableName InsideCastExpression:/
-- InsideCastExpressionWithQualifiedGenerics ::= $empty
-- /.$putCase consumeInsideCastExpressionWithQualifiedGenerics(); $break ./
-- /:$readableName InsideCastExpression:/

MultiplicativeExpression -> UnaryExpression
MultiplicativeExpression ::= MultiplicativeExpression '*' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.MULTIPLY); $break ./
MultiplicativeExpression ::= MultiplicativeExpression '/' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.DIVIDE); $break ./
MultiplicativeExpression ::= MultiplicativeExpression '%' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.REMAINDER); $break ./
/:$readableName Expression:/

AdditiveExpression -> MultiplicativeExpression
AdditiveExpression ::= AdditiveExpression '+' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.PLUS); $break ./
AdditiveExpression ::= AdditiveExpression '-' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.MINUS); $break ./
/:$readableName Expression:/

ShiftExpression -> AdditiveExpression
ShiftExpression ::= ShiftExpression '<<'  AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.LEFT_SHIFT); $break ./
ShiftExpression ::= ShiftExpression '>>'  AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.RIGHT_SHIFT); $break ./
ShiftExpression ::= ShiftExpression '>>>' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
/:$readableName Expression:/


--
--RelationalExpression 
--
RelationalExpression -> ShiftExpression
RelationalExpression ::= RelationalExpression '<'  ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS); $break ./
RelationalExpression ::= RelationalExpression '>'  ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER); $break ./
RelationalExpression ::= RelationalExpression '<=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS_EQUAL); $break ./
RelationalExpression ::= RelationalExpression '>=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER_EQUAL); $break ./
RelationalExpression ::= RelationalExpression 'instanceof' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.INSTANCEOF); $break ./
RelationalExpression ::= RelationalExpression 'in' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.IN); $break ./
/:$readableName Expression:/

RelationalExpressionNoIn -> ShiftExpression
RelationalExpressionNoIn ::= RelationalExpressionNoIn '<'  ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS); $break ./
RelationalExpressionNoIn ::= RelationalExpressionNoIn '>'  ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER); $break ./
RelationalExpressionNoIn ::= RelationalExpressionNoIn '<=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS_EQUAL); $break ./
RelationalExpressionNoIn ::= RelationalExpressionNoIn '>=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER_EQUAL); $break ./
RelationalExpressionNoIn ::= RelationalExpressionNoIn 'instanceof' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.INSTANCEOF); $break ./
/:$readableName Expression:/

--
--EqualityExpression
--

EqualityExpression -> RelationalExpression 
EqualityExpression ::= EqualityExpression '==' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL); $break ./
EqualityExpression ::= EqualityExpression '!=' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL); $break ./
EqualityExpression ::= EqualityExpression '===' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL_EQUAL); $break ./
EqualityExpression ::= EqualityExpression '!==' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL_EQUAL); $break ./
/:$readableName Expression:/

EqualityExpressionNoIn -> RelationalExpressionNoIn 
EqualityExpressionNoIn ::= EqualityExpressionNoIn '==' RelationalExpressionNoIn
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL); $break ./
EqualityExpressionNoIn ::= EqualityExpressionNoIn '!=' RelationalExpressionNoIn
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL); $break ./
EqualityExpressionNoIn ::= EqualityExpressionNoIn '===' RelationalExpressionNoIn
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL_EQUAL); $break ./
EqualityExpressionNoIn ::= EqualityExpressionNoIn '!==' RelationalExpressionNoIn
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL_EQUAL); $break ./
/:$readableName Expression:/

--
--AndExpression 
--
AndExpression -> EqualityExpression
AndExpression ::= AndExpression '&' EqualityExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND); $break ./
/:$readableName Expression:/

AndExpressionNoIn -> EqualityExpressionNoIn
AndExpressionNoIn ::= AndExpressionNoIn '&' EqualityExpressionNoIn
/.$putCase consumeBinaryExpression(OperatorIds.AND); $break ./
/:$readableName Expression:/

--
--ExclusiveOrExpression 
--
ExclusiveOrExpression -> AndExpression
ExclusiveOrExpression ::= ExclusiveOrExpression '^' AndExpression
/.$putCase consumeBinaryExpression(OperatorIds.XOR); $break ./
/:$readableName Expression:/

ExclusiveOrExpressionNoIn -> AndExpressionNoIn
ExclusiveOrExpressionNoIn ::= ExclusiveOrExpressionNoIn '^' AndExpressionNoIn
/.$putCase consumeBinaryExpression(OperatorIds.XOR); $break ./
/:$readableName Expression:/

--
--InclusiveOrExpression 
--
InclusiveOrExpression -> ExclusiveOrExpression
InclusiveOrExpression ::= InclusiveOrExpression '|' ExclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR); $break ./
/:$readableName Expression:/

InclusiveOrExpressionNoIn -> ExclusiveOrExpressionNoIn
InclusiveOrExpressionNoIn ::= InclusiveOrExpressionNoIn '|' ExclusiveOrExpressionNoIn
/.$putCase consumeBinaryExpression(OperatorIds.OR); $break ./
/:$readableName Expression:/

--
--ConditionalAndExpression 
--
ConditionalAndExpression -> InclusiveOrExpression
ConditionalAndExpression ::= ConditionalAndExpression '&&' InclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND_AND); $break ./
/:$readableName Expression:/

ConditionalAndExpressionNoIn -> InclusiveOrExpressionNoIn
ConditionalAndExpressionNoIn ::= ConditionalAndExpressionNoIn '&&' InclusiveOrExpressionNoIn
/.$putCase consumeBinaryExpression(OperatorIds.AND_AND); $break ./
/:$readableName Expression:/

--
--ConditionalOrExpression 
--
ConditionalOrExpression -> ConditionalAndExpression
ConditionalOrExpression ::= ConditionalOrExpression '||' ConditionalAndExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR_OR); $break ./
/:$readableName Expression:/

ConditionalOrExpressionNoIn -> ConditionalAndExpressionNoIn
ConditionalOrExpressionNoIn ::= ConditionalOrExpressionNoIn '||' ConditionalAndExpressionNoIn
/.$putCase consumeBinaryExpression(OperatorIds.OR_OR); $break ./
/:$readableName Expression:/

--
--ConditionalExpression 
--
ConditionalExpression -> ConditionalOrExpression
ConditionalExpression ::= ConditionalOrExpression '?' AssignmentExpression ':' AssignmentExpression 
/.$putCase consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ; $break ./
/:$readableName Expression:/

ConditionalExpressionNoIn -> ConditionalOrExpressionNoIn
ConditionalExpressionNoIn ::= ConditionalOrExpressionNoIn '?' AssignmentExpressionNoIn ':' AssignmentExpressionNoIn 
/.$putCase consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ; $break ./
/:$readableName Expression:/

--
--AssignmentExpression 
--
AssignmentExpression -> ConditionalExpression
AssignmentExpression -> Assignment
/:$readableName Expression:/
/:$recovery_template Identifier:/

AssignmentExpressionNoIn -> ConditionalExpressionNoIn
AssignmentExpressionNoIn -> AssignmentNoIn
/:$readableName Expression:/
/:$recovery_template Identifier:/

--
--Assignment
--
Assignment ::= PostfixExpression AssignmentOperator AssignmentExpression
/.$putCase consumeAssignment(); $break ./
/:$readableName Assignment:/

AssignmentNoIn ::= PostfixExpression AssignmentOperator AssignmentExpressionNoIn
/.$putCase consumeAssignment(); $break ./
/:$readableName Assignment:/

-- this rule is added to parse an array initializer in a assigment and then report a syntax error knowing the exact senario
-- InvalidArrayInitializerAssignement ::= PostfixExpression AssignmentOperator ArrayInitializer
-- /:$readableName ArrayInitializerAssignment:/
-- /:$recovery:/
-- Assignment ::= InvalidArrayInitializerAssignement
-- /.$putcase ignoreExpressionAssignment();$break ./
-- /:$recovery:/

AssignmentOperator ::= '='
/.$putCase consumeAssignmentOperator(EQUAL); $break ./
AssignmentOperator ::= '*='
/.$putCase consumeAssignmentOperator(MULTIPLY); $break ./
AssignmentOperator ::= '/='
/.$putCase consumeAssignmentOperator(DIVIDE); $break ./
AssignmentOperator ::= '%='
/.$putCase consumeAssignmentOperator(REMAINDER); $break ./
AssignmentOperator ::= '+='
/.$putCase consumeAssignmentOperator(PLUS); $break ./
AssignmentOperator ::= '-='
/.$putCase consumeAssignmentOperator(MINUS); $break ./
AssignmentOperator ::= '<<='
/.$putCase consumeAssignmentOperator(LEFT_SHIFT); $break ./
AssignmentOperator ::= '>>='
/.$putCase consumeAssignmentOperator(RIGHT_SHIFT); $break ./
AssignmentOperator ::= '>>>='
/.$putCase consumeAssignmentOperator(UNSIGNED_RIGHT_SHIFT); $break ./
AssignmentOperator ::= '&='
/.$putCase consumeAssignmentOperator(AND); $break ./
AssignmentOperator ::= '^='
/.$putCase consumeAssignmentOperator(XOR); $break ./
AssignmentOperator ::= '|='
/.$putCase consumeAssignmentOperator(OR); $break ./
/:$readableName AssignmentOperator:/
/:$recovery_template =:/

-- Expression -> AssignmentExpression
Expression -> ListExpression
/:$readableName Expression:/
/:$recovery_template Identifier:/

-- ExpressionNoIn -> AssignmentExpressionNoIn
ExpressionNoIn -> ListExpressionNoIn
/:$readableName Expression:/
/:$recovery_template Identifier:/

-- The following rules are for optional nonterminals.
--
-- ClassHeaderExtendsopt ::= $empty
-- ClassHeaderExtendsopt -> ClassHeaderExtends
-- /:$readableName ClassHeaderExtends:/

Expressionopt ::= $empty
/.$putCase consumeEmptyExpression(); $break ./
Expressionopt -> Expression
/:$readableName Expression:/

ConstantExpression -> Expression
/:$readableName ConstantExpression:/



--
-- Disambigate OpenBlock and ObjectLiteral
--
PrimaryStmt -> PrimaryNoNewArrayStmt
PrimaryStmt -> ArrayLiteral
/:$readableName PrimaryStmt:/

PrimaryNoNewArrayStmt -> Literal
PrimaryNoNewArrayStmt ::= 'this'
/.$putCase consumePrimaryNoNewArrayThis(); $break ./

PrimaryNoNewArrayStmt ::=  PushLPAREN AssignmentExpression PushRPAREN 
/.$putCase consumePrimaryNoNewArray(); $break ./

 


FullNewExpressionStmt	::= 'new' FullNewSubexpression '(' ArgumentListopt ')'
/.$putCase consumeFullNewExpression(); $break ./
/:$readableName FullNewExpression:/
	
FullNewSubexpressionStmt ->	PrimaryStmt
FullNewSubexpressionStmt ::=	SimpleName
/.$putCase consumeFullNewSubexpressionSimpleName (); $break ./
FullNewSubexpressionStmt ->	FullNewExpressionStmt
FullNewSubexpressionStmt ::=	FullNewSubexpressionStmt PropertyOperator
/.$putCase consumeFullNewSubexpressionPropertyOperator(); $break ./
/:$readableName FullNewSubexpression:/
	
ShortNewExpressionStmt ::= 'new' ShortNewSubexpression
/.$putCase comsumeShortNewSubexpression(); $break ./
/:$readableName ShortNewExpression:/
	
	
ShortNewSubexpressionStmt -> FullNewSubexpressionStmt
ShortNewSubexpressionStmt -> ShortNewExpressionStmt
/:$readableName ShortNewSubexpression:/
	
--MethodInvocationStmt ::= SimpleName '(' ArgumentListopt ')'
--/.$putCase consumeMethodInvocationName(); $break ./

MethodInvocationStmt ::= FullPostfixExpressionStmt '(' ArgumentListopt ')'
/.$putCase consumeMethodInvocationPrimary(); $break ./
/:$readableName MethodInvocation:/

PostfixExpressionStmt	->	FullPostfixExpressionStmt
PostfixExpressionStmt	->	ShortNewExpressionStmt

 

FullPostfixExpressionStmt ->	PrimaryStmt
-- used identifier instead FullPostfixExpression ->	ExpressionQualifiedIdentifier
FullPostfixExpressionStmt ::=	SimpleName
/.$putCase consumePostfixExpression(); $break ./
FullPostfixExpressionStmt ->	FullNewExpressionStmt
FullPostfixExpressionStmt ::=	FullPostfixExpressionStmt PropertyOperator
/.$putCase consumeFullPropertyOperator(); $break ./
FullPostfixExpressionStmt ::=	MethodInvocationStmt 
/.$putCase consumeMethodInvocation(); $break ./
FullPostfixExpressionStmt -> PostIncrementExpressionStmt
FullPostfixExpressionStmt -> PostDecrementExpressionStmt
-- /:$readableName FullPostfixExpression:/



PostIncrementExpressionStmt ::= PostfixExpressionStmt '++'
/.$putCase consumeUnaryExpression(OperatorIds.PLUS,true); $break ./
/:$readableName PostIncrementExpression:/

PostDecrementExpressionStmt ::= PostfixExpressionStmt '--'
/.$putCase consumeUnaryExpression(OperatorIds.MINUS,true); $break ./
/:$readableName PostDecrementExpression:/

UnaryExpressionStmt -> PreIncrementExpression
UnaryExpressionStmt -> PreDecrementExpression
UnaryExpressionStmt ::= '+' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.PLUS); $break ./
UnaryExpressionStmt ::= '-' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.MINUS); $break ./
UnaryExpressionStmt -> UnaryExpressionNotPlusMinusStmt
/:$readableName Expression:/

--PreIncrementExpression ::= '++' PushPosition UnaryExpression
--/.$putCase consumeUnaryExpression(OperatorIds.PLUS,false); $break ./
--/:$readableName PreIncrementExpression:/

--PreDecrementExpression ::= '--' PushPosition UnaryExpression
--/.$putCase consumeUnaryExpression(OperatorIds.MINUS,false); $break ./
--/:$readableName PreDecrementExpression:/

UnaryExpressionNotPlusMinusStmt -> PostfixExpressionStmt
UnaryExpressionNotPlusMinusStmt ::= '~' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.TWIDDLE); $break ./
UnaryExpressionNotPlusMinusStmt ::= '!' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.NOT); $break ./
UnaryExpressionNotPlusMinusStmt ::= 'delete' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.DELETE); $break ./
UnaryExpressionNotPlusMinusStmt ::= 'void' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.VOID); $break ./
UnaryExpressionNotPlusMinusStmt ::= 'typeof' PushPosition UnaryExpression
/.$putCase consumeUnaryExpression(OperatorIds.TYPEOF); $break ./
-- UnaryExpressionNotPlusMinus -> CastExpression
/:$readableName Expression:/


MultiplicativeExpressionStmt -> UnaryExpressionStmt
MultiplicativeExpressionStmt ::= MultiplicativeExpressionStmt '*' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.MULTIPLY); $break ./
MultiplicativeExpressionStmt ::= MultiplicativeExpressionStmt '/' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.DIVIDE); $break ./
MultiplicativeExpressionStmt ::= MultiplicativeExpressionStmt '%' UnaryExpression
/.$putCase consumeBinaryExpression(OperatorIds.REMAINDER); $break ./
/:$readableName Expression:/

AdditiveExpressionStmt -> MultiplicativeExpressionStmt
AdditiveExpressionStmt ::= AdditiveExpressionStmt '+' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.PLUS); $break ./
AdditiveExpressionStmt ::= AdditiveExpressionStmt '-' MultiplicativeExpression
/.$putCase consumeBinaryExpression(OperatorIds.MINUS); $break ./
/:$readableName Expression:/

ShiftExpressionStmt -> AdditiveExpressionStmt
ShiftExpressionStmt ::= ShiftExpressionStmt '<<'  AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.LEFT_SHIFT); $break ./
ShiftExpressionStmt ::= ShiftExpressionStmt '>>'  AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.RIGHT_SHIFT); $break ./
ShiftExpressionStmt ::= ShiftExpressionStmt '>>>' AdditiveExpression
/.$putCase consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
/:$readableName Expression:/

RelationalExpressionStmt -> ShiftExpressionStmt
RelationalExpressionStmt ::= RelationalExpressionStmt '<'  ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS); $break ./
RelationalExpressionStmt ::= RelationalExpressionStmt '>'  ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER); $break ./
RelationalExpressionStmt ::= RelationalExpressionStmt '<=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.LESS_EQUAL); $break ./
RelationalExpressionStmt ::= RelationalExpressionStmt '>=' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.GREATER_EQUAL); $break ./
RelationalExpressionStmt ::= RelationalExpressionStmt 'instanceof' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.INSTANCEOF); $break ./
RelationalExpressionStmt ::= RelationalExpressionStmt 'in' ShiftExpression
/.$putCase consumeBinaryExpression(OperatorIds.IN); $break ./
/:$readableName Expression:/

EqualityExpressionStmt -> RelationalExpressionStmt 
EqualityExpressionStmt ::= EqualityExpressionStmt '==' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL); $break ./
EqualityExpressionStmt ::= EqualityExpressionStmt '!=' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL); $break ./
EqualityExpressionStmt ::= EqualityExpressionStmt '===' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL_EQUAL); $break ./
EqualityExpressionStmt ::= EqualityExpressionStmt '!==' RelationalExpression
/.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL_EQUAL); $break ./
/:$readableName Expression:/

AndExpressionStmt -> EqualityExpressionStmt
AndExpressionStmt ::= AndExpressionStmt '&' EqualityExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND); $break ./
/:$readableName Expression:/

ExclusiveOrExpressionStmt -> AndExpressionStmt
ExclusiveOrExpressionStmt ::= ExclusiveOrExpressionStmt '^' AndExpression
/.$putCase consumeBinaryExpression(OperatorIds.XOR); $break ./
/:$readableName Expression:/

InclusiveOrExpressionStmt -> ExclusiveOrExpressionStmt
InclusiveOrExpressionStmt ::= InclusiveOrExpressionStmt '|' ExclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR); $break ./
/:$readableName Expression:/

ConditionalAndExpressionStmt -> InclusiveOrExpressionStmt
ConditionalAndExpressionStmt ::= ConditionalAndExpressionStmt '&&' InclusiveOrExpression
/.$putCase consumeBinaryExpression(OperatorIds.AND_AND); $break ./
/:$readableName Expression:/

ConditionalOrExpressionStmt -> ConditionalAndExpressionStmt
ConditionalOrExpressionStmt ::= ConditionalOrExpressionStmt '||' ConditionalAndExpression
/.$putCase consumeBinaryExpression(OperatorIds.OR_OR); $break ./
/:$readableName Expression:/

ConditionalExpressionStmt -> ConditionalOrExpressionStmt
ConditionalExpressionStmt ::= ConditionalOrExpressionStmt '?' AssignmentExpressionStmt ':' AssignmentExpressionStmt
/.$putCase consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ; $break ./
/:$readableName Expression:/

AssignmentExpressionStmt -> ConditionalExpressionStmt
AssignmentExpressionStmt -> AssignmentStmt
/:$readableName Expression:/
/:$recovery_template Identifier:/

AssignmentStmt ::= PostfixExpressionStmt AssignmentOperator AssignmentExpression
/.$putCase consumeAssignment(); $break ./
/:$readableName AssignmentStmt:/

AssignmentStmtNoIn ::= PostfixExpressionStmt AssignmentOperator AssignmentExpressionNoIn
/.$putCase consumeAssignment(); $break ./
/:$readableName AssignmentStmt:/

-- this rule is added to parse an array initializer in a assigment and then report a syntax error knowing the exact senario
-- InvalidArrayInitializerAssignement ::= PostfixExpression AssignmentOperator ArrayInitializer
-- /:$readableName ArrayInitializerAssignment:/
-- /:$recovery:/
-- Assignment ::= InvalidArrayInitializerAssignement
-- /.$putcase ignoreExpressionAssignment();$break ./
-- /:$recovery:/


---------------------------------------------------------------------------------------
--
-- The rules below are for optional terminal symbols.  An optional comma,
-- is only used in the context of an array initializer - It is a
-- "syntactic sugar" that otherwise serves no other purpose. By contrast,
-- an optional identifier is used in the definition of a break and 
-- continue statement. When the identifier does not appear, a NULL
-- is produced. When the identifier is present, the user should use the
-- corresponding TOKEN(i) method. See break statement as an example.
--
---------------------------------------------------------------------------------------

,opt -> $empty
,opt -> ,
/:$readableName ,:/

-- ClassBodyDeclarationsopt ::= $empty
-- /.$putCase consumeEmptyClassBodyDeclarationsopt(); $break ./
-- ClassBodyDeclarationsopt ::= NestedType ClassBodyDeclarations
-- /.$putCase consumeClassBodyDeclarationsopt(); $break ./
-- /:$readableName ClassBodyDeclarations:/

Modifiersopt ::= $empty 
/. $putCase consumeDefaultModifiers(); $break ./
-- Modifiersopt ::= Modifiers
-- /.$putCase consumeModifiers(); $break ./ 
/:$readableName Modifiers:/

BlockStatementsopt ::= $empty
/.$putCase consumeEmptyBlockStatementsopt(); $break ./
BlockStatementsopt -> BlockStatements
/:$readableName BlockStatements:/

-- Dimsopt ::= $empty
-- /. $putCase consumeEmptyDimsopt(); $break ./
-- Dimsopt -> Dims
-- /:$readableName Dimensions:/

ArgumentListopt ::= $empty
/. $putCase consumeEmptyArgumentListopt(); $break ./
ArgumentListopt -> ArgumentList
/:$readableName ArgumentList:/

-- MethodHeaderThrowsClauseopt ::= $empty
-- MethodHeaderThrowsClauseopt -> MethodHeaderThrowsClause
-- /:$readableName MethodHeaderThrowsClause:/

FormalParameterListopt ::= $empty
/.$putcase consumeFormalParameterListopt(); $break ./
FormalParameterListopt -> FormalParameterList
/:$readableName FormalParameterList:/

-- ClassHeaderImplementsopt ::= $empty
-- ClassHeaderImplementsopt -> ClassHeaderImplements
-- /:$readableName ClassHeaderImplements:/

-- InterfaceMemberDeclarationsopt ::= $empty
-- /. $putCase consumeEmptyInterfaceMemberDeclarationsopt(); $break ./
-- InterfaceMemberDeclarationsopt ::= NestedType InterfaceMemberDeclarations
-- /. $putCase consumeInterfaceMemberDeclarationsopt(); $break ./
-- /:$readableName InterfaceMemberDeclarations:/

NestedType ::= $empty 
/.$putCase consumeNestedType(); $break./
/:$readableName NestedType:/

ForInitopt ::= $empty
/. $putCase consumeEmptyForInitopt(); $break ./
ForInitopt -> ForInit
/:$readableName ForInit:/

ForUpdateopt ::= $empty
/. $putCase consumeEmptyForUpdateopt(); $break ./
ForUpdateopt -> ForUpdate
/:$readableName ForUpdate:/

-- InterfaceHeaderExtendsopt ::= $empty
-- InterfaceHeaderExtendsopt -> InterfaceHeaderExtends
-- /:$readableName InterfaceHeaderExtends:/

Catchesopt ::= $empty
/. $putCase consumeEmptyCatchesopt(); $break ./
Catchesopt -> Catches
/:$readableName Catches:/

-----------------------------------------------
-- 1.5 features : enum type
-----------------------------------------------
-- EnumDeclaration ::= EnumHeader EnumBody
-- /. $putCase consumeEnumDeclaration(); $break ./
-- /:$readableName EnumDeclaration:/

-- EnumHeader ::= EnumHeaderName ClassHeaderImplementsopt
-- /. $putCase consumeEnumHeader(); $break ./
-- /:$readableName EnumHeader:/

-- EnumHeaderName ::= Modifiersopt 'enum' Identifier
-- /. $putCase consumeEnumHeaderName(); $break ./
-- /:$readableName EnumHeaderName:/
-- /:$compliance 1.5:/

-- EnumBody ::= '{' EnumBodyDeclarationsopt '}'
-- /. $putCase consumeEnumBodyNoConstants(); $break ./
-- EnumBody ::= '{' ',' EnumBodyDeclarationsopt '}'
-- /. $putCase consumeEnumBodyNoConstants(); $break ./
-- EnumBody ::= '{' EnumConstants ',' EnumBodyDeclarationsopt '}'
-- /. $putCase consumeEnumBodyWithConstants(); $break ./
-- EnumBody ::= '{' EnumConstants EnumBodyDeclarationsopt '}'
-- /. $putCase consumeEnumBodyWithConstants(); $break ./
-- /:$readableName EnumBody:/

-- EnumConstants -> EnumConstant
-- EnumConstants ::= EnumConstants ',' EnumConstant
-- /.$putCase consumeEnumConstants(); $break ./
-- /:$readableName EnumConstants:/

-- EnumConstantHeaderName ::= Modifiersopt Identifier
-- /.$putCase consumeEnumConstantHeaderName(); $break ./
-- /:$readableName EnumConstantHeaderName:/

-- EnumConstantHeader ::= EnumConstantHeaderName ForceNoDiet Argumentsopt RestoreDiet
-- /.$putCase consumeEnumConstantHeader(); $break ./
-- /:$readableName EnumConstantHeader:/

-- EnumConstant ::= EnumConstantHeader ForceNoDiet ClassBody RestoreDiet
-- /.$putCase consumeEnumConstantWithClassBody(); $break ./
-- EnumConstant ::= EnumConstantHeader
-- /.$putCase consumeEnumConstantNoClassBody(); $break ./
-- /:$readableName EnumConstant:/

Arguments ::= '(' ArgumentListopt ')'
/.$putCase consumeArguments(); $break ./
/:$readableName Arguments:/

Argumentsopt ::= $empty
/.$putCase consumeEmptyArguments(); $break ./
Argumentsopt -> Arguments
/:$readableName Argumentsopt:/

-- EnumDeclarations ::= ';' ClassBodyDeclarationsopt
-- /.$putCase consumeEnumDeclarations(); $break ./
-- /:$readableName EnumDeclarations:/

-- EnumBodyDeclarationsopt ::= $empty
-- /.$putCase consumeEmptyEnumDeclarations(); $break ./
-- EnumBodyDeclarationsopt -> EnumDeclarations
-- /:$readableName EnumBodyDeclarationsopt:/

-----------------------------------------------
-- 1.5 features : enhanced for statement
-----------------------------------------------
-- EnhancedForStatement ::= EnhancedForStatementHeader Statement
-- /.$putCase consumeEnhancedForStatement(); $break ./
-- /:$readableName EnhancedForStatement:/
-- 
-- EnhancedForStatementNoShortIf ::= EnhancedForStatementHeader StatementNoShortIf
-- /.$putCase consumeEnhancedForStatement(); $break ./
-- /:$readableName EnhancedForStatementNoShortIf:/
-- 
-- EnhancedForStatementHeaderInit ::= 'for' '('  Identifier 
-- /.$putCase consumeEnhancedForStatementHeaderInit(false); $break ./
-- /:$readableName EnhancedForStatementHeaderInit:/
-- 
-- EnhancedForStatementHeaderInit ::= 'for' '(' Modifiers Identifier 
-- /.$putCase consumeEnhancedForStatementHeaderInit(true); $break ./
-- /:$readableName EnhancedForStatementHeaderInit:/

-- EnhancedForStatementHeader ::= EnhancedForStatementHeaderInit ':' ExpressionStmt ')'
-- /.$putCase consumeEnhancedForStatementHeader(); $break ./
-- /:$readableName EnhancedForStatementHeader:/
-- /:$compliance 1.5:/

-----------------------------------------------
-- 1.5 features : static imports
-----------------------------------------------
-- SingleStaticImportDeclaration ::= SingleStaticImportDeclarationName ';'
-- /.$putCase consumeImportDeclaration(); $break ./
-- /:$readableName SingleStaticImportDeclaration:/

-- SingleStaticImportDeclarationName ::= 'import' 'static' Name
-- /.$putCase consumeSingleStaticImportDeclarationName(); $break ./
-- /:$readableName SingleStaticImportDeclarationName:/
-- /:$compliance 1.5:/

-- StaticImportOnDemandDeclaration ::= StaticImportOnDemandDeclarationName ';'
-- /.$putCase consumeImportDeclaration(); $break ./
-- /:$readableName StaticImportOnDemandDeclaration:/

-- StaticImportOnDemandDeclarationName ::= 'import' 'static' Name '.' '*'
-- /.$putCase consumeStaticImportOnDemandDeclarationName(); $break ./
-- /:$readableName StaticImportOnDemandDeclarationName:/
-- /:$compliance 1.5:/

-----------------------------------------------
-- 1.5 features : generics
-----------------------------------------------
-- TypeArguments ::= '<' TypeArgumentList1
-- /.$putCase consumeTypeArguments(); $break ./
-- /:$readableName TypeArguments:/
-- /:$compliance 1.5:/

-- OnlyTypeArguments ::= '<' TypeArgumentList1
-- /.$putCase consumeOnlyTypeArguments(); $break ./
-- /:$readableName TypeArguments:/
-- /:$compliance 1.5:/

-- TypeArgumentList1 -> TypeArgument1
-- /:$compliance 1.5:/
-- TypeArgumentList1 ::= TypeArgumentList ',' TypeArgument1
-- /.$putCase consumeTypeArgumentList1(); $break ./
-- /:$readableName TypeArgumentList1:/
-- /:$compliance 1.5:/

-- TypeArgumentList -> TypeArgument
-- /:$compliance 1.5:/
-- TypeArgumentList ::= TypeArgumentList ',' TypeArgument
-- /.$putCase consumeTypeArgumentList(); $break ./
-- /:$readableName TypeArgumentList:/
-- /:$compliance 1.5:/

-- TypeArgument ::= ReferenceType
-- /.$putCase consumeTypeArgument(); $break ./
-- /:$compliance 1.5:/
-- TypeArgument -> Wildcard
-- /:$readableName TypeArgument:/
-- /:$compliance 1.5:/

-- TypeArgument1 -> ReferenceType1
-- /:$compliance 1.5:/
-- TypeArgument1 -> Wildcard1
-- /:$readableName TypeArgument1:/
-- /:$compliance 1.5:/

-- ReferenceType1 ::= ReferenceType '>'
-- /.$putCase consumeReferenceType1(); $break ./
-- /:$compliance 1.5:/
-- ReferenceType1 ::= ClassOrInterface '<' TypeArgumentList2
-- /.$putCase consumeTypeArgumentReferenceType1(); $break ./
-- /:$readableName ReferenceType1:/
-- /:$compliance 1.5:/

-- TypeArgumentList2 -> TypeArgument2
-- /:$compliance 1.5:/
-- TypeArgumentList2 ::= TypeArgumentList ',' TypeArgument2
-- /.$putCase consumeTypeArgumentList2(); $break ./
-- /:$readableName TypeArgumentList2:/
-- /:$compliance 1.5:/

-- TypeArgument2 -> ReferenceType2
-- /:$compliance 1.5:/
-- TypeArgument2 -> Wildcard2
-- /:$readableName TypeArgument2:/
-- /:$compliance 1.5:/
-- 
-- ReferenceType2 ::= ReferenceType '>>'
-- /.$putCase consumeReferenceType2(); $break ./
-- /:$compliance 1.5:/
-- ReferenceType2 ::= ClassOrInterface '<' TypeArgumentList3
-- /.$putCase consumeTypeArgumentReferenceType2(); $break ./
-- /:$readableName ReferenceType2:/
-- /:$compliance 1.5:/

-- TypeArgumentList3 -> TypeArgument3
-- TypeArgumentList3 ::= TypeArgumentList ',' TypeArgument3
-- /.$putCase consumeTypeArgumentList3(); $break ./
-- /:$readableName TypeArgumentList3:/
-- /:$compliance 1.5:/
-- 
-- TypeArgument3 -> ReferenceType3
-- TypeArgument3 -> Wildcard3
-- /:$readableName TypeArgument3:/
-- /:$compliance 1.5:/
-- 
-- ReferenceType3 ::= ReferenceType '>>>'
-- /.$putCase consumeReferenceType3(); $break ./
-- /:$readableName ReferenceType3:/
-- /:$compliance 1.5:/
-- 
-- Wildcard ::= '?'
-- /.$putCase consumeWildcard(); $break ./
-- /:$compliance 1.5:/
-- Wildcard ::= '?' WildcardBounds
-- /.$putCase consumeWildcardWithBounds(); $break ./
-- /:$readableName Wildcard:/
-- /:$compliance 1.5:/
-- 
-- WildcardBounds ::= 'extends' ReferenceType
-- /.$putCase consumeWildcardBoundsExtends(); $break ./
-- /:$compliance 1.5:/
-- WildcardBounds ::= 'super' ReferenceType
-- /.$putCase consumeWildcardBoundsSuper(); $break ./
-- /:$readableName WildcardBounds:/
-- /:$compliance 1.5:/
-- 
-- Wildcard1 ::= '?' '>'
-- /.$putCase consumeWildcard1(); $break ./
-- /:$compliance 1.5:/
-- Wildcard1 ::= '?' WildcardBounds1
-- /.$putCase consumeWildcard1WithBounds(); $break ./
-- /:$readableName Wildcard1:/
-- /:$compliance 1.5:/
-- 
-- WildcardBounds1 ::= 'extends' ReferenceType1
-- /.$putCase consumeWildcardBounds1Extends(); $break ./
-- /:$compliance 1.5:/
-- WildcardBounds1 ::= 'super' ReferenceType1
-- /.$putCase consumeWildcardBounds1Super(); $break ./
-- /:$readableName WildcardBounds1:/
-- /:$compliance 1.5:/

-- Wildcard2 ::= '?' '>>'
-- /.$putCase consumeWildcard2(); $break ./
-- /:$compliance 1.5:/
-- Wildcard2 ::= '?' WildcardBounds2
-- /.$putCase consumeWildcard2WithBounds(); $break ./
-- /:$readableName Wildcard2:/
-- /:$compliance 1.5:/
-- 
-- WildcardBounds2 ::= 'extends' ReferenceType2
-- /.$putCase consumeWildcardBounds2Extends(); $break ./
-- /:$compliance 1.5:/
-- WildcardBounds2 ::= 'super' ReferenceType2
-- /.$putCase consumeWildcardBounds2Super(); $break ./
-- /:$readableName WildcardBounds2:/
-- /:$compliance 1.5:/
-- 
-- Wildcard3 ::= '?' '>>>'
-- /.$putCase consumeWildcard3(); $break ./
-- /:$compliance 1.5:/
-- Wildcard3 ::= '?' WildcardBounds3
-- /.$putCase consumeWildcard3WithBounds(); $break ./
-- /:$readableName Wildcard3:/
-- /:$compliance 1.5:/

-- WildcardBounds3 ::= 'extends' ReferenceType3
-- /.$putCase consumeWildcardBounds3Extends(); $break ./
-- /:$compliance 1.5:/
-- WildcardBounds3 ::= 'super' ReferenceType3
-- /.$putCase consumeWildcardBounds3Super(); $break ./
-- /:$readableName WildcardBound3:/
-- /:$compliance 1.5:/

-- TypeParameterHeader ::= Identifier
-- /.$putCase consumeTypeParameterHeader(); $break ./
-- /:$readableName TypeParameter:/
-- /:$compliance 1.5:/

-- TypeParameters ::= '<' TypeParameterList1
-- /.$putCase consumeTypeParameters(); $break ./
-- /:$readableName TypeParameters:/
-- /:$compliance 1.5:/

-- TypeParameterList -> TypeParameter
-- /:$compliance 1.5:/
-- TypeParameterList ::= TypeParameterList ',' TypeParameter
-- /.$putCase consumeTypeParameterList(); $break ./
-- /:$readableName TypeParameterList:/
-- /:$compliance 1.5:/

-- TypeParameter -> TypeParameterHeader
-- /:$compliance 1.5:/
-- TypeParameter ::= TypeParameterHeader 'extends' ReferenceType
-- /.$putCase consumeTypeParameterWithExtends(); $break ./
-- /:$compliance 1.5:/
-- TypeParameter ::= TypeParameterHeader 'extends' ReferenceType AdditionalBoundList
-- /.$putCase consumeTypeParameterWithExtendsAndBounds(); $break ./
-- /:$readableName TypeParameter:/
-- /:$compliance 1.5:/

-- AdditionalBoundList -> AdditionalBound
-- /:$compliance 1.5:/
-- AdditionalBoundList ::= AdditionalBoundList AdditionalBound
-- /.$putCase consumeAdditionalBoundList(); $break ./
-- /:$readableName AdditionalBoundList:/

-- AdditionalBound ::= '&' ReferenceType
-- /.$putCase consumeAdditionalBound(); $break ./
-- /:$readableName AdditionalBound:/
-- /:$compliance 1.5:/

-- TypeParameterList1 -> TypeParameter1
-- /:$compliance 1.5:/
-- TypeParameterList1 ::= TypeParameterList ',' TypeParameter1
-- /.$putCase consumeTypeParameterList1(); $break ./
-- /:$readableName TypeParameterList1:/
-- /:$compliance 1.5:/

-- TypeParameter1 ::= TypeParameterHeader '>'
-- /.$putCase consumeTypeParameter1(); $break ./
-- /:$compliance 1.5:/
-- TypeParameter1 ::= TypeParameterHeader 'extends' ReferenceType1
-- /.$putCase consumeTypeParameter1WithExtends(); $break ./
-- /:$compliance 1.5:/
-- TypeParameter1 ::= TypeParameterHeader 'extends' ReferenceType AdditionalBoundList1
-- /.$putCase consumeTypeParameter1WithExtendsAndBounds(); $break ./
-- /:$readableName TypeParameter1:/
-- /:$compliance 1.5:/

-- AdditionalBoundList1 -> AdditionalBound1
-- /:$compliance 1.5:/
-- AdditionalBoundList1 ::= AdditionalBoundList AdditionalBound1
-- /.$putCase consumeAdditionalBoundList1(); $break ./
-- /:$readableName AdditionalBoundList1:/
-- /:$compliance 1.5:/

-- AdditionalBound1 ::= '&' ReferenceType1
-- /.$putCase consumeAdditionalBound1(); $break ./
-- /:$readableName AdditionalBound1:/
-- /:$compliance 1.5:/

-------------------------------------------------
-- Duplicate rules to remove ambiguity for (x) --
-------------------------------------------------
-- PostfixExpression_NotName -> Primary
-- PostfixExpression_NotName -> PostIncrementExpression
-- PostfixExpression_NotName -> PostDecrementExpression
-- /:$readableName Expression:/

-- UnaryExpression_NotName -> PreIncrementExpression
-- UnaryExpression_NotName -> PreDecrementExpression
-- UnaryExpression_NotName ::= '+' PushPosition UnaryExpression
-- /.$putCase consumeUnaryExpression(OperatorIds.PLUS); $break ./
-- UnaryExpression_NotName ::= '-' PushPosition UnaryExpression
-- /.$putCase consumeUnaryExpression(OperatorIds.MINUS); $break ./
-- UnaryExpression_NotName -> UnaryExpressionNotPlusMinus_NotName
-- /:$readableName Expression:/

-- UnaryExpressionNotPlusMinus_NotName -> PostfixExpression_NotName
-- UnaryExpressionNotPlusMinus_NotName ::= '~' PushPosition UnaryExpression
-- /.$putCase consumeUnaryExpression(OperatorIds.TWIDDLE); $break ./
-- UnaryExpressionNotPlusMinus_NotName ::= '!' PushPosition UnaryExpression
-- /.$putCase consumeUnaryExpression(OperatorIds.NOT); $break ./
-- -- UnaryExpressionNotPlusMinus_NotName -> CastExpression
-- /:$readableName Expression:/
-- 
-- MultiplicativeExpression_NotName -> UnaryExpression_NotName
-- MultiplicativeExpression_NotName ::= MultiplicativeExpression_NotName '*' UnaryExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.MULTIPLY); $break ./
-- MultiplicativeExpression_NotName ::= Name '*' UnaryExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.MULTIPLY); $break ./
-- MultiplicativeExpression_NotName ::= MultiplicativeExpression_NotName '/' UnaryExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.DIVIDE); $break ./
-- MultiplicativeExpression_NotName ::= Name '/' UnaryExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.DIVIDE); $break ./
-- MultiplicativeExpression_NotName ::= MultiplicativeExpression_NotName '%' UnaryExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.REMAINDER); $break ./
-- MultiplicativeExpression_NotName ::= Name '%' UnaryExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.REMAINDER); $break ./
-- /:$readableName Expression:/
-- 
-- AdditiveExpression_NotName -> MultiplicativeExpression_NotName
-- AdditiveExpression_NotName ::= AdditiveExpression_NotName '+' MultiplicativeExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.PLUS); $break ./
-- AdditiveExpression_NotName ::= Name '+' MultiplicativeExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.PLUS); $break ./
-- AdditiveExpression_NotName ::= AdditiveExpression_NotName '-' MultiplicativeExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.MINUS); $break ./
-- AdditiveExpression_NotName ::= Name '-' MultiplicativeExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.MINUS); $break ./
-- /:$readableName Expression:/
-- 
-- ShiftExpression_NotName -> AdditiveExpression_NotName
-- ShiftExpression_NotName ::= ShiftExpression_NotName '<<'  AdditiveExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.LEFT_SHIFT); $break ./
-- ShiftExpression_NotName ::= Name '<<'  AdditiveExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.LEFT_SHIFT); $break ./
-- ShiftExpression_NotName ::= ShiftExpression_NotName '>>'  AdditiveExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.RIGHT_SHIFT); $break ./
-- ShiftExpression_NotName ::= Name '>>'  AdditiveExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.RIGHT_SHIFT); $break ./
-- ShiftExpression_NotName ::= ShiftExpression_NotName '>>>' AdditiveExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
-- ShiftExpression_NotName ::= Name '>>>' AdditiveExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.UNSIGNED_RIGHT_SHIFT); $break ./
-- /:$readableName Expression:/

-- RelationalExpression_NotName -> ShiftExpression_NotName
-- RelationalExpression_NotName ::= ShiftExpression_NotName '<'  ShiftExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.LESS); $break ./
-- RelationalExpression_NotName ::= Name '<'  ShiftExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.LESS); $break ./
-- RelationalExpression_NotName ::= ShiftExpression_NotName '>'  ShiftExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.GREATER); $break ./
-- RelationalExpression_NotName ::= Name '>'  ShiftExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.GREATER); $break ./
-- RelationalExpression_NotName ::= RelationalExpression_NotName '<=' ShiftExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.LESS_EQUAL); $break ./
-- RelationalExpression_NotName ::= Name '<=' ShiftExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.LESS_EQUAL); $break ./
-- RelationalExpression_NotName ::= RelationalExpression_NotName '>=' ShiftExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.GREATER_EQUAL); $break ./
-- RelationalExpression_NotName ::= Name '>=' ShiftExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.GREATER_EQUAL); $break ./
-- /:$readableName Expression:/

-- InstanceofExpression_NotName -> RelationalExpression_NotName
-- InstanceofExpression_NotName ::= Name 'instanceof' ReferenceType
-- /.$putCase consumeInstanceOfExpressionWithName(); $break ./
-- InstanceofExpression_NotName  ::= InstanceofExpression_NotName 'instanceof' ReferenceType
-- /.$putCase consumeInstanceOfExpression(); $break ./
-- /:$readableName Expression:/
-- 
-- EqualityExpression_NotName -> InstanceofExpression_NotName
-- EqualityExpression_NotName ::= EqualityExpression_NotName '==' InstanceofExpression
-- /.$putCase consumeEqualityExpression(OperatorIds.EQUAL_EQUAL); $break ./
-- EqualityExpression_NotName ::= Name '==' InstanceofExpression
-- /.$putCase consumeEqualityExpressionWithName(OperatorIds.EQUAL_EQUAL); $break ./
-- EqualityExpression_NotName ::= EqualityExpression_NotName '!=' InstanceofExpression
-- /.$putCase consumeEqualityExpression(OperatorIds.NOT_EQUAL); $break ./
-- EqualityExpression_NotName ::= Name '!=' InstanceofExpression
-- /.$putCase consumeEqualityExpressionWithName(OperatorIds.NOT_EQUAL); $break ./
-- /:$readableName Expression:/
-- 
-- AndExpression_NotName -> EqualityExpression_NotName
-- AndExpression_NotName ::= AndExpression_NotName '&' EqualityExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.AND); $break ./
-- AndExpression_NotName ::= Name '&' EqualityExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.AND); $break ./
-- /:$readableName Expression:/
-- 
-- ExclusiveOrExpression_NotName -> AndExpression_NotName
-- ExclusiveOrExpression_NotName ::= ExclusiveOrExpression_NotName '^' AndExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.XOR); $break ./
-- ExclusiveOrExpression_NotName ::= Name '^' AndExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.XOR); $break ./
-- /:$readableName Expression:/
-- 
-- InclusiveOrExpression_NotName -> ExclusiveOrExpression_NotName
-- InclusiveOrExpression_NotName ::= InclusiveOrExpression_NotName '|' ExclusiveOrExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.OR); $break ./
-- InclusiveOrExpression_NotName ::= Name '|' ExclusiveOrExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.OR); $break ./
-- /:$readableName Expression:/
-- 
-- ConditionalAndExpression_NotName -> InclusiveOrExpression_NotName
-- ConditionalAndExpression_NotName ::= ConditionalAndExpression_NotName '&&' InclusiveOrExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.AND_AND); $break ./
-- ConditionalAndExpression_NotName ::= Name '&&' InclusiveOrExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.AND_AND); $break ./
-- /:$readableName Expression:/
-- 
-- ConditionalOrExpression_NotName -> ConditionalAndExpression_NotName
-- ConditionalOrExpression_NotName ::= ConditionalOrExpression_NotName '||' ConditionalAndExpression
-- /.$putCase consumeBinaryExpression(OperatorIds.OR_OR); $break ./
-- ConditionalOrExpression_NotName ::= Name '||' ConditionalAndExpression
-- /.$putCase consumeBinaryExpressionWithName(OperatorIds.OR_OR); $break ./
-- /:$readableName Expression:/

-- ConditionalExpression_NotName -> ConditionalOrExpression_NotName
-- ConditionalExpression_NotName ::= ConditionalOrExpression_NotName '?' Expression ':' ConditionalExpression
-- /.$putCase consumeConditionalExpression(OperatorIds.QUESTIONCOLON) ; $break ./
-- ConditionalExpression_NotName ::= Name '?' Expression ':' ConditionalExpression
-- /.$putCase consumeConditionalExpressionWithName(OperatorIds.QUESTIONCOLON) ; $break ./
-- /:$readableName Expression:/

-- AssignmentExpression_NotName -> ConditionalExpression_NotName
-- AssignmentExpression_NotName -> Assignment
-- /:$readableName Expression:/

-- Expression_NotName -> AssignmentExpression_NotName
-- /:$readableName Expression:/
-----------------------------------------------
-- 1.5 features : end of generics
-----------------------------------------------
-----------------------------------------------
-- 1.5 features : annotation - Metadata feature jsr175
-----------------------------------------------
-- AnnotationTypeDeclarationHeaderName ::= Modifiers '@' PushRealModifiers interface Identifier
-- /.$putCase consumeAnnotationTypeDeclarationHeaderName() ; $break ./
-- /:$compliance 1.5:/
-- AnnotationTypeDeclarationHeaderName ::= '@' PushModifiersForHeader interface Identifier
-- /.$putCase consumeAnnotationTypeDeclarationHeaderName() ; $break ./
-- /:$readableName AnnotationTypeDeclarationHeaderName:/
-- /:$compliance 1.5:/

-- AnnotationTypeDeclarationHeader ::= AnnotationTypeDeclarationHeaderName ClassHeaderExtendsopt ClassHeaderImplementsopt
-- /.$putCase consumeAnnotationTypeDeclarationHeader() ; $break ./
-- /:$readableName AnnotationTypeDeclarationHeader:/
-- /:$compliance 1.5:/

-- AnnotationTypeDeclaration ::= AnnotationTypeDeclarationHeader AnnotationTypeBody
-- /.$putCase consumeAnnotationTypeDeclaration() ; $break ./
-- /:$readableName AnnotationTypeDeclaration:/
-- /:$compliance 1.5:/

-- AnnotationTypeBody ::= '{' AnnotationTypeMemberDeclarationsopt '}'
-- /:$readableName AnnotationTypeBody:/
-- /:$compliance 1.5:/

-- AnnotationTypeMemberDeclarationsopt ::= $empty
-- /.$putCase consumeEmptyAnnotationTypeMemberDeclarationsopt() ; $break ./
-- /:$compliance 1.5:/
-- AnnotationTypeMemberDeclarationsopt ::= NestedType AnnotationTypeMemberDeclarations
-- /.$putCase consumeAnnotationTypeMemberDeclarationsopt() ; $break ./
-- /:$readableName AnnotationTypeMemberDeclarations:/
-- /:$compliance 1.5:/

-- AnnotationTypeMemberDeclarations -> AnnotationTypeMemberDeclaration
-- /:$compliance 1.5:/
-- AnnotationTypeMemberDeclarations ::= AnnotationTypeMemberDeclarations AnnotationTypeMemberDeclaration
-- /.$putCase consumeAnnotationTypeMemberDeclarations() ; $break ./
-- /:$readableName AnnotationTypeMemberDeclarations:/
-- /:$compliance 1.5:/

-- AnnotationMethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
-- /.$putCase consumeMethodHeaderNameWithTypeParameters(true); $break ./
-- AnnotationMethodHeaderName ::= Modifiersopt Type 'Identifier' '('
-- /.$putCase consumeMethodHeaderName(true); $break ./
-- /:$readableName MethodHeaderName:/

-- AnnotationMethodHeaderDefaultValueopt ::= $empty
-- /.$putCase consumeEmptyMethodHeaderDefaultValue() ; $break ./
-- /:$readableName MethodHeaderDefaultValue:/
-- /:$compliance 1.5:/
-- AnnotationMethodHeaderDefaultValueopt ::= DefaultValue
-- /.$putCase consumeMethodHeaderDefaultValue(); $break ./
-- /:$readableName MethodHeaderDefaultValue:/
-- /:$compliance 1.5:/

-- AnnotationMethodHeader ::= AnnotationMethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims AnnotationMethodHeaderDefaultValueopt
-- /.$putCase consumeMethodHeader(); $break ./
-- /:$readableName AnnotationMethodHeader:/
-- /:$compliance 1.5:/

-- AnnotationTypeMemberDeclaration ::= AnnotationMethodHeader ';'
-- /.$putCase consumeAnnotationTypeMemberDeclaration() ; $break ./
-- /:$compliance 1.5:/
-- AnnotationTypeMemberDeclaration -> ConstantDeclaration
-- /:$compliance 1.5:/
-- AnnotationTypeMemberDeclaration -> ConstructorDeclaration
-- /:$compliance 1.5:/
-- AnnotationTypeMemberDeclaration -> TypeDeclaration
-- /:$readableName AnnotationTypeMemberDeclaration:/
-- /:$compliance 1.5:/

-- DefaultValue ::= 'default' MemberValue
-- /:$readableName DefaultValue:/
-- /:$compliance 1.5:/

-- Annotation -> NormalAnnotation
-- /:$compliance 1.5:/
-- Annotation -> MarkerAnnotation
-- /:$compliance 1.5:/
-- Annotation -> SingleMemberAnnotation
-- /:$readableName Annotation:/
-- /:$compliance 1.5:/

-- AnnotationName ::= '@' Name
-- /.$putCase consumeAnnotationName() ; $break ./
-- /:$readableName AnnotationName:/
-- /:$compliance 1.5:/

-- NormalAnnotation ::= AnnotationName '(' MemberValuePairsopt ')'
-- /.$putCase consumeNormalAnnotation() ; $break ./
-- /:$readableName NormalAnnotation:/
-- /:$compliance 1.5:/

-- MemberValuePairsopt ::= $empty
-- /.$putCase consumeEmptyMemberValuePairsopt() ; $break ./
-- /:$compliance 1.5:/
-- MemberValuePairsopt -> MemberValuePairs
-- /:$readableName MemberValuePairsopt:/
-- /:$compliance 1.5:/

-- MemberValuePairs -> MemberValuePair
-- /:$compliance 1.5:/
-- MemberValuePairs ::= MemberValuePairs ',' MemberValuePair
-- /.$putCase consumeMemberValuePairs() ; $break ./
-- /:$readableName MemberValuePairs:/
-- /:$compliance 1.5:/

-- MemberValuePair ::= SimpleName '=' EnterMemberValue MemberValue ExitMemberValue
-- /.$putCase consumeMemberValuePair() ; $break ./
-- /:$readableName MemberValuePair:/
-- /:$compliance 1.5:/

-- EnterMemberValue ::= $empty
-- /.$putCase consumeEnterMemberValue() ; $break ./
-- /:$readableName EnterMemberValue:/
-- /:$compliance 1.5:/

-- ExitMemberValue ::= $empty
-- /.$putCase consumeExitMemberValue() ; $break ./
-- /:$readableName ExitMemberValue:/
-- /:$compliance 1.5:/

-- MemberValue -> ConditionalExpression_NotName
-- /:$compliance 1.5:/
-- MemberValue ::= Name
-- /.$putCase consumeMemberValueAsName() ; $break ./
-- /:$compliance 1.5:/
-- MemberValue -> Annotation
-- /:$compliance 1.5:/
-- MemberValue -> MemberValueArrayInitializer
-- /:$readableName MemberValue:/
-- /:$compliance 1.5:/

-- MemberValueArrayInitializer ::= '{' PushLeftBrace MemberValues ',' '}'
-- /.$putCase consumeMemberValueArrayInitializer() ; $break ./
-- /:$compliance 1.5:/
-- MemberValueArrayInitializer ::= '{' PushLeftBrace MemberValues '}'
-- /.$putCase consumeMemberValueArrayInitializer() ; $break ./
-- /:$compliance 1.5:/
-- MemberValueArrayInitializer ::= '{' PushLeftBrace ',' '}'
-- /.$putCase consumeEmptyMemberValueArrayInitializer() ; $break ./
-- /:$compliance 1.5:/
-- MemberValueArrayInitializer ::= '{' PushLeftBrace '}'
-- /.$putCase consumeEmptyMemberValueArrayInitializer() ; $break ./
-- /:$readableName MemberValueArrayInitializer:/
-- /:$compliance 1.5:/

-- MemberValues -> MemberValue
-- /:$compliance 1.5:/
-- MemberValues ::= MemberValues ',' MemberValue
-- /.$putCase consumeMemberValues() ; $break ./
-- /:$readableName MemberValues:/
-- /:$compliance 1.5:/

-- MarkerAnnotation ::= AnnotationName
-- /.$putCase consumeMarkerAnnotation() ; $break ./
-- /:$readableName MarkerAnnotation:/
-- /:$compliance 1.5:/

-- SingleMemberAnnotation ::= AnnotationName '(' MemberValue ')'
-- /.$putCase consumeSingleMemberAnnotation() ; $break ./
-- /:$readableName SingleMemberAnnotation:/
-- /:$compliance 1.5:/
--------------------------------------
-- 1.5 features : end of annotation --
--------------------------------------

-----------------------------------
-- 1.5 features : recovery rules --
-----------------------------------
-- RecoveryMethodHeaderName ::= Modifiersopt TypeParameters Type 'Identifier' '('
-- /.$putCase consumeRecoveryMethodHeaderNameWithTypeParameters(); $break ./
-- /:$compliance 1.5:/
RecoveryMethodHeaderName ::= Modifiersopt 'function' 'Identifier' '('
/.$putCase consumeRecoveryMethodHeaderName(); $break ./
/:$readableName MethodHeaderName:/

RecoveryMethodHeader ::= RecoveryMethodHeaderName FormalParameterListopt MethodHeaderRightParen 
/.$putCase consumeMethodHeader(); $break ./
-- RecoveryMethodHeader ::= RecoveryMethodHeaderName FormalParameterListopt MethodHeaderRightParen MethodHeaderExtendedDims MethodHeaderThrowsClause
-- /.$putCase consumeMethodHeader(); $break ./
/:$readableName MethodHeader:/
-----------------------------------
-- 1.5 features : recovery rules --
-----------------------------------

/.	}
}./

$names

PLUS_PLUS ::=    '++'   
MINUS_MINUS ::=    '--'   
EQUAL_EQUAL ::=    '=='   
LESS_EQUAL ::=    '<='   
GREATER_EQUAL ::=    '>='   
NOT_EQUAL ::=    '!='   
LEFT_SHIFT ::=    '<<'   
RIGHT_SHIFT ::=    '>>'   
UNSIGNED_RIGHT_SHIFT ::=    '>>>'  
PLUS_EQUAL ::=    '+='   
MINUS_EQUAL ::=    '-='   
MULTIPLY_EQUAL ::=    '*='   
DIVIDE_EQUAL ::=    '/='   
AND_EQUAL ::=    '&='   
OR_EQUAL ::=    '|='   
XOR_EQUAL ::=    '^='   
REMAINDER_EQUAL ::=    '%='   
LEFT_SHIFT_EQUAL ::=    '<<='  
RIGHT_SHIFT_EQUAL ::=    '>>='  
UNSIGNED_RIGHT_SHIFT_EQUAL ::=    '>>>=' 
OR_OR ::=    '||'   
AND_AND ::=    '&&'
PLUS ::=    '+'    
MINUS ::=    '-'    
NOT ::=    '!'    
REMAINDER ::=    '%'    
XOR ::=    '^'    
AND ::=    '&'    
MULTIPLY ::=    '*'    
OR ::=    '|'    
TWIDDLE ::=    '~'    
DIVIDE ::=    '/'    
GREATER ::=    '>'    
LESS ::=    '<'    
LPAREN ::=    '('    
RPAREN ::=    ')'    
LBRACE ::=    '{'    
RBRACE ::=    '}'    
LBRACKET ::=    '['    
RBRACKET ::=    ']'    
SEMICOLON ::=    ';'    
QUESTION ::=    '?'    
COLON ::=    ':'    
COMMA ::=    ','    
DOT ::=    '.'    
EQUAL ::=    '='    
AT ::=    '@'    

$end
-- need a carriage return after the $end
