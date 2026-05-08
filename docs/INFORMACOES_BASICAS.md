# Informações básicas

Esta página apresenta o objetivo do artefato, os dados gerais do projeto e os principais caminhos usados pelo revisor.

## Informações básicas

## Informações do projeto

- **Projeto:** SNetS-SDM-SBRC26
- **Artigo:** *IMPA: Novo algoritmo para atribuição de potência de forma adaptativa em SDM-EONs*
- **Instituições:** Universidade Federal do Piauí (UFPI) e Instituto Federal do Piauí (IFPI)
- **Trilha:** Trilha Principal
- **Linguagens principais:** Java e Python
- **Artefato principal:** Simulador Java + conjuntos de simulação + analisador gráfico em Python
- **Licença:** MIT

## Objetivo do artefato

O artefato disponibiliza um simulador e um conjunto de resultados experimentais para avaliação de algoritmos em cenários de redes ópticas elásticas com multiplexação por divisão espacial, além de uma ferramenta auxiliar para análise gráfica dos resultados.

O artefato apoia a avaliação do algoritmo **IMPA**, permitindo:

- Examinar a implementação do algoritmo e do simulador;
- Executar cenários locais;
- Verificar resultados nas topologias **NSFNet** e **USA**;
- Recriar gráficos de **PBC** (*Probabilidade de Bloqueio de Circuito*) e **PBBR** (*Probabilidade de Bloqueio de BitRate*);
- Comparar o IMPA com algoritmos clássicos e adaptativos da literatura.

## O que o revisor provavelmente vai usar

| Caminho | Uso |
|---|---|
| `simulations/SNetS-SDM-SBRC26.jar` | Execução do simulador Java em modo local |
| `simulations/Data_Analyzer/SimulationDataAnalyzer.py` | Analisador gráfico Python, com GUI e modo CLI/headless |
| `simulations/Data_Analyzer/README_data_analyzer.md` | Documentação específica do analisador de dados |
| `simulations/Data_Analyzer/configs/` | Configurações JSON para recriar os gráficos do artigo |
| `simulations/Data_Analyzer/generate_article_graphs.ps1` | Script PowerShell para gerar todos os gráficos |
| `simulations/Data_Analyzer/generate_article_graphs.sh` | Script Linux/WSL/Docker para gerar todos os gráficos |
| `simulations/Data_Analyzer/USA/` | CSVs organizados da topologia USA |
| `simulations/Data_Analyzer/NSFNet/` | CSVs organizados da topologia NSFNet |
| `simulations/USA_sims/` | Cenários completos de simulação para USA |
| `simulations/NSFNet_sims/` | Cenários completos de simulação para NSFNet |
| `src/main/java/` | Código-fonte Java do simulador |
| `Dockerfile.analyzer` | Container para o analisador Python |
| `Dockerfile.simulator` | Container para o simulador Java |

---

---

[Voltar ao README principal](../README.md)
