package info.androvert.ipcalculatorj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        EditText ipAddress = findViewById(R.id.editTextIp);
        Spinner mask = findViewById(R.id.spinnerMask);
        TextView networkClass = findViewById(R.id.textDecNetworkClass);
        TextView decNetworkIpAddress = findViewById(R.id.textDecNetworkIpAddress);
        TextView subnetNode = findViewById(R.id.textDecSubnetNode);
        TextView subnet = findViewById(R.id.textDecSubnet);
        TextView decFirstIpAddress = findViewById(R.id.textDecFirstIpAddress);
        TextView decLastIpAddress = findViewById(R.id.textDecLastIpAddress);
        TextView decBroadcastAddress = findViewById(R.id.textDecBroadcastAddress);

        try {

            IpCalculator ipCalculator = new IpCalculator(
                    ipAddress.getText().toString(),
                    mask.getSelectedItem().toString()
            ) ;

            networkClass.setText(getString(R.string.text_network_class) + ipCalculator.getIpClass());
            decNetworkIpAddress.setText(getString(R.string.text_network_ip_address) + ipCalculator.getBinSubnetAddress(false));
            subnetNode.setText(getString(R.string.text_subnet_node) + ipCalculator.determiningNumberOfNodes());
            subnet.setText(getString(R.string.text_subnet) + ipCalculator.determiningNumberOfSubnet());
            decFirstIpAddress.setText(getString(R.string.text_first_ip_address) + ipCalculator.calculateFirstIpAddress(false));
            decLastIpAddress.setText(getString(R.string.text_last_ip_address) + ipCalculator.calculateLastIpAddress(false));
            decBroadcastAddress.setText(getString(R.string.text_broadcast_address) + ipCalculator.calculateBroadcastAddress(false));

        } catch (Exception e) {

            System.out.println("Exception: " + e);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickAbout(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater()
                .inflate(R.layout.dialog_about, null);
        builder.setTitle(getString(R.string.aboutTitle))
                .setView(view)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.aboutClose),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}