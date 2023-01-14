// Carlos Pineda Guerrero. 2021-2023

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

namespace Tienda
{
    public static class Borrar
    {
        public class Articulo2 {
            public int? ID;
            public string Nombre;
            public string Descripcion;
            public float? Precio;
            public int? Cantidad;
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
        [FunctionName("Borrar")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Anonymous, "post",Route ="borrar_articulo")] HttpRequest req,
            ILogger log)
        {
            try
            {
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                Articulo2 data = JsonConvert.DeserializeObject<Articulo2>(requestBody);
                Articulo2 articulo = data;
                if(articulo.ID == null ) throw new Exception("No contiene ID");
                if(articulo.Cantidad == null || articulo.Cantidad == 0) throw new Exception("No contiene cantidad");

                string Server = Environment.GetEnvironmentVariable("Server");
                string UserID = Environment.GetEnvironmentVariable("UserID");
                string Password = Environment.GetEnvironmentVariable("Password");
                string Database = Environment.GetEnvironmentVariable("Database");

                string cs = "Server=" + Server + ";UserID=" + UserID + ";Password=" + Password + ";" + "Database=" + Database + ";SslMode=Preferred;";
                var conexion = new MySqlConnection(cs);
                conexion.Open();

                MySqlTransaction transaccion = conexion.BeginTransaction();

                try
                {
                    var cmd_1 = new MySqlCommand();
                    cmd_1.Connection = conexion;
                    cmd_1.Transaction = transaccion;
                    cmd_1.CommandText = "CALL borrarArticulo(@ID,@Cantidad)";
                    cmd_1.Parameters.AddWithValue("@ID", articulo.ID);
                    cmd_1.Parameters.AddWithValue("@Cantidad", articulo.Cantidad);
                    cmd_1.ExecuteNonQuery();
                    transaccion.Commit();
                    return new OkObjectResult("Articulo Eliminado");
                }
                catch (Exception e)
                {
                    transaccion.Rollback();
                    throw new Exception(e.Message);
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
