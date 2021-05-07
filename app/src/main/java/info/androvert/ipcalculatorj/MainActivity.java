package info.androvert.ipcalculatorj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private EditText ipAddressEdit;
    private EditText idEdit;
    private boolean isSwitchChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressEdit = findViewById(R.id.editTextIp);
        idEdit = findViewById(R.id.editTextId);
        isSwitchChecked = false;

        Switch switchNetworkAddress = findViewById(R.id.switchNetworkIp);
        if (switchNetworkAddress != null) {
            switchNetworkAddress.setOnCheckedChangeListener(this);
        }
    }

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        Spinner mask = findViewById(R.id.spinnerMask);
        TextView id = findViewById(R.id.textId);
        TextView networkClass = findViewById(R.id.textDecNetworkClass);
        TextView decNetworkIpAddress = findViewById(R.id.textDecNetworkIpAddress);
        TextView subnetNode = findViewById(R.id.textDecSubnetNode);
        TextView subnet = findViewById(R.id.textDecSubnet);
        TextView decFirstIpAddress = findViewById(R.id.textDecFirstIpAddress);
        TextView decLastIpAddress = findViewById(R.id.textDecLastIpAddress);
        TextView decBroadcastAddress = findViewById(R.id.textDecBroadcastAddress);
        TextView binIpAddress = findViewById(R.id.textBinIpAddress);
        TextView binMask = findViewById(R.id.textBinMask);
        TextView binNetworkIpAddress = findViewById(R.id.textBinNetworkIpAddress);
        TextView binFirstIpAddress = findViewById(R.id.textBinFirstIpAddress);
        TextView binLastIpAddress = findViewById(R.id.textBinLastIpAddress);
        TextView binBroadcastAddress = findViewById(R.id.textBinBroadcastAddress);

        try {

            IpCalculator ipCalculator = null;

            if (!isSwitchChecked) {
                ipCalculator = new IpCalculator(
                        ipAddressEdit.getText().toString(),
                        mask.getSelectedItem().toString()
                );
            } else {
                ipCalculator = new IpCalculator(
                        ipAddressEdit.getText().toString(),
                        mask.getSelectedItem().toString(),
                        idEdit.getText().toString()
                );
            }

            id.setText(getString(R.string.text_id) + ipCalculator.getBinId(true));
            networkClass.setText(getString(R.string.text_network_class) + ipCalculator.getIpClass());
            decNetworkIpAddress.setText(getString(R.string.text_network_ip_address) + ipCalculator.getBinSubnetAddress(false));
            subnetNode.setText(getString(R.string.text_subnet_node) + ipCalculator.determiningNumberOfNodes());
            subnet.setText(getString(R.string.text_subnet) + ipCalculator.determiningNumberOfSubnet());
            decFirstIpAddress.setText(getString(R.string.text_first_ip_address) + ipCalculator.calculateFirstIpAddress(false));
            decLastIpAddress.setText(getString(R.string.text_last_ip_address) + ipCalculator.calculateLastIpAddress(false));
            decBroadcastAddress.setText(getString(R.string.text_broadcast_address) + ipCalculator.calculateBroadcastAddress(false));
            binIpAddress.setText(getString(R.string.text_ip) + ipCalculator.getBinIpAddress(true));
            binMask.setText(getString(R.string.text_mask) + ipCalculator.getBinMask(true));
            binNetworkIpAddress.setText(getString(R.string.text_network_ip_address) + ipCalculator.getBinSubnetAddress(true));
            binFirstIpAddress.setText(getString(R.string.text_first_ip_address) + ipCalculator.calculateFirstIpAddress(true));
            binLastIpAddress.setText(getString(R.string.text_last_ip_address) + ipCalculator.calculateLastIpAddress(true));
            binBroadcastAddress.setText(getString(R.string.text_broadcast_address) + ipCalculator.calculateBroadcastAddress(true));

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
        builder.setTitle(getString(R.string.about_title))
                .setView(view)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.about_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ipAddressEdit.setHint(getString(R.string.hint_ip_state_two));
            idEdit.setVisibility(View.VISIBLE);
            isSwitchChecked = true;
        } else {
            ipAddressEdit.setHint(getString(R.string.hint_ip_state_one));
            idEdit.setVisibility(View.INVISIBLE);
            isSwitchChecked = false;
        }
    }
}