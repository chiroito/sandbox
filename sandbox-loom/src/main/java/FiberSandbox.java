public class FiberSandbox {

    public static void main(String... args){

        Fiber f = Fiber.execute(()->{
            System.out.println("Hello Fiber !");
        });

        f.await();
    }
}
