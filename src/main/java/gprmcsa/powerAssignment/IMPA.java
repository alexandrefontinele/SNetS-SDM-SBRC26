package gprmcsa.powerAssignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;

import gprmcsa.modulation.Modulation;
import gprmcsa.routing.Route;
import network.Circuit;
import network.ControlPlane;
import network.Core;
import network.Link;
import network.PhysicalLayer;

public class IMPA implements PowerAssignmentAlgorithmInterface {
	
	private Double marginOSNR;
	private Double marginXT;

	@Override
	public double assignLaunchPower(Circuit circuit, Route route, Modulation modulation, int core, int[] spectrumAssigned, ControlPlane cp) {
		
		if(marginOSNR == null){
			Map<String, String> uv = cp.getMesh().getOthersConfig().getVariables();
			if(uv.get("marginOSNR") != null) {
				marginOSNR = Double.parseDouble((String)uv.get("marginOSNR"));
			}
			
			if(marginOSNR == null) {
				System.out.println("The OSNR margin was not found. Using the default value.");
				marginOSNR = 1.0;
			}
		}
		
		if(marginXT == null){
			Map<String, String> uv = cp.getMesh().getOthersConfig().getVariables();
			if(uv.get("marginXT") != null) {
				marginXT = Double.parseDouble((String)uv.get("marginXT"));
			}
			
			if(marginXT == null) {
				System.out.println("The XT margin was not found. Using the default value.");
				marginXT = 1.0;
			}
		}
		
		//For the PABS algorithm to work correctly the PSD must be variable.
		if (cp.getMesh().getPhysicalLayer().getFixedPowerSpectralDensity()) {
			cp.getMesh().getPhysicalLayer().setFixedPowerSpectralDensity(false);
		}
		
		double launchPower = ImpairmentAwareMarginPowerAssignment(circuit, route, modulation, core, spectrumAssigned, marginOSNR, marginXT, cp);
		circuit.setLaunchPowerLinear(launchPower);
		
		return launchPower;
	}
	
	/**
	 * This method applies the NewAlgo power assignment strategy.
	 * 
	 * @param circuit Circuit
	 * @param route Route
	 * @param modulation Modulation
	 * @param core int
	 * @param spectrumAssigned int[]
	 * @param cp ControlPlane
	 * @return double
	 */
    public double ImpairmentAwareMarginPowerAssignment(Circuit circuit, Route route, Modulation modulation, int core, int spectrumAssigned[], double marginOSNR, double marginXT, ControlPlane cp){
    	
    	//To avoid mistakes
		circuit.setRoute(route);
		circuit.setModulation(modulation);
		circuit.setIndexCore(core);
		circuit.setSpectrumAssigned(spectrumAssigned);
    	
		double OSNRcurrent = 0.0;
		double XTcurrent = 0.0;
		
		//double OSNRth = modulation.getSNRthresholdLinear(); //
		//OSNRth = OSNRth + this.marginO; //margin is linear
		
		double OSNRth_dB = modulation.getSNRthreshold();
		OSNRth_dB = OSNRth_dB + marginOSNR;
		double OSNRth = PhysicalLayer.ratioOfDB(OSNRth_dB);
		
		//double XTth = modulation.getXTthresholdLinear(); //Linear
		//XTth = XTth - this.marginX; //margin is linear
		
		double XTth_dB = modulation.getXTthreshold();
		XTth_dB = XTth_dB - marginXT;
		double XTth = PhysicalLayer.ratioOfDB(XTth_dB);
		
		double Plow = 1.0E-11; //W, -80 dBm
		double Pmax = computePmax(circuit, cp);
		double Pmin = computePmin(circuit, Plow, Pmax, cp);
		
		
        
        List<Double> P_candidates = generateCandidatePowers(circuit, Pmin, Pmax, cp);

        // Retorna a média geométrica caso nenhum candidato válido apareça
        Double bestP = Math.sqrt(Pmin * Pmax);
        
        for (double P : P_candidates) {
        	
        	circuit.setLaunchPowerLinear(P); // Configura a nova potência no cirucito em analise
        	
        	//====================================================
        	// QoT do próprio circuito (OSNR)
        	OSNRcurrent = computeOSNR(circuit, cp, null, false);
            if (OSNRcurrent < OSNRth) {
            	continue; // bloqueio QoTN
            }
            
            //====================================================
            // XT do próprio circuito (XTN)
            XTcurrent = computeXT(circuit, cp, null, false);
            if (XTcurrent > XTth) {
            	continue; // bloqueio por XT
            }
            
            //====================================================
            // OSNR nos circuitos vizinhos (QoTO via OSNR)
            OSNRNeighborInfo infoOSNR = computeOSNRNeighborInfo(circuit, cp);
            if (infoOSNR.violatesThreshold) {
            	continue; // pior vizinho estourou margem OSNR
            }
            
            //====================================================
            // XT nos circuitos adjacente (QoTO via XT)
            XTNeighborInfo infoXT = computeXTNeighborsInfo(circuit, cp);
            if (infoXT.violatesThreshold) {
                continue; // algum vizinho excedeu limiar de XT
            }
            
            //Se chegou até aqui para e retorna a potência
        	bestP = P;
        	break;
        }
        
        return bestP;
	}
    
