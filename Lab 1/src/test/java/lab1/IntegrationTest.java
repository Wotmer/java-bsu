package lab1;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    void testCompleteWorkflow() {
        ProdAnalyzer analyzer = new ProdAnalyzer();

        ElectronicsLine eLine = new ElectronicsLine("E-01", 0.92);
        MechanicalLine mLine = new MechanicalLine("M-01", 0.78);

        eLine.addProduct(new ElectronicProduct("E1", "Smartphone", 120));
        eLine.addProduct(new ElectronicProduct("E2", "Tablet", 90));

        mLine.addProduct(new MechanicalProduct("M1", "Engine", 300));

        analyzer.addLine(eLine);
        analyzer.addLine(mLine);

        assertEquals(1, analyzer.getHighEfficiencyLines(0.9).size());

        Map<String, Long> counts = analyzer.countProductsByCategory();
        assertEquals(2, counts.get("Electronics"));
        assertEquals(1, counts.get("Mechanical"));

        assertEquals(eLine, analyzer.findMostLoadedLine().get());

        assertEquals(510.0, analyzer.calculateTotalProductionTime(), 0.001);
    }

    @Test
    void testEdgeCases() {
        ProdAnalyzer analyzer = new ProdAnalyzer();

        assertTrue(analyzer.getHighEfficiencyLines(0.5).isEmpty());
        assertTrue(analyzer.findMostLoadedLine().isEmpty());
        assertEquals(0.0, analyzer.calculateTotalProductionTime(), 0.001);

        Map<String, Long> emptyCounts = analyzer.countProductsByCategory();
        assertTrue(emptyCounts.isEmpty());
    }
}