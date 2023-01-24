using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using MySql.Data.MySqlClient;
using System.Collections.Generic;

namespace T10_2015030899
{
    public static class Consulta2
    {
        public class Articulo2
        {
            public int? ID;
            public string Nombre;
            public string Descripcion;
            public float Precio;
            public int Cantidad;
            public string foto;
        }
        class ParamConsultaUsuario
        {
            public string Nombre;
        }
        class Error
        {
            public string mensaje;
            public Error(string mensaje)
            {
                this.mensaje = mensaje;
            }
        }
        [FunctionName("Consulta2")]
        public static async Task<IActionResult> Run([HttpTrigger(AuthorizationLevel.Function, "post", Route = "consulta_articulo")] HttpRequest req, ILogger log)
        {
            try
            {
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                ParamConsultaUsuario data = JsonConvert.DeserializeObject<ParamConsultaUsuario>(requestBody);
                string Nombre = data.Nombre;
                string Server = Environment.GetEnvironmentVariable("Server");
                string UserID = Environment.GetEnvironmentVariable("UserID");
                string Password = Environment.GetEnvironmentVariable("Password");
                string Database = Environment.GetEnvironmentVariable("Database");

                string cs = "Server=" + Server + ";UserID=" + UserID + ";Password=" + Password + ";" + "Database=" + Database + ";SslMode=Preferred;";
                var conexion = new MySqlConnection(cs);
                conexion.Open();

                try
                {
                    var cmd = new MySqlCommand("SELECT ID_articulo,Nombre,Descripcion,Precio,Cantidad,Foto,length(Foto) FROM articulos WHERE Nombre LIKE '%" + Nombre + "%' OR Descripcion LIKE '%" + Nombre + "%' ");
                    cmd.Connection = conexion;
                    cmd.Parameters.AddWithValue("@Nombre", Nombre);
                    MySqlDataReader r = cmd.ExecuteReader();
                    List<Articulo2> articulos = new List<Articulo2>();
                    try
                    {
                        while (r.Read())
                        {
                            var auxA = new Articulo2();
                            auxA.ID = r.GetInt32(0);
                            auxA.Nombre = r.GetString(1);
                            auxA.Descripcion = r.GetString(2);
                            auxA.Precio = r.GetFloat(3);
                            auxA.Cantidad = r.GetInt32(4);
                            if (!r.IsDBNull(5))
                            {
                                var longitud = r.GetInt32(6);
                                byte[] foto = new byte[longitud];
                                r.GetBytes(5, 0, foto, 0, longitud);
                                auxA.foto = Convert.ToBase64String(foto);
                            }
                            articulos.Add(auxA);
                        }
                        // Notar que el formato de fecha es compatible con <input> de HTML con tipo "datetime-local"
                        return new ContentResult { Content = JsonConvert.SerializeObject(articulos), ContentType = "application/json" };
                    }
                    finally
                    {
                        r.Close();
                    }
                }
                finally
                {
                    conexion.Close();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return new BadRequestObjectResult(JsonConvert.SerializeObject(new Error(e.Message)));
            }
        }
    }
}
