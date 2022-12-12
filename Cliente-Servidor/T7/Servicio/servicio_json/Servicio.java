/*
  Servicio.java
  Servicio web tipo REST
  Recibe parámetros utilizando JSON
  Carlos Pineda Guerrero, noviembre 2022
*/

package servicio_json;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.google.gson.*;

// la URL del servicio web es http://localhost:8080/Servicio/rest/ws
// donde:
//	"Servicio" es el dominio del servicio web (es decir, el nombre de archivo Servicio.war)
//	"rest" se define en la etiqueta <url-pattern> de <servlet-mapping> en el archivo WEB-INF\web.xml
//	"ws" se define en la siguiente anotacin @Path de la clase Servicio

@Path("ws")
public class Servicio
{
  static DataSource pool = null;
  static
  {		
    try
    {
      Context ctx = new InitialContext();
      pool = (DataSource)ctx.lookup("java:comp/env/jdbc/datasource_Servicio");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  static Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

  @POST
  @Path("alta_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response alta(String json) throws Exception
  {
    Articulo p2 = (Articulo) j.fromJson(json,Articulo.class);



    Connection conexion = pool.getConnection();

    if (p2.Nombre == null || p2.Nombre.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el nombre del articulo"))).build();
    if (p2.Descripcion == null || p2.Descripcion.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la descripcion del articulo"))).build();
    if (p2.Precio == null || p2.Precio.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el precio del articulo"))).build();
    if (p2.Cantidad == null || p2.Cantidad.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la cantidad del articulo"))).build();
    if (p2.foto == null || p2.foto.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar foto del articulo"))).build();

    try
    {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement("INSERT INTO Articulos(ID_articulo,Nombre,Descripcion,Precio,Cantidad,Foto) VALUES (0,?,?,?,?,?)");
 
      try
      {
        stmt_1.setString(1,p2.Nombre);
        stmt_1.setString(2,p2.Descripcion);
        stmt_1.setFloat(3, p2.Precio);
        stmt_1.setInt(4, p2.Cantidad);
        stmt_1.setBytes(5,p2.foto);
        stmt_1.executeUpdate();
      }
      finally
      {
        stmt_1.close();
      }
      conexion.commit();
    }
    catch (Exception e)
    {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }
  @POST
  @Path("consulta_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consulta(String json) throws Exception
  {
    Articulo p = (Articulo) j.fromJson(json,Articulo.class);
    String Nombre = p.Nombre;

    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT ID_articulo,Nombre,Descripcion,Precio,Cantidad,Foto FROM Articulos WHERE Nombre LIKE '%"+Nombre+"%' OR Descripcion LIKE '%"+Nombre+"%' ");
      try
      {
        ResultSet rs = stmt_1.executeQuery();
        ArrayList<Articulo2> articulosArray = new ArrayList<>();
        try
        {
          if(rs!=null){
            while(rs.next()){
              Articulo2 r = new Articulo2();
              r.ID = rs.getInt(1);
              r.Nombre = rs.getString(2);
              r.Descripcion = rs.getString(3);
              r.Precio = rs.getFloat(4);
              r.Cantidad = rs.getInt(5);
              r.foto = rs.getBytes(6);
              articulosArray.add(r);
            }
            return Response.ok().entity(j.toJson(articulosArray)).build();
          }
          return Response.status(400).entity(j.toJson(new Error("No hay articulos"))).build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.close();
    }
  }
  @POST
  @Path("consulta_articulos")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consulta2(String json) throws Exception
  {
    ParamConsultaArticulo p = (ParamConsultaArticulo) j.fromJson(json,ParamConsultaArticulo.class);
    String Nombre = p.Nombre;
    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT ID_articulo,Nombre,Descripcion,Precio,Cantidad,Foto FROM Articulos");
      try
      {
        ResultSet rs = stmt_1.executeQuery();
        ArrayList<Articulo2> articulosArray = new ArrayList<>();
        try
        {
          if(rs!=null){
            while(rs.next()){
              Articulo2 r = new Articulo2();
              r.ID = rs.getInt(1);
              r.Nombre = rs.getString(2);
              r.Descripcion = rs.getString(3);
              r.Precio = rs.getFloat(4);
              r.Cantidad = rs.getInt(5);
              r.foto = rs.getBytes(6);
              articulosArray.add(r);
            }
            return Response.ok().entity(j.toJson(articulosArray)).build();
          }
          return Response.status(400).entity(j.toJson(new Error("No hay articulos"))).build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.close();
    }
  }
  @POST
  @Path("alta_articulo2")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response alta2(String json) throws Exception
  {
    Articulo p2 = (Articulo) j.fromJson(json,Articulo.class);


    Connection conexion = pool.getConnection();

    if (p2.Nombre == null || p2.Nombre.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el nombre del articulo"))).build();
    if (p2.Descripcion == null || p2.Descripcion.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la descripcion del articulo"))).build();
    if (p2.Cantidad == null || p2.Cantidad.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la cantidad del articulo"))).build();
    if (p2.foto == null || p2.foto.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar foto del articulo"))).build();
    if (p2.Precio == null || p2.Precio.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el precio del articulo"))).build();
    

    try
    {
      conexion.setAutoCommit(false);

      PreparedStatement stmt_1 = conexion.prepareStatement("INSERT INTO prueba(ID,Nombre,Descripcion,Cantidad,Foto,Precio) VALUES (0,?,?,?,?,?)");
 
      try
      {
        stmt_1.setString(1,p2.Nombre);
        stmt_1.setString(2,p2.Descripcion);
        stmt_1.setInt(3,p2.Cantidad);
        stmt_1.setBytes(4,p2.foto);
        stmt_1.setFloat(5,p2.Precio);
        stmt_1.executeUpdate();
      }
      finally
      {
        stmt_1.close();
      }
      conexion.commit();
    }
    catch (Exception e)
    {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }
  @POST
  @Path("compra_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response compra(String json) throws Exception
  {
    Articulo2 p2 = (Articulo2) j.fromJson(json,Articulo2.class);


    Connection conexion = pool.getConnection();

    if (p2.ID == null || p2.ID.equals(""))
      return Response.status(400).entity(j.toJson(new Error("No contiene ID"))).build();
    if (p2.Cantidad == null || p2.Cantidad.equals(""))
      return Response.status(400).entity(j.toJson(new Error("No contiene cantidad"))).build();
    
    try
    {
      conexion.setAutoCommit(false);
      int auxCantidad;
      PreparedStatement query1 = conexion.prepareStatement("CALL Compra(?,?)");
      try{
        query1.setInt(1, p2.ID);
        query1.setInt(2, p2.Cantidad);
        ResultSet rs=query1.executeQuery();
        try {
          if(rs.next()){
            auxCantidad = rs.getInt(1);
            if(auxCantidad == 1){
              return Response.status(400).entity(j.toJson(new Error("No tenemos esa cantidad en stock"))).build();
            }
          }else{
            return Response.status(400).entity(j.toJson(new Error("No se realizo la accion"))).build();
          }
        } finally {
          rs.close();
        } 
      }
      finally{
        query1.close();
      }
      conexion.commit();
    }
    catch (Exception e)
    {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }
  @POST
  @Path("consulta_carrito")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response carrito(String json) throws Exception
  {
    Articulo2 p = (Articulo2) j.fromJson(json,Articulo2.class);

    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT a.ID_articulo,a.Nombre,a.Descripcion,a.Precio,a.Cantidad,a.Foto,b.Cantidad FROM Articulos a INNER JOIN Carrito b ON a.ID_articulo = b.ID_articulo;");
      try
      {
        ResultSet rs = stmt_1.executeQuery();
        ArrayList<Carrito> articulosArray = new ArrayList<>();
        try
        {
          if(rs!=null){
            while(rs.next()){
              Carrito r = new Carrito();
              r.ID = rs.getInt(1);
              r.Nombre = rs.getString(2);
              r.Descripcion = rs.getString(3);
              r.Precio = rs.getFloat(4);
              r.Cantidad = rs.getInt(5);
              r.foto = rs.getBytes(6);
              r.Cantidad2 = rs.getInt(7);
              articulosArray.add(r);
            }
            return Response.ok().entity(j.toJson(articulosArray)).build();
          }else{
            return Response.status(400).entity(j.toJson(new Error("No hay articulos"))).build();
          }
        }
        finally{
          rs.close();
        }
      }
      finally{
        stmt_1.close();
      }
    }
    catch (Exception e){
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally{
      conexion.close();
    }
  }
  @POST
  @Path("borrar_articulo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response borrar(String json) throws Exception
  {
    Articulo2 p2 = (Articulo2) j.fromJson(json,Articulo2.class);


    Connection conexion = pool.getConnection();

    if (p2.ID == null || p2.ID.equals(""))
      return Response.status(400).entity(j.toJson(new Error("No contiene ID"))).build();
    if (p2.Cantidad == null || p2.Cantidad.equals(""))
      return Response.status(400).entity(j.toJson(new Error("No contiene cantidad"))).build();
    
    try
    {
      conexion.setAutoCommit(false);
      int auxCantidad;
      PreparedStatement query1 = conexion.prepareStatement("CALL borrarArticulo(?,?)");
      try{
        query1.setInt(1, p2.ID);
        query1.setInt(2, p2.Cantidad);
        ResultSet rs=query1.executeQuery();
        try {
          if(rs.next()){
            auxCantidad = rs.getInt(1);
            if(auxCantidad == 0){
              return Response.status(400).entity(j.toJson(new Error("Ocurrio un error"))).build();
            }
          }else{
            return Response.status(400).entity(j.toJson(new Error("No se realizo la accion"))).build();
          }
        } finally {
          rs.close();
        } 
      }
      finally{
        query1.close();
      }
      conexion.commit();
    }
    catch (Exception e)
    {
      conexion.rollback();
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
    }
    finally
    {
      conexion.setAutoCommit(true);
      conexion.close();
    }
    return Response.ok().build();
  }
}

