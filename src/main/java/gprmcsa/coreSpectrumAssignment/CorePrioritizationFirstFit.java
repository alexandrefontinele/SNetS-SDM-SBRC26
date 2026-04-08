package gprmcsa.coreSpectrumAssignment;

import java.util.ArrayList;
import java.util.List;

import network.Circuit;
import network.ControlPlane;
import network.Core;
import network.Link;
import util.IntersectionFreeSpectrum;

public class CorePrioritizationFirstFit implements CoreAndSpectrumAssignmentAlgorithmInterface{
	private static final int MAXPESOCORE = 9999;
	
	private int numberOfCores;
	private int[] coreOfTheTime;
	private int[] pesos;
	private int contador;
	
	public CorePrioritizationFirstFit(){
		this.numberOfCores = -1;
	}
	
	@Override
	public boolean assignCoreAndSpectrum(int numberOfSlots, Circuit circuit, ControlPlane cp) {
		if (numberOfCores == -1) {
			numberOfCores = cp.getMesh().getLinkList().get(0).getNumberOfCores();
			
			coreOfTheTime = new int[numberOfCores];
			pesos = new int[numberOfCores];
			
			for(int i = 0; i < numberOfCores; i++) {
				pesos[i] = 0;
			}
			
			if (numberOfCores == 7) {
				coreOfTheTime[0] = 6;
				coreOfTheTime[1] = 4;
				coreOfTheTime[2] = 2;
				coreOfTheTime[3] = 3;
				coreOfTheTime[4] = 1;
				coreOfTheTime[5] = 5;
				coreOfTheTime[6] = 0;
				
			} else if(numberOfCores == 12) {
				coreOfTheTime[0] = 1;
				coreOfTheTime[1] = 2;
				coreOfTheTime[2] = 3;
				coreOfTheTime[3] = 4;
				coreOfTheTime[4] = 5;
				coreOfTheTime[5] = 6;
				coreOfTheTime[6] = 7;
				coreOfTheTime[7] = 8;
				coreOfTheTime[8] = 9;
				coreOfTheTime[9] = 10;
				coreOfTheTime[10] = 11;
				coreOfTheTime[11] = 12;
				
			} else if(numberOfCores == 19) {
				coreOfTheTime[0] = 3;
				coreOfTheTime[1] = 2;
				coreOfTheTime[2] = 1;
				coreOfTheTime[3] = 6;
				coreOfTheTime[4] = 5;
				coreOfTheTime[5] = 4;
				coreOfTheTime[6] = 7;
				coreOfTheTime[7] = 0;
				coreOfTheTime[8] = 9;
				coreOfTheTime[9] = 8;
				coreOfTheTime[10] = 13;
				coreOfTheTime[11] = 12;
				coreOfTheTime[12] = 14;
				coreOfTheTime[13] = 16;
				coreOfTheTime[14] = 15;
				coreOfTheTime[15] = 14;
				coreOfTheTime[16] = 19;
				coreOfTheTime[17] = 18;
				coreOfTheTime[18] = 17;
			}
			
			contador = 0;
		}
		
		int chosenCore = coreAssignment(circuit);
    	List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(), chosenCore);
        
        int chosen[] = policy(numberOfSlots, composition, circuit, cp);
        circuit.setSpectrumAssigned(chosen);
        circuit.setIndexCore(chosenCore);

        //System.out.println("Verificação do xt dos vizinhos de:"+circuit.getId());
        //Crosstalk crosstalk = new Crosstalk();
        //crosstalk.isAdmissibleInOthers(circuit);
        //System.out.println("--------------------------------\n\n");

       //System.out.println("\n\nCorePrioritization FF");
       //System.out.println("Core escolhido: "+chosenCore);
       //System.out.println("Faixa de slots: "+chosen[0]+"-"+chosen[1]);
        
        //imprimeLog(circuit);
        
        if (chosen == null)
        	return false;

