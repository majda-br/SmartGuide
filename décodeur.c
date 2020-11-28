#include <stdio.h>
#include <string.h>
#include <stdlib.h>
char*  hexa_convertor(int decimal) //fonction nécéssaire au fonctionnement de décodeur
{
    static char hexadecimal[3];
    int decimal_divise=decimal%256;
    static char* valeurs= "0123456789ABCDEF";
    unsigned char dizaine = ((unsigned char) decimal_divise ) >> 4 ;
    unsigned char unite = decimal_divise & 0x0F;

    hexadecimal[2]='\0';
    hexadecimal[0]=valeurs[dizaine];
    hexadecimal[1]=valeurs[unite];
    return hexadecimal;

}
int decodeur(char * message, int taille_message, int * type_message,char* nom_variable,int taille_variable, float* valeur)
{
    //Decode un "message" dont la taille "taille_message".
    //"type_message" recoit le type de requete qui été identifié, question (=0), modification (=0)
    //"nom_variable" recoit le nom de la variable concernée par la requete
    //"taille_variable" est la taille allouée a l'espace ou est stocker "nom_variable"
    // "Valeur" recoit la valeur demandée par la requete en cas de demande de modification
    //Les erreurs possibles sont:
    //101 --> la taille de la variable renseignée dans la requete est trop grande par rapport a "taille variable"
    //102 --> la requete est trop courte pour etre valide
    //103 --> requete non valide, pas de '_' détécté
    //104 --> la somme de controle est fausse, requete invalide  
    //105 --> la requete n'est d'aucun type, requete invalide
    //106 --> valeur = NULL
    //107 --> type_message = NULL
    //108 --> le format de la valeur donnée est mauvais, requete invalide
 
    if(taille_message > 3)   //test pour l'erreur 102
    {
        if(message[taille_message-3]=='_')   //test pour l'erreur 103
        {
            int compteur =0;
            int compteur_value=0;
            int somme=0;
            int value;                 // On initialise nos différentes variables pour pracourir le message
            char charac;
            int drapeau_demande=0;
            int drapeau_question=0;
            int taille_mesuree=0;
            for(compteur=0;compteur<taille_message-3;compteur++) // on parcourt le message jusqu'au '_'
            {

                charac=message[compteur];
                value=message[compteur];
                if(charac ==':')   // on cherche le symbole indicateurs du type
                {
                    nom_variable[compteur]='\0'; //on termine le nom de la variable
                    drapeau_demande=1;  // on met a jour l'indicateur de recherche
                    compteur_value=compteur+1; //on indique le lieu de la valeur
                }
                if(charac =='?')
                {
                    nom_variable[compteur]='\0';
                    drapeau_question=1;
                }
                if(drapeau_question == 0 && drapeau_demande == 0) // tant qu'on a pas passer l'indicateur de type
                {
                    nom_variable[compteur]=charac; //on consrtuit le nom de la variable
                    taille_mesuree++;
                    if(taille_mesuree>taille_variable) // on teste l'erreur 101
                    {
                        return 101;
                    }
                }
                somme+= value;
                charac = message[compteur];
            }
            char* valeur_relle_beta= hexa_convertor(somme);
            char valeur_relle[3];
            valeur_relle[0]=valeur_relle_beta[0]; //On calcule la somme en hexadecimal
            valeur_relle[1]=valeur_relle_beta[1];
            valeur_relle[2]='\0';
            if(strcmp(valeur_relle,message + taille_message -2)==0) //teste erreur 104
            {
                int type;
                if(drapeau_demande && (! drapeau_question)) // on identifie le type de requete
                {
                    type=1;
                    if(type_message != NULL) //test erreur 107
                    {
                       *type_message=type; // type_message recoit sa valeur 1
                    }
                    else
                    {
                        return 107;
                    }
                    *type_message=type;
                    if(valeur != NULL) // test erreur 106
                    {
                        char *Pend;
                        *valeur=strtof(message + compteur_value,&Pend); // on recupère la valeur et on la transforme en float
                        if(Pend[0]=='_' && strcmp(Pend+1,message + taille_message -2)==0) //test erreur 108
                        {
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
                else if (drapeau_question && (! drapeau_demande))
                {
                    type=0;
                    if(type_message != NULL)
                    {
                       *type_message=type;
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
int main(void)
{
    return 0;
}