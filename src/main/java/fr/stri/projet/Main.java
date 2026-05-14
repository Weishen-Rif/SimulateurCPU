package fr.stri.projet;

import fr.stri.projet.memoire.Memoire;
import fr.stri.projet.memoire.Registres;
import fr.stri.projet.cpu.Processeur;
import fr.stri.projet.assembleur.Assembleur;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. On "branche" tes composants matériels ensemble
        Memoire memoire = new Memoire();
        Registres registres = new Registres();
        Processeur cpu = new Processeur(memoire, registres);
        Assembleur assembleur = new Assembleur();

        // 2. On prépare la lecture au clavier (comme vu en cours)
        Scanner clavier = new Scanner(System.in);

        System.out.println("=== BIENVENUE SUR LE SIMULATEUR CPU ===");
        System.out.println("Saisissez votre programme (tapez 'break' sur une nouvelle ligne pour lancer l'exécution) :");

        StringBuilder programmeComplet = new StringBuilder();

        // 1. On accumule le texte tapé ligne par ligne
        while (true) {
            String ligne = clavier.nextLine();
            programmeComplet.append(ligne).append("\n"); // On ajoute un saut de ligne

            if (ligne.equalsIgnoreCase("break")) {
                break; // On sort de la boucle de saisie
            }
        }

        // 2. On traduit le programme ENTIER en une seule fois !
        assembleur.traduire(programmeComplet.toString(), memoire);

        System.out.println("Démarrage du processeur...");
        cpu.demarrer();

        // AFFICHAGE DES RÉSULTATS :
        System.out.println("=== ÉTAT FINAL DES 16 REGISTRES ===");

        // Registres utilisés par notre algorithme
        System.out.println("Registre r0 (Somme totale calculée) : " + registres.lire(0));
        System.out.println("Registre r1 (Index final de la boucle) : " + registres.lire(1));
        System.out.println("Registre r2 (Limite du tableau) : " + registres.lire(2));
        System.out.println("Registre r3 (Dernière valeur lue du tableau) : " + registres.lire(3));
        System.out.println("Registre r4 (Constante d'incrément) : " + registres.lire(4));

        // Registres non sollicités par ce scénario
        System.out.println("Registre r5 (Non utilisé) : " + registres.lire(5));
        System.out.println("Registre r6 (Non utilisé) : " + registres.lire(6));
        System.out.println("Registre r7 (Non utilisé) : " + registres.lire(7));
        System.out.println("Registre r8 (Non utilisé) : " + registres.lire(8));
        System.out.println("Registre r9 (Non utilisé) : " + registres.lire(9));
        System.out.println("Registre r10 (Non utilisé) : " + registres.lire(10));
        System.out.println("Registre r11 (Non utilisé) : " + registres.lire(11));
        System.out.println("Registre r12 (Non utilisé) : " + registres.lire(12));
        System.out.println("Registre r13 (Non utilisé) : " + registres.lire(13));
        System.out.println("Registre r14 (Non utilisé) : " + registres.lire(14));
        System.out.println("Registre r15 (Non utilisé) : " + registres.lire(15));

        System.out.println("===================================");
        System.out.println("Case mémoire 100 (Sauvegarde finale) : " + memoire.lire(100));
    }
}