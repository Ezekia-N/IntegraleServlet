
package com.integrale;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class Integrateur {

    private final String fonction;
    private final double a;
    private final double b;
    private final int n;

    public Integrateur(String fonction, double a, double b, int n) 
    {
        if (fonction == null || fonction.isBlank()) 
        {
            throw new IllegalArgumentException("Veuillez saisir une fonction.");
        }

        if (!Double.isFinite(a) || !Double.isFinite(b)) 
        {
            throw new IllegalArgumentException("Les bornes doivent être finies.");
        }

        if (a >= b) 
        {
            throw new IllegalArgumentException("La borne A doit être inférieure à la borne B.");
        }

        if (n < 1 || n > 1_000_000) 
        {
            throw new IllegalArgumentException("Le nombre de subdivisions doit être compris entre 1 et 1000000.");
        }

        this.fonction = fonction;
        this.a = a;
        this.b = b;
        this.n = n;
    }

    private double f(double x) 
    {
        Argument argumentX = new Argument("x = " + x);
        Expression expression = new Expression(fonction, argumentX);

        double valeur = expression.calculate();

        if (!Double.isFinite(valeur)) 
        {
            throw new IllegalArgumentException("Fonction non définie ou résultat non fini pour x = " + x);
        }

        return valeur;
    }

    public double surfaceRectangle() 
    {
        double h, surface;
        h = (b - a) / n;
        surface = 0.0;

        for (int i = 0; i < n; i++) 
        {
            double x = a + i * h;
            surface += f(x) * h;
        }

        return surface;
    }

    public double surfaceTrapeze() 
    {
        double h, surface;

        h = (b - a) / n;
        surface = (f(a) + f(b)) / 2.0;

        for (int i = 1; i < n; i++) 
        {
            double x = a + i * h;
            surface += f(x);
        }

        return surface * h;
    }

    public String getFonction() 
    {
        return fonction;
    }

    public double getA() 
    {
        return a;
    }

    public double getB() 
    {
        return b;
    }

    public int getN() 
    {
        return n;
    }
}
