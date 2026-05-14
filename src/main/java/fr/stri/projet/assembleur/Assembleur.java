package fr.stri.projet.assembleur;

import fr.stri.projet.memoire.Memoire;

/**
 * Classe Assembleur.
 * Traduit le code source textuel lisible par l'utilisateur en instructions
 * numériques exécutables par le CPU, puis les charge en mémoire.
 */
public class Assembleur {

    /**
     * Constructeur par défaut.
     */
    public Assembleur() {
    }

    /**
     * Lit le code source complet, le traduit instruction par instruction,
     * et l'écrit séquentiellement dans la mémoire.
     *
     * @param codeSource Le programme assembleur sous forme de texte brut.
     * @param mem La mémoire (RAM) dans laquelle écrire le résultat.
     */
    public void traduire(String codeSource, Memoire mem) {
        // On sépare le texte ligne par ligne
        String[] lignes = codeSource.split("\n");
        int adresseEnCours = 0; // Le compteur d'écriture en mémoire

        // DÉBUT DE LA BOUCLE FOR : on parcourt chaque ligne
        for (String ligne : lignes) {

            // Nettoyage de la ligne (suppression des espaces inutiles)
            ligne = ligne.trim();
            if (ligne.isEmpty()) continue;

            // Découpage de la ligne par les espaces et les virgules
            String[] mots = ligne.split("[ ,]+");

            // On englobe TOUT le traitement de l'instruction dans un try/catch (à l'intérieur de la boucle)
            try {
                // On récupère le nom de l'instruction en minuscules (mot de la case 0)
                String instruction = mots[0].toLowerCase();

                switch (instruction) {
                    case "load": {
                        // Lecture du registre cible
                        int regLoad = Integer.parseInt(mots[1].replace("r", ""));

                        if (mots.length == 4) {
                            // Load indexé (ex: load r0, @100, r1)
                            mem.ecrire(adresseEnCours++, (byte) 10);
                            mem.ecrire(adresseEnCours++, (byte) regLoad);

                            // Lecture de l'adresse de base
                            int adresseBase = parseValeur(mots[2].replace("@", ""));
                            mem.ecrire(adresseEnCours++, (byte) (adresseBase >> 8));
                            mem.ecrire(adresseEnCours++, (byte) (adresseBase & 0xFF));

                            // Lecture du registre d'index
                            int regIndex = Integer.parseInt(mots[3].replace("r", ""));
                            mem.ecrire(adresseEnCours++, (byte) regIndex);

                        } else if (mots[2].startsWith("@")) {
                            // Load depuis la mémoire (Opcode 1)
                            mem.ecrire(adresseEnCours++, (byte) 1);
                            mem.ecrire(adresseEnCours++, (byte) regLoad);

                            // Lecture de l'adresse mémoire
                            int adresseMem = parseValeur(mots[2].replace("@", ""));
                            mem.ecrire(adresseEnCours++, (byte) (adresseMem >> 8));
                            mem.ecrire(adresseEnCours++, (byte) (adresseMem & 0xFF));
                        } else {
                            // Load d'une constante (Opcode 0)
                            mem.ecrire(adresseEnCours++, (byte) 0);
                            mem.ecrire(adresseEnCours++, (byte) regLoad);

                            // Lecture de la constante
                            int constante = parseValeur(mots[2]);
                            mem.ecrire(adresseEnCours++, (byte) constante);
                        }
                        break;
                    }

                    case "store": {
                        // Lecture du registre cible
                        int regStore = Integer.parseInt(mots[1].replace("r", ""));

                        if (mots.length == 4) {
                            // Store indexé (ex: store r0, @100, r1)
                            mem.ecrire(adresseEnCours++, (byte) 11);
                            mem.ecrire(adresseEnCours++, (byte) regStore);

                            int adresseBase = parseValeur(mots[2].replace("@", ""));
                            mem.ecrire(adresseEnCours++, (byte) (adresseBase >> 8));
                            mem.ecrire(adresseEnCours++, (byte) (adresseBase & 0xFF));

                            int regIndex = Integer.parseInt(mots[3].replace("r", ""));
                            mem.ecrire(adresseEnCours++, (byte) regIndex);
                        } else {
                            // Store vers la mémoire classique (Opcode 2)
                            mem.ecrire(adresseEnCours++, (byte) 2);
                            mem.ecrire(adresseEnCours++, (byte) regStore);

                            // Lecture de l'adresse de destination
                            int adresseDest = parseValeur(mots[2].replace("@", ""));
                            mem.ecrire(adresseEnCours++, (byte) (adresseDest >> 8));
                            mem.ecrire(adresseEnCours++, (byte) (adresseDest & 0xFF));
                        }
                        break;
                    }

                    case "break": {
                        mem.ecrire(adresseEnCours++, (byte) -1);
                        break;
                    }

                    case "jump": {
                        // Jump (Opcode 6)
                        mem.ecrire(adresseEnCours++, (byte) 6);

                        int adresseJump = parseValeur(mots[1].replace("@", ""));
                        mem.ecrire(adresseEnCours++, (byte) (adresseJump >> 8));
                        mem.ecrire(adresseEnCours++, (byte) (adresseJump & 0xFF));
                        break;
                    }

                    case "beq": {
                        // Beq (Opcode 7)
                        mem.ecrire(adresseEnCours++, (byte) 7);

                        int reg1Beq = Integer.parseInt(mots[1].replace("r", ""));
                        mem.ecrire(adresseEnCours++, (byte) reg1Beq);

                        int reg2Beq = Integer.parseInt(mots[2].replace("r", ""));
                        mem.ecrire(adresseEnCours++, (byte) reg2Beq);

                        int adresseBeq = parseValeur(mots[3].replace("@", ""));
                        mem.ecrire(adresseEnCours++, (byte) (adresseBeq >> 8));
                        mem.ecrire(adresseEnCours++, (byte) (adresseBeq & 0xFF));
                        break;
                    }

                    case "bne": {
                        // Bne (Opcode 8)
                        mem.ecrire(adresseEnCours++, (byte) 8);

                        int reg1Bne = Integer.parseInt(mots[1].replace("r", ""));
                        mem.ecrire(adresseEnCours++, (byte) reg1Bne);

                        int reg2Bne = Integer.parseInt(mots[2].replace("r", ""));
                        mem.ecrire(adresseEnCours++, (byte) reg2Bne);

                        int adresseBne = parseValeur(mots[3].replace("@", ""));
                        mem.ecrire(adresseEnCours++, (byte) (adresseBne >> 8));
                        mem.ecrire(adresseEnCours++, (byte) (adresseBne & 0xFF));
                        break;
                    }

                    case "add": {
                        // Add (Opcode 3)
                        mem.ecrire(adresseEnCours++, (byte) 3);
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[1].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[2].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[3].replace("r", "")));
                        break;
                    }

                    case "sub": {
                        // Sub (Opcode 4)
                        mem.ecrire(adresseEnCours++, (byte) 4);
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[1].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[2].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[3].replace("r", "")));
                        break;
                    }

