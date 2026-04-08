package gprmcsa.reallocation.tecniques;

import java.util.List;

import network.Circuit;
import network.ControlPlane;
import util.IntersectionFreeSpectrum;

/**
 * Técnica PUSH and PULL com desenvolvimento finalizado dia 31.05.23.
 * Técnica de migração de tráfego de dados.
 * A técnica permite REATRIBUIÇÃO ESPECTRAL no espectro.
 * @author gustavo
 *
 *
 *TESTADO e FINALIZADO
 */
public class PushPull {

	public PushPull() {

	}

	/**
	 * executa push pull para direita (para cima) no espectro
	 * 
	 * @param circuit
	 * @param cp
	 * @return
	 * @throws Exception 
	 */
	public boolean executeUpper(Circuit circuit, ControlPlane cp) throws Exception {
		int[] band = canBeShiftUpper(circuit);

		if (band == null) {
			return false;
		}

		int numberSlots = circuit.getSpectrumAssigned()[1] - circuit.getSpectrumAssigned()[0] + 1;

		// Saves the allocated spectrum band without the reallocation
		int oldBand[] = circuit.getSpectrumAssigned();

		int newBand[] = new int[2];
		newBand[1] = band[1];
		newBand[0] = band[1] - numberSlots + 1;// verficar se está correto?

		// Releasing the spectrum and guard bands already allocated
		cp.releaseSpectrum(circuit, oldBand, circuit.getRoute().getLinkList(), circuit.getGuardBand());

		// Try to realloc circuit
		circuit.setSpectrumAssigned(newBand);
		if (!cp.allocateSpectrum(circuit, newBand, circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
			throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
		}

		// Verifies if the expansion did not affect the QoT of the circuit or other
		// already active circuits
		boolean QoT = cp.isAdmissibleQualityOfTransmission(circuit);

//				System.out.println("Entrou aqui");
		// QoT ou QoTO não aceitável ou XT inaceitavel
		// colocar o circuito original de volta
		if (!QoT) {

			// QoT was not acceptable after expansion, releasing the spectrum
			cp.releaseSpectrum(circuit, newBand, circuit.getRoute().getLinkList(), circuit.getGuardBand());

			// Reallocating the spectrum and guard bands without the expansion
			circuit.setSpectrumAssigned(oldBand);
			if (!cp.allocateSpectrum(circuit, oldBand, circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
				throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
			}

			// Recalculates the QoT and XT of the circuit
			cp.computeQualityOfTransmission(circuit, null, false);

			// nao deu certo a realocacao devico a QoT inaceitavel

		} else {// QoT Aceitavel
			// verificar se Xt é aceitavel
			boolean xt = cp.isAdmissibleCrosstalk(circuit);// verifica crosstalk

			if (!xt) {
				cp.releaseSpectrum(circuit, newBand, circuit.getRoute().getLinkList(), circuit.getGuardBand());

				// Reallocating the spectrum and guard bands without the expansion
				circuit.setSpectrumAssigned(oldBand);
				if (!cp.allocateSpectrum(circuit, oldBand, circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
					throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
				}

				// recalcula crosstalk
				cp.computeCrosstalk(circuit);

			} else { // realocacao efetivada
				// atualiza o consumo da rede
//						System.out.println(" realocacao com sucesso");
				cp.updateNetworkPowerConsumption();
				return true;
			}

		}

		return false;

	}

	/**
	 * executa push pull para esquerda (para baixo) no espectro
	 * 
	 * @param circuit
	 * @param cp
	 * @return
	 */
	public boolean executeDown(Circuit circuit, ControlPlane cp) throws Exception {

		int[] band = canBeShiftDown(circuit);

		// nao e possivel fazer deslizar no espectro
		if (band == null) {
			return false;
		}

//		System.out.println(band);

		int numberSlots = circuit.getSpectrumAssigned()[1] - circuit.getSpectrumAssigned()[0] + 1;

		// Saves the allocated spectrum band without the reallocation
		int oldBand[] = circuit.getSpectrumAssigned();

		int newBand[] = oldBand.clone();
		newBand[0] = band[0];
		newBand[1] = band[0] + numberSlots - 1;

		// Releasing the spectrum and guard bands already allocated
		cp.releaseSpectrum(circuit, oldBand, circuit.getRoute().getLinkList(), circuit.getGuardBand());

		// Try to realloc circuit
		circuit.setSpectrumAssigned(newBand);
		if (!cp.allocateSpectrum(circuit, newBand, circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
			throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
		}

		// Verifies if the expansion did not affect the QoT of the circuit or other
		// already active circuits
		boolean QoT = cp.isAdmissibleQualityOfTransmission(circuit);

//		System.out.println("Entrou aqui");
		// QoT ou QoTO não aceitável ou XT inaceitavel
		// colocar o circuito original de volta
		if (!QoT) {

			// QoT was not acceptable after expansion, releasing the spectrum
			cp.releaseSpectrum(circuit, newBand, circuit.getRoute().getLinkList(), circuit.getGuardBand());

			// Reallocating the spectrum and guard bands without the expansion
			circuit.setSpectrumAssigned(oldBand);
			if (!cp.allocateSpectrum(circuit, oldBand, circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
				throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
			}

			// Recalculates the QoT and XT of the circuit
			cp.computeQualityOfTransmission(circuit, null, false);

			// nao deu certo a realocacao devico a QoT inaceitavel

		} else {// QoT Aceitavel
			// verificar se Xt é aceitavel
			boolean xt = cp.isAdmissibleCrosstalk(circuit);// verifica crosstalk

			if (!xt) {
				cp.releaseSpectrum(circuit, newBand, circuit.getRoute().getLinkList(), circuit.getGuardBand());

				// Reallocating the spectrum and guard bands without the expansion
				circuit.setSpectrumAssigned(oldBand);
				if (!cp.allocateSpectrum(circuit, oldBand, circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
					throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
				}

				// recalcula crosstalk
				cp.computeCrosstalk(circuit);

			} else { // realocacao efetivada
				// atualiza o consumo da rede
//				System.out.println(" realocacao com sucesso");
				cp.updateNetworkPowerConsumption();
				return true;
			}

		}

		return false;

	}

	/**
	 * verifica a possibilidade de deslizar para a direita (cima) do espectro
	 */
	public int[] canBeShiftUpper(Circuit circuit) {
		List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(),
				circuit.getIndexCore());

		// numero de slots livres para baixo do espectro - verifica o deslizamento para
		// baixo
		int numSlotsUpper = IntersectionFreeSpectrum.freeSlotsUpper(circuit.getSpectrumAssigned(), composition,
				circuit.getGuardBand());
		if (numSlotsUpper <= 0) {
			return null;
		}

		int[] bandToShift = bandAdjacentUpper(circuit.getSpectrumAssigned(), composition, circuit.getGuardBand());

		if (bandToShift != null) {
//			System.out.println("Entrou aqui");
			return bandToShift;
		}

		return null;

	}

	/**
	 * verifica a possibilidade de deslizar para a esquerda (baixo) do espectro
	 */
	public int[] canBeShiftDown(Circuit circuit) {

		List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), circuit.getGuardBand(),
				circuit.getIndexCore());

		// numero de slots livres para baixo do espectro - verifica o deslizamento para
		// baixo
		int numSlotsDown = IntersectionFreeSpectrum.freeSlotsDown(circuit.getSpectrumAssigned(), composition,
				circuit.getGuardBand());
		if (numSlotsDown <= 0) {
			return null;
		}

		int[] bandToShift = bandAdjacentDown(circuit.getSpectrumAssigned(), composition, circuit.getGuardBand());

		if (bandToShift != null) {
//			System.out.println("Entrou aqui2");
			return bandToShift;
		}

		return null;

	}

	/**
	 * Returns the adjacent range less than the range passed by parameter. Used in
	 * optical aggregation algorithms. Metodo retirados da classe
	 * IntersectionFreeSpectrum
	 * 
	 * @param band      int[]
	 * @param bandsFree List<int[]>
	 * @return int[]
	 */
	public static int[] bandAdjacentDown(int band[], List<int[]> bandsFree, int guardBand) {

//    	System.out.println("Espectro circuito: "+band[0]+"-"+band[1]);
//    	System.out.println("Banda de guarda: "+guardBand);
//    	System.out.println("Faixa espectro livre: ");
//    	for (int[] is : bandsFree) {
//			System.out.println(is[0]+"-"+is[1]);
//		}

		for (int[] fl : bandsFree) {

			if (fl[1] == (band[0] - 1 - guardBand)) {
//            	System.out.println("Aqui papai");
				return fl;
			}
		}
		return null;
	}

	/**
	 * Returns the adjacent range higher than the range passed by parameter. Used in
	 * optical aggregation algorithms. Metodo retirados da classe
	 * IntersectionFreeSpectrum
	 * 
	 * @param band      int[]
	 * @param bandsFree List<int[]>
	 * @return int[]
	 */
	public static int[] bandAdjacentUpper(int band[], List<int[]> bandsFree, int guardBand) {
		for (int[] fl : bandsFree) {
			if (fl[0] == (band[1] + 1 + guardBand)) {
				// System.out.println("Teste ok");
				return fl;
			}
		}
		return null;
	}

}
