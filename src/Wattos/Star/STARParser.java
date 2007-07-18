/*
 * STARParser.java
 *
 * Created on June 2, 2003, 2:10 PM
 */

package Wattos.Star;
 
import Wattos.Utils.*; 
 
/**
 * STAR parser.
 * This parser accepts tokens from <CODE>STARLexer</CODE> and calls appropriate
 * methods of <CODE>ContentHandler</CODE> and <CODE>ErrorHandler</CODE> objects.
 * <P>
 * Parse errors:
 * <UL>
 *   <LI>Lexer error (should never happen).
 *   <LI>Global block(s) in input.
 *   <LI>Anything other than comments or data block at top-level.
 *   <LI>Anything other than comments or saveframes in data block.
 *   <LI>Anything other than comments, loops, <CODE>save_</CODE>, or free tags 
 *       (tag/value pairs) in saveframe.
 *   <LI>Anything other than comments, <CODE>stop_</CODE>, tags, or values in loop
 *   <LI>Loops with no values.
 *   <LI>Loops with no tags.
 *   <LI>Premature end of file: EOF is legal only inside a data block, EOF inside
 *       a saveframe or loop is an error.
 * </UL> 
 * Error reporting is not very detailed at the moment: parser simply reports
 * <CODE>Invalid token: <EM>token</EM></CODE>. <BR>
 * Parser assumes that first data block continues to the end of input. If there
 * is more than one data block in input, parser will see the second <CODE>data_</CODE>
 * as being inside the current data block and it will report that as invalid token 
 * (only comments and saveframes are allowed in data block).
 * <P>
 * Parse warnings:
 * <UL>
 *   <LI>NMR-STAR keyword inside quoted value. This warning is generated by the
 *       lexer, it's purpose is to catch semicolon-delimited values where closing
 *       semicolon is missing.
 *   <LI>Loop count error. This warning is generated when number of values in the
 *       loop is not an exact multiple of the number of tags. Parser makes "best
 *       effort" to report the line number where the value is missing: in a 
 *       well-formatted loop it will report [first] line number where a value is
 *       missing, otherwise it'll report the line that contains <CODE>stop_</CODE>
 *       or anything in between. Note also that if there is as many values missing
 *       as there are columns in the loop, parser will not see that as error.
 * </UL>
 * @see ContentHandler
 * @see ErrorHandler
 * @see STARLexer
 * @author  dmaziuk
 * @version 1
 */
public class STARParser {
    /** scanner error */
    public static final String ERR_LEXER = "Lexer error: ";
    /** global blocks are not allowed */
    public static final String ERR_GLOBAL = "Global blocks are illegal in NMR-STAR";
    /** invalid token */
    public static final String ERR_TOKEN = "Invalid token: ";
    /** premature EOF */
    public static final String ERR_EOF = "Premature end of file";
    /** loop with no values */
    public static final String ERR_EMPTYLOOP = "Loop with no values";
    /** loop with no tags */
    public static final String ERR_LOOPNOTAGS = "Loop with no tags";
    /** parse warning */
    public static final String WARN_KEYWORD = "Keyword in value: ";
    /** loop count error */
    public static final String WARN_LOOPCOUNT = "Loop count error";
    /* content handler object */
    private ContentHandler fCh = null;
    /* error handler object */
    private ErrorHandler fEh = null;
    /* scanner */
    STARLexer fLex = null;

    /** Creates new STARParser.
     * @param lex scanner
     */
    public STARParser( STARLexer lex ) {
        fLex = lex;
    } 
    /** Creates new STARParser.
     * @param lex scanner
     * @param ch content handler object
     * @param eh error handler object
     */
    public STARParser( STARLexer lex, ContentHandler ch, ErrorHandler eh ) {
        fLex = lex;
        fCh = ch;
        fEh = eh;
    } 
    /** Returns content handler.
     * @return content handler object
     */
    public ContentHandler getContentHandler() {
        return fCh;
    } 
    /** Sets content handler.
     * @param ch content handler object
     */
    public void setContentHandler( ContentHandler ch ) {
        fCh = ch;
    } 
    /** Returns error handler.
     * @return error handler object
     */
    public ErrorHandler getErrorHandler() {
        return fEh;
    } 
    /** Sets error handler.
     * @param eh error handler object
     */
    public void setErrorHandler( ErrorHandler eh ) {
        fEh = eh;
    } 
    /** Returns scanner object.
     * @return scanner
     */
    public STARLexer getScanner() {
        return fLex;
    } 
    /** Sets scanner object.
     * @param lex scanner
     */
    public void setScanner( STARLexer lex ) {
        fLex = lex;
    } 

