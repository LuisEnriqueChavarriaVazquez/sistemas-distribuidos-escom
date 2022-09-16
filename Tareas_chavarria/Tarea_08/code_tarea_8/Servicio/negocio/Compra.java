
package negocio;

import com.google.gson.*;

public class Compra
{
  int identificador;
  String nombre;
  String descripcion;
  float precio;
  int cantidad;
  float costo;
  byte[] foto;


  public static Compra valueOf(String s) throws Exception
  {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
    return (Compra)j.fromJson(s,Compra.class);
  }
}
