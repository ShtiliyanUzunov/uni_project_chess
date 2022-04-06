package server;

// A Java program for a Server

import java.net.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {
    //initialize socket and input stream
    private final int port;
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private Scanner scanner;
    private FrontController frontController;

    // constructor with port
    public Server(int port) {
        this.port = port;
        frontController = new FrontController();

        startServer();
        mainLoop();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForClient() {
        System.out.println("Waiting for a client...");
        try {
            socket = serverSocket.accept();
            frontController.setSocket(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nClient accepted.\n");
    }

    private void mainLoop() {
        while (true) {
            waitForClient();
            initializeScanner();
            clientLoop();
        }
    }

    private void clientLoop() {
        String msg = "";
        try {
            while (!msg.equalsIgnoreCase(Protocol.CLOSE_CONNECTION)) {
                String nextLine;
                StringBuilder msgBuilder = new StringBuilder();

                do {
                    nextLine = scanner.nextLine();
                    msgBuilder.append(nextLine).append("\n");
                } while (nextLine.length() > 0);

                msg = msgBuilder.toString();
                frontController.dispatchMessage(msg);
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nClient disconnected.\n");
        }
    }

    private void initializeScanner() {
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        // close connection
        try {
            System.out.println("Stopping server.");
            socket.close();
            scanner.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server server = new Server(5000);
    }
}