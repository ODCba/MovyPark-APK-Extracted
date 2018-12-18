package com.movypark.cordoba;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.io.IOException;

public class RegistrarActivity extends AppCompatActivity implements AsyncResponse {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 18;
    ProgressDialog pdialog = null;

    class C02211 implements OnEditorActionListener {
        C02211() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
            if (event.getKeyCode() != 66 || event.getAction() != 0) {
                return false;
            }
            ((InputMethodManager) RegistrarActivity.this.getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(((EditText) RegistrarActivity.this.findViewById(C0220R.id.txtTelefono)).getWindowToken(), 0);
            return true;
        }
    }

    class C02222 implements OnClickListener {
        C02222() {
        }

        public void onClick(View v) {
            RegistrarActivity.this.enviarSmsValidacion();
        }
    }

    class C02233 implements Runnable {
        C02233() {
        }

        public void run() {
            RegistrarActivity.this.pdialog.dismiss();
        }
    }

    class C02255 implements DialogInterface.OnClickListener {
        C02255() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02934 implements AsyncResponse {

        class C02241 implements Runnable {
            C02241() {
            }

            public void run() {
                if (new UsuarioEstacionamiento(RegistrarActivity.this.getApplicationContext()).getTelefono().length() >= 10) {
                    RegistrarActivity.this.pdialog.dismiss();
                    RegistrarActivity.this.finish();
                    return;
                }
                RegistrarActivity.this.pdialog.dismiss();
                RegistrarActivity.this.showInputDialog();
            }
        }

        C02934() {
        }

        public void processFinish(String output) {
            RegistrarActivity.this.pdialog.dismiss();
            RegistrarActivity.this.pdialog.setTitle("Validando SMS de registro...");
            RegistrarActivity.this.pdialog.show();
            RegistrarActivity.this.pdialog.setCancelable(false);
            new Handler().postDelayed(new C02241(), 10000);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0220R.layout.activity_registrar);
        obtenerTelefono();
        ((EditText) findViewById(C0220R.id.txtTelefono)).setOnEditorActionListener(new C02211());
        ((Button) findViewById(C0220R.id.btnEnviarSMS)).setOnClickListener(new C02222());
    }

    public void obtenerTelefono() {
        try {
            this.pdialog = new ProgressDialog(this);
            this.pdialog.setTitle("Aguarde un momento por favor...");
            this.pdialog.show();
            this.pdialog.setCancelable(false);
            PostData pd = new PostData();
            pd.delegate = this;
            pd.execute(new String[]{"http://wap.celu1.com/msisdn.aspx"});
            new Handler().postDelayed(new C02233(), 5000);
        } catch (Exception e) {
            Log.d("REGISTER", "Error Obteniendo Tel");
            e.printStackTrace();
        }
    }

    private void enviarSmsValidacion() {
        EditText txtTelefono = (EditText) findViewById(C0220R.id.txtTelefono);
        String operador = ((Spinner) findViewById(C0220R.id.spnCarriers)).getSelectedItem().toString();
        String telefono = txtTelefono.getText().toString();
        String telefonocarrier = "";
        String idmedia = "";
        if (operador.contains("Claro")) {
            idmedia = "130";
        } else if (operador.contains("Personal")) {
            idmedia = "96";
        } else if (operador.contains("Nextel")) {
            idmedia = "15";
        } else if (operador.contains("Movistar")) {
            idmedia = "49";
        } else {
            idmedia = "";
        }
        if (!operador.contains("eleccione") && telefono.length() == 10) {
            telefonocarrier = telefono + "|" + idmedia;
            this.pdialog = new ProgressDialog(this);
            this.pdialog.setTitle("Enviando SMS de registro");
            this.pdialog.show();
            new GatewayWS(new C02934()).execute(new String[]{"EnviarPIN", telefonocarrier});
        }
    }

    protected void showInputDialog() {
        View promptView = LayoutInflater.from(this).inflate(C0220R.layout.input_dialog, null);
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setView(promptView);
        final EditText txtPinManual = (EditText) promptView.findViewById(C0220R.id.txtPinManual);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            class C02261 implements DialogInterface.OnClickListener {
                C02261() {
                }

                public void onClick(DialogInterface dialog, int which) {
                }
            }

            class C02272 implements DialogInterface.OnClickListener {
                C02272() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    RegistrarActivity.this.showInputDialog();
                }
            }

            public void onClick(DialogInterface dialog, int id) {
                EditText txtTelefono = (EditText) RegistrarActivity.this.findViewById(C0220R.id.txtTelefono);
                String operador = ((Spinner) RegistrarActivity.this.findViewById(C0220R.id.spnCarriers)).getSelectedItem().toString();
                String pinToTel = RegistrarActivity.this.getPhoneFromPin(txtPinManual.getText().toString());
                String idmedia = "";
                if (operador.contains("Claro")) {
                    idmedia = "130";
                } else if (operador.contains("Personal")) {
                    idmedia = "96";
                } else if (operador.contains("Nextel")) {
                    idmedia = "15";
                } else if (operador.contains("Movistar")) {
                    idmedia = "49";
                } else {
                    idmedia = "";
                }
                if ((txtTelefono.getText().toString() + idmedia).equalsIgnoreCase(pinToTel.toString())) {
                    try {
                        UsuarioEstacionamiento usuarioEstacionamiento = new UsuarioEstacionamiento(txtTelefono.getText().toString(), idmedia, RegistrarActivity.this.getApplicationContext());
                        Intent mServiceIntent = new Intent(RegistrarActivity.this.getApplicationContext(), MainActivity.class);
                        mServiceIntent.setFlags(268435456);
                        RegistrarActivity.this.getApplicationContext().startActivity(mServiceIntent);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                new Builder(RegistrarActivity.this).setTitle("El pin ingresado no es correcto").setMessage("Desafortunadamente no pudimos obtener el sms de validacion automatico.\n Desea ingresar nuevamente el pin manual?").setPositiveButton(17039370, new C02272()).setNegativeButton(17039369, new C02261()).setIcon(17301543).show();
            }
        }).setNegativeButton("Cancel", new C02255());
        alertDialogBuilder.create().show();
    }

    private String getPhoneFromPin(String telefonoEnc) {
        return telefonoEnc.replace("A", "0").replace("B", "1").replace("C", "2").replace("D", "3").replace("E", "4").replace("F", "5").replace("G", "6").replace("H", "7").replace("I", "8").replace("J", "9");
    }

    public void onBackPressed() {
        finish();
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    public void processFinish(String output) {
        if (output.toString().trim().length() > 8) {
            try {
                String telefono = output.trim().substring(0, output.trim().indexOf("|"));
                telefono = telefono.substring(telefono.length() - 10);
                String operador = output.trim().substring(output.trim().indexOf("|") + 1);
                String idmedia = "";
                if (operador.toLowerCase().contains("claro")) {
                    idmedia = "130";
                } else if (operador.toLowerCase().contains("personal")) {
                    idmedia = "96";
                } else if (operador.toLowerCase().contains("nextel")) {
                    idmedia = "15";
                } else if (operador.toLowerCase().contains("movistar")) {
                    idmedia = "49";
                } else {
                    return;
                }
                if (new UsuarioEstacionamiento(telefono, idmedia, getApplicationContext()).datosValidos()) {
                    Intent mServiceIntent = new Intent(getApplicationContext(), MainActivity.class);
                    mServiceIntent.setFlags(268435456);
                    getApplicationContext().startActivity(mServiceIntent);
                    finish();
                }
            } catch (Exception e) {
            }
            this.pdialog.dismiss();
        }
    }
}
