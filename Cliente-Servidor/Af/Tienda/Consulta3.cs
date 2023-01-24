using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

using MySql.Data.MySqlClient;

namespace Tienda{
    public static class Consulta3{
        public class Articulo2 {
            public int? ID;
            public string Nombre;
            public string Descripcion;
            public float Precio;
            public int Cantidad;
            public int Cantidad2;
            public string foto;
        }
        class Error
        {
            public string mensaje;
            public Error(string mensaje)
            {
                this.mensaje = mensaje;
            }
        }
        [FunctionName("Consulta3")]
        public static async Task<IActionResult> Run([HttpTrigger(AuthorizationLevel.Function, "post",Route ="consulta_carrito")] HttpRequest req,ILogger log){
            try{
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                string Server = Environment.GetEnvironmentVariable("Server");
                string UserID = Environment.GetEnvironmentVariable("UserID");
                string Password = Environment.GetEnvironmentVariable("Password");
                string Database = Environment.GetEnvironmentVariable("Database");

                string cs = "Server=" + Server + ";UserID=" + UserID + ";Password=" + Password + ";" + "Database=" + Database + ";SslMode=Preferred;";
                var conexion = new MySqlConnection(cs);
                conexion.Open();

                try{
                    var cmd = new MySqlCommand("SELECT a.ID_articulo,a.Nombre,a.Descripcion,a.Precio,a.Cantidad,a.Foto,length(a.Foto),b.Cantidad FROM Articulos a INNER JOIN Carrito b ON a.ID_articulo = b.ID_articulo;");
                    cmd.Connection = conexion;
                    MySqlDataReader r = cmd.ExecuteReader();
                    List<Articulo2> articulos = new List<Articulo2>();
                    try{
                        while(r.Read()){
                            var auxA = new Articulo2();
                            auxA.ID = r.GetInt32(0);
                            auxA.Nombre = r.GetString(1);
                            auxA.Descripcion = r.GetString(2);
                            auxA.Precio = r.GetFloat(3);
                            auxA.Cantidad = r.GetInt32(4);
                            if (!r.IsDBNull(5)){
                                var longitud = r.GetInt32(6);
                                byte[] foto = new byte[longitud];
                                r.GetBytes(5,0,foto,0,longitud);
                                auxA.foto = Convert.ToBase64String(foto);
                            }
                            auxA.Cantidad2 = r.GetInt32(7);
                            articulos.Add(auxA);
                        }
                        // Notar que el formato de fecha es compatible con <input> de HTML con tipo "datetime-local"
                        return new ContentResult { Content = JsonConvert.SerializeObject(articulos), ContentType = "application/json" };
                    }
                    finally{
                        r.Close();
                    }
                }
                finally{
                    conexion.Close();
                }
            }
            catch (Exception e){
                Console.WriteLine(e.Message);
                return new BadRequestObjectResult(JsonConvert.SerializeObject(new Error(e.Message)));
            }
        }
    }
}