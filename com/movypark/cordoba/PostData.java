package com.movypark.cordoba;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class PostData extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    String resultado = "";

    private java.lang.String downloadContent(java.lang.String r10) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0035 in list [B:5:0x0032]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r9 = this;
        r3 = 0;
        r4 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r6 = new java.net.URL;	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r6.<init>(r10);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0 = r6.openConnection();	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r7 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0.setReadTimeout(r7);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r7 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0.setConnectTimeout(r7);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r7 = "POST";	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0.setRequestMethod(r7);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r7 = 1;	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0.setDoInput(r7);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r0.connect();	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r5 = r0.getResponseCode();	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r3 = r0.getInputStream();	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r1 = r9.convertInputStreamToString(r3, r4);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        if (r3 == 0) goto L_0x0035;
    L_0x0032:
        r3.close();
    L_0x0035:
        return r1;
    L_0x0036:
        r2 = move-exception;
        r7 = "ErrorURL";	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        r8 = r2.getMessage();	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        android.util.Log.e(r7, r8);	 Catch:{ Exception -> 0x0036, all -> 0x0048 }
        if (r3 == 0) goto L_0x0045;
    L_0x0042:
        r3.close();
    L_0x0045:
        r1 = "";
        goto L_0x0035;
    L_0x0048:
        r7 = move-exception;
        if (r3 == 0) goto L_0x004e;
    L_0x004b:
        r3.close();
    L_0x004e:
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.movypark.cordoba.PostData.downloadContent(java.lang.String):java.lang.String");
    }

    public String getresultado() {
        return this.resultado;
    }

    protected String doInBackground(String... params) {
        try {
            return downloadContent(params[0]);
        } catch (IOException e) {
            return "ERROR";
        } catch (Exception e2) {
            return "ERROR";
        }
    }

    protected void onPostExecute(String result) {
        if (this.delegate != null) {
            this.delegate.processFinish(result);
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
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
