package fr.stri.projet.cpu;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ALUTest {

    private ALU alu;

    @Before
    public void setUp() {
        alu = new ALU();
    }

    @Test
    public void testAdditionner() {
        assertEquals("5 + 3 doit faire 8", 8, alu.additionner((byte) 5, (byte) 3));
    }

    @Test
    public void testMultiplier() {
        assertEquals("10 * 20 doit faire 200", 200, alu.multiplier((byte) 10, (byte) 20));
    }

    @Test
    public void testDiviser() {
        byte[] resultat = alu.diviser((byte) 10, (byte) 3);

        // On cible la case numéro 0 pour vérifier le quotient (il manquait le [0] ici)
        assertEquals("Le quotient de 10/3 doit être 3", 3, resultat[0]);

        // On cible la case numéro 1 pour vérifier le reste
        assertEquals("Le reste de 10/3 doit être 1", 1, resultat[1]);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivisionParZero() {
        // Vérifie que l'ALU capture bien l'erreur si on divise par zéro
        alu.diviser((byte) 5, (byte) 0);
    }
}