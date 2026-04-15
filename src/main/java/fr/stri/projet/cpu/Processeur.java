package fr.stri.projet.cpu;

import fr.stri.projet.memoire.Memoire;
import fr.stri.projet.memoire.Registres;

/**
 * Représente l'Unité Centrale de Traitement (CPU).
 * Gère le cycle d'exécution des instructions à l'aide d'un compteur de programme.
 */
public class Processeur {

    private Memoire memoire;
    private Registres registres;

    /**
     * Le registre d'adresse (Compteur de programme).
     * Il utilise un int (32 bits) pour pouvoir adresser positivement les 64 Ko.
     */
    private int pc;

    private boolean enExecution;

    /**
     * Constructeur du processeur.
     * @param memoire La mémoire RAM associée au processeur.
     * @param registres Les registres associés au processeur.
     */
    public Processeur(Memoire memoire, Registres registres) {
        this.memoire = memoire;
        this.registres = registres;
        this.pc = 0; // S'initialise à 0 au début [6]
        this.enExecution = false;
    }

    /**
     * Lance le cycle d'exécution du processeur.
     * Lit et exécute les instructions en mémoire jusqu'à rencontrer un Break.
     */
    public void demarrer() {
        this.enExecution = true;

        while (enExecution) {
            // Lecture du code de l'instruction pointée par le PC
            byte opcode = memoire.lire(pc);
            pc++; // On avance le compteur après lecture de l'instruction [9]

            executerInstruction(opcode);
        }
    }

    /**
     * Décode et exécute une instruction spécifique.
     * @param opcode Le code de l'instruction à exécuter.
     */
    private void executerInstruction(byte opcode) {
        switch (opcode) {
            case 0:
                // LOAD Constante (ex: 0 0 5 -> load r0, 5)
                int regLoadConst = memoire.lire(pc++);
                byte constante = memoire.lire(pc++);
                registres.ecrire(regLoadConst, constante);
                break;

            case 1:
                // LOAD Mémoire (ex: 1 2 0 100 -> load r2, @100)
                int regLoadMem = memoire.lire(pc++);
                int adresseLoad = reconstituerAdresse(memoire.lire(pc++), memoire.lire(pc++));
                byte valeurLue = memoire.lire(adresseLoad);
                registres.ecrire(regLoadMem, valeurLue);
                break;

            case 2:
                // STORE Mémoire (ex: 2 0 0 101 -> store r0, @101)
                int regStore = memoire.lire(pc++);
                int adresseStore = reconstituerAdresse(memoire.lire(pc++), memoire.lire(pc++));
                byte valeurAStocker = registres.lire(regStore);
                memoire.ecrire(adresseStore, valeurAStocker);
                break;

            case -1: // Code arbitraire pour le BREAK (255 en octet non-signé)
                // Arrête l'exécution du programme [6]
                enExecution = false;
                break;

            default:
                // Comportement de sécurité si une instruction inconnue est lue
                System.out.println("Erreur : Instruction non reconnue à l'adresse " + (pc - 1));
                enExecution = false;
                break;
        }
    }

    /**
     * Reconstitue une adresse 16 bits à partir de deux octets (poids fort et poids faible).
     * En Java, les bytes sont signés. Le masque & 0xFF permet de les traiter comme non-signés.
     * @param poidsFort L'octet de poids fort.
     * @param poidsFaible L'octet de poids faible.
     * @return L'adresse reconstituée sous forme d'entier (int).
     */
    private int reconstituerAdresse(byte poidsFort, byte poidsFaible) {
        int fort = poidsFort & 0xFF;
        int faible = poidsFaible & 0xFF;
        return (fort << 8) | faible;
    }
}