    /**
     * Estrutura para retorno combinado da análise de vizinhos
     */
    public static class OSNRNeighborInfo {
        public double minOSNRmargin;
        public double maxOSNRth;
        public boolean violatesThreshold;
        
        public OSNRNeighborInfo(double minOSNRmargin, double maxOSNRth, boolean violatesThreshold) {
            this.minOSNRmargin = minOSNRmargin;
            this.maxOSNRth = maxOSNRth;
            this.violatesThreshold = violatesThreshold;
        }
    }
    
    /**
     * Função combinada: pior vizinho + pior threshold
     * 
     * @param circuit Circuit
     * @param cp ControlPlane
     * @return NeighborInfo
     */
    public OSNRNeighborInfo computeOSNRNeighborInfo(Circuit circuit, ControlPlane cp) {
        HashSet<Circuit> neighbors = new HashSet<Circuit>(); // Circuit list for test
		
		// Search for all circuits that have links in common with the circuit under evaluation
		Route route = circuit.getRoute();
	    for (Link link : route.getLinkList()) {
	    	// Picks up the active circuits that use the link
	        for (Circuit circuitTemp : link.getCore(circuit.getIndexCore()).getCircuitList()) {
	        	// If the circuit is different from the circuit under evaluation and is not in the circuit list for test
	        	if (!circuit.equals(circuitTemp) && !neighbors.contains(circuitTemp)) {
	                neighbors.add(circuitTemp);
	        	}
	        }
	    }
	    
	    // Se NÃO EXISTEM vizinhos
	    if (neighbors.isEmpty()) {
	        return new OSNRNeighborInfo(0.0, 1.0, false); // (minOSNRmargin = neutro, maxOSNRth = neutro, nenhum vizinho violou threshold)
	    }
		
		double minOSNRmargin = Double.POSITIVE_INFINITY;
		double maxOSNRth = 0.0;
		boolean violates = false;
		
		// Tests the QoT of circuits
		for (Circuit neighbor : neighbors) {
			
			//Test this way so as not to alter the SNR and QoT of the circuit under evaluation.
			double OSNR = computeOSNR(neighbor, cp, circuit, true);
			double OSNRthreshold = neighbor.getModulation().getSNRthresholdLinear();
			double OSNRmargin = OSNR - OSNRthreshold;
			
			// Pior margem (mais negativa)
			if (OSNRmargin < minOSNRmargin) {
                minOSNRmargin = OSNRmargin;
			}
			
			// maior threshold (para normalização)
            if (OSNRthreshold > maxOSNRth) {
            	maxOSNRth = OSNRthreshold;
            }
            
            // Verifica violação
            if (OSNRmargin < 0.0) {
                violates = true;
            }
		}
		
		// segurança numérica
		if (maxOSNRth == 0.0) {
			maxOSNRth = 1.0; 
		}

        return new OSNRNeighborInfo(minOSNRmargin, maxOSNRth, violates);
    }
    
