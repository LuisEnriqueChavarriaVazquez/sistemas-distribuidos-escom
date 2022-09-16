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

import java.lang.reflect.Type;
import java.util.Base64;
import com.google.gson.*;

public class AdaptadorGsonBase64 implements JsonSerializer<byte[]>,JsonDeserializer<byte[]>
{
	public JsonElement serialize(byte[] src,Type typeOfSrc, JsonSerializationContext context)
	{
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}

	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		String s = json.getAsString().replaceAll("\\ ", "+");
		return Base64.getDecoder().decode(s);
	}
}