        return true;
	}

	@Override
	public int coreAssignment() {
		int escolha;
		escolha = contador;
		
		if (contador == numberOfCores -1) {
    		contador = 0;
    		return coreOfTheTime[escolha];
    	}else {
    		contador++;
    		return coreOfTheTime[escolha];
    	}
	}
	
	public int coreAssignment(Circuit circuit) {
		int coreEscolhido = 0;
		int pesoCoreEscolhido = MAXPESOCORE * circuit.getRoute().getLinkList().size();
		
		// Vendo qual core tem o menor peso total
		for(int i=numberOfCores-1; i>=0; i--) {
			int pesoCore = 0;
			
			for(Link link : circuit.getRoute().getLinkList()) {
				pesoCore = pesoCore + link.getCore(i).getPeso();
			}
			
			if(pesoCore < pesoCoreEscolhido) {
				coreEscolhido = i;
				pesoCoreEscolhido = pesoCore;
			}
		}
		
		//Atualizando os pesos
		for(Link link : circuit.getRoute().getLinkList()) {
			link.getCore(coreEscolhido).setPeso(MAXPESOCORE);
			link.updatePesosOfCores(coreEscolhido);
		}
		
		//zera os pesos
		for(Link link : circuit.getRoute().getLinkList()) {
			boolean flag = true;
			
			for(Core core : link.getCores()) {
				if(core.getPeso() < MAXPESOCORE) {
					flag = false;
					break;
				}
			}
			
			if(flag) {
				//System.out.println("Peso resetado no link "+link.getName());
				link.renewAllPesosOfCores();
			}
		}
		
		//Zera os pesos
		//if(pesoCoreEscolhido >= (MAXPESOCORE * circuit.getRoute().getLinkList().size() ) ){
			//for(Link link : circuit.getRoute().getLinkList()) {
				//link.renovaTodosOsPesos();
			//}
		//}
		
		return coreEscolhido;
	}

	@Override
	public int[] policy(int numberOfSlots, List<int[]> freeSpectrumBands, Circuit circuit, ControlPlane cp) {
		int maxAmplitude = circuit.getPair().getSource().getTxs().getMaxSpectralAmplitude();
        if(numberOfSlots> maxAmplitude) return null;
    	int chosen[] = null;
    	
        for (int[] band : freeSpectrumBands) {
        	
            if (band[1] - band[0] + 1 >= numberOfSlots) {
                chosen = band.clone();
                chosen[1] = chosen[0] + numberOfSlots - 1;//It is not necessary to allocate the entire band, just the amount of slots required
                break;
            }
        }
        
       // System.out.println("teste2");
        
        return chosen;
	}
	
//	private int returnPrioridade() {
//		int core;
//		
//		for(int i=6; i>=0; i--) {
//			
//		}
//	}
	
	private void incrementAdjacents(int id, Circuit circuit) {
		ArrayList<Integer> listof = returnAdjacents(id, circuit);
		
		for(int i : listof) {
			pesos[i]++;
		}
	}
	
	private ArrayList<Integer> returnAdjacents(int id, Circuit circuit) {
		ArrayList<Integer> listof = new ArrayList<Integer>();
		
		ArrayList<Core> adjacentCores = circuit.getRoute().getLink(0).getAdjacentCores(id);
		
		for(int i = 0; i < adjacentCores.size(); i++) {
			listof.add(adjacentCores.get(i).getId());
		}
		
		return listof;
	}
	
	private void imprimeLog(Circuit circuit) {
		System.out.println("\n");
		
		System.out.println("ID do caminho optico: "+circuit.getId());
		System.out.println("Rota do caminho optico: "+circuit.getRoute().getRouteInString());
		System.out.println("Comprimento total da rota: "+circuit.getRoute().getDistanceAllLinks());
		System.out.println("Formato de modulacao do caminho optico: "+circuit.getModulation().getName());
		
		if(circuit.getSpectrumAssigned()==null) {
			System.out.println("Quantidade de Slots usados pelo caminho optico: --");
			System.out.println("Slots usados pelo caminho optico: --");
		}else {
			System.out.println("Quantidade de Slots usados pelo caminho optico: "+(circuit.getSpectrumAssigned()[1] - circuit.getSpectrumAssigned()[0] + 1));
			System.out.println("Slots usados pelo caminho optico: "+circuit.getSpectrumAssigned()[0]+"-"+circuit.getSpectrumAssigned()[1]);
		}
			
		System.out.println("Nucleo usado pelo caminho optico: "+circuit.getIndexCore());
		System.out.println("Crosstalk neste caminho optico: "+circuit.getXt());
	}
}
