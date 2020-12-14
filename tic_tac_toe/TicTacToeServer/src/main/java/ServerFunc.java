import java.io.*;
import java.net.Socket;

class ServerFunc extends Thread {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerFunc(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        start();

    }


    @Override
    public void run() {
        Object board = null;
        while (true) {
            try {
                board = in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (ServerFunc vr : Server.players) {
                vr.send(board);
            }
        }
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}