using System;
using System.Collections.Generic;
using System.Linq;

namespace Fancy
{
    public class FancyClass
    {
        private readonly IQuery querier;
        private readonly ICommand commander;

        public FancyClass(IQuery querier, ICommand commander)
        {
            this.querier = querier;
            this.commander = commander;
        }

        public void ExecuteOnReversedResults(string param)
        {
            var results = this.querier.Find(param).ToList();
            if (results.Any())
            {
                this.commander.Execute(results.OrderByDescending(v => v));
            }

        }

        public IEnumerable<string> SortFilteredResults(string param, Func<string, bool> predicate)
        {
            return this.querier.Find(param).Where(predicate).OrderBy(v => v);
        }
    }
}
