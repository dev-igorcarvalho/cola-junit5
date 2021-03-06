package br.com.igorcarvalho.tests.examples.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @ExtendWith(MockitoExtension.class) permite que
 * o junit controle a criação de mocks e injections
 * a partir das anotações do mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito argument captor")
public class MockitoArgumentCaptor {

    /**
     * @Mock cria mocks de objetos simples
     * que geralmente vao ser usados como
     * dependencia de objetos mais complexos
     * que dependam deles
     */
    @Mock
    private FakeRepository repository;

    /**
     * Cria mocks de objetos que tenham
     * dependencias e injeta a dependencias
     * nesses objetos. As dependendencias precisam
     * estar anotados com @Mock
     */
    @InjectMocks
    private FakeService service;

    @Captor
    ArgumentCaptor<FakeEntity> entityCaptor;

    /**
     * ArgumentCaptor serve para capturar
     * os argumentos usados em um metodo que
     * nao se tem acesso direto pelo Sut
     * Pode ser criado inline dentro do proprio teste
     * ou criado de forma global via annotation.
     *
     * @Captor ArgumentCaptor<T> captor;
     */
    @Test
    public void inlineArgumentCaptorTest() {
        //given
        final FakeEntity entity = new FakeEntity(1l, "update");
        final ArgumentCaptor<FakeEntity> captor =
                ArgumentCaptor.forClass(FakeEntity.class);
        //when
        service.update(entity);
        //then
        assertAll(
                "Falhou em atualizar a entidade",
                () -> verify(repository).update(captor.capture()),
                () -> assertNotNull(captor.getValue().getId()),
                () -> assertEquals("update", captor.getValue().getNome()),
                () -> assertEquals(Long.valueOf(1), captor.getValue().getId())
        );
    }


    @Test
    public void annotationArgumentCaptorTest() {
        //given
        final FakeEntity entity = new FakeEntity(1l, "update");
        //when
        service.update(entity);
        //then
        assertAll(
                "Falhou em atualizar a entidade",
                () -> verify(repository).update(entityCaptor.capture()),
                () -> assertNotNull(entityCaptor.getValue().getId()),
                () -> assertEquals("update", entityCaptor.getValue().getNome()),
                () -> assertEquals(Long.valueOf(1), entityCaptor.getValue().getId())
        );
    }
}