    /**
     * Estrutura para retorno combinado da análise de XT nos vizinhos.
     */
    public static class XTNeighborInfo {
        public double minQuality;
        public boolean violatesThreshold;

        public XTNeighborInfo(double minQuality, boolean violatesThreshold) {
            this.minQuality = minQuality;
            this.violatesThreshold = violatesThreshold;
        }
    }
    
    /**
     * Analisa o XT que o circuito 'circuit' causa em seus vizinhos.
     * 
     * @param circuit
     * @param lambdaSmooth
     * @param cp
     * @return XTNeighborInfo
     */
    public XTNeighborInfo computeXTNeighborsInfo(Circuit circuit, ControlPlane cp) {
    	TreeSet<Circuit> neighbors = new TreeSet<Circuit>();

        Route route = circuit.getRoute();
        for(Link link: route.getLinkList()) {
			ArrayList<Core> adjacentsCores = link.getAdjacentCores(circuit.getIndexCore());			
			for(Core core : adjacentsCores) {
				for(Circuit adjacentCircuit : core.getCircuitList()) {
					if(cp.getMesh().getPhysicalLayer().getCrosstalk().isIntersection(circuit.getSpectrumAssigned(), adjacentCircuit.getSpectrumAssigned())) {
						if(!neighbors.contains(adjacentCircuit)) {
							neighbors.add(adjacentCircuit);
						}
					}
				}
			}
		}

        // Caso não existam vizinhos: XT não é um fator aqui.
        if (neighbors.isEmpty()) {
            return new XTNeighborInfo(1.0, false); // (minQuality: melhor possível, ninguém viola nada)
        }

        double minQuality = Double.POSITIVE_INFINITY;
        boolean violates = false;

        for (Circuit neighbor : neighbors) {

        	// XT que o vizinho sofre por causa do circuito avaliado
            double XTk = computeXT(neighbor, cp, circuit, true);
            double XTth_k = neighbor.getModulation().getXTthresholdLinear();

            if (XTk > XTth_k) {
                violates = true;
            }

            // qualidade XT normalizada (1 = ótimo, 0 = crítico)
            double qk = 1.0 - (XTk / XTth_k);

            // clamp em [0,1]
            if (qk < 0.0) qk = 0.0;
            if (qk > 1.0) qk = 1.0;
            
            // menor qualidade
            if (qk < minQuality) {
                minQuality = qk;
            }
        }

        return new XTNeighborInfo(minQuality, violates);
    }
    
