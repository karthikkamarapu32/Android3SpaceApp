package yei.tssBtTestApp;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class GLTranslateNode extends GLGraphNode{
	public float trans_x = 0.0f;
	public float trans_y = 0.0f;
	public float trans_z = 0.0f;
	
	@Override
	public void draw(GL10 mGL)
	{
		mGL.glPushMatrix();
		mGL.glTranslatef(trans_x, trans_y, trans_z);
		
		GLGraphNode cur_node;
		for (Iterator<GLGraphNode> iter = children.iterator(); iter.hasNext(); )
		{
			cur_node = iter.next();
			cur_node.draw(mGL);
		}
		mGL.glPopMatrix();
	}

}
