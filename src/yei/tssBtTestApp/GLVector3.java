package yei.tssBtTestApp;

public class GLVector3 {
	
	private float[] elements = {0,0,0};
	
	public GLVector3(float x, float y, float z)
	{
		elements[0] = x;
		elements[1] = y;
		elements[2] = z;
	}
	
	public GLVector3()
	{
		
	}
	
	public float getX()
	{
		return elements[0];
	}
	
	public float getY()
	{
		return elements[1];
	}
	
	public float getZ()
	{
		return elements[2];
	}
	
	public void setX(float x)
	{
		elements[0] = x;
	}
	
	public void setY(float y)
	{
		elements[1] = y;
	}
	
	public void setZ(float z)
	{
		elements[2] = z;
	}
	
	public float[] getArray()
	{
		return elements;
	}
	
	public void setArray(float x, float y, float z)
	{
		setX(x);
		setY(y);
		setZ(z);
	}

	public GLVector3 add(GLVector3 other)
	{
		float x_sum = getX() + other.getX();
		float y_sum = getY() + other.getY();
		float z_sum = getZ() + other.getZ();
		return new GLVector3(x_sum, y_sum, z_sum);
	}
	
	public GLVector3 sub(GLVector3 other)
	{
		float x_dif = getX() - other.getX();
		float y_dif = getY() - other.getY();
		float z_dif = getZ() - other.getZ();
		return new GLVector3(x_dif, y_dif, z_dif);
	}
	
	public GLVector3 mul(float scalar)
	{
		float new_x = getX() * scalar;
		float new_y = getY() * scalar;
		float new_z = getZ() * scalar;
		return new GLVector3(new_x, new_y, new_z);
	}
	
	public GLVector3 cross(GLVector3 other)
	{
		float new_x = getY() * other.getZ() - getZ() * other.getY();
		float new_y = getZ() * other.getX() - getX() * other.getZ();
		float new_z = getX() * other.getY() - getY() * other.getX();
		return new GLVector3(new_x, new_y, new_z);
	}
	
	public float dot(GLVector3 other)
	{
		return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
	}
	
	public float getLength()
	{
		return (float)java.lang.Math.sqrt(dot(this));
	}
	
	public void normalize()
	{
		float length = getLength();
		if (length > 0.0f)
		{
			elements[0] /= length;
			elements[1] /= length;
			elements[2] /= length;
		}
	}
	
	public GLVector3 normalizeCopy()
	{
		GLVector3 tmp_vec = new GLVector3(getX(), getY(), getZ());
		tmp_vec.normalize();
		return tmp_vec;
	}
}
