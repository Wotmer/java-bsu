package lab1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testElectronicProductCreation() {
        ElectronicProduct product = new ElectronicProduct("E1", "Laptop", 120);

        assertEquals("E1", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(120, product.getProductionTime());
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    void testMechanicalProductCreation() {
        MechanicalProduct product = new MechanicalProduct("M1", "Gear", 45);

        assertEquals("M1", product.getId());
        assertEquals("Gear", product.getName());
        assertEquals(45, product.getProductionTime());
        assertEquals("Mechanical", product.getCategory());
    }

    @Test
    void testChemicalProductCreation() {
        ChemicalProduct product = new ChemicalProduct("C1", "Paint", 90);

        assertEquals("C1", product.getId());
        assertEquals("Paint", product.getName());
        assertEquals(90, product.getProductionTime());
        assertEquals("Chemical", product.getCategory());
    }

    @Test
    void testProductToString() {
        ElectronicProduct product = new ElectronicProduct("E1", "Laptop", 120);
        String toString = product.toString();

        assertTrue(toString.contains("E1"));
        assertTrue(toString.contains("Laptop"));
        assertTrue(toString.contains("120"));
    }
}