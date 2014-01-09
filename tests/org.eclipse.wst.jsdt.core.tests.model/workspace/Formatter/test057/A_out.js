package test0002;
public class Parser
		implements
			BindingIds,
			ParserBasicInformation,
			TerminalTokens,
			CompilerModifiers,
			OperatorIds,
			TypeIds {
	protected ProblemReporter			problemReporter;
	protected CompilerOptions			options;
	public int							firstToken;															// handle for multiple parsing goals
	public int							lastAct;																//handle for multiple parsing goals
	protected ReferenceContext			referenceContext;
	public int							currentToken;
	private int							synchronizedBlockSourceStart;
	//error recovery management
	protected int						lastCheckPoint;
	protected RecoveredElement			currentElement;
	public static boolean				VERBOSE_RECOVERY			= false;
	protected boolean					restartRecovery;
	protected int						listLength;															// for recovering some incomplete list (interfaces, throws or parameters)
	protected boolean					hasError;
	protected boolean					hasReportedError;
	public static boolean				fineErrorDiagnose			= true;									//TODO remove the static modifier when new diagnose is ready
	public boolean						reportSyntaxErrorIsRequired	= true;
	public boolean						reportOnlyOneSyntaxError	= false;
	protected int						recoveredStaticInitializerStart;
	protected int						lastIgnoredToken, nextIgnoredToken;									// comment
	protected int						lastErrorEndPosition;
	protected boolean					ignoreNextOpeningBrace;
	//internal data for the automat 
	protected final static int			StackIncrement				= 255;
	protected int						stateStackTop;
	protected int[]						stack						= new int[StackIncrement];
	//scanner token 
	public Scanner						scanner;
	//ast stack
	final static int					AstStackIncrement			= 100;
	protected int						astPtr;
	protected AstNode[]					astStack					= new AstNode[AstStackIncrement];
	protected int						astLengthPtr;
	protected int[]						astLengthStack;
	public CompilationUnitDeclaration	compilationUnit;														/*the result from parse()*/
	AstNode[]							noAstNodes					= new AstNode[AstStackIncrement];
	//expression stack
	final static int					ExpressionStackIncrement	= 100;
	protected int						expressionPtr;
	protected Expression[]				expressionStack				= new Expression[ExpressionStackIncrement];
	protected int						expressionLengthPtr;
	protected int[]						expressionLengthStack;
	Expression[]						noExpressions				= new Expression[ExpressionStackIncrement];
	//identifiers stacks 
	protected int						identifierPtr;
	protected char[][]					identifierStack;
	protected int						identifierLengthPtr;
	protected int[]						identifierLengthStack;
	protected long[]					identifierPositionStack;
	//positions , dimensions , .... (what ever is int) ..... stack
	protected int						intPtr;
	protected int[]						intStack;
	protected int						endPosition;															//accurate only when used ! (the start position is pushed into intStack while the end the current one)
	protected int						endStatementPosition;
	protected int						lParenPos, rParenPos;													//accurate only when used !
	//modifiers dimensions nestedType etc.......
	protected boolean					optimizeStringLiterals		= true;
	protected int						modifiers;
	protected int						modifiersSourceStart;
	protected int						nestedType, dimensions;
	protected int[]						nestedMethod;															//the ptr is nestedType
	protected int[]						realBlockStack;
	protected int						realBlockPtr;
	protected boolean					diet						= false;									//tells the scanner to jump over some parts of the code/expressions like method bodies
	protected int						dietInt						= 0;										// if > 0 force the none-diet-parsing mode (even if diet if requested) [field parsing with anonymous inner classes...]
	protected int[]						variablesCounter;
	//===DATA===DATA===DATA===DATA===DATA===DATA===//
	public final static byte			rhs[]						= {0, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2,
			2, 1, 1, 1, 1, 3, 4, 0, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1,
			2, 1, 2, 2, 2, 1, 1, 2, 2, 2, 4, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 2, 3, 3, 2, 2, 1, 3, 1, 3, 1, 2, 1, 1, 1, 3, 0, 3,
			1, 1, 1, 1, 1, 1, 1, 4, 1, 3, 3, 7, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2,
			2, 4, 4, 5, 4, 4, 2, 1, 2, 3, 3, 1, 3, 3, 1, 3, 1, 4, 0, 2, 1, 2,
			2, 4, 1, 1, 2, 5, 5, 7, 7, 7, 7, 2, 2, 3, 2, 2, 3, 1, 2, 1, 2, 1,
			1, 2, 2, 1, 1, 1, 1, 1, 3, 3, 4, 1, 3, 4, 0, 1, 2, 1, 1, 1, 1, 2,
			3, 4, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 3, 3, 2, 1, 1, 1, 1, 1, 1, 1, 5, 7, 7, 6, 2, 3, 3,
			4, 1, 2, 2, 1, 2, 3, 2, 5, 5, 7, 9, 9, 1, 1, 1, 1, 3, 3, 5, 2, 3,
			2, 3, 3, 3, 5, 1, 3, 4, 1, 2, 5, 2, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,
			3, 3, 3, 3, 3, 1, 1, 5, 6, 8, 7, 2, 0, 2, 0, 1, 3, 3, 3, 3, 4, 3,
			4, 1, 2, 3, 2, 1, 1, 2, 2, 3, 3, 4, 6, 6, 4, 4, 4, 1, 1, 1, 1, 2,
			2, 0, 1, 1, 3, 3, 1, 3, 3, 1, 3, 3, 1, 6, 6, 5, 0, 0, 1, 3, 3, 3,
			1, 3, 3, 1, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 1, 3, 1, 3,
			1, 3, 1, 3, 1, 5, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 0, 1, 0, 1, 0,
			1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 0, 0, 1, 0, 1, 0, 1, 0, 1};
	public static char					asb[]						= null;
	public static char					asr[]						= null;
	public static char					nasb[]						= null;
	public static char					nasr[]						= null;
	public static char					terminal_index[]			= null;
	public static char					non_terminal_index[]		= null;
	public static char					term_action[]				= null;
	public final static byte			term_check[]				= {0, 0, 0,
			0, 0, 3, 0, 3, 7, 8, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 22,
			0, 24, 25, 25, 4, 5, 6, 25, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 54, 55, 0, 57,
			58, 59, 60, 61, 62, 63, 64, 65, 66, 0, 0, 0, 70, 71, 72, 73, 74,
			75, 76, 77, 78, 79, 80, 0, 0, 0, 84, 85, 18, 19, 7, 8, 0, 1, 2, 3,
			4, 5, 6, 7, 8, 9, 10, 11, 12, 22, 21, 24, 25, 0, 26, 27, 28, 55,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
			48, 49, 50, 51, 52, 81, 54, 55, 0, 57, 58, 59, 60, 61, 62, 63, 64,
			65, 66, 81, 12, 83, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 0,
			56, 0, 84, 85, 0, 4, 7, 8, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 22, 0, 24, 25, 24, 4, 5, 6, 55, 31, 32, 33, 34, 35, 36, 37, 38,
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 54, 55,
			56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 0, 0, 0, 70, 71, 72,
			73, 74, 75, 76, 77, 78, 79, 80, 0, 13, 14, 15, 16, 17, 82, 7, 8, 0,
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 22, 0, 24, 25, 0, 4, 5, 6,
			0, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
			47, 48, 49, 50, 51, 52, 81, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
			64, 65, 66, 41, 82, 0, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
			0, 13, 14, 15, 16, 17, 57, 7, 8, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			11, 12, 22, 0, 24, 25, 0, 4, 5, 6, 0, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 27, 54,
			55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 0, 0, 0, 70, 71,
			72, 73, 74, 75, 76, 77, 78, 79, 80, 0, 13, 14, 15, 16, 17, 0, 7, 8,
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 22, 0, 24, 25, 3, 0, 22,
			0, 24, 31, 32, 33, 34, 35, 36, 37, 38, 39, 91, 41, 42, 43, 44, 45,
			46, 47, 48, 49, 50, 51, 52, 0, 54, 55, 3, 57, 58, 59, 60, 61, 62,
			63, 64, 65, 66, 40, 82, 0, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
			80, 81, 0, 83, 0, 0, 0, 18, 19, 7, 8, 0, 1, 2, 0, 4, 5, 6, 7, 8, 9,
			10, 11, 0, 22, 0, 24, 25, 22, 0, 24, 84, 85, 31, 32, 33, 34, 35,
			36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
			0, 54, 55, 3, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 40, 0, 29,
			70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 0, 83, 0, 1, 2, 0,
			86, 7, 8, 0, 1, 2, 91, 4, 5, 6, 7, 8, 9, 10, 11, 0, 22, 82, 24, 25,
			87, 22, 89, 84, 85, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 54, 55, 70, 57, 58, 59,
			60, 61, 62, 63, 64, 65, 66, 0, 1, 2, 70, 71, 72, 73, 74, 75, 76,
			77, 78, 79, 80, 81, 0, 83, 0, 1, 2, 0, 0, 7, 8, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 0, 0, 0, 12, 22, 3, 24, 25, 55, 9, 10, 11, 0, 31, 32, 33,
			34, 35, 36, 37, 38, 39, 21, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
			51, 52, 0, 54, 55, 0, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 0, 1,
			2, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 0, 26, 0, 28, 55, 0,
			87, 7, 8, 0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 12, 22, 0, 24, 25,
			24, 9, 10, 11, 25, 31, 32, 33, 34, 35, 36, 37, 38, 39, 20, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 30, 54, 55, 56, 57, 58, 59,
			60, 61, 62, 63, 64, 65, 66, 0, 0, 0, 70, 71, 72, 73, 74, 75, 76,
			77, 78, 79, 80, 0, 0, 0, 0, 3, 0, 3, 7, 8, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 0, 0, 0, 12, 22, 0, 24, 25, 25, 9, 10, 11, 25, 31, 32, 33, 34,
			35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
			52, 0, 54, 69, 0, 1, 2, 0, 4, 5, 6, 7, 8, 25, 7, 8, 0, 0, 70, 71,
			72, 73, 74, 75, 76, 77, 78, 79, 80, 22, 0, 24, 25, 0, 0, 0, 0, 0,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 12, 41, 42, 43, 44, 45, 46, 47,
			48, 49, 50, 51, 52, 23, 54, 0, 30, 0, 3, 4, 5, 6, 7, 8, 7, 8, 40,
			12, 104, 40, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 24, 25,
			55, 0, 56, 0, 3, 31, 32, 33, 34, 35, 36, 37, 38, 39, 103, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 24, 54, 0, 86, 57, 58, 59,
			60, 61, 62, 63, 64, 65, 66, 0, 1, 2, 41, 4, 5, 6, 7, 8, 9, 10, 11,
			0, 13, 14, 15, 16, 17, 18, 19, 20, 21, 0, 23, 0, 3, 26, 27, 28, 7,
			8, 7, 8, 0, 12, 0, 0, 1, 2, 0, 0, 0, 54, 7, 8, 57, 58, 59, 60, 61,
			62, 63, 64, 65, 66, 22, 0, 22, 0, 3, 24, 25, 29, 30, 29, 30, 30,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46, 47,
			48, 49, 50, 51, 52, 0, 90, 55, 92, 93, 94, 95, 96, 97, 98, 99, 100,
			101, 102, 67, 68, 0, 1, 2, 69, 0, 0, 22, 7, 8, 90, 0, 92, 93, 94,
			95, 96, 97, 98, 99, 100, 101, 102, 0, 87, 24, 25, 4, 5, 6, 7, 8,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
			48, 49, 50, 51, 52, 0, 0, 55, 0, 1, 2, 55, 55, 0, 0, 7, 8, 55, 12,
			67, 68, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 25, 24, 25, 12, 30, 22, 24,
			25, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46,
			47, 48, 49, 50, 51, 52, 0, 0, 55, 0, 1, 2, 53, 7, 8, 0, 7, 8, 25,
			12, 67, 68, 56, 0, 1, 2, 3, 4, 5, 6, 7, 8, 25, 24, 25, 12, 0, 22,
			0, 0, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45,
			46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 29, 0, 0, 0, 7, 8, 4, 5, 6, 7,
			8, 67, 68, 69, 0, 1, 2, 0, 4, 5, 6, 24, 25, 9, 10, 11, 25, 24, 31,
			32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48,
			49, 50, 51, 52, 0, 1, 2, 0, 0, 0, 0, 7, 8, 3, 0, 24, 7, 8, 67, 68,
			69, 0, 1, 2, 0, 4, 5, 6, 24, 25, 22, 24, 41, 42, 43, 31, 32, 33,
			34, 35, 36, 37, 38, 39, 30, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
			51, 52, 0, 0, 55, 89, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 67, 68, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 30, 0, 26, 27,
			28, 29, 30, 0, 0, 1, 2, 0, 4, 5, 6, 40, 40, 9, 10, 11, 0, 13, 14,
			15, 16, 17, 18, 19, 20, 53, 29, 56, 56, 22, 0, 0, 0, 0, 1, 2, 3, 4,
			5, 6, 24, 69, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 25, 0, 26, 27, 28, 29, 30, 0, 0, 1, 2, 0, 4, 5, 6, 69, 40,
			9, 10, 11, 0, 13, 14, 15, 16, 17, 18, 19, 0, 53, 29, 3, 56, 22, 0,
			7, 8, 0, 1, 2, 3, 4, 5, 6, 24, 69, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 88, 55, 26, 27, 28, 29, 30, 0, 1, 2, 3,
			4, 5, 6, 7, 8, 40, 0, 0, 0, 1, 2, 0, 4, 5, 6, 7, 8, 0, 53, 12, 3,
			56, 0, 0, 7, 8, 0, 1, 2, 3, 4, 5, 6, 22, 69, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27, 28, 29, 30, 0, 0,
			9, 10, 11, 40, 40, 0, 54, 40, 56, 57, 58, 59, 60, 61, 62, 63, 64,
			65, 66, 0, 53, 22, 3, 56, 0, 0, 7, 8, 0, 1, 2, 3, 4, 5, 6, 30, 69,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0,
			26, 27, 28, 29, 30, 0, 0, 69, 3, 4, 5, 6, 7, 8, 40, 0, 1, 2, 12, 0,
			22, 0, 7, 8, 25, 55, 0, 53, 30, 0, 56, 25, 3, 4, 5, 6, 7, 8, 12,
			24, 25, 12, 0, 69, 25, 24, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 29, 0, 0,
			0, 7, 8, 0, 0, 24, 3, 3, 67, 68, 7, 8, 0, 0, 0, 12, 12, 0, 24, 25,
			22, 9, 10, 11, 24, 31, 32, 33, 34, 35, 36, 37, 38, 39, 20, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 0, 0, 0, 3, 7, 8,
			0, 0, 0, 3, 3, 67, 68, 7, 8, 7, 8, 0, 12, 12, 53, 24, 25, 22, 22,
			25, 0, 0, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44,
			45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 90, 0, 0, 0, 7, 8, 0, 0,
			0, 3, 3, 67, 68, 7, 8, 7, 8, 0, 12, 12, 0, 24, 25, 22, 7, 8, 24,
			24, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46,
			47, 48, 49, 50, 51, 52, 0, 1, 2, 69, 0, 0, 0, 7, 8, 0, 0, 0, 3, 3,
			67, 68, 7, 8, 7, 8, 0, 12, 12, 53, 24, 25, 22, 7, 8, 24, 24, 31,
			32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48,
			49, 50, 51, 52, 0, 1, 2, 0, 0, 0, 0, 7, 8, 0, 0, 0, 3, 3, 67, 68,
			7, 8, 12, 0, 0, 12, 12, 12, 24, 25, 23, 40, 0, 25, 25, 31, 32, 33,
			34, 35, 36, 37, 38, 39, 12, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
			51, 52, 0, 1, 2, 0, 0, 0, 40, 7, 8, 0, 0, 0, 3, 3, 67, 68, 7, 8, 0,
			0, 53, 12, 12, 12, 24, 25, 0, 0, 25, 25, 25, 31, 32, 33, 34, 35,
			36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
			0, 1, 2, 0, 0, 0, 40, 7, 8, 0, 0, 24, 3, 3, 67, 68, 7, 8, 0, 0, 55,
			12, 12, 0, 24, 25, 22, 0, 25, 24, 0, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2,
			29, 0, 0, 29, 7, 8, 0, 0, 0, 3, 3, 67, 68, 7, 8, 0, 0, 0, 12, 12,
			3, 24, 25, 0, 40, 0, 25, 25, 31, 32, 33, 34, 35, 36, 37, 38, 39,
			29, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 0, 0,
			40, 0, 7, 8, 0, 0, 0, 3, 3, 67, 68, 7, 8, 0, 0, 0, 12, 12, 3, 24,
			25, 0, 0, 25, 25, 24, 31, 32, 33, 34, 35, 36, 37, 38, 39, 29, 41,
			42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 29, 0, 40, 0,
			7, 8, 0, 0, 0, 3, 3, 67, 68, 7, 8, 0, 0, 0, 12, 12, 3, 24, 25, 0,
			0, 0, 25, 24, 31, 32, 33, 34, 35, 36, 37, 38, 39, 29, 41, 42, 43,
			44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2, 30, 29, 0, 0, 7, 8, 0,
			0, 0, 3, 3, 67, 68, 7, 8, 12, 0, 0, 12, 12, 12, 24, 25, 0, 0, 0, 3,
			25, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 41, 42, 43, 44, 45, 46,
			47, 48, 49, 50, 51, 52, 0, 1, 2, 29, 0, 30, 0, 7, 8, 0, 0, 0, 3, 0,
			67, 68, 7, 8, 0, 0, 0, 12, 12, 3, 24, 25, 22, 0, 22, 0, 0, 31, 32,
			33, 34, 35, 36, 37, 38, 39, 22, 41, 42, 43, 44, 45, 46, 47, 48, 49,
			50, 51, 52, 0, 1, 2, 89, 30, 29, 29, 7, 8, 0, 0, 0, 3, 3, 67, 68,
			7, 8, 0, 0, 0, 12, 88, 0, 24, 25, 0, 0, 0, 0, 0, 31, 32, 33, 34,
			35, 36, 37, 38, 39, 29, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
			52, 0, 1, 2, 29, 29, 29, 0, 7, 8, 0, 0, 0, 3, 0, 67, 68, 7, 8, 0,
			0, 0, 12, 0, 0, 24, 25, 0, 0, 22, 0, 0, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 0, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 1, 2,
			0, 0, 29, 29, 7, 8, 0, 0, 0, 25, 0, 67, 68, 0, 0, 0, 0, 0, 0, 0, 0,
			24, 25, 0, 0, 25, 25, 0, 31, 32, 33, 34, 35, 36, 37, 38, 39, 29,
			41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 30, 88, 29, 29, 0,
			0, 29, 0, 1, 2, 3, 4, 5, 6, 67, 68, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27, 28, 29, 30, 0, 1, 2, 3,
			4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			53, 23, 30, 56, 26, 27, 28, 0, 0, 1, 2, 3, 4, 5, 6, 0, 69, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27,
			28, 29, 30, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 21, 53, 23, 0, 56, 26, 27, 28, 0, 0, 1, 2, 3,
			4, 5, 6, 0, 69, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 0, 0, 26, 27, 28, 29, 30, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 53, 23, 0, 56, 26, 27,
			28, 0, 0, 1, 2, 3, 4, 5, 6, 0, 69, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27, 28, 29, 30, 0, 1, 2, 3,
			4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			53, 23, 0, 56, 26, 27, 28, 0, 0, 1, 2, 3, 4, 5, 6, 0, 69, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27,
			28, 29, 30, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 21, 53, 23, 0, 56, 0, 1, 2, 3, 4, 5, 6, 0, 0,
			9, 10, 11, 69, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0,
			26, 27, 28, 29, 30, 24, 0, 1, 2, 0, 4, 5, 6, 0, 40, 9, 10, 11, 0,
			13, 14, 15, 16, 17, 0, 0, 0, 53, 29, 0, 56, 0, 1, 2, 3, 4, 5, 6,
			29, 0, 9, 10, 11, 69, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			29, 0, 26, 27, 28, 29, 30, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 53, 23, 0, 56, 0, 1, 2, 0,
			4, 5, 6, 0, 0, 9, 10, 11, 69, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 0, 22, 26, 27, 28, 29, 30, 0, 24, 0, 3, 4, 5, 6, 7, 8, 40,
			0, 0, 12, 0, 0, 22, 0, 0, 0, 42, 43, 0, 53, 0, 22, 56, 0, 1, 2, 0,
			4, 5, 6, 22, 24, 9, 10, 11, 69, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 29, 40, 26, 27, 28, 29, 30, 0, 81, 0, 0, 0, 0, 0, 0, 54,
			40, 0, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 0, 53, 0, 22, 56, 0,
			1, 2, 22, 4, 5, 6, 29, 24, 9, 10, 11, 69, 13, 14, 15, 16, 17, 18,
			19, 20, 21, 22, 23, 0, 0, 26, 27, 28, 29, 30, 0, 1, 2, 3, 4, 5, 6,
			7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 53, 23, 0,
			56, 0, 1, 2, 0, 4, 5, 6, 0, 0, 9, 10, 11, 69, 13, 14, 15, 16, 17,
			18, 19, 20, 21, 22, 23, 0, 22, 26, 27, 28, 29, 30, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 53,
			23, 0, 56, 26, 27, 28, 0, 0, 1, 2, 3, 4, 5, 6, 0, 69, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27, 28,
			22, 30, 0, 0, 1, 2, 3, 4, 5, 6, 0, 40, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27, 28, 0, 30, 0, 0, 1,
			2, 3, 4, 5, 6, 0, 40, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
			20, 21, 22, 23, 0, 0, 26, 27, 28, 0, 30, 0, 0, 1, 2, 3, 4, 5, 6, 0,
			40, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0,
			0, 26, 27, 28, 0, 30, 0, 0, 1, 2, 3, 4, 5, 6, 0, 40, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 26, 27, 28, 0,
			30, 0, 3, 4, 5, 6, 7, 8, 0, 0, 40, 12, 3, 4, 5, 6, 7, 8, 24, 25, 0,};
}