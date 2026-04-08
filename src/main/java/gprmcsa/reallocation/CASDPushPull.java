package gprmcsa.reallocation;

import java.util.ArrayList;
import java.util.List;

import network.Circuit;
import network.ControlPlane;
import network.Core;
import network.Link;
import util.IntersectionFreeSpectrum;

/**
 * Algoritmo de desfragmentacao de redes opticas multinucleo Retirado do artigo
 * "Empowering Hitless Spectral Defragmentation in Elastic Optical Networks with
 * Spatial Multiplexing"
 * 
 * @author gustavo
 *
 */
public class CASDPushPull implements ReallocationAlgorithmInterface {

	@Override
	public void chooseNewResourcesForSelectedCircuits(ControlPlane cp) {
		// TODO Auto-generated method stub
	}

	@Override
	public void trafficMigration() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean strategy(Circuit requisicaoCircuito, ControlPlane controlPlane) {
		ArrayList<Core> coresSCabaixoLimiar = new ArrayList<>();
		boolean realocacaoSucesso = false;

		// calcular SC de todos os nucleos de todos os enlaces da rede
		for (Link link : controlPlane.getMesh().getLinkList()) {
			for (Core core : link.getCores()) {
				double scCurrent = controlPlane.sc.compute(core);

//				if (core.getCircuitList().isEmpty()) {
//					System.out.println(" entrou aqui");
//				}
				if (core.getCircuitList().size() != 0) {

					if (scCurrent > 0 && scCurrent < 50 && core.getCircuitList().size() != 0) {
						System.out.println(scCurrent);
						System.out.println(core.getCircuitList());
						//lista de circuitos do nucleo
						for (Circuit circuitAtivo : core.getCircuitList()) {
							// System.out.println(circuitAtivo);
							for (int i = 0; i <= 6; i++) {
								if (core.getId() != i) {
									List<int[]> composition = IntersectionFreeSpectrum.merge(circuitAtivo.getRoute(), circuitAtivo.getGuardBand(), i);
									double scAntiga = controlPlane.sc.compute(core);
									System.out.println(scAntiga);

									for (int[] freeBand : composition) {
										if (circuitAtivo.getSpectrumAssigned()[0] >= freeBand[0] && circuitAtivo.getSpectrumAssigned()[1] <= freeBand[1]) {
											// ssdc
											// criando um circuito copia
											Circuit circuitoTeste = new Circuit();
											try {
												circuitoTeste = circuitAtivo.clone();
											} catch (CloneNotSupportedException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											try {
												// releaseSpectrum(circ, circ.getSpectrumAssigned(),
												// circ.getRoute().getLinkList(), circ.getGuardBand());
//												controlPlane.releaseCircuit2(circuitAtivo);
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}

											circuitoTeste.setIndexCore(i);
											try {
												if (controlPlane.isAdmissibleQualityOfTransmission(circuitoTeste)) {
													if (controlPlane.isAdmissibleCrosstalk(circuitoTeste)) {
														if (controlPlane.sc.compute(core) > scAntiga || controlPlane.sc.compute(core)>50) {
															circuitAtivo.setIndexCore(i);
															controlPlane.allocateCircuit(circuitAtivo);
															realocacaoSucesso = true;
															System.out.println("realocou");
															System.out.println(controlPlane.sc.compute(core));

															break;
														} else {
															controlPlane.allocateCircuit(circuitAtivo);
														}
													} else {
														controlPlane.allocateCircuit(circuitAtivo);
													}
												} else {
													controlPlane.allocateCircuit(circuitAtivo);
												}
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}

								if (realocacaoSucesso) {
									realocacaoSucesso = false;
									break;
								}
							}
						}
					}
				}
			}
		}

		System.out.println();

		return false;
	}

	private void ssdc() {
		// TODO Auto-generated method stub
	}

	@Override
	public void selectActivesCircuits(ControlPlane cp, Circuit requisicao) {
		// TODO Auto-generated method stub
	}

}
