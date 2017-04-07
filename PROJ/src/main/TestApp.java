package main;

import services.RemoteServiceInterface;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class TestApp {

    public static void main(String[] args) {

        // java TestAPP <peer_ap> <sub_protocol> <opnd_1> <opnd_2>
        // <peer_ap> - Peer Access Point
        // <operation> - BACKUP RESTORE DELETE RECLAIM
        // <opnd_1> - path name for BACKUP RESTORE DELETE or the amount of space to reclaim in KByte
        // <opnd_2> - repDeg for BACKUP

        if (args.length > 4){
            System.out.println("usage: java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
            System.exit(1);
        }

        String peer_ap = args[0];
        String op = args[1];

        try {
            Registry registry = LocateRegistry.getRegistry();
            RemoteServiceInterface remote = (RemoteServiceInterface) registry.lookup(peer_ap);

            String filepath = args[2];

            switch (op) {
                case "BACKUP": {
                    int repDeg = Integer.parseInt(args[3]);
                    remote.backup(filepath, repDeg);
                    break;
                }
                case "RESTORE":
                    remote.restore(filepath);
                    break;
                case "DELETE":
                    remote.delete(filepath);
                    break;
                case "RECLAIM":
                    remote.manageStorage(Integer.parseInt(args[2]));
                    break;
            }

        } catch (Exception e){
            System.err.println("Caught exception: " + e.toString());
            e.printStackTrace();
        }

    }

}
