


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



/**
 *
 * @author jdavi
 */
@WebServlet(name = "ReservaServlet", urlPatterns = {"/ReservaServlet"})
public class ReservaServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReservaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReservaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");

    // Obtener parámetros del formulario
    String nombre = request.getParameter("nombre_sala");
    String fechaStr = request.getParameter("fecha"); // Fecha en formato "Mar 22, 2025"
    String hora = request.getParameter("hora_inicio");
    String idSala = request.getParameter("id_se");

    System.out.println("Datos recibidos:");
    System.out.println("Nombre: " + nombre);
    System.out.println("Fecha (original): " + fechaStr);
    System.out.println("Hora: " + hora);
    System.out.println("ID Sala: " + idSala);

    // **Conversión de fecha al formato MySQL (YYYY-MM-DD)**
    String fechaConvertida = null;
    try {
        // Definir el formato de entrada que llega desde el formulario
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", java.util.Locale.ENGLISH);

        // Definir el formato de salida compatible con MySQL (YYYY-MM-DD)
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convertir la fecha de entrada al formato deseado
        LocalDate fecha = LocalDate.parse(fechaStr, inputFormatter);
        fechaConvertida = fecha.format(outputFormatter);

        System.out.println("Fecha convertida a formato MySQL: " + fechaConvertida);
    } catch (DateTimeParseException e) {
        System.out.println("Error al convertir la fecha: " + fechaStr);
        e.printStackTrace();
        response.getWriter().println("<h2>Error: Formato de fecha incorrecto</h2>");
        return; // Detener la ejecución si hay un error
    }

    // Datos de conexión a la base de datos
    String url = "jdbc:mysql://localhost:3308/library"; 
    String usuario = "root"; 
    String password = "darkmaster24"; 

    try (PrintWriter out = response.getWriter()) {
        // Cargar el driver de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver cargado correctamente");

        // Establecer conexión
        try (Connection conn = DriverManager.getConnection(url, usuario, password)) {
            System.out.println("Conexión a MySQL establecida");

            // Consulta SQL para insertar los datos
            String sql = "INSERT INTO reservas (nombre_sala, fecha, hora_inicio, id_sala) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, fechaConvertida); // Ahora la fecha tiene el formato correcto
                stmt.setString(3, hora);
                stmt.setInt(4, Integer.parseInt(idSala));

                int filasInsertadas = stmt.executeUpdate();
                if (filasInsertadas > 0) {
                    out.println("<h2>Reserva guardada exitosamente</h2>");
                    System.out.println("Reserva insertada correctamente");
                } else {
                    out.println("<h2>Error al guardar la reserva</h2>");
                    System.out.println("No se insertó ninguna fila");
                }
            }
        }
    } catch (ClassNotFoundException e) {
        System.out.println("Error: No se encontró el driver JDBC");
        e.printStackTrace();
    } catch (SQLException e) {
        System.out.println("Error en la conexión a la base de datos");
        e.printStackTrace();
    }
}





    
    
    
    
    
    
    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
