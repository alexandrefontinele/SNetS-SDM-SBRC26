package gprmcsa.coreSpectrumAssignment;

import java.util.List;
import java.util.Random;

import gprmcsa.spectrumAssignment.SpectrumAssignmentAlgorithmInterface;
import network.Circuit;
import network.ControlPlane;
import util.IntersectionFreeSpectrum;

/**
 * This class represents the Random Core core allocation algorithm.
 * This algorithm selects a core randomly.
 * After selecting a core, the algorithm applies the spectrum allocation algorithm defined in the simulation configuration file.
 * 
 * @author Alexandre
 *
 */
public class RandomCoreAssignment implements CoreAndSpectrumAssignmentAlgorithmInterface {
	private Random generator;
	private int numberOfCores;
	private SpectrumAssignmentAlgorithmInterface spectrumAssignment;
	
	public RandomCoreAssignment() {
		this.generator = new Random();
		this.numberOfCores = -1;
	}
	
	@Override
	public boolean assignCoreAndSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
		if(spectrumAssignment == null){
			spectrumAssignment = cp.getSpectrumAssignment(); //spectrum allocation algorithm defined in the simulation configuration file
		}
		
		if (numberOfCores == -1) {
			numberOfCores = cp.getMesh().getLinkList().get(0).getNumberOfCores();
		}
		
		int chosenCore = coreAssignment();
    	List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), chosenCore);
        
        int chosen[] = policy(numberOfSlots, composition, circuit, cp);
        circuit.setSpectrumAssigned(chosen);
        circuit.setIndexCore(chosenCore);
        
        if (chosen == null)
        	return false;

        return true;
	}
	
	@Override
	public int coreAssignment() {
		int coreIndex = generator.nextInt(this.numberOfCores);
		return coreIndex;
	}

	@Override
	public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp) {
		
        //Applies the spectrum allocation algorithm selected in the simulation configuration file
    	int chosen[] = spectrumAssignment.policy(numberOfSlots, freeSpectrumBands, circuit, cp);
        
        return chosen;
	}

}
