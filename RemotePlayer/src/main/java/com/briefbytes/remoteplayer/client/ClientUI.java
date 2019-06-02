package com.briefbytes.remoteplayer.client;

import com.briefbytes.remoteplayer.client.action.*;
import com.briefbytes.remoteplayer.utils.OsUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console based app to test the server
 */
public class ClientUI {

    private static Client client;
    private static Scanner scanner;

    private static <T> T selectItemFromList(List<T> list, Scanner scanner, String title) {
        System.out.println("\n" + title);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + ": " + list.get(i));
        }
        int op = Integer.parseInt(scanner.nextLine());
        return op <= 0 | op > list.size() ? null : list.get(op - 1);
    }

    private static void close() {
        if (client != null) client.close();
        if (scanner != null) scanner.close();
    }

    private static void executeAction(PlayerAction action) throws IOException {
        VideoFile video = null;
        if (action instanceof PlayerActionPlayFile) {
            List<VideoFile> files = client.requestFiles();
            if (files.isEmpty()) {
                System.out.println("No files found");
            } else {
                video = selectItemFromList(files, scanner, "Select a file");
            }
        }
        action.execute(client, video);
    }

    public static void main(String[] args) {
        try {
            scanner = new Scanner(System.in);
            if (OsUtils.isWindows) {
                InetAddress broadcastAddress = selectItemFromList(Client.findBroadcastAddresses(),
                        scanner, "Select broadcast address");
                client = new Client(broadcastAddress.getHostAddress());
            } else {
                client = new Client();
            }

            InetAddress address;

            List<AddressInfo> addresses = client.findAddresses();
            if (addresses.isEmpty()) {
                System.out.print("Could not find addresses\nEnter address manually: ");
                address = InetAddress.getByName(scanner.nextLine().trim());
            } else {
                AddressInfo addressInfo = selectItemFromList(addresses, scanner, "Select an address");
                if (addressInfo == null) {
                    throw new IllegalArgumentException("Invalid address selected");
                } else {
                    address = addressInfo.getAddress();
                }
            }

            client.updateAddress(address);

            List<PlayerAction> actions = Arrays.asList(new PlayerActionPlayFile(), new PlayerActionPause(),
                    new PlayerActionToggleSubtitles(), new PlayerActionQuit(), new PlayerActionShutdown());

            while (true) {
                PlayerAction action = selectItemFromList(actions, scanner, "Select an action");
                if (action == null) break;
                executeAction(action);
            }
        } catch (IOException e) {
            System.out.println("I/O error.\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
    }

}
