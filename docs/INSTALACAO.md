# Instalação

## Instalação

## Opção A — Windows 11 Pro com ambiente virtual Python

Este é o ambiente principal usado pelos autores para execução local.

### 1. Clonar o repositório

```powershell
git clone https://github.com/alexandrefontinele/SNetS-SDM-SBRC26.git
cd SNetS-SDM-SBRC26
```

### 2. Verificar Java

```powershell
java -version
```

O ideal é usar Java 8.

### 3. Criar ambiente virtual Python para o analisador

```powershell
cd simulations\Data_Analyzer
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
```

Se o PowerShell bloquear a ativação do ambiente virtual, execute apenas na sessão atual:

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\.venv\Scripts\Activate.ps1
```

### 4. Abrir o analisador gráfico

```powershell
python .\SimulationDataAnalyzer.py
```

### 5. Voltar para a raiz do repositório

```powershell
cd ..\..
```

## Opção B — Ubuntu 24.04 com ambiente virtual Python

```bash
sudo apt update
sudo apt install -y git python3 python3-venv python3-pip python3-tk
git clone https://github.com/alexandrefontinele/SNetS-SDM-SBRC26.git
cd SNetS-SDM-SBRC26/simulations/Data_Analyzer
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
python ./SimulationDataAnalyzer.py
```

## Opção C — Docker no Ubuntu/WSL/Windows

Este é o caminho recomendado quando se deseja reduzir problemas de versão de sistema operacional e dependências locais.

A partir da raiz do repositório:

```bash
docker build -f Dockerfile.analyzer -t snets-analyzer .
docker build -f Dockerfile.simulator -t snets-simulator .
```

Os comandos de execução estão na seção [Uso com Docker](#uso-com-docker).

---

---

[Voltar ao README principal](../README.md)
