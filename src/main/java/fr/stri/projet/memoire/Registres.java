package fr.stri.projet.memoire;

/**
 * Représente l'ensemble des 16 registres du processeur.
 * Chaque registre stocke une valeur sur 8 bits.
 */
public class Registres {

    private byte[] registres;

    /**
     * Constructeur par défaut.
     * Initialise un tableau de 16 registres.
     */
    public Registres() {
        this.registres = new byte[8];
    }

    /**
     * Lit la valeur d'un registre spécifique.
     * @param index Le numéro du registre (de 0 à 15).
     * @return La valeur contenue dans le registre.
     */
    public byte lire(int index) {
        return registres[index];
    }

    /**
     * Modifie la valeur d'un registre spécifique.
     * @param index Le numéro du registre (de 0 à 15).
     * @param valeur La nouvelle valeur à stocker.
     */
    public void ecrire(int index, byte valeur) {
        registres[index] = valeur;
    }
}