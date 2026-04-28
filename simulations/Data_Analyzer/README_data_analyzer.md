# SimGraph — Simulation Graph Analyzer

README (PT-BR)

## Visão geral

**SimGraph — Simulation Graph Analyzer** é uma ferramenta em Python para carregar arquivos CSV com resultados de simulações, comparar algoritmos e gerar gráficos para uso em artigos, relatórios e apresentações.

O programa suporta:

- Gráficos de linha;
- Gráficos de barras empilhadas;
- Gráficos de barras percentuais;
- Intervalos de confiança;
- Comparação de melhor algoritmo por carga;
- Cálculo de ganho entre algoritmos;
- Personalização de rótulos, textos, legendas, cores, marcadores, estilos de linha, fontes, aparência e margens;
- Exportação dos gráficos em **SVG** e **PDF**;
- Exportação de tabelas de intervalo de confiança em **CSV**;
- Salvamento e carregamento de customizações do usuário em **JSON**;
- Uso interativo por interface gráfica (**GUI**);
- Uso automático por linha de comando (**CLI/headless**) com arquivos JSON de configuração;
- Geração automática dos gráficos do artigo por meio da pasta `configs/` e dos scripts `generate_article_graphs.ps1` e `generate_article_graphs.sh`.

O fluxo recomendado para **reprodução dos gráficos do artigo** é o modo **CLI/headless** com os arquivos JSON da pasta `configs/`.

---

## Sumário

