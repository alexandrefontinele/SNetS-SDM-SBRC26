package network;

import gprmcsa.modulation.Modulation;
import gprmcsa.routing.Route;
import request.RequestForConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an established transparent circuit in an optical network
 * 
 * @author Iallen
 */
public class Circuit implements Comparable<Object>, Serializable, Cloneable {
	
	//To identify the type of circuit blocking
	public static final int NO_BLOCKING = 0;
	public static final int BY_LACK_TX = 1;
	public static final int BY_LACK_RX = 2;
	public static final int BY_FRAGMENTATION = 3;
	public static final int BY_QOTN = 4;
	public static final int BY_QOTO = 5;
	public static final int BY_XTN = 6;
	public static final int BY_XTO = 7;
	public static final int BY_OTHER = 8;

	protected static int quantity = 0;
	protected Integer id;
	protected Pair pair;
	protected Route route;
	protected int spectrumAssigned[];
	protected int indexCore; // Core in use
	protected int guardBand;
	protected Modulation modulation;
	protected List<RequestForConnection> requests; // Requests attended by this circuit

	protected double SNR; // dB
	protected double SNR_linear;
	protected boolean QoT;
	protected boolean QoTForOther;
	protected double xt; // dB
	protected double xt_linear;
	protected boolean xtAdmissible;
	protected boolean xtAdmissibleInOther;

	protected boolean wasBlocked = false;
	protected int blockCause;

	protected double launchPowerLinear;
	protected double powerConsumption;

	protected int spectrumUtilizationAbsolut[]; // Para calcular os valores de utilização de espectro para Machine Learning
	protected int spectrumUtilizationWeighted[]; // Para calcular os valores de utilização de espectro para Machine Learning
	protected int spectrumUtilizationInRoute[]; // Para calcular os valores de utilização de espectro para Machine Learning
	protected double averageXT[]; // Para calcular a média de XT para Machine Learning
	
	protected double peso; //Variavel para uso diverso em algoritmos RMCSA
	
	/**
	 * Instantiates a circuit with the list of requests answered by it in empty
	 */
	public Circuit() {
		this.id = quantity++;
		this.requests = new ArrayList<>();

		this.QoT = true; // Assuming that a request always starts with admissible QoT
		this.QoTForOther = true; // Assuming that it is admissible for the other requirements
		this.xtAdmissible = true;
		this.xtAdmissibleInOther = true;
		
		this.launchPowerLinear = Double.POSITIVE_INFINITY;
		this.indexCore = -1; //For core selection error checking
		this.guardBand = -1; //For guard band selection error checking
		
		this.peso = 0.0;
		
		setSNR(PhysicalLayer.maxOSNR);
		setXt(Crosstalk.lowestXT);
	}
	
	/**
	 * This method initializes the information for Machine Learning
	 * 
	 * @param numberOfCores int
	 */
	public void initializesInfoForMachineLearning(int numberOfCores) {
		this.spectrumUtilizationAbsolut = new int[numberOfCores]; // Para calcular os valores de utilização de espectro para Machine Learning
		this.spectrumUtilizationWeighted = new int[numberOfCores]; // Para calcular os valores de utilização de espectro para Machine Learning
		this.spectrumUtilizationInRoute = new int[numberOfCores]; // Para calcular os valores de utilização de espectro para Machine Learning
		this.averageXT = new double[numberOfCores]; // Para calcular a média de XT para Machine Learning
	}

	/**
	 * Returns the source and destination pair of the circuit
	 * 
	 * @return the pair - Pair
	 */
	public Pair getPair() {
		return pair;
	}

	/**
	 * Sets the source and destination pair of the circuit
	 * 
	 * @param pair the pair to set
	 */
	public void setPair(Pair pair) {
		this.pair = pair;
	}

	/**
	 * Returns the route used by the circuit
	 * 
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * Configures the route used by the
	 * 
	 * @param route the route to set
	 */
	public void setRoute(Route route) {
		this.route = route;
	}

