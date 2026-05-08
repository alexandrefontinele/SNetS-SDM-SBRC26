# Quick Start para revisores

Esta página reúne os caminhos rápidos para gerar gráficos e executar simulações.

## Quick Start para revisores

## Caminho rápido 1 — Recriar todos os gráficos do artigo a partir dos CSVs

### PowerShell no Windows 11 Pro

```powershell
git clone https://github.com/alexandrefontinele/SNetS-SDM-SBRC26.git
cd SNetS-SDM-SBRC26\simulations\Data_Analyzer
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

Saída esperada:

```text
outputs/article/
```

Exemplos de arquivos gerados:

```text
PBC_log_USA.svg
PBC_log_NSFNet.svg
PBBR_log_USA.svg
PBBR_log_NSFNet.svg
PBC_Comp_USA.svg
PBC_Comp_NSFNet.svg
```

## Caminho rápido 2 — Recriar todos os gráficos usando Docker

A partir da raiz do repositório:

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace/simulations/Data_Analyzer \
  snets-analyzer \
  bash ./generate_article_graphs.sh
```

Saída esperada:

```text
simulations/Data_Analyzer/outputs/article/
```

## Caminho rápido 3 — Executar uma simulação local com o JAR

Na raiz do repositório:

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

O simulador deve:

- Ler os arquivos de configuração;
- Validar a configuração;
- Inicializar a simulação local;
- Exibir progresso no terminal.

## Caminho rápido 4 — Executar uma simulação local com Docker

```bash
docker build -f Dockerfile.simulator -t snets-simulator .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace \
  snets-simulator \
  simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

---

---

[Voltar ao README principal](../README.md)
