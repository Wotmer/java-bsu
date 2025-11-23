package lab1;

import java.util.*;
import java.util.stream.Collectors;

public class ProdAnalyzer {
    private final List<ProductionLine<? extends Product>> lines = new ArrayList<>();

    public void addLine(ProductionLine<? extends Product> line) {
        if (line == null) throw new IllegalArgumentException("line null");
        lines.add(line);
    }

    public List<ProductionLine<? extends Product>> getAllLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<String> getHighEfficiencyLines(double threshold) {
        return lines.stream()
                .filter(l -> l.getEfficiency() > threshold)
                .map(ProductionLine::getLineId)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countProductsByCategory() {
        return lines.stream()
                .flatMap(l -> l.getProducts().stream())
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }

    public Optional<ProductionLine<? extends Product>> findMostLoadedLine() {
        return lines.stream()
                .max(Comparator.comparingInt(l -> l.getProducts().size()));
    }

    public List<Product> getAllProductsFromLines() {
        return lines.stream()
                .flatMap(l -> l.getProducts().stream())
                .collect(Collectors.toList());
    }

    public double calculateTotalProductionTime() {
        return lines.stream()
                .flatMap(l -> l.getProducts().stream())
                .mapToInt(Product::getProductionTime)
                .sum();
    }
}
