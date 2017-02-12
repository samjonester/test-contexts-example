import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.Times;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FancyTest {

    protected final Querier querier;
    protected final Commander commander;
    protected final Fancy subject;

    public FancyTest() {
        querier = mock(Querier.class);
        commander = mock(Commander.class);
        subject = new Fancy(querier, commander);
    }

    public static class ExecuteOnSortedResults extends FancyTest {
        protected final String param;

        public ExecuteOnSortedResults() {
            param = "search-text";
        }

        public static class WhenResultsFound extends ExecuteOnSortedResults {
            private final Stream<String> unsortedResults;
            private final Stream<String> sortedResults;

            public WhenResultsFound() {
                unsortedResults = Stream.of("abc", "xyz", "hij");
                sortedResults = Stream.of("xyz", "hij", "abc");
            }

            @Test
            public void executes_WithResults_SortedReverseAlphabetically() {
                when(querier.Find(param)).thenReturn(unsortedResults);

                subject.executeOnReversedResults(param);

                ArgumentCaptor captor = ArgumentCaptor.forClass(Stream.class);
                verify(commander).Execute((Stream<String>) captor.capture());
                assertThat((Stream<String>) captor.getValue(), StreamMatchers.equalsStream(sortedResults));
            }

        }

        public static class WhenNoResultsFound extends ExecuteOnSortedResults {
            private final Stream<String> emptyResults;

            public WhenNoResultsFound() {
                emptyResults = Stream.empty();
            }

            @Test
            public void skipsExecution() {
                when(querier.Find(param)).thenReturn(emptyResults);

                subject.executeOnReversedResults(param);

                verify(commander, new Times(0)).Execute(any());
            }
        }
    }

    public static class SortFilteredResults extends FancyTest {
        private final String param;

        public SortFilteredResults() {
            param = "search-text";
        }

        @Test
        public void returnsResults_Filtered_ReverseAlphabetized() {
            Stream<String> allResults = Stream.of("abc", "d", "ef", "ghi");
            when(this.querier.Find(param)).thenReturn(allResults);

            Stream<String> results = subject.sortFilteredResults(param, s -> s.length() == 3);

            assertThat(Stream.of("abc", "ghi"), StreamMatchers.equalsStream(results));
        }
    }
}
