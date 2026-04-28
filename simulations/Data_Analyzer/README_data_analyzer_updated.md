# SimGraph — Simulation Graph Analyzer  
README (PT-BR)

## Visão geral

**SimGraph — Simulation Graph Analyzer** é um aplicativo desktop em Python para carregar arquivos CSV com resultados de simulações, comparar algoritmos e gerar gráficos para métricas como:

- Probabilidade de bloqueio;
- Probabilidade de bloqueio por taxa de bits;
- Estatísticas de crosstalk;
- Utilização de modulação;
- Utilização de espectro.

O programa também oferece:

- Gráficos de linha e de barras;
- Intervalos de confiança;
- Comparação de ganho entre algoritmos;
- Personalização de rótulos, textos e aparência dos gráficos;
- Personalização de estilos de linha por algoritmo;
- Exportação dos gráficos em **SVG** e **PDF**;
- Exportação de tabelas de intervalo de confiança em **CSV**;
- Salvamento e carregamento de customizações do usuário em **JSON**;
- Execução em modo **CLI/headless** por arquivos de configuração JSON;
- Geração automática dos gráficos do artigo por meio da pasta `configs/` e dos scripts `generate_article_graphs.ps1` e `generate_article_graphs.sh`.

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

## Requisitos

### Sistema operacional
O programa é adequado para Windows e também deve funcionar em Linux/macOS, desde que Python, Tkinter e as bibliotecas necessárias estejam instalados.

### Python
Recomendado:

- **Python 3.10 ou mais recente**

Também pode funcionar em versões um pouco anteriores, mas o ideal é usar versões mais novas.

---

## Dependências

Instale estes pacotes Python:

```bash
pip install numpy pandas matplotlib scipy
```

### Tkinter
A interface usa **Tkinter** e **ttk**.

- No Windows, o Tkinter normalmente já vem com o Python;
- No Linux, pode ser necessário instalar separadamente.

Exemplo no Debian/Ubuntu:

```bash
sudo apt-get install python3-tk
```

---

## Arquivo principal do programa

O programa principal é:

```text
SimulationDataAnalyzer.py
```

A versão atual pode ser usada de duas formas:

1. **Modo GUI**, com interface gráfica Tkinter;
2. **Modo CLI/headless**, sem abrir a interface, usando arquivos JSON de configuração.

### Abrir a interface gráfica

```bash
python SimulationDataAnalyzer.py
```

### Executar em modo CLI/headless com um arquivo de configuração

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

O modo CLI é usado principalmente para reproduzir automaticamente os gráficos do artigo e para executar o analisador em ambientes sem interface gráfica, como Docker, WSL ou servidores.

---

## Modo CLI/headless

O modo CLI permite gerar gráficos diretamente pelo terminal, sem interação manual na interface.

Esse modo é acionado quando você informa um arquivo de configuração com `--config`:

```bash
python SimulationDataAnalyzer.py --config caminho/do/arquivo.json
```

Também é possível listar as métricas detectadas antes de gerar o gráfico:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json --list-metrics
```

### Para que serve o modo CLI

Use o modo CLI quando quiser:

- Gerar gráficos automaticamente;
- Reproduzir figuras do artigo;
- Executar o analisador em Docker, WSL ou servidor;
- Evitar cliques manuais na interface gráfica;
- Repetir exatamente a mesma configuração visual e estatística.

### Relação entre GUI e CLI

A recomendação atual é manter apenas um arquivo principal:

```text
SimulationDataAnalyzer.py
```

A GUI e o CLI devem usar a mesma lógica interna de leitura dos CSVs, identificação de métricas, inferência dos algoritmos, cálculo estatístico e geração dos gráficos. Isso evita divergências como usar nomes de métricas no lugar dos nomes dos algoritmos.

---

## Geração automática dos gráficos do artigo com configs

A forma recomendada para recriar os gráficos do artigo é usar a pasta `configs/` em conjunto com os scripts:

```text
generate_article_graphs.ps1
generate_article_graphs.sh
```

Esses scripts chamam o `SimulationDataAnalyzer.py` em modo CLI para cada gráfico do artigo.

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

### Gerar todos os gráficos no PowerShell

No Windows PowerShell, entre na pasta:

```powershell
cd simulations\Data_Analyzer
```

Depois execute:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

O `Set-ExecutionPolicy` com `-Scope Process` vale apenas para a sessão atual do PowerShell.

### Gerar todos os gráficos no Linux/WSL

No Linux ou WSL, entre na pasta:

```bash
cd simulations/Data_Analyzer
```

Depois execute:

```bash
chmod +x ./generate_article_graphs.sh
./generate_article_graphs.sh
```

### Saída esperada

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

### Gerar apenas um gráfico

Também é possível executar um único arquivo de configuração.

Exemplo para PBC na topologia USA:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

Exemplo para PBBR na topologia NSFNet:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBBR_log_NSFNet.json
```

