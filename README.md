# SNetS-SDM-SBRC26

> **Título do artigo:** **[SUBSTITUIR PELO TÍTULO EXATO DO ARTIGO ACEITO NO SBRC 2026]**

## Resumo do artefato

Este repositório contém o artefato associado ao artigo submetido ao **SBRC 2026**, incluindo:

- o código-fonte do simulador em **Java**;
- uma versão empacotada do simulador em **JAR**;
- conjuntos de simulações organizados por topologia e algoritmo;
- arquivos de configuração e resultados das execuções;
- uma ferramenta em **Python** para análise dos resultados e geração de gráficos.

O artefato foi organizado para permitir que os revisores:
1. inspecionem o código-fonte do simulador;
2. executem uma instância local do simulador usando um diretório de configuração;
3. utilizem os resultados já incluídos no repositório para regenerar gráficos e tabelas;
4. reproduzam as principais reivindicações experimentais do artigo a partir dos dados disponibilizados.

> **Observação importante:** substitua o título acima e, se desejar, complemente este resumo com o **resumo oficial do artigo** antes da submissão final do artefato.

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

## Objetivo do artefato

O artefato disponibiliza um simulador e um conjunto de resultados experimentais para avaliação de algoritmos em cenários de redes ópticas, além de uma ferramenta auxiliar para análise gráfica dos resultados.

Pelo conteúdo público do repositório, o artefato inclui:
- código Java organizado em módulos como `gprmcsa`, `measurement`, `network`, `request`, `simulationControl`, `simulator` e `util`;
- diretórios de simulação para as topologias **USA** e **NSFNet**;
- múltiplas variantes de algoritmos, como `APAmen`, `APAnoMen`, `CPA`, `CPSD`, `EPA`, `EnPA`, `IMPA` e `PABS`;
- arquivos de resultados, como:
  - `BlockingProbability.csv`
  - `BitRateBlockingProbability.csv`
  - `CrosstalkStatistics.csv`
  - `ModulationUtilization.csv`
  - `SpectrumUtilization.csv`

## Estrutura do repositório

Estrutura principal observada no repositório:

```text
SNetS-SDM-SBRC26/
├── .settings/
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
│   │   ├── imgs/
│   │   ├── README_data_analyzer.md
│   │   └── SimulationDataAnalyzer.py
│   ├── NSFNet_sims/
│   ├── USA_sims/
│   ├── SNetS-SDM-SBRC26.jar
│   └── run_SNetS-SDM-SBRC26_jar.bat
├── pom.xml
├── LICENSE
└── README.md
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

---

# Dependências

## Dependências do simulador (Java)

O projeto possui um `pom.xml` com:
- **Java 8**
- codificação **ISO-8859-1**
- dependências:
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
- o **modo local** do simulador;
- a ferramenta Python de análise de resultados;
- os resultados já incluídos no repositório.

## Modos que não precisam ser usados na avaliação
O código possui suporte a:
- servidor de simulação com Firebase;
- cliente/servidor LAN;
- leitura de credenciais em `private-key-firebase.json`.

Esses modos não são necessários para reproduzir as funcionalidades e os experimentos principais do artefato e podem ser ignorados durante a avaliação.

## Boas práticas para os revisores
- não execute modos que dependam de credenciais privadas;
- não adicione chaves de Firebase ao repositório;
- use o artefato em um ambiente isolado (máquina virtual ou ambiente de testes), se desejar.

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
- carregar arquivos CSV;
- escolher métricas;
- gerar gráficos;
- exportar gráficos em SVG/PDF;
- exportar tabelas em CSV.

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
- além de arquivos CSV de saída e auxiliares.

### Execução com o JAR

Na raiz do repositório:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

### Resultado esperado
O simulador deve:
- ler os arquivos de configuração;
- validar a configuração de rede;
- iniciar a simulação local;
- imprimir progresso no terminal.

> **Observação:** o tempo de execução depende do hardware e do tamanho da configuração. Para a avaliação inicial, é suficiente verificar se a execução inicia corretamente e se a leitura da configuração é feita com sucesso.

---

# Experimentos

Esta seção descreve formas de reproduzir as principais reivindicações experimentais do artigo.

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
- ou `simulations/Data_Analyzer/NSFNet`

#### Passo 3
Selecione uma métrica, por exemplo:
- `BlockingProbability`
- `BitRateBlockingProbability`

#### Passo 4
Gere um gráfico de linha.

### Resultado esperado
O analisador deve:
- listar os algoritmos carregados;
- permitir selecionar a métrica;
- gerar um gráfico comparativo;
- calcular IC;
- imprimir no terminal a tabela `Best algorithm by load`.

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

ou um diretório equivalente em:
- `USA_sims`
- `NSFNet_sims`

#### Passo 2
Execute:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

### Resultado esperado
O simulador deve:
- carregar os arquivos do diretório;
- iniciar a execução local;
- produzir saída textual no terminal com progresso.

### Observação
Os diretórios de simulação já incluem resultados CSV e configurações. Assim, mesmo que o revisor não deseje rerodar muitos cenários, ele ainda poderá:
- verificar a consistência das configurações;
- usar os resultados já incluídos para regenerar gráficos e comparar algoritmos.

---

## Reivindicação #3 — o artefato contém experimentos organizados por topologia, algoritmo e parâmetros

### Objetivo
Mostrar que o repositório foi organizado para facilitar rastreabilidade experimental.

### Evidência observável
No repositório há:
- topologias distintas (`USA_sims`, `NSFNet_sims`);
- subpastas por algoritmo (`APAmen`, `APAnoMen`, `CPA`, `CPSD`, `EPA`, `EnPA`, `IMPA`, `PABS`);
- subpastas de configuração com nomes parametrizados, por exemplo:

```text
USA_IMPA_HXT_mo_0_00_mx_0_00
USA_IMPA_HXT_mo_0_00_mx_0_25
USA_IMPA_HXT_mo_0_25_mx_0_00
...
```

### Resultado esperado
O revisor deve conseguir:
- identificar facilmente o cenário correspondente;
- localizar os arquivos de configuração;
- localizar os arquivos de resultados.

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

1. Executar o **analisador Python**;
2. Carregar CSVs já incluídos e regenerar pelo menos um gráfico;
3. Verificar a saída de melhor algoritmo por carga no terminal;
4. Executar **uma** simulação local com o JAR em **um** diretório de configuração;
5. Inspecionar a organização das pastas e do código-fonte.

Esse fluxo já cobre, de forma prática:
- disponibilidade;
- funcionalidade;
- organização;
- reprodutibilidade parcial das reivindicações principais.

---

# LICENSE

Este projeto está licenciado sob a licença **MIT**.

Consulte o arquivo:

```text
LICENSE
```

