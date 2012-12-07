import java.util.Scanner;
import java.util.Vector;

import jp.co.csk.vdm.toolbox.VDM.CGException;


public class Prompt {
   
    private Diagrama d;
    private int id;
    private static String tiposMensagem = " {message, action, asynchronous, link, destroy, call, create ou other}";
    private static String tiposObjeto = " {Actor, Unit, Object ou Other}";
   
    public Prompt(int limite, String titulo) throws CGException{
        d = new Diagrama(titulo, limite);
        id = 0;
    }
   
    private int gerarId(){
       
        id++;
        return id;
    }
   
    public void inserirObjeto(String nome, String tipo) throws CGException{
        int c=1;
        for(Objeto o: this.getListaObjetos()){
            if(o.nome.equals(nome)) c++;
        }
        if(c>1) nome += "("+c+")";
        d.inserirObjeto(new Objeto(nome, gerarId(), tipo));
       
    }
   
    public void inserirMensagem(String nome, String tipo, Objeto objOrigem, Objeto objDestino) throws CGException{
        d.inserirSegmento(new Mensagem(nome, gerarId(), tipo, objOrigem, objDestino));
    }
   
    public void inserirBlocoLoop(String nome,  Vector<Mensagem> loopSeq) throws CGException{
        d.inserirSegmento(new BlocoLoop(nome, gerarId(),  loopSeq));
    }
   
    public void inserirBlocoAlt(String nome,  Vector<Mensagem> thenSeq, Vector<Mensagem> elseSeq) throws CGException{
        d.inserirSegmento(new BlocoAlt(nome, gerarId(),  thenSeq, elseSeq));
    }
   
    public Vector<Segmento> getListaSegmentos(){
        Vector<Segmento> ret = new Vector<Segmento>();
        for(int i=0; i<d.segmentos.size(); i++){
            ret.add((Segmento) d.segmentos.get(i));
        }
        return ret;
    }
   
    public Vector<Objeto>getListaObjetos(){
       
        Vector<Objeto> ret = new Vector<Objeto>();
        for(int i=0; i<d.objetos.size(); i++){
            ret.add((Objeto) d.objetos.get(i));
        }
       
        return ret;  
    }
   
    public Vector<String> getDescSegmentos(){
        Vector<Segmento> segs = this.getListaSegmentos();
        Vector<String> segsDesc = new Vector<String>();
       
        for(Segmento s: segs){
            if(s instanceof Mensagem){
                Mensagem m = (Mensagem) s;
                segsDesc.add(descMensagem(m));
               
            }else if(s instanceof BlocoLoop){
                BlocoLoop bl = (BlocoLoop) s;
                String desc = "loop "+bl.nome+System.lineSeparator();
               
                for(int i=0; i<bl.loopSeq.size();i++){
                    Mensagem m = (Mensagem) bl.loopSeq.get(i);
                    desc+= descMensagem(m);
                    desc+=System.lineSeparator();
                }
               
                desc+="end";
                segsDesc.add(desc);
            }else{
                BlocoAlt ba = (BlocoAlt) s;
                String desc = "alt "+ ba.nome + System.lineSeparator();
               
                for(int i=0; i<ba.thenSeq.size();i++){
                    Mensagem m = (Mensagem) ba.thenSeq.get(i);
                    desc+= descMensagem(m);
                    desc+=System.lineSeparator();
                }
               
                desc+="else "+ System.lineSeparator();
               
                for(int i=0; i<ba.elseSeq.size();i++){
                    Mensagem m = (Mensagem) ba.elseSeq.get(i);
                    desc+=descMensagem(m);
                    desc+=System.lineSeparator();
                }
               
                desc+="end";
                segsDesc.add(desc);
            }
        }
       
        return segsDesc;
    }
   
    private String descMensagem(Mensagem m){
        return (m.objOrigem.nome + " -> "+m.objDestino.nome +": ("+m.tipo+") "+m.nome);
    }
   
    public Vector<String>getDescObjetos(){
        Vector<Objeto> objs = this.getListaObjetos();
        Vector<String> objsDesc = new Vector<String>();
       
        for (int i=0; i<objs.size(); i++){
            objsDesc.add(i+ " - " + objs.get(i).nome+ " "+objs.get(i).tipo);
        }
       
        return objsDesc;
       
    }
   
