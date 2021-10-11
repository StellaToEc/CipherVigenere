//Contreras Mendez Brandon CBC
//Hernandez Rojas Mauricio CTR
//Morales Hernandez Carlos Jesus CFB
//Toledo Espinosa Cristina Aline CBC

import java.nio.file.Files;
import java.util.Locale;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class vigenere{

    static int[] findnumbers(String text, char[] abc) {
        char textc[]=text.toCharArray();
        int[] textn= new int[textc.length];

        for(int i=0; i < textc.length; i++) {
            for(int j=0; j<abc.length;j++)
                if (abc[j] == textc[i]) {
                    textn[i] = j;
                    break;
                }
        }
        return textn;
    }

    static char[] findchars(int[] textn, char[]abc){
        char[]text= new char[textn.length];

        for (int i=0; i< textn.length; i++) {
            text[i] = abc[textn[i]];
        }
        return text;
    }

    static String cipherVigenere(String pt, String key, char[] abc,int elec){

        do {
            key+=key;
        }while(pt.length()>key.length());

        int ptn[]=findnumbers(pt,abc);
        int keyn[]=findnumbers(key,abc);


        if(elec==1) {
            for (int i = 0; i < ptn.length; i++) {
                ptn[i] = (ptn[i] + keyn[i]) % abc.length;
            }
            return String.valueOf(findchars(ptn, abc));
        }
        else{
            for(int i=0; i< ptn.length;i++) {
                ptn[i] = (ptn[i] - keyn[i]) % abc.length;
                if (ptn[i] < 0)
                    ptn[i] = abc.length + ptn[i];
            }
            return String.valueOf(findchars(ptn,abc));
        }
    }

    static String[] dividePlainText(String pt, String iv,char[] abc) {
        String[] m= new String[pt.length()];

        if(pt.length()%iv.length()!=0) {
            do {
                pt += " ";
            } while (pt.length() % iv.length() != 0);
        }

        for(int i=0; i<pt.length()/iv.length(); i++) {
            m[i] = pt.substring(iv.length()*i, iv.length()*(i+1));
        }
        return m;
    }

    static String modeCBCcipher(String pt, String iv,char[] abc,String key) {
        String[] divideText=dividePlainText(pt,iv,abc);
        String ct="";

        int i=0;
        do{
            int[] m = findnumbers(divideText[i],abc);
            int[] ivn= findnumbers(iv,abc);
            int[] res= new int[m.length];

            for(int j=0; j< m.length;j++)
                res[j]=m[j]^ivn[j];
            iv= cipherVigenere(String.valueOf(findchars(res,abc)),key,abc,1);
            ct+=iv;
            i++;
        }while (divideText[i]!=null);
        //System.out.println("Ciphertext" + ct);
        return ct;
    }

    static String modeCBCdecipher(String ct, String iv,char[] abc,String key) {
        String[] divideText=dividePlainText(ct,iv,abc);
        String ot="";

        int i=0;
        do{
            int[] m = findnumbers(divideText[i],abc);
            int[] ivn= findnumbers(iv,abc);
            String res= cipherVigenere(divideText[i],key,abc,2);


            int[] resn= findnumbers(res,abc);

            for(int j=0; j< m.length;j++)
                resn[j]=resn[j]^ivn[j];

            iv= divideText[i];

            ot+=String.valueOf(findchars(resn,abc));
            i++;
        }while (divideText[i]!=null);

        return ot;
    }

    static String fileMessage(String fileName, int opc) throws IOException {
        String dirActual = "Text";
        Path path = Paths.get("");
        String directoryName = path.toAbsolutePath().toString();

        directoryName+="\\"+dirActual+"\\"+fileName+".txt";

        byte[] source = Files.readAllBytes(Path.of(directoryName));
        String message = new String(source);

        if(opc==1){
            return message.toLowerCase(Locale.ROOT);
        }else {
            byte[] decodedBytes = Base64.getDecoder().decode(message);
            String decodedString = new String(decodedBytes);
            //decodedString.toLowerCase(Locale.ROOT);
            return decodedString;
        }
    }

    static void saveText(String text,String fileName, int opc) {
        String dirActual = "Text";
        Path path = Paths.get("");
        String directoryName = path.toAbsolutePath().toString();

        directoryName+="\\"+dirActual+"\\"+fileName+"Edited.txt";

        try {
            File fileText = new File(directoryName);
            FileWriter pw = new FileWriter(fileText, true);

            if(opc==1){
                pw.write(Base64.getEncoder().encodeToString(text.getBytes()));
                System.out.println("Ciphertext stored correctly");
            }else{
                pw.write(text);
                System.out.println("Decrypted text stored correctly");
            }

            pw.close();
        }
        catch (Exception e) {
            System.out.println("Error writing text");
        }
    }

    public static void main (String[]args) throws  IOException{
        char abc[]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',' ',',','.','\n','’','?'};

        Scanner reader = new Scanner(System.in);

        System.out.println("Write the file:");
        String namefile= reader.nextLine();
        System.out.println("Write the IV:");
        String iv = reader.nextLine();
        System.out.println("Write the key:");
        String key = reader.nextLine();

        if(iv.length()!=key.length()) {
            do {
                System.out.println("Please try again the IV and the key will have the same size");
                System.out.println("Write the IV:");
                iv = reader.nextLine();
                System.out.println("Write the key:");
                key = reader.nextLine();
            } while (iv.length() != key.length());
        }

        System.out.println("to encrypt press 1 to decrypt 2");
        int elec= reader.nextInt();

        switch (elec){
            case 1-> saveText(modeCBCcipher(fileMessage(namefile,1),iv,abc,key),namefile,1);
            case 2-> saveText(modeCBCdecipher(fileMessage(namefile,2),iv,abc,key),namefile,2);
        }
    }
}