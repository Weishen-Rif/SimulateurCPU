package fr.stri.projet.cpu;

/**
 * Classe ALU (Unité Arithmétique et Logique).
 * Gère toutes les opérations mathématiques et logiques du processeur.
 */
public class ALU {

    /**
     * Constructeur par défaut.
     */
    public ALU() {
    }

    /**
     * Additionne deux valeurs.
     * @param valeur1 La première valeur.
     * @param valeur2 La deuxième valeur.
     * @return Le résultat de l'addition sur 8 bits.
     */
    public byte additionner(byte valeur1, byte valeur2) {
        return (byte) (valeur1 + valeur2);
    }

    /**
     * Soustrait une valeur à une autre.
     * @param valeur1 La valeur de base.
     * @param valeur2 La valeur à soustraire.
     * @return Le résultat de la soustraction sur 8 bits.
     */
    public byte soustraire(byte valeur1, byte valeur2) {
        return (byte) (valeur1 - valeur2);
    }

    /**
     * Multiplie deux valeurs.
     * @param valeur1 La première valeur.
     * @param valeur2 La deuxième valeur.
     * @return Le résultat sous forme d'entier (int) pour éviter le dépassement de capacité.
     */
    public int multiplier(byte valeur1, byte valeur2) {
        // Comme tu l'as précisé dans ton rapport de conception, on renvoie un 'int'
        // pour prévenir le dépassement de la capacité d'un octet (8 bits).
        return valeur1 * valeur2;
    }

    /**
     * Divise une valeur par une autre.
     * @param valeur1 Le dividende.
     * @param valeur2 Le diviseur.
     * @return Un tableau de 2 octets :  = quotient, [3] = reste.
     * @throws ArithmeticException Si le diviseur est égal à 0.
     */
    public byte[] diviser(byte valeur1, byte valeur2) {
        // Gestion des erreurs vue en cours : on lève une exception en cas de division par zéro
        if (valeur2 == 0) {
            throw new ArithmeticException("Erreur ALU : Division par zéro !");
        }

        // Le cahier des charges demande de séparer le quotient et le reste
        byte quotient = (byte) (valeur1 / valeur2);
        byte reste = (byte) (valeur1 % valeur2);

        return new byte[]{quotient, reste};
    }

    /**
     * Opération logique : ET binaire (AND).
     */
    public byte etBinaire(byte valeur1, byte valeur2) {
        return (byte) (valeur1 & valeur2);
    }

    /**
     * Opération logique : OU binaire (OR).
     */
    public byte ouBinaire(byte valeur1, byte valeur2) {
        return (byte) (valeur1 | valeur2);
    }

    /**
     * Opération logique : OU exclusif (XOR).
     */
    public byte ouExclusif(byte valeur1, byte valeur2) {
        return (byte) (valeur1 ^ valeur2);
    }
}