# Estrutura do repositório

Esta página descreve as pastas e arquivos principais do artefato.

## Estrutura do repositório

Estrutura principal:

```text
SNetS-SDM-SBRC26/
├── src/
│   └── main/
│       └── java/
│           ├── gprmcsa/
│           ├── measurement/
│           ├── network/
│           ├── request/
│           ├── simulationControl/
│           ├── simulator/
│           └── util/
├── simulations/
│   ├── Data_Analyzer/
│   │   ├── NSFNet/
│   │   ├── USA/
│   │   ├── configs/
│   │   │   ├── config_article_PBC_log_USA.json
│   │   │   ├── config_article_PBC_log_NSFNet.json
│   │   │   ├── config_article_PBBR_log_USA.json
│   │   │   ├── config_article_PBBR_log_NSFNet.json
│   │   │   ├── config_article_PBC_Comp_USA.json
│   │   │   └── config_article_PBC_Comp_NSFNet.json
│   │   ├── generate_article_graphs.ps1
│   │   ├── generate_article_graphs.sh
│   │   ├── README_data_analyzer.md
│   │   ├── requirements.txt
│   │   └── SimulationDataAnalyzer.py
│   ├── NSFNet_sims/
│   ├── USA_sims/
│   ├── SNetS-SDM-SBRC26.jar
│   └── run_SNetS-SDM-SBRC26_jar.bat
├── Dockerfile.analyzer
├── Dockerfile.simulator
├── pom.xml
├── LICENSE
└── README.md
```

## Descrição das principais pastas e arquivos

### Raiz do repositório

| Caminho | Descrição |
|---|---|
| `README.md` | Documento principal do artefato. Contém visão geral, dependências, instalação, testes mínimos, uso com Docker e instruções de reprodução. |
| `LICENSE` | Licença do projeto. |
| `pom.xml` | Arquivo Maven do projeto Java. É necessário apenas para quem deseja compilar o simulador a partir do código-fonte. |
| `Dockerfile.analyzer` | Dockerfile usado para criar a imagem do analisador Python. |
| `Dockerfile.simulator` | Dockerfile usado para criar a imagem do simulador Java com Java 8. |

### Código-fonte Java

| Caminho | Descrição |
|---|---|
| `src/main/java/gprmcsa/` | Implementa algoritmos e procedimentos relacionados a roteamento, alocação de recursos, modulação, núcleo, espectro e potência. |
| `src/main/java/measurement/` | Contém classes de medição, estatísticas e coleta de métricas da simulação. |
| `src/main/java/network/` | Contém modelos da rede óptica, como nós, enlaces, rotas, recursos espectrais, núcleos e topologias. |
| `src/main/java/request/` | Contém classes relacionadas às requisições de conexão e demandas de tráfego. |
| `src/main/java/simulationControl/` | Contém classes de controle da simulação, leitura de configurações, validação de parâmetros e organização da execução. |
| `src/main/java/simulator/` | Contém o núcleo do simulador e a lógica principal de execução baseada em eventos. |
| `src/main/java/util/` | Contém classes utilitárias usadas por diferentes partes do simulador. |

### Diretório de simulações e análise

| Caminho | Descrição |
|---|---|
| `simulations/` | Diretório que agrupa o JAR executável, cenários de simulação, resultados e ferramentas de análise. |
| `simulations/SNetS-SDM-SBRC26.jar` | JAR já empacotado do simulador. É o caminho recomendado para executar uma simulação sem recompilar o código Java. |
| `simulations/run_SNetS-SDM-SBRC26_jar.bat` | Script auxiliar para execução do JAR no Windows. |
| `simulations/USA_sims/` | Conjuntos completos de simulação para a topologia USA, organizados por algoritmo e parâmetros experimentais. |
| `simulations/NSFNet_sims/` | Conjuntos completos de simulação para a topologia NSFNet, organizados por algoritmo e parâmetros experimentais. |
| `simulations/Data_Analyzer/` | Ferramenta Python usada para analisar CSVs e gerar gráficos do artigo. |

### Analisador de dados e geração de gráficos

| Caminho | Descrição |
|---|---|
| `simulations/Data_Analyzer/SimulationDataAnalyzer.py` | Programa principal do analisador. Pode ser executado com GUI ou em modo CLI/headless usando `--config`. |
| `simulations/Data_Analyzer/README_data_analyzer.md` | Documentação específica do analisador de dados, incluindo GUI, CLI, Docker, configs e solução de problemas. |
| `simulations/Data_Analyzer/requirements.txt` | Lista de dependências Python necessárias para executar o analisador fora do Docker. |
| `simulations/Data_Analyzer/generate_article_graphs.ps1` | Script PowerShell para gerar automaticamente todos os gráficos principais do artigo no Windows. |
| `simulations/Data_Analyzer/generate_article_graphs.sh` | Script Bash para gerar automaticamente todos os gráficos principais do artigo no Linux, WSL ou Docker. |
| `simulations/Data_Analyzer/USA/` | CSVs organizados para geração dos gráficos da topologia USA. |
| `simulations/Data_Analyzer/NSFNet/` | CSVs organizados para geração dos gráficos da topologia NSFNet. |
| `simulations/Data_Analyzer/configs/` | Arquivos JSON com as configurações usadas para recriar automaticamente os gráficos principais do artigo. |

### Arquivos de configuração dos gráficos do artigo

| Caminho | Descrição |
|---|---|
| `simulations/Data_Analyzer/configs/config_article_PBC_log_USA.json` | Gera o gráfico de Probabilidade de Bloqueio de Circuito em escala logarítmica para a topologia USA. |
| `simulations/Data_Analyzer/configs/config_article_PBC_log_NSFNet.json` | Gera o gráfico de Probabilidade de Bloqueio de Circuito em escala logarítmica para a topologia NSFNet. |
| `simulations/Data_Analyzer/configs/config_article_PBBR_log_USA.json` | Gera o gráfico de Probabilidade de Bloqueio de BitRate em escala logarítmica para a topologia USA. |
| `simulations/Data_Analyzer/configs/config_article_PBBR_log_NSFNet.json` | Gera o gráfico de Probabilidade de Bloqueio de BitRate em escala logarítmica para a topologia NSFNet. |
| `simulations/Data_Analyzer/configs/config_article_PBC_Comp_USA.json` | Gera o gráfico percentual dos componentes de bloqueio para a topologia USA. |
| `simulations/Data_Analyzer/configs/config_article_PBC_Comp_NSFNet.json` | Gera o gráfico percentual dos componentes de bloqueio para a topologia NSFNet. |

### Arquivos gerados durante a execução

| Caminho | Descrição |
|---|---|
| `simulations/Data_Analyzer/outputs/` | Diretório criado pelo analisador para armazenar gráficos e tabelas geradas. |
| `simulations/Data_Analyzer/outputs/article/` | Diretório padrão usado pelos scripts para salvar os gráficos do artigo. |
| `*.svg` | Gráficos exportados pelo analisador. |
| `*_ci.csv` | Tabelas de intervalo de confiança geradas para gráficos de linha. |
| `*_best.csv` | Tabelas com o melhor algoritmo por carga, geradas para gráficos de linha. |

---

---

[Voltar ao README principal](../README.md)
