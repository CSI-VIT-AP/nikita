package com.example.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private Background background1,background2;
    private int screenX,screenY;
    private Paint paint;
    private Flight flight;
    public static float screenRatioX, screenRatioY;

    public GameView(Context context,int screenX,int screenY) {
        super(context);
        this.screenX = screenX;
        paint = new Paint();

        this.screenY = screenY;
        screenRatioX = 1920f/screenX;
        screenRatioY = 1080f / screenY;
        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());
        flight = new Flight(screenY,getResources());
        background2.x = screenX;
    }

    @Override
    public void run() {
        while (isPlaying){
            update ();
            draw();
            sleep();
        }
    }
    private void update(){
        background1.x -= 10 * screenRatioX;
        background2.x -=10 * screenRatioX;
        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
         }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }
        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y <0 )
            flight.y =0;
        if (flight.y > screenY-flight.height)
            flight.y = screenY - flight.height;

    }

    private void draw(){
        if (getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.background,background2.x,background2.y,paint);
            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }
    private void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
    public void pause(){
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX/2){
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                break;
        }
        return true;
    }



}
