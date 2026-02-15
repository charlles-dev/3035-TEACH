package Orientacao_a_Objeto;

class Carro {
    String marca;
    String modelo;
    int ano;

    public Carro(String marca, String modelo, int ano) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }

    public String getDescricao() {
        return "Marca: " + marca + ", Modelo: " + modelo + ", Ano: " + ano;
    }
}

public class atividade_01 {
    static void main() {
        Carro meuCarro = new Carro("Toyota", "Corolla", 2022);
        System.out.println(meuCarro.getDescricao());
    }
}
