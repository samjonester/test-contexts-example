import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fancy {

    private final Querier querier;
    private final Commander commander;

    public Fancy(Querier querier, Commander commander) {
        this.querier = querier;
        this.commander = commander;
    }

    public void executeOnReversedResults(String param) {
        List<String> results = this.querier.Find(param).collect(Collectors.toList());
        if (!results.isEmpty()) {
            commander.Execute(results.stream().sorted(Comparator.reverseOrder()));
        }
    }

    public Stream<String> sortFilteredResults(String param, Predicate<String> predicate) {
        return querier.Find(param).filter(predicate).sorted();
    }
}
