package com.movypark.cordoba;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GatewayWS extends AsyncTask<String, Integer, String> {
    private static final String TAG = "GatewayWS";
    public AsyncResponse delegate = null;

    GatewayWS(AsyncResponse asyncResponse) {
        this.delegate = asyncResponse;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.String doInBackground(java.lang.String... r5) {
        /*
        r4 = this;
        r0 = 0;
        r1 = "GatewayWS";
        r2 = "doInBackground";
        android.util.Log.i(r1, r2);
        r1 = r5[r0];
        r2 = r1.toString();
        r1 = -1;
        r3 = r2.hashCode();
        switch(r3) {
            case -1274328088: goto L_0x001d;
            default: goto L_0x0016;
        };
    L_0x0016:
        r0 = r1;
    L_0x0017:
        switch(r0) {
            case 0: goto L_0x0026;
            default: goto L_0x001a;
        };
    L_0x001a:
        r0 = "";
    L_0x001c:
        return r0;
    L_0x001d:
        r3 = "EnviarPIN";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0016;
    L_0x0025:
        goto L_0x0017;
    L_0x0026:
        r0 = 1;
        r0 = r5[r0];
        r0 = r0.toString();
        r0 = r4.enviarPin(r0);
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.movypark.cordoba.GatewayWS.doInBackground(java.lang.String[]):java.lang.String");
    }

    protected void onProgressUpdate(Integer... params) {
    }

    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute");
    }

    protected void onPostExecute(String result) {
        Log.i(TAG, "onPostExecute");
        this.delegate.processFinish(result);
    }

    private String enviarPin(String telefonocarrier) {
        InputStream is = null;
        try {
            String telefono = telefonocarrier.substring(0, telefonocarrier.lastIndexOf("|"));
            String idmedia = telefonocarrier.substring(telefonocarrier.lastIndexOf("|") + 1, telefonocarrier.length());
            String pin = "";
            for (char letra : telefono.toCharArray()) {
                pin = pin + getCharForNumber(Integer.parseInt(String.valueOf(letra)));
            }
            pin = pin + idmedia;
            try {
                if (telefono.trim().length() == 10 && Integer.parseInt(idmedia) == 96) {
                    telefono = "54" + telefono;
                }
            } catch (Exception e) {
            }
            try {
                String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n  <soap:Body>\n    <SubmitSMSReplyResult xmlns=\"http://www.unlp.edu.ar/\">\n      <providerID>pm</providerID>\n      <gatewayRefID>plusmo</gatewayRefID>\n      <DN>" + telefono + "</DN>\n      <carrier>" + idmedia + "</carrier>\n      <shortCode>54351</shortCode>\n      <SMSText>SU PIN A INGRESAR ES: " + pin + "</SMSText>\n    </SubmitSMSReplyResult>\n  </soap:Body>\n</soap:Envelope>";
                HttpURLConnection conn = (HttpURLConnection) new URL("http://200.80.209.251/gwunlpCordoba/Service.asmx").openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("SOAPAction", "\"http://www.unlp.edu.ar/SubmitSMSReplyResult\"");
                conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                conn.setDoInput(true);
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                out.write(soap.getBytes());
                out.flush();
                out.close();
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();
                String convertInputStreamToString = convertInputStreamToString(is, 500);
                if (is == null) {
                    return convertInputStreamToString;
                }
                is.close();
                return convertInputStreamToString;
            } catch (Exception ex) {
                Log.e("ErrorURL", ex.getMessage());
                if (is != null) {
                    is.close();
                }
                return "";
            } catch (Throwable th) {
                if (is != null) {
                    is.close();
                }
            }
        } catch (Exception e2) {
        }
    }

    private String getCharForNumber(int i) {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        if (i > 25) {
            return null;
        }
        return Character.toString(alphabet[i]);
    }

    private String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        try {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            try {
                char[] buffer = new char[length];
                reader.read(buffer);
                return new String(buffer);
            } catch (Exception e) {
                Reader reader2 = reader;
            }
        } catch (Exception e2) {
            return "";
        }
    }
}