Exemplo para componentes na topologia USA:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_Comp_USA.json
```

### Campos principais de um arquivo config

Um arquivo de configuração JSON pode conter campos como:

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

Campos importantes:

- `input`: pasta de entrada, como `USA` ou `NSFNet`;
- `output`: caminho do gráfico exportado;
- `ci_output`: caminho da tabela de intervalo de confiança;
- `best_output`: caminho da tabela de melhor algoritmo por carga;
- `metric_type`: tipo de arquivo CSV a ser carregado;
- `metric`: valor exato da coluna `Metrics` a ser plotado;
- `plot`: tipo de gráfico, como `line` ou `bar`;
- `log_scale`: usa escala logarítmica no eixo Y;
- `language`: idioma usado nos textos do gráfico;
- `init_load`: primeira carga considerada;
- `load_step`: incremento entre cargas;
- `replications`: número de replicações; `0` usa detecção automática;
- `legend_position`: posição da legenda.

### Configs dos gráficos PBC

Os gráficos PBC usam:

```json
"metric_type": ["BlockingProbability"],
"metric": "Blocking probability",
"plot": "line",
"log_scale": true
```

Textos configurados:

```text
Probabilidade de Bloqueio de Circuito (log10)
Carga da rede (Erlangs)
```

### Configs dos gráficos PBBR

Os gráficos PBBR usam:

```json
"metric_type": ["BitRateBlockingProbability"],
"metric": "BitRate blocking probability",
"plot": "line",
"log_scale": true
```

Textos configurados:

```text
Probabilidade de Bloqueio de BitRate (log10)
Carga da rede (Erlangs)
```

### Configs dos gráficos de componentes

Os gráficos de componentes usam:

```json
"metric_type": ["BlockingProbability"],
"plot": "bar",
"bar_mode": "percent",
"legend_position": "Bottom (outside)"
```

Textos configurados:

```text
% das Componentes de Bloqueio
Algoritmos de atribuição de potência
```

Componentes usados nos gráficos do artigo:

```text
Blocking probability by QoTN
Blocking probability by QoTO
Blocking probability by crosstalk
Blocking probability by crosstalk in other
```

Eles são exibidos com as legendas:

```text
OSNRN
OSNRO
XTN
XTO
```

### Observação sobre codificação dos configs

Os arquivos JSON de configuração podem ser salvos em ASCII usando escapes Unicode. Isso reduz problemas de codificação no Windows/PowerShell e ainda permite que o Python leia corretamente textos com acentos.

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

### Confidence level (Nível de confiança)
Exemplos:
- `90%`
- `95%`
- `99%`

### CI (Confidence Interval / Intervalo de Confiança) method
Métodos disponíveis:
- **t-Student**
- **Bootstrap**

### Bootstrap resamples (Reamostragens bootstrap)
Define quantas reamostragens bootstrap serão usadas quando esse método estiver selecionado.

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

## Estrutura esperada dos CSVs

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

## Formas de carregar dados

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

---

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
O programa tenta identificar o tipo do arquivo em **duas etapas**:

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

---

### Qual modo escolher?

#### Use o carregamento manual quando:
- Quiser selecionar arquivos específicos;
- Estiver testando poucos arquivos;
- Quiser controle total sobre os CSVs da análise.

#### Use o carregamento por pasta quando:
- Houver muitas subpastas;
- Os resultados estiverem organizados por experimento;
- Você quiser carregar automaticamente todos os arquivos de um mesmo tipo.

---

#### 9. `generate_article_graphs.ps1` mostra caracteres estranhos no terminal

Se o terminal exibir textos como `grÃ¡fico`, o problema é a codificação do PowerShell. Para evitar isso, prefira mensagens sem acentos nos scripts `.ps1`, ou salve o script em UTF-8 com BOM.

Os textos dos gráficos podem continuar em português com acentos, pois eles são lidos pelo Python a partir dos arquivos JSON.

### 10. Um gráfico por config falha com `Metric ... was not found`

Verifique se o campo `metric` do JSON corresponde exatamente a um valor existente na coluna `Metrics` dos CSVs.

Exemplos:

```json
"metric": "Blocking probability"
```

para PBC, e:

```json
"metric": "BitRate blocking probability"
```

para PBBR.

Também verifique se `metric_type` aponta para o tipo correto:

```json
"metric_type": ["BlockingProbability"]
```

ou:

```json
"metric_type": ["BitRateBlockingProbability"]
```

### 11. A legenda aparece em posição diferente do artigo

Ajuste o campo `legend_position` no arquivo JSON.

Exemplos:

```json
"legend_position": "Inside (lower right)"
```

para gráficos de linha, ou:

```json
"legend_position": "Bottom (outside)"
```

para gráficos de componentes.

---

## Observações importantes sobre carregamento
- O programa espera arquivos CSV válidos e legíveis;
- Arquivos com coluna `Metrics` e colunas de replicação (`rep0`, `rep1`, etc.) tendem a ser identificados com mais facilidade;
- Mesmo no carregamento por pasta, o programa tenta preservar compatibilidade com diferentes estruturas de arquivos;
- Se algum arquivo não puder ser classificado pelo nome, o programa tentará classificá-lo pelo conteúdo antes de ignorá-lo.

---

## Fluxo rápido de uso

Há dois fluxos principais: uso interativo pela GUI e geração automática por configs.

### Fluxo rápido pela GUI

1. Abra o programa;
2. Clique em **Load CSV files (Carregar arquivos CSV)**;
3. Carregue os arquivos por seleção manual ou por pasta;
4. Escolha a métrica desejada em **Metric selection (Seleção de métrica)**;
5. Escolha o tipo de gráfico em **Plot setup (Configuração do gráfico)**;
6. Ajuste as opções estatísticas e visuais, se necessário;
7. Clique em **Generate plot (Gerar gráfico)**;
8. Exporte o gráfico ou a tabela de IC, se desejar.

### Fluxo rápido por configs

1. Entre em `simulations/Data_Analyzer`;
2. Verifique se a pasta `configs/` está presente;
3. Execute `generate_article_graphs.ps1` no Windows ou `generate_article_graphs.sh` no Linux/WSL;
4. Consulte os arquivos gerados em `outputs/article/`.

PowerShell:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

Linux/WSL:

```bash
chmod +x ./generate_article_graphs.sh
./generate_article_graphs.sh
```

---

## Edição dos textos dos gráficos

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

---

## Edição das legendas dos componentes

Para gráficos de componentes de bloqueio, você pode renomear os rótulos exibidos na legenda.

Exemplo de mapeamento dos componentes:

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

---

## Edição dos estilos de linha

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

## Exemplos de uso

### Instalar dependências

```bash
pip install numpy pandas matplotlib scipy
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

