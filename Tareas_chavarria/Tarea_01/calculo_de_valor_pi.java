/*
************************************************
*
* Datos de la práctica.
*
* Materia: Desarrollo de Sistemas distribuidos
* Profesor: Carlos Pineda Guerrero
* Alumnos:
*           Cazares Martínez Maximiliano.
*           Chavarría Vázquez Luis Enrique.
*           Cipriano Damián Sebastián.
*
* Equipo 2
* Grupo: 4CV11
* Practica 01: Calculo de PI.
* 
************************************************
*/

//Primero debemos importar todas nuestras bibliotecas
// Se describen algunos aspectos básicos de dichas
// bibliotecas para que podamos trabajar sin confusiones.
/*
    Las primeras sirven para el flujo de entrada de los datos
    de la aplicación donde se permite leer y sacar datos de
    tipo primitivo de un flujo de entrada que es subyacente
    e independiente a la máquina.

    En este caso los datos estan dentro del flujo de datos "stream"
    y pueden ser leidos por un flujo de datos del mismo modo

    Finalmente es necesario aclarar que la parte para el server socket
    se suele emplear para un socket que esta esperando
    una petición o un "request" que venga de la red.
*/

/*Bibliotecas de java.io...*/
import java.io.DataInputStream;
import java.io.DataOutputStream;

/*Bibliotecas de java.net...*/
import java.net.ServerSocket;
import java.net.Socket;

/*Hemos agregado esta biblioteca para mostrar algunas ventanas emergentes*/
/*Tambien para herramientas extras en caso de que queramos redondear*/
import javax.swing.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

//Debemos tener nuestra clase la cual será llamada de la siguiente manera
//calculo_de_valor_pi
public class calculo_de_valor_pi {

    // Definimos el valor de pi como "valor_de_pi"
    // Por defecto será definido como 0
    // Aunque es double por la naturaleza del valor.
    static double valor_de_pi = 0;

