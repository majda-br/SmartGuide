import java.lang.*;
import java.util.Scanner;

public class MyClass {
    public static void main(String args[]) {
        
    }
    
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
    
    public static int decodeur(char [] message, int taille_message, int  type_message,char [] nom_variable,int taille_variable, float valeur){
        if (taille_message > 3){
            if (message[taille_message-3]=='_'){
                int cmpt = 0;
                int cmpt_value=0;
                int somme=0;
                int value;                 // On initialise nos différentes variables pour pracourir le message
                char charac;
                int drapeau_demande=0;
                int drapeau_question=0;
                int taille_mesuree=0;
                for(cmpt=0;cmpt<taille_message-3;cmpt++){ // on parcourt le message jusqu'au '_'
                    charac = message[cmpt];
                    value = message[cmpt];
                    System.out.println(value);
                    if (charac == ':'){
                        nom_variable[cmpt]='\0';
                        drapeau_demande =1;
                        cmpt_value = cmpt+1;
                    }
                    if (charac =='?'){
                        nom_variable[cmpt]='\0';
                        drapeau_question=1;
                    }
                    if (drapeau_question ==0 && drapeau_demande == 0){
                        nom_variable[cmpt]=charac; //on consrtuit le nom de la variable
                        taille_mesuree += 1;
                        if(taille_mesuree>taille_variable){
                            return 101;
                        }
                    }
                    somme += value;
                    charac = message[cmpt];
                }
                char [] valeur_reelle_beta;
                valeur_reelle_beta = hexa_convertor(somme);
                char [] valeur_reelle = new char[2];
                valeur_reelle[0]=valeur_reelle_beta[0]; //On calcule la somme en hexadecimal
                valeur_reelle[1]=valeur_reelle_beta[1];
                //valeur_reelle[2]='\0';
                
                int egalite = 0;  
            
                char [] fin_message = new char[2];
                for(int k = taille_message-2; k < taille_message; k++){
                    fin_message[k-(taille_message-2)] = message[k];
                    if (valeur_reelle_beta[k-(taille_message-2)] == fin_message[k-(taille_message-2)]){
                        egalite ++;      
                    }
                }    
                if(egalite == 2){ //teste erreur 10
                    System.out.println(9999);
                    int type;
                    if ((drapeau_demande == 1) && (drapeau_question == 0)){
                        type=1;
                        if(type_message != 0) //test erreur 107
                        {
                            type_message=type; // type_message recoit sa valeur 1
                        }
                        else
                        {
                            return 107;
                        }
                        type_message=type;
                        if(valeur != 0){ // test erreur 101
                    
                            char [] pend = new char[2];
                            for(int n = taille_message-3; n < taille_message; n++){
                                pend[n-(taille_message-3)] = message[n];
                                if (pend[0] == '_' && (valeur_reelle_beta[n-(taille_message-1)] == pend[1])){
                                    egalite ++;      
                                }
                            }  
                            //valeur=strtof(message + cmpt_value,&Pend); // on recupère la valeur et on la transforme en float
                            if(egalite == 1){ //teste erreur 10
                                System.out.println(9999);
                                return 0;
                            }
                            else
                            {
                                return 108;
                            } 
                        }
                        else
                        {
                            return 106;
                        }
                    }
                    else if (drapeau_question ==1 && ( drapeau_demande==0))
                    {
                        type=0;
                        if(type_message != 0)
                        {
                            type_message=type;
                            return 0; 
                        }
                        else
                        {
                            return 107;
                        }
                    }
                    else
                    {
                        return 105;
                    }
                }
                else
                {
                    return 104;
                }
            }
            else
            {
                return 103;
            }
        }
        else
        {
            return 102;
        }
    }
    
}

