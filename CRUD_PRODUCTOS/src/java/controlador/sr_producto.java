/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.Marca;

@MultipartConfig // Needed to handle file uploads
public class sr_producto extends HttpServlet {

    Marca marca;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                String producto = request.getParameter("txt_producto");
                int idMarca = Integer.parseInt(request.getParameter("drop_marca"));
                String descripcion = request.getParameter("txt_descripcion");

                // Procesamiento de la imagen subida
                Part filePart = request.getPart("txt_imagen");
                String imagenPath = request.getParameter("current_image_path"); // Ruta de imagen actual por defecto


                if (filePart != null && filePart.getSize() > 0) {
                    
                    
                     String localUploadPath = "C:\\Program Files\\NetBeans-22\\CRUD_PRODUCTOS\\Imagenes";
                    File localUploadDir = new File(localUploadPath);
                    if (!localUploadDir.exists()) {
                        localUploadDir.mkdir(); // Crea la carpeta si no existe
                    }
                    
                    // Define la ruta de la carpeta de subida
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                   
                    String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
                    imagenPath = "uploads" + File.separator + fileName;

                       // Guarda el archivo en la ubicación especificada
                    File file = new File(uploadPath + File.separator + fileName);
                    try (InputStream fileContent = filePart.getInputStream()) {
                        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                

                double precioCosto = Double.parseDouble(request.getParameter("txt_precio_costo"));
                double precioVenta = Double.parseDouble(request.getParameter("txt_precio_venta"));
                int existencia = Integer.parseInt(request.getParameter("txt_existencia"));
                String fechaIngreso = request.getParameter("txt_fecha_ingreso");
                int id = Integer.parseInt(request.getParameter("txt_id")); // Get ID

                // Create Marca instance
                marca = new Marca(producto, idMarca, id, existencia, descripcion, imagenPath, fechaIngreso, precioCosto, precioVenta);

               
                if (request.getParameter("btn_agregar") != null) {
                    agregarProducto(response, out);
                } else if (request.getParameter("btn_modificar") != null) {
                    modificarProducto(response, out);
                } else if (request.getParameter("btn_eliminar") != null) {
                    eliminarProducto(response, out);
                } else {
                    out.println("<h1>Acción no válida.</h1>");
                    out.println("<a href='index.jsp'>Regresar</a>");
                }

            } catch (NumberFormatException e) {
                out.println("<h1>Error en los parámetros: " + e.getMessage() + "</h1>");
                out.println("<a href='index.jsp'>Regresar</a>");
            }
        }
    }
  
    private void agregarProducto(HttpServletResponse response, PrintWriter out) throws IOException {
        try {
            if (marca.agregar() > 0) {
                response.sendRedirect("index.jsp");
            } else {
                out.println("<h1>Error al agregar producto.</h1>");
                out.println("<a href='index.jsp'>Regresar</a>");
            }
        } catch (Exception e) {
            out.println("<h1>Error al agregar producto: " + e.getMessage() + "</h1>");
            out.println("<a href='index.jsp'>Regresar</a>");
        }
    }

    private void modificarProducto(HttpServletResponse response, PrintWriter out) throws IOException {
        try {
            if (marca.modificar() > 0) {
                response.sendRedirect("index.jsp");
            } else {
                out.println("<h1>Error al modificar producto.</h1>");
                out.println("<a href='index.jsp'>Regresar</a>");
            }
        } catch (Exception e) {
            out.println("<h1>Error al modificar producto: " + e.getMessage() + "</h1>");
            out.println("<a href='index.jsp'>Regresar</a>");
        }
    }

    private void eliminarProducto(HttpServletResponse response, PrintWriter out) throws IOException {
        try {
            if (marca.eliminar() > 0) {
                response.sendRedirect("index.jsp");
            } else {
                out.println("<h1>Error al eliminar producto.</h1>");
                out.println("<a href='index.jsp'>Regresar</a>");
            }
        } catch (Exception e) {
            out.println("<h1>Error al eliminar producto: " + e.getMessage() + "</h1>");
            out.println("<a href='index.jsp'>Regresar</a>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestión de productos";
    }
}
