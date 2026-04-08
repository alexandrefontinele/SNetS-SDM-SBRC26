package util.tools;

import java.util.*;

import simulationControl.parsers.NetworkConfig;

/**
 * Validador estrutural de NetworkConfig.
 */
public class NetworkConfigValidator {
	
	private static final double EPS = 1e-9;

    private NetworkConfigValidator() {
    	//
    }

    public static ValidationResult validate(NetworkConfig config) {
        ValidationResult result = new ValidationResult();

        if (config == null) {
            result.addError("NetworkConfig é null.");
            return result;
        }

        List<NetworkConfig.NodeConfig> nodes = config.getNodes() != null ? config.getNodes() : Collections.emptyList();

        List<NetworkConfig.LinkConfig> links = config.getLinks() != null ? config.getLinks() : Collections.emptyList();

        List<NetworkConfig.CoreConfig> cores = config.getCores() != null ? config.getCores() : Collections.emptyList();

        NodeValidationContext nodeCtx = validateNodes(nodes, result);
        LinkValidationContext linkCtx = validateLinks(nodeCtx, links, result);
        
        validateGraphConnectivity(nodeCtx, linkCtx, result);
        validateCores(cores, result);

        return result;
    }

    public static void validateOrThrow(NetworkConfig config) {
        ValidationResult result = validate(config);
        if (!result.isValid()) {
            throw new IllegalStateException(result.toPrettyString());
        }
    }

    private static NodeValidationContext validateNodes(List<NetworkConfig.NodeConfig> nodes, ValidationResult result) {
        NodeValidationContext ctx = new NodeValidationContext();

        if (nodes.isEmpty()) {
            result.addError("A rede não possui nós cadastrados.");
            return ctx;
        }

        for (int i = 0; i < nodes.size(); i++) {
            NetworkConfig.NodeConfig node = nodes.get(i);

            if (node == null) {
                result.addError("Existe um NodeConfig null na posição " + i + ".");
                continue;
            }

            String name = safeTrim(node.getName());

            if (name.isEmpty()) {
                result.addError("Existe nó com nome vazio na posição " + i + ".");
                continue;
            }

            if (!ctx.validNodes.add(name)) {
                result.addError("Nome de nó duplicado: '" + name + "'.");
            }
        }

        return ctx;
    }

    private static LinkValidationContext validateLinks(NodeValidationContext nodeCtx, List<NetworkConfig.LinkConfig> links, ValidationResult result) {
        LinkValidationContext ctx = new LinkValidationContext();

        if (links.isEmpty()) {
            result.addError("A rede não possui links cadastrados.");
            return ctx;
        }

        for (String nodeName : nodeCtx.validNodes) {
            ctx.incomingCount.put(nodeName, 0);
            ctx.outgoingCount.put(nodeName, 0);
            ctx.adjacency.put(nodeName, new HashSet<>());
        }

        for (int i = 0; i < links.size(); i++) {
            NetworkConfig.LinkConfig link = links.get(i);

            if (link == null) {
                result.addError("Existe um LinkConfig null na posição " + i + ".");
                continue;
            }

            String source = safeTrim(link.getSource());
            String destination = safeTrim(link.getDestination());

            if (source.isEmpty()) {
                result.addError("Link na posição " + i + " possui source vazio.");
                continue;
            }

            if (destination.isEmpty()) {
                result.addError("Link na posição " + i + " possui destination vazio.");
                continue;
            }

            if (!nodeCtx.validNodes.contains(source)) {
                result.addError("Link " + source + " -> " + destination + " referencia source inexistente.");
            }

            if (!nodeCtx.validNodes.contains(destination)) {
                result.addError("Link " + source + " -> " + destination + " referencia destination inexistente.");
            }

            if (source.equals(destination)) {
                result.addError("Link inválido do tipo auto-loop: '" + source + " -> " + destination + "'.");
            }

            if (link.getSlots() <= 0) {
                result.addError("Link " + source + " -> " + destination + " possui slots <= 0.");
            }

            if (link.getSpectrum() <= 0.0) {
                result.addError("Link " + source + " -> " + destination + " possui spectrum <= 0.");
            }

            if (link.getSize() < 0.0) {
                result.addError("Link " + source + " -> " + destination + " possui size negativo.");
            }

            boolean sourceExists = nodeCtx.validNodes.contains(source);
            boolean destinationExists = nodeCtx.validNodes.contains(destination);

            if (sourceExists) {
                ctx.outgoingCount.put(source, ctx.outgoingCount.get(source) + 1);
            }

            if (destinationExists) {
                ctx.incomingCount.put(destination, ctx.incomingCount.get(destination) + 1);
            }

            if (sourceExists && destinationExists && !source.equals(destination)) {
                ctx.adjacency.get(source).add(destination);
                ctx.adjacency.get(destination).add(source);
            }

            DirectedPair pair = new DirectedPair(source, destination);
            LinkSignature sig = new LinkSignature(link.getSize(), link.getSlots(), link.getSpectrum());

            ctx.pairToSignatures
                    .computeIfAbsent(pair, k -> new HashMap<>())
                    .merge(sig, 1, Integer::sum);
        }

        for (String nodeName : nodeCtx.validNodes) {
            if (ctx.incomingCount.getOrDefault(nodeName, 0) == 0) {
                result.addError("O nó '" + nodeName + "' não possui nenhum link chegando.");
            }

            if (ctx.outgoingCount.getOrDefault(nodeName, 0) == 0) {
                result.addError("O nó '" + nodeName + "' não possui nenhum link saindo.");
            }
        }

        detectDuplicateLinks(ctx.pairToSignatures, result);
        detectMissingOrInvalidReverseLinks(ctx.pairToSignatures, result);

        return ctx;
    }

