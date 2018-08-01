import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/*
 * Echo Server using Fiber in Project Loom
 * Test Command:
 * echo "abc" | nc localhost 8080
 */
public class FiberEchoServer {

    public static void main(String... args) throws Exception {

        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(8080));

        Fiber acceptFiber = Fiber.execute(() -> {

            try {
                while (true) {
                    // Open channel
                    final SocketChannel socketChannel = serverSocketChannel.accept();

                    Fiber.execute(() -> {
                        try {
                            // Read input.
                            ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
                            socketChannel.read(readBuffer);

                            // Write output
                            ByteBuffer writeBuffer = readBuffer.position(0);
                            socketChannel.write(writeBuffer);

                            // Close channel
                            socketChannel.close();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        acceptFiber.await();
        serverSocketChannel.close();
    }

}
