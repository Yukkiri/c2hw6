package com.company;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    static Socket socket = null;

    static final int PORT = 4592;


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server OK");
            socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getRemoteSocketAddress());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            Thread t1 = new Thread(() -> {
                try {
                    while (true) {
                        output.writeUTF(scan.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t1.setDaemon(true);
            t1.start();

            while (true) {
                String inputStr = input.readUTF();
                if (inputStr.equalsIgnoreCase("/end")) {
                    System.out.println("Client disconnected");
                    output.writeUTF("/close");
                    break;
                } else {
                    System.out.println("User: " + inputStr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
