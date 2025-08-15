/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author Hp
 */
public class Validador {

    // Patrones comunes
    private static final Pattern PATRON_CODIGO_EVENTO = Pattern.compile("^EVT-\\d{3}$");
    private static final Pattern PATRON_CODIGO_ACTIVIDAD = Pattern.compile("^ACT-\\d{3}$");
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[A-Za-z0-9+_%+\\-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Validaciones genéricas
    public static void validarNoVacio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " no puede estar vacío");
        }
    }

    public static void validarLongitudMaxima(String valor, int maximo, String campo) {
        if (valor != null && valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " no puede exceder los " + maximo + " caracteres");
        }
    }

    public static void validarNumeroPositivo(Number numero, String campo) {
        if (numero == null || numero.doubleValue() <= 0) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número positivo");
        }
    }

    // Validaciones específicas
    public static void validarCodigoEvento(String codigo) {
        validarNoVacio(codigo, "código de evento");
        if (!PATRON_CODIGO_EVENTO.matcher(codigo).matches()) {
            throw new IllegalArgumentException("El código de evento debe tener el formato EVT-XXX");
        }
    }

    public static void validarCodigoActividad(String codigo) {
        validarNoVacio(codigo, "código de actividad");
        if (!PATRON_CODIGO_ACTIVIDAD.matcher(codigo).matches()) {
            throw new IllegalArgumentException("El código de actividad debe tener el formato ACT-XXX");
        }
    }

    public static void validarFecha(String fechaStr, String formato) {
        validarNoVacio(fechaStr, "fecha");
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        sdf.setLenient(false);

        try {
            sdf.parse(fechaStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Debe ser " + formato);
        }
    }

    public static void validarEmail(String email) {
        validarNoVacio(email, "correo electrónico");
        if (!PATRON_EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido");
        }
    }

    public static <T extends Enum<T>> void validarEnumerado(Class<T> enumType, String valor, String campo) {
        validarNoVacio(valor, campo);
        try {
            Enum.valueOf(enumType, valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor inválido para " + campo + ". Valores permitidos: "
                    + String.join(", ", obtenerValoresEnumerado(enumType)));
        }
    }

    private static <T extends Enum<T>> String[] obtenerValoresEnumerado(Class<T> enumType) {
        T[] valores = enumType.getEnumConstants();
        String[] nombres = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            nombres[i] = valores[i].name();
        }
        return nombres;
    }
    
    public static void validarFechaFutura(String fechaStr, String formato) {
        validarFecha(fechaStr, formato);

        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        try {
            Date fecha = sdf.parse(fechaStr);
            if (fecha.before(new Date())) {
                throw new IllegalArgumentException("La fecha no puede ser anterior a la fecha actual");
            }
        } catch (ParseException e) {
            // No debería ocurrir porque ya se validó antes
            throw new RuntimeException("Error inesperado al validar fecha", e);
        }
    }
    
}
