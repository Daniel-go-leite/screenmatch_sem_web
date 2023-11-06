package br.com.daniel.screenmatch_serie.service;

public interface IConveteDados {
    <T> T obeterDados(String json, Class<T> classe);
}
