package simulationControl.parsers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OthersConfig implements Serializable {

	 private Map<String, String> variables = new HashMap<>();
	 private Map<String, Map<String, Double>> kbpWeights = new HashMap<>();

	 public OthersConfig() {
		 //Constructor
	 }

	 public Map<String, String> getVariables() {
		 return this.variables;
	 }

	 public void setVariables(Map<String, String> variables) {
		 this.variables = variables;
	 }

	 public Map<String, Map<String, Double>> getKbpWeights() {
		 return kbpWeights;
	 }

	 public void setKbpWeights(Map<String, Map<String, Double>> kbpWeights) {
		 this.kbpWeights = kbpWeights;
	 }
}
