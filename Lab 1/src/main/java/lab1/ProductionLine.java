package lab1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ProductionLine<T extends Product> {
    private final String lineId;
    protected final List<T> products = new ArrayList<>();
    private double efficiency;

    protected ProductionLine(String lineId, double efficiency) {
        if (lineId == null || lineId.isBlank()) throw new IllegalArgumentException("lineId required");
        if (efficiency < 0.0 || efficiency > 1.0) throw new IllegalArgumentException("efficiency must be 0.0-1.0");
        this.lineId = lineId;
        this.efficiency = efficiency;
    }

    public String getLineId() { return lineId; }
    public List<T> getProducts() { return Collections.unmodifiableList(products); }
    public double getEfficiency() { return efficiency; }
    public void setEfficiency(double efficiency) {
        if (efficiency < 0.0 || efficiency > 1.0) throw new IllegalArgumentException("efficiency must be 0.0-1.0");
        this.efficiency = efficiency;
    }

    public abstract boolean canProduce(Product product);

    public void addProduct(T product) {
        if (product == null) throw new IllegalArgumentException("product is null");
        if (!canProduce(product)) {
            throw new IllegalArgumentException(
                    String.format("Line %s cannot produce product of type %s", lineId, product.getClass().getSimpleName()));
        }
        products.add(product);
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', eff=%.2f, products=%d}", getClass().getSimpleName(), lineId, efficiency, products.size());
    }
}
