package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * This class represents a Core in SDM network
 * 
 * @author Jurandir
 */
public class Core implements Serializable {
	
	private int id;
    private Spectrum spectrum;
    private int peso;
    
    private HashSet<Circuit> circuitList;
    private ArrayList<Core> adjacentCores;

    /**
     * Creates a new instance of Link.
     *
     * @param s             Oxc New value of property source.
     * @param d             Oxc New value of property destination.
     * @param numberOfSlots int New value of property number of slots
     * @param spectrumBand  double New value of property spectrum band
     * @param distance      double New Value of distance
     */
    public Core(int id, int numberOfSlots, double spectrumBand) {
        this.id = id;
        this.spectrum = new Spectrum(numberOfSlots, spectrumBand);
        this.peso = 0;
        
        this.circuitList = new HashSet<Circuit>();
        this.adjacentCores = new ArrayList<Core>();
    }
    
    /**
     * Returns the list of adjacent cores
     * 
     * @return ArrayList<Integer> the adjacentCores
     */
    public ArrayList<Core> getAdjacentCores() {
    	return adjacentCores;
    }
    
    /**
     * Sets the list of adjacent cores
     * 
     * @param adjacentCores ArrayList<Core>
     */
    public void setAdjacentCores(ArrayList<Core> adjacentCores) {
    	this.adjacentCores = adjacentCores;
    }

    /**
     * This method occupies a certain range of spectrum defined in the parameter
     *
     * @param interval int[] - Vector of two positions, the first refers to the first slot 
     *                           and the second to the last slot to be used
     * @param guardBand int
     * @return boolean
     */
    public boolean useSpectrum(int interval[], int guardBand) throws Exception {
        return spectrum.useSpectrum(interval, guardBand);
    }

    /**
     * Releases a certain range of spectrum being used
     *
     * @param spectrumBand int[]
     * @param guardBand int
     */
    public void liberateSpectrum(int spectrumBand[], int guardBand) throws Exception {
        spectrum.freeSpectrum(spectrumBand, guardBand);
    }

    /**
     * Returns the list of spectrum bands available on the link
     *
     * @return List<int[]>
     */
    public List<int[]> getFreeSpectrumBands(int guardBand) {
        return spectrum.getFreeSpectrumBands(guardBand);
    }
    
    /**
     * Returns the bandwidth of a slot
     * 
     * @return the slotSpectrumBand
     */
    public double getSlotSpectrumBand() {
        return spectrum.getSlotSpectrumBand();
    }

    /**
     * Returns the number of slots in the link
     * 
     * @return int the numOfSlots
     */
    public int getNumOfSlots() {
        return spectrum.getNumOfSlots();
    }
    
    /**
     * Returns the number of used slots
     * 
	 * @return int
	 */
	public int getUsedSlots(){
		return spectrum.getUsedSlots();
	}

    /**
     * Returns link usage
     *
     * @return Double
     */
    public Double getUtilization() {
        return this.spectrum.utilization();
    }
	
	/**
	 * Returns the list of circuits that use this link
	 * 
	 * @return the listRequests
	 */
	public HashSet<Circuit> getCircuitList() {
		return circuitList;
	}

	/**
	 * Sets the list of circuits that use this link
	 * 
	 * @param listRequests the listRequests to set
	 */
	public void setCircuitList(HashSet<Circuit> circuitList) {
		this.circuitList = circuitList;
	}
	
	/**
	 * Adds a circuit to the list of circuits that use this link
	 * 
	 * @param circuit Circuit
	 */
	public void addCircuit(Circuit circuit){
		if(!circuitList.contains(circuit)){
			circuitList.add(circuit);
		}
	}
	
	/**
	 * Removes a circuit from the list of circuits using this link
	 * 
	 * @param circuit Circuit
	 */
	public void removeCircuit(Circuit circuit){
		circuitList.remove(circuit);
	}
	
	/**
	 * Returns the id
	 * 
	 * @return int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the Spectrum
	 * 
	 * @return Spectrum
	 */
	public Spectrum getSpectrum() {
		return spectrum;
	}
	
	/**
	 * Returns the peso
	 * 
	 * @return int
	 */
	public int getPeso() {
		return peso;
	}
	
	/**
	 * Sets the peso
	 * 
	 * @param peso int
	 */
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	/**
	 * Increments the peso
	 */
	public void incrementPeso() {
		this.peso = this.peso + 1;
	}
	
	/**
	 * Renews the peso
	 */
	public void renewPeso() {
		this.peso = 0;
	}
}
