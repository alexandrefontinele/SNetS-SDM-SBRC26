# Teste mínimo

Esta página descreve os testes mínimos recomendados para confirmar que o artefato está funcionando.

## Teste mínimo

## Teste mínimo 1 — Gerar um gráfico por linha de comando

Entre na pasta do analisador:

```bash
cd simulations/Data_Analyzer
```

Execute:

```bash
python SimulationDataAnalyzer.py --config configs/config_article_PBC_log_USA.json
```

Resultado esperado:

```text
outputs/article/PBC_log_USA.svg
outputs/article/PBC_log_USA_ci.csv
outputs/article/PBC_log_USA_best.csv
```

Esse teste é preferível para revisão rápida porque não depende de interação manual com a GUI.

## Teste mínimo 2 — Abrir a interface gráfica

```bash
cd simulations/Data_Analyzer
python SimulationDataAnalyzer.py
```

Resultado esperado:

- A interface gráfica deve abrir;
- O revisor deve conseguir carregar CSVs;
- O revisor deve conseguir selecionar métricas;
- O revisor deve conseguir gerar gráficos.

## Teste mínimo 3 — Executar uma simulação local

Na raiz do repositório:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

Resultado esperado:

- Leitura do diretório de configuração;
- Validação da configuração;
- Inicialização da simulação;
- Saída textual de progresso no terminal.

## Teste mínimo 4 — Executar teste com Docker

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  python ./SimulationDataAnalyzer.py --config ./configs/config_article_PBC_log_USA.json
```

---

---

[Voltar ao README principal](../README.md)
