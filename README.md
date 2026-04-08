# SNetS-SDM-SBRC26

## **Título do artigo:** **IMPA: Novo algoritmo para atribuição de potência de forma adaptativa em SDM-EONs**

## Resumo do artigo

Neste trabalho investigamos o problema ampliado PRMCSA, no qual a potência é tratada como variável de decisão. Propomos o algoritmo **IMPA** (*Impairment-Aware Margin Power Assignment*) para atribuição adaptativa de potência em **SDM-EONs**. O IMPA seleciona a menor potência viável que satisfaz simultaneamente margens de **OSNR** e **crosstalk**, considerando também o impacto sobre circuitos vizinhos já estabelecidos. Avaliações nas topologias **NSFNet** e **USA** demonstram que o IMPA reduz significativamente a probabilidade de bloqueio em relação aos algoritmos clássicos e adaptativos da literatura, alcançando uma redução na probabilidade de bloqueio de bitrate de pelo menos **18%** na topologia NSFNet e **46,5%** na topologia USA.

## Resumo do artefato

Este repositório contém o artefato associado ao artigo submetido ao **SBRC 2026**, incluindo:

- O código-fonte do simulador em **Java**;
- Uma versão empacotada do simulador em **JAR**;
- Conjuntos de simulações organizados por topologia e algoritmo;
- Arquivos de configuração e resultados das execuções;
- Uma ferramenta em **Python** para análise dos resultados e geração de gráficos.

O artefato foi organizado para permitir que os revisores:
1. **Inspecionem** o código-fonte do simulador;
2. **Executem** uma instância local do simulador usando um diretório de configuração;
3. **Utilizem** os resultados já incluídos no repositório para regenerar gráficos e tabelas;
4. **Reproduzam** as principais reivindicações experimentais do artigo a partir dos dados disponibilizados.

---

# Quick Start para revisores

Se você dispõe de pouco tempo, siga este roteiro mínimo.

## Caminho rápido 1 — Regenerar gráficos a partir dos CSVs já incluídos

```bash
git clone https://github.com/alexandrefontinele/SNetS-SDM-SBRC26.git
cd SNetS-SDM-SBRC26/simulations/Data_Analyzer
pip install numpy pandas matplotlib scipy
python SimulationDataAnalyzer.py
```

Depois:
1. Carregue arquivos CSV da pasta `USA` ou `NSFNet`;
2. Selecione a métrica `BlockingProbability` ou `BitRateBlockingProbability`;
3. Gere um gráfico de linha;
4. Observe no terminal a tabela de melhor algoritmo por carga.