    /**
     * Função que gera uma lista de potências candidatas.
     * A lista de potências é gerada usando as estratégias Log-Uniforme e Linear Uniforme.
     * 
     * @param Pmin double
     * @param Pmax double
     * @return List<Double>
     */
    public List<Double> generateCandidatePowers(Circuit circuit, double Pmin, double Pmax, ControlPlane cp) {
    	//Lista de potências candidatas
    	List<Double> candidates = new ArrayList<>();
    	
    	// -----------------------------------------------------
        // (1) Obter carga da rede ρ (valores entre 0 e 1)
    	// passamos w=0.7 como padrão para priorizar caminho (rota).
    	// -----------------------------------------------------
    	double rho = measureNetworkLoad(circuit, 0.7, cp);
    	
    	// -----------------------------------------------------
        // (2) Cálculo automático da quantidade total de amostras
    	//    Depende de:
        //    - Amplitude do intervalo de potência (ordens de grandeza)
        //    - Carga da rede (rho)
    	// -----------------------------------------------------
    	double ratio = Pmax / Pmin;
        double orders = Math.log10(ratio);

        int N = (int)(10 + 3 * orders + 10 * rho);
        
        // Limites de segurança (para não exagerar em rede muito pesada)
        if (N < 12) N = 12;
        if (N > 40) N = 40;

        // Divisão entre log-uniforme e uniforme
        int Nlog  = N / 2;
        int Nunif = N - Nlog;

        // Segurança
        if (Nlog < 4)  Nlog  = 4;
        if (Nunif < 4) Nunif = 4;

        // ----------------------------------
        // 3) Amostragem Log-Uniforme
        //    Para baixa potência e regiões críticas
        // ----------------------------------
        for (int i = 0; i < Nlog; i++) {
            double ratioLog = (double)i / (Nlog - 1);
            double P = Pmin * Math.pow(Pmax / Pmin, ratioLog);
            candidates.add(P);
        }

        // ----------------------------------
        // 4) Amostragem Linear Uniforme
        //    Para cobrir todo intervalo
        // ----------------------------------
        for (int i = 0; i < Nunif; i++) {
            double P = Pmin + i * (Pmax - Pmin) / (Nunif - 1);
            candidates.add(P);
        }

        // ----------------------------------
        // 5) Garantir extremos
        // ----------------------------------
        candidates.add(Pmin);
        candidates.add(Pmax);
    	
        // ----------------------------------
        // 6) Remover duplicatas + ordenar
        // ----------------------------------
        List<Double> finalCandidates = candidates.stream().distinct().sorted().collect(Collectors.toList());
        
        return finalCandidates;
	}
    
     
    /**
     * Cálculo da carga da rede ρ ∈ [0,1]
     * 
     * @param circuit  circuit
     * @param w double
     * @param cp ControlPlane
     * @return double
     */
    public double measureNetworkLoad(Circuit circuit, double w, ControlPlane cp) {
        
    	Vector<Link> allLinks = cp.getMesh().getLinkList();
    	Vector<Link> routeLinks = circuit.getRoute().getLinkList();
    	int circuitCore = circuit.getIndexCore();
    	
    	double usedGlobal = 0.0;
    	double totalGlobal = 0.0;
    	double utGlobal = 0.0;
    	for (Link tLink : allLinks) {
			usedGlobal += tLink.getCore(circuitCore).getUsedSlots();
			totalGlobal += tLink.getCore(circuitCore).getNumOfSlots();
    	}
    	
    	if (totalGlobal > 0.0) {
    		utGlobal = usedGlobal / totalGlobal;
    	}
    	
    	double usedRoute = 0.0;
    	double totalRoute = 0.0;
    	double utLocal = 0.0;
		for (Link rLink : routeLinks) {
			usedRoute += rLink.getCore(circuitCore).getUsedSlots();
			totalRoute += rLink.getCore(circuitCore).getNumOfSlots();
		}
		
		if (totalRoute > 0.0) {
			utLocal = usedRoute / totalRoute;
		}
    	
		// Utilizacao da média ponderada
		double p = w * utLocal + ((1.0 - w) * utGlobal);
    	
        return p;
    }
    
