using System.Collections.Generic;

namespace Fancy
{
    public interface ICommand
    {
         void Execute(IEnumerable<string> values);
    }
}