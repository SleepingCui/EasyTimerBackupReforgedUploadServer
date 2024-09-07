package com.etbkreforged.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = Integer.valueOf(config_process.get_config("port")); // Listening port

    public static void main(String[] args) {
        System.out.println("====== Loading EasyTimerBackupReforged server ... ======\n");
        // Create upload directory
        new File(server.UPLOAD_DIR).mkdirs();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("INFO: Server started, waiting for client connections...");
            System.out.println("INFO: Listening on port " + PORT);
            System.out.println("INFO: File Path: " + server.UPLOAD_DIR);


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("INFO: Client connected: " + clientSocket.getInetAddress());

                // Handle client requests
                new Thread(() -> server.handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}