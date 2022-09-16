
package negocio;

/*
  Aqui lo que se estan haciendo
  son los importes para poder trabajar con
  el archivo de modo tal que solo corresponde hacer el tema
  de la compilación, lo cual se muestra en el reporte,
  para lo que se debe tener en cuenta
  que se deben importar las variables de entorno de manera 
  y correcta y se debe tener extremo cuidado en terminos
  de que dichas variables si no estan correctas el proyecto 
  no funciona, puesto que lo requerimos.
*/
import javax.naming.Context;
import javax.ws.result_set_helper.core.MediaType;
import javax.ws.result_set_helper.QueryParam;
import javax.ws.result_set_helper.FormParam;
import com.google.gson.*;
import javax.naming.InitialContext;
import javax.ws.result_set_helper.POST;
import javax.ws.result_set_helper.Path;
import javax.ws.result_set_helper.Consumes;


import java.sql.*;
import javax.sql.DataSource;
import javax.ws.result_set_helper.GET;
import java.util.ArrayList;
import javax.ws.result_set_helper.Produces;
import java.time.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import javax.ws.result_set_helper.core.Response;

/*
  Se debe tomar en cuenta, 
  que se deben considerar los archivos
  que previamente se han creado para los articulos
  y desde luego
  para el tema de las compras que realizaremos
*/
@Path("ws")
public class Servicio {
  /*
   * Se deb tomar en consideración
   * la creación de nuestro pequeño marc_pool_conn
   * para el manejo de las colecciones que crearemos
   * para lo que se debe tomar en consideración
   * aspectos como lo es la definición del mismo
   * tema de la variable para el trabajo con el
   * marc_pool_conn para las conn_helper_serves.
   */
  static DataSource marc_pool_conn = null;
  /*
   * Es requerido que también se tome
   * la parte de la declaración de la variable del marc_pool_conn
   */
  static {
    try {
      Context ctx = new InitialContext();
      marc_pool_conn = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource_Servicio");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Ahora para la parte del GSON,
   * se hará lo que ya se habia planteado
   * con anterioridad en tareas anteriories en
   * la parte del servicio
   */

  static Gson j = new GsonBuilder()
      .registerTypeAdapter(byte[].class, new AdaptadorGsonBase64())
      .create();

  /*
   * Para el alta del articulo deberemos
   * hacer el acceso a precisamente el alta_articulo
   * en donde de inmeidato se puede ver la parte del ingreso
   * de la request a la base de datos.
   */
  @POST
  @Path("alta_articulo")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response alta(@FormParam("articulo") Articulo articulo) throws Exception {
    Connection conn_helper_serv = marc_pool_conn.getConnection();
    try {
      /*
       * Se hace la parte del
       * insert del elemento
       */
      PreparedStatement 
      
      line_request_statement_s = conn_helper_serv.prepareStatement(
          "INSERT INTO articulos (Nombre_articulo,Descripcion_articulo,Precio_articulo,Cantidad_articulo,Foto_articulo) VALUES (?,?,?,?,?)");
      try {
        /*
         * La parte de los elementos que se han declarado para el articulo son los
         * siguientes.
         */
        // Nombre
        
        
        line_request_statement_s.setString(1, articulo.nombre);

        // Descripción
        
        
        line_request_statement_s.setString(2, articulo.descripcion);

        // Precio
        
        
        line_request_statement_s.setFloat(3, articulo.precio);

        // Cantidad
        
        
        line_request_statement_s.setInt(4, articulo.cantidad);

        // Foto
        
        
        line_request_statement_s.setBytes(5, articulo.foto);
        
        // Se ejecuta la request para el update del insert de los elementos
        
        
        line_request_statement_s.executeUpdate();
      } finally {
        
        
        line_request_statement_s.close();
      }

      /*
       * En caso de que algo salga mal deberemos
       * mostrar el mensaje que aparece a continuación.
       */
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conn_helper_serv.close();
    }
    /*
     * En caso de que algo salga bien,
     * simplemente el response atendera el
     * ok
     */
    return Response.ok().build();
  }

  /*
   * Para la parte de
   * la busqueda de los archivos se
   * debe gestionar todo en la
   * siguiente sección de código en donde se presenta
   * presecisamente la preselección de la parte
   * en los articulos, en donde se toma la
   * sentencia like del tema de la descripción
   */
  @POST
  @Path("buscar_articulos")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response buscar_articulos(@FormParam("descripcion") String descripcion) throws Exception {
    Connection conn_helper_serv = marc_pool_conn.getConnection();
    ArrayList<Articulo> list_data_saver_reg = new ArrayList<Articulo>();

    /*
     * Se ejecuta la sentencia para poder
     * hacer la busqueda de los datos de manera adecuada
     */
    try {

      PreparedStatement 
      
      line_request_statement_s = conn_helper_serv
          .prepareStatement("SELECT * FROM articulos WHERE Descripcion_articulo LIKE ?");
      try {
        
        
        line_request_statement_s.setString(1, "%" + descripcion + "%");
        ResultSet result_set_helper = 
        
        line_request_statement_s.executeQuery();

        while (result_set_helper.next()) {
          Articulo articulo = new Articulo();
          
          // IDARTICULO
          articulo.identificador = result_set_helper.getInt("IDArticulo");
          
          // nombre
          articulo.nombre = result_set_helper.getString("Nombre_articulo");
          
          // descripcion
          articulo.descripcion = result_set_helper.getString("Descripcion_articulo");
          
          // precio del articulo
          articulo.precio = result_set_helper.getFloat("Precio_articulo");
          
          // cantidad del articulo
          articulo.cantidad = result_set_helper.getInt("Cantidad_articulo");
          
          // foto del articulo
          articulo.foto = result_set_helper.getBytes("Foto_articulo");
          
          // agregación del articulo
          list_data_saver_reg.add(articulo);

        }
        return Response.ok().entity(j.toJson(list_data_saver_reg)).build();
      } finally {
        
        
        line_request_statement_s.close();
      }

      /*
       * En caso de que exista algún tipo de fallo
       * lo que se debe tener en consideración lo siguiente
       */
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      /*
       * Se cierra la conexión
       */
      conn_helper_serv.close();
    }
  }

  /*
   * Se hace la compra de los articulos
   * para lo cual se establece en terminos de la
   * siguiente sección de código.
   */
  @POST
  @Path("comprar_articulo")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response comprar_articulo(@FormParam("cantidad") int cantidad, @FormParam("identificador") int identificador)
      throws Exception {
    Connection conn_helper_serv = marc_pool_conn.getConnection();

    try {
      conn_helper_serv.setAutoCommit(false);
      conn_helper_serv.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      /*
       * Para la parte de las transacciones
       * se debe hacer la parte siguiente en donde tendremos
       * varias lineas en donde se debera poder hacer
       * una seleccion de los articulos,
       * se podra hacer una inservion de los elementos
       * y finalmente una actualización de las cantidades
       * de los articulos que se tienen dentro del stock de la
       * tienda.
       */

     
       // ´rimera rquest
      PreparedStatement 
      
      line_request_statement_s = conn_helper_serv
          .prepareStatement("SELECT Cantidad_articulo FROM articulos WHERE IDArticulo=?");
     
          // Segunda request
      PreparedStatement 
      line_request_statement_t = conn_helper_serv
          .prepareStatement("INSERT INTO carro (IDArticulo,Cantidad_carro) VALUES (?,?)");
     
          // Tercera request
      PreparedStatement 
      line_request_statement_f = conn_helper_serv
          .prepareStatement("UPDATE articulos SET Cantidad_articulos = ? WHERE IDArticulo = ?");
      try {
        
        
        line_request_statement_s.setInt(1, identificador);
        ResultSet result_set_helper = 
        
        line_request_statement_s.executeQuery();
        int value_calculated_opt = 0;
        while (result_set_helper.next()) {
          value_calculated_opt = result_set_helper.getInt("Cantidad_articulo");
        }
        if (value_calculated_opt >= cantidad) {
          try {

            
            line_request_statement_t.setInt(1, identificador);
            
            line_request_statement_t.setInt(2, cantidad);
            
            line_request_statement_t.executeUpdate();

            
            line_request_statement_f.setInt(1, (value_calculated_opt - cantidad));
            
            line_request_statement_f.setInt(2, identificador);
            
            line_request_statement_f.executeUpdate();

            conn_helper_serv.commit();

          } catch (Exception e) {
            conn_helper_serv.rollback();
          }
        } else {

          return Response.status(400)
              .entity(j.toJson(new Error("Solo hay " + Integer.toString(value_calculated_opt)))).build();

        }

        return Response.ok().build();
      } finally {
        
        
        line_request_statement_s.close();
        
        line_request_statement_t.close();
        
        line_request_statement_f.close();
      }

    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conn_helper_serv.close();
    }

  }

  /*
   * Para la parte de
   * recolectar los elementos del
   * carrito se debe tomar en consideración el
   * siguiente aspectos,
   * para lo cual se destina el siguiente fragmento de
   * código
   */

  @POST
  @Path("traer_carrito")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response traer_carrito() throws Exception {
    Connection conn_helper_serv = marc_pool_conn.getConnection();
    ArrayList<Compra> list_data_saver_reg = new ArrayList<Compra>();

    try {

      PreparedStatement 
      
      line_request_statement_s = conn_helper_serv.prepareStatement(
          "select carro.IDCompra,articulos.nombre_articulo,articulos.foto_articulo,articulos.descripcion_articulo,carro.cantidad_carro,articulos.precio_articulo,carro.cantidad_carro*articulos.precio as costo from articulos, carro  WHERE articulos.IDArticulo =carro.IDArticulo");
      try {
        ResultSet result_set_helper = 
        
        line_request_statement_s.executeQuery();

        while (result_set_helper.next()) {
          Compra compra_def_h = new Compra();
         
          // Parte de la ID de la compra
          compra_def_h.identificador = result_set_helper.getInt("carro.IDCompra");
         
          // Nombre del articulo
          compra_def_h.nombre = result_set_helper.getString("articulos.nombre_articulo");
         
          // descripcion del articulo
          compra_def_h.descripcion = result_set_helper.getString("articulos.descripcion_articulo");
         
          // Precio del articulo
          compra_def_h.precio = result_set_helper.getFloat("articulos.precio_articulo");
         
          // cantidades del articulo
          compra_def_h.cantidad = result_set_helper.getInt("carro.cantidad");
         
          // Costo de los articulos
          compra_def_h.costo = result_set_helper.getFloat("costo");
         
          // Foto del articulo
          compra_def_h.foto = result_set_helper.getBytes("Foto");

         
          // Agregación de la list_data_saver_reg de la compra.
          list_data_saver_reg.add(compra_def_h);

        }
        return Response.ok().entity(j.toJson(list_data_saver_reg)).build();
      } finally {
        
        
        line_request_statement_s.close();
      }

      // Manejo de los posibles fallos dentro del sistema.
    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conn_helper_serv.close();
    }

  }

  /*
   * En caso de que se quiera
   * elimnar la compra se debe
   * tomar en consideración el siguiente
   * fragmento de código de modo
   * tal que podamos hacer el
   * "barrido" del contenido del
   * carrito de la tienda.
   */

  @POST
  @Path("eliminar_compra")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response eliminar_compra(@FormParam("identificador") int identificador) throws Exception {
    Connection conn_helper_serv = marc_pool_conn.getConnection();

    /*
     * Lo que se hace en esta sección es la parte de las consultas
     * o de las request para la base de datos.
     */
    try {
      conn_helper_serv.setAutoCommit(false);
      conn_helper_serv.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      
      // Primera request
      PreparedStatement 
      line_request_statement_fi = conn_helper_serv
          .prepareStatement("SELECT Cantidad_carro,IDArticulo FROM carro WHERE  IDCompra =  ?");
      
          // Segunda request
      PreparedStatement 
      
      line_request_statement_s = conn_helper_serv
          .prepareStatement("SELECT Cantidad_articulo FROM articulos WHERE  IDArticulo =  ?");
      
          // Tercera request
      PreparedStatement 
      line_request_statement_t = conn_helper_serv
          .prepareStatement("  UPDATE articulos SET Cantidad_articulo = ? WHERE IDArticulo = ?");
      
          // Cuarta request
      PreparedStatement 
      line_request_statement_f = conn_helper_serv
          .prepareStatement("DELETE FROM carro WHERE  IDCompra =  ?");
      /*
       * Se hace el uso de un try catch
       * para poder hacer el setteo del entero
       */
      try {
        
        line_request_statement_fi.setInt(1, identificador);
        ResultSet result_set_helper1 = 
        line_request_statement_fi.executeQuery();
        int value_return_helper_opt = 0;
        int idArticulo = 0;
        while (result_set_helper1.next()) {

          value_return_helper_opt = result_set_helper1.getInt("Cantidad_articulo");
          idArticulo = result_set_helper1.getInt("IDArticulo_articulo");
        }

        
        
        line_request_statement_s.setInt(1, idArticulo);
        ResultSet result_set_helper2 = 
        
        line_request_statement_s.executeQuery();
        int value_opt_reach_helper = 0;
        while (result_set_helper2.next()) {

          value_opt_reach_helper = result_set_helper2.getInt("Cantidad_articulo");

        }
        
        line_request_statement_t.setInt(1, value_opt_reach_helper + value_return_helper_opt);
        
        line_request_statement_t.setInt(2, idArticulo);
        
        line_request_statement_t.executeUpdate();

        
        line_request_statement_f.setInt(1, identificador);
        
        line_request_statement_f.executeUpdate();

        conn_helper_serv.commit();

      } catch (Exception e) {
        conn_helper_serv.rollback();
        return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
      } finally {
        
        line_request_statement_fi.close();
        
        
        line_request_statement_s.close();
        
        line_request_statement_t.close();
        
        line_request_statement_f.close();
      }

    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conn_helper_serv.close();
    }
    return Response.ok().build();
  }

  /*
   * Lo mismo que como en la parte anterior
   * se debe hacer la eliminación del carrito
   */

  @POST
  @Path("eliminar_carrito")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response eliminar_carrito() throws Exception {
    Connection conn_helper_serv = marc_pool_conn.getConnection();
    try {
      conn_helper_serv.setAutoCommit(false);
      conn_helper_serv.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
      ArrayList<Integer> list_data_saver_regCompras = new ArrayList<Integer>();
      
      // Se tiene la primera parte
      // del request

       /*
          Se hace la parte la seleccción de los elementos
          agrupados
        */
      PreparedStatement 
      line_request_statement_z = conn_helper_serv
          .prepareStatement("SELECT IDArticulo,Sum(cantidad) AS suma FROM carro  GROUP BY IDArticulo");

      try {

        ResultSet result_set_helper0 = 
        line_request_statement_z.executeQuery();
        while (result_set_helper0.next()) {
          PreparedStatement 
          line_request_statement_fi = conn_helper_serv
           /*
            Se hace la preparación del statement para 
            su posterior ejecución.
            */
              .prepareStatement("SELECT Cantidad_articulo FROM articulos WHERE IDArticulo = ?");
          
              line_request_statement_fi.setInt(1, result_set_helper0.getInt("IDArticulo"));
          ResultSet result_set_helper1 = 
          line_request_statement_fi.executeQuery();
          int value_begin_help_opt = 0;
          while (result_set_helper1.next()) {
            value_begin_help_opt = result_set_helper1.getInt("Cantidad_articulo");
          }
          result_set_helper1.close();

           /*
              Se hace el update de los elementos que del articulos
              con el IDArticulo conincidente.
            */
          PreparedStatement 
          
          line_request_statement_s = conn_helper_serv
              .prepareStatement("UPDATE articulos SET Cantidad_articulo = ? WHERE IDArticulo = ?");
          
              
              line_request_statement_s.setInt(1, result_set_helper0.getInt("suma") + value_begin_help_opt);
          
          
              line_request_statement_s.setInt(2, result_set_helper0.getInt("IDArticulo"));
          
          
          line_request_statement_s.executeUpdate();
          
          
          line_request_statement_s.close();

        }

         /*
            Se hace el borrado de los elementos bajo la condicional de del mayor
          */

        PreparedStatement 
        line_request_statement_t = conn_helper_serv
            .prepareStatement("DELETE FROM carro WHERE IDCompra > 0");
        
            line_request_statement_t.executeUpdate();
        
        line_request_statement_t.close();
        conn_helper_serv.commit();
        return Response.ok().build();

      } catch (Exception e) {
        conn_helper_serv.rollback();
        return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
      } finally {
        
        line_request_statement_z.close();
      }

    } catch (Exception e) {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    } finally {
      conn_helper_serv.close();
    }

  }

}
