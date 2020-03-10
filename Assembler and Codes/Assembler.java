import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Assembler {
    public static void main(String[]args) throws FileNotFoundException {
        MultiCoreAssembler("C:\\Users\\hiroh\\Desktop\\MInput5.2.txt");
        System.out.println("Multi Core Done");
//        SingleCoreAssembler("C:\\Users\\hiroh\\Desktop\\SInput.txt");
//        System.out.println("Single Core Done");
    }


    public static void MultiCoreAssembler(String path) throws FileNotFoundException{
        String full1 = "";
        String full2 = "";
        Scanner reader = new Scanner(new File(path));

        HashMap<String, String> tempAdrs = new HashMap<>();

        while (reader.hasNextLine()){
            Boolean flag = false;
            String str = reader.nextLine();
            String [] split1 = str.split("#");
            String[] split;
            if(split1.length>0){
                split = split1[0].split(" ");
            }
            else{
                split = str.split(" ");
            }

            if(split[1].equals("EQ")){
                tempAdrs.put(split[0],split[2]);
                flag = true;
            }

            if(!split[0].equals("MV")){

                //String combined =combineInstruction(split[0],split[2] ) + " ";
                if(tempAdrs.containsKey(split[2])){
                    String combined = combineInstruction(split[0],tempAdrs.get(split[2]))+ " ";
                    if(split[1].equals("0")&& flag == false){
                        full1+=combined;
                    }
                    else if(split[1].equals("1") && flag == false){
                        full2+=combined;
                    }
                    else if(split[1].equals("X")  && flag == false){
                        full1+=combined;
                        full2+=combined;
                    }
                }
                else if(flag == false) {
                    String combined = combineInstruction(split[0],split[2])+ " ";
                    if (split[1].equals("0") && flag == false) {
                        full1 += combined;
                    }
                    else if (split[1].equals("1") && flag == false) {
                        full2 += combined;
                    }
                    else if (split[1].equals("X") && flag == false) {
                        full1 += combined;
                        full2 += combined;
                    }
                }
            }
            else{
                String adr1 = split[2];
                String adr2 = split[3];
                if(tempAdrs.containsKey(split[2]))
                    adr1 = tempAdrs.get(split[2]);
                if (tempAdrs.containsKey(split[3]))
                    adr2 = tempAdrs.get(split[3]);


                String a = "1"+decimalToBinary(adr1)+""+decimalToBinary(adr2);
                String b = binaryToHex(a) + " ";

                if(split[1].equals("0")){
                    full1+=b;
                }
                else if(split[1].equals("1")){
                    full2+=b;
                }
            }
        }
        PrintWriter writer1 = null;
        PrintWriter writer2 = null;
        try {
            writer1 = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\hiroh\\Desktop\\MCOut1.asm"), "utf-8"));
            writer1.println("v2.0 raw");
            writer1.println(full1);

            writer2 = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\hiroh\\Desktop\\MCOut2.asm"), "utf-8"));
            writer2.println("v2.0 raw");
            writer2.println(full2);
        } catch (IOException ex) {
            // Report
        } finally {
            try {
                writer1.close();
                writer2.close();
            }
            catch (Exception ex) {/*ignore*/}
        }
    }

    public static void SingleCoreAssembler(String path) throws FileNotFoundException {
        String needed = "";
        Scanner reader = new Scanner(new File(path));
        HashMap<String, String> tempAdrs = new HashMap<>();

        while (reader.hasNextLine()){
            Boolean flag = false;
            String str = reader.nextLine();
            String [] split1 = str.split("#");
            String[] split;
            if(split1.length>0){
                split = split1[0].split(" ");
            }
            else{
                split = str.split(" ");
            }

            if(split[1].equals("EQ")){
                tempAdrs.put(split[0],split[2]);
                flag = true;
            }

            if(tempAdrs.containsKey(split[1] )&& flag == false){
                needed += combineInstruction(split[0],tempAdrs.get(split[1]))+ " ";
            }
            else if(flag == false){
                needed += combineInstruction(split[0],split[1])+ " ";
            }

        }
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\hiroh\\Desktop\\SingleCoreOutput.asm"), "utf-8"));
            writer.println("v2.0 raw");
            writer.println(needed);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }

    }

    private static String combineInstruction(String ins , String adr){
        //System.out.println(returnBinary(ins) + decimalToBinary(adr));
        return binaryToHex(returnBinary(ins) + decimalToBinary(adr));
    }

    private static String returnBinary(String ins){
        String instNumber = "";
        if(ins.equals("BZ"))
            instNumber = "000000";
        else if(ins.equals("BN"))
            instNumber = "000001";
        else if(ins.equals("ST"))
            instNumber = "000010";
        else if(ins.equals("RL"))
            instNumber = "000011";
        else if(ins.equals("LI"))
            instNumber = "000100";
        else if(ins.equals("LM"))
            instNumber = "000101";
        else if(ins.equals("AD"))
            instNumber = "000110";
        else if(ins.equals("SB"))
            instNumber = "000111";
        else if(ins.equals("MV"))
            instNumber = "1";
        else return null;

        return instNumber;
    }


    private static String decimalToBinary(String adrDec){
        int n, count = 0, a;
        String adrBin = "";
        int adrInt = Integer.parseInt(adrDec);

        adrBin = Integer.toBinaryString(adrInt);

        String zeroes= "";
        String fullAdr = "";
        for (int i= 0; i<5-adrBin.length(); i++){
            zeroes+="0";
        }
        fullAdr=zeroes+adrBin;
        return fullAdr;
    }



    private static String binaryToHex(String binary) {
        int decimalValue = 0;
        int length = binary.length() - 1;
        for (int i = 0; i < binary.length(); i++) {
            decimalValue += Integer.parseInt(binary.charAt(i) + "") * Math.pow(2, length);
            length--;
        }
        if(decimalValue != 00000000000){
            return decimalToHex(decimalValue);
        }
        return "00";
    }
    private static String decimalToHex(int decimal){
        String hex = "";
        while (decimal != 0){
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        return hex;
    }

    private static char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0)
            return (char)(hexValue + '0');
        else
            return (char)(hexValue - 10 + 'A');
    }
}
