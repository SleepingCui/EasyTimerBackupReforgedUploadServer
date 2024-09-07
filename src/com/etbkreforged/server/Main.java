package com.etbkreforged.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = Integer.valueOf(config_process.get_config("port")); // Listening port
    private static final String UPLOAD_DIR = config_process.get_config("path")+"\\"; // Directory to save uploaded files

    public static void main(String[] args) {
        System.out.println("====== Loading EasyTimerBackupReforged server ... ======\n");
        // Create upload directory
        new File(UPLOAD_DIR).mkdirs();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("INFO: Server started, waiting for client connections...");
            System.out.println("INFO: Listening on port " + PORT);
            System.out.println("INFO: File Path: " + UPLOAD_DIR);


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("INFO: Client connected: " + clientSocket.getInetAddress());

                // Handle client requests
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream in = clientSocket.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(in);
             DataInputStream dis = new DataInputStream(bis)) {

            // Read the filename from the input stream
            String fileName = dis.readUTF();
            File outputFile = new File(UPLOAD_DIR, fileName);

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytes = 0;

                // Receive file data
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }

                bos.flush();
                System.out.println("INFO: File received: " + fileName + ", size: " + totalBytes + " bytes");

                // Send confirmation message to client
                OutputStream out = clientSocket.getOutputStream();
                String confirmationMessage = "INFO: File upload successful, size: " + totalBytes + " bytes";
                out.write(confirmationMessage.getBytes());
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}