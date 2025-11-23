package lab1;

public class Main {
    public static void main(String[] args) {
        ElectronicsLine e1 = new ElectronicsLine("E-01", 0.92);
        MechanicalLine m1 = new MechanicalLine("M-01", 0.78);
        ChemicalLine c1 = new ChemicalLine("C-01", 0.55);

        ElectronicProduct ep1 = new ElectronicProduct("EP-001", "Resistor", 10);
        ElectronicProduct ep2 = new ElectronicProduct("EP-002", "Capacitor", 8);
        MechanicalProduct mp1 = new MechanicalProduct("MP-001", "Bolt", 5);
        ChemicalProduct cp1 = new ChemicalProduct("CP-001", "Adhesive", 20);

        e1.addProduct(ep1);
        e1.addProduct(ep2);

        m1.addProduct(mp1);

        c1.addProduct(cp1);

        ProdAnalyzer analyzer = new ProdAnalyzer();
        analyzer.addLine(e1);
        analyzer.addLine(m1);
        analyzer.addLine(c1);

        System.out.println("All lines:");
        analyzer.getAllLines().forEach(System.out::println);

        System.out.println("\nHigh-efficiency lines (>0.8):");
        analyzer.getHighEfficiencyLines(0.8).forEach(System.out::println);

        System.out.println("\nCount products by category:");
        analyzer.countProductsByCategory().forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("\nMost loaded line:");
        analyzer.findMostLoadedLine().ifPresent(System.out::println);

        System.out.println("\nAll products:");
        analyzer.getAllProductsFromLines().forEach(System.out::println);

        System.out.println("\nTotal production time (minutes): " + analyzer.calculateTotalProductionTime());

        try {
            m1.addProduct(new MechanicalProduct("MP-002", "Gasket", 7));
        } catch (IllegalArgumentException ex) {
            System.err.println("Error adding product: " + ex.getMessage());
        }
    }
}