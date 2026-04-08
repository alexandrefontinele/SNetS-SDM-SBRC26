package gprmcsa.reallocation;

import java.util.ArrayList;

import gprmcsa.reallocation.tecniques.PushPull;
import gprmcsa.reallocation.util.SortListCircuits;
import gprmcsa.routing.Route;
import network.Circuit;
import network.ControlPlane;
import network.Link;

/**
 * O gatilho é o bloqueio do crosstalk Seleciona os circuitos ativos que estao
 * causando crosstalk no novo circuito a ser estabelecido Realoca o circuito
 * ativo para nucleos não vizinhos do novo circuito A cada realocacao, tenta-se
 * atender o novo circuito Quando o novo circuito for atendido, após
 * realocacoes, o algoritmo é finalizado
 *
 * @author gustavo
 *
 */
public class PushPull_CSBASDM implements ReallocationAlgorithmInterface {

	// uso do arraylist facilita a ordenacao da lista
	private ArrayList<Circuit> selectedCircuits;// circuitos selecionados
	private SortListCircuits sortListCircuits;// classe para ordenacao de lista de circuitos
	private PushPull pushPull;

	public PushPull_CSBASDM() {
		// this.selectedCircuits = new HashSet<>();
		this.sortListCircuits = new SortListCircuits();
		this.pushPull = new PushPull();
	}

	/**
	 * Selecao de circuitos que compartilham os enlaces (nucleo) da rota do circuito
	 * finalizado.
	 */
	@Override
	public void selectActivesCircuits(ControlPlane cp, Circuit circuitoFinalizado) {

		// criar uma lista (zerada de elementos)
		this.selectedCircuits = new ArrayList<>();

		Route rota = circuitoFinalizado.getRoute();
		int indexNucleo = circuitoFinalizado.getIndexCore();
//		System.out.println("Nucleo do circuito finalizado: "+indexNucleo);
//		System.out.println("Demais cicruitos:");;

		// percorrendo enlaces da rota
		for (Link enlace : rota.getLinkList()) {
			// percorrendo lista de circuitos do nucleo - nucleo fixado
			for (Circuit circuit : enlace.getCore(indexNucleo).getCircuitList()) {
				if (!selectedCircuits.contains(circuit)) {

					selectedCircuits.add(circuit);
//					System.out.println(circuit.getIndexCore());

				}
			}
		}

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
	public boolean strategy(Circuit circuitoFinalizado, ControlPlane cp) {
		// selecionar circuitos
		selectActivesCircuits(cp, circuitoFinalizado);
		// lista vazia
		if (this.selectedCircuits.size() == 0) {
			return false;
		}

		int indexNucleo = circuitoFinalizado.getIndexCore();

		if (indexNucleo == 1 || indexNucleo == 3 || indexNucleo == 5) {
			// ordenando a lista de circuito
			sortListCircuits.sortIndexSlot(this.selectedCircuits, "indiceslotcrescente");

//			System.out.println("Lista 1: "+this.selectedCircuits.size());
//			imprimeListaCircuitos();
//			System.out.println("***************************");

			for (Circuit circuit : this.selectedCircuits) {

				try {
					pushPull.executeDown(circuit, cp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (indexNucleo == 2 || indexNucleo == 4 || indexNucleo == 6) {
			// ordenando a lista de circuito
			sortListCircuits.sortIndexSlot(selectedCircuits, "indiceslotdecrescente");

			for (Circuit circuit : this.selectedCircuits) {

				try {
					pushPull.executeUpper(circuit, cp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

//		System.out.println("Terminou");
//		System.out.println();

		return false;

	}

	private void imprimeListaCircuitos(ControlPlane cp) {
		String criterio = "nucleo";
		System.out.println(" CIRCUITOS ");

		if (criterio.equals("hops")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println(circuit.getRoute().getHops());
			}
		}

		if (criterio.equals("distance")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println(circuit.getRoute().getDistanceAllLinks());
			}
		}
		if (criterio.equals("crosstalk")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println(circuit.getXt());
			}
		}
		if (criterio.equals("deltacrosstalk")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println((-1) * (circuit.getXt() - circuit.getModulation().getXTthreshold()));
			}
		}
		if (criterio.equals("recurso")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println(circuit.getRoute().getHops() * circuit.getSpectrumAssigned().length);
			}
		}
		if (criterio.equals("indiceslot")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println(circuit.getSpectrumAssigned()[0]);
			}
		}
		if (criterio.equals("nucleo")) {
			for (Circuit circuit : selectedCircuits) {
				System.out.println(circuit.getIndexCore());
			}
		}

	}

	private void imprimeDadosCircuito(Circuit c) {
		// TODO Auto-generated method stub
		System.out.println("Circuito: " + c);
		System.out.println("Espectro: " + c.getSpectrumAssigned()[0] + "-" + c.getSpectrumAssigned()[1]);
		System.out.println("Modulacao: " + c.getModulation());
		System.out.println("Crosstalk: " + c.getXt());
		System.out.println("Nucleo: " + c.getIndexCore());
		System.out.println("-------------------------");
	}

	private Circuit copiaCircuito(Circuit c) {
		// TODO Auto-generated method stub
		Circuit cCopia = new Circuit();
		
		cCopia.setPair(c.getPair());
		cCopia.setRequests(c.getRequests());

		cCopia.setRoute(c.getRoute());
		cCopia.setModulation(c.getModulation());
		cCopia.setGuardBand(c.getGuardBand());
		cCopia.setIndexCore(c.getIndexCore());
		cCopia.setSpectrumAssigned(c.getSpectrumAssigned());
		
		cCopia.setSNR(c.getSNR());
		cCopia.setQoT(c.isQoT());
		cCopia.setQoTForOther(c.isQoTForOther());
		cCopia.setXt(c.getXt());
		cCopia.setXtAdmissible(c.getXtAdmissible());
		cCopia.setXtAdmissibleInOther(c.getXtAdmissibleInOther());

		return cCopia;

	}

}
