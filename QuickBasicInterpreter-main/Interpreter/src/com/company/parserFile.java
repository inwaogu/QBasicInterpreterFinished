package com.company;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;
import java.io.FileNotFoundException;

public class parserFile
{
    private FileWriter Output;
    private String filePath;
    private int tokenIndex;
    private String holder;
    private int ahead;
    private int behind;
    private int twoBehind;
    private int twoAhead;
    private String currentToken;
    private String nextToken;
    private ArrayList<String> tokenInfo = new ArrayList<>();
    private List<String> lineNumbers = new ArrayList<>();
    private int lineCount = 1;
    private boolean EOF;
    private int binaryTreePosition;

    TreeMap<Integer, String> binaryTree = new TreeMap<Integer, String>();

    parserFile(ArrayList<String> tokenInfo, List<String> lineNumbers) {
        this.lineNumbers = lineNumbers;
        tokenIndex = 0;
        this.tokenInfo = tokenInfo;
        currentToken = tokenInfo.get(tokenIndex);
        binaryTreePosition = 0;

        try {
            Files.deleteIfExists(Paths.get("parserOutput.txt"));
            Output = new FileWriter("parserOutput.txt", true);
        }

        catch (NoSuchFileException e) {
            System.out.println("No such file/directory exists");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
       // System.out.println("--------BINARY TREE-----------");
    }

    public void createBinaryTree() {

        int position = 1;


        while (tokenIndex < tokenInfo.size())
        {
            nextToken = lookAhead();
            outerloop:
            for (; position <= tokenInfo.size() - 1; position++)//checking for everything begins here
            {
                //region error checking
                if (currentToken.equals("INTEGER")) {
                    if (!(lookTwoAhead().equals("+") || lookTwoAhead().equals("-") || lookTwoAhead().equals("*") || lookTwoAhead().equals("/") || lookTwoAhead().equals("=") || lookTwoAhead().equals("LET") || !lineNumbersContain(lookTwoAhead()) || lookTwoAhead().equals(")") || lookTwoAhead().equals("EXP"))) {
                        for (; lineCount < lineNumbers.size();) {
                                System.out.println("Error: Integer needs to be followed by an arithmetic function"); //throw an error
                                return;
                        }
                    }
                }
                else if (currentToken.equals("FLOAT")) {
                    if (!(lookTwoAhead().equals("+") || lookTwoAhead().equals("-") || lookTwoAhead().equals("*") || lookTwoAhead().equals("/") || lookTwoAhead().equals("=") || lookTwoAhead().equals("LET") || !lineNumbersContain(lookTwoAhead()))) {
                        for (; lineCount < lineNumbers.size();) {
                            String temp = lookTwoAhead();
                            System.out.println("Error: FLOAT needs to be followed by an arithmetic function"); //throw an error
                            return;
                        }
                    }
                }
                //PRINT string, int, or variable
                else if (currentToken.equals("PRINT")) {
                    if  (!(lookAhead().equals("VAR") || lookAhead().equals("\"\"") || lookAhead().equals("INTEGER"))) {
                        System.out.println("Error: Print command must be followed by a String, VAR, or INTEGER"); //throw an error
                        return;
                    }
                    String temp = lookTwoAhead();
                }
                else if (currentToken.equals("LET")) {
                    if  ((lookAhead().equals("INTEGER")))
                    {
                        System.out.println("Error: Let keyword needs to be followed by a character"); //throw an error
                        return;
                    }
                }
                else if (currentToken.equals("=")) {
                    if (!(lookAhead().equals("INTEGER") || lookAhead().equals("VAR") || lookAhead().equals("\"\"") || lookAhead().equals("EXP") || lookAhead().equals("INT"))) {
                        System.out.println("Error: = operator must be followed by INT,VAR or STRING."); //throw an error
                        return;
                    }
                }
                else if (currentToken.equals("NEXT")) {
                    if (!(lookAhead().equals("VAR"))) {
                        System.out.println("Error: NEXT must be followed by VAR."); //throw an error
                        return;
                    }
                }
                else if (currentToken.equals("EXP"))
                {
                    if (!(lookAhead().equals("("))) {
                        System.out.println("Error: Unexpected token after EXP."); //throw an error
                        return;
                    }
                }
                else if (currentToken.equals("SQR"))
                {
                    if (!(lookAhead().equals("("))) {
                        System.out.println("Error: Unexpected token after SQR   ."); //throw an error
                        return;
                    }
                }
                else if (currentToken.equals("END"))
                {
                    String temp = lookAhead();
                }


                //endregion

                for (int x = 0; x < lineNumbers.size(); x++)
                {
                    if (currentToken.equals(lineNumbers.get(x)))
                    {
                        currentToken = getNextToken();
                        continue outerloop;
                    }
                }
                if (currentToken.equals("INTEGER")) {
                    currentToken = getNextToken();
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if (currentToken.equals("FLOAT")) {
                    currentToken = getNextToken();
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if (currentToken.equals("PRINT"))
                {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if (currentToken.equals("\"\""))
                {
                    currentToken = getNextToken();
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("+"))
                {

                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("LET"))
                {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("="))
                {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("VAR"))
                {
                    currentToken = getNextToken();
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("NEXT"))
                {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("END")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("(")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals(")")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("EXP")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("SQR")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("^")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("-")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("/")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
                else if(currentToken.equals("INT")) {
                    binaryTree.put(binaryTreePosition++, currentToken);
                    currentToken = getNextToken();
                    break;
                }
            }
        }

        for(Map.Entry m:binaryTree.entrySet())
        {
           // System.out.println("Key:"+m.getKey() + " Value:"+m.getValue());

            for (int count = 0; count < 1; count++) {
               try {
                      Output.write("Key:"+m.getKey() + " Value:"+m.getValue()+"\n");
                      break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
      //  System.out.println("--------BINARY TREE END-----------");
       try {
            Output.close();
        } catch (IOException e) {
            e.printStackTrace();
          }

    }

    private String getNextToken() {
        tokenIndex += 1;//calling this increments tokenIndex
        if (tokenIndex < tokenInfo.size())
            currentToken = tokenInfo.get(tokenIndex);
        return currentToken;
    }

    private String lookAhead() {
        if (tokenIndex < tokenInfo.size()-1) {
            ahead = tokenIndex + 1;
            holder = tokenInfo.get(ahead);
            return holder;
        } else {
            return "Error: no can do buckaroo";
        }
    }

    private String lookTwoAhead() {
        if (tokenIndex < tokenInfo.size() - 2) {
            twoAhead = tokenIndex + 2;
            holder = tokenInfo.get(twoAhead);
            return holder;
        } else {
            return "Error: no can do buckaroo";
        }
    }

    private String lookBehind() {
        behind = tokenIndex - 1;
        holder = tokenInfo.get(behind);
        return holder;
    }

    private String lookTwoBehind() {
        twoBehind = tokenIndex - 2;
        holder = tokenInfo.get(twoBehind);
        return holder;
    }

   private boolean lineNumbersContain(String x)
   {
       for (int i = 0; i < lineNumbers.size()-1; i++)
       {
           if (x.equals(lineNumbers))
           {
               return true;
           }
       }
       return false;
   }

    public TreeMap<Integer, String>  getBinaryTree(){
        return binaryTree;
      }


}
