package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server listening on port 5000");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                // Входной поток для получения файла
                InputStream inputStream = clientSocket.getInputStream();
                byte[] fileData = inputStream.readAllBytes();

                // Проверка размера файла
                if (fileData.length <= 1024) { // размер должен быть меньше или равен 1 KB
                    File file = new File("received_file.txt");
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        fileOutputStream.write(fileData);
                        System.out.println("File saved on server");

                        // Отправка файла обратно клиенту
                        OutputStream outputStream = clientSocket.getOutputStream();
                        outputStream.write(fileData);
                        System.out.println("File sent back to client");
                    }
                } else {
                    System.out.println("File is too large and will not be saved on server");
                }

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
