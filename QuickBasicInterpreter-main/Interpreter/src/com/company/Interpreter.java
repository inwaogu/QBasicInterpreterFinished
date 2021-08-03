package com.company;

import java.io.*;
import java.util.*;


public class Interpreter {


    ArrayList<String> tokenInfo = Lexer.getTokenInfo();
    List<String> lineNumbers = Lexer.getLineNumbers();

    private parserFile parserFile = Lexer.getParserFile();


    //parserFile p = new parserFile(tokenInfo, lineNumbers);
    //parserFile = new parserFile(tokenInfo, lineNumbers);
    //  parserFile.createBinaryTree();


    public TreeMap<Integer, String> map;
    //Set<Integer> keys = map.keySet();


    public void traverse()          //traverse the binary tree
    {
      //  map = parserFile.getBinaryTree();


        Program pro = new Program();
        pro.load();
        pro.run();
    }


}

