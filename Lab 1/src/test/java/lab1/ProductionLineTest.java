package lab1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLineTest {

    @Test
    void testElectronicsLineCreation() {
        ElectronicsLine line = new ElectronicsLine("EL1", 0.85);

        assertEquals("EL1", line.getLineId());
        assertEquals(0.85, line.getEfficiency(), 0.001);
        assertTrue(line.getProducts().isEmpty());
    }

    @Test
    void testMechanicalLineCreation() {
        MechanicalLine line = new MechanicalLine("ML1", 0.75);

        assertEquals("ML1", line.getLineId());
        assertEquals(0.75, line.getEfficiency(), 0.001);
        assertTrue(line.getProducts().isEmpty());
    }

    @Test
    void testChemicalLineCreation() {
        ChemicalLine line = new ChemicalLine("CL1", 0.60);

        assertEquals("CL1", line.getLineId());
        assertEquals(0.60, line.getEfficiency(), 0.001);
        assertTrue(line.getProducts().isEmpty());
    }

    @Test
    void testCanProduceCompatibleProduct() {
        ElectronicsLine eLine = new ElectronicsLine("EL1", 0.85);
        ElectronicProduct eProduct = new ElectronicProduct("E1", "Phone", 60);

        assertTrue(eLine.canProduce(eProduct));
    }

    @Test
    void testCannotProduceIncompatibleProduct() {
        ElectronicsLine eLine = new ElectronicsLine("EL1", 0.85);
        MechanicalProduct mProduct = new MechanicalProduct("M1", "Bolt", 10);

        assertFalse(eLine.canProduce(mProduct));
    }

    @Test
    void testAddProductToLine() {
        ElectronicsLine line = new ElectronicsLine("EL1", 0.85);
        ElectronicProduct product1 = new ElectronicProduct("E1", "Phone", 60);
        ElectronicProduct product2 = new ElectronicProduct("E2", "Tablet", 45);

        line.addProduct(product1);
        line.addProduct(product2);

        assertEquals(2, line.getProducts().size());
        assertTrue(line.getProducts().contains(product1));
        assertTrue(line.getProducts().contains(product2));
    }

    @Test
    void testAddIncompatibleProductThrowsException() {
        ElectronicsLine eLine = new ElectronicsLine("EL1", 0.85);
        MechanicalProduct mProduct = new MechanicalProduct("M1", "Bolt", 10);

        assertFalse(eLine.canProduce(mProduct));
    }

    @Test
    void testLineToString() {
        ElectronicsLine line = new ElectronicsLine("EL1", 0.85);
        String toString = line.toString();

        assertTrue(toString.contains("EL1"));
        assertTrue(toString.contains("0.85") || toString.contains("0,85"));
    }
}