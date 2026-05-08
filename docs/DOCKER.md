# Uso com Docker

Esta página descreve como usar Docker para executar o analisador Python e o simulador Java.

## Uso com Docker

Os comandos desta seção devem ser executados a partir da raiz do repositório.

## Analisador Python

### Build da imagem

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .
```

### Gerar todos os gráficos do artigo

```bash
docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  bash ./generate_article_graphs.sh
```

### Gerar apenas um gráfico

```bash
docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  python ./SimulationDataAnalyzer.py --config ./configs/config_article_PBC_log_USA.json
```

### Listar métricas detectadas

```bash
docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  python ./SimulationDataAnalyzer.py --config ./configs/config_article_PBC_log_USA.json --list-metrics
```

## Simulador Java

### Build da imagem

```bash
docker build -f Dockerfile.simulator -t snets-simulator .
```

### Executar cenário de exemplo

```bash
docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace \
  snets-simulator \
  simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

---

---

[Voltar ao README principal](../README.md)
