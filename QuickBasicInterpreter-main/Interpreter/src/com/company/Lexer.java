package com.company;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Lexer {

    private final String codePath; //pathway of the file containing the Quick Basic code
    private Scanner reader;
    private String basicCode = "";
    private int position;
    private String currentString;
    private static List<String> basicCodeList;
    private String previousCharacter;
    private List<String> listOfVariables;
    private FileWriter Output;
    private static parserFile parserFile;
    private String holdNextLine;
    protected static List<String> lineNumbers;
    private int tempCount;
    private String hold;
    private int posHold;
    private String currChar;
    public static ArrayList<String> tokenInfo = new ArrayList<>();


    Lexer() {
        codePath = "QuickBasicInterpreter-main/Interpreter/src/com/company/basiccode";
        position = -1;
        previousCharacter = "";
        listOfVariables = new LinkedList<>();
        lineNumbers = new ArrayList<>();
    }

    protected void readFile() {
        try {
            reader = new Scanner(new File(codePath));
            while (reader.hasNextLine()) {
                holdNextLine = reader.nextLine();
                if (holdNextLine.contains("REM"))//REM = comment
                    continue;
                if (!holdNextLine.equals("")) {
                    lineNumbers.add(holdNextLine.substring(0, 3));
                    basicCode += holdNextLine + " ";
                }
            }
            reader.close();


            basicCodeList = new LinkedList<>(Arrays.asList(basicCode.split(" ")));
        } catch (FileNotFoundException e) {
            System.out.print("File not found: " + e.getMessage());
        }
    }

    protected void createTokens() throws IOException {
        /*
    * LEXEME - Sequence of characters matched by PATTERN forming the TOKEN
      PATTERN - The set of rule that define a TOKEN
       TOKEN - The meaningful collection of characters over the character set of the programming language ex:ID, Constant, Keywords, Operators, Punctuation, Literal String
        [Token]       [Informal Description]                  [Sample Lexemes]
        if            characters i, f                         if
        else          characters e, l, s, e                   else
        comparison    < or > or <= or >= or == or !=          <=, !=
        id            letter followed by letters and digits   pi, score, D2
        number        any numeric constant                    3.14159, 0, 6.02e23
        literal       anything but ", surrounded by "'s       "core dumped"
        * */
        ArrayList<String> tokenType = new ArrayList<>();
        tokenType.add("INTEGER"); //0
        tokenType.add("PRINT");//1
        tokenType.add("PLUS");//2
        tokenType.add("SUB");//3
        tokenType.add("MULT");//4
        tokenType.add("DIVIDE");//5
        tokenType.add("STRING");//6
        tokenType.add("EQUALS");//7
        tokenType.add("GREATER_THAN");//8
        tokenType.add("LESS_THAN");//9
        tokenType.add("GREATER_THAN_EQUALS");//10
        tokenType.add("LESS_THAN_EQUALS");//11
        tokenType.add("COLON");//12
        tokenType.add("FOR");//13
        tokenType.add("TO");//14
        tokenType.add("STEP");//15
        tokenType.add("NEXT");//16
        tokenType.add("IF");//17
        tokenType.add("LET");//18
        tokenType.add("VAR");//19
        tokenType.add("SEMICOLON");//20
        tokenType.add("THEN");//21
        tokenType.add("LPAR");//22
        tokenType.add("RPAR");//23
        tokenType.add("DEF");//24
        tokenType.add("EXP");//25
        tokenType.add("SQR");//26
        tokenType.add("END");//27
        tokenType.add("CARROT");//28
        tokenType.add("FLOAT");//29
        tokenType.add("INT");//30

        //holds token info
        try {
            Files.deleteIfExists(Paths.get("output.txt"));
        } catch (NoSuchFileException e) {
            System.out.println("No such file/directory exists");
        }
        //gets our first character
        getNextString();
        try {
            Output = new FileWriter("output.txt", true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //99:
        //Stores symbolTable in array
        String symbolTable[] = createSymbolTable("QuickBasicInterpreter-main/Interpreter/src/com/company/symbolTable");

        int lineCounter = 0;
        while (position < basicCodeList.size()) {
            handleColon();

            for (int i = 0; i < symbolTable.length; i += 2) {
                if (symbolTable[i].equals("PRINT") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + symbolTable[i] + " Token:" + tokenType.get(1) + "\n");
                    tokenInfo.add("PRINT");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }
                //INTEGER token  //current character = 5
                else if (symbolTable[i].equals("INTEGER") && currentString.matches(symbolTable[i + 1])) {
                    if (!currentString.equals(lineNumbers.get(lineCounter))) {
                        Output.write("Lexeme:" + symbolTable[i] + " Token:" + tokenType.get(0) + " Value:" + currentString + "\n");
                        tokenInfo.add("INTEGER");
                        tokenInfo.add(currentString);
                    } else {
                        tokenInfo.add(lineNumbers.get(lineCounter));
                        if (lineCounter < lineNumbers.size() - 1)
                            ++lineCounter;
                        //System.out.println(lineCounter);
                    }
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }
                //PLUS token
                else if (symbolTable[i].equals("+") && currentString.contains(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "+" + " Token:" + tokenType.get(2) + "\n");
                    tokenInfo.add("+");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }
                //SUB token
                else if (symbolTable[i].equals("-") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "-" + " Token:" + tokenType.get(3) + "\n");
                    tokenInfo.add("-");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }
                //MULT token
                else if (symbolTable[i].equals("*") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "*" + " Token:" + tokenType.get(4) + "\n");
                    tokenInfo.add("*");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }
                //DIV token
                else if (symbolTable[i].equals("/") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "/" + " Token:" + tokenType.get(5) + "\n");
                    tokenInfo.add("/");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }
                //STRING token
                else if (symbolTable[i].equals("\"") && currentString.startsWith(symbolTable[i + 1])) {
                    //Output.write(currentString);
                    String holdcurrentString = "";
                    String fullString = "";
                    if (!currentString.endsWith("\"")) {
                        holdcurrentString = currentString;
                        boolean isCompleteString = false;
                        while (!isCompleteString) {
                            getNextString();
                            fullString += currentString + " ";
                            if (currentString.endsWith("\"")) {
                                Output.write("Lexeme:" + "\"\"" + " Token:" + tokenType.get(6) + " Value:" + holdcurrentString + " " + fullString + "\n");
                                // "
                                // "
                                tokenInfo.add("\"\"");
                                tokenInfo.add(holdcurrentString + " " + fullString);
                                //tokenInfo.add(holdcurrentString+" "+fullString);

                                getNextString();
                                isCompleteString = true;
                            }
                        }
                    } else {
                        holdcurrentString = currentString;
                        Output.write("Lexeme:" + "\"\"" + " Token:" + tokenType.get(6) + " Value:" + holdcurrentString + "\n");
                        tokenInfo.add("\"\"");
                        tokenInfo.add(holdcurrentString);
                        getNextString();
                        handleColon();
                        handleSemicolon();
                        //break;
                    }
                } else if (symbolTable[i].equals("=") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "=" + " Token:" + tokenType.get(7) + "\n");
                    tokenInfo.add("=");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals(">") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + ">" + " Token:" + tokenType.get(8) + "\n");
                    tokenInfo.add(">");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("<") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "<" + " Token:" + tokenType.get(9) + "\n");
                    tokenInfo.add("<");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals(">=") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + ">=" + " Token:" + tokenType.get(10) + "\n");
                    tokenInfo.add(">=");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("<=") && currentString.startsWith(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "<=" + " Token:" + tokenType.get(11) + "\n");
                    tokenInfo.add("<=");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals(":") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + ":" + " Token:" + tokenType.get(12) + "\n");
                    tokenInfo.add(":");
                    getNextString();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("FOR") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "FOR" + " Token:" + tokenType.get(13) + "\n");
                    tokenInfo.add("FOR");
                    previousCharacter = tokenType.get(13);
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("TO") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "TO" + " Token:" + tokenType.get(14) + "\n");
                    tokenInfo.add("TO");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("STEP") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "STEP" + " Token:" + tokenType.get(15) + "\n");
                    tokenInfo.add("STEP");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("NEXT") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "NEXT" + " Token:" + tokenType.get(16) + "\n");
                    tokenInfo.add("NEXT");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("IF") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "IF" + " Token:" + tokenType.get(17) + "\n");
                    tokenInfo.add("IF");
                    getNextString();
                    handleSemicolon();
                    handleColon();
                    break;
                } else if (symbolTable[i].equals("LET") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "LET" + " Token:" + tokenType.get(18) + "\n");
                    tokenInfo.add("LET");
                    previousCharacter = tokenType.get(18);
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if ((getPreviousCharacter().equals(tokenType.get(18)) && symbolTable[i].equals("VAR")) || (getPreviousCharacter().equals(tokenType.get(13)) && symbolTable[i].equals("VAR")) || listOfVariables.contains(currentString)
                        || currentString.endsWith("$") || (getPreviousCharacter().equals(tokenType.get(17)) && symbolTable[i].equals("VAR")) || (getPreviousCharacter().equals(tokenType.get(16)) && symbolTable[i].equals("VAR"))) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(19) + "\n");
                    //created a list to keep track of the list of variables because if there wasn't one there and the code ran into spot where it
                    //wrote PRINT BOTTLES$ it wouldn't know how to handle it because the code before only assumed it was a variable if it began with
                    //LET or FOR
                    listOfVariables.add(currentString);
                    tokenInfo.add("VAR");
                    tokenInfo.add(currentString);
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    previousCharacter = getPreviousCharacter();
                    break;
                } else if (symbolTable[i].equals("SEMICOLON") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(20) + "\n");
                    tokenInfo.add(";");
                    getNextString();
                    handleColon();
                    break;
                } else if (symbolTable[i].equals("THEN") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + "THEN" + " Token:" + tokenType.get(21) + "\n");
                    tokenInfo.add("THEN");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("LPAR") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(22) + "\n");
                    tokenInfo.add("(");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("RPAR") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(23) + "\n");
                    tokenInfo.add(")");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("DEF") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(24) + "\n");
                    tokenInfo.add("DEF");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("EXP") && currentString.contains("EXP")) {
                    tokenInfo.add("EXP");
                    String tempString = currentString.substring(3);
                    basicCodeList.set(position, currentString.substring(0, 3));
                    Output.write("Lexeme:" + currentString.substring(0, 3) + " Token:" + tokenType.get(25) + "\n");
                    posHold = position + 1;
                    for (int x = 0; x < tempString.length(); x++) {
                        currChar = String.valueOf(tempString.charAt(x));
                        if (currChar.equals("S"))
                            break;
                        basicCodeList.add(posHold++, String.valueOf(tempString.charAt(x)));
                    }
                    tempString = currentString.substring(14);
                    basicCodeList.add(posHold++, tempString);
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("SQR") && currentString.contains("SQR")) {
                    tokenInfo.add("SQR");
                    String tempString = currentString.substring(3);
                    basicCodeList.set(position, currentString.substring(0, 3));
                    Output.write("Lexeme:" + currentString.substring(0, 3) + " Token:" + tokenType.get(26) + "\n");
                    posHold = position + 1;
                    String temp = tempString.substring(0, 1);
                    basicCodeList.add(posHold++, temp);
                    temp = tempString.substring(1, 12);
                    basicCodeList.add(posHold++, temp);
                    basicCodeList.add(posHold++, String.valueOf(tempString.charAt(12)));
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("END") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(27) + "\n");
                    tokenInfo.add("END");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("CARROT") && currentString.equals(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(28) + "\n");
                    tokenInfo.add("^");
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("FLOAT") && currentString.matches(symbolTable[i + 1])) {
                    Output.write("Lexeme:" + currentString + " Token:" + tokenType.get(29) + "\n");
                    tokenInfo.add("FLOAT");
                    tokenInfo.add(currentString);
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                } else if (symbolTable[i].equals("INT") && currentString.contains("INT")) {
                    tokenInfo.add("INT");//output INT
                    String tempString = currentString.substring(3);
                    basicCodeList.set(position, currentString.substring(0, 3));
                    Output.write("Lexeme:" + currentString.substring(0, 3) + " Token:" + tokenType.get(30) + "\n");
                    posHold = position + 1;
                    String temp = tempString.substring(0, 1);
                    basicCodeList.add(posHold++, temp);
                    temp = tempString.substring(1, 4);
                    basicCodeList.add(posHold++, temp);
                    temp = tempString.substring(4, 5);
                    basicCodeList.add(posHold++, temp);
                    basicCodeList.add(posHold++, String.valueOf(tempString.charAt(5)));
                    getNextString();
                    handleColon();
                    handleSemicolon();
                    break;
                }

            }

        }
        Output.close();


        parserFile = new parserFile(tokenInfo, lineNumbers);
        parserFile.createBinaryTree();

        Interpreter interpreter = new Interpreter();
        interpreter.traverse();
    }



    private void handleColon() {
        if (currentString.endsWith(":") && !currentString.equals(":")) {

            String reformatCharacter = currentString.replace(":", "").trim();
            basicCodeList.set(position, reformatCharacter);
            currentString = basicCodeList.get(position);
            basicCodeList.add(position + 1, ":");
            //dont change these position variables. add inserts before it and set replaces at position

        }
    }

    private void handleSemicolon() {
        if (currentString.endsWith(";") && !currentString.equals(";")) {

            String reformatCharacter = currentString.replace(";", "").trim();
            basicCodeList.set(position, reformatCharacter);
            currentString = basicCodeList.get(position);
            basicCodeList.add(position + 1, ";");
            //dont change these position variables. add inserts before it and set replaces at position


        }
    }

    private String[] createSymbolTable(String symbolTablePath) {
        String[] symbolTable = {};
        boolean fileExists = false;
        String symbolTableContents = "";

        try {
            reader = new Scanner(new File(symbolTablePath));
            fileExists = true;
        } catch (FileNotFoundException e) {
            System.out.print("File not found: " + e.getMessage());
        }


        if (fileExists) {
            while (reader.hasNextLine()) {
                symbolTableContents += reader.nextLine();
                //Output.write(symbolTableContents);
                symbolTable = symbolTableContents.split("\\|");
            }

        } else {
            System.out.println("File does not exist so symbol table could not be created");
        }
        return symbolTable;
    }

    protected String getNextString() {
        position += 1;
        if (position < basicCodeList.size())
            currentString = basicCodeList.get(position);
        return currentString;
    }

    protected String getPreviousCharacter() {
        if (position > 0)
            tempCount = position - 1;
        hold = basicCodeList.get(tempCount);

        return hold;
    }

    public static List<String> getBasicCodeList() {
        return basicCodeList;
    }

    public static ArrayList getTokenInfo() {
        return tokenInfo;
    }

    public static List<String> getLineNumbers()
    {
        return lineNumbers;
    }

    public static parserFile getParserFile(){
        return parserFile;
    }


}

