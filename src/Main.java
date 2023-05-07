public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            new Controller(4322, 1, 1000, 1000);
        }).start();
//        Controller controller = new Controller(4322,1,1000,1000);
        System.out.println("Controller Connected");
        Dstore dstore = new Dstore(1,4322,1000,"File1");
    }
}