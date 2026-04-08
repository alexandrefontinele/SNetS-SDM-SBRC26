package gprmcsa.coreSpectrumAssignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import network.Circuit;
import network.ControlPlane;
import util.IntersectionFreeSpectrum;

public class RandomCoreRandomFit implements CoreAndSpectrumAssignmentAlgorithmInterface{
	private Random generator;
	private int numberOfCores;
	
	public RandomCoreRandomFit() {
		this.generator = new Random();
		this.numberOfCores = -1;
	}
	
 
	@Override
	public boolean assignCoreAndSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
		if (numberOfCores == -1) {
			numberOfCores = cp.getMesh().getLinkList().get(0).getNumberOfCores();
		}
		
		int chosenCore = coreAssignment();
    	List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), chosenCore);

        int chosen[] = policy(numberOfSlots, composition, circuit, cp);
        circuit.setSpectrumAssigned(chosen);
        circuit.setIndexCore(chosenCore);
        
        //System.out.println(chosen[0]);
		//System.out.println(chosen[1]);
		//System.out.println("core: "+chosenCore);
		//System.out.println("quant. de slots: "+ (chosen[1]-chosen[0]+1));
		//System.out.println("----");
        
		
        if (chosen == null)
        	return false;

        return true;
	}

	@Override
	public int coreAssignment() {
								  
		//int a = generator.nextInt()%this.numberOfCores;
		int a = generator.nextInt(this.numberOfCores);
		//System.out.println("core escolhido aleatoriamente: "+a);
		return a;
	}

	@Override
	public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp) {
		int maxAmplitude = circuit.getPair().getSource().getTxs().getMaxSpectralAmplitude();
		if(numberOfSlots> maxAmplitude) return null;
    	int chosen[] = null;
		ArrayList<int[]> bandList = new ArrayList<int[]>();
		
		for (int[] band : freeSpectrumBands) { //checks and guard the free bands that can establish the requisition
			if(band[1] - band[0] + 1 >= numberOfSlots){
				int faixaTemp[] = band.clone();
				bandList.add(faixaTemp);
			}
		}
		
		if(bandList.size() > 0){ //if you have free bands, choose one randomly
			Random rand = new Random();
			int indexBand = rand.nextInt(bandList.size());
			chosen = bandList.get(indexBand);
			chosen[1] = chosen[0] + numberOfSlots - 1; //it is not necessary to allocate the entire band, only the number of slots necessary
		}
		
		return chosen;
	}

}
