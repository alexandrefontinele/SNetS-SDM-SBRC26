package gprmcsa.reallocation;

import java.io.Serializable;

import network.Circuit;
import network.ControlPlane;


public interface ReallocationAlgorithmInterface extends Serializable {

	/**
	 * Metodo para selecionar os circuitos ativos que serao realocados
	 * @param cp
	 */	
	void selectActivesCircuits(ControlPlane cp, Circuit c);
	
	/**
	 * Escolher novos recursos na rede para realocar os circuitos selecionados
	 * @param cp
	 */
	void chooseNewResourcesForSelectedCircuits(ControlPlane cp);
	
	/**
	 * Migrar o circuito de posicao na rede - novo rmlsa
	 */
	void trafficMigration();
		
	boolean strategy(Circuit requisicaoCircuito, ControlPlane cp);
}
