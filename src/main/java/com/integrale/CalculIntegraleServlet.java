
package com.integrale;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/calculer")
public class CalculIntegraleServlet extends HttpServlet 
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        afficherFormulaire(response, "", "-1", "1", "1000", null, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        String fonction, aTexte, bTexte, nTexte, erreur;
        Double rectangle, trapeze;


        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        fonction = request.getParameter("fonction");
        aTexte = request.getParameter("borneA");
        bTexte = request.getParameter("borneB");
        nTexte = request.getParameter("subdivisions");

        erreur = null;
        rectangle = null;
        trapeze = null;

        if (fonction == null) 
        {
            fonction = "";
        }

        if (aTexte == null)
        {
            aTexte = "-1";
        }

        if (bTexte == null)
        {
            bTexte = "1";
        }

        if (nTexte == null)
        {
            nTexte = "1000";
        }

        try 
        {
                double a, b;
                int n;

                a = Double.parseDouble(aTexte);
                b = Double.parseDouble(bTexte);
            n = Integer.parseInt(nTexte);

            Integrateur integrateur = new Integrateur(fonction, a, b, n);

            rectangle = integrateur.surfaceRectangle();
            trapeze = integrateur.surfaceTrapeze();

        } 
        catch (NumberFormatException e) 
        {
            erreur = "Veuillez saisir des bornes numériques et un nombre entier de subdivisions.";
        } 
        catch (IllegalArgumentException e) 
        {
            erreur = e.getMessage();
        } 
        catch (Exception e) 
        {
            erreur = "Une erreur est survenue pendant le calcul.";
            getServletContext().log("Erreur de calcul intégral", e);
        }

        afficherFormulaire(response,
                fonction,
                aTexte,
                bTexte,
                nTexte,
                rectangle,
                trapeze,
                erreur
        );
    }

    private void afficherFormulaire(HttpServletResponse response, String fonction, String a, String b, String n, Double rectangle, Double trapeze) throws IOException 
    {
        afficherFormulaire(response, fonction, a, b, n, rectangle, trapeze, null);
    }

    private void afficherFormulaire(HttpServletResponse response, String fonction, String a, String b, String n, Double rectangle, Double trapeze, String erreur) throws IOException 
    {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html lang='fr'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Calcul d'intégrales</title>");

        out.println("<style>");
        out.println("body{font-family:Arial;max-width:800px;margin:40px auto;padding:20px;line-height:1.6}");
        out.println("input{padding:9px;width:100%;box-sizing:border-box;margin:5px 0 15px}");
        out.println("button{padding:10px 18px;cursor:pointer}");
        out.println(".resultat{padding:15px;border:1px solid #198754;border-radius:8px;margin-top:20px}");
        out.println(".erreur{padding:15px;border:1px solid #dc3545;border-radius:8px;margin-top:20px}");
        out.println("</style></head><body>");

        out.println("<h1>Calcul d'intégrales numériques</h1>");
        out.println("<form method='post' action='" + response.encodeURL("calculer") + "'>");

        out.println("<label>Fonction f(x)</label>");
        out.println("<input name='fonction' required value='" + html(fonction) + "' placeholder='x^2'>");

        out.println("<label>Borne A</label>");
        out.println("<input type='number' step='any' name='borneA' required value='" + html(a) + "'>");

        out.println("<label>Borne B</label>");
        out.println("<input type='number' step='any' name='borneB' required value='" + html(b) + "'>");

        out.println("<label>Nombre de subdivisions</label>");
        out.println("<input type='number' min='1' max='1000000' name='subdivisions' required value='" + html(n) + "'>");

        out.println("<button type='submit'>Calculer les intégrales</button>");
        out.println("</form>");

        if (erreur != null) 
        {
            out.println("<div class='erreur'><h2>Erreur</h2><p>" + html(erreur) + "</p></div>");
        }

        if (rectangle != null && trapeze != null) 
        {
            out.println("<section class='resultat'>");
            out.println("<h2>Résultats</h2>");
            out.println("<p>Fonction : f(x) = " + html(fonction) + "</p>");
            out.println("<p>Intervalle : [" + html(a) + ", " + html(b) + "]</p>");
            out.println("<p>Subdivisions : " + html(n) + "</p>");

            out.println("<p>Méthode des rectangles : <strong>" + String.format(Locale.US, "%.12g", rectangle) + "</strong></p>");

            out.println("<p>Méthode des trapèzes : <strong>" + String.format(Locale.US, "%.12g", trapeze) + "</strong></p>");

            out.println("</section>");
        }

        out.println("</body></html>");
    }

    private String html(String texte) 
    {
        if (texte == null) 
        {
            return "";
        }

        return texte.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
