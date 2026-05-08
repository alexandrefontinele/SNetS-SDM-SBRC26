# Ambiente de execução e dependências

## Ambiente de execução

## Ambiente principal usado pelos autores

O ambiente principal usado para desenvolvimento e execução local do simulador foi:

- **Sistema operacional:** Windows 11 Pro
- **Java:** Java 8
- **Python:** Python 3.10+ / 3.11
- **Execução do simulador:** via JAR local
- **Análise de resultados:** via `SimulationDataAnalyzer.py`

## Ambiente de referência para reprodução

Para reduzir variações de ambiente entre revisores, foram documentados dois caminhos:

1. **Windows 11 Pro com ambiente virtual Python**, correspondente ao ambiente local principal usado pelos autores;
2. **Docker em Ubuntu/Linux/WSL**, usando imagens com dependências controladas.

Ambiente Linux de referência:

- **Ubuntu 24.04 LTS** ou ambiente Linux equivalente;
- **Docker Engine** no Ubuntu/WSL ou Docker Desktop;
- Imagens Docker:
  - `python:3.11-slim-bookworm` para o analisador;
  - `eclipse-temurin:8-jre` para o simulador.

## Configuração de hardware recomendada

Para uma avaliação confortável:

- **CPU:** 10 núcleos ou mais;
- **RAM:** 16 GB ou mais;
- **Armazenamento livre:** 5 GB ou mais.

Essa configuração é uma recomendação prática. O artefato não impõe esses valores como requisitos rígidos para os testes mínimos.

Também é possível ajustar a quantidade de threads usadas pelo simulador no parâmetro `threads`, localizado no arquivo `simulation` dentro das pastas de configuração das simulações.

---

## Dependências

Esta seção separa dependências obrigatórias, opcionais e não necessárias para evitar ambiguidade.

## Dependências obrigatórias para clonar o repositório

É necessário ter **Git** instalado.

### Ubuntu 24.04

```bash
sudo apt update
sudo apt install -y git
```

### Windows 11 Pro

Instale o **Git for Windows** a partir de:

```text
https://git-scm.com/download/win
```

Depois confirme:

```powershell
git --version
```

## Dependências obrigatórias para executar o simulador Java localmente

Para usar o JAR já incluído:

- **Java 8**
- Arquivo `simulations/SNetS-SDM-SBRC26.jar`
- Um diretório de simulação válido em:
  - `simulations/USA_sims/`
  - `simulations/NSFNet_sims/`

### Ubuntu 24.04

Em Ubuntu 24.04, o pacote `openjdk-8-jdk` pode não estar disponível diretamente nos repositórios padrão. Para evitar esse problema, o caminho recomendado para reprodução em Linux é usar Docker com a imagem `eclipse-temurin:8-jre`.

Se desejar instalar Java localmente, use uma distribuição Java 8, como Temurin/OpenJDK 8, e confirme:

```bash
java -version
```

### Windows 11 Pro

Instale uma distribuição Java 8, por exemplo Temurin/OpenJDK 8, e confirme:

```powershell
java -version
```

## Dependências opcionais para compilar o código-fonte Java

Maven é necessário **somente** se o revisor quiser compilar o projeto a partir do código-fonte.

- **Maven 3.x**
- **Java 8 JDK**

### Ubuntu 24.04

```bash
sudo apt update
sudo apt install -y maven
mvn -version
```

### Windows 11 Pro

Instale Maven 3.x e confirme:

```powershell
mvn -version
```

Para a avaliação padrão, **não é necessário compilar o projeto**, pois o JAR já está incluído.

## Dependências obrigatórias para gerar gráficos localmente

Para usar o analisador Python fora do Docker:

- Python 3.10+;
- Ambiente virtual Python;
- Bibliotecas em `simulations/Data_Analyzer/requirements.txt`.

### Ubuntu 24.04

```bash
sudo apt update
sudo apt install -y python3 python3-venv python3-pip python3-tk
```

### Windows 11 Pro

Instale Python 3.10+ pelo instalador oficial ou pela Microsoft Store. O Tkinter normalmente já vem incluído no Python para Windows.

Confirme:

```powershell
python --version
```

## Dependências recomendadas para reprodução via Docker

Para usar Docker:

- Docker Engine em Linux/WSL; ou
- Docker Desktop no Windows.

Com Docker, não é necessário instalar localmente as bibliotecas Python nem o Java dentro do sistema hospedeiro, além do próprio Docker.

## Dependências não necessárias para a avaliação padrão

Não são necessárias para os testes mínimos e para a reprodução dos gráficos principais:

- Firebase;
- Credenciais privadas;
- Servidor distribuído;
- Modo cliente/servidor LAN;
- Infraestrutura externa de nuvem.

---

---

[Voltar ao README principal](../README.md)
