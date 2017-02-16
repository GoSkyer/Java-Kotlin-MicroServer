package kotlin;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by galaxy on 2017/2/14.
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
    }

}