    private static void detectDuplicateLinks(
            Map<DirectedPair, Map<LinkSignature, Integer>> pairToSignatures,
            ValidationResult result
    ) {
        for (Map.Entry<DirectedPair, Map<LinkSignature, Integer>> pairEntry : pairToSignatures.entrySet()) {
            DirectedPair pair = pairEntry.getKey();

            for (Map.Entry<LinkSignature, Integer> sigEntry : pairEntry.getValue().entrySet()) {
                int count = sigEntry.getValue();
                if (count > 1) {
                    LinkSignature sig = sigEntry.getKey();
                    result.addError(
                            "Link duplicado detectado para '" + pair.source + " -> " + pair.destination +
                            "' com size=" + sig.size +
                            ", slots=" + sig.slots +
                            ", spectrum=" + sig.spectrum +
                            ". Ocorrências: " + count + "."
                    );
                }
            }
        }
    }

    private static void detectMissingOrInvalidReverseLinks(
            Map<DirectedPair, Map<LinkSignature, Integer>> pairToSignatures,
            ValidationResult result
    ) {
        Set<UndirectedPair> mismatchedReported = new HashSet<>();

        for (Map.Entry<DirectedPair, Map<LinkSignature, Integer>> entry : pairToSignatures.entrySet()) {
            DirectedPair direct = entry.getKey();
            DirectedPair reverse = new DirectedPair(direct.destination, direct.source);

            Map<LinkSignature, Integer> directMap = entry.getValue();
            Map<LinkSignature, Integer> reverseMap = pairToSignatures.getOrDefault(reverse, Collections.emptyMap());

            for (Map.Entry<LinkSignature, Integer> sigEntry : directMap.entrySet()) {
                LinkSignature sig = sigEntry.getKey();
                int directCount = sigEntry.getValue();
                int reverseCount = reverseMap.getOrDefault(sig, 0);

                if (reverseCount < directCount) {
                    int missing = directCount - reverseCount;
                    for (int i = 0; i < missing; i++) {
                        result.addError(
                                "Falta link oposto para '" + direct.source + " -> " + direct.destination +
                                "' com size=" + sig.size +
                                ", slots=" + sig.slots +
                                ", spectrum=" + sig.spectrum +
                                ". Esperado: '" + direct.destination + " -> " + direct.source + "'."
                        );
                    }
                }
            }

            if (!reverseMap.isEmpty()) {
                boolean hasAnyExactMatch = false;
                for (LinkSignature sig : directMap.keySet()) {
                    if (reverseMap.containsKey(sig)) {
                        hasAnyExactMatch = true;
                        break;
                    }
                }

                if (!hasAnyExactMatch) {
                    UndirectedPair up = new UndirectedPair(direct.source, direct.destination);
                    if (mismatchedReported.add(up)) {
                        result.addError(
                                "Existe link nos dois sentidos entre '" + direct.source + "' e '" + direct.destination +
                                "', mas sem correspondência exata de size/slots/spectrum entre os sentidos."
                        );
                    }
                }
            }
        }
    }

