import java.util.stream.Stream;

public interface Querier {
    Stream<String> Find(String param);
}
