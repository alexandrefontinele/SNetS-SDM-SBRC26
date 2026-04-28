# generate_article_graphs.ps1
# Generate all article graphs using SimulationDataAnalyzer.py.
# Run this script from simulations\Data_Analyzer.

$ErrorActionPreference = "Stop"

function Invoke-GraphConfig {
    param(
        [Parameter(Mandatory = $true)]
        [string]$ConfigPath
    )

    if (-not (Test-Path $ConfigPath)) {
        throw "Config file not found: $ConfigPath"
    }

    Write-Host ""
    Write-Host "Generating graph with config: $ConfigPath"

    python .\SimulationDataAnalyzer.py --config $ConfigPath

    if ($LASTEXITCODE -ne 0) {
        throw "Graph generation failed for config: $ConfigPath"
    }
}

if (-not (Test-Path ".\SimulationDataAnalyzer.py")) {
    throw "SimulationDataAnalyzer.py not found. Run this script from simulations\Data_Analyzer."
}

if (-not (Test-Path ".\configs")) {
    throw "configs folder not found. Copy the configs folder to simulations\Data_Analyzer\configs."
}

New-Item -ItemType Directory -Force ".\outputs\article" | Out-Null

Invoke-GraphConfig ".\configs\config_article_PBC_log_USA.json"
Invoke-GraphConfig ".\configs\config_article_PBC_log_NSFNet.json"
Invoke-GraphConfig ".\configs\config_article_PBBR_log_USA.json"
Invoke-GraphConfig ".\configs\config_article_PBBR_log_NSFNet.json"
Invoke-GraphConfig ".\configs\config_article_PBC_Comp_USA.json"
Invoke-GraphConfig ".\configs\config_article_PBC_Comp_NSFNet.json"

Write-Host ""
Write-Host "Done."
Write-Host "Graphs generated in: outputs\article"
