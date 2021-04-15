package jeuxmemoirev1_1.android.saidahakim21.com.jeuxmemoire;

/**
 * Created by Hakim on 5/5/2018.
 */

public class Plan {
    private Line[] mVLines;
    private Line[] mHLines;
    public int ratioXs;
    public int ratioYs;
    public int resU;
    public int resV;
    public int resHeight;
    public int resWidth;

    public Plan(int ratioXs, int ratioYs) {
        int i;
        mVLines = new Line[ratioXs];
        mHLines = new Line[ratioYs];
        for (i = 0; i < ratioXs; i++)
            mVLines[i] = new Line();
        for (i = 0; i < ratioYs; i++)
            mHLines[i] = new Line();

        this.ratioXs = ratioXs;
        this.ratioYs = ratioYs;
    }


    public void setVertexes(float resX, float resY) {

        this.resHeight = (int) resY;
        this.resWidth = (int) resX;
        resU = (int) resX / ratioXs;
        resV = (int) resY / ratioYs;
        int i = 0;
        int k = 0;

        while (k < resX & i < ratioXs) {
            this.mVLines[i].SetVerts(k - resX / 2, (resY / 2f), 0f, k - (resX / 2), -(resY / 2), 0f);
            k = k + resU;
            i++;
        }
        i = 0;
        k = 0;

        while (k < resY & i < ratioYs) {
            this.mHLines[i].SetVerts(-resX / 2, (resY / 2f) - k, 0f, (resX / 2), (resY / 2) - k, 0f);
            k = k + resV;
            i++;
        }

        MainActivity.available.release();
    }


    public void setColors(float r, float g, float b, float a) {
        int i;
        for (i = 0; i < ratioXs - 1; i++)
            mVLines[i].SetColor(r, g, b, a);

        for (i = 0; i < ratioYs - 1; i++)
            mHLines[i].SetColor(r, g, b, a);
    }

    public void draw(float[] mvpMatrix) {
        int i;
        for (i = 0; i < mVLines.length; i++)
            mVLines[i].draw(mvpMatrix);

        for (i = 0; i < mHLines.length; i++)
            mHLines[i].draw(mvpMatrix);
    }
}
