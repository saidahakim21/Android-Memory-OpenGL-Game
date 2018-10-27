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

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class OpenGLES20Activity extends Activity {

    private MyGLSurfaceView mGLView;
    private int posX;   // centre de random  triangle  X
    private int posY;   // centre de random triangle   Y
    private float x1;    // premier point x   ( pour  dessiner le random triangle par rapport au random centre(posX,posY)
    private float y1;    // premier point y
    private float x2;       // deuxieme point x
    private float y2;      //  ......
    public float x3;        //  ......
    private      int i;       //  index de boucle d animation

    float green;
    float red;
    float bleu;
    private  int columnNum;// nombre de column horizontale , nombre de ligne est calcule  selon le ration 9/16
    public  int speed;  //  speed d animation
    public  int NumActions;     // nombre de fois le triangle s affiche
    public  int result;         // nombre de cout correct par l user
    public  int [] realPosXs;       //  pour sauvgarder le posX chaque centre du random triangle (posX,posY)
    public  int [] realPosYs;       //pour sauvgarder le posY chaque centre du random triangle (posX,posY)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //recuperer les donnee envoye par la premiere activity
        columnNum = getIntent().getIntExtra("columnNum",0);
        speed = getIntent().getIntExtra("speed",0);
        NumActions = getIntent().getIntExtra("NumActions",0);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //envoyer reference to this object pour recevoire la reponce lors de fin de saisie par l'user
        mGLView = new MyGLSurfaceView(this,NumActions,this,columnNum);
        setContentView(mGLView);

        // set ratios  pour designer les lignes



        new Thread(new Runnable() {
            @Override

            public void run() {

            /*

            blocker le thread jusqu'a la creation des  autres object
                        si non le thread trouve une valeur NULL dans les valeur resU,resV,...
                                                object non cree encore
                   */

               try {
                    MainActivity.available.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println("Thread launched ");
            /*    try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                int resU = (int)mGLView.mRenderer.mPlane.resU;
                int resV = (int)mGLView.mRenderer.mPlane.resV;
                int ratiosXs = mGLView.mRenderer.mPlane.ratioXs;
                int ratiosYs = mGLView.mRenderer.mPlane.ratioYs;

                for (i=0;i<NumActions;i++) {
                    Random rnd = new Random();



                    // positioner random  centre X  pour dessiner le triangle

                    if (ratiosXs % 2 == 0) {
                        //   ratio Xs is pair
                        posX=0;
                        while(posX==0)  // si le nombre de column est paire en doit  par dessiner au milieu de l ecrant car il y a une ligne qui appartient
                            posX = rnd.nextInt() % (ratiosXs / 2 + 1);// au plan juste au milieu



                        if (posX < 0) {
                            x1 = (float) (posX * resU + 0.1 * resU);
                            x2 = (float) (posX * resU + 0.5 * resU);
                            x3 = (float) (posX * resU + 0.9 * resU);
                        } else {
                            x1 = (float) (posX * resU - 0.9 * resU);
                            x2 = (float) (posX * resU - 0.5 * resU);
                            x3 = (float) (posX * resU - 0.1 * resU);


                        }
                    } else {// ratioXs  is impaire
                        posX = rnd.nextInt() % ((ratiosXs + 1) / 2);
                        // ici meme s il est zero le problem ne se pose pas

                        if (posX < 0) {
                            x1 = (float) (posX * resU - resU * 0.9 / 2);
                            x2 = (float) (posX * resU);
                            x3 = (float) (posX * resU + resU * 0.9 / 2);
                        } else {
                            x1 = (float) (posX * resU - resU * 0.9 / 2);
                            x2 = (float) (posX * resU);
                            x3 = (float) (posX * resU + resU * 0.9 / 2);


                        }
                    }



//same thing for centre Y
                    if (ratiosYs % 2 == 0) {
                        posY = 0;

                        while (posY==0)
                            posY = rnd.nextInt() % (ratiosYs / 2 + 1);


                        if (posY < 0) {
                            y1 = (float) (posY * resV + 0.9 * resV);
                            y2 = (float) (posY * resV + 0.1 * resV);

                        } else {
                            y2 = (float) (posY * resV - 0.9 * resV);
                            y1 = (float) (posY * resV - 0.1 * resV);

                        }

                    } else {// triangle between lines
                        posY = rnd.nextInt() % ((ratiosYs + 1) / 2);
                        if (posY < 0) {
                            y2 = (float) (posY * resV - resV * 0.4);
                            y1 = (float) (posY * resV + resV * 0.5);
                        } else {
                            y2 = (float) (posY * resV - resV * 0.4);
                            y1 = (float) (posY * resV + resV * 0.5);
                        }
                    }

                    float tmp = rnd.nextFloat();
                     red =Math.abs (tmp-((int)tmp));
                    tmp = rnd.nextFloat();
                     green=Math.abs (tmp-((int)tmp));
                    tmp = rnd.nextFloat();
                     bleu=Math.abs (tmp-((int)tmp));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           mGLView.mRenderer.mTriangle.setColors(red,green,bleu);
                            mGLView.mRenderer.switchP(x1, y1, x2, y2, x3, y1); // afficher le triangle
                            mGLView.mRenderer.savePos(i,posX,-posY);   // sauvgarder la position

                        }
                    });


                    try {
                        Thread.sleep(speed*100+300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mGLView.mRenderer.switchP(0f, 0f, 0f, 0f, 0f, 0f);


                }


            }
        }).start();




    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }



    public  void result(int[] Xs,int[] Ys){

        int i =0;
        result =0;



        for (i=0;i<Xs.length;i++){

            if(realPosXs[i]==Xs[i]  && realPosYs[i]==Ys[i])

                result=result+1;


        }

        Intent intent = new Intent(this, Result.class);
        intent.putExtra("result", result);
        intent.putExtra("numAction", NumActions);

        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}