	/**
	 * Returns the total bit rate used by the circuit
	 * 
	 * @return the requiredBitRate
	 */
	public double getRequiredBitRate() {
		double res = 0.0;

		for (RequestForConnection r : requests) {
			res += r.getRequiredBitRate();
		}

		return res;
	}

	/**
	 * Compute the residual capacity of circuit in terms of bit rate
	 * 
	 * @return
	 */
	public double getResidualCapacity() {
		double rb = getRequiredBitRate();
		double cap = getModulation().potentialBitRate(spectrumAssigned[1] - spectrumAssigned[0] + 1);
		return cap - rb;
	}

	/**
	 * Returns the source node of the circuit
	 * 
	 * @return
	 */
	public Node getSource() {
		return pair.getSource();
	}

	/**
	 * Returns the destination node of the circuit
	 * 
	 * @return Node
	 */
	public Node getDestination() {
		return pair.getDestination();
	}

	/**
	 * Returns the spectrum band allocated by the circuit
	 * 
	 * @return int[]
	 */
	public int[] getSpectrumAssigned() {
		return spectrumAssigned;
	}

	/**
	 * Configures the spectrum band allocated by the circuit
	 * 
	 * @param sa int[]
	 */
	public void setSpectrumAssigned(int sa[]) {
		if (sa != null && sa[0] > sa[1]) {
			throw new UnsupportedOperationException();
		}
		spectrumAssigned = sa;
	}

	/**
	 * Returns the modulation format used by the circuit
	 * 
	 * @return the modulation - Modulation
	 */
	public Modulation getModulation() {
		return modulation;
	}

	/**
	 * Configures the modulation format used by the circuit
	 * 
	 * @param modulation the modulation to set - Modulation
	 */
	public void setModulation(Modulation modulation) {
		this.modulation = modulation;
	}

	/**
	 * Adds a given request to the list of requests answered by the circuit
	 * 
	 * @param rfc RequestForConnection
	 */
	public void addRequest(RequestForConnection rfc) {
		requests.add(rfc);
	}

	/**
	 * Removes a given request from the list of requests served by the circuit
	 * 
	 * @param rfc
	 */
	public void removeRequest(RequestForConnection rfc) {
		requests.remove(rfc);
	}

	/**
	 * Returns the list of requests answered by the circuit
	 * 
	 * @return List<RequestForConnection>
	 */
	public List<RequestForConnection> getRequests() {
		return requests;
	}
	
	/**
     * Sets the list of requests answered by the circuit
     * 
     * @param requests
     */
    public void setRequests(List<RequestForConnection> requests) {
    	this.requests = requests;
    }
	
	/**
	 * Returns the SNR
	 * 
	 * @return the SNR double
	 */
	public double getSNR() {
		return SNR;
	}

	/**
	 * Sets the SNR
	 * 
	 * @param SNR the SNR to set double
	 */
	public void setSNR(double SNR) {
		this.SNR = SNR;
		this.SNR_linear = PhysicalLayer.ratioOfDB(SNR);
	}

	/**
	 * Returns the QoT
	 * 
	 * @return the QoT boolean
	 */
	public boolean isQoT() {
		return QoT;
	}

	/**
	 * Sets the QoT
	 * 
	 * @param QoT the QoT to set boolean
	 */
	public void setQoT(boolean QoT) {
		this.QoT = QoT;
	}

	/**
	 * Return if circuit is blocked
	 * 
	 * @return boolean
	 */
	public boolean isWasBlocked() {
		return wasBlocked;
	}

	/**
	 * Sets if circuit is blocked
	 * 
	 * @param wasBlocked
	 */
	public void setWasBlocked(boolean wasBlocked) {
		this.wasBlocked = wasBlocked;
	}

	/**
	 * Returns the type of blockage suffered by the circuit
	 * 
	 * @return int
	 */
	public int getBlockCause() {
		return blockCause;
	}

	/**
	 * Sets the type of blockage suffered by the circuit
	 * 
	 * @param blockCause int
	 */
	public void setBlockCause(int blockCause) {
		this.blockCause = blockCause;
	}

	/**
	 * Returns if the QoTO is acceptable or not
	 * 
	 * @return the QoTForOther
	 */
	public boolean isQoTForOther() {
		return QoTForOther;
	}

