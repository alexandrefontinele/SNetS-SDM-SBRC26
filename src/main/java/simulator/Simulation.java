package simulator;

import measurement.Measurements;
import network.ControlPlane;
import network.Mesh;
import network.TranslucentControlPlane;
import simulationControl.Util;
import simulationControl.parsers.SimulationConfig;

import java.io.Serializable;

import gprmcsa.GPRMCSA;

/**
 * This class represents a single simulation
 * 
 * @author Iallen
 */
@SuppressWarnings("serial")
public class Simulation implements Serializable {

    private Mesh mesh;
    private ControlPlane controlPlane;
    private Measurements measurements;
    private Util util;

    private int loadPoint;
    private int replication;

    /**
     * Creates a new instance of Simulation
     * 
     * @param sc SimulationConfig
     * @param mesh Mesh
     * @param loadPoint int
     * @param replication int
     * @throws Exception
     */
    public Simulation(SimulationConfig sc, Mesh mesh, int loadPoint, int replication, Util util){
        this.loadPoint = loadPoint;
        this.replication = replication;
        this.measurements = new Measurements(sc.getRequests(), loadPoint, replication, mesh, sc.getActiveMetrics());
        this.mesh = mesh;
        this.util = util;
        
        GPRMCSA grmlsa = new GPRMCSA(sc.getGrooming(), sc.getIntegratedRmlsa(), sc.getRouting(), sc.getkRouting(), sc.getModulationSelection(), sc.getSpectrumAssignment(), 
        							 sc.getRegeneratorAssignment(), sc.getCoreAndSpectrumAssignment(), sc.getReallocation(), sc.getPowerAssignment());
        
        // Check if any algorithm was entered incorrectly.
		checkNotNull(grmlsa.instantiateGrooming(), "grooming: " + sc.getGrooming());
		checkNotNull(grmlsa.instantiateIntegratedRSA(), "integrated: " + sc.getIntegratedRmlsa());
		checkNotNull(grmlsa.instantiateRouting(), "routing: " + sc.getRouting());
		checkNotNull(grmlsa.instantiateKRouting(), "kRouting: " + sc.getkRouting());
		checkNotNull(grmlsa.instantiateSpectrumAssignment(), "spectrumAssignment: " + sc.getSpectrumAssignment());
		checkNotNull(grmlsa.instantiateCoreAndSpectrumAssignment(), "coreAndSpectrumAssignment: " + sc.getCoreAndSpectrumAssignment());
		checkNotNull(grmlsa.instantiateModulationSelection(), "modulationSelection: " + sc.getModulationSelection());
		checkNotNull(grmlsa.instantiatePowerAssignment(), "powerAssignment: " + sc.getPowerAssignment());
		checkNotNull(grmlsa.instantiateReallocation(), "reallocation: " + sc.getReallocation());
		checkNotNull(grmlsa.instantiateRegeneratorAssignment(), "regeneratorAssignment: " + sc.getRegeneratorAssignment());
		
        if(sc.getNetworkType() == GPRMCSA.TRANSPARENT){
        	controlPlane = new ControlPlane(mesh, sc.getRmlsaType(), grmlsa.instantiateGrooming(), grmlsa.instantiateIntegratedRSA(), grmlsa.instantiateRouting(), grmlsa.instantiateKRouting(), 
        									grmlsa.instantiateSpectrumAssignment(), grmlsa.instantiateModulationSelection(), grmlsa.instantiateCoreAndSpectrumAssignment(), 
        									grmlsa.instantiateReallocation(), grmlsa.instantiatePowerAssignment());
        
        }else if(sc.getNetworkType() == GPRMCSA.TRANSLUCENT){
        	controlPlane = new TranslucentControlPlane(mesh, sc.getRmlsaType(), grmlsa.instantiateGrooming(), grmlsa.instantiateIntegratedRSA(), grmlsa.instantiateRouting(), 
        												grmlsa.instantiateKRouting(), grmlsa.instantiateSpectrumAssignment(), grmlsa.instantiateRegeneratorAssignment(), grmlsa.instantiateModulationSelection(), 
        												grmlsa.instantiateCoreAndSpectrumAssignment(), grmlsa.instantiateReallocation(), grmlsa.instantiatePowerAssignment());
        }
    }
    
    /**
     * Check if any algorithm name was entered incorrectly.
     * @param obj Object
     * @param name String
     */
    private static void checkNotNull(Object obj, String name) {
	    if (obj == null) {
	        System.err.println("Erro: Classe/algoritmo não encontrado -> " + name);
	        System.err.println("Encerrando o programa.");
	        System.exit(1);
	    }
	}
    
    
    /**
     * Returns the load point
     * 
     * @return the loadPoint
     */
    public int getLoadPoint() {
        return loadPoint;
    }

    /**
     * Sets the replication
     * 
     * @return replication
     */
    public int getReplication() {
        return replication;
    }

    /**
     * Returns the mesh of the network
     * 
     * @return the mesh
     */
    public Mesh getMesh() {
        return mesh;
    }
    
    /**
     * Sets the mesh of the network
     * 
     * @param mesh the mesh to set
     */
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Returns the control plane
     * 
     * @return controlPlane
     */
    public ControlPlane getControlPlane() {
        return controlPlane;
    }

    /**
     * Returns the measurements
     * 
     * @return measurements
     */
    public Measurements getMeasurements() {
        return measurements;
    }
    
    public Util getUtil() {
    	return this.util;
    }
    
}
