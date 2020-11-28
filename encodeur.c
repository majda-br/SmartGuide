#include <stdio.h>
#include <string.h>
#include <stdlib.h>
int somme_des_caracteres(char * chaine)
{
    //somme de caractère
    int somme = 0;
    int compteur;
    for (compteur=0;compteur < strlen(chaine);compteur++)
    {

        somme+=(int)chaine[compteur];
    }
    return somme;
}

char*  hexa_convertor(int decimal) {
    static char hexadecimal[3];
    int decimal_divise = decimal % 256;
    static char *valeurs = "0123456789ABCDEF";
    unsigned char dizaine = ((unsigned char) decimal_divise) >> 4;
    unsigned char unite = decimal_divise & 0x0F;

    hexadecimal[2] = '\0';
    hexadecimal[0] = valeurs[dizaine];
    hexadecimal[1] = valeurs[unite];
    return hexadecimal;
}

int encodeur(char* message, int taille, int type_message,char *nom_variable,float valeur) {

        if (type_message == 1) {
            //modification
            int i;
            char a[6];
            //------------Changer float ->char---------------
            gcvt(valeur, 5, a);
            int len_variable = strlen(nom_variable);
            int len_valeur = strlen(a);
            //a ='valeur'
            //-----------------------------------------------
            if (len_variable + 1 + len_valeur + 4 < taille) {
                for (i = 0; i < len_variable; i++) {
                    message[i] = nom_variable[i];
                }

                message[len_variable] = ':';
                //message = 'nom_variable:'
                for (i = len_variable + 1; i < len_variable + 1 + len_valeur; i++) {
                    message[i] = a[i - (len_variable + 1)];
                }
                message[len_variable + len_valeur + 1] = '\0';
                message[len_variable + len_valeur + 2] = '\0';
                //message = 'nom_variable:valeur'
                message[len_variable + 1 + len_valeur] = '_';
                //message = 'nom_variable:valeur_'

                char *hexa;
                int somme = somme_des_caracteres(message);
                hexa = hexa_convertor(somme);

                for (i = len_variable + 1 + len_valeur + 1; i < len_variable + 1 + len_valeur + 4; i++) {
                    message[i] = hexa[i - (len_variable + 1 + len_valeur + 1)];
                }
                //message = 'nom_variable:valeur_FF'

               printf("%s\n",message);
                //tester

                return 0;


            } else {
                //printf("ERREUR de sizeof message\n");
                return 101;
            }

        } else if (type_message == 0) {
            //consultation
            int i;
            if (strlen(nom_variable) + 5 < taille) {


                for (i = 0; i < strlen(nom_variable); i++) {
                    message[i] = nom_variable[i];
                }
                //message = 'nom_variable'
                message[strlen(nom_variable)] = '?';
                //message = 'nom_variable?'
                char *hexa;
                int somme = somme_des_caracteres(message);
                hexa = hexa_convertor(somme);
                
                message[strlen(nom_variable) + 1] = '_';
                //message = 'nom_variable?_'





                for (i = strlen(nom_variable) + 2; i < strlen(nom_variable) + 5; i++) {
                    message[i] = hexa[i - (strlen(nom_variable) + 2)];
                }
                //message = 'nom_variable?_FF'
                printf("%s\n",message);
                return 0;


            } else {
                //erreur de sizeof message
                return 101;
            }
        } else {
            //printf("Type_message 0 ou 1");
            return 102;
        }

}


int main()
{
    //fonctionnement du programme
    char message[40];
    int resultat;
    resultat = encodeur(message, sizeof(message), 1,"joy.angle",12.14);

    printf("result is %i\n", resultat);
    return 0;
}

