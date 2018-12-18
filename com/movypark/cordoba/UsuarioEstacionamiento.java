package com.movypark.cordoba;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UsuarioEstacionamiento {
    private Context contexto = null;
    private Boolean envioAPI = Boolean.valueOf(false);
    private Boolean estaRegistrado = Boolean.valueOf(false);
    String filename = "usuario";
    private String idmedia = "";
    private String telefono = "";

    public UsuarioEstacionamiento(String tel, String media, Context cont) throws IOException {
        this.contexto = cont;
        this.telefono = tel;
        this.idmedia = media;
        GrabarUsuario();
    }

    public UsuarioEstacionamiento(Context cont) {
        this.contexto = cont;
        obtenerArchivo();
    }

    private void GrabarUsuario() throws IOException {
        try {
            if (datosValidos()) {
                String string = this.telefono + "|" + this.idmedia;
                FileOutputStream outputStream = this.contexto.openFileOutput(this.filename, 0);
                outputStream.write(string.getBytes());
                outputStream.close();
                return;
            }
            this.contexto.openFileOutput(this.filename, 0).close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean datosValidos() {
        try {
            this.telefono = this.telefono.trim().substring(this.telefono.trim().length() - 10);
            if (this.telefono.trim().length() != 10 || this.idmedia.toString().length() < 2) {
                return false;
            }
            long tel = Long.parseLong(this.telefono);
            Integer idMedia = Integer.valueOf(Integer.parseInt(this.idmedia));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void obtenerArchivo() {
        String ret = "";
        try {
            InputStream inputStream = this.contexto.openFileInput(this.filename);
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    receiveString = bufferedReader.readLine();
                    if (receiveString == null) {
                        break;
                    }
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
            this.telefono = ret.substring(0, ret.indexOf("|"));
            this.idmedia = ret.substring(ret.indexOf("|") + 1);
            this.estaRegistrado = Boolean.valueOf(true);
            if (!datosValidos()) {
                this.telefono = "";
                this.idmedia = "";
                this.estaRegistrado = Boolean.valueOf(false);
                GrabarUsuario();
            }
        } catch (FileNotFoundException e) {
            this.estaRegistrado = Boolean.valueOf(false);
        } catch (IOException e2) {
            this.estaRegistrado = Boolean.valueOf(false);
        } catch (Exception e3) {
            this.estaRegistrado = Boolean.valueOf(false);
        }
    }

    public void setEstaRegistrado(Boolean esta) {
        this.estaRegistrado = esta;
    }

    public Boolean getEstaRegistrado() {
        return this.estaRegistrado;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getMedia() {
        return this.idmedia;
    }

    public String getCarrier() {
        String str = this.idmedia;
        Object obj = -1;
        switch (str.hashCode()) {
            case 1572:
                if (str.equals("15")) {
                    obj = 3;
                    break;
                }
                break;
            case 1669:
                if (str.equals("49")) {
                    obj = 2;
                    break;
                }
                break;
            case 1821:
                if (str.equals("96")) {
                    obj = 1;
                    break;
                }
                break;
            case 48718:
                if (str.equals("130")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";
            case 3:
                return "4";
            default:
                return "";
        }
    }
}
