package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    IntentFilter intentFilter = new IntentFilter();
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            int number1 = Integer.parseInt(((EditText)findViewById(R.id.number1)).getText().toString());
            int number2 = Integer.parseInt(((EditText)findViewById(R.id.number2)).getText().toString());
            switch (view.getId()) {
                case R.id.press_me:
                    ((EditText)findViewById(R.id.number1)).setText(String.valueOf(number1 + 1));
                    break;
                case R.id.press_me2:
                    ((EditText)findViewById(R.id.number2)).setText(String.valueOf(number2 + 1));
                    break;
                case R.id.navigate_to_secondary_activity_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int numberOfClicks = number1 + number2;
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, 1);
                    break;
            }
            if (number1 + number2> Constants.NUMBER_OF_CLICKS_THRESHOLD
                    ) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", number1);
                intent.putExtra("secondNumber", number2);
                getApplicationContext().startService(intent);


                Context context = getApplicationContext();
                CharSequence text = "Threshold reached";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


                //serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        //ex D
        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

        Button press_me = (Button)findViewById(R.id.press_me);
        Button press_me2 = (Button)findViewById(R.id.press_me2);
        Button navigate = (Button)findViewById(R.id.navigate_to_secondary_activity_button);
        press_me.setOnClickListener(buttonClickListener);
        press_me2.setOnClickListener(buttonClickListener);
        navigate.setOnClickListener(buttonClickListener);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText number1 = (EditText)findViewById(R.id.number1);
        EditText number2 = (EditText)findViewById(R.id.number2);
        savedInstanceState.putString(Constants.NUMBER1, number1.getText().toString());
        savedInstanceState.putString(Constants.NUMBER2, number2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText number1= (EditText)findViewById(R.id.number1);
        EditText number2= (EditText)findViewById(R.id.number2);
        if (savedInstanceState.getString(Constants.NUMBER1) != null) {
            number1.setText(savedInstanceState.getString(Constants.NUMBER1));
        }
        if (savedInstanceState.getString(Constants.NUMBER2) != null) {
            number2.setText(savedInstanceState.getString(Constants.NUMBER2));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
