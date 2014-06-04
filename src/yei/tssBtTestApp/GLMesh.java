package yei.tssBtTestApp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class GLMesh {
	
	// Our vertex buffer.
    private FloatBuffer verticesBuffer = null;
 
    // Our index buffer for vertices.
    private ShortBuffer indicesBuffer = null;
    
    // Our normal buffer
    private FloatBuffer normalsBuffer = null;
    
    // Our UV buffer
    private FloatBuffer texCoordBuffer = null;
 
    // The number of indices.
    private int numOfIndices = -1;
    
    // Our Texture id
    private int textureId = -1;
    
    // The bitmap we want to load as a texture
    private Bitmap textureBitmap;
    private boolean shouldLoadTexture = false;
 
    // Base Material Color
    private float[] ambient_color = new float[]{0.3f, 0.3f, 0.3f, 1.0f};
    private float[] diffuse_color = new float[]{0.8f, 0.8f, 0.8f, 1.0f};
    private float[] specular_color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private float shininess_factor = 50.0f;
    
 
    public void draw(GL10 gl) {
        // Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);
		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
		// Enabled the normals buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuffer);
        // Set base material color
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, ambient_color, 0);
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, diffuse_color, 0);
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, specular_color, 0);
        gl.glMaterialf(GL10.GL_FRONT, GL10.GL_SHININESS, shininess_factor);
        
        
        //Textures
        if (shouldLoadTexture) {
    		loadGLTexture(gl);
    		shouldLoadTexture = false;
    	}
    	if (textureId != -1 && texCoordBuffer != null) {
    		gl.glEnable(GL10.GL_TEXTURE_2D);
    		// Enable the texture state
    		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
     
    		// Point to our buffers
    		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
    		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    	}
    	
	    //Actual drawing
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
			GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		
		// Disable the texture coordinate buffer.
		if (textureId != -1 && texCoordBuffer != null) {
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		// Disable the normals buffer.
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);		
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
    }
    
    protected void setVertices(float[] vertices) {
    	// a float is 4 bytes, therefore we multiply the number if
    	// vertices with 4.
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
    	vbb.order(ByteOrder.nativeOrder());
    	verticesBuffer = vbb.asFloatBuffer();
    	verticesBuffer.put(vertices);
    	verticesBuffer.position(0);
        }
    
    protected void setNormals(float[] normals) {
    	// a float is 4 bytes, therefore we multiply the number if
    	// vertices with 4.
    	ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
    	nbb.order(ByteOrder.nativeOrder());
    	normalsBuffer = nbb.asFloatBuffer();
    	normalsBuffer.put(normals);
    	normalsBuffer.position(0);
        }
 
    protected void setIndices(short[] indices) {
	// short is 2 bytes, therefore we multiply the number if
	// vertices with 2.
	ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
	ibb.order(ByteOrder.nativeOrder());
	indicesBuffer = ibb.asShortBuffer();
	indicesBuffer.put(indices);
	indicesBuffer.position(0);
	numOfIndices = indices.length;
    }
 
    protected void setColor(float[] ambient, float[] diffuse, float[] specular, float shininess) {
        // Setting the flat color.
    	ambient_color[0] = ambient[0]; ambient_color[1] = ambient[1]; ambient_color[2] = ambient[2]; ambient_color[3] = ambient[3];
    	diffuse_color[0] = diffuse[0]; diffuse_color[1] = diffuse[1]; diffuse_color[2] = diffuse[2]; diffuse_color[3] = diffuse[3];
    	specular_color[0] = specular[0]; specular_color[1] = specular[1]; specular_color[2] = specular[2]; specular_color[3] = specular[3];
    	shininess_factor = shininess;
    }

    protected void setTextureCoordinates(float[] textureCoords) {
    	// float is 4 bytes, therefore we multiply the number if
            // vertices with 4.
    	ByteBuffer byteBuf = ByteBuffer.allocateDirect(
                                               textureCoords.length * 4);
    	byteBuf.order(ByteOrder.nativeOrder());
    	texCoordBuffer = byteBuf.asFloatBuffer();
    	texCoordBuffer.put(textureCoords);
    	texCoordBuffer.position(0);
    }
    
    public void loadBitmap(Bitmap bitmap)
    {
    	this.textureBitmap = bitmap;
    	shouldLoadTexture = true;
    }
    
    private void loadGLTexture(GL10 gl){
    	// Generate one texture pointer...
    	int[] textures = new int[1];
    	gl.glGenTextures(1, textures, 0);
    	textureId = textures[0];
    	
    	// ...and bind it to our array
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    	
    	// Create Nearest Filtered Texture
    	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
    					GL10.GL_LINEAR);
    	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
    	
    	// Different possible texture parameters
    	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
    			GL10.GL_CLAMP_TO_EDGE);
    	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
    			GL10.GL_REPEAT);
    	
    	// Use the Android GLUtils to specify a two-dimensional texture image
    	// from our bitmap
    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);
    }

}