    /** Parses input file */
    public boolean parse() {
        String dataid = null;
        int tok;
//        String tag = null;
//        int tagline = -1;
//        DataItemNode item = null;
        String sfname = null;
        
        try {
            do {
                tok = fLex.yylex();
                switch( tok ) {
                    case STARLexer.ERROR :
                        fEh.error( fLex.getLine(), fLex.getColumn(), ERR_LEXER
                        + fLex.getText() );
                        return false;
                    case STARLexer.COMMENT :
                        if( fCh.comment( fLex.getLine(), fLex.getText() ) ) return true;
                        break;
                    case STARLexer.DATASTART : // data block
//                        dataid = fLex.getText().substring( 5 ); // strip data_
                        dataid = fLex.getText(); // strip data_
                        if( fCh.startData( fLex.getLine(), dataid ) ) return true;
                        break;
                    case STARLexer.SAVESTART : // saveframe begin
//                        sfname = fLex.getText().substring( 5 ); // strip save_
                        sfname = fLex.getText(); // strip save_
                        if( fCh.startSaveFrame( fLex.getLine(), sfname ) ) 
                            return true;
                        break;
                    case STARLexer.SAVEEND : // end of saveframe
                        if( fCh.endSaveFrame( fLex.getLine(), sfname ) )
                            return true;
                        break;
                    case STARLexer.LOOPSTART : // start of loop
                        if( fCh.startLoop( fLex.getLine() ) ) return true;
                        break;
                    case STARLexer.STOP : // end of loop
                        if( fCh.endLoop( fLex.getLine() ) ) return true;
                        break;
                    case STARLexer.TAGNAME: // tag label
                        if ( fCh.tagName( fLex.getLine(), fLex.getText() ) ) {
                            return true;
                        }
                        break;
                    case STARLexer.DVNSINGLE:
                    case STARLexer.DVNDOUBLE:
                    case STARLexer.DVNSEMICOLON:
                        String value = fLex.getText();
//                        General.showDebug("Parsed value: " + value);
//                        General.showDebug("tok         : " + tok);
                        if( ( tok == STARLexer.DVNSEMICOLON ) && 
                            ( value.substring( 0, 1 ).equals( "\n" ) )) {
                            value = value.substring( 1 );
                        }
                        if( fCh.data( value ) ) {
                            return true;
                        }
                        break;                        
                    case STARLexer.DVNFRAMECODE:                        
                        if ( fCh.data( "$" + fLex.getText() ) ) {
                            return true;
                        }
                        break;
                    case STARLexer.DVNNON :
//                        General.showDebug("Parsed value: " + fLex.getText());
//                        General.showDebug("tok         : " + tok);
                        if ( fCh.data( fLex.getText() ) ) {
                            return true;
                        }
                        break; 
                    case STARLexer.EOF : // fake end of data block
                        fCh.endData( fLex.getLine(), dataid );
                        return true;
                    default : // invalid token
                        fEh.error( fLex.getLine(), fLex.getColumn(), ERR_TOKEN
                        + fLex.getText() );
                        return false;
                }
            } while( tok != STARLexer.EOF );
            return true;
        } catch( Exception e ) { 
            General.showThrowable(e); 
            return false;
        }
    } 


    public void test_parse() {
        try {
            int tok = fLex.yylex();
	    while( tok != STARLexer.EOF ) {
                    General.showOutputNoEol(STARLexer.TOKEN_TYPES[tok] + "(" 
                    + fLex.getLine() + ":" + fLex.getColumn() + "): " );
	        switch( tok ) {
                        case STARLexer.DVNSINGLE :
                        case STARLexer.DVNDOUBLE :
                        case STARLexer.DVNSEMICOLON :
                        General.showOutput( fLex.getText() );
                            break;
                        default :
                            General.showOutput( fLex.getText() );
	        }
	        tok = fLex.yylex();
            }
            General.showOutput("End of data_ (EOF)" );
        }
        catch( Exception e ) { General.showThrowable(e); }
    } 

    
    /** 
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        try {
            java.io.InputStream in;
            if( args.length < 1 ) in = System.in;
            else in = new java.io.FileInputStream( args[0] );
            java.io.BufferedReader reader = new java.io.BufferedReader( 
            new java.io.InputStreamReader( in ) );
            STARLexer lex = new STARLexer( reader );
            STARParser p  = new STARParser( lex );
            if ( ! p.parse()) {
                General.doErrorExit("Parsing error");
            }
        }
        catch( Exception e ) { General.showThrowable(e); }
    } 
}
