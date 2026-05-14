package fr.stri.projet.cpu;
import fr.stri.projet.cpu.Processeur;
import fr.stri.projet.memoire.Memoire;
import fr.stri.projet.memoire.Registres;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe dédiée au test du Processeur et de ses instructions de base.
 */
public class ProcesseurTest {

    private Memoire memoire;
    private Registres registres;
    private Processeur cpu;

    /**
     * Initialise un environnement propre avant CHAQUE test.
     */
    @Before
    public void setUp() {
        memoire = new Memoire();
        registres = new Registres();
        cpu = new Processeur(memoire, registres);
    }

    /**
     * Teste l'instruction LOAD Constante.
     */
    @Test
    public void testLoadConstante() {
        // 1. SOIT : On écrit manuellement notre "programme" dans la RAM
        memoire.ecrire(0, (byte) 0);  // Opcode : 0 (LOAD Constante)
        memoire.ecrire(1, (byte) 2);  // Paramètre 1 : Registre r2
        memoire.ecrire(2, (byte) 15); // Paramètre 2 : Valeur 15
        memoire.ecrire(3, (byte) -1); // Opcode : 255/-1 (BREAK pour arrêter)
        // 2. QUAND : On exécute le processeur
        cpu.demarrer();
        // 3. ALORS : On vérifie que r2 contient bien 15
        assertEquals("Le registre r2 doit contenir la valeur 15", 15, registres.lire(2));
    }

    /**
     * Teste l'instruction STORE Mémoire.
     */
    @Test
    public void testStoreMemoire() {
        // 1. SOIT : On initialise les registres et la RAM
        registres.ecrire(5, (byte) 42);

        memoire.ecrire(0, (byte) 2);   // Opcode : STORE
        memoire.ecrire(1, (byte) 5);   // Registre r5
        memoire.ecrire(2, (byte) 0);   // Adresse poids fort (0)
        memoire.ecrire(3, (byte) 100); // Adresse poids faible (100)
        memoire.ecrire(4, (byte) -1);  // BREAK

        // 2. QUAND : On lance le CPU
        cpu.demarrer();

        // 3. ALORS : Vérification automatique
        assertEquals("La mémoire à l'adresse 100 doit contenir la valeur 42", 42, memoire.lire(100));
    }
}