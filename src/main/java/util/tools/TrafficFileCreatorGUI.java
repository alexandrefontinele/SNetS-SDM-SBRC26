package util.tools;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TrafficFileCreatorGUI extends JFrame {
	
    private JTextField caminhoArqField;
    private JTextField quantNodesField;
    private JTextField taxasField;
    private JTextField proporcoesField;
    private JTextField roField;
    private JTextField incrementoField;
    private JTextField muField;
    private JButton criarButton;
    private JButton selecionarArquivoButton;

    public TrafficFileCreatorGUI() {
        initComponents();
        setupLayout();
        setupListeners();
        setDefaultValues();
    }

    private void initComponents() {
        setTitle("Criador de Arquivo de Tráfego");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        caminhoArqField = new JTextField(30);
        quantNodesField = new JTextField();
        taxasField = new JTextField();
        proporcoesField = new JTextField();
        roField = new JTextField();
        incrementoField = new JTextField();
        muField = new JTextField();
        
        criarButton = new JButton("Criar Arquivo de Tráfego");
        selecionarArquivoButton = new JButton("Selecionar...");
    }

    private void setDefaultValues() {
        // Valores padrão/exemplo para cada campo
        quantNodesField.setText("14");
        taxasField.setText("100E+9, 200E+9, 300E+9, 400E+9, 500E+9");
        proporcoesField.setText("5, 4, 3, 2, 1");
        roField.setText("500");
        incrementoField.setText("200");
        muField.setText("1");
        
        // Sugerir um caminho padrão para o arquivo
        String userHome = System.getProperty("user.home");
        caminhoArqField.setText(userHome + File.separator + "traffic");
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de configurações
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Configurações do Tráfego", 
            TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Linha 0 - Caminho do arquivo
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        configPanel.add(new JLabel("Caminho do arquivo que será criado:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        configPanel.add(caminhoArqField, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        configPanel.add(selecionarArquivoButton, gbc);

        // Linha 1 - Quantidade de Nodes
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        configPanel.add(new JLabel("Quantidade de nós da rede:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        configPanel.add(quantNodesField, gbc);
        gbc.gridwidth = 1;

        // Linha 2 - Taxas (String[])
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        configPanel.add(new JLabel("Taxas de bits (separadas por vírgula):"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        configPanel.add(taxasField, gbc);
        gbc.gridwidth = 1;

        // Linha 3 - Proporções (double[])
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        configPanel.add(new JLabel("Proporções (separadas por vírgula):"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        configPanel.add(proporcoesField, gbc);
        gbc.gridwidth = 1;

        // Linha 4 - Ro
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        configPanel.add(new JLabel("Ro (Carga inicial - Erlangs):"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        configPanel.add(roField, gbc);
        gbc.gridwidth = 1;

        // Linha 5 - Incremento
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        configPanel.add(new JLabel("Incremento da carga (Erlangs):"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        configPanel.add(incrementoField, gbc);
        gbc.gridwidth = 1;

        // Linha 6 - Mu
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        configPanel.add(new JLabel("Mu (Taxa de retenção):"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        configPanel.add(muField, gbc);

        // Painel de botão
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(criarButton);

        mainPanel.add(configPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupListeners() {
        selecionarArquivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salvar arquivo de tráfego");
                
                // Sugerir o nome do arquivo baseado no campo atual
                String currentPath = caminhoArqField.getText();
                if (!currentPath.isEmpty()) {
                    fileChooser.setSelectedFile(new File(currentPath));
                }
                
                int userSelection = fileChooser.showSaveDialog(TrafficFileCreatorGUI.this);
                
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    caminhoArqField.setText(fileToSave.getAbsolutePath());
                }
            }
        });

        criarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarArquivoTrafico();
            }
        });

        // Adicionar tooltips para explicar cada campo
        quantNodesField.setToolTipText("Número total de nodes na topologia (ex: 10)");
        taxasField.setToolTipText("Taxas de bits (ex: 100E+9, 200E+9, 300E+9)");
        proporcoesField.setToolTipText("Proporções para cada taxa de bit (ex: 5, 4, 1)");
        roField.setToolTipText("Carga inicial da rede em Erlangs (ex: 500)");
        incrementoField.setToolTipText("Valor de incremento para a carga da rede em Erlangs (ex: 200)");
        muField.setToolTipText("Taxa de retenção de circuito (ex: 1.0)");
    }

    private void criarArquivoTrafico() {
        try {
            // Validar e obter os valores dos campos
            String caminhoArq = caminhoArqField.getText().trim();
            if (caminhoArq.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um caminho para o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantNodes = Integer.parseInt(quantNodesField.getText().trim());
            
            // Processar taxas (String[])
            String[] taxasArray = taxasField.getText().split(",");
            String[] taxas = new String[taxasArray.length];
            for (int i = 0; i < taxasArray.length; i++) {
                taxas[i] = taxasArray[i].trim();
            }

            // Processar proporções (double[])
            String[] proporcoesArray = proporcoesField.getText().split(",");
            double[] proporcoes = new double[proporcoesArray.length];
            for (int i = 0; i < proporcoesArray.length; i++) {
                proporcoes[i] = Double.parseDouble(proporcoesArray[i].trim());
            }

            // Validar se o número de taxas e proporções é o mesmo
            if (taxas.length != proporcoes.length) {
                JOptionPane.showMessageDialog(this, 
                    "O número de taxas deve ser igual ao número de proporções.", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double ro = Double.parseDouble(roField.getText().trim());
            double incremento = Double.parseDouble(incrementoField.getText().trim());
            double mu = Double.parseDouble(muField.getText().trim());

            // Chamar o método original
            createProportionalTrafficFile(caminhoArq, quantNodes, taxas, proporcoes, ro, incremento, mu);
            
            JOptionPane.showMessageDialog(this, 
                "Arquivo de tráfego criado com sucesso!\nLocal: " + caminhoArq, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, verifique se todos os valores numéricos estão corretos.", 
                "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao criar arquivo: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Método para criar arquivo de tráfego
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
            java.io.FileWriter fw = new java.io.FileWriter(caminhoArq);
            java.io.BufferedWriter out = new java.io.BufferedWriter(fw);
            
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
            
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar o arquivo de trafego.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TrafficFileCreatorGUI().setVisible(true);
            }
        });
    }
}

