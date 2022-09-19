package br.unigran.listatelefonicabd;

public class Contato {
    private Integer id;
    private String nome;
    private String telefone;
    private String dataNasc;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getDataNasc() { return dataNasc; }

    public void setDataNasc(String dataNasc) { this.dataNasc = dataNasc; }

    @Override
    public String toString() {
        return nome + " " + telefone + " " + dataNasc;
    }
}
