package simulationControl.distributedProcessing;

import measurement.Measurements;
import simulator.Simulation;
import simulator.Simulator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerS extends UnicastRemoteObject implements ServerSInterface {
    String name = "anonimous";

    private int numJobs = 0;
    private static int quantity = 0;

    public static void runServerS(String serverMLocation){
        try {
            ServerMInterface server = (ServerMInterface) Naming.lookup("//"+serverMLocation+"/ServerM");
            ServerS severS = new ServerS();
            server.register(severS);
            System.out.println("registered");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void runServerS(String serverMLocation, String threadsNumber){
    	
    	Integer threads = Integer.parseInt(threadsNumber);
    	ExecutorService executor = Executors.newScheduledThreadPool(threads);

    	for(int i = 0; i < threads; i++) {
	    	executor.execute(new Runnable() {
	    		@Override
	            public void run() {
			        try {
			            ServerMInterface server = (ServerMInterface) Naming.lookup("//"+serverMLocation+"/ServerM");
			            ServerS severS = new ServerS();
			            quantity++;
			            severS.name = "Slave " + quantity;
			            server.register(severS);
			            System.out.println("registered");
			        }catch (Exception ex){
	                    ex.printStackTrace();
	                    executor.shutdown();
	                }
	    		}
	    	});
    	}
	
		executor.shutdown();
    }

    protected ServerS() throws RemoteException {

    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Measurements simulate(Simulation simulation) throws Exception {
        Simulator simulator = new Simulator(simulation);
        //System.out.println("init simulation");
        Measurements res = simulator.start();
        //System.out.println("end simulation");
        numJobs++;
        System.out.println(name + ", num jobs: " + numJobs);
        return res;
    }


}
