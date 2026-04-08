package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import simulationControl.parsers.NetworkConfig.CoreConfig;


/**
 * This class represents a link in SDM network
 * 
 * @author Jurandir
 */
public class Link implements Serializable {
	
	private ArrayList<Core> cores;
	
    private Oxc source;
    private Oxc destination;
    private double cost;
    private double distance;  //km
//  private Spectrum spectrum;
    
//  private HashSet<Circuit> circuitList;

    /**
     * Creates a new instance of Link.
     *
     * @param s             Oxc New value of property source.
     * @param d             Oxc New value of property destination.
     * @param numberOfSlots int New value of property number of slots
     * @param spectrumBand  double New value of property spectrum band
     * @param distance      double New Value of distance
     * @param coreList      List<CoreConfig> New value of cores
     */
    public Link(Oxc s, Oxc d, int numberOfSlots, double spectrumBand, double distance, List<CoreConfig> coreList) {
    	this.source = s;
        this.destination = d;
        this.distance = distance;
        
        startCores(coreList, numberOfSlots, spectrumBand);
        
        //this.spectrum = new Spectrum(numberOfSlots, spectrumBand);
        //this.circuitList = new HashSet<Circuit>();
        //this.cores = new Core[NUMBEROFCORES];
        //startCores(s, d, numberOfSlots, spectrumBand, distance);
    }