    public Vector<String> calcularSequencias() throws CGException{
        Vector<Vector<Segmento>> seqs = (Vector<Vector<Segmento>>) d.calcularSequencias();
        Vector<String> resp = new Vector<String>();
       
        for(Vector<Segmento> v1: seqs){
            String seg = "";
            for(Segmento s: v1){
                seg+=s.nome + " -> ";
            }
            seg+="fim";
            resp.add(seg);
        }
       
        return resp;
    }
   
    public void eliminarSegmento(int ord) throws CGException{
        this.d.eliminarSegmento(ord);
    }
   
    public void eliminarObjeto(int ord) throws CGException{
        this.d.eliminarObjeto(ord);
    }
   
    public void editarSegmento(int ord, String novoNome) throws CGException{
        this.d.editarSegmento(ord, novoNome);
    }
   
    public void editarObjeto(String novoNome, String novoTipo, int ord) throws CGException{
        this.d.editarObjeto(novoNome, novoTipo, ord);
    }
    public String menu(){
        return (
                 "1- Inserir Objeto"+ System.lineSeparator()
                +"2- Inserir Mensagem" + System.lineSeparator()
                +"3- Ver Sequencia"+ System.lineSeparator()
                +"4- Ver Objetos"+System.lineSeparator()
                +"5- Inserir Loop"+System.lineSeparator()
                +"6- Inserir Bloco Alternativo"+System.lineSeparator()
                +"7- Calcular Sequencias"+System.lineSeparator()
                +"8- Editar Objeto"+System.lineSeparator()
                +"9- Editar Segmento"+System.lineSeparator()
                +"10-Eliminar Objeto"+System.lineSeparator()
                +"11-Eliminar Segmento"+System.lineSeparator()
                );
    }
   

