using System.Collections.Generic;
using Xunit;
using Moq;
using Fancy;

namespace FancyTest
{
    public class FancyClassFixture
    {
        private readonly FancyClass subject;
        private readonly IQuery querier;
        private readonly ICommand commander;

        public FancyClassFixture()
        {
            this.querier = Mock.Of<IQuery>();
            this.commander = Mock.Of<ICommand>();
            this.subject = new FancyClass(this.querier, this.commander);
        }
        public class ExecuteOnSortedResults : FancyClassFixture
        {
            private readonly string param;

            public ExecuteOnSortedResults()
            {
                this.param = "search-text";
            }
            public class WhenResultsFound : ExecuteOnSortedResults
            {
                private readonly List<string> unsortedResults;
                private readonly List<string> sortedResults;

                public WhenResultsFound()
                {
                    this.unsortedResults = new List<string> { "abc", "xyz", "hij" };
                    this.sortedResults = new List<string> { "xyz", "hij", "abc" };
                }

                [Fact]
                public void Executes_WithResults_SortedReverseAlphabetically()
                {
                    Mock.Get(this.querier).Setup(q => q.Find(this.param)).Returns(this.unsortedResults);

                    this.subject.ExecuteOnReversedResults(this.param);

                    Mock.Get(this.commander)
                        .Verify(c => c.Execute(this.sortedResults));
                }
            }

            public class WhenNoResultsFound : ExecuteOnSortedResults
            {
                private readonly IEnumerable<string> emptyResults;

                public WhenNoResultsFound() {
                    this.emptyResults = new List<string>();
                }

                [Fact]
                public void SkipsExecution()
                {
                    Mock.Get(this.querier).Setup(q => q.Find(this.param)).Returns(this.emptyResults);

                    this.subject.ExecuteOnReversedResults(this.param);

                    Mock.Get(this.commander)
                        .Verify(c => c.Execute(It.IsAny<IEnumerable<string>>()), Times.Never);
                }
            }
        }

        public class SortFilteredResults : FancyClassFixture
        {
            private readonly string param;

            public SortFilteredResults() {
                this.param = "123";
            }

            [Fact]
            public void ReturnsResults_Filtered_ReverseAlphabetized()
            {
                var allResults = new List<string> { "abc", "d", "ef", "ghi" };
                Mock.Get(this.querier).Setup(q => q.Find(this.param)).Returns(allResults);

                var results = this.subject.SortFilteredResults(this.param, (s => s.Length == 3));

                Assert.Equal(new List<string> { "abc", "ghi" }, results);
            }
        }

    }
}
