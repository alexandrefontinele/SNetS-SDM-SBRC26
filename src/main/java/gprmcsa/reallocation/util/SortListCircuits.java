package gprmcsa.reallocation.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import network.Circuit;
import network.ControlPlane;

/**
 * Classe objetiva ordenar circuitos a partir de diferentes criterios
 * 
 * @author gustavo
 *
 */
public class SortListCircuits {

	/**
	 * Construtor
	 */
	public SortListCircuits() {
		//
	}

	/**
	 * ordenando lista segundo algum criterio
	 */
	public void sort(ArrayList<Circuit> circuits, String criterion, ControlPlane cp) {

		Collections.sort(circuits, new Comparator<Circuit>() {
			@Override
			public int compare(Circuit o1, Circuit o2) {
				return auxCost(o1).compareTo(auxCost(o2));
			}

			private Double auxCost(Circuit le) {

				// saltos da rota
				if (criterion.equals("hops")) {
					return (double) le.getRoute().getHops();
				}
				// distancia da rota
				if (criterion.equals("distance")) {
					return le.getRoute().getDistanceAllLinks();
				}
				// valor de crosstalk do circuito
				if (criterion.equals("crosstalk")) {
					return le.getXt();
				}
				// valor do delta crosstalk do circuito
				if (criterion.equals("deltacrosstalk")) {
					return (-1) * (le.getXt() - le.getModulation().getXTthreshold());
				}
				// recurso usado no circuto (numero de slots x saltos da rota)
				return (double) (le.getRoute().getHops() * le.getSpectrumAssigned().length);

			}
		});
	}

	public void sortIndexSlot(ArrayList<Circuit> circuits, String criterion) {

		Collections.sort(circuits, new Comparator<Circuit>() {
			@Override
			public int compare(Circuit o1, Circuit o2) {
				return auxCost(o1).compareTo(auxCost(o2));
			}

			private Integer auxCost(Circuit le) {
				// indice de slot crescente
				if (criterion.equals("indiceslotcrescente")) {
					return le.getSpectrumAssigned()[0];
				}
				// indice de slot decrescente
				if (criterion.equals("indiceslotdecrescente")) {
					return le.getRoute().getLink(0).getCore(0).getNumOfSlots() - le.getSpectrumAssigned()[0];
				}

				// numero de slots do circuito
				return le.getSpectrumAssigned().length;

			}
		});
	}

	private void printCircuitList(ArrayList<Circuit> circuits, ControlPlane cp) {
		String criterio = "deltacrosstalk";
		System.out.println(" CIRCUITOS ");

		if (criterio.equals("hops")) {
			for (Circuit circuit : circuits) {
				System.out.println(circuit.getRoute().getHops());
			}
		}

		if (criterio.equals("distance")) {
			for (Circuit circuit : circuits) {
				System.out.println(circuit.getRoute().getDistanceAllLinks());
			}
		}
		if (criterio.equals("crosstalk")) {
			for (Circuit circuit : circuits) {
				System.out.println(circuit.getXt());
			}
		}
		if (criterio.equals("deltacrosstalk")) {
			for (Circuit circuit : circuits) {
				System.out.println((-1) * (circuit.getXt() - circuit.getModulation().getXTthreshold()));
			}
		}
		if (criterio.equals("recurso")) {
			for (Circuit circuit : circuits) {
				System.out.println(circuit.getRoute().getHops() * circuit.getSpectrumAssigned().length);
			}
		}

	}

	private void printCircuitData(Circuit c) {
		// TODO Auto-generated method stub
		System.out.println("Circuito: " + c);
		System.out.println("Espectro: " + c.getSpectrumAssigned()[0] + "-" + c.getSpectrumAssigned()[1]);
		System.out.println("Modulacao: " + c.getModulation());
		System.out.println("Crosstalk: " + c.getXt());
		System.out.println("Nucleo: " + c.getIndexCore());
		System.out.println("-------------------------");
	}

}
