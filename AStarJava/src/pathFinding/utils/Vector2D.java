package pathFinding.utils;

public class Vector2D {
	public float x;
	public float y;
	
	public Vector2D() {
		this.x = 0f;
		this.y = 0f;
	}
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D sub(Vector2D vector) {
		Vector2D result = new Vector2D();
		result.x = this.x - vector.x;
		result.y = this.y - vector.y;
		return result;
	}
	
	public Vector2D normalize() {
		Vector2D result = new Vector2D();
		float dis = (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y , 2));
		result.x = this.x / dis;
		result.y = this.y / dis;
		return result;
	}
}
