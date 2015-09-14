using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar
{
    class Point : ICloneable, IEquatable<Point>
    {
        public int x;
        public int y;

        public Point()
        {
            this.x = 0;
            this.y = 0;
        }

        public Point(int x, int y) 
        {
            this.x = x;
            this.y = y;
        }

        public Point(Point p)
        {
            this.x = p.x;
            this.y = p.y;
        }

        public object Clone()
        {
            return new Point(x, y);
        }

        public override String ToString()
        {
            return new StringBuilder().Append("[x=").Append(x).Append(", y=").Append(y).Append("]").ToString();
        }

        public bool Equals(Point other)
        {
            return (x == other.x) && (y == other.y);
        }
    }
}
