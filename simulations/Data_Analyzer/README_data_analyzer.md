# SimGraph - Simulation Graph Analyzer — README

## Visão geral

**SimGraph - Simulation Graph Analyzer** é um aplicativo desktop em Python para carregar arquivos CSV com resultados de simulações, comparar algoritmos e gerar gráficos para métricas como:

- Probabilidade de bloqueio;
- Probabilidade de bloqueio por taxa de bits;
- Estatísticas de crosstalk;
- Utilização de modulação;
- Utilização de espectro.

O programa também oferece:

- Gráficos de linha e de barras;
- Intervalos de confiança;
- Comparação de ganho entre algoritmos;
- Personalização de rótulos e textos dos gráficos;
- Personalização de estilos de linha;
- Exportação dos gráficos em **SVG** e **PDF**;
- Exportação de tabelas de IC em **CSV**;
- Salvamento e carregamento de customizações do usuário em **JSON**.

---

## O que o programa faz

De forma prática, o programa permite:

1. Carregar arquivos CSV de resultados de simulações;
2. Identificar automaticamente as métricas disponíveis;
3. Comparar algoritmos em diferentes cargas de rede;
4. Gerar gráficos de linha e gráficos de barras;
5. Calcular e exibir intervalos de confiança;
6. Calcular o ganho de um algoritmo em relação aos demais;
7. Exportar gráficos e tabelas para uso em artigos, relatórios e apresentações.

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

Exemplo:

```text
SimulationDataAnalyzer.py
```

Execução:

```bash
python SimulationDataAnalyzer.py
```

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

---

## Formas de carregar dados

O programa suporta dois modos.

### 1. Carregamento direto de CSV
Carrega arquivos CSV manualmente, selecionando os arquivos diretamente.

Isso é útil quando você já sabe exatamente quais arquivos quer analisar.

### 2. Carregamento por pasta
O programa pode varrer uma pasta raiz que contém várias subpastas com arquivos de resultados.

Exemplo de estrutura:

```text
results_root/
├── run_01/
│   ├── USA_IMPA_HXT_mo_0_00_mx_0_00_BlockingProbability.csv
│   ├── USA_IMPA_HXT_mo_0_00_mx_0_00_BitRateBlockingProbability.csv
│   └── ...
├── run_02/
│   ├── USA_PABS_HXT_mo_0_00_mx_0_00_BlockingProbability.csv
│   ├── USA_PABS_HXT_mo_0_00_mx_0_00_BitRateBlockingProbability.csv
│   └── ...
```

O tipo do arquivo é detectado pelo final do nome, por exemplo:

- `_BlockingProbability`
- `_BitRateBlockingProbability`
- `_CrosstalkStatistics`
- `_ModulationUtilization`
- `_SpectrumUtilization`

O carregador agrupa os arquivos por tipo detectado e permite escolher quais tipos devem ser carregados.

---

## Interface principal

A janela principal é dividida em seções como:

- **Actions**
- **Metric selection**
- **Loaded files**
- **Plot setup**
- **Statistics**
- **Appearance**
- **Margins**

---

## Área Actions

Ações comuns:

- **Load CSV files**
- **Configure algorithm names**
- **Edit graph texts**
- **Edit component legends**
- **Select components for bar plot**
- **Generate plot**
- **Compute gains**

Algumas ações só ficam habilitadas depois que CSVs válidos são carregados.

---

## Seleção de métrica

Depois de carregar os arquivos, o programa analisa a coluna `Metrics` e lista todas as métricas detectadas.

Depois disso, você pode escolher qual métrica será analisada e plotada.

---

## Plot setup

### Tipo de gráfico
Opções:
- **Line plot**
- **Bar plot**

### Idioma do gráfico
Opções:
- `en`
- `pt`

Isso afeta os rótulos e textos dos gráficos.

### Modo do gráfico de barras
As opções normalmente incluem:
- Valores absolutos empilhados;
- Porcentagens / normalizado empilhado.

### Tratamento da barra de erro em escala log
Controla como o limite inferior do intervalo de confiança é tratado quando o gráfico usa eixo Y em escala logarítmica.

Modos típicos:
- Esconder parte inferior quando IC inferior <= 0;
- Calcular intervalo em escala log;
- Marcar erro inferior truncado.

### Estilo da grade do eixo Y
Permite escolher como a grade horizontal é desenhada.

### Cargas e replicações
Campos típicos:
- **Initial load**
- **Load increment**
- **Replications (0=auto)**
- **Bar plot load point**
- **Specific loads filter**

Exemplo de filtro de cargas:

```text
500, 1000, 1500
```

---

## Statistics

### Confidence level
Exemplos:
- `90%`;
- `95%`;
- `99%`.

### CI method
Métodos disponíveis:
- **t-Student**
- **Bootstrap**

### Bootstrap resamples
Define quantas reamostragens bootstrap serão usadas quando esse método estiver selecionado.

---

## Explicação simples dos métodos de intervalo de confiança

O programa oferece dois métodos principais para calcular o **IC (Intervalo de Confiança)**.

### Método t-Student
O método **t-Student** calcula o intervalo de confiança usando:
- a média dos resultados;
- o desvio padrão;
- o número de replicações;
- uma fórmula estatística clássica.

