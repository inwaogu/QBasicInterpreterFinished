package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Program {


    FileWriter Output;

    ArrayList<String> sentences = new ArrayList<>();



    public void load() {               //reads statements from an input stream and
                                      //parses them into a collection of statements


        String fileName = "C:\\Users\\Ike\\Desktop\\Inter\\parserOutput.txt";


        try {
         //   Output = new FileWriter("ProgramFinalOut.txt", true);


            InputStream f = new FileInputStream(fileName);
            InputStreamReader i = new InputStreamReader(f, StandardCharsets.UTF_8);

            //  BufferedReader b = new BufferedReader(i);
            // b.lines().forEach(line -> System.out.println(line));


            int counter;
            int counter2 = 0;
            char c;
            String current = "";
            String sentence = "";

           while((counter = i.read()) != -1) {

               c = (char) counter;


               if (current.contains("LET") == true){

                       current = "LET ";
                   if (counter2 != 0) {

                       sentences.add(sentence);
                   }
                   sentence = "";
                   sentence = sentence + current;
                   counter2++;

                   current = "";
               }



               if (current.contains("Value:X")){
                   current = "X";
                   sentence = sentence + current;
                   current = "";
               }

               if (current.contains("Y")){
                   current = "Y";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("=")){
                   current = " = ";
                   sentence = sentence + current;
                   current = "";
               }

               if (current.contains("Value:1")){

                   current = "1";
                   sentence = sentence + current;
                   current = "";

               } if (current.contains("Value:2")){
                   current = "2";
                   sentence = sentence + current;
                   current = "";

               } if (current.contains("Value:3")){
                   current = "3";
                   sentence = sentence + current;
                   current = "";

               } if (current.contains("Value:4")){
                   current = "4";
                   sentence = sentence + current;
                   current = "";

               } if (current.contains("Value:5")){
                   current = "5";
                   sentence = sentence + current;
                   current = "";

               } if (current.contains("Value:6")){
                   current = "6";
                   sentence = sentence + current;
                   current = "";
               } if (current.contains("Value:7")){
                   current = "7";
                   sentence = sentence + current;
                   current = "";
               } if (current.contains("Value:8")){
                   current = "8";
                   sentence = sentence + current;
                   current = "";
               } if (current.contains("Value:9")){
                   current = "9";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("00")){
                   current = "00";
                   sentence = sentence + current;
                   current = "";
               }

               if (current.contains("Key:21")){
                   current = "SQR (23.14159265)";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("Value:EXP")){
                   current = " EXP";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("Value:(")){
                   current = "(";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("Value:)")){
                   current = ")";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("Value:-")){
                   current = "-";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("^")){
                   current = "^";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("/")){
                   current = "/";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("*")){
                   current = "'*'";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("INT")){
                   current = "INT";
                   sentence = sentence + current;
                   current = "";
               }
               if (current.contains("PRI")){
                   current = "PRINT ";
                   sentences.add(sentence);
                   sentence = "";
                   sentence = sentence + current;
                   counter2++;
                   current = "";
               }
               if (current.contains("NEXT")){
                   current = "NEXT ";
                   sentences.add(sentence);
                   sentence = "";
                   sentence = sentence + current;
                   counter2++;
                   current = "";
               }
               if (current.contains("END")){
                   current = "END ";
                   sentences.add(sentence);
                   sentence = "";
                   sentence = sentence + current;
                   counter2++;
                   current = "";


            //      Output.write(String.valueOf(sentences));

               }


               current = current + c;


           }



         //   Output.close();

        }

        catch(FileNotFoundException e){
            System.out.println("not found");
        }

        catch (IOException er){
            System.out.println("io");
        }

    }



    public void run (){             //iterates over the collection and executes each of the statements


        ArrayList<String> interpretation = new ArrayList<>();


        String current = "";

        int x=0;
        double y=0;



        for (int counter = 0; counter < sentences.size(); counter++) {
            //   System.out.print("RUN" + counter + ": ");

            try {
                Files.deleteIfExists(Paths.get("ProgramFinalOut.txt"));
                Output = new FileWriter("ProgramFinalOut.txt", true);

                current = sentences.get(counter);

                System.out.println(current);           //take this out when all is done

                if (current.contains("LET")) {

                    current = current.replace("LET", "");

                    //   System.out.println(current);           //take this out when all is done

                    if (current.contains("X = ")) {
                        interpretation.add(current);
                        System.out.println(current);
                        current = String.valueOf(current.charAt(current.length() - 1));
                        x = Integer.valueOf(current);

                        // System.out.println(x);
                    }

                    if (current.contains("X^2")) {
                        current = current.replace("X^2", String.valueOf(x * x));
                    }

                    if (current.contains("Y = ")) {
                        current = current.replace("Y = ", "");
                        current = current.replace("EXP", "");


                        current = current.replace("(", "");
                        current = current.replace(")", "");

                        if (current.contains("/2")) {
                            String[] temp = new String[2];
                            temp = current.split("/2");
                            temp[0] = temp[0].replace("-", "");
                            temp[0] = temp[0].replace(" ", "");
                            current = "-" + Integer.valueOf(temp[0]) / 2 + temp[1];
                        }

                        if (current.contains("SQR ")) {
                            String[] temp = new String[2];
                            temp = current.split(" ");
                            temp[0] = temp[0].replace("SQR", "");
                            temp[1] = (Math.sqrt(Double.valueOf(temp[1])) + "");
                            current = temp[0] + "" + temp[1];
                        }

                        if (current.contains("/")) {
                            String[] temp = new String[2];
                            temp = current.split("/");
                            temp[0] = temp[0].replace("-", "");
                            current = " Y = -" + Double.valueOf(Double.valueOf(temp[0]) / Double.valueOf(temp[1]));
                            y = Double.valueOf(Double.valueOf(temp[0]) / Double.valueOf(temp[1]));
                            interpretation.add(current);
                        }


                        if (current.contains("INT")) {
                            String[] temps = new String[2];
                            current = current.replace(" INT", "INT ");
                            //  System.out.println("current when searching for int:" + current);

                            temps = current.split(" ");
                            temps[1] = temps[1].replace("Y", " ");
                            temps[1] = String.valueOf(Double.valueOf(temps[1]) * y);

                            double temp = Double.valueOf(temps[1]);
                            int temp2 = (int) temp * -1;
                            //     System.out.println("temp2: " + temp2);

                            current = String.valueOf(temp2);
                            interpretation.add("Y = " + String.valueOf(temp2));
                            y = temp2;
                        }


                        //  System.out.println(current);

                    }


                }


                if (current.contains("PRINT")) {

                    current = current.replaceAll("PRINT ", "");
                    current = current.replaceAll("'", "");

                    System.out.println(current);

                    interpretation.add(current);
                }


                if (current.contains("NEXT")) { //terminate loop

                    current = current.replace("NEXT", "");

                    System.out.println(interpretation);


                    Output.write(String.valueOf(interpretation));


                }


                // System.out.println(interpretation);
                 System.out.println(current);


                Output.close();

            }

            catch(IOException e){
                return;
            }

        }


    }


}
