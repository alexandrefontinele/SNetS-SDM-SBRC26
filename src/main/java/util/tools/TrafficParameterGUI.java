package util.tools;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class TrafficParameterGUI extends JFrame {
	
    private JTextField filePathField;
    private JButton browseButton;
    private JButton analyzeButton;
    private JTextArea resultArea;
    private JLabel statusLabel;
    
    public TrafficParameterGUI() {
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initComponents() {
        setTitle("Analisador de Parâmetros de Tráfego");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null);
        
        filePathField = new JTextField(30);
        filePathField.setEditable(false);
        
        browseButton = new JButton("Procurar");
        analyzeButton = new JButton("Analisar Arquivo");
        analyzeButton.setEnabled(false);
        
        resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(new Color(240, 240, 240));
        
        statusLabel = new JLabel("Selecione um arquivo de tráfego para analisar");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior - seleção de arquivo
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBorder(BorderFactory.createTitledBorder("Seleção do Arquivo"));
        filePanel.add(new JLabel("Arquivo:"));
        filePanel.add(filePathField);
        filePanel.add(browseButton);
        filePanel.add(analyzeButton);
        
        // Painel central - resultados
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Parâmetros Identificados"));
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        // Painel inferior - status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        
        // Adicionar painéis ao frame
        add(filePanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        // Adicionar margens
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void setupListeners() {
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseForFile();
            }
        });
        
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeFile();
            }
        });
    }
    
    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo de tráfego");
        
        // Aceitar qualquer tipo de arquivo
        fileChooser.setAcceptAllFileFilterUsed(true);
        
        // Opcional: ainda mostrar JSON como preferência, mas aceitar todos
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos JSON", "json"));
        //fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Todos os arquivos", "*"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            filePathField.setText(filePath);
            analyzeButton.setEnabled(true);
            statusLabel.setText("Arquivo selecionado: " + fileChooser.getSelectedFile().getName());
            resultArea.setText(""); // Limpar resultados anteriores
        }
    }
    
    private void analyzeFile() {
        String filePath = filePathField.getText();
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um arquivo primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mostrar indicador de carregamento
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        analyzeButton.setEnabled(false);
        statusLabel.setText("Analisando arquivo...");
        
        // Executar análise em thread separada para não travar a interface
        SwingWorker<TrafficParameters, Void> worker = new SwingWorker<TrafficParameters, Void>() {
            @Override
            protected TrafficParameters doInBackground() throws Exception {
                return extractParametersFromTrafficFile(filePath);
            }
            
            @Override
            protected void done() {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                analyzeButton.setEnabled(true);
                
                try {
                    TrafficParameters params = get();
                    if (params != null) {
                        displayResults(params);
                        statusLabel.setText("Análise concluída com sucesso!");
                    } else {
                        resultArea.setText("Erro: Não foi possível extrair os parâmetros do arquivo.\n\n"
                                + "Possíveis causas:\n"
                                + "- Formato do arquivo não suportado\n"
                                + "- Estrutura JSON inválida\n"
                                + "- Arquivo corrompido\n"
                                + "- Encoding incorreto");
                        statusLabel.setText("Erro na análise do arquivo");
                    }
                } catch (Exception ex) {
                    resultArea.setText("Erro durante a análise:\n" + ex.getMessage() + "\n\n"
                            + "Verifique se o arquivo:\n"
                            + "- Tem formato JSON válido\n"
                            + "- Contém a estrutura 'requestGenerators'\n"
                            + "- Não está corrompido");
                    statusLabel.setText("Erro na análise do arquivo");
                    ex.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    private void displayResults(TrafficParameters params) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PARÂMETROS IDENTIFICADOS ===\n\n");
        
        sb.append("Quantidade de Nós: ").append(params.getQuantNodes()).append("\n\n");
        
        sb.append("Taxas de Transmissão:\n");
        double[] taxas = params.getTaxas();
        for (int i = 0; i < taxas.length; i++) {
            sb.append("  - ").append(formatBitRate(taxas[i])).append("\n");
        }
        sb.append("\n");
        
        sb.append("Proporções (normalizadas):\n");
        double[] proporcoes = params.getProporcoes();
        for (int i = 0; i < proporcoes.length; i++) {
            sb.append("  - ").append(formatBitRate(taxas[i])).append(": ").append(String.format("%.2f", proporcoes[i])).append("\n");
        }
        sb.append("\n");
        
        sb.append("Parâmetros de Tráfego:\n");
        sb.append("  - Ro (Carga inicial - Erlangs): ").append(String.format("%.2f", params.getRo())).append("\n");
        sb.append("  - Incremento da carga (Erlangs): ").append(String.format("%.2f", params.getIncremento())).append("\n");
        sb.append("  - Mu (Taxa de retenção): ").append(params.getMu()).append("\n");
        
        sb.append("\n=== RESUMO ===\n");
        sb.append("Arquivo analisado: ").append(filePathField.getText()).append("\n");
        sb.append("Total de pares de nós: ").append(params.getQuantNodes() * (params.getQuantNodes() - 1)).append("\n");
        sb.append("Quantidade de taxas diferentes: ").append(taxas.length).append("\n");
        
        resultArea.setText(sb.toString());
    }
    
    private String formatBitRate(double bitRate) {
        if (bitRate >= 1e9) {
            return String.format("%.0f Gbps", bitRate / 1e9);
        } else if (bitRate >= 1e6) {
            return String.format("%.0f Mbps", bitRate / 1e6);
        } else if (bitRate >= 1e3) {
            return String.format("%.0f Kbps", bitRate / 1e3);
        } else {
            return String.format("%.0f bps", bitRate);
        }
    }
    
    // Método de extração de parâmetros atualizado para detectar automaticamente o formato
    public static TrafficParameters extractParametersFromTrafficFile(String caminhoArq) {
        try {
            FileReader fr = new FileReader(caminhoArq);
            BufferedReader br = new BufferedReader(fr);
            
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            
            br.close();
            fr.close();
            
            String content = jsonContent.toString().trim();
            
            // Detectar automaticamente se é JSON
            if (isValidJSON(content)) {
                return parseJSONContent(content);
            } else {
                // Tentar outros formatos no futuro
                throw new IOException("Formato de arquivo não suportado. Apenas JSON é aceito no momento.");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Método para verificar se o conteúdo é JSON válido
    private static boolean isValidJSON(String content) {
        try {
            new JSONObject(content);
            return true;
        } catch (Exception e1) {
            try {
                new JSONArray(content);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }
    
    // Método para parsear conteúdo JSON
    private static TrafficParameters parseJSONContent(String content) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONArray requestGenerators = jsonObject.getJSONArray("requestGenerators");
            
            Set<Integer> nodes = new HashSet<>();
            Set<Double> taxasSet = new HashSet<>();
            
            List<Double> lambdaValues = new ArrayList<>();
            List<Double> incValues = new ArrayList<>();
            List<Double> taxasList = new ArrayList<>();
            
            double mu = 0;
            boolean muSet = false;
            
            for (int i = 0; i < requestGenerators.length(); i++) {
                JSONObject generator = requestGenerators.getJSONObject(i);
                
                int source = generator.getInt("source");
                int destination = generator.getInt("destination");
                double arrivalRate = generator.getDouble("arrivalRate");
                double arrivalRateIncrease = generator.getDouble("arrivalRateIncrease");
                double holdRate = generator.getDouble("holdRate");
                double bitRate = generator.getDouble("bitRate");
               
                nodes.add(source);
                nodes.add(destination);
                taxasSet.add(bitRate);
                
                if (!muSet) {
                    mu = holdRate;
                    muSet = true;
                }
                
                lambdaValues.add(arrivalRate);
                incValues.add(arrivalRateIncrease);
                taxasList.add(bitRate);
            }
            
            int quantNodes = nodes.size();
            double quantPares = quantNodes * (quantNodes - 1);
            
            List<Double> taxasUnicas = new ArrayList<>(taxasSet);
            Collections.sort(taxasUnicas);
            
            List<Double> somaPorTaxa = new ArrayList<>();
            
            for (Double taxa : taxasUnicas) {
                double sumLambdaForTaxa = 0;
                for (int i = 0; i < taxasList.size(); i++) {
                    if (taxasList.get(i).equals(taxa)) {
                        sumLambdaForTaxa += lambdaValues.get(i);
                    }
                }
                somaPorTaxa.add(sumLambdaForTaxa);
            }
            
            double minSoma = Double.MAX_VALUE;
            for (double soma : somaPorTaxa) {
                if (soma > 0 && soma < minSoma) {
                    minSoma = soma;
                }
            }
            
            if (minSoma == Double.MAX_VALUE) {
                minSoma = 1.0;
            }
            
            double[] proporcoes = new double[somaPorTaxa.size()];
            for (int i = 0; i < somaPorTaxa.size(); i++) {
                proporcoes[i] = somaPorTaxa.get(i) / minSoma;
            }
            
            double sumPropor = 0;
            for (double prop : proporcoes) {
                sumPropor += prop;
            }
            
            double[] taxas = new double[taxasUnicas.size()];
            for (int i = 0; i < taxasUnicas.size(); i++) {
                taxas[i] = taxasUnicas.get(i);
            }
            
            double lambdaRef = 0;
            double incRef = 0;
            for (int i = 0; i < requestGenerators.length(); i++) {
                JSONObject generator = requestGenerators.getJSONObject(i);
                double bitRate = generator.getDouble("bitRate");
                
                if (bitRate == taxas[0]) {
                    lambdaRef = generator.getDouble("arrivalRate");
                    incRef = generator.getDouble("arrivalRateIncrease");
                    break;
                }
            }
            
            double lambdaPorTaxaTotal = lambdaRef / proporcoes[0];
            double lambdaPorPar = lambdaPorTaxaTotal * sumPropor;
            double ro = (lambdaPorPar * quantPares) / mu;
            
            double incPorTaxaTotal = incRef / proporcoes[0];
            double incPorPar = incPorTaxaTotal * sumPropor;
            double incremento = (incPorPar * quantPares) / mu;
            
            return new TrafficParameters(quantNodes, taxas, proporcoes, ro, incremento, mu);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Método robusto para parsear bitRate de diferentes formatos
    private static double parseBitRate(String bitRateStr) {
        try {
            // Remover espaços e converter para maiúsculas
            String cleaned = bitRateStr.trim().toUpperCase();
            
            // Tentar parse direto
            try {
                return Double.parseDouble(cleaned);
            } catch (NumberFormatException e) {
                // Ignorar e tentar outros formatos
            }
            
            // Tentar com notação científica
            if (cleaned.contains("E")) {
                return Double.parseDouble(cleaned.replace("E+", "E"));
            }
            
            // Tentar com sufixos comuns
            if (cleaned.endsWith("G") || cleaned.endsWith("GBPS")) {
                String numStr = cleaned.replace("G", "").replace("BPS", "").replace("GBPS", "");
                return Double.parseDouble(numStr) * 1e9;
            } else if (cleaned.endsWith("M") || cleaned.endsWith("MBPS")) {
                String numStr = cleaned.replace("M", "").replace("BPS", "").replace("MBPS", "");
                return Double.parseDouble(numStr) * 1e6;
            } else if (cleaned.endsWith("K") || cleaned.endsWith("KBPS")) {
                String numStr = cleaned.replace("K", "").replace("BPS", "").replace("KBPS", "");
                return Double.parseDouble(numStr) * 1e3;
            }
            
            // Última tentativa - remover caracteres não numéricos
            String numericOnly = cleaned.replaceAll("[^0-9.E+-]", "");
            if (!numericOnly.isEmpty()) {
                return Double.parseDouble(numericOnly);
            }
            
            throw new NumberFormatException("Não foi possível converter: " + bitRateStr);
            
        } catch (Exception e) {
            System.err.println("Erro ao converter bitRate: " + bitRateStr + " - " + e.getMessage());
            throw new NumberFormatException("Formato de bitRate não suportado: " + bitRateStr);
        }
    }
    
    // Classe TrafficParameters
    public static class TrafficParameters {
        private int quantNodes;
        private double[] taxas;
        private double[] proporcoes;
        private double ro;
        private double incremento;
        private double mu;
        
        public TrafficParameters(int quantNodes, double[] taxas, double[] proporcoes, double ro, double incremento, double mu) {
            this.quantNodes = quantNodes;
            this.taxas = taxas;
            this.proporcoes = proporcoes;
            this.ro = ro;
            this.incremento = incremento;
            this.mu = mu;
        }
        
        public int getQuantNodes() { return quantNodes; }
        public double[] getTaxas() { return taxas; }
        public double[] getProporcoes() { return proporcoes; }
        public double getRo() { return ro; }
        public double getIncremento() { return incremento; }
        public double getMu() { return mu; }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TrafficParameterGUI().setVisible(true);
            }
        });
    }
}