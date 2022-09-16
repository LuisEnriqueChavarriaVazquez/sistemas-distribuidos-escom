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


//Hacemos las importaciones y la inclusión del paquete
package negocio;
import java.lang.reflect.Type;
//Fundamental para el uso de base 64
import java.util.Base64;
//Para el uso de gson dentro de java, util para hacer la deserialización y la serialización.
import com.google.gson.*;

public class AdaptadorGsonBase64 implements JsonSerializer<byte[]>,JsonDeserializer<byte[]>
{
	public JsonElement serialize(byte[] src,Type typeOfSrc, JsonSerializationContext context)
	{
		//Se retorna el encoder para la conversión.
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}

	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		//Reemplazo dentro del string y reconversión.
		String s = json.getAsString().replaceAll("\\ ", "+");
		return Base64.getDecoder().decode(s);
	}
}
