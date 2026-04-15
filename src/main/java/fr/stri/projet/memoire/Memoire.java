package fr.stri.projet.memoire;

/**
 * Représente la mémoire principale (RAM) du simulateur.
 * Elle contient 64 Ko de données (65536 octets).
 */
public class Memoire {

    private byte[] ram;

    /**
     * Constructeur par défaut.
     * Initialise la mémoire avec 65536 cases de type byte.
     */
    public Memoire() {
        this.ram = new byte[65536];
    }

    /**
     * Lit une valeur dans la mémoire à une adresse donnée.
     * @param adresse L'adresse mémoire à lire (entre 0 et 65535).
     * @return La valeur de type byte contenue à cette adresse.
     */
    public byte lire(int adresse) {
        return ram[adresse];
    }

    /**
     * Écrit une valeur dans la mémoire à une adresse donnée.
     * @param adresse L'adresse mémoire où écrire.
     * @param valeur La valeur de type byte à stocker.
     */
    public void ecrire(int adresse, byte valeur) {
        ram[adresse] = valeur;
    }
}