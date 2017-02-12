using System.Collections.Generic;

namespace Fancy
{
    public interface IQuery
    {
         IEnumerable<string> Find(string param);
    }
}