	/**
	 * Sets if the QoTO is acceptable or not
	 * 
	 * @param qoTForOther the qoTForOther to set
	 */
	public void setQoTForOther(boolean QoTForOther) {
		this.QoTForOther = QoTForOther;
	}

	/**
	 * This method returns the spectrum allocated by the circuit on a link
	 * Can change according to the type of circuit
	 * 
	 * @param link - Link
	 * @return int[]
	 */
	public int[] getSpectrumAssignedByLink(Link link) {
		return getSpectrumAssigned();
	}

	/**
	 * This method that returns the modulation format used in a given route link
	 * Can change according to the type of circuit
	 * 
	 * @param link - Link
	 * @return Modulation
	 */
	public Modulation getModulationByLink(Link link) {
		return getModulation();
	}
	
	/**
	 * This method that returns the index core used in a given route link
	 * Can change according to the type of circuit
	 * 
	 * @param link - Link
	 * @return Modulation
	 */
	public Integer getIndexCoreByLink(Link link) {
		return getIndexCore();
	}

	/**
	 * Returns the power consumption by circuit
	 * 
	 * @return the powerConsumption
	 */
	public double getPowerConsumption() {
		return powerConsumption;
	}

	/**
	 * Sets the power consumption by circuit
	 * 
	 * @param powerConsumption the powerConsumption to set
	 */
	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return int
	 */
	@Override
	public int compareTo(Object o) {
		return this.id.compareTo(((Circuit) o).getId());
	}

	/**
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return this.id * 31;
	}

	/**
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			return this.id == ((Circuit) o).getId();
		}
		return false;
	}

	/**
	 * Returns the total bit rate of the circuit
	 * 
	 * @return double
	 */
	public double getBandwidth() {
		return getModulation().potentialBitRate(spectrumAssigned[1] - spectrumAssigned[0] + 1);
	}

	/**
	 * Returns the launch power
	 * 
	 * @return double
	 */
	public double getLaunchPowerLinear() {
		return launchPowerLinear;
	}

	/**
	 * Sets the launch power
	 * 
	 * @param launchPowerLinear double
	 */
	public void setLaunchPowerLinear(double launchPowerLinear) {
		this.launchPowerLinear = launchPowerLinear;
	}

	/**
	 * Returns the guard band
	 * 
	 * @return int
	 */
	public int getGuardBand() {
		if (guardBand == -1) {
			System.err.println("Guard band not selected.");
		}
		return guardBand;
	}

	/**
	 * Sets the guard band
	 * 
	 * @param guardBand int
	 */
	public void setGuardBand(int guardBand) {
		this.guardBand = guardBand;
	}

	public String toString() {
		return "(s:" + pair.getSource().getName() + " d:" + pair.getDestination().getName() + ")";
	}

	public int getIndexCore() {
		if (indexCore == -1) {
			System.err.println("Core not selected.");
		}
		return indexCore;
	}

	public void setIndexCore(int indexCore) {
		this.indexCore = indexCore;
	}

	public double getXt() {
		return xt;
	}

	public void setXt(double xt) {
		this.xt = xt;
		this.xt_linear = PhysicalLayer.ratioOfDB(xt);
	}

	public void setXtAdmissible(boolean xtAdmissible) {
		this.xtAdmissible = xtAdmissible;
	}

	public boolean getXtAdmissible() {
		return this.xtAdmissible;
	}
 
	public void setXtAdmissibleInOther(boolean xtAdmissibleInOther) {
		this.xtAdmissibleInOther = xtAdmissibleInOther;
	}
	
	public boolean getXtAdmissibleInOther() {
		return this.xtAdmissibleInOther;
	}
	
	public void refreshSpectrumUtilizationAbsolutUnit(int core, int valor) {
		this.spectrumUtilizationAbsolut[core] = valor;
	}

	public int[] getSpectrumUtilizationAbsolut() {
		return spectrumUtilizationAbsolut;
	}

	public void refreshSpectrumUtilizationWeightedUnit(int core, int valor) {
		this.spectrumUtilizationWeighted[core] = valor;
	}

