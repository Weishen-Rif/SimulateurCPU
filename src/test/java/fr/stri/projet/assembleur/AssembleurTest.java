package fr.stri.projet.assembleur;
import fr.stri.projet.memoire.Memoire;
// Tu pourras décommenter les deux lignes suivantes si tu fais des tests avancés plus tard
// import fr.stri.projet.cpu.Processeur;
// import fr.stri.projet.memoire.Registres;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AssembleurTest {

    @Test
    public void testTraductionLoadStore() {
        Memoire mem = new Memoire();
        Assembleur assembleur = new Assembleur();

        // 1. Soit : le code texte de l'utilisateur (avec saut de ligne)
        String code = "load r2, @100\nstore r0, @101\nbreak";

        // 2. Quand : on lance la traduction
        assembleur.traduire(code, mem);

        // 3. Alors : on vérifie la mémoire
        // "load r2, @100" -> doit donner "1, 2, 0, 100" [7]
        assertEquals(1, mem.lire(0));
        assertEquals(2, mem.lire(1));
        assertEquals(0, mem.lire(2));
        assertEquals(100, mem.lire(3));

        // "break" -> doit donner "-1"
        assertEquals(-1, mem.lire(8));
    }
}