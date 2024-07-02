public interface FirstInterface {
    void firstMethod(String string);

    default void log(String string) {
        System.out.println("This method is default implementation from FirstInterface: " + string);
    }
}
