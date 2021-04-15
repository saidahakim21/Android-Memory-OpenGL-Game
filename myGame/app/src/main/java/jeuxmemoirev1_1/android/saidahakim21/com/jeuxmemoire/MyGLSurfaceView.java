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

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


public class MyGLSurfaceView extends GLSurfaceView {

    public final MyGLRenderer mRenderer;
    public final OpenGLES20Activity myActivity;

    private int actions;

    public MyGLSurfaceView(Context context, int NumActions, OpenGLES20Activity superActivity, int columnNum) {
        super(context);
        setEGLContextClientVersion(2);
        actions = NumActions;
        myActivity = superActivity;
        mRenderer = new MyGLRenderer(actions, myActivity, columnNum);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        clickingTimes = 0;
        clickedPosXs = new int[actions];
        clickedPosYs = new int[actions];
    }

    public int clickingTimes;
    public int[] clickedPosXs;
    public int[] clickedPosYs;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean animationEnd = mRenderer.animationEnd;
        if (animationEnd) {
            // si le click est avant la fin d animation  ne fait rien
            //  animationEnd est initialise a false
            int x = (int) e.getX();
            int y = (int) e.getY();

            int resHeight = mRenderer.mPlane.resHeight;
            int resWidth = mRenderer.mPlane.resWidth;
            int resU = (int) mRenderer.mPlane.resU; // resolution du vecteur U dans le plan  , = resWidth / ratioXs(nombre de column)
            int resV = (int) mRenderer.mPlane.resV;// resolution du vecteur U dans le plan  , = resHeight / ratioYs(nombre de column)
            int ratioXs = mRenderer.mPlane.ratioXs;// nombre de column
            int ratioYs = mRenderer.mPlane.ratioYs;// nombre de ligne

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    //           dx = x - mPreviousX;
                    //         dy = y - mPreviousY;
                    break;

                case MotionEvent.ACTION_UP:
                    // For the first pointer that touches the screen. This starts the gesture
                    // inssi7ab

                    // algorithme pour recuperer les donnee
                    x = x - resWidth / 2;
                    y = -y;
                    y = y + resHeight / 2;


                    if (ratioXs % 2 == 0) {
                        if (x < 0) {
                            x = x / resU - 1;
                        } else
                            x = x / resU + 1;
                    } else {

                        if (x < 0) {
                            x = x - resU / 2;
                            x = x / resU;

                        } else {
                            x = x + resU / 2;
                            x = x / resU;
                        }
                    }

                    if (ratioYs % 2 == 0) {
                        if (y < 0) {
                            y = y / resV - 1;
                        } else
                            y = y / resV + 1;
                    } else {
                        if (y < 0) {
                            y = y - resV / 2;
                            y = y / resV;

                        } else {
                            y = y + resV / 2;
                            y = y / resV;
                        }
                    }


                    clickedPosXs[clickingTimes] = x;
                    clickedPosYs[clickingTimes] = y;
                    clickingTimes++;

                    if (clickingTimes == (actions)) {
                        this.myActivity.result(clickedPosXs, clickedPosYs);
                        clickingTimes = 0;
                    }

                    break;
            }
        }
        return true;
    }
}
