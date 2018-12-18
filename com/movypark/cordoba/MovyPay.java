package com.movypark.cordoba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MovyPay extends AppCompatActivity {
    ProgressBar bar = null;
    String estado = "";
    WebView mWebView = null;
    String multas = "";
    ProgressDialog pDialog = null;
    String patente = "";
    String saldo = "";
    String telefono = "";

    class C02191 extends WebViewClient {
        C02191() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MovyPay.this.pDialog.setMessage("Cargando...");
            MovyPay.this.pDialog.show();
            return false;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            MovyPay.this.pDialog.dismiss();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public void onPageFinished(WebView view, String url) {
            MovyPay.this.pDialog.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0220R.layout.activity_movypay);
        Intent intent = getIntent();
        this.telefono = intent.getStringExtra("telefono");
        this.patente = intent.getStringExtra("patente");
        this.saldo = intent.getStringExtra("saldo");
        this.estado = intent.getStringExtra("estado");
        this.multas = intent.getStringExtra("multas");
        LinearLayout tbMultas = (LinearLayout) findViewById(C0220R.id.tblMultas);
        LinearLayout tbEstado = (LinearLayout) findViewById(C0220R.id.tbEstado);
        TextView lblMultasAviso = (TextView) findViewById(C0220R.id.lblMultasAviso);
        TextView lblSaldo = (TextView) findViewById(C0220R.id.lblSaldoMovypay);
        TextView lblEstado = (TextView) findViewById(C0220R.id.lblEstado);
        if (this.estado.contains("INSUFICIENTE")) {
            lblEstado.setTextColor(Color.parseColor("#c7b61d"));
            lblSaldo.setTextColor(Color.parseColor("#c7b61d"));
        } else {
            lblEstado.setTextColor(Color.parseColor("#1a8861"));
            lblSaldo.setTextColor(Color.parseColor("#1a8861"));
        }
        lblSaldo.setText(this.saldo);
        lblEstado.setText(this.estado);
        if (this.multas == null || this.multas == "") {
            tbMultas.setVisibility(8);
        } else {
            tbMultas.setVisibility(0);
            lblMultasAviso.setText(this.multas);
        }
        iniciarNavegacion();
    }

    private void iniciarNavegacion() {
        this.pDialog = new ProgressDialog(this);
        this.pDialog.setTitle("Aguarde un momento");
        this.pDialog.setMessage("");
        this.pDialog.show();
        this.mWebView = (WebView) findViewById(C0220R.id.webView);
        WebSettings webSettings = this.mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(-1);
        this.mWebView.setWebViewClient(new WebViewClient());
        UsuarioEstacionamiento usr = new UsuarioEstacionamiento(this);
        if (this.patente == null || this.patente.toString().length() < 6) {
            this.mWebView.loadUrl("http://www.movypark.com/cordoba/home?celular=" + usr.getTelefono() + "&operador=" + usr.getCarrier() + "&app=1");
        } else {
            this.mWebView.loadUrl("http://www.movypark.com/cordoba/multas.aspx?patente=" + this.patente + "&celular=" + usr.getTelefono() + "&operador=" + usr.getCarrier() + "&app=1");
        }
        this.mWebView.setWebViewClient(new C02191());
    }
}
