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

//Debemos importar todas nuestras bibliotecas
//de mo tal que podamos disponer de los recursos.
/*Se importar el buffered reader*/
import java.io.BufferedReader;
/*Para el manejo de las excepciones*/
import java.io.IOException;
/*Para poder trabajar con GSON*/
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
/*elementos adicionales*/
import java.util.Scanner; 
/*Importamos el encoder de nuestra URL*/
import java.net.URLEncoder;
/*Para el manejo de las librerias*/
import java.net.URL;
import java.net.HttpURLConnection;
/*Para el manejo de los stream de nuestro archivo*/
import java.io.OutputStream;
import java.io.InputStreamReader;


//Definimos el nombre nuestra clase
class cliente07{

	//Ya esta definida por defecto de acuerdo a los requerimientos de la tarea 7
	public static void Alta_usuario_m(Usuario usuario) throws Exception{
		/*Esta parte se nos especifico en la parte de nuestra carpeta de Servicio de la Tarea 6
		para lo cual solamente en la clase se nos indico modificar la ip de nuestra
		maquina virtual para poder hacer la conexión*/
		URL url = new URL("http://20.185.38.54:8080/Servicio/rest/ws/alta_usuario");

		//Para la conexión con nuestro archivo
		HttpURLConnection conexion_sistema_wd = (HttpURLConnection) url.openConnection();

		//Este es el protocolo base para poder realizar la conexión con nuestro archivo
		/*Es importante para el manejo de nuestra data dentro de la aplicación de modo tal que no tengamos
		problemas al momento de hacer el dealing de la data*/
		conexion_sistema_wd.setDoOutput(true);
		conexion_sistema_wd.setRequestMethod("POST");
		conexion_sistema_wd.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		/*Esta parte del GSON builder es para poder hacer el manejo de los aspectos que recibamos en
		los archivos con lo cual podamos hacer la codificación o el encoder de la data*/
		Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
		String parametros_taken_rw = "usuario=" + URLEncoder.encode(j.toJson(usuario), "UTF-8");

		//Debemos hacer el dealing con la salida de los datos que requerimos.
		OutputStream salida_stream_help = conexion_sistema_wd.getOutputStream();

		//Hacemos la escritura y posterior flush
		salida_stream_help.write(parametros_taken_rw.getBytes());
		salida_stream_help.flush();

		//Validación para la conexión a nuestro sistema donde
		//verificamos el código de conexió como 200
		if(conexion_sistema_wd.getResponseCode() == 200){
			//En caso de que la operación sea exitosa debemos 
			//regresar el mensaje de ok en pantalla, eso lo estipulamos en los 
			//requerimientos de la práctica.
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getInputStream())));
			String answer_system_data;
			while ((answer_system_data = dealbro_help.readLine()) != null ) {
				System.out.println("\nOK\n");
				System.out.println("\nEl usuario ha sido ingresado\n");
			}

		}else{
			//En caso de que la operación falle, hemos de retornar 
			//los mensajes correspondientes para indicar el error
			//del que se trata en el programa, algunas de las excepciones comunes
			//solian ser tema de parametros incopatibles o directamente tienen que ver con el SQL.
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getErrorStream())));
			String answer_system_data;
			System.out.println("__ERROR: ");
			while((answer_system_data = dealbro_help.readLine()) != null)
			{
				System.out.println(answer_system_data);
			};
		}
		//Hay que hacer la desconexión del sistema para proceder.
		conexion_sistema_wd.disconnect();
	}

	/*
	* Tambien podemos hacer la parte de consulta, que hara precisamente eso a la
	* base de datos 
	*/
	public static void Consulta_usuario_m(String email) throws Exception{
		
		/*
		La sección anterior nos ayuda a poder hacer el manejo de nuestro scanner y ademas
		debemos considerar lo ya mencionado respecto a las ips publicas y la forma
		de actualizar.
		*/
		URL url = new URL("http://20.185.38.54:8080/Servicio/rest/ws/consulta_usuario");
		//Debemos hacer el manejo necesario para nuestra URL
		Scanner sc = new Scanner(System.in);
		HttpURLConnection conexion_sistema_wd = (HttpURLConnection) url.openConnection();

		//Este es el protocolo base para poder realizar la conexión con nuestro archivo
		/*Es importante para el manejo de nuestra data dentro de la aplicación de modo tal que no tengamos
		problemas al momento de hacer el dealing de la data*/
		conexion_sistema_wd.setDoOutput(true);
		conexion_sistema_wd.setRequestMethod("POST");
		conexion_sistema_wd.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		//Hay que hacer el manejo del parametro del mail que nos ayudará
		//ha hacer nuestra consulta.
		String parametros_taken_rw = "email=" + URLEncoder.encode( "" + email, "UTF-8");

		//Para la parte de la conexión del sistema, en realidad es bastante repetitivo,
		//podriamos automatizar esta rutina, pero para efectos de esta práctica no es necesario.
		OutputStream salida_stream_help = conexion_sistema_wd.getOutputStream();
		salida_stream_help.write(parametros_taken_rw.getBytes());
		salida_stream_help.flush();

		//Validación para la conexión a nuestro sistema donde
		//verificamos el código de conexió como 200
		if(conexion_sistema_wd.getResponseCode() == 200){

			//Lo que hacemos es el manejo necesario de los buffers para
			//el envio y el recibimiento de los datos.
			char helper_combined_data = ' ';
			String answer_system_data;

			//Aqui esta lo ya descrito...
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getInputStream())));
			Usuario usuario_consultado = new Usuario();

			Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                
			while((answer_system_data = dealbro_help.readLine()) != null){
				//Hacemos el dealing con el json
				usuario_consultado = (Usuario) j.fromJson(answer_system_data, Usuario.class);

				/*
					NOTA.....
					esta sección nos sirve para la impresión de la data que emplearemos, pero
					debemos ser cuidadosos, porque importar mucho el orden de como obtuvimos
					los datos, esto lo atiendo en la parte final del código
				*/
				System.out.println("-------Datos del usuarios--------\n");
				System.out.println("__E-mail: " + usuario_consultado.email);
				System.out.println("__Nombre: " + usuario_consultado.nombre);
				System.out.println("__Apellido paterno: " + usuario_consultado.apellido_materno);
				System.out.println("__Apellido materno: " + usuario_consultado.apellido_paterno);
				System.out.println("__Fecha de nacimiento: " + usuario_consultado.fecha_nacimiento);
				System.out.println("__Telefono: " + usuario_consultado.telefono);
				System.out.println("__Genero: " + usuario_consultado.genero);

			} 
			
			//El formato de la pregunta ya esta estandarizado en los requerimientos de la práctica.
			System.out.println("\u00bfDesea modificar el usuario (s/n)?: ");

			//Tenemos que hacer nuestra variable helper
			helper_combined_data = sc.next().charAt(0);
			//jump next line
			sc.nextLine();

			//En caso de que el cliente nos diga que si quiere modificar los datos del usuario.
			if(helper_combined_data == 's'){
				//Iniciamos la petición de los datos para la modificación.
				System.out.println("\n_____________________________________\n");
				System.out.println("Por favor ingrese lo siguiente: \n");
				Usuario usuario = new Usuario(); 

				//Asignamos por defecto el mail del usuario.
				usuario.email = email;

				//Le pedimos que ingrese el nomrbe
			  	System.out.print("\n__Ingrese el nombre: ");
				usuario.nombre = sc.nextLine();
				if(usuario.nombre.equals("")) 
				{
					usuario.nombre = usuario_consultado.nombre;
				};

				//Le pedimos que ingrese el apellido paterno
				System.out.print("\n__Ingrese el apellido paterno: ");
				usuario.apellido_paterno = sc.nextLine();
				if(usuario.apellido_paterno.equals("")) 
				{
					usuario.apellido_paterno = usuario_consultado.apellido_paterno;
				};

				//Le pedimos que ingrese el apellido materno
				System.out.print("\n__Ingrese el apellido materno: ");
				usuario.apellido_materno = sc.nextLine();
				if(usuario.apellido_materno.equals("")) 
				{
					usuario.apellido_materno = usuario_consultado.apellido_materno;
				};

				//Le pedimos que ingrese la fecha de nacimiento
				System.out.print("\n__Ingrese la fecha de nacimiento: ");
				usuario.fecha_nacimiento = sc.nextLine();
				if(usuario.fecha_nacimiento.equals("")) 
				{
					usuario.fecha_nacimiento = usuario_consultado.fecha_nacimiento;	
				};			

				//Le pedimos que ingrese el número de telefono
				System.out.print("\n__Ingrese el numero de telefono: ");
				usuario.telefono = sc.nextLine();
				if(usuario.telefono.equals("")) {
					usuario.telefono = usuario_consultado.telefono;	
				};

				//Le pedimos que ingrese el genero
				System.out.print("\n__Ingrese el genero: ");
				usuario.genero = sc.nextLine();
				if(usuario.genero.equals("")) 
				{
					usuario.genero = usuario_consultado.genero;	
				};

				//Ya con los datos lo unico que tenemos que hacer es invocar al metodo...
				Modificar_usuario_m(usuario);
			}
		}else{
			//En caso de que tengamos un fallo esto es realmente repetido de la alta de los usuarios
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getErrorStream())));
			String answer_system_data;
			System.out.println("ERROR: ");
			while((answer_system_data = dealbro_help.readLine()) != null) {
				System.out.println(answer_system_data);
			};
			
		}
		//Necesitamos desconectarnos del sistema.
		conexion_sistema_wd.disconnect();

	}

	//Hacemos la modificación del usuarios aqui.
	public static void Modificar_usuario_m(Usuario usuario) throws Exception{

		//Este es el protocolo base para poder realizar la conexión con nuestro archivo
		/*Es importante para el manejo de nuestra data dentro de la aplicación de modo tal que no tengamos
		problemas al momento de hacer el dealing de la data*/
		//En realidad como lo mencioné este es el protocolo que yo segui para hacer la conexión de manera
		//exitosa.
		URL url = new URL("http://20.185.38.54:8080/Servicio/rest/ws/modifica_usuario");
		HttpURLConnection conexion_sistema_wd = (HttpURLConnection) url.openConnection();
		conexion_sistema_wd.setDoOutput(true);
		conexion_sistema_wd.setRequestMethod("POST");
		conexion_sistema_wd.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		//Para el manejo de nuestros GSON, debemos implementar lo siguiente.
		Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
		String parametros_taken_rw = "usuario=" + URLEncoder.encode(j.toJson(usuario), "UTF-8");
		//Para el dealing de nuestro output.
		OutputStream salida_stream_help = conexion_sistema_wd.getOutputStream();

		//Debemos hacer la escritura y el flush correspondiente.
		salida_stream_help.write(parametros_taken_rw.getBytes());
		salida_stream_help.flush();

		if(conexion_sistema_wd.getResponseCode() == 200){

			//En los requerimientos se nos especifica que debemos cubrir la parte de que
			//el mensaje que nosotros tenemos que hacer llegar es un mensaje de ok, para lo cual 
			//se hizo esta condición de modo tal que podemos hacer la adaptación al caso del response
			//200
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getInputStream())));
			String answer_system_data;
			System.out.println("\nOK\n");
			System.out.println("\nEl usuario ha sido modificado\n");
			while ((answer_system_data = dealbro_help.readLine()) != null ){
				System.out.println(answer_system_data);
			};
			
		}else{

			//En caso de que tengamos un error podemos eliminar el contenido sin ningun problema.
			/*
				Como lo mencioné con aterioridad debemos considerar que en la mayoria de los casos los errores que nos llegan son de tipo SQL
				aunque tambien en mi caso fue común recibir excepciones ante casos no validados, como cuando no se recibe la data esperada.
			*/
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getErrorStream())));
			String answer_system_data;
			System.out.println("ERROR: ");
			//Hacemos la impresión de la data.
			while((answer_system_data = dealbro_help.readLine()) != null) 
			{
				System.out.println(answer_system_data);
			};
		}
		//Para evitar problemas de desperdicio de recursos o rendimiento, es recomendable
		//y una buena práctica hacer la desconexión de nuestro sistema.
		conexion_sistema_wd.disconnect();

	}

	public static void Borrar_usuario_m(String email) throws Exception{
		//Este es el protocolo base para poder realizar la conexión con nuestro archivo
		/*Es importante para el manejo de nuestra data dentro de la aplicación de modo tal que no tengamos
		problemas al momento de hacer el dealing de la data*/
		//En realidad como lo mencioné este es el protocolo que yo segui para hacer la conexión de manera
		//exitosa.
		URL url = new URL("http://20.185.38.54:8080/Servicio/rest/ws/borra_usuario");
		HttpURLConnection conexion_sistema_wd = (HttpURLConnection) url.openConnection();
		conexion_sistema_wd.setDoOutput(true);
		conexion_sistema_wd.setRequestMethod("POST");
		conexion_sistema_wd.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		//Definimos nuestro parametro para el elemento del correo electrónico.
		String parametros_taken_rw = "email=" + URLEncoder.encode("" + email, "UTF-8");
		//Ahora bien, para el manejo de la salida del stream volvemos a definir lo siguiente.
		OutputStream salida_stream_help = conexion_sistema_wd.getOutputStream();

		//Hacemos la escritura y el flush correspondiente
		salida_stream_help.write(parametros_taken_rw.getBytes());
		salida_stream_help.flush();

		if(conexion_sistema_wd.getResponseCode() == 200){
			//Confirmación de la data una vez que el usuario ha sido borrado..
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getInputStream())));
			String answer_system_data;
			System.out.println("\nOk\n");
			System.out.println("\nEl usuario ha sido borrado\n");
			while ((answer_system_data = dealbro_help.readLine()) != null )
			{
				System.out.println(answer_system_data);
			};
			
		}else{
			BufferedReader dealbro_help = new BufferedReader(new InputStreamReader((conexion_sistema_wd.getErrorStream())));
			String answer_system_data;
			System.out.println("ERROR: ");
			while((answer_system_data = dealbro_help.readLine()) != null) 
			{
				System.out.println(answer_system_data);
			}
		}
		//Cerramos nuestra conexión
		conexion_sistema_wd.disconnect();

		
	}

	/*Zona principal del programa*/
	public static void main(String [] args) throws Exception{
		//Declaramos nuestro scanner
		Scanner sc = new Scanner(System.in);
		//Declaramos la variable del usuario y de el mail
		String email;
		Usuario usuario;
		//Para manerjo delos char.
		char opc = ' ';
		//Debemos hacer la gestión de nuestro menu
		/* 
			Nos mantendremos en el bucle mientras no tengamos la d pulsada.
		*/
		while(opc != 'd'){
			//Despliegue del menú segpun las indicaciones de los requermientos
			System.out.println("\n");
			System.out.println("a. Alta usuario");
			System.out.println("b. Consulta usuario");
			System.out.println("c. Borra usuario");
			System.out.println("d. Salir");
			System.out.println("\n Opci\u00f3n: ");
			opc = sc.next().charAt(0);
			sc.nextLine();
			System.out.println("\n\n");

			//Para el manejo de las opciones en caso de que el 
			//resultado de la operación haya sido una a. (alta de usuario.)
			switch(opc){ 
				//En caso de la a
				case 'a':
					//Bienvenida
					System.out.println("Introduzca los siguientes datos: ");
					usuario = new Usuario(); 
					//Para el caso del mail
					System.out.print("\n__Ingrese el e-mail: ");
				  	usuario.email = sc.nextLine();
					  //Para el caso del nombre
				  	System.out.print("\n__Ingrese el nombre: ");
					usuario.nombre = sc.nextLine();
					//Para el caso del apellido paterno
					System.out.print("\n__Ingrese el apellido paterno: ");
					usuario.apellido_paterno = sc.nextLine();
					//Para el caso del materno
					System.out.print("\n__Ingrese el apellido materno: ");
					usuario.apellido_materno = sc.nextLine();
					//Para la fecha de nacimiento
					System.out.print("\n__Ingrese la fecha de nacimiento: ");
					//Para el caso del número de telefono
					usuario.fecha_nacimiento = sc.nextLine();
					System.out.print("\n__Ingrese telefono: ");
					usuario.telefono = sc.nextLine();
					//Para el caso del genero
					System.out.print("\n__Ingrese el genero: ");
					usuario.genero = sc.nextLine();
					Alta_usuario_m(usuario);

					break;
				case 'b':
					//Hacemos la llamada en caso de que queramos consultar
					System.out.print("\nIntroduzca el email: ");
					email = sc.nextLine();
					Consulta_usuario_m(email);
					break;
				case 'c':
					//Hacemos la llamada en caso de que queramos borrar.
					System.out.print("\nIntroduzca el email: ");
					email = sc.nextLine();
					Borrar_usuario_m(email);
					break;
				default:
					break;
			}
		}
	}
}

//Definición de los elementos para el usuario
class Usuario {


    int id_usuario;
    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;

    public Usuario(){}
}

