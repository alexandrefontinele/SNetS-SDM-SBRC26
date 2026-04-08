package util.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TrafficFileCreator {
	
	public static void main(String[] args) throws Exception {
		
		Scanner sc = new Scanner(System.in);
        System.out.println("=== Cria o arquivo de trafego ===");
		
        while (true) {
            System.out.println("\nEscolha a topologia:");
            System.out.println("1) NSFNet");
            System.out.println("2) Cost239");
            System.out.println("3) USA");
            System.out.println("4) Japan");
            System.out.println("5) N8E13");
            System.out.println("0) Sair");
            System.out.print("Opção: ");
            
            int opcao = sc.nextInt();

            if (opcao == 0) {
                System.out.println("Encerrando o programa...");
                break;
            }

            switch (opcao) {
                case 1:
                    System.out.println("Topologia NSFNet");
                    createNSFNetTrafficFile();
                    esperarTecla(sc);
                    break;

                case 2:
                	System.out.println("Topologia Cost239");
                	createCost239TrafficFile();
                    esperarTecla(sc);
                    break;
                
                case 3:
                	System.out.println("Topologia USA");
                	createUSATrafficFile();
                    esperarTecla(sc);
                    break;

                case 4:
                	System.out.println("Topologia Japan");
                	createJapanTrafficFile();
                    esperarTecla(sc);
                    break;
                    
                case 5:
                	System.out.println("Topologia N8E13");
                	createN8E13TrafficFile();
                    esperarTecla(sc);
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }

        sc.close();
	}
	
	/**
     * Espera o usuário pressionar Enter para continuar.
     */
    public static void esperarTecla(Scanner sc) {
        System.out.print("\nPressione ENTER para continuar...");
        sc.nextLine(); // consome o Enter pendente anterior
        sc.nextLine(); // espera novo Enter
    }
	
	public static void createNSFNetTrafficFile() {
		String caminhoArq = "C:\\Users\\alexa\\OneDrive\\Área de Trabalho\\traffic_NSFNet";
		
		int quantNodes = 14; //Quantidade de nodes da topologia
		
		//String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9", "600E+9"};
		//String[] taxas = {"500E+9", "600E+9", "700E+9", "800E+9", "900E+9", "1000E+9"};
		//String[] taxas = {"100E+9", "200E+9", "400E+9", "600E+9", "800E+9", "1000E+9"};
		//String[] taxas = {"200E+9", "400E+9", "600E+9", "800E+9", "1000E+9"};
		String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9"};
		//String[] taxas = {"100E+9", "150E+9", "200E+9", "250E+9", "300E+9", "350E+9", "400E+9"};
		//String[] taxas = {"10E+9", "40E+9", "80E+9", "100E+9", "160E+9", "200E+9", "400E+9"};
		
		//double proporcoes[] = {6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		double proporcoes[] = {5.0, 4.0, 3.0, 2.0, 1.0};
		//double proporcoes[] = {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		
		double ro = 2000.0; //Erlangs, carga inicial
		double incremento = 500.0; //Erlangs, incremento da carga
		double mu = 1.0;
		
		createProportionalTrafficFile(caminhoArq, quantNodes, taxas, proporcoes, ro, incremento, mu);
	}
	
	public static void createCost239TrafficFile() {
		String caminhoArq = "C:\\Users\\alexa\\OneDrive\\Área de Trabalho\\traffic_Cost239";
		
		int quantNodes = 11; //Quantidade de nodes da topologia
		
		//String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9", "600E+9"};
		String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9"};
		//String[] taxas = {"100E+9", "150E+9", "200E+9", "250E+9", "300E+9", "350E+9", "400E+9"};
		//String[] taxas = {"10E+9", "40E+9", "80E+9", "100E+9", "160E+9", "200E+9", "400E+9"};
		
		//double proporcoes[] = {6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		double proporcoes[] = {5.0, 4.0, 3.0, 2.0, 1.0};
		//double proporcoes[] = {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		
		double ro = 500.0; //Erlangs, carga inicial
		double incremento = 200.0; //Erlangs, incremento da carga
		double mu = 1.0;
		
		createProportionalTrafficFile(caminhoArq, quantNodes, taxas, proporcoes, ro, incremento, mu);
	}
	
	public static void createJapanTrafficFile() {
		String caminhoArq = "C:\\Users\\Alexandre Cardoso\\eclipse-workspace\\SNetS_SDM\\simulations\\JPN12_KSP-RC-RF\\traffic";
		
		int quantNodes = 12; //Quantidade de nodes da topologia
		
		String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9"};
		//String[] taxas = {"100E+9", "150E+9", "200E+9", "250E+9", "300E+9", "350E+9", "400E+9"};
		//String[] taxas = {"10E+9", "40E+9", "80E+9", "100E+9", "160E+9", "200E+9", "400E+9"};
		
		double proporcoes[] = {5.0, 4.0, 3.0, 2.0, 1.0};
		//double proporcoes[] = {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		
		double ro = 500.0; //Erlangs, carga inicial
		double incremento = 250.0; //Erlangs, incremento da carga
		double mu = 1.0;
		
		createProportionalTrafficFile(caminhoArq, quantNodes, taxas, proporcoes, ro, incremento, mu);
	}
	
	public static void createUSATrafficFile() {
		String caminhoArq = "C:\\Users\\Alexandre Cardoso\\eclipse-workspace\\SNetS_SDM\\simulations\\USA_KSP-AIDEN_c7\\traffic";
		
		int quantNodes = 24; //Quantidade de nodes da topologia
		
		String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9"};
		//String[] taxas = {"100E+9", "150E+9", "200E+9", "250E+9", "300E+9", "350E+9", "400E+9"};
		//String[] taxas = {"10E+9", "40E+9", "80E+9", "100E+9", "160E+9", "200E+9", "400E+9"};
		
		double proporcoes[] = {5.0, 4.0, 3.0, 2.0, 1.0};
		//double proporcoes[] = {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		
		double ro = 500.0; //Erlangs, carga inicial
		double incremento = 250.0; //Erlangs, incremento da carga
		double mu = 1.0;
		
		createProportionalTrafficFile(caminhoArq, quantNodes, taxas, proporcoes, ro, incremento, mu);
	}
	
	public static void createN8E13TrafficFile() {
		String caminhoArq = "C:\\Users\\alexa\\OneDrive\\Área de Trabalho\\traffic_N8E13";
		
		int quantNodes = 8; //Quantidade de nodes da topologia
		
		String[] taxas = {"100E+9", "200E+9", "300E+9", "400E+9", "500E+9"};
		
		double proporcoes[] = {5.0, 4.0, 3.0, 2.0, 1.0};
		
		double ro = 300.0; //Erlangs, carga inicial
		double incremento = 100.0; //Erlangs, incremento da carga
		double mu = 1.0;
		
		createProportionalTrafficFile(caminhoArq, quantNodes, taxas, proporcoes, ro, incremento, mu);
	}
	
	/**
	 * This method creates the traffic file for the given topology according to the passed parameters.
	 * 
	 * @param caminhoArq String
	 * @param quantNodes int
	 * @param taxas String[]
	 * @param proporcoes double
	 * @param ro double
	 * @param incremento double
	 * @param mu double
	 */
	public static void createProportionalTrafficFile(String caminhoArq, int quantNodes, String[] taxas, double proporcoes[], double ro, double incremento, double mu) {
		
		double quantPares = quantNodes * (quantNodes - 1); //Quantidade de pares de nodes
		double quantTaxas = taxas.length; //Quantidade de taxas de bits
		
		double sumPropor = 0.0;
		for(int i = 0; i < quantTaxas; i++) {
			sumPropor += proporcoes[i];
		}
		
		double lambdaPorPar = (ro / quantPares) * mu;
		double lambdaPorTaxaTotal = lambdaPorPar / sumPropor;
		
		double lambdaPorTaxaPropor[] = new double[(int)quantTaxas];
		for(int i = 0; i < quantTaxas; i++) {
			lambdaPorTaxaPropor[i] = lambdaPorTaxaTotal * proporcoes[i];
		}
		
		double incPorPar = (incremento / quantPares) * mu;
		double incPorTaxaTotal = incPorPar / sumPropor;
		
		double incPorTaxaPropor[] = new double[(int)quantTaxas];
		for(int i = 0; i < quantTaxas; i++) {
			incPorTaxaPropor[i] = incPorTaxaTotal * proporcoes[i];
		}
		
		try {
			FileWriter fw = new FileWriter(caminhoArq);
			BufferedWriter out = new BufferedWriter(fw);
			
			StringBuilder sbaux = new StringBuilder();
			sbaux.append("{\"requestGenerators\":[\n");
			
			int cont = 0;
			for(int i = 1; i <= quantNodes; i++){
				StringBuilder sb = new StringBuilder();
				for(int j = 1; j <= quantNodes; j++){
					if(i != j){
						for(int t = 0; t < quantTaxas; t++) {
							sb.append("{\"source\":\"" + i + "\",\"destination\":\"" + j + "\",\"arrivalRate\":" + lambdaPorTaxaPropor[t] + ",\"arrivalRateIncrease\":" + incPorTaxaPropor[t] + ",\"holdRate\":" + mu + ",\"bitRate\":" + taxas[t] + "}");
							if(cont < ((quantPares * quantTaxas) - 1)){
								sb.append(",\n");
							}else{
								sb.append("\n");
							}
							cont++;
						}
					}
				}
				sbaux.append(sb.toString());
			}
			
			sbaux.append("]}");
			out.append(sbaux.toString());
			//System.out.println(sbaux.toString());
			
			out.close();
			fw.close();
			
			System.out.println("Arquivo de trafego criado.");
			
		} catch (IOException e) {
			e.printStackTrace();
			
			System.out.println("Erro ao criar o arquivo de trafego.");
		}
	}
	
}
