package com.movypark.cordoba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();
    private Context context = null;
    private Runnable sendUpdatesToUI = new C02291();
    private String textoSms = "";

    class C02291 implements Runnable {
        C02291() {
        }

        public void run() {
            UsuarioEstacionamiento usr = new UsuarioEstacionamiento(SmsReceiver.this.context);
            if (!(usr.datosValidos() && usr.getEstaRegistrado().booleanValue()) && SmsReceiver.this.textoSms.contains(" PIN ")) {
                try {
                    String telefono = "";
                    String idmedia = "";
                    String telEnc = SmsReceiver.this.textoSms.substring(SmsReceiver.this.textoSms.indexOf(":") + 2).substring(0, 10);
                    telefono = SmsReceiver.this.getPhoneFromPin(telEnc);
                    idmedia = SmsReceiver.this.textoSms.substring(SmsReceiver.this.textoSms.indexOf(":") + 2).replace(telEnc, "");
                    if (telefono.trim().length() != 10) {
                        throw new Exception("El telefono es incorrecto " + telefono + " el idmedia es " + idmedia);
                    }
                    UsuarioEstacionamiento usuarioEstacionamiento = new UsuarioEstacionamiento(telefono, idmedia, SmsReceiver.this.context);
                    Intent mServiceIntent = new Intent(SmsReceiver.this.context, MainActivity.class);
                    mServiceIntent.setFlags(268435456);
                    SmsReceiver.this.context.startActivity(mServiceIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onReceive(Context contexto, Intent intent) {
        Bundle bundle = intent.getExtras();
        this.context = contexto;
        String str = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (msgs[i].getOriginatingAddress().contains("54351")) {
                    this.textoSms = msgs[i].getMessageBody().toString();
                    new Handler().postDelayed(this.sendUpdatesToUI, 10);
                    return;
                }
            }
            Log.d(this.TAG, str);
        }
    }

    private String getPhoneFromPin(String telefonoEnc) {
        return telefonoEnc.replace("A", "0").replace("B", "1").replace("C", "2").replace("D", "3").replace("E", "4").replace("F", "5").replace("G", "6").replace("H", "7").replace("I", "8").replace("J", "9");
    }
}
