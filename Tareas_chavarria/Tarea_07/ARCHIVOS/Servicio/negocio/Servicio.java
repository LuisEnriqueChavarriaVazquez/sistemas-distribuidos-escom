/*
************************************************
*
* Datos de la práctica.
*
* Materia: Desarrollo de Sistemas distribuidos
* Profesor: Carlos Pineda Guerrero
* Alumno:
*           Chavarría Vázquez Luis Enrique.
*
* Grupo: 4CV11
* Practica 07
* 
************************************************
*/


//Paquete de negocio
package negocio;

/*
  Importamos los elementos necesarios para nuestro servicio web.
*/

//Requerimos las siguientes librerias para poder consumir el servicio web.
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

//Para nuestra base de datos, requerimos el uso de las bibliotecas base de sql.
import java.sql.*;
import javax.sql.DataSource;

//Del mismo modo se requiere para poder consumir las consultas base a nuestro servicio.
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

//Para el manejo de arrays y gson, esto para el manejo de los datos, cuando hagamos consultas o cambios.
import java.util.ArrayList;
import com.google.gson.*;
import javax.naming.Context;
import javax.naming.InitialContext;

@Path("ws")
public class Servicio
{
  static DataSource pool = null;
  static
  {		
    try
    {
      Context ctx = new InitialContext();
      pool = (DataSource)ctx.lookup("java:comp/env/jdbc/datasource_Servicio");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  static Gson j = new GsonBuilder()
		.registerTypeAdapter(byte[].class,new AdaptadorGsonBase64())
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
		.create();

  @POST
  //Para la alta de los usuarios, el nombre esta de acuerdo a lo que se indica en moodle
  @Path("alta_usuario")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response alta(@FormParam("usuario") Usuario usuario) throws Exception
  {
    //Uso del pool de conexiones.
    Connection conexion = pool.getConnection();

    //Validaciones de los datos que nosotros requerimos en nuestro form.
    //Para el mail
    if (usuario.email == null || usuario.email.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Ingrese el email por favor."))).build();
    //Para el nombre del usuario
    if (usuario.nombre == null || usuario.nombre.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Ingrese el nombre por favor."))).build();
    //Para el apellido paterno del usuario
    if (usuario.apellido_paterno == null || usuario.apellido_paterno.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Ingrese el apellido paterno por favor."))).build();
    //Para la fecha de nacimiento del usuario
    if (usuario.fecha_nacimiento == null || usuario.fecha_nacimiento.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Ingrese la fecha de nacimiento por favor."))).build();

    try
    {
      //Hacemos la consulta en caso de que el email coincida.
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT id_usuario FROM usuarios WHERE email=?");
      try
      {
        stmt_1.setString(1,usuario.email);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          //En caso de que nuestro email ya este registrado
          if (rs.next())
             return Response.status(400).entity(j.toJson(new Error("El email ya esta registrado."))).build();
        }
        //Cuando el proceso termina lo cerramos.
        finally
        {
          rs.close();
        }
      }
      //Cuando lo terminamos lo cerramos también.
      finally
      {
        stmt_1.close();
      }

      /*
        Cuando tomamos los datos podemos insertarlos dentro de la base de dato, debe quedar claro que
        el id debe ir increchendo conforme agreguemos nuevos usuarios.
      */
      PreparedStatement stmt_2 = conexion.prepareStatement("INSERT INTO usuarios VALUES (0,?,?,?,?,?,?,?)");
      try
      {
        //Seteamos los datos que nosotros ingresamos
        stmt_2.setString(1,usuario.email);
        stmt_2.setString(2,usuario.nombre);
        stmt_2.setString(3,usuario.apellido_paterno);
        stmt_2.setString(4,usuario.apellido_materno);
        stmt_2.setString(5,usuario.fecha_nacimiento);
        stmt_2.setString(6,usuario.telefono);
        stmt_2.setString(7,usuario.genero);
        stmt_2.executeUpdate();
      }
      finally
      {
        //Cerramos
        stmt_2.close();
      }

      //En caso de que nuestra do 
      if (usuario.foto != null)
      {
        PreparedStatement stmt_3 = conexion.prepareStatement("INSERT INTO fotos_usuarios VALUES (0,?,?)");
        try
        {
          stmt_3.setBytes(1,usuario.foto);
          stmt_3.setString(2,String.valueOf(usuario.id_usuario));
          stmt_3.executeUpdate();
        }
        finally
        {
          stmt_3.close();
        }
      }

      
      PreparedStatement stmt_4 = conexion.prepareStatement("SELECT LAST_INSERT_ID()");
      try
      {

        ResultSet rs = stmt_4.executeQuery();
        try
        {
          if (rs.next())
          {
            int id_usuario = rs.getInt(1);
            return Response.ok().entity(j.toJson(id_usuario)).build();
          }
          return Response.status(400).entity(j.toJson(new Error("El email no esta registrado"))).build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_4.close();
      }
    
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.close();
    }
  }

  @POST
  @Path("consulta_usuario")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consulta(@FormParam("id_usuario") int id_usuario) throws Exception
  {
    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT a.id_usuario,a.email,a.nombre,a.apellido_paterno,a.apellido_materno,a.fecha_nacimiento,a.telefono,a.genero,b.foto FROM usuarios a LEFT OUTER JOIN fotos_usuarios b ON a.id_usuario=b.id_usuario WHERE a.id_usuario=?");
      try
      {
        stmt_1.setInt(1,id_usuario);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          if (rs.next())
          {
            Usuario r = new Usuario();
            r.id_usuario = rs.getInt(1);
            r.email = rs.getString(2);
            r.nombre = rs.getString(3);
            r.apellido_paterno = rs.getString(4);
            r.apellido_materno = rs.getString(5);
            r.fecha_nacimiento = rs.getString(6);
            r.telefono = rs.getString(7);
            r.genero = rs.getString(8);
	          r.foto = rs.getBytes(9);
            return Response.ok().entity(j.toJson(r)).build();
          }
          return Response.status(400).entity(j.toJson(new Error("El ID no existe en la base de datos"))).build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.close();
    }
  }

  @POST
  @Path("modifica_usuario")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response modifica(@FormParam("usuario") Usuario usuario) throws Exception
  {
    Connection conexion= pool.getConnection();

    if (usuario.email == null || usuario.email.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Por favor ingresar el email"))).build();

    if (usuario.nombre == null || usuario.nombre.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Por favor ingresar el nombre"))).build();

    if (usuario.apellido_paterno == null || usuario.apellido_paterno.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Por favor ingresar el apellido paterno"))).build();

    if (usuario.fecha_nacimiento == null || usuario.fecha_nacimiento.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Por favor ingresar la fecha de nacimiento"))).build();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("UPDATE usuarios SET email=?,nombre=?,apellido_paterno=?,apellido_materno=?,fecha_nacimiento=?,telefono=?,genero=? WHERE id_usuario=?");
      try
      {
        stmt_1.setString(1,usuario.email);
        stmt_1.setString(2,usuario.nombre);
        stmt_1.setString(3,usuario.apellido_paterno);
        stmt_1.setString(4,usuario.apellido_materno);
        stmt_1.setString(5,usuario.fecha_nacimiento);
        stmt_1.setString(6,usuario.telefono);
        stmt_1.setString(7,usuario.genero);
        stmt_1.setInt(8,usuario.id_usuario);
        stmt_1.executeUpdate();
      }
      finally
      {
        stmt_1.close();
      }

      if (usuario.foto != null)
      {
        PreparedStatement stmt_2 = conexion.prepareStatement("DELETE FROM fotos_usuarios WHERE id_usuario=?");
        try
        {
          stmt_2.setInt(1,usuario.id_usuario);
          stmt_2.executeUpdate();
        }
        finally
        {
          stmt_2.close();
        }

        PreparedStatement stmt_3 = conexion.prepareStatement("INSERT INTO fotos_usuarios VALUES (0,?,?))");
        try
        {
          stmt_3.setBytes(1,usuario.foto);
          stmt_3.setInt(2,usuario.id_usuario);
          stmt_3.executeUpdate();
        }
        finally
        {
          stmt_3.close();
        }
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.close();
    }
    return Response.ok().build();
  }

  @POST
  @Path("borra_usuario")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response borra(@FormParam("id_usuario") int id_usuario) throws Exception
  {
    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT 1 FROM usuarios WHERE id_usuario=?");
      try
      {
        stmt_1.setInt(1,id_usuario);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          if (!rs.next())
		return Response.status(400).entity(j.toJson(new Error("El ID no existe"))).build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }
      PreparedStatement stmt_2 = conexion.prepareStatement("DELETE FROM fotos_usuarios WHERE id_usuario=?");
      try
      {
        stmt_2.setInt(1,id_usuario);
	stmt_2.executeUpdate();
      }
      finally
      {
        stmt_2.close();
      }

      PreparedStatement stmt_3 = conexion.prepareStatement("DELETE FROM usuarios WHERE id_usuario=?");
      try
      {
        stmt_3.setInt(1,id_usuario);
	stmt_3.executeUpdate();
      }
      finally
      {
        stmt_3.close();
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.close();
    }
    return Response.ok().build();
  }

}