1. [O que o programa faz](#o-que-o-programa-faz)
2. [Arquivos principais](#arquivos-principais)
3. [Modos de uso](#modos-de-uso)
4. [Requisitos](#requisitos)
5. [Instalação local](#instalação-local)
6. [Execução](#execução)
7. [Interface principal](#interface-principal)
8. [Área Actions](#área-actions-ações)
9. [Carregamento de dados](#carregamento-de-dados)
10. [Estrutura dos arquivos CSV](#estrutura-dos-arquivos-csv)
11. [Seleção de métrica](#seleção-de-métrica)
12. [Plot setup](#plot-setup-configuração-do-gráfico)
13. [Statistics](#statistics-estatísticas)
14. [Explicação dos métodos de intervalo de confiança](#explicação-dos-métodos-de-intervalo-de-confiança)
15. [Appearance](#appearance-aparência)
16. [Margins](#margins-margens)
17. [Arquivos de configuração JSON](#arquivos-de-configuração-json)
18. [Geração automática dos gráficos do artigo](#geração-automática-dos-gráficos-do-artigo)
19. [Uso com Docker](#uso-com-docker)
20. [Personalização dos gráficos](#personalização-dos-gráficos)
21. [Geração de gráficos](#geração-de-gráficos)
22. [Saída do intervalo de confiança](#saída-do-intervalo-de-confiança)
23. [Cálculo de ganho](#cálculo-de-ganho)
24. [Salvar e carregar customizações](#salvar-e-carregar-customizações)
25. [Opções de exportação](#opções-de-exportação)
26. [Saídas geradas](#saídas-geradas)
27. [Exemplos de uso](#exemplos-de-uso)
28. [Solução de problemas](#solução-de-problemas)
29. [Boas práticas para reprodução](#boas-práticas-para-reprodução)
30. [Ambiente sugerido](#ambiente-sugerido)
31. [Resumo final](#resumo-final)

---

## O que o programa faz

De forma prática, o programa permite:

1. Carregar arquivos CSV de resultados de simulações;
2. Identificar automaticamente as métricas disponíveis;
3. Comparar algoritmos em diferentes cargas de rede;
4. Gerar gráficos de linha e gráficos de barras;
5. Calcular e exibir intervalos de confiança;
6. Calcular o ganho de um algoritmo em relação aos demais;
7. Exportar gráficos e tabelas para uso em artigos, relatórios e apresentações;
8. Executar a geração de gráficos de forma automática por linha de comando;
9. Recriar os gráficos do artigo a partir de configurações JSON versionadas no repositório.

---

## Arquivos principais

A estrutura esperada dentro de `simulations/Data_Analyzer/` é:

```text
simulations/Data_Analyzer/
├── SimulationDataAnalyzer.py
├── README_data_analyzer.md
├── requirements.txt
├── generate_article_graphs.ps1
├── generate_article_graphs.sh
├── USA/
├── NSFNet/
└── configs/
    ├── config_article_PBC_log_USA.json
    ├── config_article_PBC_log_NSFNet.json
    ├── config_article_PBBR_log_USA.json
    ├── config_article_PBBR_log_NSFNet.json
    ├── config_article_PBC_Comp_USA.json
    └── config_article_PBC_Comp_NSFNet.json
```

### Descrição dos principais arquivos

| Caminho | Descrição |
|---|---|
| `SimulationDataAnalyzer.py` | Programa principal. Pode abrir a GUI ou executar em modo CLI/headless com `--config`. |
| `README_data_analyzer.md` | Documentação específica do analisador de dados e geração de gráficos. |
| `requirements.txt` | Lista de dependências Python necessárias para executar o analisador fora do Docker. |
| `generate_article_graphs.ps1` | Script PowerShell para gerar automaticamente todos os gráficos principais do artigo. |
| `generate_article_graphs.sh` | Script Bash para Linux, WSL ou Docker para gerar automaticamente todos os gráficos principais do artigo. |
| `USA/` | CSVs organizados para gerar os gráficos da topologia USA. |
| `NSFNet/` | CSVs organizados para gerar os gráficos da topologia NSFNet. |
| `configs/` | Arquivos JSON que preservam métrica, escala, textos, posição da legenda, estilos e caminhos de saída dos gráficos do artigo. |
| `outputs/article/` | Pasta criada durante a execução para armazenar os gráficos e tabelas gerados. |

---

## Modos de uso

O SimGraph pode ser usado de duas formas principais: **GUI** e **CLI/headless**.

### 1. Modo GUI

Abre uma interface gráfica Tkinter para carregar CSVs, escolher métricas, ajustar estilos e gerar gráficos manualmente.

Comando:

```bash
python SimulationDataAnalyzer.py
```

Esse modo é recomendado para:

- Explorar os dados manualmente;
- Testar métricas;
- Ajustar textos, legendas e estilos visualmente;
- Exportar gráficos de forma interativa.

### 2. Modo CLI/headless

Gera gráficos diretamente pelo terminal a partir de um arquivo JSON de configuração.

Comando:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

Esse modo é recomendado para:

- Recriar os gráficos do artigo;
- Executar em Docker;
- Executar em WSL/Linux;
- Automatizar a geração de figuras;
- Reduzir dependência de cliques manuais;
- Repetir exatamente a mesma configuração visual e estatística.

---

## Requisitos

### Sistema operacional

O programa foi usado principalmente em:

- **Windows 11 Pro**
- **Python 3.10+**
- **Tkinter**
- **PowerShell**, para execução dos scripts `.ps1`

Também foi testado com Docker em ambiente Ubuntu/Linux/WSL.

### Python

Recomendado:

- **Python 3.10 ou mais recente**

Também pode funcionar em versões um pouco anteriores, mas o ideal é usar versões mais novas.

### Dependências Python

As dependências Python devem ser instaladas a partir de:

```text
requirements.txt
```

Dependências principais:

- `numpy`
- `pandas`
- `matplotlib`
- `scipy`

### Tkinter

A GUI usa **Tkinter** e **ttk**.

No Windows, o Tkinter normalmente já vem com o Python.

No Ubuntu/Debian, pode ser necessário instalar:

```bash
sudo apt update
sudo apt install -y python3-tk
```

Mesmo em modo CLI/headless, se o arquivo principal importar Tkinter no início, o ambiente precisa ter Tkinter disponível. Por isso o Dockerfile do analisador instala `tk` e `tcl`.

---

## Instalação local

### Windows 11 Pro

Entre na pasta do analisador:

```powershell
cd simulations\Data_Analyzer
```

Crie e ative um ambiente virtual:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
```

Se o PowerShell bloquear a ativação, libere apenas a sessão atual:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\.venv\Scripts\Activate.ps1
```

Instale as dependências:

```powershell
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
```

### Linux/WSL/Ubuntu

Entre na pasta do analisador:

```bash
cd simulations/Data_Analyzer
```

Instale os pacotes do sistema:

```bash
sudo apt update
sudo apt install -y python3 python3-venv python3-pip python3-tk
```

Crie e ative o ambiente virtual:

```bash
python3 -m venv .venv
source .venv/bin/activate
```

Instale as dependências:

```bash
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
```

---

## Execução

Esta seção reúne os comandos principais para executar o programa em modo GUI, em modo CLI/headless, com scripts de geração automática e com Docker.

### Abrir a interface gráfica

Dentro de `simulations/Data_Analyzer`:

```bash
python SimulationDataAnalyzer.py
```

### Executar em modo CLI/headless com um arquivo de configuração

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

### Listar métricas detectadas antes de gerar o gráfico

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json --list-metrics
```

### Gerar todos os gráficos do artigo no PowerShell

Dentro de `simulations\Data_Analyzer`:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

O `Set-ExecutionPolicy` com `-Scope Process` vale apenas para a sessão atual do PowerShell.

### Gerar todos os gráficos do artigo no Linux/WSL

Dentro de `simulations/Data_Analyzer`:

```bash
chmod +x ./generate_article_graphs.sh
./generate_article_graphs.sh
```

### Gerar todos os gráficos do artigo com Docker

A partir da raiz do repositório:

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  bash ./generate_article_graphs.sh
```

### Saída esperada da execução automática

Os gráficos e tabelas serão gerados em:

```text
outputs/article/
```

Exemplos de arquivos de saída:

```text
outputs/article/PBC_log_USA.svg
outputs/article/PBC_log_USA_ci.csv
outputs/article/PBC_log_USA_best.csv

outputs/article/PBBR_log_USA.svg
outputs/article/PBBR_log_USA_ci.csv
outputs/article/PBBR_log_USA_best.csv

outputs/article/PBC_Comp_USA.svg
outputs/article/PBC_Comp_NSFNet.svg
```

---

## Interface principal

A janela principal é dividida em seções como:

- **Actions (Ações)**
- **Metric selection (Seleção de métrica)**
- **Loaded files (Arquivos carregados)**
- **Plot setup (Configuração do gráfico)**
- **Statistics (Estatísticas)**
- **Appearance (Aparência)**
- **Margins (Margens)**

Essa organização ajuda a separar:

- As ações principais do programa;
- A escolha da métrica;
- A configuração do gráfico;
- As opções estatísticas;
- Os ajustes visuais.

Além das áreas principais da janela, o programa também possui menus superiores como **File**, **Theme** e **Export**, usados para carregar configurações, trocar o tema visual e exportar gráficos e tabelas.

### Fluxo básico pela GUI

1. Clique em **Load CSV files (Carregar arquivos CSV)**.
2. Escolha carregamento manual ou por pasta.
3. Selecione a métrica em **Metric selection (Seleção de métrica)**.
4. Escolha o tipo de gráfico em **Plot setup (Configuração do gráfico)**.
5. Ajuste as opções estatísticas em **Statistics (Estatísticas)**.
6. Ajuste idioma, escala, legenda, fontes e margens.
7. Clique em **Generate plot (Gerar gráfico)**.
8. Exporte o gráfico em SVG/PDF ou a tabela de IC em CSV.

---

## Área Actions (Ações)

Ações comuns:

- **Load CSV files (Carregar arquivos CSV)**
- **Configure algorithm names (Configurar nomes dos algoritmos)**
- **Edit graph texts (Editar textos dos gráficos)**
- **Edit component legends (Editar legendas dos componentes)**
- **Edit line styles (Editar estilos de linha)**
- **Select components for bar plot (Selecionar componentes para gráfico de barras)**
- **Generate plot (Gerar gráfico)**
- **Compute gains (Calcular ganhos)**

Algumas ações só ficam habilitadas depois que CSVs válidos são carregados.

### O que cada ação faz

- **Load CSV files**: abre a janela de carregamento manual ou por pasta;
- **Configure algorithm names**: permite renomear os algoritmos exibidos nos gráficos;
- **Edit graph texts**: altera títulos, eixos e textos dos gráficos;
- **Edit component legends**: altera os nomes usados nas legendas dos componentes;
- **Edit line styles**: altera cor, marcador e estilo de linha dos algoritmos;
- **Select components for bar plot**: escolhe quais componentes aparecem nos gráficos de barras;
- **Generate plot**: gera o gráfico com base nas opções atuais;
- **Compute gains**: abre a janela de configuração de ganhos, permitindo comparar um algoritmo com os demais e exportar os resultados em CSV.

---

## Carregamento de dados

O programa suporta **dois modos principais de carregamento**, acessados pela janela **Load CSV files (Carregar arquivos CSV)**.

### 1. Carregamento manual de arquivos CSV

Esse modo permite selecionar os arquivos diretamente, como em um fluxo tradicional de abertura de arquivos.

Ele é recomendado quando:

- Você já sabe exatamente quais arquivos quer comparar;
- Os arquivos estão espalhados em pastas diferentes;
- Você quer montar manualmente o conjunto de arquivos da análise.

#### Como funciona

1. Clique em **Load CSV files (Carregar arquivos CSV)**;
2. Na janela aberta, use a opção de carregamento manual;
3. Selecione um ou mais arquivos CSV;
4. O programa lê os arquivos, identifica as métricas disponíveis e atualiza a área de seleção de métricas.

#### Vantagens

- Maior controle sobre os arquivos carregados;
- Útil para análises pontuais;
- Mantém compatibilidade com versões anteriores do programa.

### Modo de compatibilidade

Na janela de carregamento também existe uma área de **Compatibility mode (Modo de compatibilidade)**, que permite usar o botão **Load files manually... (Carregar arquivos manualmente...)** para manter o fluxo tradicional de seleção direta de arquivos CSV.

### 2. Carregamento por pasta

Esse modo foi criado para facilitar análises em cenários com muitas subpastas e vários arquivos de resultados.

Ele é recomendado quando:

- Você tem uma pasta raiz com várias subpastas de experimentos;
- Cada subpasta contém arquivos CSV de tipos diferentes;
- Você quer carregar automaticamente arquivos do mesmo tipo em vários cenários.

#### Exemplo de estrutura

```text
results_root/
├── run_01/
│   ├── USA_IMPA_HXT_mo_0_00_mx_0_00_BlockingProbability.csv
│   ├── USA_IMPA_HXT_mo_0_00_mx_0_00_BitRateBlockingProbability.csv
│   ├── USA_IMPA_HXT_mo_0_00_mx_0_00_CrosstalkStatistics.csv
│   └── ...
├── run_02/
│   ├── USA_PABS_HXT_mo_0_00_mx_0_00_BlockingProbability.csv
│   ├── USA_PABS_HXT_mo_0_00_mx_0_00_BitRateBlockingProbability.csv
│   ├── USA_PABS_HXT_mo_0_00_mx_0_00_CrosstalkStatistics.csv
│   └── ...
```

#### Como funciona

1. Clique em **Load CSV files (Carregar arquivos CSV)**;
2. Na janela aberta, clique em **Browse folder (Selecionar pasta)**;
3. Selecione a **pasta raiz** que contém as subpastas com os resultados;
4. O programa varre as subpastas e tenta identificar os tipos de arquivo disponíveis;
5. Depois disso, ele lista os tipos encontrados na área **Available metric file types (Tipos de arquivos de métrica disponíveis)**;
6. Você escolhe quais tipos deseja carregar;
7. O programa agrupa os arquivos selecionados e os prepara para análise.

#### Como o programa identifica o tipo do arquivo

O programa tenta identificar o tipo do arquivo em **duas etapas**.

##### a) Pelo nome do arquivo

Primeiro, ele verifica o final do nome do arquivo, por exemplo:

- `_BlockingProbability`
- `_BitRateBlockingProbability`
- `_CrosstalkStatistics`
- `_ModulationUtilization`
- `_SpectrumUtilization`

##### b) Pelo conteúdo do CSV

Se o nome do arquivo não seguir o padrão esperado, o programa tenta identificar o tipo com base nos **dados internos do CSV**, analisando por exemplo:

- A coluna `Metrics`;
- Os nomes das métricas;
- A estrutura das colunas do arquivo.

Isso torna o carregamento por pasta mais robusto mesmo quando o nome do arquivo não está totalmente padronizado.

#### Vantagens

- Economiza tempo ao trabalhar com muitos arquivos;
- Facilita a comparação entre algoritmos e topologias;
- Reduz a necessidade de selecionar arquivo por arquivo;
- Funciona melhor mesmo em diretórios com nomenclatura incompleta ou inconsistente.

### Qual modo escolher?

Use o carregamento manual quando:

- Quiser selecionar arquivos específicos;
- Estiver testando poucos arquivos;
- Quiser controle total sobre os CSVs da análise.

Use o carregamento por pasta quando:

- Houver muitas subpastas;
- Os resultados estiverem organizados por experimento;
- Você quiser carregar automaticamente todos os arquivos de um mesmo tipo.

### Observações importantes sobre carregamento

- O programa espera arquivos CSV válidos e legíveis;
- Arquivos com coluna `Metrics` e colunas de replicação (`rep0`, `rep1`, etc.) tendem a ser identificados com mais facilidade;
- Mesmo no carregamento por pasta, o programa tenta preservar compatibilidade com diferentes estruturas de arquivos;
- Se algum arquivo não puder ser classificado pelo nome, o programa tentará classificá-lo pelo conteúdo antes de ignorá-lo.

---

## Estrutura dos arquivos CSV

O programa espera arquivos CSV com:

- Uma coluna chamada **`Metrics`**;
- Uma ou mais colunas de replicação como:
  - `rep0`
  - `rep1`
  - `rep2`
  - ...

Exemplo:

```csv
Metrics,rep0,rep1,rep2
BlockingProbability,0.0001,0.0002,0.00015
BlockingProbability,0.0004,0.00035,0.00038
```

O programa detecta automaticamente as colunas de replicação.

### Separador decimal

O programa aceita valores numéricos com:

- ponto (`.`);
- vírgula (`,`), convertida internamente quando necessário.

### Observação importante

Mesmo quando os arquivos possuem nomes diferentes ou organização variada entre pastas, o programa tenta identificar os conteúdos relevantes de forma automática durante o carregamento.

---

## Seleção de métrica

Depois de carregar os arquivos, o programa analisa a coluna `Metrics` e lista todas as métricas detectadas.

Depois disso, você pode escolher qual métrica será analisada e plotada.

### Dica

Se nenhuma métrica aparecer:

- Verifique se os CSVs possuem a coluna `Metrics`;
- Confira se os arquivos foram carregados corretamente;
- Tente o modo manual, caso o modo por pasta não encontre o padrão esperado.

---

## Plot setup (Configuração do gráfico)

### Tipo de gráfico

Opções:

- **Line plot (Gráfico de linha)**
- **Bar plot (Gráfico de barras)**

### Idioma do gráfico

Opções:

- **English (Inglês)**
- **Português**

Isso afeta os rótulos e textos dos gráficos. Não altera o idioma da interface do programa.

### Modo do gráfico de barras

As opções normalmente incluem:

- Valores absolutos empilhados;
- Porcentagens / normalizado empilhado.

### Tratamento da barra de erro em escala log

Controla como o limite inferior do intervalo de confiança é tratado quando o gráfico usa eixo Y em escala logarítmica.

Modos típicos:

- Esconder parte inferior quando CI inferior <= 0;
- Calcular intervalo em escala log;
- Marcar erro inferior truncado.

### Estilo da grade do eixo Y

Permite escolher como a grade horizontal é desenhada.

### Cargas e replicações

Campos típicos:

- **Initial load (Carga inicial)**
- **Load increment (Incremento de carga)**
- **Replications (0=auto) (Replicações, 0=automático)**
- **Bar plot load point (Ponto de carga do gráfico de barras)**
- **Specific loads filter (Filtro de cargas específicas)**

Exemplo de filtro de cargas:

```text
500, 1000, 1500
```

### Observação

O filtro de cargas permite restringir a análise apenas a determinados pontos de carga, o que é útil para comparações específicas.

---

## Statistics (Estatísticas)

A seção **Statistics (Estatísticas)** controla como o programa calcula os intervalos de confiança e como interpreta as replicações disponíveis nos CSVs.

### Confidence level (Nível de confiança)

Define o nível de confiança usado no cálculo do intervalo de confiança.

Exemplos:

- `90%`
- `95%`
- `99%`

Em geral, `95%` é o valor mais comum para gráficos de artigos e relatórios experimentais.

### CI (Confidence Interval / Intervalo de Confiança) method

Métodos disponíveis:

- **t-Student**
- **Bootstrap**

Esse campo define o método usado para calcular o intervalo de confiança das médias exibidas nos gráficos.

### Bootstrap resamples (Reamostragens bootstrap)

Define quantas reamostragens bootstrap serão usadas quando esse método estiver selecionado.

Quanto maior o número de reamostragens, mais estável tende a ser a estimativa, mas maior será o tempo de processamento.

### Replications (0=auto) (Replicações, 0=automático)

Quando definido como `0`, o programa tenta detectar automaticamente as colunas de replicação disponíveis, normalmente colunas com prefixo `rep`.

Use um valor manual quando quiser limitar explicitamente a quantidade de replicações analisadas.

### Truncated (Truncado)

Em gráficos com escala logarítmica, o limite inferior do intervalo de confiança pode ficar menor ou igual a zero. O campo `Truncated` indica quando a parte inferior da barra de erro precisou ser ajustada para não quebrar a escala logarítmica.

---

## Explicação dos métodos de intervalo de confiança

O programa oferece dois métodos principais para calcular o **CI (Confidence Interval / Intervalo de Confiança)**.

### Método t-Student

O método **t-Student** calcula o intervalo de confiança usando:

- A média dos resultados;
- O desvio padrão;
- O número de replicações;
- Uma fórmula estatística clássica.

Em termos práticos:

- Ele é mais tradicional;
- Costuma ser mais rápido;
- Funciona bem quando os dados têm comportamento mais regular.

**Resumo simples:**  
O método t-Student estima o intervalo de confiança a partir de uma fórmula baseada na média e na variação dos dados.

### Método Bootstrap

O método **Bootstrap** calcula o intervalo de confiança por **reamostragem**.

Em vez de usar apenas uma fórmula direta, ele:

1. Pega os valores das replicações;
2. Cria várias novas amostras com reposição;
3. Calcula várias médias simuladas;
4. Usa essas médias para estimar o intervalo de confiança.

Em termos práticos:

- Ele é mais empírico;
- Faz menos suposições sobre a distribuição dos dados;
- Pode ser útil quando os dados são mais irregulares ou assimétricos.

**Resumo simples:**  
O método Bootstrap estima o intervalo de confiança repetindo várias amostragens sobre os próprios dados.

### Diferença prática entre os dois

- **t-Student**: mais clássico, mais rápido e baseado em fórmula;
- **Bootstrap**: mais flexível, baseado em reamostragem e pode representar melhor dados menos regulares.

### Quando usar cada um

- Use **t-Student** quando quiser um método clássico e rápido;
- Use **Bootstrap** quando quiser uma estimativa mais baseada no comportamento real das replicações.

### Observação importante

Se você tiver poucas replicações, os dois métodos podem produzir resultados diferentes com mais facilidade. Em geral:

- **t-Student** tende a ser mais direto;
- **Bootstrap** tende a refletir mais o comportamento real da amostra.

---

## Appearance (Aparência)

O programa permite personalizar a aparência dos gráficos, incluindo:

- Tamanho da fonte dos rótulos dos eixos;
- Tamanho da fonte dos ticks;
- Tamanho da fonte da legenda;
- Posição da legenda;
- Negrito nos rótulos dos eixos;
- Negrito nos ticks dos eixos.

### Posição da legenda

Dependendo da versão, as opções podem incluir:

- **No legend (Sem legenda)**
- **Inside (best) (Dentro, melhor posição)**
- **Inside (upper right) (Dentro, superior direita)**
- **Inside (upper left) (Dentro, superior esquerda)**
- **Inside (lower right) (Dentro, inferior direita)**
- **Inside (lower left) (Dentro, inferior esquerda)**
- **Inside (center right) (Dentro, centro à direita)**
- **Inside (center left) (Dentro, centro à esquerda)**
- **Inside (upper center) (Dentro, centro superior)**
- **Inside (lower center) (Dentro, centro inferior)**
- **Inside (center) (Dentro, centro)**
- **Bottom (outside) (Embaixo, fora)**
- **Top (outside) (Em cima, fora)**
- **Right (outside) (À direita, fora)**
- **Left (outside) (À esquerda, fora)**

### Dica de uso

- Use posições **inside (dentro)** quando quiser manter tudo dentro do gráfico;
- Use posições **outside (fora)** quando houver muitas curvas ou componentes;
- Use **No legend (Sem legenda)** quando o gráfico for apenas para inspeção visual rápida.

### Observação

A troca do tema visual da aplicação é feita pelo menu **Theme**, e não dentro da aba **Appearance (Aparência)**.

---

## Margins (Margens)

Esses campos controlam o espaçamento extra ao redor dos gráficos.

### Margens do eixo X

- **Left margin (Margem esquerda)**
- **Right margin (Margem direita)**
- **Bar-plot X margin (Margem X do gráfico de barras)**

### Margens do eixo Y

- **Linear bottom margin (Margem inferior linear)**
- **Linear top margin (Margem superior linear)**
- **Log bottom factor (Fator inferior log)**
- **Log top factor (Fator superior log)**

Esses valores são úteis quando curvas ou rótulos ficam muito próximos das bordas do gráfico.

### Exemplo prático

Se a curva estiver muito distante do topo do gráfico, você pode reduzir o **Linear top margin**.  
Se estiver muito encostada, pode aumentar esse valor.

---

## Arquivos de configuração JSON

Os arquivos JSON da pasta `configs/` controlam a geração automática dos gráficos.

Exemplo simplificado:

```json
{
  "input": "USA",
  "output": "outputs/article/PBC_log_USA.svg",
  "ci_output": "outputs/article/PBC_log_USA_ci.csv",
  "best_output": "outputs/article/PBC_log_USA_best.csv",
  "metric_type": ["BlockingProbability"],
  "metric": "Blocking probability",
  "plot": "line",
  "log_scale": true,
  "language": "pt",
  "init_load": 500,
  "load_step": 250,
  "replications": 0,
  "legend_position": "Inside (lower right)"
}
```

### Campos principais

| Campo | Função |
|---|---|
| `input` | Pasta de entrada, como `USA` ou `NSFNet` |
| `output` | Caminho do gráfico exportado |
| `ci_output` | Caminho da tabela de intervalo de confiança |
| `best_output` | Caminho da tabela de melhor algoritmo por carga |
| `metric_type` | Tipo de arquivo CSV a carregar |
| `metric` | Valor exato da coluna `Metrics` |
| `plot` | Tipo de gráfico: `line` ou `bar` |
| `bar_mode` | Modo do gráfico de barras: `absolute` ou `percent` |
| `log_scale` | Usa escala logarítmica no eixo Y |
| `language` | Idioma dos textos do gráfico |
| `init_load` | Carga inicial |
| `load_step` | Incremento entre cargas |
| `replications` | Número de replicações; `0` usa detecção automática |
| `legend_position` | Posição da legenda |

### Configuração dos gráficos PBC

Os gráficos PBC usam:

```json
"metric_type": ["BlockingProbability"],
"metric": "Blocking probability",
"plot": "line",
"log_scale": true
```

Textos esperados:

```text
Probabilidade de Bloqueio de Circuito (log10)
Carga da rede (Erlangs)
```

### Configuração dos gráficos PBBR

Os gráficos PBBR usam:

```json
"metric_type": ["BitRateBlockingProbability"],
"metric": "BitRate blocking probability",
"plot": "line",
"log_scale": true
```

Textos esperados:

```text
Probabilidade de Bloqueio de BitRate (log10)
Carga da rede (Erlangs)
```

### Configuração dos gráficos de componentes

Os gráficos de componentes usam:

```json
"metric_type": ["BlockingProbability"],
"plot": "bar",
"bar_mode": "percent",
"legend_position": "Bottom (outside)"
```

Textos esperados:

```text
% das Componentes de Bloqueio
Algoritmos de atribuição de potência
```

Componentes usados:

```text
Blocking probability by QoTN
Blocking probability by QoTO
Blocking probability by crosstalk
Blocking probability by crosstalk in other
```

Rótulos exibidos:

```text
OSNRN
OSNRO
XTN
XTO
```

### Codificação dos configs

Para evitar problemas de codificação no Windows/PowerShell, os arquivos JSON podem ser salvos em ASCII com escapes Unicode.

Exemplo:

```json
"line_y": "Probabilidade de Bloqueio de Circuito (log10)"
```

pode aparecer no arquivo como escapes Unicode, mas o Python renderiza o texto corretamente no gráfico.

---

## Geração automática dos gráficos do artigo

A forma recomendada para recriar todos os gráficos principais é usar os scripts:

```text
generate_article_graphs.ps1
generate_article_graphs.sh
```

Esses scripts executam o `SimulationDataAnalyzer.py` várias vezes, uma vez para cada arquivo JSON da pasta `configs/`.

### Estrutura esperada

Dentro da pasta:

```text
simulations/Data_Analyzer/
```

a estrutura esperada é:

```text
simulations/Data_Analyzer/
├── SimulationDataAnalyzer.py
├── generate_article_graphs.ps1
├── generate_article_graphs.sh
├── requirements.txt
├── USA/
├── NSFNet/
└── configs/
    ├── config_article_PBC_log_USA.json
    ├── config_article_PBC_log_NSFNet.json
    ├── config_article_PBBR_log_USA.json
    ├── config_article_PBBR_log_NSFNet.json
    ├── config_article_PBC_Comp_USA.json
    └── config_article_PBC_Comp_NSFNet.json
```

### Gráficos gerados

| Configuração | Gráfico |
|---|---|
| `config_article_PBC_log_USA.json` | PBC na topologia USA |
| `config_article_PBC_log_NSFNet.json` | PBC na topologia NSFNet |
| `config_article_PBBR_log_USA.json` | PBBR na topologia USA |
| `config_article_PBBR_log_NSFNet.json` | PBBR na topologia NSFNet |
| `config_article_PBC_Comp_USA.json` | Componentes de bloqueio na topologia USA |
| `config_article_PBC_Comp_NSFNet.json` | Componentes de bloqueio na topologia NSFNet |

Onde:

- **PBC**: Probabilidade de Bloqueio de Circuito;
- **PBBR**: Probabilidade de Bloqueio de BitRate;
- **PBC_Comp**: decomposição percentual dos componentes de bloqueio.

### Executar no Windows PowerShell

Dentro de `simulations\Data_Analyzer`:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

### Executar no Linux/WSL

Dentro de `simulations/Data_Analyzer`:

```bash
chmod +x ./generate_article_graphs.sh
./generate_article_graphs.sh
```

### Saída esperada

Os arquivos serão gerados em:

```text
outputs/article/
```

Exemplos:

```text
outputs/article/PBC_log_USA.svg
outputs/article/PBC_log_USA_ci.csv
outputs/article/PBC_log_USA_best.csv

outputs/article/PBBR_log_USA.svg
outputs/article/PBBR_log_USA_ci.csv
outputs/article/PBBR_log_USA_best.csv

outputs/article/PBC_Comp_USA.svg
outputs/article/PBC_Comp_NSFNet.svg
```

---

## Uso com Docker

O uso com Docker é recomendado para reduzir problemas de ambiente.

Os comandos abaixo devem ser executados na raiz do repositório.

### Build da imagem do analisador

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .
```

### Gerar todos os gráficos do artigo com Docker

```bash
docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  bash ./generate_article_graphs.sh
```

### Gerar um único gráfico com Docker

```bash
docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  python ./SimulationDataAnalyzer.py --config ./configs/config_article_PBC_log_USA.json
```

### Verificar a saída

```bash
ls simulations/Data_Analyzer/outputs/article
```

---

## Personalização dos gráficos

O programa permite personalizar:

- Textos dos eixos;
- Título;
- Legenda;
- Nomes dos algoritmos;
- Rótulos dos componentes;
- Cores;
- Marcadores;
- Estilos de linha;
- Fontes;
- Margens;
- Posição da legenda.

### Edição dos textos dos gráficos

O programa permite personalizar títulos e rótulos dos eixos dos gráficos gerados.

Você pode usar placeholders como:

- `{metric}`;
- `{load}`.

Exemplo:

```text
{metric} at {load} Erlangs
```

### Quando isso é útil

Essa personalização é especialmente útil quando você quer:

- Gerar figuras para artigos;
- Alternar entre inglês e português;
- Ajustar títulos para apresentações e relatórios.

### Edição das legendas dos componentes

Para gráficos de componentes de bloqueio, você pode renomear os rótulos exibidos na legenda.

Exemplo de mapeamento:

- QoTN → OSNRN;
- QoTO → OSNRO;
- crosstalk → XTN;
- crosstalk in other → XTO;
- lack of transmitters → Transmissores;
- lack of receivers → Receptores;
- fragmentation → Fragmentação;
- other → Outros.

### Observação

A ordem de exibição dos componentes pode ser configurada no código e influencia tanto a legenda quanto a montagem das barras empilhadas.

### Edição dos estilos de linha

O programa suporta estilos de linha personalizados por algoritmo.

Você pode ajustar:

- Cor da linha;
- Marcador;
- Estilo da linha.

Isso é especialmente útil em gráficos de linha quando muitos algoritmos são exibidos juntos.

### Exemplo de uso

Você pode definir:

- Uma cor distinta por algoritmo;
- Marcadores diferentes;
- Estilos de linha distintos para facilitar a leitura visual em gráficos com muitas curvas.

---

## Geração de gráficos

### Gráfico de linha

Use gráficos de linha para comparar o desempenho dos algoritmos ao longo dos pontos de carga.

Recursos típicos:

- Curva média por algoritmo;
- Barras de erro com intervalo de confiança;
- Escala log opcional;
- Melhor algoritmo por carga impresso no terminal/console.

### Gráfico de barras

Use gráficos de barras para analisar a decomposição dos componentes.

Modos típicos:

- Valores absolutos empilhados;
- Porcentagens empilhadas.

### Observação

Os gráficos de linha são mais indicados para acompanhar o comportamento da métrica em função da carga.  
Os gráficos de barras são mais indicados para comparar a composição dos bloqueios ou distribuições em um ponto específico.

---

## Saída do intervalo de confiança

Quando um gráfico de linha é gerado, o programa imprime no terminal uma tabela de **CI (Confidence Interval / Intervalo de Confiança)** com campos como:

- `Load (Carga)`;
- `Mean (Média)`;
- `CI lower (Limite inferior do IC)`;
- `CI upper (Limite superior do IC)`;
- `Truncated (Truncado)`.

Isso é útil para inspeção numérica da incerteza em torno da média.

### O que significa “Truncated (Truncado)”

Esse campo indica se a parte inferior da barra de erro precisou ser ajustada, por exemplo em casos de escala log ou quando o limite inferior ficaria inválido.

---

## Cálculo de ganho

O programa pode calcular o ganho de um algoritmo em relação a outros usando uma métrica selecionada.

Fórmula:

```text
Gain = (Rother - Ralgo) / Rother
```

Onde:

- `Rother` é a métrica do algoritmo de referência;
- `Ralgo` é a métrica do algoritmo selecionado.

O ganho normalmente é apresentado em porcentagem.

A tabela de ganhos também pode ser exportada.

### Interpretação

- Ganho positivo: o algoritmo escolhido teve desempenho melhor;
- Ganho negativo: o algoritmo escolhido teve desempenho pior;
- Valor zero: os dois resultados foram equivalentes.

---

## Salvar e carregar customizações

O programa suporta exportação/importação de customizações do usuário em arquivos JSON.

### Save customizations (Salvar customizações)

Salva configurações como:

- Idioma do gráfico;
- Modo do gráfico de barras;
- Tratamento de erro em escala log;
- Métrica selecionada;
- Tema;
- Estilos de linha;
- Textos dos gráficos;
- Rótulos da legenda dos componentes;
- Seleção de componentes;
- Posição da legenda;
- Fontes;
- Margens;
- Outras preferências do usuário, dependendo da versão.

### Load customizations (Carregar customizações)

Restaura preferências salvas anteriormente.

Isso é útil quando você usa o mesmo layout e as mesmas configurações com frequência.

### Dica

Salve uma customização para cada tipo de análise, por exemplo:

- Uma para PBC;
- Uma para PBBR;
- Uma para gráficos em português;
- Outra para gráficos em inglês.

---

## Opções de exportação

Dependendo da versão atual, o programa suporta:

### Exportação de gráficos

No menu **Export (Exportar)**:

- **Save last plot (SVG) (Salvar último gráfico em SVG)**;
- **Save last plot (PDF) (Salvar último gráfico em PDF)**.

### Exportação de tabelas

- **Export last CI table (CSV) (Exportar última tabela de IC em CSV)**

### Quando usar cada formato

- **SVG**: ideal para edição posterior e uso em artigos;
- **PDF**: ideal para compartilhamento e impressão;
- **CSV**: ideal para análise numérica e documentação dos intervalos de confiança.

---

## Saídas geradas

### Gráficos de linha

Podem gerar:

```text
*.svg
*_ci.csv
*_best.csv
```

Exemplo:

```text
PBC_log_USA.svg
PBC_log_USA_ci.csv
PBC_log_USA_best.csv
```

### Gráficos de componentes

Normalmente geram apenas o gráfico:

```text
PBC_Comp_USA.svg
PBC_Comp_NSFNet.svg
```

### Significado dos arquivos

| Arquivo | Conteúdo |
|---|---|
| `*.svg` | Gráfico exportado |
| `*.pdf` | Gráfico exportado em PDF, quando usado pela GUI/exportação |
| `*_ci.csv` | Tabela de intervalo de confiança |
| `*_best.csv` | Melhor algoritmo por carga |

---

## Exemplos de uso

### Instalar dependências localmente

```bash
python -m pip install -r requirements.txt
```

### Instalação opcional do Tkinter no Linux

```bash
sudo apt-get install python3-tk
```

### Abrir a GUI

```bash
python SimulationDataAnalyzer.py
```

### Gerar um gráfico com config

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

### Listar métricas detectadas

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json --list-metrics
```

### Gerar todos os gráficos do artigo no Windows

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

### Gerar todos os gráficos do artigo no Linux/WSL

```bash
chmod +x ./generate_article_graphs.sh
./generate_article_graphs.sh
```

---

## Solução de problemas

### 1. Instalação e dependências

#### `ModuleNotFoundError`

Ative o ambiente virtual e instale as dependências:

```bash
python -m pip install -r requirements.txt
```

#### Tkinter indisponível

No Ubuntu/Debian:

```bash
sudo apt install -y python3-tk
```

No Windows, reinstale o Python com suporte a Tcl/Tk, se necessário.

#### PowerShell bloqueia a ativação do venv

Use apenas na sessão atual:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\.venv\Scripts\Activate.ps1
```

### 2. Problemas ao gerar gráficos por scripts/configs

#### `generate_article_graphs.ps1` mostra caracteres estranhos

Se o terminal exibir textos como `grÃ¡fico`, o problema é codificação do PowerShell.

Soluções:

- Usar mensagens sem acentos nos scripts `.ps1`;
- Salvar o script em UTF-8 com BOM;
- Manter os textos dos gráficos nos arquivos JSON, que são lidos corretamente pelo Python.

#### `Metric ... was not found`

O campo `metric` do JSON deve corresponder exatamente a um valor da coluna `Metrics` dos CSVs.

Exemplos:

```json
"metric": "Blocking probability"
```

para PBC, e:

```json
"metric": "BitRate blocking probability"
```

para PBBR.

#### Os gráficos PBBR saem errados

Verifique se o config usa:

```json
"metric_type": ["BitRateBlockingProbability"],
"metric": "BitRate blocking probability"
```

#### A legenda aparece em posição diferente do artigo

Ajuste:

```json
"legend_position": "Inside (lower right)"
```

para PBC/PBBR, ou:

```json
"legend_position": "Bottom (outside)"
```

para gráficos de componentes.

#### O gráfico começa em carga zero

Verifique no config:

```json
"init_load": 500,
"load_step": 250
```

e confirme se a versão do `SimulationDataAnalyzer.py` usa os valores reais de carga para definir o eixo X.

#### Algoritmos aparecem como nomes de métricas

Esse problema indica que a inferência dos nomes das séries está incorreta.

A versão atual deve manter separadas duas coisas:

- `metric`: valor da coluna `Metrics`;
- algoritmo: nome inferido do arquivo/pasta de resultado.

Use a versão unificada do `SimulationDataAnalyzer.py`, evitando manter um `SimulationDataAnalyzer_cli.py` separado.

#### Arquivos não são gerados

Verifique:

- Se a pasta `configs/` existe;
- Se os caminhos `input` dos configs existem;
- Se há permissão de escrita em `outputs/article/`;
- Se o ambiente virtual está ativo;
- Se as dependências foram instaladas.

### 3. Problemas com CSVs e carregamento de dados

#### Os CSVs carregam, mas nenhuma métrica aparece

Verifique se:

- O arquivo tem coluna `Metrics`;
- As colunas de replicação começam com `rep`;
- O CSV não está corrompido;
- Separadores e codificação estão corretos.

#### O carregamento por pasta não encontra os tipos corretamente

Tente:

- Verificar se os arquivos possuem nomes parcialmente padronizados;
- Usar o carregamento manual;
- Confirmar se os CSVs têm conteúdo coerente, especialmente a coluna `Metrics`.

#### O gráfico aparece, mas alguns algoritmos não são mostrados

Possíveis motivos:

- Todos os valores daquela métrica são zero ou inválidos;
- As colunas de replicação não foram detectadas;
- O filtro de cargas excluiu todos os pontos.

#### Problemas com escala log

Se limites inferiores do CI virarem zero ou negativos, use outra opção em:

- **Log-scale error bar handling (Tratamento da barra de erro em escala log)**

#### Exportação não funciona

Garanta que:

- Um gráfico já foi gerado;
- O arquivo de destino não está aberto em outro programa;
- Você tem permissão de escrita na pasta de destino.

#### A legenda ou o gráfico ficou poluído

Tente:

- Reduzir a quantidade de algoritmos exibidos;
- Mover a legenda para fora do gráfico;
- Usar fontes menores;
- Ajustar as margens.

---

## Boas práticas para reprodução

Para reproduzir os gráficos do artigo:

1. Use a versão unificada do `SimulationDataAnalyzer.py`;
2. Use os arquivos JSON da pasta `configs/`;
3. Execute `generate_article_graphs.ps1` ou `generate_article_graphs.sh`;
4. Evite editar manualmente os gráficos gerados;
5. Versione os configs junto com o código;
6. Mantenha os CSVs de entrada no repositório ou indique claramente como gerá-los;
7. Use Docker quando quiser evitar diferenças de ambiente entre Windows, Linux e WSL.

---

## Ambiente sugerido

Para melhores resultados:

- Python 3.10+;
- Windows 10/11 ou Linux/WSL;
- Arquivos CSV em UTF-8;
- Resultados organizados em pastas e por tipo de métrica;
- Docker, quando o objetivo for reprodução com ambiente isolado.

---

## Resumo final

O **SimGraph** foi projetado para facilitar a análise comparativa de resultados de simulações, oferecendo:

- Carregamento flexível de CSVs;
- Identificação automática de métricas;
- Geração de gráficos configuráveis;
- Cálculo de intervalos de confiança;
- Cálculo de ganho entre algoritmos;
- Exportação para formatos úteis em pesquisa e documentação.

Para uso exploratório, a GUI é mais conveniente.

Para reprodução de figuras, especialmente em avaliação de artefatos, o modo CLI com configs é o fluxo recomendado:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

Para gerar todos os gráficos principais:

```bash
./generate_article_graphs.sh
```

ou, no Windows PowerShell:

```powershell
.\generate_article_graphs.ps1
```

Os resultados ficam em:

```text
outputs/article/
```
