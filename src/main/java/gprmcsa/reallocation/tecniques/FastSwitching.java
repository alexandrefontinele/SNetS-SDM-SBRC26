package gprmcsa.reallocation.tecniques;

import java.util.List;

import network.Circuit;
import network.ControlPlane;
import util.IntersectionFreeSpectrum;

/**
 * Tecnica de migracao de trafego entre nucleos para a mesma faixa de espectro.
 * 
 * @author gustavo
 *
 *
 *         FALTANDO TESTAR
 */
public class FastSwitching {

	public FastSwitching() {
		//
	}

	/**
	 * Troca de nucleo
	 * 
	 * @param circuit
	 * @param cp
	 * @return
	 * @throws Exception
	 */
	public boolean execute(Circuit circuit, int newIndexCore, ControlPlane cp) throws Exception {
		
		// nucleo deve ser diferente do nucleo do circuito a ser realocado
		if (circuit.getIndexCore() == newIndexCore) {
			return false;
		}

		// verifica se a faixa espectral do novo nucleo esta livre
		boolean isPossible = canBeSwitchingCore(circuit, newIndexCore);

		if (isPossible == false) {
			return false;
		}

		// Saves the allocated spectrum band without the reallocation
		int oldIndexCore = circuit.getIndexCore();

//		System.out.println("Espectro ");
		
		// Releasing the spectrum and guard bands already allocated do nucleo original
		cp.releaseSpectrum(circuit, circuit.getSpectrumAssigned(), circuit.getRoute().getLinkList(), circuit.getGuardBand());

		// Try to realloc circuit
		circuit.setIndexCore(newIndexCore);
		if (!cp.allocateSpectrum(circuit, circuit.getSpectrumAssigned(), circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
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
			cp.releaseSpectrum(circuit, circuit.getSpectrumAssigned(), circuit.getRoute().getLinkList(), circuit.getGuardBand());

			// Reallocating the spectrum and guard bands without the expansion
			circuit.setIndexCore(oldIndexCore);
			if (!cp.allocateSpectrum(circuit, circuit.getSpectrumAssigned(), circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
				throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
			}

			// Recalculates the QoT and XT of the circuit
			cp.computeQualityOfTransmission(circuit, null, false);

			// nao deu certo a realocacao devico a QoT inaceitavel

		} else {// QoT Aceitavel
			// verificar se Xt é aceitavel
			boolean xt = cp.isAdmissibleCrosstalk(circuit);// verifica crosstalk

			if (!xt) {
				cp.releaseSpectrum(circuit, circuit.getSpectrumAssigned(), circuit.getRoute().getLinkList(), circuit.getGuardBand());

				// Reallocating the spectrum and guard bands without the expansion
				circuit.setIndexCore(oldIndexCore);
				if (!cp.allocateSpectrum(circuit, circuit.getSpectrumAssigned(), circuit.getRoute().getLinkList(), circuit.getGuardBand())) {
					throw new Exception("Bad RMLSA. Spectrum cant be allocated.");
				}

				// recalcula crosstalk
				cp.computeCrosstalk(circuit);

			} else { // realocacao efetivada
				// atualiza o consumo da rede
//				System.out.println("realocacao com sucesso");
				
				// atualizar a lista de circuitos dos cores
				removeCircuitOldCore(circuit, oldIndexCore);
				addCircuitNewCore(circuit, newIndexCore);
				
				cp.updateNetworkPowerConsumption();
				return true;
			}

		}

		return false;
	}

	/**
	 * Remover circuito da lista de circuitos do Nucleo Antigo
	 * @param circuit
	 * @param oldIndexCore
	 */
	private void removeCircuitOldCore(Circuit circuit, int oldIndexCore) {
		for (int i = 0; i < circuit.getRoute().getLinkList().size(); i++) {
			circuit.getRoute().getLinkList().get(i).getCore(oldIndexCore).removeCircuit(circuit);
		}
	}
	
	/**
	 * Adicionar circuito na lista de circuitos do Nucleo Novo
	 */
	private void addCircuitNewCore(Circuit circuit, int newIndexCore) {
		for (int i = 0; i < circuit.getRoute().getLinkList().size(); i++) {
			circuit.getRoute().getLinkList().get(i).getCore(newIndexCore).addCircuit(circuit);
		}
	}

	/**
	 * verifica se a faixa de espetro do novo nucleo está livre ou nao
	 */
	public boolean canBeSwitchingCore(Circuit circuit, int newIndexCore) {

		int guardBand = circuit.getGuardBand();
		int band[] = circuit.getSpectrumAssigned();

		// composition da rota do nucleo a ser realocado
		List<int[]> composition = IntersectionFreeSpectrum.merge(circuit.getRoute(), guardBand, newIndexCore);

		for (int[] fl : composition) {
			// verifica se o intervalo espectral está contido em um bloco livre nno novo
			// nucleo
			if (band[0] >= fl[0] && band[1] <= fl[1]) {
				return true;
			}

		}

		return false;
	}

}
