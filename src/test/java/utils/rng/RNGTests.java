package utils.rng;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rogueworld.utils.rng.RNG;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class RNGTests {

    private RNG rng = RNG.getInstance();

    @RepeatedTest(10)
    public void nextInt_conDosValoresValidos_devuelveIntEntreLosDosValores() {
        int primerValor = 10;
        int segundoValor = 15;

        int valorResultante = rng.nextInt(primerValor, segundoValor);
        assertTrue(valorResultante >= primerValor && valorResultante < segundoValor);
    }

    @Test
    public void nextInt_conDosValoresIguales_devuelveValor() {
        int valor = 10;

        int valorResultante = rng.nextInt(valor, valor);
        assertTrue(valorResultante == valor);
    }

    @RepeatedTest(10)
    public void nextFloat_conValorValido_devuelveFloatEntreCeroYElValorDado() {
        float maximo = 5.5f;

        float valorResultante = rng.nextFloat(maximo);

        assertTrue(valorResultante >= 0 && valorResultante < maximo);
    }

    @RepeatedTest(10)
    public void nextFloat_conDosValoresValidos_devuelveFloatEntreLosDosValores() {
        float primerValor = 0.5f;
        float segundoValor = 5.5f;

        float valorResultante = rng.nextFloat(primerValor, segundoValor);
        assertTrue(valorResultante >= primerValor && valorResultante < segundoValor);
    }

    @Test
    public void nextFloat_conDosValoresIguales_devuelveValor() {
        float valor = 5.5f;

        float valorResultante = rng.nextFloat(valor, valor);
        assertTrue(valorResultante == valor);
    }

}