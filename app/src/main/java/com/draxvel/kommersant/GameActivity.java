package com.draxvel.kommersant;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[] buyButtons;
    private TextView[] itemsCountText;
    private TextView moneyText;

    private static final int[] buyButtonIds = {
            R.id.item1_btn,
            R.id.item2_btn,
            R.id.item3_btn,
            R.id.item4_btn,
            R.id.item5_btn,
            R.id.item6_btn,
            R.id.item7_btn
    };

    private static final int[] itemCountIds = {
            R.id.item1_count_tv,
            R.id.item2_count_tv,
            R.id.item3_count_tv,
            R.id.item4_count_tv,
            R.id.item5_count_tv,
            R.id.item6_count_tv,
            R.id.item7_count_tv
    };

    private static final int[] itemDMoneys = {0, 1, 2, 4, 8, 16, 320};
    private static final int[] itemPrices = {0, 20, 80, 320, 1280, 5120, 204800};
    private static int[] itemCount;

    private static long money, dMoney, totalMoney;
    private static int cliclCount;

    private MediaPlayer backgroundMusic;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //no title and full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.avtivity_game);

        initView();
    }

    private void initView() {
        money = 0;
        dMoney = 0;
        totalMoney = 0;
        cliclCount = 0;

        buyButtons = new Button[buyButtonIds.length];
        itemsCountText = new TextView[itemCountIds.length];
        itemCount = new int [itemCountIds.length];

        handler = new Handler();

        //initialization
        for(int i = 0; i<buyButtons.length; i++){
            buyButtons[i] = (Button) findViewById(buyButtonIds[i]);
            buyButtons[i].setOnClickListener(this);
            if(i!=0){
                String currentText = buyButtons[i].getText().toString();
                buyButtons[i].setText(currentText+ "\n("+itemPrices[i]+"$ +" +itemDMoneys[i]+"$/с)");
            }
        }

        for(int i = 0; i<itemCountIds.length; i++){
            itemsCountText[i] = (TextView) findViewById(itemCountIds[i]);
        }

        moneyText = (TextView) findViewById(R.id.main_money_tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBalanceChecker();
        printTotalStat();
        prepareButtons();
        turnBackgroundMusic();
    }

    @Override
    protected void onPause() {
        stopBalanceChecker();

        if(backgroundMusic!=null){
            backgroundMusic.stop();
        }
        super.onPause();
    }

    private void printTotalStat() {
        moneyText.setText(money+"$, загалом: "+totalMoney+"$");
        for(int i = 0; i<itemCountIds.length; i++){
            itemsCountText[i].setText(""+itemCount[i]);
        }
    }

    private void turnBackgroundMusic() {
        if(AppData.isSoundOn){
            backgroundMusic = MediaPlayer.create(GameActivity.this, R.raw.hiphop);
            backgroundMusic.setLooping(true);
            backgroundMusic.start();
        }

    }

    private void prepareButtons() {
        for(int i=0; i<buyButtonIds.length; i++){
            if(money<itemPrices[i]){
                buyButtons[i].setEnabled(false);
            }else {
                buyButtons[i].setEnabled(true);
            }
        }
    }

    private void turnSoundEffect(){
        if(AppData.isSoundEffectOn){
            MediaPlayer mp = MediaPlayer.create(GameActivity.this, R.raw.coin);
            mp.start();
        }
    }

    private void turnVibration(){
        if(AppData.isVibrationOn){
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100);
        }
    }

    private void updateBalance(){
        money+=dMoney;
        totalMoney +=dMoney;
        printTotalStat();
        prepareButtons();
    }

    @Override
    public void onClick(View view) {
        for(int i = 0; i<buyButtonIds.length; i++){
            if(buyButtonIds[i]==view.getId()){
                cliclCount++;
                if(i==0){
                    money++;
                    itemCount[i]++;
                    totalMoney++;
                }
                else {
                    money -=itemPrices[i];
                    itemCount[i]++;
                    dMoney += itemDMoneys[i];
                    if(itemCount[i]==1){
                        //first time buy
                        turnSoundEffect();
                    }
                    if(i==buyButtonIds.length-1){
                        showDialog();
                    }
                }
                turnVibration();
            }
            printTotalStat();
            prepareButtons();
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Вітаю!")
                .setMessage("Ви розширили свою економічну імперію та виграли! \n Кількість кліків:" +cliclCount)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //balance checker
    Runnable mBalanceChecker = new Runnable() {
        @Override
        public void run() {
            updateBalance();
            handler.postDelayed(mBalanceChecker, 1*1000);
        }
    };

    private void startBalanceChecker(){
        mBalanceChecker.run();
    }

    private void stopBalanceChecker(){
        handler.removeCallbacks(mBalanceChecker);
    }
}