	public int[] getSpectrumUtilizationWeighted() {
		return spectrumUtilizationWeighted;
	}

	public void refreshSpectrumUtilizationInRouteUnit(int core, int valor) {
		this.spectrumUtilizationInRoute[core] = valor;
	}

	public int[] getSpectrumUtilizationInRoute() {
		return spectrumUtilizationInRoute;
	}
	
	public void refreshAverageXt(int core, double valor) {
		this.averageXT[core] = valor;
	}
	
	public double[] getAverageXt() {
		return averageXT;
	}

	// Classe para informar a modulação no padrão de ML, no formato 0 0 0
	// adicionado por Jurandir
	public int[] getModulationML() {
		int[] mod = new int[3];

		if (this.getModulation().getName().equals("BPSK")) {
			mod[0] = 0;
			mod[1] = 0;
			mod[2] = 0;
		}

		if (this.getModulation().getName().equals("QPSK")) {
			mod[0] = 1;
			mod[1] = 0;
			mod[2] = 0;
		}

		if (this.getModulation().getName().equals("8QAM")) {
			mod[0] = 0;
			mod[1] = 1;
			mod[2] = 0;
		}

		if (this.getModulation().getName().equals("16QAM")) {
			mod[0] = 0;
			mod[1] = 0;
			mod[2] = 1;
		}

		if (this.getModulation().getName().equals("32QAM")) {
			mod[0] = 1;
			mod[1] = 1;
			mod[2] = 0;
		}

		if (this.getModulation().getName().equals("64QAM")) {
			mod[0] = 0;
			mod[1] = 1;
			mod[2] = 1;
		}

		return mod;
	}
	
	public double getPeso() {
		return peso;
	}
	
	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public Circuit clone() throws CloneNotSupportedException {
		return (Circuit) super.clone();
	}

	public double getSNRlinear() {
		return SNR_linear;
	}

	public void setSNRlinear(double sNR_linear) {
		this.SNR_linear = sNR_linear;
		this.SNR = PhysicalLayer.ratioForDB(SNR_linear);
	}

	public double getXTlinear() {
		return xt_linear;
	}

	public void setXTlinear(double xt_linear) {
		this.xt_linear = xt_linear;
		this.xt = PhysicalLayer.ratioForDB(xt_linear);
	}

	/**
	 * Ordenar lista de circuito de acordo com algum criterio
	 * 
	 * @param otherCircuit
	 * @param criterio
	 * @return
	 */
//	public int compareTo(Circuit otherCircuit) {
//
//		String criterio = "hops";
//		System.out.println("Entrou aqui");
//		if (criterio.equals("hops")) {
//			if (this.route.getHops() > otherCircuit.getRoute().getHops()) {
//				System.out.println(" Entrou aqui papai" );
//				return -1;
//			}
//			if (this.route.getHops() <= otherCircuit.getRoute().getHops()) {
//				System.out.println(" Entrou aqui mamae" );
//				return 1;
//			}
//		}
//
//		if (criterio.equals("distance")) {
//			if (this.route.getDistanceAllLinks() > otherCircuit.getRoute().getDistanceAllLinks()) {
//				return -1;
//			}
//			if (this.route.getDistanceAllLinks() <= otherCircuit.getRoute().getDistanceAllLinks()) {
//				return 1;
//			}
//		}
//
//		if (criterio.equals("crosstalk")) {
//			if (this.getXt() > otherCircuit.getXt()) {
//				return -1;
//			}
//			if (this.getXt() <= otherCircuit.getXt()) {
//				return 1;
//			}
//		}
//
//		if (criterio.equals("recurso")) {
//			if (this.route.getHops() * this.getSpectrumAssigned().length > otherCircuit.getRoute().getHops()
//					* this.getSpectrumAssigned().length) {
//				return -1;
//			}
//			if (this.route.getHops() * this.getSpectrumAssigned().length <= otherCircuit.getRoute().getHops()
//					* this.getSpectrumAssigned().length) {
//				return 1;
//			}
//		}
//
//		return 0;
//
//	}

}
