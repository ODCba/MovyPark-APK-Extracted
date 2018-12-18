package com.movypark.cordoba;

import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int NOTIFY_ME_ID = 1337;
    Boolean EstaEstacionado = Boolean.valueOf(false);
    Boolean EstaRegistrado = Boolean.valueOf(false);
    Boolean EstacionamientoHabilitado = Boolean.valueOf(false);
    String PatenteLast = "";
    double Saldo = 0.0d;
    Boolean SaldoSuficiente = Boolean.valueOf(false);
    Location carlocation = null;
    private GoogleApiClient client;
    Handler handler = null;
    boolean intentoRegistrar = false;
    LocationListener locListener;
    private GoogleMap mMap;
    ProgressDialog pdialog = null;
    Runnable runnable = null;
    private int tiempoMinimo = 30;
    UsuarioEstacionamiento usr = null;

    class C02053 implements OnClickListener {
        C02053() {
        }

        public void onClick(View view) {
            MainActivity.this.cambiarPatente();
        }
    }

    class C02064 implements OnClickListener {
        C02064() {
        }

        public void onClick(View view) {
            MainActivity.this.cambiarPatente();
        }
    }

    class C02178 implements DialogInterface.OnClickListener {
        C02178() {
        }

        public void onClick(DialogInterface dialog, int which) {
            MainActivity.this.finish();
        }
    }

    class C02189 implements DialogInterface.OnClickListener {
        C02189() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    class C02916 implements AsyncResponse {

        class C02132 implements DialogInterface.OnClickListener {
            C02132() {
            }

            public void onClick(DialogInterface dialog, int which) {
            }
        }

        C02916() {
        }

        public void processFinish(String output) {
            LinearLayout tbEstado = (LinearLayout) MainActivity.this.findViewById(C0220R.id.tbEstado);
            final TextView txtPatente = (TextView) MainActivity.this.findViewById(C0220R.id.txtPatente);
            Button btnCambiarPatente = (Button) MainActivity.this.findViewById(C0220R.id.btnCambiarPatente);
            TextView lblEstado = (TextView) MainActivity.this.findViewById(C0220R.id.lblEstado);
            TextView lblSaldo = (TextView) MainActivity.this.findViewById(C0220R.id.lblSaldoMovypay);
            Button btnIniciar = (Button) MainActivity.this.findViewById(C0220R.id.btnIniciar);
            try {
                JSONObject json = new JSONObject(output);
                JSONObject jSONObject;
                try {
                    if (json.getInt("Status") != 1) {
                        throw new Exception(json.getString("Error"));
                    }
                    MainActivity.this.EstaRegistrado = Boolean.valueOf(json.getBoolean("EstaRegistrado"));
                    MainActivity.this.EstaEstacionado = Boolean.valueOf(json.getBoolean("EstaEstacionado"));
                    MainActivity.this.SaldoSuficiente = Boolean.valueOf(json.getBoolean("SaldoSuficiente"));
                    MainActivity.this.PatenteLast = json.getString("PatenteLast");
                    MainActivity.this.Saldo = json.getDouble("Saldo");
                    MainActivity.this.EstacionamientoHabilitado = Boolean.valueOf(json.getBoolean("EstacionamientoHabilitado"));
                    txtPatente.setText(MainActivity.this.PatenteLast);
                    lblSaldo.setText("$" + MainActivity.this.Saldo);
                    txtPatente.setEnabled(true);
                    if (MainActivity.this.EstaRegistrado.booleanValue()) {
                        if (!MainActivity.this.EstacionamientoHabilitado.booleanValue()) {
                            lblEstado.setTextColor(-7829368);
                            lblSaldo.setTextColor(-7829368);
                            lblEstado.setText("No rige estacionamiento");
                            btnIniciar.setBackgroundColor(-1);
                            btnIniciar.setText("NO RIGE ESTACIONAMIENTO");
                            btnIniciar.setTextColor(-7829368);
                            txtPatente.setEnabled(false);
                            btnCambiarPatente.setEnabled(false);
                        } else if (!MainActivity.this.SaldoSuficiente.booleanValue()) {
                            lblEstado.setTextColor(Color.parseColor("#c7b61d"));
                            lblSaldo.setTextColor(Color.parseColor("#c7b61d"));
                            btnIniciar.setBackgroundColor(Color.parseColor("#c7b61d"));
                            btnIniciar.setTextColor(-1);
                            btnIniciar.setText("SALDO INSUFICIENTE");
                            txtPatente.setEnabled(false);
                            lblEstado.setText("SALDO INSUFICIENTE");
                            btnCambiarPatente.setEnabled(false);
                        } else if (MainActivity.this.EstaEstacionado.booleanValue()) {
                            txtPatente.setEnabled(false);
                            txtPatente.setText(MainActivity.this.PatenteLast);
                            lblEstado.setText("ESTACIONADO");
                            btnIniciar.setText("FINALIZAR ESTACIONAMIENTO");
                            btnIniciar.setBackgroundColor(Color.parseColor("#1a8861"));
                            lblEstado.setTextColor(Color.parseColor("#1a8861"));
                            lblSaldo.setTextColor(Color.parseColor("#1a8861"));
                            btnIniciar.setTextColor(-1);
                            btnCambiarPatente.setEnabled(false);
                        } else {
                            txtPatente.setEnabled(true);
                            lblEstado.setText("NO ESTACIONADO");
                            btnIniciar.setText("INICIAR ESTACIONAMIENTO");
                            btnIniciar.setBackgroundColor(Color.parseColor("#1a8861"));
                            lblEstado.setTextColor(Color.parseColor("#1a8861"));
                            lblSaldo.setTextColor(Color.parseColor("#1a8861"));
                            btnIniciar.setTextColor(-1);
                            btnCambiarPatente.setEnabled(true);
                        }
                        MainActivity.this.consultarMultasPendientes();
                        jSONObject = json;
                        MainActivity.this.pdialog.dismiss();
                    }
                    MainActivity.this.registrarTelefono();
                    jSONObject = json;
                } catch (Exception e) {
                    jSONObject = json;
                    new Builder(MainActivity.this).setTitle("Error al consultar estado").setMessage("Ocurrio un error al consultar el estado").setPositiveButton(17039379, new C02132()).setNegativeButton(17039369, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            txtPatente.setText(MainActivity.this.PatenteLast.toUpperCase());
                        }
                    }).setIcon(17301543).show();
                    MainActivity.this.pdialog.dismiss();
                }
            } catch (Exception e2) {
                new Builder(MainActivity.this).setTitle("Error al consultar estado").setMessage("Ocurrio un error al consultar el estado").setPositiveButton(17039379, new C02132()).setNegativeButton(17039369, /* anonymous class already generated */).setIcon(17301543).show();
                MainActivity.this.pdialog.dismiss();
            }
        }
    }

    class C02927 implements AsyncResponse {

        class C02151 implements DialogInterface.OnClickListener {

            class C02141 implements DialogInterface.OnClickListener {
                C02141() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                }
            }

            C02151() {
            }

            public void onClick(DialogInterface dialog, int which) {
                new Builder(MainActivity.this).setTitle("La aplicacion no funciona sin conexion a internet").setMessage("Si no tiene internet puede utilizar el SMS, enviando E al 54351.").setPositiveButton(17039379, new C02141()).setIcon(17301659).show();
            }
        }

        class C02162 implements DialogInterface.OnClickListener {
            C02162() {
            }

            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.this.registrarTelefono();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        C02927() {
        }

        public void processFinish(String output) {
            MainActivity.this.pdialog.dismiss();
            if (output != "") {
                MainActivity.this.consultarEstadoEstacionamiento();
            } else {
                new Builder(MainActivity.this).setTitle("Ocurrio un error al consultar internet").setMessage("Desafortunadamente no pudimos registrar su telefono automaticamente, verifique que tiene internet, desea reintentar?").setPositiveButton(17039379, new C02162()).setNegativeButton(17039369, new C02151()).setIcon(17301543).show();
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (this.usr != null && this.usr.datosValidos()) {
            consultarEstadoEstacionamiento();
            cargarPuntosDeCarga();
            cargarOcupacion();
        }
    }

    public void onDestroy() {
        try {
            getApplicationContext();
            ((LocationManager) getSystemService("location")).removeUpdates(this.locListener);
        } catch (Exception ex) {
            Log.e("ERROR", "Error en OnDestroy " + ex.getMessage());
        }
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0220R.layout.activity_main);
        if (this.usr != null && this.usr.datosValidos()) {
            consultarEstadoEstacionamiento();
            cargarPuntosDeCarga();
        }
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(C0220R.id.map)).getMapAsync(this);
        this.usr = new UsuarioEstacionamiento(this);
        if (this.usr.datosValidos()) {
            this.PatenteLast = "";
            final LinearLayout tbMultas = (LinearLayout) findViewById(C0220R.id.tblMultas);
            final TextView lblMultasAviso = (TextView) findViewById(C0220R.id.lblMultasAviso);
            ((Button) findViewById(C0220R.id.btnCargarSaldo)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    TextView lblSaldo = (TextView) MainActivity.this.findViewById(C0220R.id.lblSaldoMovypay);
                    TextView lblEstado = (TextView) MainActivity.this.findViewById(C0220R.id.lblEstado);
                    TextView txtPatente = (TextView) MainActivity.this.findViewById(C0220R.id.txtPatente);
                    Intent intent = new Intent(MainActivity.this.getApplicationContext(), MovyPay.class);
                    intent.putExtra("telefono", MainActivity.this.usr.getTelefono());
                    intent.putExtra("saldo", lblSaldo.getText());
                    intent.putExtra("estado", lblEstado.getText());
                    if (tbMultas.getVisibility() != 8) {
                        intent.putExtra("multas", lblMultasAviso.getText());
                    }
                    MainActivity.this.startActivity(intent);
                }
            });
            ((Button) findViewById(C0220R.id.btnPagarMulta)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    TextView lblSaldo = (TextView) MainActivity.this.findViewById(C0220R.id.lblSaldoMovypay);
                    TextView lblEstado = (TextView) MainActivity.this.findViewById(C0220R.id.lblEstado);
                    TextView txtPatente = (TextView) MainActivity.this.findViewById(C0220R.id.txtPatente);
                    Intent intent = new Intent(MainActivity.this.getApplicationContext(), MovyPay.class);
                    intent.putExtra("telefono", MainActivity.this.usr.getTelefono());
                    intent.putExtra("saldo", lblSaldo.getText());
                    intent.putExtra("patente", txtPatente.getText());
                    intent.putExtra("estado", lblEstado.getText());
                    if (tbMultas.getVisibility() != 8) {
                        intent.putExtra("multas", lblMultasAviso.getText());
                    }
                    MainActivity.this.startActivity(intent);
                }
            });
            final TextView txtPatente = (TextView) findViewById(C0220R.id.txtPatente);
            txtPatente.setText(this.PatenteLast.toUpperCase());
            ((ImageView) findViewById(C0220R.id.imgPatente)).setOnClickListener(new C02053());
            ((Button) findViewById(C0220R.id.btnCambiarPatente)).setOnClickListener(new C02064());
            ((TextView) findViewById(C0220R.id.lblEstado)).setText("validando estado parking..");
            ((TextView) findViewById(C0220R.id.lblSaldoMovypay)).setText("");
            ((Button) findViewById(C0220R.id.btnIniciar)).setOnClickListener(new OnClickListener() {

                class C02071 implements DialogInterface.OnClickListener {
                    C02071() {
                    }

                    public void onClick(DialogInterface dialog, int which) {
                    }
                }

                class C02082 implements DialogInterface.OnClickListener {
                    C02082() {
                    }

                    public void onClick(DialogInterface dialog, int which) {
                    }
                }

                class C02093 implements DialogInterface.OnClickListener {
                    C02093() {
                    }

                    public void onClick(DialogInterface dialog, int which) {
                        TextView lblSaldo = (TextView) MainActivity.this.findViewById(C0220R.id.lblSaldoMovypay);
                        TextView lblEstado = (TextView) MainActivity.this.findViewById(C0220R.id.lblEstado);
                        TextView txtPatente = (TextView) MainActivity.this.findViewById(C0220R.id.txtPatente);
                        Intent intent = new Intent(MainActivity.this.getApplicationContext(), MovyPay.class);
                        intent.putExtra("telefono", MainActivity.this.usr.getTelefono());
                        intent.putExtra("saldo", lblSaldo.getText());
                        intent.putExtra("estado", lblEstado.getText());
                        if (tbMultas.getVisibility() != 8) {
                            intent.putExtra("multas", lblMultasAviso.getText());
                        }
                        MainActivity.this.startActivity(intent);
                    }
                }

                class C02104 implements DialogInterface.OnClickListener {
                    C02104() {
                    }

                    public void onClick(DialogInterface dialog, int which) {
                    }
                }

                public void onClick(View view) {
                    if (!MainActivity.this.EstaRegistrado.booleanValue()) {
                        return;
                    }
                    if (!MainActivity.this.EstacionamientoHabilitado.booleanValue()) {
                        new Builder(MainActivity.this).setTitle("Estacionamiento no requerido").setMessage("En este momento no rige estacionamiento medido, muchas gracias.").setPositiveButton(17039370, new C02071()).setIcon(17301659).show();
                    } else if (!MainActivity.this.SaldoSuficiente.booleanValue()) {
                        new Builder(MainActivity.this).setTitle("Saldo insuficiente").setMessage("No tiene saldo para iniciar estacionamiento, desea recargar con tarjeta ahora?\nPresione ACEPTAR, sino puede ver los puntos de carga cercanos haciendo click en el boton").setPositiveButton(17039379, new C02093()).setNegativeButton(17039369, new C02082()).setIcon(17301659).show();
                    } else if (MainActivity.this.EstaEstacionado.booleanValue()) {
                        MainActivity.this.finalizarParking();
                    } else {
                        try {
                            MainActivity.this.iniciarParking(txtPatente.getText().toString());
                            Geocoder geo = new Geocoder(MainActivity.this.getApplicationContext());
                            try {
                                if (MainActivity.this.carlocation != null) {
                                    geo.getFromLocation(MainActivity.this.carlocation.getLatitude(), MainActivity.this.carlocation.getLongitude(), 1);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception ex) {
                            new Builder(MainActivity.this).setTitle("Error al iniciar").setMessage(ex.getMessage()).setPositiveButton(17039379, new C02104()).setIcon(17301659).show();
                        }
                    }
                }
            });
            return;
        }
        Intent intent = new Intent(getApplicationContext(), RegistrarActivity.class);
        intent.putExtra("telefono", this.usr.getTelefono());
        startActivity(intent);
    }

    private void consultarEstadoEstacionamiento() {
        this.pdialog = new ProgressDialog(this);
        this.pdialog.setTitle("Validando estado estacionamiento");
        this.pdialog.setCancelable(false);
        this.pdialog.show();
        new ConsultasWS(this.usr.getTelefono(), new C02916()).execute(new String[]{"getStatusWS"});
    }

    private void registrarTelefono() {
        this.pdialog = new ProgressDialog(this);
        this.pdialog.setTitle("Registrando Telefono automaticamente");
        this.pdialog.show();
        if (!this.usr.datosValidos()) {
            new Builder(this).setTitle("La aplicacion no funciona sin conexion a internet").setMessage("Si no tiene internet puede utilizar el SMS, enviando E al 54351.").setPositiveButton(17039379, new C02189()).setIcon(17301659).show();
        } else if (this.intentoRegistrar) {
            new Builder(this).setTitle("Error").setMessage("Desafortunadamente no pudimos obtener los datos del estacionamiento").setPositiveButton(17039379, new C02178()).setIcon(17301543).show();
        } else {
            this.intentoRegistrar = true;
            new ConsultasWS(this.usr.getTelefono(), new C02927()).execute(new String[]{"RegisterPhone", this.usr.getMedia()});
        }
    }

    private void consultarMultasPendientes() {
        TextView txtPatente = (TextView) findViewById(C0220R.id.txtPatente);
        final LinearLayout tbMultas = (LinearLayout) findViewById(C0220R.id.tblMultas);
        final TextView lblMultasAviso = (TextView) findViewById(C0220R.id.lblMultasAviso);
        new ConsultasWS(this.usr.getTelefono(), new AsyncResponse() {
            public void processFinish(String output) {
                try {
                    if (output != "") {
                        JSONObject json = new JSONObject(output);
                        JSONObject jSONObject;
                        try {
                            if (json.getInt("Status") != 1) {
                                throw new Exception(json.getString("Error"));
                            }
                            if (json.getJSONArray("multas").length() > 0) {
                                lblMultasAviso.setText("Ud. tiene " + json.getJSONArray("multas").length() + " multas pendientes de pago voluntario");
                                tbMultas.setVisibility(0);
                            } else {
                                tbMultas.setVisibility(8);
                            }
                            jSONObject = json;
                            return;
                        } catch (Exception e) {
                            jSONObject = json;
                            tbMultas.setVisibility(8);
                        }
                    }
                    throw new Exception("fallo la conexion con el servidor");
                } catch (Exception e2) {
                    tbMultas.setVisibility(8);
                }
            }
        }).execute(new String[]{"getMultasPendientes", this.PatenteLast});
    }

    private void iniciarParking(final String patente) {
        this.pdialog = new ProgressDialog(this);
        this.pdialog.setTitle("Iniciando estacionamiento...");
        this.pdialog.show();
        new ConsultasWS(this.usr.getTelefono(), new AsyncResponse() {

            class C01951 implements DialogInterface.OnClickListener {
                C01951() {
                }

                public void onClick(DialogInterface dialog, int which) {
                }
            }

            class C01962 implements DialogInterface.OnClickListener {
                C01962() {
                }

                public void onClick(DialogInterface dialog, int which) {
                }
            }

            class C01973 implements DialogInterface.OnClickListener {
                C01973() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    try {
                        MainActivity.this.iniciarParking(patente);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void processFinish(String output) {
                JSONObject jSONObject;
                Exception ex;
                MainActivity.this.pdialog.dismiss();
                if (output != "") {
                    try {
                        JSONObject json = new JSONObject(output);
                        try {
                            if (json.getInt("Status") != 1) {
                                throw new Exception(json.getString("Error"));
                            }
                            String nroControl = json.getString("NroControl");
                            if (nroControl.length() > 1) {
                                MainActivity.this.carlocation = MainActivity.this.getMyLocation();
                                MainActivity.this.ejecutarNotificacionInicioEstacionamiento(nroControl);
                                MainActivity.this.consultarEstadoEstacionamiento();
                            } else {
                                new Builder(MainActivity.this).setTitle("Ocurrio un error al Iniciar Estacionamiento").setMessage("La patente ingresada es invalida, por favor verifique la patente y vuelva a intentarlo").setPositiveButton(17039370, new C01951()).setIcon(17301543).show();
                            }
                            jSONObject = json;
                            return;
                        } catch (Exception e) {
                            ex = e;
                            jSONObject = json;
                            new Builder(MainActivity.this).setTitle("Ocurrio un error al Iniciar Estacionamiento").setMessage(ex.getMessage()).setIcon(17301543).show();
                            return;
                        }
                    } catch (Exception e2) {
                        ex = e2;
                        new Builder(MainActivity.this).setTitle("Ocurrio un error al Iniciar Estacionamiento").setMessage(ex.getMessage()).setIcon(17301543).show();
                        return;
                    }
                }
                new Builder(MainActivity.this).setTitle("Error al iniciar").setMessage("Debe aguardar 5 minutos para iniciar Estacionamiento, presione Aceptar para reintentar.").setPositiveButton(17039379, new C01973()).setNegativeButton(17039369, new C01962()).setIcon(17301543).show();
            }
        }).execute(new String[]{"ParkStart", patente});
    }

    private void finalizarParking() {
        this.pdialog = new ProgressDialog(this);
        this.pdialog.setTitle("Finalizando estacionamiento");
        this.pdialog.show();
        new ConsultasWS(this.usr.getTelefono(), new AsyncResponse() {

            class C01991 implements DialogInterface.OnClickListener {

                class C01981 implements DialogInterface.OnClickListener {
                    C01981() {
                    }

                    public void onClick(DialogInterface dialog, int which) {
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    }
                }

                C01991() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    new Builder(MainActivity.this).setTitle("La aplicacion no funciona sin conexion a internet").setMessage("Si no tiene internet puede utilizar el SMS").setPositiveButton(17039379, new C01981()).setIcon(17301659).show();
                }
            }

            class C02002 implements DialogInterface.OnClickListener {
                C02002() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    try {
                        MainActivity.this.finalizarParking();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void processFinish(String output) {
                JSONObject jSONObject;
                Exception ex;
                MainActivity.this.pdialog.dismiss();
                try {
                    if (output != "") {
                        JSONObject json = new JSONObject(output);
                        try {
                            if (json.getInt("Status") != 1) {
                                throw new Exception(json.getString("Error"));
                            }
                            MainActivity.this.actualizarNotificacion(json.getString("NroControl"));
                            MainActivity.this.consultarEstadoEstacionamiento();
                            jSONObject = json;
                            return;
                        } catch (Exception e) {
                            ex = e;
                            jSONObject = json;
                            new Builder(MainActivity.this).setTitle("Ocurrio un error al consultar internet").setMessage("Desafortunadamente no pudimos Finalizar Estacionamiento, " + ex.getMessage() + ", desea reintentar?").setPositiveButton(17039379, new C02002()).setNegativeButton(17039369, new C01991()).setIcon(17301543).show();
                        }
                    }
                    throw new Exception("fallo la conexion con el servidor");
                } catch (Exception e2) {
                    ex = e2;
                    new Builder(MainActivity.this).setTitle("Ocurrio un error al consultar internet").setMessage("Desafortunadamente no pudimos Finalizar Estacionamiento, " + ex.getMessage() + ", desea reintentar?").setPositiveButton(17039379, new C02002()).setNegativeButton(17039369, new C01991()).setIcon(17301543).show();
                }
            }
        }).execute(new String[]{"ParkStop"});
    }

    private void ejecutarNotificacionInicioEstacionamiento(String nroControl) {
        try {
            Calendar cal = Calendar.getInstance();
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
            nBuilder.setContentTitle("Movypark Escobar - INICIADO");
            nBuilder.setContentText("INICIADO " + cal.get(11) + ":" + cal.get(12) + "- Nro. Ctrl:" + nroControl);
            nBuilder.setTicker("Inicio Estacionamiento");
            nBuilder.setAutoCancel(false);
            nBuilder.setSmallIcon(C0220R.mipmap.ic_launcher);
            nBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), C0220R.mipmap.ic_launcher));
            nBuilder.setSound(RingtoneManager.getDefaultUri(2));
            Intent intent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intent);
            nBuilder.setContentIntent(stackBuilder.getPendingIntent(0, 134217728));
            ((NotificationManager) getSystemService("notification")).notify(NOTIFY_ME_ID, nBuilder.build());
        } catch (Exception e) {
        }
    }

    private void actualizarNotificacion(String nroControl) {
        try {
            Calendar cal = Calendar.getInstance();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle("Movypark Escobar - FINALIZADO");
            mBuilder.setContentText("FINALIZADO " + cal.get(11) + ":" + cal.get(12) + "- Nro. Ctrl:" + nroControl);
            mBuilder.setTicker("Fin de Estacionamiento");
            mBuilder.setAutoCancel(false);
            mBuilder.setSmallIcon(C0220R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), C0220R.mipmap.ic_launcher));
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, 134217728);
            mBuilder.setSound(RingtoneManager.getDefaultUri(2));
            mBuilder.setContentIntent(resultPendingIntent);
            ((NotificationManager) getSystemService("notification")).notify(NOTIFY_ME_ID, mBuilder.build());
        } catch (Exception e) {
        }
    }

    private void cambiarPatente() {
        Builder builder = new Builder(this);
        builder.setTitle("Ingrese su patente por favor");
        final EditText input = new EditText(this);
        input.setInputType(1);
        input.setFilters(new InputFilter[]{new LengthFilter(7)});
        input.setText(this.PatenteLast);
        builder.setView(input);
        final TextView txtPatente = (TextView) findViewById(C0220R.id.txtPatente);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            class C02011 implements DialogInterface.OnClickListener {
                C02011() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    txtPatente.setText(MainActivity.this.PatenteLast.toUpperCase());
                }
            }

            class C02022 implements DialogInterface.OnClickListener {
                C02022() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.cambiarPatente();
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                String patenteIngresada = input.getText().toString().replace(" ", "");
                if (patenteIngresada.length() < 6) {
                    new Builder(MainActivity.this).setTitle("Patente ingresada invalida").setMessage("Por favor, vuelva a ingresar la patente correctamente, solo letras y numeros").setPositiveButton(17039379, new C02022()).setNegativeButton(17039369, new C02011()).setIcon(17301543).show();
                    return;
                }
                ImageView imgPatente = (ImageView) MainActivity.this.findViewById(C0220R.id.imgPatente);
                if (patenteIngresada.length() < 7) {
                    txtPatente.setTextColor(Color.parseColor("#ffffff"));
                    imgPatente.setImageResource(C0220R.drawable.patentevieja);
                } else {
                    txtPatente.setTextColor(Color.parseColor("#000000"));
                    imgPatente.setImageResource(C0220R.drawable.patente);
                }
                MainActivity.this.PatenteLast = patenteIngresada;
                txtPatente.setText(MainActivity.this.PatenteLast.toUpperCase());
                MainActivity.this.consultarMultasPendientes();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMyLocationEnabled(true);
        if (!this.mMap.isMyLocationEnabled()) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            iniciarLocalizacion();
        } else {
            showSettingsAlert();
        }
    }

    private void showSettingsAlert() {
        Builder alertDialog = new Builder(this);
        alertDialog.setTitle("GPS");
        alertDialog.setMessage("El GPS no se encuentra activado, por favor activelo para solucionar el problema");
        alertDialog.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void iniciarLocalizacion() {
        Location location = getMyLocation();
        if (location != null) {
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0f));
        } else {
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-31.416317044702833d, -64.19598788022995d), 15.0f));
        }
        actualizarUbicacionMapa();
    }

    private void actualizarUbicacionMapa() {
        getApplicationContext();
        LocationManager locManager = (LocationManager) getSystemService("location");
        this.locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location != null) {
                    MainActivity.this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0f));
                    String direccion = "";
                    try {
                        List<Address> ubicacion = new Geocoder(MainActivity.this.getApplicationContext()).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (ubicacion.size() > 0) {
                            direccion = "Ud. se encuentra en " + ((Address) ubicacion.get(0)).getThoroughfare() + " " + ((Address) ubicacion.get(0)).getFeatureName();
                        }
                    } catch (Exception e) {
                        String str = "Error al obtener direccion";
                    }
                }
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        locManager.requestLocationUpdates("gps", 0, 1.0f, this.locListener);
        locManager.requestLocationUpdates("network", 0, 1.0f, this.locListener);
    }

    private LatLng midPoint(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        return new LatLng(Math.toDegrees(Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt(((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx)) + (By * By)))), Math.toDegrees(lon1 + Math.atan2(By, Math.cos(lat1) + Bx)));
    }

    private Location getMyLocation() {
        getApplicationContext();
        LocationManager lm = (LocationManager) getSystemService("location");
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return null;
        }
        Location myLocation = null;
        for (String proveedor : lm.getAllProviders()) {
            myLocation = lm.getLastKnownLocation(proveedor);
            if (myLocation != null) {
                return myLocation;
            }
        }
        return myLocation;
    }

    private void cargarPuntosDeCarga() {
        new ConsultasWS(this.usr.getTelefono(), new AsyncResponse() {
            public void processFinish(String output) {
                try {
                    JSONArray arrPuntosVenta = new JSONArray(output.trim());
                    for (int i = 0; i < arrPuntosVenta.length(); i++) {
                        JSONObject puntoVenta = arrPuntosVenta.getJSONObject(i);
                        try {
                            double latitud = Double.parseDouble(puntoVenta.getString("latitud"));
                            double longitud = Double.parseDouble(puntoVenta.getString("longitud"));
                            MainActivity.this.mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(puntoVenta.getString("descripcion")));
                        } catch (Exception e) {
                        }
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }).execute(new String[]{"GetPuntosVenta"});
    }

    private void cargarOcupacion() {
        final List<Polyline> polylines = new ArrayList();
        try {
            new ConsultasWS(this.usr.getTelefono(), new AsyncResponse() {
                public void processFinish(String output) {
                    String json = output.trim();
                    try {
                        for (Polyline line : polylines) {
                            line.remove();
                        }
                        polylines.clear();
                    } catch (Exception e) {
                    }
                    try {
                        JSONArray arrStatusInicial = new JSONObject(json).getJSONObject("extra").getJSONArray("baldes");
                        for (int i = 0; i < arrStatusInicial.length(); i++) {
                            JSONObject cuadra = arrStatusInicial.getJSONObject(i);
                            try {
                                float disponibilidad;
                                int color;
                                JSONObject initialStatus = cuadra.getJSONObject("initialStatus");
                                int cantidadInicial = initialStatus.getInt("places");
                                double latInicial = Double.parseDouble(initialStatus.getString("startLatitude"));
                                double lonInicial = Double.parseDouble(initialStatus.getString("startLongitude"));
                                double latFinal = Double.parseDouble(initialStatus.getString("endLatitude"));
                                double lonFinal = Double.parseDouble(initialStatus.getString("endLongitude"));
                                try {
                                    disponibilidad = (((float) (cantidadInicial - cuadra.getJSONObject("currentStatus").getInt("emptyPlaces"))) / ((float) cantidadInicial)) * 100.0f;
                                } catch (JSONException e2) {
                                    disponibilidad = 0.0f;
                                }
                                LatLng latLng = new LatLng(latInicial, lonInicial);
                                latLng = new LatLng(latFinal, lonFinal);
                                if (cantidadInicial == 0) {
                                    disponibilidad = 100.0f;
                                }
                                if (disponibilidad < 50.0f) {
                                    color = -16711936;
                                } else if (disponibilidad < 51.0f || disponibilidad > 90.0f) {
                                    color = SupportMenu.CATEGORY_MASK;
                                } else {
                                    color = InputDeviceCompat.SOURCE_ANY;
                                }
                                polylines.add(MainActivity.this.mMap.addPolyline(new PolylineOptions().add(latLng, latLng).width(25.0f).color(color)));
                            } catch (Exception e3) {
                            }
                        }
                    } catch (JSONException e4) {
                    }
                }
            }).execute(new String[]{"GetOcupacion"});
        } catch (Exception e) {
        }
    }
}
