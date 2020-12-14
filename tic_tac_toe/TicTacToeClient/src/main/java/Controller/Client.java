package Controller;

import javafx.application.Application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {

    public static String ipAddr = "localhost";
    public static int port = 8080;
    ClientFunc clientFunc;

    public Client() {
        this.clientFunc = new ClientFunc(ipAddr, port);
    }

    public void sendMsg(Game.Tile[][] board) throws IOException {
        clientFunc.send(board);
    }

//    public void receiveMsg(Byte bytes) throws IOException, ClassNotFoundException {
//        Game.board = clientSm.receive();
//    }

    public static String getIpAddr() {
        return ipAddr;
    }

    public static void setIpAddr(String ipAddr) {
        Client.ipAddr = ipAddr;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Client.port = port;
    }

    public static void main(String[] args) {
        Application.launch(Game.class, args);
    }
}