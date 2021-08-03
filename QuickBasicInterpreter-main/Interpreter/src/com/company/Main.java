package com.company;

import java.io.IOException;
/*
* Class: CS 4308 Section 2
* Term: Spring 2021
* Name: Ike Nwaogu, Cameron Jones, Cline Graham
* Instructor: Deepa Muralidhar
* Project: Deliverable 1 Scanner-Java
* */
public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.readFile();
        try {
            lexer.createTokens();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
