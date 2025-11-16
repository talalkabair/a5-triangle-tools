/*
 * Token.java – Clean Code Revision (2025, Student B)
 *
 * Purpose:
 *   Defines the Token class used by the Triangle compiler’s lexical analyzer.
 *   Each Token represents a lexical unit with type (Kind), spelling, and position.
 *
 * Improvements:
 *   • Clearer structure and modern commenting style
 *   • Readable enum formatting with logical grouping
 *   • Better documentation for maintainability
 *
 * Original source:
 *   (c) 1999–2003 D.A. Watt & D.F. Brown – University of Glasgow / RGU
 *   Updated (c) 2022–2025 Sandy Brownlee, University of Stirling
 */

package triangle.syntacticAnalyzer;

final class Token {

	/** The category/type of this token (e.g. IDENTIFIER, IF, WHILE). */
	protected Kind kind;

	/** The exact characters forming this token. */
	protected String spelling;

	/** The position (line/column range) of the token in the source file. */
	protected SourcePosition position;

	// ---------------------------------------------------------------------
	// Constructor
	// ---------------------------------------------------------------------

	/**
	 * Constructs a Token with given kind, spelling, and position.
	 * If it’s an IDENTIFIER, checks whether the spelling matches a reserved word.
	 */
	public Token(Kind kind, String spelling, SourcePosition position) {
		this.kind = (kind == Kind.IDENTIFIER)
				? Kind.fromSpelling(spelling)
				: kind;
		this.spelling = spelling;
		this.position = position;
	}

	// ---------------------------------------------------------------------
	// Utilities
	// ---------------------------------------------------------------------

	/** Returns the spelling of a token kind (e.g. "if" or ":="). */
	public static String spell(Kind kind) {
		return kind.spelling;
	}

	/** Returns a readable string representation for debugging. */
	@Override
	public String toString() {
		return "Kind=" + kind + ", spelling=" + spelling + ", position=" + position;
	}

	// ---------------------------------------------------------------------
	// Enum of token kinds
	// ---------------------------------------------------------------------

	public enum Kind {

		// --- Literals and identifiers ---
		INTLITERAL("<int>"),
		CHARLITERAL("<char>"),
		IDENTIFIER("<identifier>"),
		OPERATOR("<operator>"),

		// --- Reserved words (alphabetical for clarity) ---
		ARRAY("array"), BEGIN("begin"), CONST("const"), DO("do"), ELSE("else"),
		END("end"), FUNC("func"), IF("if"), IN("in"), LET("let"), OF("of"),
		PROC("proc"), RECORD("record"), THEN("then"), TYPE("type"),
		VAR("var"), WHILE("while"),

		// --- Punctuation ---
		DOT("."), COLON(":"), SEMICOLON(";"), COMMA(","), BECOMES(":="), IS("~"),

		// --- Brackets ---
		LPAREN("("), RPAREN(")"),
		LBRACKET("["), RBRACKET("]"),
		LCURLY("{"), RCURLY("}"),

		// --- Special tokens ---
		EOT(""), ERROR("<error>");

		/** Literal spelling used in source code. */
		public final String spelling;

		Kind(String spelling) {
			this.spelling = spelling;
		}

		// -----------------------------------------------------------------
		// Reserved-word lookup
		// -----------------------------------------------------------------

		/**
		 * Checks whether a given spelling corresponds to a reserved word.
		 * If it does, returns that Kind; otherwise returns IDENTIFIER.
		 */
		public static Kind fromSpelling(String spelling) {
			boolean insideReserved = false;

			for (Kind kind : Kind.values()) {
				if (kind == firstReservedWord)
					insideReserved = true;

				if (insideReserved && kind.spelling.equals(spelling))
					return kind;

				if (kind == lastReservedWord)
					break;
			}
			return IDENTIFIER;
		}

		private static final Kind firstReservedWord = ARRAY;
		private static final Kind lastReservedWord  = WHILE;
	}
}
