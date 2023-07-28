public enum FoodItem {
    STARTER(10), MAINCOURSE(20), DESSERT(20), DRINK(5);

    private final int cost;
    FoodItem(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return this.cost;
    }
}
