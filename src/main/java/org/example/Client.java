package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;

public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 5000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while(true) {
                File file = new File(console.readLine());

                if(file.exists()) {
                    out.println(Files.readString(file.toPath()));

                    int[] counter = new int[1];
                    new Thread(() -> {
                        counter[0]++;

                        if(counter[0] > 600) {
                            System.out.println("Seems server thinks file too big, no response!");
                            System.out.println("You can press Crtl+C to exit");
                        }
                    }).start();

                    in.readLine();
                    System.out.println("Yes it's small!");
                    break;
                } else {
                    System.out.println("File not found! try again!");
                }
            }

        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}