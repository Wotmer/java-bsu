package lab1;

public abstract class Product {
    private final String id;
    private final String name;
    private final int productionTime;

    public Product(String id, String name, int productionTime) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (productionTime < 0) throw new IllegalArgumentException("productionTime must be >= 0");
        this.id = id;
        this.name = name;
        this.productionTime = productionTime;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getProductionTime() { return productionTime; }

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', time=%d}", getClass().getSimpleName(), id, name, productionTime);
    }
}
