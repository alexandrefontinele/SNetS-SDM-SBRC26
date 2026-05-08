---

# Documentação complementar

Para manter este README principal mais curto, as seções detalhadas foram movidas para páginas auxiliares em `docs/`.

| Documento | Conteúdo |
|---|---|
| [docs/SELOS.md](docs/SELOS.md) | Selos considerados na avaliação |
| [docs/INFORMACOES_BASICAS.md](docs/INFORMACOES_BASICAS.md) | Objetivo do artefato, dados do projeto e caminhos principais |
| [docs/ESTRUTURA_REPOSITORIO.md](docs/ESTRUTURA_REPOSITORIO.md) | Estrutura completa do repositório e descrição dos arquivos |
| [docs/AMBIENTE_E_DEPENDENCIAS.md](docs/AMBIENTE_E_DEPENDENCIAS.md) | Ambientes testados, dependências obrigatórias, opcionais e não necessárias |
| [docs/SEGURANCA.md](docs/SEGURANCA.md) | Preocupações com segurança |
| [docs/INSTALACAO.md](docs/INSTALACAO.md) | Instalação local no Windows, Ubuntu/WSL e preparação com Docker |
| [docs/QUICK_START.md](docs/QUICK_START.md) | Caminhos rápidos para revisores |
| [docs/DOCKER.md](docs/DOCKER.md) | Execução do analisador e do simulador com Docker |
| [docs/TESTE_MINIMO.md](docs/TESTE_MINIMO.md) | Testes mínimos para validar o artefato |
| [docs/EXPERIMENTOS.md](docs/EXPERIMENTOS.md) | Reprodução das reivindicações experimentais |
| [docs/SUSTENTABILIDADE.md](docs/SUSTENTABILIDADE.md) | Organização, documentação e sustentabilidade do código |
| [docs/LIMITACOES.md](docs/LIMITACOES.md) | Limitações conhecidas |
| [docs/FIGURAS.md](docs/FIGURAS.md) | Sugestão de página para imagens e capturas de tela |
| [docs/LICENCA.md](docs/LICENCA.md) | Licença |

A documentação completa do analisador gráfico está em:

- [simulations/Data_Analyzer/README_data_analyzer.md](simulations/Data_Analyzer/README_data_analyzer.md)

---

# Quick Start para revisores

## Recriar todos os gráficos usando Docker

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

## Recriar todos os gráficos no Windows PowerShell

```powershell
cd simulations\Data_Analyzer
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\generate_article_graphs.ps1
```

## Executar uma simulação com Docker

```bash
docker build -f Dockerfile.simulator -t snets-simulator .

docker run --rm \
  --mount type=bind,src="$(pwd)",dst=/workspace \
  -w /workspace \
  snets-simulator \
  simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

## Executar uma simulação com o JAR local

```bash
java -jar simulations/SNetS-SDM-SBRC26.jar simulations/USA_sims/IMPA/USA_IMPA_HXT_mo_0_00_mx_0_00
```

---

# Estrutura resumida do repositório

```text
SNetS-SDM-SBRC26/
├── src/main/java/
├── simulations/
│   ├── Data_Analyzer/
│   ├── USA_sims/
│   ├── NSFNet_sims/
│   └── SNetS-SDM-SBRC26.jar
├── Dockerfile.analyzer
├── Dockerfile.simulator
├── pom.xml
├── LICENSE
└── README.md
```

A descrição completa das pastas e arquivos está em:

- [docs/ESTRUTURA_REPOSITORIO.md](docs/ESTRUTURA_REPOSITORIO.md)

---

# LICENÇA

Este projeto está licenciado sob a licença **MIT**. Consulte o arquivo:

```text
LICENSE
```
