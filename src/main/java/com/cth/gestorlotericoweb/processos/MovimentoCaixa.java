/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.processos;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.estoque.Itens;
import com.cth.gestorlotericoweb.estoque.MovimentosEstoque;
import com.cth.gestorlotericoweb.operador.Operacoes;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.utils.Parser;
import com.cth.gestorlotericoweb.utils.MyPreparedStatement;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class MovimentoCaixa extends Processos{
    Integer idTerminal;
    public Integer idFuncionario;
    Integer idCofre;
    Timestamp dataHoraMov;
    BigDecimal valorMovimentado;
    Integer tipoOperacao;
    Integer tipoOperacaoAnt;
    String observacoes;
    
    Integer tipoMoeda;
    Integer idOperacaoFuncionario;
    
    public MovimentoCaixa(HttpServletRequest request) {
        super(request);
        setMovimentosCaixas();
    }
    
    public MovimentoCaixa(HttpServletRequest request, Operacoes operacoes){
        super(request);
        if (operacoes.getTipoOperacaoCaixa()==1) {
            this.tipoOperacao = 2;
        }else {
            this.tipoOperacao = 1;
        }
        this.idTerminal = operacoes.getIdTerminal();
        this.idFuncionario = operacoes.getIdFuncionario();
        AberturaTerminal aberturaTerminal = new AberturaTerminal(request);
        this.idCofre = Parser.toIntegerNull(aberturaTerminal.getIdCofre());
    
        aberturaTerminal = new AberturaTerminal(operacoes.getIdAberturaTerminal(),request);
    
        String dataAbertura = new SimpleDateFormat("yyyy-MM-dd").format(aberturaTerminal.getDataAbertura());
        String hora = new SimpleDateFormat("hh:mm").format(new java.util.Date());
    
        this.dataHoraMov = Parser.toDbTimeStamp(dataAbertura+hora);
        
        this.valorMovimentado = operacoes.getValorMovimentado();
    
        Itens itens = new Itens(request);
        
        this.tipoMoeda = itens.getIdItensEstoque(operacoes.getEdicaoItem(),false);
        this.observacoes = "Movimento de caixa gerado automaticamente pela rotina de Operações do funcionário";
        this.idOperacaoFuncionario = operacoes.getId();
        this.id = getIdMovimentoCaixa(operacoes);
    }
    
    public MovimentoCaixa(HttpServletRequest request, ResultSet rs) throws SQLException {
        super(request);
        tipoOperacao = rs.getInt(1);
        idTerminal = rs.getInt(2);
        idFuncionario = rs.getInt(3);
        dataHoraMov = rs.getTimestamp(4);
        valorMovimentado = rs.getBigDecimal(5);
        observacoes = rs.getString(6);
        idCofre = rs.getInt(7);
        tipoMoeda = rs.getInt(8);
    }
    
    private void setMovimentosCaixas() {
        this.id = Parser.toInteger(request.getParameter("id"));
        this.idTerminal = Parser.toIntegerNull(request.getParameter("id_terminal"));
        this.idFuncionario = Parser.toIntegerNull(request.getParameter("id_funcionario"));
        this.idCofre = Parser.toIntegerNull(request.getParameter("id_cofre"));
        this.dataHoraMov = Parser.toDbTimeStamp(request.getParameter("data_hora_mov"));
        this.valorMovimentado = Parser.toBigDecimalFromHtml(request.getParameter("valor_movimentado"));
        this.tipoOperacao = Parser.toIntegerNull(request.getParameter("tipo_operacao_caixa"));
        this.observacoes = request.getParameter("observacoes");
        this.tipoMoeda = Parser.toIntegerNull(request.getParameter("tipo_moeda"));
    }
    
    public MovimentoCaixa(Integer id,HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    public void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
                    "       valor_movimentado, observacoes, id_cofre, tipo_moeda \n" +
                    "  FROM public.movimentos_caixas where id = ? and id_entidade = ? ",false);
            ps.setInt(1, id);
            ps.setInt(2, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tipoOperacao = rs.getInt(1);
                idTerminal = rs.getInt(2);
                idFuncionario = rs.getInt(3);
                dataHoraMov = rs.getTimestamp(4);
                valorMovimentado = rs.getBigDecimal(5);
                observacoes = rs.getString(6);
                idCofre = rs.getInt(7);
                tipoMoeda = rs.getInt(8);
            }else{
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void deleta(){
        try{
            PreparedStatement psIdMovCofre = Parametros.getConexao().getPst("SELECT id FROM movimentos_cofres WHERE id_movimento_caixa = ? and id_entidade = ? ",false);
            psIdMovCofre.setInt(1, id);
            psIdMovCofre.setInt(2, Parametros.idEntidade);
            ResultSet rs = psIdMovCofre.executeQuery();
            while (rs.next()) {     
                MovimentoCofre movimentoCofre = new  MovimentoCofre(rs.getInt(1), request);
                movimentoCofre.deleta();          
            }
            MovimentosEstoque movimentosEstoque = new MovimentosEstoque(this);
            movimentosEstoque.deleta();
            PreparedStatement ps = Parametros.getConexao(request).getPst("delete from movimentos_caixas where id = ? and id_entidade = ?", Boolean.FALSE);     
            ps.setInt(1, this.id);
            ps.setInt(2, Parametros.idEntidade);
            ps.execute();
        }catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public List<MovimentoCaixa> getDadosPorTerminalFuncionarioData(){
        try {
            List<MovimentoCaixa> movimentoCaixaList = new ArrayList<>();
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
                    "       valor_movimentado, observacoes, id_cofre, tipo_moeda \n" +
                    "  FROM public.movimentos_caixas where id_terminal = ? and id_funcionario = ? and date( data_hora_mov) = ?  and id_entidade = ? ",false);
            ps.setInt(1, Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2, Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setDate(3, Parser.toDbDate(request.getParameter("data_movs")));
            ps.setInt(4, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                MovimentoCaixa mc = new MovimentoCaixa(request,rs);
                movimentoCaixaList.add(mc);
            }
            return movimentoCaixaList;
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        return new ArrayList<>();
    }
    
    public void gravaAutoMov(){
        if (this.id == null) {
            this.id = 0;
        }
        if (this.id>0) {
            altera();
        }else {
            insere();
        }
    }
    
    public void insere(){
        try {
            //language=PostgresPLSQL
            String sql ="INSERT INTO public.movimentos_caixas(\n" +
"            tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, \n" +
"            valor_movimentado, observacoes,id_cofre, tipo_moeda,id_operacao_funcionario, id_entidade)\n" +
"    VALUES (?, ?, ?, ?, \n" +
"            ?, ?, ?, ?, ?, ?);";
            MyPreparedStatement ps = new MyPreparedStatement(sql,request);
            ps.set( Integer.valueOf(tipoOperacao));
            ps.set(idTerminal);
            ps.set(Integer.valueOf(idFuncionario));
            ps.set( dataHoraMov);
            ps.set( valorMovimentado);
            ps.set(observacoes);
            ps.set(Integer.valueOf(idCofre));
            ps.set(tipoMoeda);
            ps.set(idOperacaoFuncionario);
            ps.set(Parametros.idEntidade);
            
            ps.getPst().execute();
            ResultSet rs = ps.getPst().getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
    
                gravaOutrosMovimentos();
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void altera(){
        //String idL = request.getParameter("id");
        try {
            //language=PostgresPLSQL
            String sql = ("UPDATE public.movimentos_caixas\n" +
            "   SET tipo_operacao_caixa=?, id_terminal=?, id_funcionario=?, data_hora_mov=?,\n" +
            "    valor_movimentado=?, observacoes=?, id_cofre=?, tipo_moeda = ?, id_operacao_funcionario = ? \n"
                    + " where id = ? and id_entidade = ? ");
            MyPreparedStatement ps = new MyPreparedStatement(sql,request);
            
            ps.set( Integer.valueOf(tipoOperacao));
            ps.set(idTerminal);
            ps.set(Integer.valueOf(idFuncionario));
            ps.set( dataHoraMov);
            ps.set( valorMovimentado);
            ps.set(observacoes);
            ps.set(Integer.valueOf(idCofre));
            ps.set(tipoMoeda);
            ps.set(idOperacaoFuncionario);
            MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
            movimentoCaixa.id = id;
            movimentoCaixa.getDados();
            tipoOperacaoAnt = movimentoCaixa.tipoOperacao;
            ps.set( id);
            ps.set(Parametros.idEntidade);
            
            ps.getPst().execute();
            gravaOutrosMovimentos();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void gravaOutrosMovimentos(){
        if(this.idCofre!=null){
            try{
                MovimentoCofre movimentoCofre = new MovimentoCofre(request, this);
                movimentoCofre.gravaAutoMov();
                if (tipoMoeda!= null) {
                    if (tipoMoeda>0) {
                        MovimentosEstoque movimentosEstoque = new MovimentosEstoque(this);
                        movimentosEstoque.gravaAutoMov();
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/movimentos_caixas.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());
        Operacoes operacoes = new Operacoes(request);
        operacoes.setTipoItem(5);
        contextConteudo.put("opts_padrao", operacoes.getOptsEdicaoItem());
        setOpcoesFiltro("movimentos_caixas");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"movimentos_caixas").toString());
        return contextPrinc;
    }
    
    public Integer getTipoOperacao() {
        return tipoOperacao;
    }
    
    public BigDecimal getValorMovimentado() {
        return valorMovimentado;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public Integer getTipoMoeda() {
        return tipoMoeda;
    }
    
    public Integer getTipoOperacaoAnt() {
        return tipoOperacaoAnt;
    }
    
    private Integer getIdMovimentoCaixa(Operacoes operacoes){
        String sql = "SELECT id FROM movimentos_caixas where id_operacao_funcionario = ? AND id_entidade = ?";
        try {
            MyPreparedStatement ps = new MyPreparedStatement(sql,request,false);
            ps.set(operacoes.getId());
            ps.set(Parametros.idEntidade);
            ResultSet rs = ps.getPst().executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            new LogError(e.getMessage(),e,request);
        }
        return null;
    }
}
