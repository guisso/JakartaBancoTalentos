/*
 * CC BY-NC-SA 4.0
 *
 * Copyright 2022 Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;.
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *
 * You are free to:
 *   Share - copy and redistribute the material in any medium or format
 *   Adapt - remix, transform, and build upon the material
 *
 * Under the following terms:
 *   Attribution - You must give appropriate credit, provide 
 *   a link to the license, and indicate if changes were made.
 *   You may do so in any reasonable manner, but not in any 
 *   way that suggests the licensor endorses you or your use.
 *   NonCommercial - You may not use the material for commercial purposes.
 *   ShareAlike - If you remix, transform, or build upon the 
 *   material, you must distribute your contributions under 
 *   the same license as the original.
 *   No additional restrictions - You may not apply legal 
 *   terms or technological measures that legally restrict 
 *   others from doing anything the license permits.
 *
 * Notices:
 *   You do not have to comply with the license for elements 
 *   of the material in the public domain or where your use 
 *   is permitted by an applicable exception or limitation.
 *   No warranties are given. The license may not give you 
 *   all of the permissions necessary for your intended use. 
 *   For example, other rights such as publicity, privacy, 
 *   or moral rights may limit how you use the material.
 */
package io.github.guisso.bancotalentos;

import io.github.guisso.bancotalentos.categoria.Categoria;
import io.github.guisso.bancotalentos.credencial.Perfil;
import io.github.guisso.bancotalentos.telefone.Telefone;
import io.github.guisso.bancotalentos.docente.Docente;
import io.github.guisso.bancotalentos.docente.DocenteBeanLocal;
import io.github.guisso.bancotalentos.discente.Discente;
import io.github.guisso.bancotalentos.credencial.Credencial;
import io.github.guisso.bancotalentos.credencial.CredencialBeanLocal;
import io.github.guisso.bancotalentos.discente.DiscenteBeanLocal;
import io.github.guisso.bancotalentos.habilidade.Habilidade;
import io.github.guisso.bancotalentos.proficiencia.Proficiencia;
import io.github.guisso.bancotalentos.projeto.Projeto;
import io.github.guisso.bancotalentos.util.Util;
import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 *
 * @author Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;
 */
@WebServlet(name = "SistemaTests",
        urlPatterns = {"/SistemaTests"})
//
// !!! Importante para recuperação de elementos LAZY na transação !!!
//
@Transactional
public class SistemaTests extends HttpServlet {

    @Inject
    private DocenteBeanLocal docenteBean;

    @Inject
    private CredencialBeanLocal credencialBean;
    
    @Inject
    private DiscenteBeanLocal discenteBean;

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
        try ( PrintWriter out = response.getWriter()) {
            
            // Discente 1
            Credencial c2 = new Credencial("beatriz@mail.com", "123456", Perfil.OPERADOR, true);

            Discente discente1 = new Discente(777888999L);
            discente1.setNome("Beatriz Yana");
            discente1.setTelefone(new Telefone((byte) 38, 33332222L));
            discente1.setCredencial(c2);
            
            // Projeto 1
            Projeto projeto1 = new Projeto("Aliança");
            projeto1.getDiscentes().add(discente1);
            discente1.getProjetos().add(projeto1);
            
            // Projeto 2
            Projeto projeto2 = new Projeto("Bistrô");
            projeto2.getDiscentes().add(discente1);
            discente1.getProjetos().add(projeto2);
            
            // Projeto 3
            Projeto projeto3 = new Projeto("Cachê");
            projeto3.getDiscentes().add(discente1);
            discente1.getProjetos().add(projeto3);
            
            // Habilidade 1
            Categoria cat1 = new Categoria("EngSoft");
            Habilidade hab1 = new Habilidade("SCRUM", null);
            hab1.setCategoria(cat1);
            
            Proficiencia prof1 = new Proficiencia(
                    Proficiencia.NivelProficiencia.INTERMEDIARIO, 
                    "Diversos papéis SCRUM em projetos de 2 meses");
            prof1.setHabilidade(hab1);
            
            discente1.getProficiencias().add(prof1);
            
            //Habilidad2 2 (mesma categoria)
            Habilidade hab2 = new Habilidade("TDD", "Desenvolvimento orientado por testes");
            hab2.setCategoria(cat1);
            
            Proficiencia prof2 = new Proficiencia(
                    Proficiencia.NivelProficiencia.AVANCADO, 
                    "Projetos de médio porte por 4 anos");
            prof2.setHabilidade(hab2);
            
            discente1.getProficiencias().add(prof2);
            
            // Discente 2
            Credencial c3 = new Credencial("cecilia@mail.com", "jklçjklç", Perfil.OPERADOR, false);
            
            Discente discente2 = new Discente(777888999L);
            discente2.setNome("Cecília Xerxes");
            discente2.setTelefone(new Telefone((byte) 38, 22224444L));
            discente2.setCredencial(c3);
            
            // Docente
            Credencial c1 = new Credencial("ana@mail.com", "asdf123", Perfil.GERENTE, true);

            Docente docente1 = new Docente(963852741L);
            docente1.setNome("Ana Zaira");
            docente1.setCredencial(c1);
            docente1.setTelefone(new Telefone((byte) 38, 912341234L));
            docente1.getOrientandos().add(discente1);
            docente1.getOrientandos().add(discente2);

            // Salvamento em cascata
            docenteBean.salvar(docente1);

            // Recuperação de dados
            Docente dAux = docenteBean.localizarPorId(1L);
            Credencial cAux = credencialBean.localizarPorId(1L);
            Discente disAux = discenteBean.localizarPorId(2L);

            // Saída de dados
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SistemaTests</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Processamento concluído</h1>");

            // Exibição dos objetos
            out.println("<p><pre>-- Docente --\n" 
                    + Util.toJson(dAux) + "</pre></p>");
            out.println("<p><pre>-- Credencial --\n" 
                    + Util.toJson(cAux) + "</pre></p>");
            out.println("<p><pre>-- Docente a partir da credencial --"
                    + Util.toJson(cAux.getUsuario()) + "</pre></p>");
            out.println("<p><pre>-- Discente --\n" 
                    + Util.toJson(disAux) + "</pre></p>");

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
        processRequest(request, response);
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