    public static void main(String args[]){
   
       
        Scanner in = new Scanner(System.in);
        String t;
        int limite;
        System.out.println("Qual o titulo do diagrama?");
        t = in.nextLine();
        System.out.println("Qual o limite?");
        limite = in.nextInt();
       
        Prompt p;
        try {
            p = new Prompt(limite, t);
       
       
        boolean exit = false;
        while(!exit){
            try {
            System.out.println("O que deseja?");
            System.out.println(p.menu());
           
            int e = in.nextInt();
            in = new Scanner(System.in);
            switch(e){
            case 1:
                System.out.println("Qual o nome?");
                String n = in.nextLine();
                System.out.println("Qual o tipo? "+ Prompt.tiposObjeto);
                String tipo = in.nextLine();
               
                p.inserirObjeto(n, tipo);
                break;
            case 2:
                System.out.println("Qual o nome?");
                String n2 = in.nextLine();
                System.out.println("Qual o tipo? " + Prompt.tiposMensagem);
                String tipo2 = in.nextLine();
                System.out.println("Qual objeto de Origem? "+ p.getDescObjetos());
                int e1 = in.nextInt();
                System.out.println("Qual objeto de Destino? ");
                int e2 = in.nextInt();
               
                p.inserirMensagem(n2, tipo2,p.getListaObjetos().get(e1), p.getListaObjetos().get(e2));
                break;
            case 3:
                Vector<String> resp = p.getDescSegmentos();
                String ss = "";
                for(String s : resp){
                    System.out.println(s);
                    ss+=s+System.lineSeparator();
                }
               
                new Main().getSequenceDiagram(ss, "out.png", "rose");
                new ShowImage("out.png").mostrarImagem(p.d.titulo);
               
                break;
            case 4:
                Vector<String> resp1 = p.getDescObjetos();
                for(String s: resp1){
                    System.out.println(s);
                }
                break;
            case 5:
               
                Vector<Mensagem> loopSeq = new Vector<Mensagem>();
                String resposta = "";
                System.out.println("Qual o nome do Loop? ");
                String nomeLoop = in.nextLine();
                while(!resposta.equalsIgnoreCase("sim")){
                   
                    System.out.println("Qual o nome da próxima Mensagem?");
                    String n3 = in.nextLine();
                    System.out.println("Qual o tipo? "+ Prompt.tiposMensagem);
                    String tipo3 = in.nextLine();
                    System.out.println("Qual objeto de Origem? "+ p.getDescObjetos());
                    int e3 = in.nextInt();
                    System.out.println("Qual objeto de Destino? ");
                    int e4 = in.nextInt();
                    in = new Scanner(System.in);
                    loopSeq.add(new Mensagem(n3, p.gerarId(), tipo3, p.getListaObjetos().get(e3), p.getListaObjetos().get(e4)));
                    System.out.println("Fim da sequencia? sim\nao");
                    resposta = in.nextLine();
                }
               
                p.inserirBlocoLoop(nomeLoop, loopSeq);
                break;
            case 6:
               
                Vector<Mensagem> Seq = new Vector<Mensagem>();
                Vector<Mensagem> thenSeq = new Vector<Mensagem>();
                Vector<Mensagem> elseSeq = new Vector<Mensagem>();
                String resposta1 = "";
                System.out.println("Qual o nome do Bloco Alternativo? ");
                String nomeBloco = in.nextLine();
                System.out.println("Primeira Sequência (if)");
                for(int i=0; i<2; i++){
                    resposta1 = "nao";
                    while(!resposta1.equalsIgnoreCase("sim")){
                       
                        System.out.println("Qual o nome da próxima Mensagem?");
                        String n3 = in.nextLine();
                        System.out.println("Qual o tipo? "+ Prompt.tiposMensagem);
                        String tipo3 = in.nextLine();
                        System.out.println("Qual objeto de Origem? "+ p.getDescObjetos());
                        int e3 = in.nextInt();
                        System.out.println("Qual objeto de Destino? ");
                        int e4 = in.nextInt();
                        in = new Scanner(System.in);
                        Seq.add(new Mensagem(n3, p.gerarId(), tipo3, p.getListaObjetos().get(e3), p.getListaObjetos().get(e4)));
                        System.out.println("Fim da sequencia? sim\nao");
                        resposta1 = in.nextLine();
                    }
                   
                    for (Mensagem m: Seq){
                        if(i==0) thenSeq.add(m);
                        else elseSeq.add(m);
                    }
                   
                    Seq = new Vector<Mensagem>();
                    if(i==0)System.out.println("Segunda Sequência (else)");
                }
               
                p.inserirBlocoAlt(nomeBloco, thenSeq, elseSeq);
                break;
            case 7:
                Vector<String> seqs = p.calcularSequencias();
                for(String s: seqs){
                    System.out.println(s);
                }
                break;
           
            case 8:
                for(String s: p.getDescObjetos()) System.out.println(s);
                System.out.println("Qual Objeto deseja editar?");
                int ord1 = in.nextInt() + 1;
                in = new Scanner(System.in);
                System.out.println("Qual novo nome?");
                String nomeO = in.nextLine();
                System.out.println("Qual novo tipo? "+ Prompt.tiposObjeto);
                String tipoO = in.nextLine();
               
                p.editarObjeto(nomeO, tipoO, ord1);
                break;
           
            case 9:
                for(String s: p.getDescSegmentos())  System.out.println(p.getDescSegmentos().indexOf(s)+" - "+s);
                System.out.println("Qual Segmento deseja editar?");
                int ord2 = in.nextInt()+1;
                in = new Scanner(System.in);
                System.out.println("Qual novo nome?");
                String nome2 = in.nextLine();
               
                p.editarSegmento(ord2, nome2);
               
                break;
            case 10:
                for(String s: p.getDescObjetos()) System.out.println(s);
                System.out.println("Qual Objeto deseja eliminar?");
                int ord3 = in.nextInt()+1;
                in = new Scanner(System.in);
               
                p.eliminarObjeto(ord3);
                break;
           
            case 11:
                for(String s: p.getDescSegmentos()) System.out.println(p.getDescSegmentos().indexOf(s)+" - "+s);
                System.out.println("Qual Objeto deseja eliminar?");
                int ord4 = in.nextInt()+1;
                in = new Scanner(System.in);
               
                p.eliminarSegmento(ord4);
                break;
            }
           
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println(e.getMessage());
            }
        }
       
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getLocalizedMessage());
        }
    }

}