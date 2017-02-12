import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamMatchers {
    public static <T> Matcher<Stream<T>> equalsStream(Stream<T> expected) {
        List<T> expectedList = expected.collect(Collectors.toList());
        return new CustomTypeSafeMatcher<Stream<T>>("Expected streams to be equal.") {
            @Override
            protected boolean matchesSafely(Stream<T> actual) {
                return expectedList.equals(actual.collect(Collectors.toList()));
            }
        };
    }
}
