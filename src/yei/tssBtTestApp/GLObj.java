package yei.tssBtTestApp;

import java.lang.String;
import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GLObj extends GLGraphNode{
	
	public Vector<GLMesh> meshes = new Vector<GLMesh>();
	private HashMap<String, GLMaterial> materials = new HashMap<String, GLMaterial>();
	private String cur_material = null;
	
	@Override
	public void draw(GL10 mGL)
	{
		GLMesh cur_mesh;
		for (Iterator<GLMesh> iter = meshes.iterator(); iter.hasNext(); )
		{
			cur_mesh = iter.next();
			cur_mesh.draw(mGL);
		}
		
		GLGraphNode cur_node;
		for (Iterator<GLGraphNode> iter = children.iterator(); iter.hasNext(); )
		{
			cur_node = iter.next();
			cur_node.draw(mGL);
		}
	}
	
	private String ReadLine(InputStream in_stream)
	{
		Vector<Byte> byte_buffer = new Vector<Byte>();
		int cur_byte = 0;
		while (cur_byte != 10)//Newline character
		{
			try {
				cur_byte = in_stream.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (cur_byte == -1)
			{
				break;
			}
			byte_buffer.add((byte)cur_byte);
		}
		if (byte_buffer.size() == 0)
		{
			return null;
		}
		else
		{
			byte[] bytes_for_string = new byte[byte_buffer.size()];
			for (int i = 0; i < byte_buffer.size(); i++)
			{
				bytes_for_string[i] = byte_buffer.elementAt(i);
			}
			return new String(bytes_for_string);
		}
	}
	
	private void LoadMtl(InputStream in_stream, SampleOpenGLProjectActivity myApp)
	{
		String line = null;
		while ((line = ReadLine(in_stream)) != null)
		{
			switch (line.charAt(0)) {
				case 35: //#
					//The line is a comment
					continue;
				case 110: //n
					//String tmp_string = line.substring(0, 5);
					if(line.substring(0, 6).equals("newmtl"))
					{
						//We are creating a new material
						String material_name = line.substring(7, line.length() - 1);
						materials.put(material_name, new GLMaterial());
						cur_material = material_name;
					}
				case 78: //N
					if(line.substring(0,2).equals("Ns"))
					{
						//Specular Weighting
						float new_spec_weight = new Float(line.substring(3,line.length() - 1));
						materials.get(cur_material).shininess = new_spec_weight;
					}
					else if(line.substring(0,2).equals("Ni"))
					{
						//"Optical Density"
						continue; //Not currently supported
					}
				case 75: //K
					if(line.substring(0,2).equals("Ka"))
					{
						//Ambient Reflectivity
						String [] line_parts = line.split("\\s+");
						float[] new_ambient = new float[]{new Float(line_parts[1]), new Float(line_parts[2]),new Float(line_parts[3]), 1.0f};
						materials.get(cur_material).ambient_color = new_ambient;
					}
					else if(line.substring(0,2).equals("Kd"))
					{
						//Diffuse Reflectivity
						String [] line_parts = line.split("\\s+");
						float[] new_diffuse = new float[]{new Float(line_parts[1]), new Float(line_parts[2]),new Float(line_parts[3]), 1.0f};
						materials.get(cur_material).diffuse_color = new_diffuse;
					}
					else if(line.substring(0,2).equals("Ks"))
					{
						//Specular Reflectivity
						String [] line_parts = line.split("\\s+");
						float[] new_specular = new float[]{new Float(line_parts[1]), new Float(line_parts[2]),new Float(line_parts[3]), 1.0f};
						materials.get(cur_material).specular_color = new_specular;
					}
				case 109: //m
					if (line.substring(0,4).equals("map_"))
					{
						//This is a color map of some sort. Regardless of its intended reflectivity
						//of application, we will treat it as a single texture to be applied to the
						//mesh
						int slash_idx = line.lastIndexOf("\\\\");
						String texture_file_name = line.substring(slash_idx + 2, line.length() - 5);
						int tex_file_id = myApp.getResources().getIdentifier(texture_file_name, "drawable", "yei.tssBtTestApp");
						Bitmap tmp_bitmap = BitmapFactory.decodeResource(myApp.getResources(), tex_file_id);
						materials.get(cur_material).textureBitmap = tmp_bitmap;
					}
			}
		}
	}
	
    public GLObj(InputStream obj_in_stream, SampleOpenGLProjectActivity myApp) {
 
    	//Lets set up some storage constructs for parsing the obj
    	Vector<GLVector3> raw_verts = new Vector<GLVector3>();
    	Vector<GLVector3> raw_normals = new Vector<GLVector3>();
    	Vector<GLTexCoord> raw_tex_coord = new Vector<GLTexCoord>();
    	
    	Vector<Float> final_verts = new Vector<Float>();
    	Vector<Float> final_norms = new Vector<Float>();
    	Vector<Float> final_tex_coords = new Vector<Float>();
    	Vector<Short> final_indices = new Vector<Short>();

    	String line = null;
    	try {
			while ((line = ReadLine(obj_in_stream)) != null){
				switch (line.charAt(0)) {
					case 35: //#
						//The line is a comment
						continue;
					case 111: //o
						//The line is a new object
						if(meshes.size() > 0)
						{
							//First lets append the current vert/normal/uv data to
							//the current GLMesh (if there is one)
							GLMesh cur_mesh = meshes.lastElement();
							//Vertices
							float[] vert_array = new float[final_verts.size()];
							for (int i = 0; i < final_verts.size(); i++)
							{
								vert_array[i] = final_verts.elementAt(i);
							}
							cur_mesh.setVertices(vert_array);
							//Normals
							float[] norm_array = new float[final_norms.size()];
							for (int i = 0; i < final_norms.size(); i++)
							{
								norm_array[i] = final_norms.elementAt(i);
							}
							cur_mesh.setNormals(norm_array);
							//Texture Coordinates
							float[] uv_array = new float[final_tex_coords.size()];
							for (int i = 0; i < final_tex_coords.size(); i++)
							{
								uv_array[i] = final_tex_coords.elementAt(i);
							}
							cur_mesh.setTextureCoordinates(uv_array);
							//Indicies
							short[] indice_array = new short[final_indices.size()];
							for (int i = 0; i < final_indices.size(); i++)
							{
								indice_array[i] = final_indices.elementAt(i);
							}
							cur_mesh.setIndices(indice_array);
						}
						//First clear our storage vectors for the new object
						//raw_verts.clear();
						//raw_normals.clear();
						//raw_tex_coord.clear();
						final_verts.clear();
						final_indices.clear();
						final_norms.clear();
						final_tex_coords.clear();
						//Then add a new GLMesh to our list for building
						meshes.add(new GLMesh());
						break;
					case 118: //v
						if (line.charAt(1) == 't'){
							//The line is a texture coordinate
							String [] line_parts = line.split("\\s+");
							float tmp_u = new Float(line_parts[1]);
							float tmp_v = new Float(line_parts[2]);
							raw_tex_coord.add(new GLTexCoord(tmp_u, tmp_v));
						}
						else if (line.charAt(1) == 'n'){
							//The line is a normal
							String [] line_parts = line.split("\\s+");
							float tmp_x = new Float(line_parts[1]);
							float tmp_y = new Float(line_parts[2]);
							float tmp_z = new Float(line_parts[3]);
							GLVector3 tmp_vec = new GLVector3(tmp_x, tmp_y, tmp_z);
							tmp_vec.normalize();
							raw_normals.add(tmp_vec);
						}
						else{
							//The line is a new vertex
							String [] line_parts = line.split("\\s+");
							float tmp_x = new Float(line_parts[1]);
							float tmp_y = new Float(line_parts[2]);
							float tmp_z = new Float(line_parts[3]);
							raw_verts.add(new GLVector3(tmp_x, tmp_y, tmp_z));
						}
						break;
						
					case 115: //s
						//The line toggles smooth shading
						break;
						
					case 102: //f
						//The line declares a face (where the magic happens :) )
						String [] line_parts = line.split("\\s+");
						//Declare and initialize our temporary arrays
						int[] vert_indicies = new int[line_parts.length - 1];
						int[] tex_indicies = new int[line_parts.length - 1];
						int[] norm_indicies = new int[line_parts.length - 1];
						for (int i = 0; i < vert_indicies.length; i++)
						{
							vert_indicies[i] = 0;
							tex_indicies[i] = 0;
							norm_indicies[i] = 0;
						}
						
						//Begin parsing the face line
						for (int i = 1; i < line_parts.length; i++)
						{
							if (line_parts[i].contains("//"))
							{
								//We are dealing with only verts and normals
								String [] sub_parts = line_parts[i].split("//");
								int tmp_vert_ind = new Integer(sub_parts[0]);
								int tmp_norm_ind = new Integer(sub_parts[1]);
								vert_indicies[i - 1] = tmp_vert_ind;
								norm_indicies[i - 1] = tmp_norm_ind;
							}
							else
							{
								//We have either verts, verts and texCoords, or verts and texCoords and norms
								String [] sub_parts = line_parts[i].split("/");
								if (sub_parts.length == 1)
								{
									//We are dealing with just verts
									int tmp_vert_ind = new Integer(sub_parts[0]);
									vert_indicies[i - 1] = tmp_vert_ind;
								}
								else if (sub_parts.length == 2)
								{
									//We are dealing with verts and texCoords
									int tmp_vert_ind = new Integer(sub_parts[0]);
									int tmp_texCoord_ind = new Integer(sub_parts[1]);
									vert_indicies[i - 1] = tmp_vert_ind;
									tex_indicies[i - 1] = tmp_texCoord_ind;
								}
								else
								{
									//We have all three parts
									int tmp_vert_ind = new Integer(sub_parts[0]);
									int tmp_texCoord_ind = new Integer(sub_parts[1]);
									int tmp_norm_ind = new Integer(sub_parts[2]);
									vert_indicies[i - 1] = tmp_vert_ind;
									tex_indicies[i - 1] = tmp_texCoord_ind;
									norm_indicies[i - 1] = tmp_norm_ind;
								}
							}
						}
						//Now that we have our indicies, we will add the faces to
						//the most recient GLMesh
						//Because a face listing may have more than 3 verts declared (not triangle shape)
						//we will attempt to automatically triangulate the face
						int second_vert = 1;
						while (vert_indicies.length - second_vert >= 2)
						{
							//Get our actual vertices
							GLVector3 vert1 = null;
							GLVector3 vert2 = null;
							GLVector3 vert3 = null;

							vert1 = raw_verts.elementAt(vert_indicies[0] - 1);
							vert2 = raw_verts.elementAt(vert_indicies[second_vert] - 1);
							vert3 = raw_verts.elementAt(vert_indicies[second_vert + 1] - 1);
							
							//Get our actual normals
							GLVector3 norm1 = null;
							GLVector3 norm2 = null;
							GLVector3 norm3 = null;
							if (norm_indicies[0] == 0)
							{
								//We have no normals from the file. We should
								//make our own
								norm1 = vert2.sub(vert1).cross(vert3.sub(vert2));
								norm2 = vert2.sub(vert1).cross(vert3.sub(vert2));
								norm3 = vert2.sub(vert1).cross(vert3.sub(vert2));
							}
							else
							{
								norm1 = raw_normals.elementAt(norm_indicies[0] - 1);
								norm2 = raw_normals.elementAt(norm_indicies[second_vert] - 1);
								norm3 = raw_normals.elementAt(norm_indicies[second_vert + 1] - 1);
							}
							norm1.normalize();
							norm2.normalize();
							norm3.normalize();
							
							//Get our actual texture coordinates
							GLTexCoord uv1 = null;
							GLTexCoord uv2 = null;
							GLTexCoord uv3 = null;
							if (tex_indicies[0] == 0)
							{
								//Well, we have no texture coordinates, so lets just make them up!
								uv1 = new GLTexCoord(0,0);
								uv2 = new GLTexCoord(1,0);
								uv3 = new GLTexCoord(1,1);
							}
							else
							{
								uv1 = raw_tex_coord.elementAt(tex_indicies[0] - 1);
								uv2 = raw_tex_coord.elementAt(tex_indicies[second_vert] - 1);
								uv3 = raw_tex_coord.elementAt(tex_indicies[second_vert + 1] - 1);
							}
							
							//Finally, we will add our verts, normals, and texture coordinates to the
							//running array to be used for the GLMesh
							//Vert1
							final_verts.add(vert1.getX());
							final_verts.add(vert1.getY());
							final_verts.add(vert1.getZ());
							final_norms.add(norm1.getX());
							final_norms.add(norm1.getY());
							final_norms.add(norm1.getZ());
							final_tex_coords.add(uv1.getU());
							final_tex_coords.add(uv1.getV());
							final_indices.add((short)((final_verts.size() / 3) - 1));
							//Vert2
							final_verts.add(vert2.getX());
							final_verts.add(vert2.getY());
							final_verts.add(vert2.getZ());
							final_norms.add(norm2.getX());
							final_norms.add(norm2.getY());
							final_norms.add(norm2.getZ());
							final_tex_coords.add(uv2.getU());
							final_tex_coords.add(uv2.getV());
							final_indices.add((short)((final_verts.size() / 3) - 1));
							//Vert3
							final_verts.add(vert3.getX());
							final_verts.add(vert3.getY());
							final_verts.add(vert3.getZ());
							final_norms.add(norm3.getX());
							final_norms.add(norm3.getY());
							final_norms.add(norm3.getZ());
							final_tex_coords.add(uv3.getU());
							final_tex_coords.add(uv3.getV());
							final_indices.add((short)((final_verts.size() / 3) - 1));
							second_vert += 1;
						}
						
						break;
						
					case 117: //u
						if(line.substring(0,6).equals("usemtl")){
							//The line is declaring what material the object uses
							GLMesh cur_mesh = meshes.lastElement();
							GLMaterial mesh_material = materials.get(line.substring(7,line.length()-1));
							cur_mesh.setColor(mesh_material.ambient_color, mesh_material.diffuse_color, mesh_material.specular_color, mesh_material.shininess);
							if (mesh_material.textureBitmap != null)
							{
								cur_mesh.loadBitmap(mesh_material.textureBitmap);
							}
						}
						break;
						
					case 109: //m
						String tmp_string = line.substring(0,6);
						if(tmp_string.equals("mtllib")){
							//The line is declaring a material file to load
							String mat_file_name = line.substring(7, line.length() - 5);
							int mat_file_id = myApp.getResources().getIdentifier(mat_file_name, "raw", "yei.tssBtTestApp");
							InputStream mtl_in_stream = myApp.getResources().openRawResource(mat_file_id);
							LoadMtl(mtl_in_stream, myApp);
						}
						break;
				}
				
			}
			obj_in_stream.close();
			//First lets append the current vert/normal/uv data to
			//the current GLMesh (if there is one)
			GLMesh cur_mesh = meshes.lastElement();
			//Vertices
			float[] vert_array = new float[final_verts.size()];
			for (int i = 0; i < final_verts.size(); i++)
			{
				vert_array[i] = final_verts.elementAt(i);
			}
			cur_mesh.setVertices(vert_array);
			//Normals
			float[] norm_array = new float[final_norms.size()];
			for (int i = 0; i < final_norms.size(); i++)
			{
				norm_array[i] = final_norms.elementAt(i);
			}
			cur_mesh.setNormals(norm_array);
			//Texture Coordinates
			float[] uv_array = new float[final_tex_coords.size()];
			for (int i = 0; i < final_tex_coords.size(); i++)
			{
				uv_array[i] = final_tex_coords.elementAt(i);
			}
			cur_mesh.setTextureCoordinates(uv_array);
			//Indicies
			short[] indice_array = new short[final_indices.size()];
			for (int i = 0; i < final_indices.size(); i++)
			{
				indice_array[i] = final_indices.elementAt(i);
			}
			cur_mesh.setIndices(indice_array);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