    private static void validateGraphConnectivity(
            NodeValidationContext nodeCtx,
            LinkValidationContext linkCtx,
            ValidationResult result
    ) {
        if (nodeCtx.validNodes.isEmpty()) {
            return;
        }

        String start = nodeCtx.validNodes.iterator().next();
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            String current = stack.pop();

            for (String neighbor : linkCtx.adjacency.getOrDefault(current, Collections.emptySet())) {
                if (visited.add(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        if (visited.size() != nodeCtx.validNodes.size()) {
            Set<String> missing = new TreeSet<>(nodeCtx.validNodes);
            missing.removeAll(visited);

            result.addError(
                    "A rede não é totalmente conectada. Nós não alcançáveis a partir de '" +
                    start + "': " + missing
            );
        }
    }

    private static void validateCores(List<NetworkConfig.CoreConfig> cores, ValidationResult result) {
        if (cores.isEmpty()) {
            result.addWarning("A configuração não possui cores cadastrados.");
            return;
        }

        Map<Integer, NetworkConfig.CoreConfig> coreById = new HashMap<>();

        for (int i = 0; i < cores.size(); i++) {
            NetworkConfig.CoreConfig core = cores.get(i);

            if (core == null) {
                result.addError("Existe um CoreConfig null na posição " + i + ".");
                continue;
            }

            int id = core.getId();

            if (coreById.containsKey(id)) {
                result.addError("Core duplicado com id=" + id + ".");
            } else {
                coreById.put(id, core);
            }
        }

        for (NetworkConfig.CoreConfig core : cores) {
            if (core == null) {
                continue;
            }

            int coreId = core.getId();
            List<Integer> adjacents =
                    core.getAdjacentCores() != null ? core.getAdjacentCores() : Collections.emptyList();

            Set<Integer> seenAdj = new HashSet<>();

            for (Integer adjId : adjacents) {
                if (adjId == null) {
                    result.addError("Core " + coreId + " possui adjacência null.");
                    continue;
                }

                if (adjId == coreId) {
                    result.addError("Core " + coreId + " possui auto-adjacência com ele mesmo.");
                }

                if (!seenAdj.add(adjId)) {
                    result.addError(
                            "Core " + coreId + " possui adjacência repetida para core " + adjId + "."
                    );
                    continue;
                }

                NetworkConfig.CoreConfig adjCore = coreById.get(adjId);
                if (adjCore == null) {
                    result.addError("Core " + coreId + " aponta para core adjacente inexistente: " + adjId + ".");
                    continue;
                }

                List<Integer> reverseAdj =
                        adjCore.getAdjacentCores() != null ? adjCore.getAdjacentCores() : Collections.emptyList();

                if (!reverseAdj.contains(coreId)) {
                    result.addError(
                            "Adjacência de core não é recíproca: core " + coreId +
                            " aponta para " + adjId +
                            ", mas core " + adjId +
                            " não aponta de volta para " + coreId + "."
                    );
                }
            }
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private static final class NodeValidationContext {
        private final Set<String> validNodes = new LinkedHashSet<>();
    }

    private static final class LinkValidationContext {
        private final Map<String, Integer> incomingCount = new HashMap<>();
        private final Map<String, Integer> outgoingCount = new HashMap<>();
        private final Map<String, Set<String>> adjacency = new HashMap<>();
        private final Map<DirectedPair, Map<LinkSignature, Integer>> pairToSignatures = new HashMap<>();
    }

    private static final class DirectedPair {
        private final String source;
        private final String destination;

        private DirectedPair(String source, String destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof DirectedPair)) return false;
            DirectedPair other = (DirectedPair) obj;
            return Objects.equals(source, other.source) && Objects.equals(destination, other.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, destination);
        }
    }

    private static final class UndirectedPair {
        private final String a;
        private final String b;

        private UndirectedPair(String x, String y) {
            if (x.compareTo(y) <= 0) {
                this.a = x;
                this.b = y;
            } else {
                this.a = y;
                this.b = x;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof UndirectedPair)) return false;
            UndirectedPair other = (UndirectedPair) obj;
            return Objects.equals(a, other.a) && Objects.equals(b, other.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    private static final class LinkSignature {
        private final double size;
        private final int slots;
        private final double spectrum;

        private LinkSignature(double size, int slots, double spectrum) {
            this.size = normalize(size);
            this.slots = slots;
            this.spectrum = normalize(spectrum);
        }

        private static double normalize(double value) {
            if (Math.abs(value) < EPS) {
                return 0.0;
            }
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof LinkSignature)) return false;
            LinkSignature other = (LinkSignature) obj;
            return slots == other.slots
                    && Math.abs(size - other.size) < EPS
                    && Math.abs(spectrum - other.spectrum) < EPS;
        }

        @Override
        public int hashCode() {
            long roundedSize = Math.round(size / EPS);
            long roundedSpectrum = Math.round(spectrum / EPS);
            return Objects.hash(roundedSize, slots, roundedSpectrum);
        }
    }

    public static final class ValidationResult {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return Collections.unmodifiableList(errors);
        }

        public List<String> getWarnings() {
            return Collections.unmodifiableList(warnings);
        }

        public void addError(String message) {
            errors.add(message);
        }

        public void addWarning(String message) {
            warnings.add(message);
        }

        public void throwIfInvalid() {
            if (!isValid()) {
                throw new IllegalStateException(toPrettyString());
            }
        }

        public String toPrettyString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Validation Network {\n");
            sb.append("  valid = ").append(isValid()).append(",\n");
            sb.append("  errors = ").append(errors.size()).append(",\n");
            sb.append("  warnings = ").append(warnings.size()).append("\n");

            if (!errors.isEmpty()) {
                sb.append("  Error list:\n");
                for (String error : errors) {
                    sb.append("    - ").append(error).append('\n');
                }
            }

            if (!warnings.isEmpty()) {
                sb.append("  Warning list:\n");
                for (String warning : warnings) {
                    sb.append("    - ").append(warning).append('\n');
                }
            }

            sb.append("}");
            return sb.toString();
        }

        @Override
        public String toString() {
            return toPrettyString();
        }
    }
}
