package main;

import services.RemoteServiceInterface;
import services.State;

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

            

            switch (op) {
                case "BACKUP": {
                	if (args.length != 4){
                        System.out.println("usage: java TestApp <peer_ap> BACKUP PATH REPDEG");
                        System.exit(1);
                    }
                	
                    int repDeg = Integer.parseInt(args[3]);
                    String filepath = args[2];
                    remote.backup(filepath, repDeg);
                    break;
                }
                case "RESTORE":{
                	if (args.length != 3){
                        System.out.println("usage: java TestApp <peer_ap> RESTORE PATH");
                        System.exit(1);
                    }
                	String filepath = args[2];
                    remote.restore(filepath);
                    break;
                    }
                case "DELETE":{
                	if (args.length != 3){
                        System.out.println("usage: java TestApp <peer_ap> DELETE PATH");
                        System.exit(1);
                    }
                	String filepath = args[2];
                    remote.delete(filepath);
                    break;
                    }
                case "RECLAIM":{
                	if (args.length != 3){
                        System.out.println("usage: java TestApp <peer_ap> RESTORE <OP1>");
                        System.exit(1);
                    }
                    remote.manageStorage(Integer.parseInt(args[2]));
                    break;}
                case "STATE":
                	if (args.length != 2){
                        System.out.println("usage: java TestApp <peer_ap> STATE");
                        System.exit(1);
                    }
                	State state = remote.state();
                	state.display();
                	break;
            }

        } catch (Exception e){
            System.err.println("Caught exception: " + e.toString());
            e.printStackTrace();
        }

    }

}
