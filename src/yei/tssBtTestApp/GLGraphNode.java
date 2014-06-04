package yei.tssBtTestApp;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.opengles.GL10;

public class GLGraphNode {
	public GLGraphNode parent = null;
	public Vector<GLGraphNode> children = new Vector<GLGraphNode>();
	public ReentrantLock resource_lock;
	
	public GLGraphNode(){
		resource_lock  = new ReentrantLock();
	}
	
	public void draw(GL10 mGL){
		GLGraphNode cur_node;
		for (Iterator<GLGraphNode> iter = children.iterator(); iter.hasNext(); )
		{
			cur_node = iter.next();
			cur_node.draw(mGL);
		}
	}
	
	public void addChild(GLGraphNode new_child)
	{
		new_child.parent = this;
		children.add(new_child);
	}
}
