package com.briefbytes.remoteplayer;

import com.briefbytes.remoteplayer.server.Server;

import java.io.FileNotFoundException;
import java.net.SocketException;

public class Main {

    private static void exit(String msg) {
        System.out.println(msg);
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length < 1) exit("Config file needed as argument");

        try {
            Server server = Server.createServer(args[0]);
            System.out.println("Remote Player is listening for requests (both over IPv6 or IPv4).");
            server.run();
        } catch (SocketException e) {
            exit("Error opening socket. " + e.getMessage());
        } catch (FileNotFoundException e) {
            exit("Config file not found");
        } catch (IllegalArgumentException e) {
            exit(e.getMessage());
        }
    }

}