    // Esta es la estructura base de nuestr programa
    public static void main(String[] args) {

        // Hemos de hacer la definiciones pertinentes
        // para el trabajo con nuestros nodos.
        // El cual se muestra en la siguiente linea de código.
        int numero_de_los_nodos = Integer.parseInt(args[0]);

        // Para las ventanas que mostraremos en el programa
        JFrame jFrame = new JFrame();
        JDialog jd = new JDialog(jFrame);

        // Es pertinente hacer algunas cuantas validaciones
        // para poder actuar dependiendo del nodo al que estemos
        // haciendo el llamado
        /*
         * En el caso de nuestro nodo 0, lo que tenemos que
         * ver es que será nuestro setteo inicial para que los demas nodos
         * puedan funcionar según la estructura que usted nos proporcionó.
         * 
         * Y a posteriori se hará con los demás nodos.
         */

        // En caso de nodo origen.
        if (numero_de_los_nodos == 0) {
            // Para la consola del sistema
            System.out.println("---------------------------------");
            System.out.println("Ya esta listo, ingrese los otros valores en las terminales...");
            System.out.println("---------------------------------\n\n");
            JOptionPane.showMessageDialog(jFrame, "Programa corriendo de manera correcta");
            /*
             * Debemos iniciiar cada uno de los nodos, para lo cual en principio hay que
             * definir a todos ellos, segun la estructura que usted nos dio debemos númerar
             * otros cuatro unidades de nodos que se enlazaran.
             * 
             * Una cosa que queremos recalcar es que como en el diagrama las flechas
             * hiban hacia afuera por eso estamos tomando nuestro nodo cero o nodo origen
             * como el central.
             */

            try {
                // Creamos la primera unidad de nodos
                sender_designado unidad_uno_def = new sender_designado(35_007);
                unidad_uno_def.start();
                unidad_uno_def.join();

                // Creamos la segunda unidad de nodos
                sender_designado unidad_dos_def = new sender_designado(35_009);
                unidad_dos_def.start();
                unidad_dos_def.join();

                // Creamos la tercera unidad de nodos
                sender_designado unidad_tres_def = new sender_designado(35_011);
                unidad_tres_def.start();
                unidad_tres_def.join();

                // Creamos la cuarta unidad de nodos
                sender_designado unidad_cuatro_def = new sender_designado(35_013);
                unidad_cuatro_def.start();
                unidad_cuatro_def.join();

                /*
                 * Conforme vayamos ejecutando mostraremos los valores
                 * en la consola, (previos a la llegada al valor de PI)
                 * , del mismo modo presentamos el valor redondo.
                 */

                BigDecimal corto = new BigDecimal(valor_de_pi).setScale(3, RoundingMode.HALF_UP);
                double valor_de_pi_corto = corto.doubleValue();
                
                System.out.println("--------------------------------------");
                System.out.println("Calculo del valor para Pi == " + valor_de_pi);
                System.out.println("Valor redondeado a tres decimales == " + valor_de_pi_corto);
                System.out.println("--------------------------------------\n");
            } catch (Exception e) {
                // En caso de...
                System.out.println("--------------------------------------");
                System.err.println("Hubo un error!!! ==> " + e);
                System.out.println("--------------------------------------\n");
            }
        } else if (numero_de_los_nodos == 1) { // Selección del primer nodo
            /*
             * Literamente debemos echar a andar el proceso pero necesitamos
             * pasar el número de nuestro nodo y tambien debemos pasarle el puerto,
             * en este caso los puertos que hemos elegido son para evitar problemas
             * con otros programas adyacentes.
             */

            Servidor_practica primer_servidor = new Servidor_practica(numero_de_los_nodos, 35_007);
            primer_servidor.exec_srv_programa();

            /*
             * Debemos notificar en la consola que el proceso a funcionado y además de ello
             * hemos implementado un mensaje en caso de que el usuario no este viendo su
             * consola
             * en ese momento.
             */
            System.out.println("--------------------------------------");
            System.out.println("Estado del servidor nodo_" + numero_de_los_nodos + " ==> listo ... 35007");
            System.out.println("--------------------------------------\n");
            JOptionPane.showMessageDialog(jFrame, "LISTO el nodo " + numero_de_los_nodos);
        } else if (numero_de_los_nodos == 2) { // Selección del segundo nodo
            Servidor_practica segundo_servidor = new Servidor_practica(numero_de_los_nodos, 35_009);
            segundo_servidor.exec_srv_programa();

            System.out.println("--------------------------------------");
            System.out.println("Estado del servidor nodo_" + numero_de_los_nodos + " ==> listo ... 35009");
            System.out.println("--------------------------------------\n");
            JOptionPane.showMessageDialog(jFrame, "LISTO el nodo " + numero_de_los_nodos);
        } else if (numero_de_los_nodos == 3) { // Selección del tercer nodo
            Servidor_practica tercer_servidor = new Servidor_practica(numero_de_los_nodos, 35_011);
            tercer_servidor.exec_srv_programa();

            System.out.println("--------------------------------------");
            System.out.println("Estado del servidor nodo_" + numero_de_los_nodos + " ==> listo ... 35011");
            System.out.println("--------------------------------------\n");
            JOptionPane.showMessageDialog(jFrame, "LISTO el nodo " + numero_de_los_nodos);
        } else if (numero_de_los_nodos == 4) { // Selección del cuarto nodo
            Servidor_practica cuarto_servidor = new Servidor_practica(numero_de_los_nodos, 35_013);
            cuarto_servidor.exec_srv_programa();

            System.out.println("--------------------------------------");
            System.out.println("Estado del servidor nodo_" + numero_de_los_nodos + " ==> listo ... 35013");
            System.out.println("--------------------------------------\n");
            JOptionPane.showMessageDialog(jFrame, "LISTO el nodo " + numero_de_los_nodos);
        } else {
            /*
             * Si el usuario no ingresa un valor acorde entonces
             * se envia mensaje.
             */
            JOptionPane.showMessageDialog(jFrame, "Denos un valor que este en el rango del 0 al 4");
        }
    }

    static class sender_designado extends Thread {

        // Primero que nada definimos nuestra variable
        // para el puerto.
        int puerto_designado;

        static Object obj = new Object();

        sender_designado(int puerto_designado) {
            this.puerto_designado = puerto_designado;
        }