### 1. `ModuleNotFoundError`
Instale os pacotes ausentes:

```bash
pip install numpy pandas matplotlib scipy
```

### 2. Tkinter indisponível
Instale o Tkinter para o seu sistema.  
No Windows, reinstalar o Python com suporte a Tcl/Tk pode ajudar.

### 3. Os CSVs carregam, mas nenhuma métrica aparece
Verifique se:
- O arquivo tem coluna `Metrics`;
- As colunas de replicação começam com `rep`;
- O CSV não está corrompido;
- Separadores e codificação estão corretos.

### 4. O gráfico aparece, mas alguns algoritmos não são mostrados
Possíveis motivos:
- Todos os valores daquela métrica são zero ou inválidos;
- As colunas de replicação não foram detectadas;
- O filtro de cargas excluiu todos os pontos.

### 5. Problemas com escala log
Se limites inferiores do CI virarem zero ou negativos, use outra opção em:
- **Log-scale error bar handling (Tratamento da barra de erro em escala log)**

### 6. Exportação não funciona
Garanta que:
- Um gráfico já foi gerado;
- O arquivo de destino não está aberto em outro programa;
- Você tem permissão de escrita na pasta de destino.

### 7. O carregamento por pasta não encontra os tipos corretamente
Tente:
- Verificar se os arquivos possuem nomes parcialmente padronizados;
- Usar o carregamento manual;
- Confirmar se os CSVs têm conteúdo coerente, especialmente a coluna `Metrics`.

### 8. A legenda ou o gráfico ficou poluído
Tente:
- Reduzir a quantidade de algoritmos exibidos;
- Mover a legenda para fora do gráfico;
- Usar fontes menores;
- Ajustar as margens.

---

## Observações

- O programa é voltado para análise de arquivos CSV com resultados de simulações;
- Ele é especialmente útil para comparar múltiplos algoritmos sob diferentes cargas de rede;
- Manter nomes de arquivo consistentes ajuda bastante no carregamento por pasta;
- O carregamento por conteúdo melhora a robustez quando o nome do arquivo não segue padrão;
- Para reproduzir os gráficos do artigo, prefira os arquivos JSON da pasta `configs/`, pois eles preservam métrica, escala, textos, posição da legenda e caminhos de saída.

---

## Ambiente sugerido

Para melhores resultados:
- Python 3.10+;
- Windows 10/11;
- Arquivos CSV em UTF-8;
- Resultados organizados em pastas e por tipo de métrica.

---

## Resumo final

O **SimGraph** foi projetado para facilitar a análise comparativa de resultados de simulações, oferecendo:
- Carregamento flexível de CSVs;
- Identificação automática de métricas;
- Geração de gráficos configuráveis;
- Cálculo de intervalos de confiança;
- Cálculo de ganho entre algoritmos;
- Exportação para formatos úteis em pesquisa e documentação.

Se você utiliza muitos resultados organizados por topologia, algoritmo e carga, o modo de carregamento por pasta tende a ser o mais prático. Se você quer controle total sobre quais arquivos comparar, o carregamento manual é a melhor opção.

---
