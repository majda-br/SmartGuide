import java.util.Scanner;

public class MyClass {
    public static void main(String args[]) {
        String test = "resistance:10.234_";
        char [] e = test.toCharArray(); 
        //System.out.println(somme_des_caracteres(e));
        
        /* byte[] un = new byte[1];
        un[0] = 10; 
        System.out.println(hexa(un)); */
        
        //System.out.println(hexa_convertor(1586));
        char [] nom_variable = "fer".toCharArray();
        double d = 5.567;
        float val = (float)d;
        
        char [] a = new char[5];
        String floattostring = Float.toString(val);
        a = floattostring.toCharArray();
            
        int mess_length = nom_variable.length + a.length + 4; 
        char [] message = new char[mess_length];

        System.out.println(mess_length);

        int resultat;
        resultat = encodeur(message, 25, 1, nom_variable , val);
    }
    
    public static int somme_des_caracteres(char [] chaine){
        int somme = 0;
        int cmpt;
        int taille = chaine.length;
        for (cmpt =0; cmpt < taille; cmpt ++){
            somme += (int)chaine[cmpt];
        }
        //System.out.println(somme);
        return somme;
    }

    /* private static final char[] valeurs = "0123456789ABCDEF".toCharArray();     
    public static final String hexa(byte[] bytes) {
        char[] hexchar = new char[bytes.length*2];
        for (int j=0; j<bytes.length; j++){
            int v = bytes[j] & 0xFF;
            hexchar[j*2] = valeurs[v >> 4];
            hexchar[j*2+1] = valeurs[v&0x0F];
        }
        return new String(hexchar); 
    } */
    
    public static final char [] hexa_convertor(int decimal){
        char [] hexadecimal = new char[2];
        int decimal_divise = decimal%256;
        char [] valeur = "0123456789ABCDEF".toCharArray();
        int dizaine = (char)(decimal_divise) >> 4;
        int unite = decimal_divise & 0x0F;
        hexadecimal[0] = valeur[dizaine];
        hexadecimal[1] = valeur[unite];
        return hexadecimal;
    }
    
    public static int encodeur(char[] message, int taille, int type_message, char[] nom_variable, float valeur){
        int nv_taille = nom_variable.length;
        char [] a = new char[5];
        int i;
        int a_taille = a.length;
        if (type_message ==1){
            /*modifier*/
            /*------------Changer float -> char[]
            on veut que a soit un char avec la valeur valeur dedans */
            String floattostring = Float.toString(valeur);
            a = floattostring.toCharArray();
            //System.out.println(a);
            //----------------------------------------------
            if (nv_taille + 1 + a_taille + 4 < taille){
                
                for (i =0; i < nv_taille; i++){
                    message[i] = nom_variable[i];
                }
                //vérifer que message = 'nom_variable'
                if (message == nom_variable){
                    System.out.println("ERROR");
                }
                else{
                    message[nv_taille] = ':';
                    //message = 'nom_variable:'
                    for (i = nv_taille+1; i < nv_taille +1+ a_taille; i++){
                        message[i] = a[i-(nv_taille)-1];
                    }
                    System.out.println(message);
                    //message = 'nom_variable:valeur'        
                    message[nv_taille+1+a_taille] = '_';
                    //message = 'nom_variable:valeur_'
                    System.out.println(message);
                    char [] hexa;
                    int somme = somme_des_caracteres(message);
                    //System.out.println(somme);
                    hexa = hexa_convertor(somme);
                    System.out.println(message);
                
                    for(i=nv_taille+1+a_taille+1;i<nv_taille + 1 +a_taille+3;i++){
                        message[i] = hexa[i-(nv_taille+1+a_taille+1)];
                    }
		    //message = 'nom_variable:valeur_FF'
                    System.out.println(message);
                    return 0;    
                }
            }
            else{
                System.out.println("ERREUR de taille du message");
                return 101;
            }
        }
        else if (type_message ==0){
            //consultation
            if (nv_taille +5 < taille) {

            for (i = 0; i < nv_taille; i++) {
                message[i] = nom_variable[i];
            }
            //message = 'nom_variable'
            message[nv_taille] = '?';
            //message = 'nom_variable?'
            message[nv_taille+1] = '_';
            //message = 'nom_variable?_'

            char [] hexa;
            int somme = somme_des_caracteres(message);
            //System.out.println(somme);
            hexa = hexa_convertor(somme);

            for(i = nv_taille+2;i<nv_taille+5;i++){
                message[i] = hexa[i-(nv_taille+2)];
            }
	    // message = 'nom_variable?_FF'
            return 0;
            }
            else{
                //erreur de sizeof message
                return 101;
            }
        }
        else{
            //printf("Type_message 0 ou 1");
            return 102; 
        }
        return 0;
    }
}
