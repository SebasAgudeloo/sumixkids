package com.sumixkids;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Programa de prueba muy simple.
 * No forma parte del sitio web; sólo imprime un mensaje si se ejecuta como aplicación normal.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("Aplicación web SumixKids: este main sólo es de prueba.");
    }
}