Em termos práticos:
- ele é mais tradicional;
- costuma ser mais rápido;
- funciona bem quando os dados têm comportamento mais regular.

**Resumo simples:**  
O método t-Student estima o intervalo de confiança a partir de uma fórmula baseada na média e na variação dos dados.

### Método Bootstrap
O método **Bootstrap** calcula o intervalo de confiança por **reamostragem**.

Em vez de usar apenas uma fórmula direta, ele:
1. pega os valores das replicações;
2. cria várias novas amostras com reposição;
3. calcula várias médias simuladas;
4. usa essas médias para estimar o intervalo de confiança.

Em termos práticos:
- ele é mais empírico;
- faz menos suposições sobre a distribuição dos dados;
- pode ser útil quando os dados são mais irregulares ou assimétricos.

**Resumo simples:**  
O método Bootstrap estima o intervalo de confiança repetindo várias amostragens sobre os próprios dados.

### Diferença prática entre os dois
- **t-Student**: mais clássico, mais rápido e baseado em fórmula;
- **Bootstrap**: mais flexível, baseado em reamostragem e pode representar melhor dados menos regulares.

### Quando usar cada um
- Use **t-Student** quando quiser um método clássico e rápido;
- Use **Bootstrap** quando quiser uma estimativa mais baseada no comportamento real das replicações.

---

## Appearance

O programa permite personalizar a aparência dos gráficos, incluindo:

- Tamanho da fonte dos rótulos dos eixos;
- Tamanho da fonte dos ticks;
- Tamanho da fonte da legenda;
- Posição da legenda;
- Negrito nos rótulos dos eixos;
- Negrito nos ticks dos eixos.

### Posição da legenda
Dependendo da versão, as opções podem incluir:

- No legend;
- Inside (best);
- Inside (upper right);
- Inside (upper left);
- Inside (lower right);
- Inside (lower left);
- Inside (center right);
- Inside (center left);
- Inside (upper center);
- Inside (lower center);
- Inside (center);
- Bottom (outside);
- Top (outside);
- Right (outside);
- Left (outside).

---

## Margins

Esses campos controlam o espaçamento extra ao redor dos gráficos.

### Margens do eixo X
- Left margin;
- Right margin;
- Bar-plot X margin.

### Margens do eixo Y
- Linear bottom margin;
- Linear top margin;
- Log bottom factor;
- Log top factor.

Esses valores são úteis quando curvas ou rótulos ficam muito próximos das bordas do gráfico.

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

---

## Edição dos estilos de linha

O programa suporta estilos de linha personalizados por algoritmo.

Você pode ajustar:
- Cor da linha;
- Marcador;
- Estilo da linha.

Isso é especialmente útil em gráficos de linha quando muitos algoritmos são exibidos juntos.

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

---

## Saída do intervalo de confiança

Quando um gráfico de linha é gerado, o programa imprime no terminal uma tabela de IC com campos como:

- Load;
- Mean;
- CI lower;
- CI upper;
- Truncated.

Isso é útil para inspeção numérica da incerteza em torno da média.

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

---

## Salvar e carregar customizações

O programa suporta exportação/importação de customizações do usuário em arquivos JSON.

### Save customizations
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

### Load customizations
Restaura preferências salvas anteriormente.

Isso é útil quando você usa o mesmo layout e as mesmas configurações com frequência.

---

## Opções de exportação

Dependendo da versão atual, o programa suporta:

### Exportação de gráficos
No menu **Export**:
- **Save last plot (SVG)**;
- **Save last plot (PDF)**.

### Exportação de tabelas
- **Export last CI table (CSV)**

---

## Fluxo de uso típico

1. Abra o programa;
2. Carregue os arquivos CSV;
3. Selecione a métrica a ser analisada;
4. Escolha gráfico de linha ou de barras;
5. Configure as opções de intervalo de confiança;
6. Ajuste aparência e margens, se necessário;
7. Gere o gráfico;
8. Exporte o gráfico ou a tabela de IC, se desejar;
9. Salve as customizações para reutilizar depois.

---

## Exemplos de uso

### Executar o programa

```bash
python SimulationDataAnalyzer.py
```

### Instalar dependências

```bash
pip install numpy pandas matplotlib scipy
```

### Instalação opcional do Tkinter no Linux

```bash
sudo apt-get install python3-tk
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
Se limites inferiores do IC virarem zero ou negativos, use outra opção em:
- **Log-scale error bar handling**

### 6. Exportação não funciona
Garanta que:
- Um gráfico já foi gerado;
- O arquivo de destino não está aberto em outro programa;
- Você tem permissão de escrita na pasta de destino.

---

## Observações

- O programa é voltado para análise de arquivos CSV com resultados de simulações;
- Ele é especialmente útil para comparar múltiplos algoritmos sob diferentes cargas de rede;
- Manter nomes de arquivo consistentes ajuda bastante no carregamento por pasta.

---

## Ambiente sugerido

Para melhores resultados:
- Python 3.10+;
- Windows 10/11;
- Arquivos CSV em UTF-8;
- Resultados organizados em pastas e por tipo de métrica.

---
