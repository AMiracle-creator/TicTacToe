package Controller;

import java.net.*;
import java.io.*;

class ClientFunc {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String addr;
    private int port;


    public ClientFunc(String addr, int port)  {
        this.addr = addr;
        this.port = port;
        try {
            System.out.println("socket1");
            this.socket = new Socket(addr, port);
            System.out.println("socket2");
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("ya tut");
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
//        new ReadMsg().start();
    }

    public void send(Game.Tile[][] board) throws IOException {
        out.writeObject(board);
        out.flush();
    }

    public void receive() throws IOException, ClassNotFoundException {
        Game.Tile[][] board;

        board = ((Game.Tile[][])in.readObject());

        Game.board = board;
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            Game.Tile[][] board;
            try {
                while (true) {
                    board = ((Game.Tile[][])in.readObject());

                    Game.board = board;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}