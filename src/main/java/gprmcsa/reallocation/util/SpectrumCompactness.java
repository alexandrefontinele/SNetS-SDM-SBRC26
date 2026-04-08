package gprmcsa.reallocation.util;

import java.io.Serializable;

import network.Core;

/**
 * Equacao da medida de fragmentacao Spectrum Compactness retirada do artigo <Crosstalk-Aware Spectrum Defragmentation Based on Spectrum Compactness in Space Division Multiplexing Enabled Elastic Optical Networks With Multicore Fiber >
 * @author gustavo
 *
 */

public class SpectrumCompactness implements Serializable {
	
	private int indexSlotMaxOcup; // maior indice de slot ocupado
	private int indexSlotMinOcup; // menor indice de slot ocupado
	private double amountSlotsOcup; // quantidade de slots ocupados
	private double amountSlotsFree; // quantidade de slots livres
	private int amountOfFreeFragments; // quantidade de fragmentos (blocos) de slots livres
	private double sc; // resultado do calculo do spectrum compactness
	

	public SpectrumCompactness(){
		this.indexSlotMaxOcup = -1;
		this.indexSlotMinOcup = 99999;
		this.amountOfFreeFragments = 0;
		this.amountSlotsFree = 0;
		this.amountSlotsOcup = 0;
	}
	
	/**
	 * calcula spectrum compactness de um nucleo
	 * returna -1 caso o nucleo nao tenha ocupacao
	 * @param c
	 * @return
	 */
	public double compute(Core c) {
		// o calculo é feito apenas se o nucleo tem alguma ocupacao
		if (!isCoreOccupied(c)) {
			return -1;
		}
		// para garantir, reseto todas as variaveis
		
		resetAttributes();
		
		// zerando o valor do atributo spectrum compactness
		this.sc = 0;
		
		// numero de blocos de slots livres - fragmentos
		this.amountOfFreeFragments = c.getSpectrum().getFreeSpectrumBands().size();
		
		// somatorio de slots livres
		for (int[] freeBand : c.getSpectrum().getFreeSpectrumBands()) {
			this.amountSlotsFree = this.amountSlotsFree + (freeBand[1] - freeBand[0] + 1);
		}
		
		// somatorio de slots ocupados
		this.amountSlotsOcup = c.getSpectrum().getNumOfSlots() - this.amountSlotsFree;
		
		// menor indice de slot ocupado
		int indexSlotMinFree = c.getSpectrum().getFreeSpectrumBands().get(0)[0];
		if (indexSlotMinFree == 1) {
			this.indexSlotMinOcup = c.getSpectrum().getFreeSpectrumBands().get(0)[1] + 1;
		}else {
			this.indexSlotMinOcup = 1;
		}
		
		
		// maior indice de slot ocupado
		int sizeList = c.getSpectrum().getFreeSpectrumBands().size();
		int indexSlotMaxFree = c.getSpectrum().getFreeSpectrumBands().get(sizeList-1)[1];
		if (indexSlotMaxFree == c.getNumOfSlots()) {
			this.indexSlotMaxOcup = c.getSpectrum().getFreeSpectrumBands().get(sizeList-1)[0] - 1;
		}else {
			this.indexSlotMaxOcup = c.getNumOfSlots();
		}
		
		
		
		sc = ((indexSlotMaxOcup - indexSlotMinOcup +1)/amountSlotsOcup)*(amountSlotsFree/amountOfFreeFragments);
		
		return sc;
	}
	
	public void resetAttributes() {
		// TODO Auto-generated method stub
		this.indexSlotMaxOcup = -1;
		this.indexSlotMinOcup = 99999;
		this.amountOfFreeFragments = 0;
		this.amountSlotsFree = 0;
		this.amountSlotsOcup = 0;
	}
	
	/**
	 * verifica se o nucleo tem slots ocupados
	 * @param c
	 * @return
	 */
	private boolean isCoreOccupied(Core c) {
		// TODO Auto-generated method stub
		if (c.getUsedSlots() > 0) {
			return true;
		}
		
		return false;

	}
	
	
}
