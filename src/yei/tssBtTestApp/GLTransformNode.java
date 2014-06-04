package yei.tssBtTestApp;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class GLTransformNode extends GLGraphNode{
	public float[] matrix = new float[]{1,0,0,0,
										0,1,0,0,
										0,0,1,0,
										0,0,0,1};
	
	@Override
	public void draw(GL10 mGL)
	{
		mGL.glPushMatrix();
		resource_lock.lock();
		mGL.glMultMatrixf(matrix, 0);
		resource_lock.unlock();
		
		GLGraphNode cur_node;
		for (Iterator<GLGraphNode> iter = children.iterator(); iter.hasNext(); )
		{
			cur_node = iter.next();
			cur_node.draw(mGL);
		}
		mGL.glPopMatrix();
	}

}
