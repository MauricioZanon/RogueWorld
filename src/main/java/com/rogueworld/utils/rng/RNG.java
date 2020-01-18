package com.rogueworld.utils.rng;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import javafx.scene.paint.Color;

public class RNG extends Random {

    private static final long serialVersionUID = 1L;
    private static RNG instance;

    private RNG() {
    }

    /**
     * @return [primerValor-segundoValor)
     */
    public int nextInt(int primerValor, int segundoValor) {
        if (primerValor == segundoValor) {
            return primerValor;
        } else {
            return nextInt(segundoValor - primerValor) + primerValor;
        }
    }

    public float nextFloat(float maximo) {
        return nextFloat() * maximo;
    }

    /**
     * @return [primerValor-segundoValor)
     */
    public float nextFloat(float primerValor, float segundoValor) {
        if (primerValor == segundoValor) {
            return primerValor;
        } else {
            return nextFloat(segundoValor - primerValor) + primerValor;
        }
    }

    /**
     * @param mean:      valor base
     * @param variation: variación máxima desde el valor base
     * @return un entero entre (mean - variation) y (mean + variation) que tiende a
     *         quedarse cerca del valor de mean
     */
    public int nextGaussian(int mean, int variation) {
        float result;
        do {
            result = (float) (nextGaussian() * variation + mean);
        } while (result < (mean - variation) || result > (mean + variation));

        return Math.round(result);
    }

    /**
     * ========================================================================
     * ============================COLLECTIONS=================================
     * ========================================================================
     */

    public <T> T getRandom(T[] array) {
        return array[nextInt(array.length)];
    }

    public <T> T getRandom(T[][] array) {
        return array[nextInt(array.length)][nextInt(array[0].length)];
    }

    public <T> T getRandom(T[][] array, Predicate<T> cond) {
        Set<T> items = new HashSet<>();
        for (T[] subArray : array) {
            items.addAll(Arrays.asList(subArray));
        }
        return getRandom(items, cond);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRandom(Collection<T> collection) {
        if (collection.isEmpty())
            return null;
        int index = nextInt(collection.size());
        return (T) collection.toArray()[index];
    }

    public <T> T getRandom(Collection<T> collection, Predicate<T> cond) {
        while (!collection.isEmpty()) {
            T t = getRandom(collection);
            if (cond.test(t)) {
                return t;
            } else {
                collection.remove(t);
            }
        }
        return null;
    }

    public Color getAproximateColor(Color baseColor) { //TODO: test
		int r = (int) (baseColor.getRed()*255);
		int g = (int) (baseColor.getGreen()*255);
		int b = (int) (baseColor.getBlue()*255);
		
		int mean = (int) (255*0.01); //TODO el mean debe ser un porcentaje de cada valor rgb
		r += nextInt(-mean, mean);
		g += nextInt(-mean, mean);
		b += nextInt(-mean, mean);
		
		if(r < 0) r = 0;
		else if (r > 255) r = 255;
		if(g < 0) g = 0;
		else if (g > 255) g = 255;
		if(b < 0) b = 0;
		else if (b > 255) b = 255;
		
		return Color.rgb(r, g, b);
	}

    public static RNG getInstance() {
        if (instance == null) {
            instance = new RNG();
        }
        return instance;
    }

}