        int ctr = 0;
        public void run() {
            try {
                while (true) {
                    try {
                        // Hacemos lo propio con los sockets, identificando el loopback "localhost"
                        // dentro
                        // de los parametros y además el puerto designado al nodo.
                        Socket enlace_designado = new Socket("localhost", puerto_designado);
                        DataInputStream puerta_de_acceso = new DataInputStream(enlace_designado.getInputStream());

                        // Debemos especificar que se trata de un double por la naturaleza del cálculo.
                        double calculo_aux_de_acumulacion = puerta_de_acceso.readDouble();

                        // Aunque no se epscifica, se implementa la sincronización.
                        synchronized (obj) {
                            valor_de_pi = valor_de_pi + calculo_aux_de_acumulacion;
                        }

                        /*
                         * En el caso de que estemos en el cuarto nodo, lo que hacemos es mostrar
                         * en ventana el valor final del PI y tambien redondeamos.
                         */
                        BigDecimal corto = new BigDecimal(valor_de_pi).setScale(3, RoundingMode.HALF_UP);
                        double valor_de_pi_corto = corto.doubleValue();
                        ctr = ctr + 1;
                        if (valor_de_pi_corto == 3.142) {
                            // Se muestra el valor "final" en consola
                            System.out.println("---------------------------------------------");
                            System.out.println("------ DATOS " + puerto_designado);
                            System.out.println("---------------------------------------------");
                            System.out.println("------ VALOR OBTENIDO FINAL ==> " + valor_de_pi);
                            System.out.println("------ VALOR OBTENIDO FINAL REDONDEADO ==> " + valor_de_pi_corto);
                            System.out.println("//////////////////////////////////\n");
                            //Ventana con el resultado final.
                            JFrame jFrame = new JFrame();
                            JOptionPane.showMessageDialog(jFrame,
                                    "Proceso terminado! el valor final es..." + valor_de_pi);
                        } else {
                            // Se muestra el valor "previo" en consola
                            System.out.println("---------------------------------------------");
                            System.out.println("------ DATOS " + puerto_designado);
                            System.out.println("---------------------------------------------");
                            System.out.println("------ VALOR OBTENIDO ==> " + valor_de_pi);
                            System.out.println("------ VALOR OBTENIDO REDONDEADO ==> " + valor_de_pi_corto);
                            System.out.println("//////////////////////////////////\n");
                        }

                        // Cerramos el enlace
                        enlace_designado.close();
                        break;

                    } catch (Exception e) {
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                System.out.println("--------------------------------------");
                System.out.println("Excepcion!!! ==> " + e);
                System.out.println("--------------------------------------\n");
            }
        }
    }

    static class Servidor_practica {

        /*
         * Primero que nada debemos designar las variables
         * para el numero de los nodos y tambien el
         * puerto designado para cada uno de los nodos
         * que necesitamos. (0-4)
         */
        int numero_de_los_nodos, puerto_designado;

        Servidor_practica(int numero_de_los_nodos, int puerto_designado) {

            this.numero_de_los_nodos = numero_de_los_nodos;
            this.puerto_designado = puerto_designado;

        }

        double calculo_aux_de_acumulacion;

        public void exec_srv_programa() {
            try {
                // Para los sockets
                ServerSocket server__f = new ServerSocket(puerto_designado);
                Socket enlace_designado = server__f.accept();

                // Definimos el data output stream
                DataOutputStream output_principal_del_acumulado = new DataOutputStream(
                        enlace_designado.getOutputStream());

                // Seteamos el valor a cero
                calculo_aux_de_acumulacion = 0;

                /* Procedemos hacer nuestro calculo
                Haciendo la gestión en caso de que el nodo 
                sea impar o que se trate de un nodo par. */
                double numero_de_los_nodosDouble = Double.valueOf(numero_de_los_nodos);

                double i = 0;
                while (i < 1_000_000) {
                    calculo_aux_de_acumulacion = calculo_aux_de_acumulacion
                            + (4 / ((i * 8) + (2 * (numero_de_los_nodosDouble - 2)) + 3));
                    i++;
                }

                calculo_aux_de_acumulacion = (numero_de_los_nodos % 2 == 0) ? -calculo_aux_de_acumulacion
                        : calculo_aux_de_acumulacion;

                output_principal_del_acumulado.writeDouble(calculo_aux_de_acumulacion);

                // Cerramos el enlace
                enlace_designado.close();
                server__f.close();

            } catch (Exception e) {
                // En caso de...
                System.out.println(e);
            }
        }
    }
}