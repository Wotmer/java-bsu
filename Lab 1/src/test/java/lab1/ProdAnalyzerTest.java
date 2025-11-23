package lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdAnalyzerTest {

    private ProdAnalyzer analyzer;
    private ElectronicsLine eLine;
    private MechanicalLine mLine;
    private ChemicalLine cLine;

    @BeforeEach
    void setUp() {
        analyzer = new ProdAnalyzer();

        eLine = new ElectronicsLine("E-01", 0.92);
        mLine = new MechanicalLine("M-01", 0.78);
        cLine = new ChemicalLine("C-01", 0.55);

        eLine.addProduct(new ElectronicProduct("EP-001", "Resistor", 10));
        eLine.addProduct(new ElectronicProduct("EP-002", "Capacitor", 8));

        mLine.addProduct(new MechanicalProduct("MP-001", "Bolt", 5));

        cLine.addProduct(new ChemicalProduct("CP-001", "Adhesive", 20));

        analyzer.addLine(eLine);
        analyzer.addLine(mLine);
        analyzer.addLine(cLine);
    }

    @Test
    void testGetAllLines() {
        List<ProductionLine<? extends Product>> lines = analyzer.getAllLines();

        assertEquals(3, lines.size());
        assertTrue(lines.contains(eLine));
        assertTrue(lines.contains(mLine));
        assertTrue(lines.contains(cLine));
    }

    @Test
    void testGetHighEfficiencyLines() {
        List<String> highEffLines = analyzer.getHighEfficiencyLines(0.8);

        assertEquals(1, highEffLines.size());
        assertEquals("E-01", highEffLines.get(0));

        List<String> mediumEffLines = analyzer.getHighEfficiencyLines(0.5);
        assertEquals(3, mediumEffLines.size());
    }

    @Test
    void testGetHighEfficiencyLinesWithNoMatches() {
        List<String> veryHighEffLines = analyzer.getHighEfficiencyLines(0.95);

        assertTrue(veryHighEffLines.isEmpty());
    }

    @Test
    void testCountProductsByCategory() {
        Map<String, Long> countByCategory = analyzer.countProductsByCategory();

        assertEquals(3, countByCategory.size());
        assertEquals(2, countByCategory.get("Electronics"));
        assertEquals(1, countByCategory.get("Mechanical"));
        assertEquals(1, countByCategory.get("Chemical"));
    }

    @Test
    void testFindMostLoadedLine() {
        Optional<ProductionLine<? extends Product>> mostLoaded = analyzer.findMostLoadedLine();

        assertTrue(mostLoaded.isPresent());
        assertEquals(eLine, mostLoaded.get());
        assertEquals(2, mostLoaded.get().getProducts().size());
    }

    @Test
    void testFindMostLoadedLineWithEmptyAnalyzer() {
        ProdAnalyzer emptyAnalyzer = new ProdAnalyzer();
        Optional<ProductionLine<? extends Product>> mostLoaded = emptyAnalyzer.findMostLoadedLine();

        assertFalse(mostLoaded.isPresent());
    }

    @Test
    void testGetAllProductsFromLines() {
        List<Product> allProducts = analyzer.getAllProductsFromLines();

        assertEquals(4, allProducts.size());

        long electronicCount = allProducts.stream()
                .filter(p -> p instanceof ElectronicProduct)
                .count();
        long mechanicalCount = allProducts.stream()
                .filter(p -> p instanceof MechanicalProduct)
                .count();
        long chemicalCount = allProducts.stream()
                .filter(p -> p instanceof ChemicalProduct)
                .count();

        assertEquals(2, electronicCount);
        assertEquals(1, mechanicalCount);
        assertEquals(1, chemicalCount);
    }

    @Test
    void testCalculateTotalProductionTime() {
        double totalTime = analyzer.calculateTotalProductionTime();

        assertEquals(43.0, totalTime, 0.001);
    }

    @Test
    void testCalculateTotalProductionTimeWithEmptyAnalyzer() {
        ProdAnalyzer emptyAnalyzer = new ProdAnalyzer();
        double totalTime = emptyAnalyzer.calculateTotalProductionTime();

        assertEquals(0.0, totalTime, 0.001);
    }

    @Test
    void testAddLineToAnalyzer() {
        ProdAnalyzer newAnalyzer = new ProdAnalyzer();
        assertEquals(0, newAnalyzer.getAllLines().size());

        newAnalyzer.addLine(eLine);
        assertEquals(1, newAnalyzer.getAllLines().size());

        newAnalyzer.addLine(mLine);
        assertEquals(2, newAnalyzer.getAllLines().size());
    }
}