    /**
     * Cálculo da potência minima
     * 
     * @param circuit Circuit
     * @param P_low double
     * @param P_high double
     * @param cp ControlPlane
     * @return double
     */
    public double computePmin(Circuit circuit, double P_low, double P_high, ControlPlane cp) {

    	//===========================================================
        // Parâmetros
        //===========================================================
        double OSNRtarget = circuit.getModulation().getSNRthresholdLinear();
        double tolP       = 1e-6;   // tolerância na potência
        double tolOSNR    = 1e-3;   // tolerância na margem OSNR
        int ITER          = 40;     // iterações

        double L = P_low;
        double R = P_high;

        //===========================================================
        // (1) Teste rápido: P_low já atinge o limiar?
        //===========================================================
        circuit.setLaunchPowerLinear(L);
        double OSNR_L = computeOSNR(circuit, cp, null, false);

        if (!Double.isNaN(OSNR_L) && OSNR_L >= OSNRtarget) {
            return L; // Já satisfaz o limiar -> retorna o menor P possível
        }

        //===========================================================
        // (2) Teste rápido: mesmo com P_high não atende?
        //===========================================================
        circuit.setLaunchPowerLinear(R);
        double OSNR_R = computeOSNR(circuit, cp, null, false);

        if (Double.isNaN(OSNR_R) || OSNR_R < OSNRtarget) {
            return R; // Nem com a potência máxima atingimos o OSNR necessário.
        }

        //===========================================================
        // (3) Busca binária
        //===========================================================
        double lastGood = R; // última potência válida encontrada

        for (int i = 0; i < ITER; i++) {

            double Pmid = (L + R) * 0.5;

            circuit.setLaunchPowerLinear(Pmid);
            double osnrMid = computeOSNR(circuit, cp, null, false);

            // Segurança contra NaN, infinito ou valores esquisitos
            if (Double.isNaN(osnrMid) || Double.isInfinite(osnrMid)) {
                // assume que Pmid não é válida -> aumenta potência
                L = Pmid;
                continue;
            }

            double margin = osnrMid - OSNRtarget;

            if (margin >= 0.0) {
                // Pmid satisfaz o limiar -> salvar como "boa"
                lastGood = Pmid;

                // tentar reduzir potência
                R = Pmid;

                if (Math.abs(margin) < tolOSNR) {
                    break; // convergência numérica da margem
                }
            } else {
                // Pmid insuficiente -> aumentar potência
                L = Pmid;
            }
            
            if (Math.abs(R - L) < tolP) {
                break; // convergiu na potência
            }
        }

        //===========================================================
        // (4) Retornar a melhor estimativa encontrada
        //===========================================================
        return lastGood;
    }
    
    /**
     * Cálculo da potência maxima
     * 
     * @param circuit Circuit
     * @param cp ControlPlane
     * @return double
     */
    public double computePmax(Circuit circuit, ControlPlane cp) {
    	double Pmax = cp.getMesh().getPhysicalLayer().computeMaximumPower(circuit, circuit.getRequiredBitRate(), circuit.getRoute(), 0, circuit.getRoute().getNodeList().size() - 1, 
				circuit.getModulation(), circuit.getIndexCore(), circuit.getSpectrumAssigned());
    	return Pmax;
    }
    
    /**
     * Computa o OSNR do circuito informado
     * 
     * @param circuit Circuit
     * @param cp ControlPlane
     * @param testCircuit Circuit
     * @param addTestCircuit boolean
     * @return double
     */
    public double computeOSNR(Circuit circuit, ControlPlane cp, Circuit testCircuit, boolean addTestCircuit) {
    	double osnr = cp.getMesh().getPhysicalLayer().computeSNRSegment(circuit, circuit.getRoute(), 0, circuit.getRoute().getNodeList().size() - 1, 
				circuit.getModulation(), circuit.getIndexCore(), circuit.getSpectrumAssigned(), testCircuit, addTestCircuit);
    	return osnr;
    }
    
    /**
     * Computa o XT do circuito informado
     * 
     * @param circuit Circuit
     * @param cp ControlPlane
     * @param testCircuit Circuit
     * @param addTestCircuit boolean
     * @return double
     */
    public double computeXT(Circuit circuit, ControlPlane cp, Circuit testCircuit, boolean addTestCircuit) {
    	double xt = cp.getMesh().getPhysicalLayer().getCrosstalk().calculateCrosstalk(circuit, testCircuit, addTestCircuit);
    	xt = PhysicalLayer.ratioOfDB(xt);
    	return xt;
    }
    
    public int getOverlaps(Circuit cicuit, ControlPlane cp) {
    	int overlaps = cp.getMesh().getPhysicalLayer().getCrosstalk().numberSlotsOverlapping(cicuit, cicuit.getRoute(), cicuit.getModulation(), cicuit.getIndexCore(), cicuit.getSpectrumAssigned());
    	return overlaps;
    }
}
