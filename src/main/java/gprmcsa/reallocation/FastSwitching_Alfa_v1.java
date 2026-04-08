package gprmcsa.reallocation;

import java.util.ArrayList;

import gprmcsa.reallocation.tecniques.FastSwitching;
import gprmcsa.reallocation.util.SortListCircuits;
import network.Circuit;
import network.ControlPlane;
import network.Core;
import network.Link;

/**
 * O gatilho é o bloqueio do crosstalk Seleciona os circuitos ativos que estao
 * causando crosstalk no novo circuito a ser estabelecido Realoca o circuito
 * ativo para nucleos não vizinhos do novo circuito A cada realocacao, tenta-se
 * atender o novo circuito Quando o novo circuito for atendido, após
 * realocacoes, o algoritmo é finalizado
 *
 * 
 * 
 * @author gustavo
 *
 */
public class FastSwitching_Alfa_v1 implements ReallocationAlgorithmInterface {

	// uso do arraylist facilita a ordenacao da lista
	private ArrayList<Circuit> selectedCircuits;
	private ArrayList<Core> coresAdjacents;// nucleos adjacentes
	private ArrayList<Core> coresNoAdjacents;// nucleos nao adjacentes que nao seja o do centro
	private SortListCircuits sortListCircuits;
	private FastSwitching fastSwitching;

	public FastSwitching_Alfa_v1() {
		this.sortListCircuits = new SortListCircuits();
		this.fastSwitching = new FastSwitching();
	}

	@Override
	public void selectActivesCircuits(ControlPlane cp, Circuit request) {

		this.selectedCircuits = new ArrayList<>();

		// indice do nucleo do novo circuito
		int indiceNucleo = request.getIndexCore();

		/*
		 * Circuitos que estao nos nucleos vizinhos do nucleo do novo circuito Seleção
		 * dos circuitos que tem espectro em comum, ou seja, que causam espectro
		 */
		for (Link enlace : request.getRoute().getLinkList()) {
			for (Core core : enlace.getAdjacentCores(indiceNucleo)) {
				// adicionando nucleo adjacente a lista, para uso futuro
				for (Circuit circuit : core.getCircuitList()) {
					// verifica se o circuito já está na lista de circuitos selecionados, se nao,
					// adiciona-os
					// verifica se circuito ativo tem slots em comum com novo circuito
					if (!selectedCircuits.contains(circuit)) {
						if (espectroEmComum(request, circuit)) {
							selectedCircuits.add(circuit);
						}
					}
				}
			}
		}

		this.sortListCircuits.sort(selectedCircuits, "hops", cp);
	}

	@Override
	public void chooseNewResourcesForSelectedCircuits(ControlPlane cp) {
		// TODO Auto-generated method stub
	}

	@Override
	public void trafficMigration() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean strategy(Circuit requisicao, ControlPlane cp) {

		this.coresAdjacents = new ArrayList<>();
		this.coresNoAdjacents = new ArrayList<>();

		// nao fazer fast switching caso a requisicao for do nucleo central
		if (requisicao.getIndexCore() == 0) {
			return false;
		}

		// selecionar circuitos
		this.selectActivesCircuits(cp, requisicao);

		// nao foi selecionado nenhum circuito para a realocacao
		if (this.selectedCircuits.size() == 0) {
			return false;
		}

		// lista de cores adjacentes
		this.coresAdjacents = requisicao.getRoute().getLinkList().get(0).getAdjacentCores(requisicao.getIndexCore());

		// popular a lista de nucleos nao adjacentes
		ArrayList<Core> coreList = requisicao.getRoute().getLinkList().get(0).getCores();
		for (Core c : coreList) {
			if (!coresAdjacents.contains(c) && c.getId() != requisicao.getIndexCore()) {
				this.coresNoAdjacents.add(c);
			}
		}

		// percorrendo a lista de circuitos para tentar realocar
		// tentar realocar para nucleo nao vizinho

		for (Circuit circuit : this.selectedCircuits) {

			for (Core coreNoAdjacent : this.coresNoAdjacents) {
				// verificar se o nucleo adjacente nao é o mesmo do circuito a ser realocado
				try {

					boolean sucess = this.fastSwitching.execute(circuit, coreNoAdjacent.getId(), cp);

					// sucesso na realocacao - tentar atender a requisicao
					if (sucess) {

						// nao há necessidade de calcular QoT e QoTO novamente pq o problema dessa
						// requisicao é crosstalk - Não ha mudanca no nucleo da requisicao, por isso QoT
						// e QoTO permanece inalterada

						boolean xt = cp.isAdmissibleCrosstalk(requisicao);

						// xt aceitavel, tenta atender a requisicao iminente de bloqueio
						if (xt) {
							cp.allocateCircuit(requisicao);
							return true;
						}

						break;// para pegar o proximo circuito, ja que o circuito foi realocado
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return false;
	}

	/**
	 * verifica se tem slots em comum entre dois circuitos de nucleos diferentes
	 */
	protected boolean espectroEmComum(Circuit novoC, Circuit c) {

		int[] espectroNovoC = novoC.getSpectrumAssigned();
		int[] espectro = c.getSpectrumAssigned();

		for (int i = espectroNovoC[0]; i <= espectroNovoC[1]; i++) {
			if (i >= espectro[0] && i <= espectro[1]) {
				return true;
			}
		}

		return false;
	}

}