Para instruções detalhadas sobre o analisador gráfico, consulte:  
[README do programa de geração de gráficos](https://github.com/alexandrefontinele/SNetS-SDM-SBRC26/blob/main/simulations/Data_Analyzer/README_data_analyzer.md)

## Caminho rápido 2 — Executar uma simulação local com o JAR

Na raiz do repositório:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

Esse comando deve:
- Ler os arquivos de configuração;
- Inicializar a simulação local;
- Exibir progresso no terminal.

---

# Estrutura do README.md

Este README está organizado da seguinte forma:

1. [Selos considerados](#selos-considerados)
2. [Informações básicas](#informações-básicas)
3. [Dependências](#dependências)
4. [Preocupações com segurança](#preocupações-com-segurança)
5. [Instalação](#instalação)
6. [Teste mínimo](#teste-mínimo)
7. [Experimentos](#experimentos)
8. [LICENSE](#license)

---

# Selos considerados

Os selos considerados para o processo de avaliação são:

- **Artefatos Disponíveis (SeloD)**
- **Artefatos Funcionais (SeloF)**
- **Artefatos Sustentáveis (SeloS)**
- **Experimentos Reprodutíveis (SeloR)**

---

# Informações básicas

## Informações do projeto

- **Projeto:** SNetS-SDM-SBRC26
- **Artigo:** *IMPA: Novo algoritmo para atribuição de potência de forma adaptativa em SDM-EONs*
- **Instituições:** Universidade Federal do Piauí (UFPI) e Instituto Federal do Piauí (IFPI)
- **Trilha que o trabalho foi submetido:** Trilha Principal
- **Linguagens principais:** Java e Python
- **Artefato principal:** Simulador Java + conjuntos de simulação + analisador gráfico em Python
- **Licença:** MIT

## Objetivo do artefato

O artefato disponibiliza um simulador e um conjunto de resultados experimentais para avaliação de algoritmos em cenários de redes ópticas, além de uma ferramenta auxiliar para análise gráfica dos resultados.

Pelo conteúdo do repositório, o artefato inclui:
- Código Java organizado em módulos como `gprmcsa`, `measurement`, `network`, `request`, `simulationControl`, `simulator` e `util`;
- Diretórios de simulação para as topologias **USA** e **NSFNet**;
- Múltiplas variantes de algoritmos, como `APAmen`, `APAnoMen`, `CPA`, `CPSD`, `EPA`, `EnPA`, `IMPA` e `PABS`;
- Arquivos de resultados, como:
  - `BlockingProbability.csv`
  - `BitRateBlockingProbability.csv`
  - `CrosstalkStatistics.csv`
  - `ModulationUtilization.csv`
  - `SpectrumUtilization.csv`

## Relação com o artigo

O artefato apoia a avaliação do algoritmo **IMPA**, proposto no artigo, permitindo:
- Examinar a implementação do algoritmo e do simulador;
- Executar cenários locais;
- Verificar os resultados nas topologias **NSFNet** e **USA**;
- Regenerar gráficos de **PBC** e **PBBR**;
- Comparar o IMPA com algoritmos clássicos e adaptativos da literatura.

## O que o revisor realmente vai usar

Para a avaliação prática do artefato, os caminhos mais importantes são:

- `simulations/SNetS-SDM-SBRC26.jar`  
  **Uso:** executar o simulador em modo local.

- `simulations/Data_Analyzer/SimulationDataAnalyzer.py`  
  **Uso:** abrir a ferramenta de análise e geração de gráficos.  
  **README da ferramenta:** [README do programa de geração de gráficos](https://github.com/alexandrefontinele/SNetS-SDM-SBRC26/blob/main/simulations/Data_Analyzer/README_data_analyzer.md)

- `simulations/Data_Analyzer/USA/`  
  **Uso:** carregar CSVs já organizados da topologia USA.

- `simulations/Data_Analyzer/NSFNet/`  
  **Uso:** carregar CSVs já organizados da topologia NSFNet.

- `simulations/USA_sims/`  
  **Uso:** cenários completos de simulação para a topologia USA.

- `simulations/NSFNet_sims/`  
  **Uso:** cenários completos de simulação para a topologia NSFNet.

- `src/main/java/`  
  **Uso:** inspeção do código-fonte do simulador.

## Estrutura do repositório

Estrutura principal observada no repositório:

```text
SNetS-SDM-SBRC26/
├── .settings/                         # Configurações do ambiente de desenvolvimento (ex.: Eclipse)
├── src/                               # Código-fonte principal do projeto
│   └── main/
│       └── java/
│           ├── gprmcsa/               # Implementações de algoritmos e lógica de alocação PRMCSA/RMCSA
│           ├── measurement/           # Métricas, coleta de resultados e estatísticas de simulação
│           ├── network/               # Modelos de topologia, enlaces, nós, núcleos e recursos da rede
│           ├── request/               # Modelagem das requisições de tráfego e circuitos
│           ├── simulationControl/     # Controle da execução das simulações, leitura de configurações e modo local
│           ├── simulator/             # Núcleo do simulador e fluxo principal de execução
│           └── util/                  # Classes utilitárias e funções auxiliares
├── simulations/                       # Arquivos prontos para execução e resultados experimentais
│   ├── Data_Analyzer/                 # Ferramenta Python para análise dos CSVs e geração de gráficos
│   │   ├── NSFNet/                    # CSVs organizados para análise da topologia NSFNet
│   │   ├── USA/                       # CSVs organizados para análise da topologia USA
│   │   ├── imgs/                      # Imagens e recursos auxiliares do analisador
│   │   ├── README_data_analyzer.md    # Documentação específica da ferramenta de análise
│   │   └── SimulationDataAnalyzer.py  # Script principal do analisador gráfico em Python
│   ├── NSFNet_sims/                   # Conjuntos de cenários e resultados da topologia NSFNet
│   ├── USA_sims/                      # Conjuntos de cenários e resultados da topologia USA
│   ├── SNetS-SDM-SBRC26.jar           # Versão empacotada do simulador em JAR
│   └── run_SNetS-SDM-SBRC26_jar.bat   # Script de apoio para execução do JAR no Windows
├── pom.xml                            # Arquivo Maven com dependências e configuração de build
├── LICENSE                            # Licença do projeto
└── README.md                          # README principal do repositório
```

## Ambiente de execução recomendado

### Hardware recomendado
- CPU: 4 núcleos ou mais
- RAM: 8 GB ou mais
- Armazenamento livre: 5 GB ou mais

### Software recomendado
- **Windows 10/11** ou **Linux**
- **Java 8**
- **Python 3.10+**
- **Maven 3.8+** (opcional, para compilar a partir do código-fonte)

## Necessário, opcional e não necessário para a avaliação

### Necessário
- Java 8;
- Python 3.10+;
- Bibliotecas Python: `numpy`, `pandas`, `matplotlib`, `scipy`;
- O JAR do simulador;
- Os CSVs e diretórios de simulação do repositório.

### Opcional
- Maven, caso o revisor deseje compilar a partir do código-fonte;
- Linux com `python3-tk`, caso o sistema não tenha Tkinter disponível por padrão.

### Não necessário para a avaliação padrão
- Firebase;
- Credenciais privadas;
- Modos distribuídos/LAN;
- Infraestrutura externa de nuvem.

---

# Dependências

## Dependências do simulador (Java)

O projeto possui um `pom.xml` com:
- **Java 8**
- Codificação **ISO-8859-1**
- Dependências:
  - `gson`
  - `opencsv`
  - `org.json`
  - `firebase-admin`

### Dependências Maven
Se desejar instalar tudo automaticamente via Maven:

```bash
mvn dependency:resolve
```

## Dependências do analisador de dados (Python)

Instale as bibliotecas necessárias para a ferramenta de gráficos:

```bash
pip install numpy pandas matplotlib scipy
```

Em Linux, pode ser necessário instalar também o Tkinter:

```bash
sudo apt-get install python3-tk
```

## Recursos de terceiros

O modo local do simulador **não depende** de Firebase.  
No entanto, o código também possui modos de execução distribuída/servidor com Firebase. Esses modos **não são necessários** para a avaliação padrão do artefato.

---

# Preocupações com segurança

## Execução recomendada para avaliação
Para a avaliação do artefato, recomenda-se utilizar **somente**:
- O **modo local** do simulador;
- A ferramenta Python de análise de resultados;
- Os resultados já incluídos no repositório.

## Modos que não precisam ser usados na avaliação
O código possui suporte a:
- Servidor de simulação com Firebase;
- Cliente/servidor LAN;
- Leitura de credenciais em `private-key-firebase.json`.

Esses modos não são necessários para reproduzir as funcionalidades e os experimentos principais do artefato e podem ser ignorados durante a avaliação.

## Boas práticas para os revisores
- Não execute modos que dependam de credenciais privadas;
- Não adicione chaves de Firebase ao repositório;
- Use o artefato em um ambiente isolado (máquina virtual ou ambiente de testes), se desejar.

---

# Instalação

Há dois caminhos recomendados.

## Opção A — usar o JAR já incluído no repositório (recomendado para os revisores)

### 1. Clonar o repositório

```bash
git clone https://github.com/alexandrefontinele/SNetS-SDM-SBRC26.git
cd SNetS-SDM-SBRC26
```

### 2. Verificar a instalação do Java

```bash
java -version
```

O ideal é usar **Java 8**.

### 3. Verificar a instalação do Python

```bash
python --version
```

ou

```bash
python3 --version
```

### 4. Instalar dependências do analisador de dados

```bash
pip install numpy pandas matplotlib scipy
```

### 5. Verificar se o JAR está presente

O arquivo deve estar em:

```text
simulations/SNetS-SDM-SBRC26.jar
```

### 6. Verificar se o analisador está presente

O script deve estar em:

```text
simulations/Data_Analyzer/SimulationDataAnalyzer.py
```

---

## Opção B — compilar a partir do código-fonte

### 1. Clonar o repositório

```bash
git clone https://github.com/alexandrefontinele/SNetS-SDM-SBRC26.git
cd SNetS-SDM-SBRC26
```

### 2. Verificar Maven

```bash
mvn -version
```

### 3. Compilar

```bash
mvn package
```

> **Observação:** para a avaliação do artefato, a opção mais simples continua sendo usar o **JAR já incluído** no repositório.

---

# Teste mínimo

Esta seção apresenta um teste mínimo para verificar se o artefato está funcional.

## Teste mínimo 1 — abrir o analisador de dados

### Passo 1
Entre na pasta do analisador:

```bash
cd simulations/Data_Analyzer
```

### Passo 2
Execute a ferramenta:

```bash
python SimulationDataAnalyzer.py
```

ou

```bash
python3 SimulationDataAnalyzer.py
```

### Resultado esperado
A interface gráfica do analisador deve abrir, permitindo:
- Carregar arquivos CSV;
- Escolher métricas;
- Gerar gráficos;
- Exportar gráficos em SVG/PDF;
- Exportar tabelas em CSV.

### Evidência concreta esperada
O revisor deve conseguir:
- Selecionar arquivos em `USA/` ou `NSFNet/`;
- Escolher `BlockingProbability` ou `BitRateBlockingProbability`;
- Gerar um gráfico de linha;
- Ver no terminal a saída textual com a tabela de melhor algoritmo por carga.

## Teste mínimo 2 — executar uma simulação local

Escolha um diretório de configuração completo, por exemplo:

```text
simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

Esse diretório contém:
- `simulation`
- `network`
- `others`
- `physicalLayer`
- `traffic`
- Além de arquivos CSV de saída e auxiliares.

### Execução com o JAR

Na raiz do repositório:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

### Resultado esperado
O simulador deve:
- Ler os arquivos de configuração;
- Validar a configuração de rede;
- Iniciar a simulação local;
- Imprimir progresso no terminal.

### Evidência concreta esperada
O revisor deve observar no terminal:
- leitura do diretório de configuração;
- inicialização da simulação;
- mensagens de progresso ou processamento.

---

# Experimentos

Esta seção descreve formas de reproduzir as principais reivindicações experimentais do artigo.

## Tabela-resumo das reivindicações

| Reivindicação | Como reproduzir | Resultado esperado |
|---|---|---|
| Regenerar os principais gráficos a partir dos CSVs | Usar `SimulationDataAnalyzer.py` com os CSVs de `USA/` e `NSFNet/` | Gráficos de PBC/PBBR, ICs e melhor algoritmo por carga |
| Executar uma simulação local válida | Rodar o JAR com um diretório completo de `USA_sims/` ou `NSFNet_sims/` | Inicialização correta e progresso da simulação no terminal |
| Verificar organização experimental do artefato | Inspecionar pastas por topologia, algoritmo e parametrização | Relação clara entre cenários, resultados e configurações |
| Inspecionar a implementação | Examinar `src/main/java/` | Código modularizado e rastreável em relação ao artigo |

---

## Reivindicação #1 — os resultados experimentais podem ser regenerados graficamente a partir dos CSVs incluídos

### Objetivo
Mostrar que os resultados salvos no repositório podem ser carregados e convertidos novamente em gráficos comparativos.

### Procedimento

#### Passo 1
Abra o analisador:

```bash
cd simulations/Data_Analyzer
python SimulationDataAnalyzer.py
```

#### Passo 2
Carregue arquivos CSV de uma das topologias:
- `simulations/Data_Analyzer/USA`
- Ou `simulations/Data_Analyzer/NSFNet`

#### Passo 3
Selecione uma métrica, por exemplo:
- `BlockingProbability`
- `BitRateBlockingProbability`

#### Passo 4
Gere um gráfico de linha.

### Resultado esperado
O analisador deve:
- Listar os algoritmos carregados;
- Permitir selecionar a métrica;
- Gerar um gráfico comparativo;
- Calcular IC;
- Imprimir no terminal a tabela de melhor algoritmo por carga.

### Recursos esperados
- RAM: ~1–2 GB
- Tempo: poucos segundos até alguns minutos, dependendo da quantidade de arquivos carregados

---

## Reivindicação #2 — os diretórios de simulação incluídos permitem reproduzir execuções locais do simulador

### Objetivo
Executar ao menos uma configuração completa de simulação local contida no repositório.

### Procedimento

#### Passo 1
Selecione um diretório completo, por exemplo:

```text
simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

Ou um diretório equivalente em:
- `USA_sims`
- `NSFNet_sims`

#### Passo 2
Execute:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

### Resultado esperado
O simulador deve:
- Carregar os arquivos do diretório;
- Iniciar a execução local;
- Produzir saída textual no terminal com progresso.

### Observação
Os diretórios de simulação já incluem resultados CSV e configurações. Assim, mesmo que o revisor não deseje rerodar muitos cenários, ele ainda poderá:
- Verificar a consistência das configurações;
- Usar os resultados já incluídos para regenerar gráficos e comparar algoritmos.

---

## Reivindicação #3 — o artefato contém experimentos organizados por topologia, algoritmo e parâmetros

### Objetivo
Mostrar que o repositório foi organizado para facilitar rastreabilidade experimental.

### Evidência observável
No repositório há:
- Topologias distintas (`USA_sims`, `NSFNet_sims`);
- Subpastas por algoritmo (`APAmen`, `APAnoMen`, `CPA`, `CPSD`, `EPA`, `EnPA`, `IMPA`, `PABS`);
- Subpastas de configuração com nomes parametrizados, por exemplo:

```text
USA_IMPA_HXT_mo_0_00_mx_0_00
USA_IMPA_HXT_mo_0_00_mx_0_25
USA_IMPA_HXT_mo_0_25_mx_0_00
...
```

### Resultado esperado
O revisor deve conseguir:
- Identificar facilmente o cenário correspondente;
- Localizar os arquivos de configuração;
- Localizar os arquivos de resultados.

---

## Reivindicação #4 — o código-fonte está disponível e organizado de forma modular

### Objetivo
Sustentar a avaliação de sustentabilidade e compreensibilidade do artefato.

### Evidência observável
O código Java está organizado em módulos como:
- `gprmcsa`
- `measurement`
- `network`
- `request`
- `simulationControl`
- `simulator`
- `util`

Além disso, há um `pom.xml`, licença MIT e estrutura consistente de projeto Java/Maven.

---

## Sugestão de execução para os revisores

Se o tempo de avaliação for limitado, recomenda-se o seguinte roteiro:

1. **Executem** o **analisador Python**;
2. **Carreguem** CSVs já incluídos e regenerem pelo menos um gráfico;
3. **Verifiquem** a saída de melhor algoritmo por carga no terminal;
4. **Executem** **uma** simulação local com o JAR em **um** diretório de configuração;
5. **Inspecionem** a organização das pastas e do código-fonte.

Esse fluxo já cobre, de forma prática:
- Disponibilidade;
- Funcionalidade;
- Organização;
- Reprodutibilidade parcial das reivindicações principais.

---

# Limitações conhecidas

- O ambiente principal testado é **Java 8**;
- Os modos com **Firebase** não fazem parte do fluxo recomendado de avaliação;
- Alguns cenários completos podem demandar mais tempo de execução que o ideal para uma avaliação rápida;
- O fluxo recomendado para os revisores é usar primeiro os **resultados já incluídos** para regeneração de gráficos e, em seguida, executar um cenário local representativo;
- O analisador gráfico depende de ambiente com suporte a interface gráfica via **Tkinter**.

---

# Como citar

Se desejar citar o artigo associado a este artefato, utilize os dados do trabalho:

**IMPA: Novo algoritmo para atribuição de potência de forma adaptativa em SDM-EONs.**  
Autores: Jordana França, Uriel P. Mori, Alexandre C. Fontinele, Iallen Santos, Andre C. B. Soares.

> Ajuste esta seção para o formato bibliográfico final adotado pelo SBRC 2026, se necessário.

---

# LICENSE

Este projeto está licenciado sob a licença **MIT**.

Consulte o arquivo:

```text
LICENSE
```
