package yei.tssBtTestApp;

public class GLTexCoord {
	private float[] elements = {0,0};
	
	public GLTexCoord(float u, float v)
	{
		elements[0] = u;
		elements[1] = v;
	}
	
	public float getU()
	{
		return elements[0];
	}
	
	public float getV()
	{
		return elements[1];
	}
}
