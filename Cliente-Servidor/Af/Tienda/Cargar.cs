using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

namespace Tienda
{
    public static class Cargar
    {
        [FunctionName("Cargar")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Anonymous, "post")] HttpRequest req,
            ILogger log)
        {
            try
            {
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                dynamic data = JsonConvert.DeserializeObject(requestBody);

                // la variable de entorno HOME está predefinida en el servidor (C:\home o D:\home)
                // Los archivos cargados se pueden ver en el portal, seleccionando la aplicación de funciones y la opción "Consola" en la sección "Herramientas de desarrollo"
                // En el servidor no se puede escribir en el directorio c:\home\site\wwwroot
                // Para que la aplicación "carga.html" pueda acceder a la función "Cargar" es necesario habilitar CORS en el portal de Azure.

                string home = Environment.GetEnvironmentVariable("HOME");

                string ruta = data.ruta;
                string nombre = data.nombre;
                byte[] contenido = Convert.FromBase64String((string)data.contenido);

                // si no existe el directorio correspondiente a la ruta, lo crea
                if (!Directory.Exists(home + "/data" + ruta))
                    Directory.CreateDirectory(home + "/data" + ruta);

                File.WriteAllBytes(home + "/data" + ruta + "/" + nombre, contenido);
                return new OkObjectResult("OK");
            }
            catch (Exception e)
            {
                return new BadRequestObjectResult(e.Message);
            }
        }
    }
}