package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER = "localhost";
    private static final int PORT = 4592;

    public static void main(String[] args) {
        Socket socket = null;
        Scanner scan = new Scanner(System.in);
        try {
            socket = new Socket(SERVER, PORT);
            System.out.println("Подключен к серверу: " + socket.getRemoteSocketAddress());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            Thread threadReader = new Thread(() -> {
                try {
                    while (true) {
                        output.writeUTF(scan.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadReader.setDaemon(true);
            threadReader.start();

            while (true) {
                String inputStr = input.readUTF();
                if (inputStr.equalsIgnoreCase("/end")) {
                    System.out.println("Disconnected from server");
                    output.writeUTF("/end");
                    break;
                } else {
                    System.out.println("Server: " + inputStr);
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
    //useless comment for pull
}