                    case "div": {
                        // Div (Opcode 9)
                        mem.ecrire(adresseEnCours++, (byte) 9);
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[1].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[2].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[3].replace("r", "")));
                        mem.ecrire(adresseEnCours++, (byte) Integer.parseInt(mots[4].replace("r", "")));
                        break;
                    }

                    case "data": {
                        adresseEnCours = traiterData(ligne, adresseEnCours, mem);
                        break;
                    }

                    case "string": {
                        adresseEnCours = traiterString(ligne, adresseEnCours, mem);
                        break;
                    }

                    default: {
                        System.out.println("Erreur d'assemblage : instruction non reconnue -> " + instruction);
                        break;
                    }
                } // Fin du switch

            } catch (NumberFormatException e) {
                System.out.println("Erreur d'assemblage à la ligne '" + ligne + "' : Le paramètre saisi n'est pas valide.");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Erreur d'assemblage à la ligne '" + ligne + "' : Il manque des paramètres pour cette instruction.");
            }

        } // FIN DE LA BOUCLE FOR
    }

    /**
     * Convertit une chaîne de caractères en entier, en gérant le format hexadécimal (0x...).
     * @param val La chaîne à convertir.
     * @return La valeur sous forme d'entier.
     */
    private int parseValeur(String val) {
        if (val.startsWith("0x")) {
            return Integer.parseInt(val.substring(2), 16);
        }
        return Integer.parseInt(val);
    }

    // --- Méthodes prévues pour l'Étape 5 ---

    /**
     * Traite la directive data pour stocker une suite de valeurs brutes en mémoire.
     */
    private int traiterData(String ligne, int adresseEnCours, Memoire mem) {
        // On récupère tout ce qui se trouve après le mot "data "
        String valeurs = ligne.substring(5).trim();
        String[] elements = valeurs.split(","); // On découpe par les virgules

        for (String val : elements) {
            int entier = parseValeur(val.trim());
            mem.ecrire(adresseEnCours++, (byte) entier);
        }
        return adresseEnCours;
    }

    /**
     * Traite la directive string pour stocker une chaîne de caractères (UTF-8) en mémoire.
     */
    private int traiterString(String ligne, int adresseEnCours, Memoire mem) {
        int debut = ligne.indexOf('"');
        int fin = ligne.lastIndexOf('"');

        // On vérifie que la phrase contient bien des guillemets
        if (debut != -1 && fin != -1 && debut < fin) {
            String texte = ligne.substring(debut + 1, fin);

            // Encodage en UTF-8
            byte[] octets = texte.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            for (byte b : octets) {
                mem.ecrire(adresseEnCours++, b);
            }
        }
        return adresseEnCours;
    }
}