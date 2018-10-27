/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jeuxmemoirev1_1.android.saidahakim21.com.jeuxmemoire;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Triangle {






    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private  FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;





    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
     float triangleCoords[] ={
             // in counterclockwise order:
             -0.25f,  0.0f, 0.0f,   // top
             0.0f, 0.3f, 0.0f,   // bottom left
             0.25f, 0.0f, 0.0f    // bottom right
     };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1f, 0.07843137254f, 0.5764705882f, 0.0f };

    public Triangle() {
         ByteBuffer bb = ByteBuffer.allocateDirect(
                 triangleCoords.length * 4);
         bb.order(ByteOrder.nativeOrder());

         vertexBuffer = bb.asFloatBuffer();
         vertexBuffer.put(triangleCoords);
         vertexBuffer.position(0);

         int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);


    }




// by me

    public void setVerts(float x1, float y1, float x2, float y2, float x3, float y3) {
        triangleCoords[0] = x1;
        triangleCoords[1] = y1;
        triangleCoords[3] = x2;
        triangleCoords[4] = y2;
        triangleCoords[6] = x3;
        triangleCoords[7] = y3;


        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }




    public void setColors(float r, float g, float b) {
        color[0]=r;
        color[1]=g;
        color[2]=b;
    }


    public Triangle(float x1,float y1,float x2,float y2,float x3,float y3) {
        float[] coords ={
             x1,  y1, 0.0f,   // top
                    x2, y2, 0.0f,   // bottom left
                    x3, y3, 0.0f    // bottom right
        };
            triangleCoords = coords;
         ByteBuffer bb = ByteBuffer.allocateDirect(
                 triangleCoords.length * 4);
         bb.order(ByteOrder.nativeOrder());

         vertexBuffer = bb.asFloatBuffer();
         vertexBuffer.put(triangleCoords);
         vertexBuffer.position(0);

         int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);


    }







    public void draw(float[] mvpMatrix) {

         GLES20.glUseProgram(mProgram);

         mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

         GLES20.glEnableVertexAttribArray(mPositionHandle);

         GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

         mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

         GLES20.glUniform4fv(mColorHandle, 1, color, 0);

         mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

         GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

         GLES20.glDisableVertexAttribArray(mPositionHandle);
      }

}
