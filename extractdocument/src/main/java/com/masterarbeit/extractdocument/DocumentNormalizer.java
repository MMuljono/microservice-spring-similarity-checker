package com.masterarbeit.extractdocument;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Token;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The tokenizer class
 * Main purpose: Convert stream of inputs into tokens
 * */
public class DocumentNormalizer {
    private static final Hashtable<Integer,String> myHashTableJS = new Hashtable<>() {{
        put(0,       "NULL");
        put(6,       "WORD");
        put(7,       "RESW2");
        put(8,       "FUNC");
        put(10,      "INT");
        put(11,      "FLOAT");
        put(12,      "HEX");
        put(13,      "STR");
        put(14,      "STR");
        put(15,      "BACKQUOTE");
        put(16,      "DATYPE");
        put(17,      "VAR");
        put(18,      "REGEX");
        put(19,      "ANNOT");
        put(20,      "ID");
        put(22,      "SEP");
        put(23,      "OPRT");
    }};

    /**
     * Function to replace the identified number into a token string
     * */
    private static void getValueHash (Integer type, List<String> array) {
        String existInTable = myHashTableJS.get(type);
        if (existInTable != null) {
            array.add(myHashTableJS.get(type));
        }
    }

    /**
     * The core of tokenizer process
     * The function will stream the inputs and will identify it with a unique id number
     */
    public static List<String> setText(String resource, String filetype) {
        RSyntaxTextArea textArea = new RSyntaxTextArea(resource);
        /** Tokenizer will process according to the file's type */
        if (filetype.equals("js")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        } else if (filetype.equals("java")) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        }
        List<String> tokenInArray = new ArrayList<>();
        try {
            textArea.setCaretPosition(0);
            textArea.discardAllEdits();
            RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
            /** Stream of token will be recognized and some specific token will be skipped
             * For example tokens for whitespace, comments, etc */
            for (Token token : doc) {
                Integer type = token.getType();
                switch(type){
                    case 1:
                    case 3:
                    case 2:
                    case 4:
                    case 5:
                        break;
                    case 15:
                        if (token.getLexeme().trim().length() > 1) {
                            getValueHash(type, tokenInArray);
                        }
                        break;
                    case 20:
                        if (!token.getLexeme().equals(";")) {
                            getValueHash(type, tokenInArray);
                        }
                        break;
                    case 21:
                        break;
                    default:
                        //Check if the variable exist in the table
                        getValueHash(type, tokenInArray);
                        break;
                }
            }
        } catch (RuntimeException re) {
            throw re; //
        } catch (Exception e) {
            System.out.println(e);
        }
        return tokenInArray;
    }
}
