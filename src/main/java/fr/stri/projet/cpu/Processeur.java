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
    private ALU alu; // NOUVEAU : Le processeur possède une ALU


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
        this.alu = new ALU(); // NOUVEAU : Initialisation de l'ALU
        this.pc = 0;
        this.enExecution = true;
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
            case 0: // LOAD Constante (ex: 0 0 5 -> load r0, 5)
                int regLoadConst = memoire.lire(pc++);
                byte constante = memoire.lire(pc++);
                registres.ecrire(regLoadConst, constante);
                break;

            case 2: // STORE Mémoire (ex: 2 0 0 101 -> store r0, @101)
                // 1. On lit le numéro du registre à sauvegarder
                int regStore = memoire.lire(pc++);

                // 2. On lit les deux octets de l'adresse mémoire de destination
                byte poidsFortStore = memoire.lire(pc++);
                byte poidsFaibleStore = memoire.lire(pc++);

                // 3. On reconstitue l'adresse 16 bits grâce à ta méthode
                int adresseStore = reconstituerAdresse(poidsFortStore, poidsFaibleStore);

                // 4. On récupère la valeur actuelle du registre
                byte valeurAStocker = registres.lire(regStore);

                // 5. On écrit cette valeur dans la mémoire RAM
                memoire.ecrire(adresseStore, valeurAStocker);
                break;

            case 3: // ADD : Addition (Opcode 3)
                // L'assembleur nous enverra 3 registres : Destination, Source 1, Source 2
                int regDestAdd = memoire.lire(pc++);
                byte val1Add = registres.lire(memoire.lire(pc++));
                byte val2Add = registres.lire(memoire.lire(pc++));
                // On délègue le calcul à l'ALU et on stocke le résultat
                registres.ecrire(regDestAdd, alu.additionner(val1Add, val2Add));
                break;

            case 4: // SUB : Soustraction (Opcode 4)
                int regDestSub = memoire.lire(pc++);
                byte val1Sub = registres.lire(memoire.lire(pc++));
                byte val2Sub = registres.lire(memoire.lire(pc++));
                registres.ecrire(regDestSub, alu.soustraire(val1Sub, val2Sub));
                break;

            case 9: // DIV : Division (Opcode 9)
                // Le cahier des charges demande de gérer Quotient ET Reste
                int regQuotient = memoire.lire(pc++);
                int regReste = memoire.lire(pc++);
                byte val1Div = registres.lire(memoire.lire(pc++));
                byte val2Div = registres.lire(memoire.lire(pc++));

                // L'ALU renvoie un tableau avec  = quotient et [1] = reste
                byte[] resultatDiv = alu.diviser(val1Div, val2Div);
                registres.ecrire(regQuotient, resultatDiv[0]);
                registres.ecrire(regReste, resultatDiv[1]);
                break;

            case -1: // BREAK (Code 255)
                // Arrête proprement la boucle du processeur
                this.enExecution = false;
                break;

            default:
                // Bonne pratique : avertir si une instruction inconnue est lue
                System.out.println("Erreur : Instruction inconnue (" + opcode + ") à l'adresse " + (pc - 1));
                this.enExecution = false;
                break;
            case 6: // JUMP : Saut inconditionnel (Opcode 6)
                // Lecture de l'adresse sur 16 bits (2 octets)
                byte poidsFortJump = memoire.lire(pc++);
                byte poidsFaibleJump = memoire.lire(pc++);
                int adresseJump = reconstituerAdresse(poidsFortJump, poidsFaibleJump);
                sauter(adresseJump);
                break;

            case 7: // BEQ : Branch if EQual (Opcode 7)
                // Lecture des deux registres à comparer
                byte val1Eq = registres.lire(memoire.lire(pc++));
                byte val2Eq = registres.lire(memoire.lire(pc++));
                // Lecture de l'adresse de destination
                byte poidsFortBeq = memoire.lire(pc++);
                byte poidsFaibleBeq = memoire.lire(pc++);
                int adresseBeq = reconstituerAdresse(poidsFortBeq, poidsFaibleBeq);
                brancherSiEgal(val1Eq, val2Eq, adresseBeq);
                break;

            case 8: // BNE : Branch if Not Equal (Opcode 8)
                // Lecture des deux registres à comparer
                byte val1Neq = registres.lire(memoire.lire(pc++));
                byte val2Neq = registres.lire(memoire.lire(pc++));
                // Lecture de l'adresse de destination
                byte poidsFortBne = memoire.lire(pc++);
                byte poidsFaibleBne = memoire.lire(pc++);
                int adresseBne = reconstituerAdresse(poidsFortBne, poidsFaibleBne);
                brancherSiDifferent(val1Neq, val2Neq, adresseBne);
                break;

            case 10: // LOAD Indexé (Opcode 10) -> load rDest, @AdresseBase, rIndex
                int regLoadIdx = memoire.lire(pc++); // Registre de destination
                int baseAddrLoad = reconstituerAdresse(memoire.lire(pc++), memoire.lire(pc++)); // Adresse de base
                int regIndexLoad = memoire.lire(pc++); // Registre d'index

                // On additionne l'adresse de base et la valeur contenue dans le registre d'index
                int offsetLoad = registres.lire(regIndexLoad);
                byte valLue = memoire.lire(baseAddrLoad + offsetLoad);
                registres.ecrire(regLoadIdx, valLue);
                break;

            case 11: // STORE Indexé (Opcode 11) -> store rSource, @AdresseBase, rIndex
                int regStoreIdx = memoire.lire(pc++); // Registre source
                int baseAddrStore = reconstituerAdresse(memoire.lire(pc++), memoire.lire(pc++));
                int regIndexStore = memoire.lire(pc++);

                int offsetStore = registres.lire(regIndexStore);
                byte valAEcrire = registres.lire(regStoreIdx);
                // On écrit à l'adresse décalée
                memoire.ecrire(baseAddrStore + offsetStore, valAEcrire);
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

    /**
     * Effectue un saut inconditionnel vers une adresse mémoire spécifiée.
     * @param adresse L'adresse de destination (sur 16 bits).
     */
    private void sauter(int adresse) {
        // En modifiant directement le PC, la prochaine instruction lue
        // par la boucle while sera à cette nouvelle adresse !
        this.pc = adresse;
    }

    /**
     * Effectue un saut conditionnel si les deux valeurs sont égales (BEQ).
     * @param val1 La valeur du premier registre.
     * @param val2 La valeur du deuxième registre.
     * @param adresse L'adresse de destination si la condition est vérifiée.
     */
    private void brancherSiEgal(byte val1, byte val2, int adresse) {
        if (val1 == val2) {
            this.pc = adresse;
        }
    }

    /**
     * Effectue un saut conditionnel si les deux valeurs sont différentes (BNE).
     * @param val1 La valeur du premier registre.
     * @param val2 La valeur du deuxième registre.
     * @param adresse L'adresse de destination si la condition est vérifiée.
     */
    private void brancherSiDifferent(byte val1, byte val2, int adresse) {
        if (val1 != val2) {
            this.pc = adresse;
        }
    }
}