    /**
     * Is node x destination of this link.
     *
     * @param x Oxc
     * @return true if Oxc x is destination of this Link; false otherwise.
     */
    public boolean isAdjacentOxc(Oxc x) {
        if (destination == x) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method occupies a certain range of spectrum defined in the parameter
     *
     * @param interval - int[] - Vector of two positions, the first refers to the first slot 
     *                           and the second to the last slot to be used
     * @return boolean
     */
 //   public boolean useSpectrum(int interval[], int guardBand) throws Exception {
 //       return spectrum.useSpectrum(interval, guardBand);
 //   }

    /**
     * Releases a certain range of spectrum being used
     *
     * @param spectrumBand int[]
     */
 //   public void liberateSpectrum(int spectrumBand[], int guardBand) throws Exception {
 //       spectrum.freeSpectrum(spectrumBand, guardBand);
 //   }

    /**
     * Getter for property destination.
     *
     * @return Oxc destination
     */
    public Oxc getDestination() {
        return destination;
    }

    /**
     * Setter for property destination.
     *
     * @param destination Oxc New value of property destination.
     */
    public void setDestination(Oxc destination) {
        this.destination = destination;
    }

    /**
     * Setter for property source.
     *
     * @param source Oxc New value of property source.
     */
    public void setSource(Oxc source) {
        this.source = source;
    }

    /**
     * Getter for property source.
     *
     * @return Oxc source
     */
    public Oxc getSource() {
        return source;
    }

    /**
     * Getter for property cost.
     *
     * @return double cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Setter for property Cost.
     *
     * @param cost double new cost.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns the distance of this link
     *
     * @return double
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the name of the link in the format <source, destination>
     *
     * @return String
     */
    public String getName() {
        return "<" + getSource().getName() + "," + getDestination().getName() + ">";
    }

    /**
     * Returns the list of spectrum bands available on the link
     *
     * @return List<int[]>
     */
 //   public List<int[]> getFreeSpectrumBands(int guardBand) {
 //       return spectrum.getFreeSpectrumBands(guardBand);
 //   }
    
    /**
     * Returns the bandwidth of a slot
     * 
     * @return the slotSpectrumBand
     */
 //   public double getSlotSpectrumBand() {
 //       return spectrum.getSlotSpectrumBand();
 //   }

    /**
     * Returns the number of slots in the link
     * 
     * @return int the numOfSlots
     */
  //  public int getNumOfSlots() {
  //      return spectrum.getNumOfSlots();
  //  }
    
    /**
     * Returns the number of used slots
     * 
	 * @return int
	 */
//	public int getUsedSlots(){
//		return spectrum.getUsedSlots();
//	}

    /**
     * Returns link usage
     *
     * @return Double
     */
 //   public Double getUtilization() {
 //       return this.spectrum.utilization();
 //   }
	
	/**
	 * Returns the list of circuits that use this link
	 * 
	 * @return the listRequests
	 */
//	public HashSet<Circuit> getCircuitList() {
//		return circuitList;
//	}

	/**
	 * Sets the list of circuits that use this link
	 * 
	 * @param listRequests the listRequests to set
	 */
//	public void setCircuitList(HashSet<Circuit> circuitList) {
//		this.circuitList = circuitList;
//	}
	
	/**
	 * Adds a circuit to the list of circuits that use this link
	 * 
	 * @param circuit Circuit
	 */
//	public void addCircuit(Circuit circuit){
//		if(!circuitList.contains(circuit)){
//			circuitList.add(circuit);
//		}
//	}
	
	/**
	 * Removes a circuit from the list of circuits using this link
	 * 
	 * @param circuit Circuit
	 */
//	public void removeCircuit(Circuit circuit){
//		circuitList.remove(circuit);
//	}
	
	
//	private void startCores(Oxc s, Oxc d, int numberOfSlots, double spectrumBand, double distance) {
//		for(int i=0; i<NUMBEROFCORES; i++) {
//			this.cores[i] = new Core(s, d, numberOfSlots, spectrumBand, distance, i);
//		}
//	}
	
    /**
     * Initializes the cores of the link
     * 
     * @param s Oxc
     * @param d Oxc
     * @param numberOfSlots int
     * @param spectrumBand double
     * @param distance double
     */
	private void startCores(List<CoreConfig> coreList, int numberOfSlots, double spectrumBand) {
		this.cores = new ArrayList<Core>();
		
		//Create the cores
		for(CoreConfig coreConf : coreList) {
			this.cores.add(coreConf.getId(), new Core(coreConf.getId(), numberOfSlots, spectrumBand));
		}
		
		//Configures the list of adjacent cores
		for(CoreConfig coreConf : coreList) {
			ArrayList<Core> adjacentCoresList = new ArrayList<>();
			
			List<Integer> adjacentIdList = coreConf.getAdjacentCores();
			for(Integer adjId : adjacentIdList) {
				adjacentCoresList.add(getCore(adjId));
			}
			
			getCore(coreConf.getId()).setAdjacentCores(adjacentCoresList);
		}
	}
	
	/**
	 * Returns the Core by id
	 * 
	 * @param idCore int
	 * @return Core
	 */
	public Core getCore(int idCore) {
		for(Core core : this.cores) {
			if(core.getId() == idCore) {
				return core;
			}
		}
		return null;
	}
	
//	public ArrayList<Integer> indexOfAdjacentCores(int id){
//		ArrayList<Integer> listof = new ArrayList<Integer>();
//		
//		if(id == 0) {
//			listof.add(1); // all
//			listof.add(2); // all
//			listof.add(3); // all
//			listof.add(4); // all
//			listof.add(5); // all
//			listof.add(6); // all
//			return listof;
//		}
//		
//		if(id == 6) {
//			listof.add(0); //central
//			listof.add(5); //anterior
//			listof.add(1); //proximo
//			return listof;
//		}
//		
//		if(id == 1) {
//			listof.add(0); //central
//			listof.add(6); //anterior
//			listof.add(2); //proximo
//			return listof;
//		}
//		
//		if((id>1) && (id<6)) {
//			listof.add(0); //central
//			listof.add(id-1); //anterior
//			listof.add(id+1); //proximo
//			return listof;
//		}
//		
//		return null;
//	}
	
//	private boolean isAdjacent(Core core1, Core core2) {
//		ArrayList<Integer> indexOfAdjacents = indexOfAdjacentCores(core1.getId());
//		
//		if(indexOfAdjacents.contains(core2.getId())) {
//			return true;
//		}
//		
//		return false;
//	}
	
	
//	public ArrayList<Core> getAdjacentCores(int id){
//		ArrayList<Core> adjCores = new ArrayList<Core>();
//		
//		for(Core core : this.cores) {
//			if(isAdjacent(getCore(id), core)) {
//				adjCores.add(core);
//			}
//		}
//		
//		return adjCores;
//	}
	
	public ArrayList<Core> getAdjacentCores(int idCore){
		return getCore(idCore).getAdjacentCores();
	}
	
	/**
	 * Returns the total number of slots in link
	 * 
	 * @return int
	 */
	public int totalNumberOfSlots() {
		int cont = 0;
		
		for(Core core : cores) {
			cont = cont + core.getNumOfSlots();
		}
		
		return cont;
	}
	
	/**
	 * Returns list of cores
	 * 
	 * @return ArrayList<Core>
	 */
	public ArrayList<Core> getCores() {
		return cores;
	}
	
	/**
	 * Update the pesos of the adjacent cores
	 * 
	 * @param core int
	 */
	public void updatePesosOfCores(int core) {
		ArrayList<Core> adjacentCores = getAdjacentCores(core);
		
		for (Core coreAdj: adjacentCores) {
			coreAdj.incrementPeso();
		}
	}
	
	/**
	 * Renew all pesos of the cores
	 */
	public void renewAllPesosOfCores() {
		for (Core core: this.cores) {
			core.renewPeso();
		}
	}
	
	/**
	 * Returns the number of cores
	 * 
	 * @return in
	 */
	public int getNumberOfCores() {
		return cores.size();
	}
	
}
