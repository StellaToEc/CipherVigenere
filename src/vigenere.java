import java.util.Scanner;

public class vigenere {

    static int[] findnumbers(String text, char[] abc)
    {
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

        for (int i=0; i< textn.length; i++)
            text[i]=abc[textn[i]];
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
                ptn[i]=(ptn[i]-keyn[i])% abc.length;
                if(ptn[i]<0)
                    ptn[i]= abc.length+ptn[i];
            }
            return String.valueOf(findchars(ptn,abc));
        }
    }

    static String[] dividePlainText(String pt, String iv,char[] abc)
    {
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

    static String modeCBCcipher(String pt, String iv,char[] abc,String key)
    {
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

        return ct;
    }

    static String modeCBCdecipher(String ct, String iv,char[] abc,String key)
    {
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


      public static void main (String[]args){
          char abc[]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',' ',',','.','-','\''};

          Scanner reader = new Scanner(System.in);
          System.out.println("Write the text:");
          String pt = reader.nextLine();
          System.out.println("Write the IV:");
          String iv= reader.nextLine();
          System.out.println("Write the key:");
          String key= reader.nextLine();

          System.out.println("to encrypt press 1 to decrypt 2");
          int elec= reader.nextInt();

          System.out.println(modeCBCcipher(pt,iv,abc,key));
          System.out.println(modeCBCdecipher(modeCBCcipher(pt,iv,abc,key),iv,abc,key));
      }
}