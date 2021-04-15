
package jeuxmemoirev1_1.android.saidahakim21.com.jeuxmemoire;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    int i;
    private static final String TAG = "MyGLRenderer";
    public Triangle mTriangle;
    private float[] mMVPMatrix = new float[16];   // Model  View Projection matrix
    private final float[] mProjectionMatrix = new float[16];  // projection matrix
    private final float[] mModelMatrix = new float[16]; // model matrix du plan
    private final float[] mModelMatrix2 = new float[16];  // model matrix du triangle
    private final float[] mViewMatrix = new float[16];   // View Matrix
    private final float[] mVPMatrix = new float[16];    //  View Projection Matrix
    public final OpenGLES20Activity myActivity;    // pour envoyer les position reel au l activite et faire le test
    private int[] posXs;   // pour sauvgarder les posXs reel  afficher sur l ecrant
    private int[] posYs;   // pour sauvgarder  les posYs reel afficher sur l ecrant
    public boolean animationEnd = false;   //  pour ne pas commencer a recuperer les click de l utilisateur avant la fin de l animation
    private int rX;            // nombre de column pour passer au constructeur du plan
    private int rY;                // nombre de ligne calculer a partir du nombre de column en utilisant le ratio 9/16 utilise dans la majorite des smartphone
    // private float mAngle;   non utiliser
    public Plan mPlane;  //  le plan
    private int actions;  // le nombre d action

    public MyGLRenderer(int actions, OpenGLES20Activity superActivity, int columnNum) {
        this.rX = columnNum;
        float r2 = 16f / 9f * columnNum;
        int r3 = (int) r2;
        this.rY = r3;
        this.actions = actions; // initialisation de nombre d action de l animation (nombre de fois le triangle apparait)
        myActivity = superActivity; //  pour reenvoyer le resultat (posXs ) and  posYs
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // almost fully edited
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // set background color white
        mTriangle = new Triangle(0f, 0.0f, 0.0f, 0f, 0f, 0.0f);  // crier  un triangle  invisible  pour l utiliser a l animation ulterierement
        posXs = new int[actions]; // initialisation du tableau qui porte les position random apparait  de triangle
        posYs = new int[actions];       // le nombre de positions est egale au nombre de fois d animation (actions )
        mPlane = new Plan(rX, rY); // creer le plan

        Matrix.setIdentityM(mMVPMatrix, 0); // set matrixs to identity
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mModelMatrix2, 0);
        Matrix.setIdentityM(mVPMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.multiplyMM(mVPMatrix, 0, mViewMatrix, 0, mProjectionMatrix, 0); // cree VP matrix
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // almost fully edited
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.multiplyMM(mVPMatrix, 0, mViewMatrix, 0, mProjectionMatrix, 0);
        Matrix.multiplyMM(scratch, 0, mModelMatrix, 0, mVPMatrix, 0);
        mPlane.draw(scratch);
        Matrix.setIdentityM(scratch, 0);
        Matrix.multiplyMM(scratch, 0, mModelMatrix2, 0, mVPMatrix, 0);
        mTriangle.draw(scratch);
    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        //edited  here
        mPlane.setVertexes(width, height);
        //edited  here
        Matrix.orthoM(mProjectionMatrix, 0, -width / 2f, width / 2f, height / 2f, -height / 2f, -1f, 1f);
    }

    public void switchP(float x1, float y1, float x2, float y2, float x3, float y3) { // pour switcher la position du triangle en utilisant les cordonner
        mTriangle.setVerts(x1, y1, x2, y2, x3, y3);                                  // x1,x2... et  en reappelon le shader
    }

    public void dragScreen(float dx, float dy) {
        Matrix.translateM(mViewMatrix, 0, dx, -dy, 0.0f);
    }

    public void dragObject(float dx, float dy) {
        Matrix.translateM(mModelMatrix2, 0, dx, dy, 0.0f);
    }

    public void savePos(int i, int posX, int posY) {
        posXs[i] = posX;
        posYs[i] = posY;

        //  if (i == (OpenGLES20Activity.NumActions-1)){
        if (i == (actions - 1)) { // donner un peut  de temp au utilisateur pour realiser la position du dernier triangle
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myActivity.realPosXs = posXs;
            myActivity.realPosYs = posYs;
            animationEnd = true;
            this.switchP(0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public void setObject(float dx, float dy) {
        Matrix.translateM(mModelMatrix2, 0, dx, dy, 0.0f);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }


    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}