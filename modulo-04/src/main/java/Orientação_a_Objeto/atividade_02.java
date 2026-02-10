package Orientação_a_Objeto;

class Animal {
    String nome;
    String som;

    public Animal(String nome, String som) {
        this.nome = nome;
        this.som = som;
    }

    public void fazerSom() {
        System.out.println(nome + " faz: " + som);
    }
}

class Cachorro extends Animal {
    public Cachorro(String nome) {
        super(nome, "Au Au");
    }

    @Override
    public void fazerSom() {
        System.out.println("O cachorro " + nome + " late: " + som);
    }
}

class Gato extends Animal {
    public Gato(String nome) {
        super(nome, "Miau");
    }

    @Override
    public void fazerSom() {
        System.out.println("O gato " + nome + " mia: " + som);
    }
}

public class atividade_02 {
    public static void main(String[] args) {
        Animal meuCachorro = new Cachorro("Rex");
        Animal meuGato = new Gato("Félix");

        meuCachorro.fazerSom();
        meuGato.fazerSom();
    }
}
