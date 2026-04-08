package util.tools;

import java.util.Scanner;

public class SpectralDensityConverter {
	
	/**
     * Converte potência em dBm para watts (W).
     * Fórmula: P(W) = 10^(P_dBm/10) / 1000
     */
    public static double dbmToWatts(double pdBm) {
        return Math.pow(10.0, pdBm / 10.0) / 1000.0;
    }

    /**
     * Converte potência em watts (W) para dBm.
     * Fórmula inversa: P_dBm = 10 * log10(P(W) * 1000)
     */
    public static double wattsToDbm(double watts) {
        return 10.0 * Math.log10(watts * 1000.0);
    }

    /**
     * Converte frequência em GHz para Hz.
     */
    public static double ghzToHz(double freqGHz) {
        return freqGHz * 1e9;
    }

    /**
     * Converte frequência em Hz para GHz.
     */
    public static double hzToGHz(double freqHz) {
        return freqHz / 1e9;
    }

    /**
     * Converte dBm/GHz para W/Hz.
     * Fórmula geral:
     * P(W/Hz) = (10^(P_dBm/10) / 1000) / (freqGHz * 1e9)
     */
    public static double convertDbmPerGHzToWPerHz(double xDbmPerGHz, double freqGHz) {
        double powerW = dbmToWatts(xDbmPerGHz);
        double freqHz = ghzToHz(freqGHz);
        return powerW / freqHz;
    }

    /**
     * Converte W/Hz para dBm/GHz.
     * Fórmula inversa:
     * P_dBm/GHz = 10 * log10(P(W/Hz) * 1000 * (freqGHz * 1e9))
     */
    public static double convertWPerHzToDbmPerGHz(double wPerHz, double freqGHz) {
        double freqHz = ghzToHz(freqGHz);
        double powerW = wPerHz * freqHz;
        return wattsToDbm(powerW);
    }
    
    /**
     * Calcula dBm/GHz a partir de uma potência total em dBm e largura de banda em GHz.
     * Fórmula: dBm/GHz = dBm_total - 10 * log10(B_GHz)
     */
    public static double convertDbmToDbmPerGHz(double pdBmTotal, double bandwidthGHz) {
        return pdBmTotal - 10.0 * Math.log10(bandwidthGHz);
    }

    /**
     * Calcula potência total em dBm a partir de dBm/GHz e largura de banda em GHz.
     * Fórmula inversa: dBm_total = dBm/GHz + 10 * log10(B_GHz)
     */
    public static double convertDbmPerGHzToDbm(double pdBmPerGHz, double bandwidthGHz) {
        return pdBmPerGHz + 10.0 * Math.log10(bandwidthGHz);
    }
    
    /**
     * Espera o usuário pressionar Enter para continuar.
     */
    public static void esperarTecla(Scanner sc) {
        System.out.print("\nPressione ENTER para continuar...");
        sc.nextLine(); // consome o Enter pendente anterior
        sc.nextLine(); // espera novo Enter
    }
	
    /**
     * Exibe o menu principal e gerencia a interação com o usuário.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Conversor entre dBm, dBm/GHz e W/Hz ===");

        while (true) {
            System.out.println("\nEscolha a conversão desejada:");
            System.out.println("1) dBm/GHz → W/Hz");
            System.out.println("2) W/Hz → dBm/GHz");
            System.out.println("3) Converter dBm + largura de banda (GHz) → dBm/GHz");
            System.out.println("4) Converter dBm/GHz + largura de banda (GHz) → dBm total");
            System.out.println("0) Sair");
            System.out.print("Opção: ");
            
            int opcao = sc.nextInt();

            if (opcao == 0) {
                System.out.println("Encerrando o programa...");
                break;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor em dBm/GHz: ");
                    double dbmPerGHz = sc.nextDouble();
                    System.out.print("Digite a largura de banda em GHz (ex: 1): ");
                    double freqGHz1 = sc.nextDouble();
                    double resultWPerHz = convertDbmPerGHzToWPerHz(dbmPerGHz, freqGHz1);
                    System.out.printf("Resultado: %.6e W/Hz%n", resultWPerHz);
                    esperarTecla(sc);
                    break;

                case 2:
                    System.out.print("Digite o valor em W/Hz: ");
                    double wPerHz = sc.nextDouble();
                    System.out.print("Digite a largura de banda em GHz (ex: 1): ");
                    double freqGHz2 = sc.nextDouble();
                    double resultDbmPerGHz = convertWPerHzToDbmPerGHz(wPerHz, freqGHz2);
                    System.out.printf("Resultado: %.6f dBm/GHz%n", resultDbmPerGHz);
                    esperarTecla(sc);
                    break;
                
                case 3:
                    System.out.print("Informe a potência total (dBm): ");
                    double pdBm = sc.nextDouble();
                    System.out.print("Informe a largura de banda (GHz): ");
                    double bwGHz1 = sc.nextDouble();
                    double dbmPerGHz1 = convertDbmToDbmPerGHz(pdBm, bwGHz1);
                    System.out.printf("Resultado: %.6f dBm/GHz%n", dbmPerGHz1);
                    esperarTecla(sc);
                    break;

                case 4:
                    System.out.print("Informe o valor em dBm/GHz: ");
                    double dbmPerGHz2 = sc.nextDouble();
                    System.out.print("Informe a largura de banda (GHz): ");
                    double bwGHz2 = sc.nextDouble();
                    double dbmTotal = convertDbmPerGHzToDbm(dbmPerGHz2, bwGHz2);
                    System.out.printf("Resultado: %.6f dBm total%n", dbmTotal);
                    esperarTecla(sc);
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }

        sc.close();
    }
    
}
