package com.etbkreforged.server;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final int PORT = Integer.valueOf(config_process.get_config("port")); // Listening port

    public static void main(String[] args) {
        Loginit.initLog();
        LOGGER.info("====== Loading EasyTimerBackupReforged server ... ======");
        // Create upload directory
        new File(server.UPLOAD_DIR).mkdirs();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Server started, waiting for client connections...");
            LOGGER.info("Listening on port " + PORT);
            LOGGER.info("File Path: " + server.UPLOAD_DIR);


            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Client connected: " + clientSocket.getInetAddress());

                // Handle client requests
                new Thread(() -> server.handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}