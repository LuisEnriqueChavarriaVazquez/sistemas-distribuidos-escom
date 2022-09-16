/*
************************************************
*
* Datos de la práctica.
*
* Materia: Desarrollo de Sistemas distribuidos
* Profesor: Carlos Pineda Guerrero
* Alumnos:
*           Chavarría Vázquez Luis Enrique.
*
* Grupo: 4CV11
* Practica 07.
* 
*************************************************
*/

package negocio;

import com.google.gson.*;

public class Usuario
{
  int id_usuario;
  String email;
  String nombre;
  String apellido_paterno;
  String apellido_materno;
  String fecha_nacimiento;
  String telefono;
  String genero;
  byte[] foto;

  public static Usuario valueOf(String s) throws Exception
  {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
    return (Usuario)j.fromJson(s,Usuario.class);
  }
}
