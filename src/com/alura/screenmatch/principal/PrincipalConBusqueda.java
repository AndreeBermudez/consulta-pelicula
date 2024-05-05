package com.alura.screenmatch.principal;

import com.alura.screenmatch.excepcion.ErrorEnConversionDeDuracionException;
import com.alura.screenmatch.modelos.Titulo;
import com.alura.screenmatch.modelos.TituloOMDB;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalConBusqueda{
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner entrada = new Scanner(System.in);
        List<Titulo> titulos = new ArrayList<>();
        //Crear JSON
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while (true) {
            System.out.println("Escriba el nombre de la pelicula: ");
            var busqueda = entrada.nextLine();

            if (busqueda.equalsIgnoreCase("salir")) {
                break;
            }

            String direccion = "https://www.omdbapi.com/?t=" + busqueda.replace(" ","+") + "&apikey=66e40810";
            try{
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();

                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());
                String json = response.body();
                //System.out.println(json);


                TituloOMDB miTituloOMDB = gson.fromJson(json, TituloOMDB.class);
                System.out.println(miTituloOMDB);

                Titulo miTitulo = new Titulo(miTituloOMDB);
                System.out.println("Titulo ya convertido"+miTitulo);
                titulos.add(miTitulo);

            } catch(NumberFormatException e){
                System.out.println("Ocurrió un error: ");
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e){
                System.out.println("Error en la URI, verifique la dirección");
            } catch (ErrorEnConversionDeDuracionException e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println(titulos);

        FileWriter escritura = new FileWriter("titulos.json");
        escritura.write(gson.toJson(titulos)); //Convertimos una lista a Json
        escritura.close();
        System.out.println("Finalizó la ejecución del programa");
    }
}
