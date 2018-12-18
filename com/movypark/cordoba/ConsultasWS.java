package com.movypark.cordoba;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConsultasWS extends AsyncTask<String, Integer, String> {
    private static String CONS_URLCLOUD = "https://api.movypark.com/apiparking.aspx?idCliente=4&";
    private static final String TAG = "ConsultasWS";
    private static String telefono = "";
    public AsyncResponse delegate = null;

    static class C01941 implements X509TrustManager {
        C01941() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new C01941()};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ConsultasWS(String telefono, AsyncResponse asyncResponse) {
        telefono = telefono;
        this.delegate = asyncResponse;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.String doInBackground(java.lang.String... r6) {
        /*
        r5 = this;
        r0 = 0;
        r2 = 1;
        disableSSLCertificateChecking();
        r1 = "ConsultasWS";
        r3 = "doInBackground";
        android.util.Log.i(r1, r3);
        r1 = r6[r0];
        r3 = r1.toString();
        r1 = -1;
        r4 = r3.hashCode();
        switch(r4) {
            case -1252377274: goto L_0x0066;
            case -909453133: goto L_0x003e;
            case -898374716: goto L_0x0021;
            case -605160341: goto L_0x0052;
            case -399999801: goto L_0x005c;
            case -73829768: goto L_0x002a;
            case 1244544812: goto L_0x0034;
            case 1893112833: goto L_0x0048;
            default: goto L_0x001a;
        };
    L_0x001a:
        r0 = r1;
    L_0x001b:
        switch(r0) {
            case 0: goto L_0x0070;
            case 1: goto L_0x0075;
            case 2: goto L_0x0080;
            case 3: goto L_0x0085;
            case 4: goto L_0x0090;
            case 5: goto L_0x0095;
            case 6: goto L_0x00a0;
            case 7: goto L_0x00a6;
            default: goto L_0x001e;
        };
    L_0x001e:
        r0 = "";
    L_0x0020:
        return r0;
    L_0x0021:
        r4 = "getStatusWS";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x001a;
    L_0x0029:
        goto L_0x001b;
    L_0x002a:
        r0 = "ParkStart";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x0032:
        r0 = r2;
        goto L_0x001b;
    L_0x0034:
        r0 = "ParkStop";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x003c:
        r0 = 2;
        goto L_0x001b;
    L_0x003e:
        r0 = "getMultasPendientes";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x0046:
        r0 = 3;
        goto L_0x001b;
    L_0x0048:
        r0 = "GetOcupacion";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x0050:
        r0 = 4;
        goto L_0x001b;
    L_0x0052:
        r0 = "RegisterPhone";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x005a:
        r0 = 5;
        goto L_0x001b;
    L_0x005c:
        r0 = "GetPuntosVenta";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x0064:
        r0 = 6;
        goto L_0x001b;
    L_0x0066:
        r0 = "EnviarIDOneSignal";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x001a;
    L_0x006e:
        r0 = 7;
        goto L_0x001b;
    L_0x0070:
        r0 = r5.getStatusWS();
        goto L_0x0020;
    L_0x0075:
        r0 = r6[r2];
        r0 = r0.toString();
        r0 = r5.ParkStart(r0);
        goto L_0x0020;
    L_0x0080:
        r0 = r5.ParkStop();
        goto L_0x0020;
    L_0x0085:
        r0 = r6[r2];
        r0 = r0.toString();
        r0 = r5.GetMultasPendientes(r0);
        goto L_0x0020;
    L_0x0090:
        r0 = r5.GetOcupacion();
        goto L_0x0020;
    L_0x0095:
        r0 = r6[r2];
        r0 = r0.toString();
        r0 = r5.RegisterPhone(r0);
        goto L_0x0020;
    L_0x00a0:
        r0 = r5.GetPuntosVenta();
        goto L_0x0020;
    L_0x00a6:
        r0 = r6[r2];
        r0 = r0.toString();
        r0 = r5.EnviarIdOneSignal(r0);
        goto L_0x0020;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.movypark.cordoba.ConsultasWS.doInBackground(java.lang.String[]):java.lang.String");
    }

    private String EnviarIdOneSignal(String idUsuario) {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=ADDNEWONESIGNALID&idusuario=" + idUsuario + "&telefono=" + telefono).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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

    private String GetMultasPendientes(String matricula) {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=GETMULTASPENDIENTES&patente=" + matricula + "&telefono=" + telefono).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String GetPuntosVenta() {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=GETPUNTOSVENTA&telefono=" + telefono).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String RegisterPhone(String idmedia) {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=SendSmsRegistracion&telefono=" + telefono + "&idmedia=" + idmedia).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String ParkStop() {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=PARKSTOP&telefono=" + telefono).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String GetOcupacion() {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=GETOCUPACION&agente=8&telefono=" + telefono).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String ParkStart(String patente) {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=PARKSTART&telefono=" + telefono + "&patente=" + patente).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String getStatusWS() {
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(CONS_URLCLOUD + "accion=GETSTATUSWS&telefono=" + telefono).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            String convertInputStreamToString = convertInputStreamToString(is);
            if (is == null) {
                return convertInputStreamToString;
            }
            try {
                is.close();
                return convertInputStreamToString;
            } catch (Exception e) {
            }
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
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                result = result + line;
            }
            if (inputStream == null) {
                return result;
            }
            inputStream.close();
            return result;
        } catch (Exception e) {
            return "";
        }
    }
}
