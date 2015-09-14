using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.utils
{
    class Vector2D
    {
        public float x;
        public float y;

        public Vector2D()
        {
            this.x = 0f;
            this.y = 0f;
        }

        public Vector2D(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        public Vector2D sub(Vector2D other)
        {
            Vector2D result = new Vector2D();
            result.x = this.x - other.x;
            result.y = this.y - other.y;
            return result;
        }

        public Vector2D normalize()
        {
            Vector2D result = new Vector2D();
            float dis = (float)Math.Sqrt(x * x + y * y);
            result.x = x / dis;
            result.y = y / dis;
            return result;
        }
    }
}
