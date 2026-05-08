# Experimentos e reprodução das reivindicações

Esta página descreve como reproduzir as principais reivindicações experimentais do artigo.

## Experimentos

Esta seção descreve formas de reproduzir as principais reivindicações experimentais do artigo.

## Tabela-resumo das reivindicações

| Reivindicação | Como reproduzir | Resultado esperado |
|---|---|---|
| Recriar os gráficos PBC/PBBR e componentes | Rodar `generate_article_graphs.ps1` ou `generate_article_graphs.sh` | Arquivos SVG e CSV em `outputs/article/` |
| Executar simulação local válida | Rodar o JAR com um diretório completo de `USA_sims/` ou `NSFNet_sims/` | Inicialização correta e progresso no terminal |
| Usar ambiente reprodutível | Rodar analisador e simulador via Docker | Mesmos comandos funcionam sem instalar dependências Python/Java localmente |
| Inspecionar implementação | Examinar `src/main/java/` | Código modular e documentado |

## Reivindicação 1 — Recriar os principais gráficos do artigo

### Objetivo

Recriar automaticamente os gráficos principais usando os CSVs já incluídos.

### Procedimento no Windows

```powershell
cd simulations\Data_Analyzer
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

### Procedimento no Linux/WSL

```bash
cd simulations/Data_Analyzer
chmod +x ./generate_article_graphs.sh
./generate_article_graphs.sh
```

### Procedimento via Docker

Na raiz do repositório:

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  bash ./generate_article_graphs.sh
```

### Resultado esperado

Arquivos em:

```text
simulations/Data_Analyzer/outputs/article/
```

Incluindo:

```text
PBC_log_USA.svg
PBC_log_NSFNet.svg
PBBR_log_USA.svg
PBBR_log_NSFNet.svg
PBC_Comp_USA.svg
PBC_Comp_NSFNet.svg
```

Também podem ser gerados:

```text
*_ci.csv
*_best.csv
```

para os gráficos de linha.

### Recursos esperados

- RAM: ~1–2 GB;
- Tempo: poucos segundos até alguns minutos, dependendo do ambiente.

## Reivindicação 2 — Executar uma simulação local

### Objetivo

Executar pelo menos uma configuração completa de simulação local.

### Diretório de exemplo

```text
simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

Esse diretório contém os arquivos de configuração usados pelo simulador, como:

- `simulation`: parâmetros gerais da execução;
- `network`: configuração da topologia, enlaces, núcleos, modulações e recursos;
- `others`: parâmetros adicionais dos algoritmos;
- `physicalLayer`: parâmetros de camada física e QoT;
- `traffic`: configuração do tráfego e das demandas.

### Procedimento com JAR

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

### Procedimento com Docker

```bash
docker build -f Dockerfile.simulator -t snets-simulator .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace \
  snets-simulator \
  simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

### Resultado esperado

O simulador deve:

- Carregar arquivos do diretório;
- Validar a configuração;
- Iniciar execução local;
- Produzir saída textual com progresso.

### Recursos esperados

- RAM: depende do cenário, mas o teste mínimo deve ser executável em uma máquina comum de avaliação;
- Tempo: variável conforme número de requisições, replicações e threads configuradas.

## Reivindicação 3 — Organização experimental

O repositório contém:

- Topologias `USA_sims` e `NSFNet_sims`;
- Subpastas por algoritmo;
- Configurações parametrizadas;
- CSVs organizados para análise;
- Configs JSON de geração de gráficos.

Essa organização permite rastrear cada resultado até sua topologia, algoritmo e parâmetros experimentais.

## Reivindicação 4 — Código-fonte modular e documentado

O código Java está organizado em módulos como:

- `gprmcsa`: implementa a lógica dos algoritmos de alocação de recursos, incluindo roteamento, espectro, núcleo, modulação, potência, banda de guarda, regeneração, realocação e grooming;
- `measurement`: reúne as métricas e estatísticas da simulação, como probabilidade de bloqueio, bloqueio por taxa de bits, fragmentação, consumo de energia e utilização de recursos;
- `network`: modela os principais elementos da rede e do plano de controle, como nós, enlaces, núcleos, espectro, circuitos, malha e camada física;
- `request`: define as requisições de conexão usadas nas simulações;
- `simulationControl`: controla a execução da simulação, leitura das configurações, gerenciamento dos experimentos e processamento dos resultados;
- `simulator`: contém o núcleo do simulador baseado em eventos;
- `util`: reúne classes utilitárias de apoio usadas em diferentes partes do projeto.

A documentação Javadoc e os comentários de API devem ser mantidos em **inglês** ao longo do projeto para consistência.

---

---

[Voltar ao README principal](../README.md)
