public interface SecondInterface {
    void secondMethod();

    default void log(String string) {
        System.out.println("This method is default implementation from SecondInterface: " + string);
    }
}