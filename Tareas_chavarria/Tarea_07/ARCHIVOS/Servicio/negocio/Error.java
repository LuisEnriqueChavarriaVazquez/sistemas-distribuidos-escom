
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

/*
  Error.java

  Ayuda a retornar un mensaje de error a nuestro usuario

  *En esencia lo que hacemos es definir una variable de tipo STRING, de modo que podemos
  pasar el mensaje una vez que llamemos.

  Evidentemente los mensajes de error podemos hacer que varien dependiendo de la situación o 
  el caso al que nos estemos enfrentando, por ejemplo en caso de que el usuario ingrese elementos
  incorrectos al momento de una busqueda, este podra recibir mensajes de error.
*/



package negocio;

public class Error
{
	String message;

	Error(String message)
	{
		this.message = message;
	}
}
