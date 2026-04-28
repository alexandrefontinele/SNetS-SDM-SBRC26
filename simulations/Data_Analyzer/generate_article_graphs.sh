#!/usr/bin/env bash
# generate_article_graphs.sh
# Generate all article graphs using SimulationDataAnalyzer.py.
# Run this script from simulations/Data_Analyzer.

set -euo pipefail

run_graph_config() {
  local config_path="$1"

  if [ ! -f "$config_path" ]; then
    echo "Config file not found: $config_path" >&2
    exit 1
  fi

  echo ""
  echo "Generating graph with config: $config_path"
  python ./SimulationDataAnalyzer.py --config "$config_path"
}

if [ ! -f "./SimulationDataAnalyzer.py" ]; then
  echo "SimulationDataAnalyzer.py not found. Run this script from simulations/Data_Analyzer." >&2
  exit 1
fi

if [ ! -d "./configs" ]; then
  echo "configs folder not found. Copy the configs folder to simulations/Data_Analyzer/configs." >&2
  exit 1
fi

mkdir -p ./outputs/article

run_graph_config "./configs/config_article_PBC_log_USA.json"
run_graph_config "./configs/config_article_PBC_log_NSFNet.json"
run_graph_config "./configs/config_article_PBBR_log_USA.json"
run_graph_config "./configs/config_article_PBBR_log_NSFNet.json"
run_graph_config "./configs/config_article_PBC_Comp_USA.json"
run_graph_config "./configs/config_article_PBC_Comp_NSFNet.json"

echo ""
echo "Done."
echo "Graphs generated in: outputs/article"
