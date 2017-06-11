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
import com.cth.gestorlotericoweb.utils.Seter;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosEduardo
 */
public class OutrosMovimentos extends Processos{
    Integer idTerminal;
    Integer idFuncionario;
    Integer idOperacaoFuncionario;
    Integer quantidade;
    Integer idEdicaoItem;
    
    
    Boolean entrada;
    
    Timestamp dataHoraMov;
    BigDecimal valorMovimentado;
    Integer tipoOperacao;
    Integer tipoOperacaoAnt;
    String observacoes;
    
    Operacoes operacoes;
    
    public OutrosMovimentos(HttpServletRequest request) {
        super(request);
        setOutrosMovimentos();
    }
    
    public OutrosMovimentos(HttpServletRequest request,ResultSet rs) throws SQLException {
        super(request);
        tipoOperacao = rs.getInt(1);
        idTerminal = rs.getInt(2);
        idFuncionario = rs.getInt(3);
        dataHoraMov = rs.getTimestamp(4);
        valorMovimentado = rs.getBigDecimal(5);
        observacoes = rs.getString(6);
    }
    
    public OutrosMovimentos(HttpServletRequest request, Operacoes operacoes){
        super(request);
        this.operacoes = operacoes;
        switch (operacoes.getTipoItem()){
            //1 BOLÃO
            //2 Bilhete
            //4 Tele Sena
            //6 Outros
            case 1:
                switch (operacoes.getTipoOperacaoCaixa()){
                    //1 Entrada >> 2
                    //2 Geração >> 8
                    //3 Saída >> 1
                    //4 Venda >> 9
                    case 1:
                        this.tipoOperacao = 2;
                        this.entrada = true;
                        break;
                    case 2:
                        this.tipoOperacao = 8;
                        this.entrada = true;
                        break;
                    case 3:
                        this.tipoOperacao = 1;
                        this.entrada = false;
                        break;
                    case 4:
                        this.tipoOperacao = 9;
                        this.entrada = false;
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (operacoes.getTipoOperacaoCaixa()){
                    //1 Entrada >> 12
                    //3 Saída >> 11
                    //4 Venda >> 3
                    case 1:
                        this.tipoOperacao = 12;
                        this.entrada = true;
                        break;
                    case 3:
                        this.tipoOperacao = 11;
                        this.entrada = false;
                        break;
                    case 4:
                        this.tipoOperacao = 3;
                        this.entrada = false;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        this.idTerminal = operacoes.getIdTerminal();
        this.idFuncionario = operacoes.getIdFuncionario();
        this.dataHoraMov = new Timestamp(new java.util.Date().getTime());
        this.valorMovimentado = operacoes.getValorMovimentado();
        this.idEdicaoItem = operacoes.getEdicaoItem();
        this.quantidade = operacoes.getQuantidade();
        Itens itens = new Itens(request);
        itens.getIdItensEstoque(idEdicaoItem,operacoes.getTipoItem() == 1);
        
        if (this.quantidade!=null){
            if(this.quantidade>1){
                this.valorMovimentado = this.valorMovimentado.multiply(new BigDecimal(quantidade.toString()));
                if (itens.getValorPadrao()!=null) {
                    if (valorMovimentado.compareTo(BigDecimal.ZERO)==0) {
                        this.valorMovimentado = itens.getValorPadrao().multiply(new BigDecimal(quantidade.toString()));
                    }
                }
            }
        }
        this.idOperacaoFuncionario = operacoes.getId();
        this.observacoes = "Movimento gerado automaticamente pela tela de operações do funcionário";
    }
    
    private void setOutrosMovimentos() {
        this.idTerminal = Parser.toIntegerNull(request.getParameter("id_terminal"));
        this.idFuncionario = Parser.toIntegerNull(request.getParameter("id_funcionario"));
        this.dataHoraMov = Parser.toDbTimeStamp(request.getParameter("data_hora_mov"));
        this.valorMovimentado = Parser.toBigDecimalFromHtml(request.getParameter("valor_movimentado"));
        this.tipoOperacao = Parser.toIntegerNull(request.getParameter("tipo_operacao_caixa"));
        this.observacoes = request.getParameter("observacoes");
    }
    
    public OutrosMovimentos(Integer id, HttpServletRequest request) {
        super(request, id);
        getDados();
    }
    
    public List<OutrosMovimentos> getDadosPorTerminalFuncionarioData(){
        List<OutrosMovimentos> outrosMovimentos = new ArrayList<>();
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
                    "       valor_movimentado, observacoes,id_operacao_funcionario\n" +
                    "  FROM public.outros_movimentos where id_terminal = ? and id_funcionario = ? and date( data_hora_mov) = ?  and  id_entidade = ? ORDER BY id",false);
            ps.setInt(1, Integer.valueOf(request.getParameter("id_terminal")));
            ps.setInt(2, Integer.valueOf(request.getParameter("id_funcionario")));
            ps.setDate(3, Parser.toDbDate(request.getParameter("data_movs")));
            ps.setInt(4, Parametros.idEntidade);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                OutrosMovimentos om = new OutrosMovimentos(request,rs);
                outrosMovimentos.add(om);
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
        return outrosMovimentos;
    }
    
    public void getDados(){
        try {
            PreparedStatement ps = Parametros.getConexao().getPst("SELECT  tipo_operacao_caixa,id_terminal, id_funcionario, data_hora_mov, \n" +
            "       valor_movimentado, observacoes, id_operacao_funcionario\n" +
            "  FROM public.outros_movimentos where id = ? and id_entidade = ? ",false);
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
                idOperacaoFuncionario = rs.getInt(7);
            }else{
                observacoes = "";
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public void insere(){
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("INSERT INTO public.outros_movimentos(\n" +
            "            tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, \n" +
            "            valor_movimentado, observacoes, id_entidade, id_operacao_funcionario)\n" +
            "    VALUES ( ?, ?, ?, ?, \n" +
            "            ?, ?, ?, ?);");
            ps.setInt(1, tipoOperacao);
            ps = Seter.set(ps,2,idTerminal);
            ps.setInt(3, idFuncionario);
            ps.setTimestamp(4, dataHoraMov);
            ps.setBigDecimal(5, valorMovimentado);
            ps = Seter.set(ps, 6, observacoes);
            ps.setInt(7, Parametros.idEntidade);
            ps = Seter.set(ps,8,idOperacaoFuncionario);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
                gravaOutrosMovimentos();
            }
            
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
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
    
    public void gravaOutrosMovimentos(){
        MovimentosEstoque movimentosEstoque = new MovimentosEstoque(request,this);
        movimentosEstoque.gravaAutoMov();
    }
    
    public void altera(){
        String idL = request.getParameter("id");
        try {
            PreparedStatement ps = Parametros.getConexao(request).getPst("UPDATE public.outros_movimentos\n" +
            "   SET tipo_operacao_caixa=?, id_terminal=?, id_funcionario=?, data_hora_mov=?,\n" +
            "    valor_movimentado=?, observacoes=?, id_operacao_funcionario = ?\n"
                    + " where id = ? and id_entidade = ? ", false);
            ps.setInt(1, tipoOperacao);
            ps = Seter.set(ps,2,idTerminal);
            ps.setInt(3, idFuncionario);
            ps.setTimestamp(4, dataHoraMov);
            ps.setBigDecimal(5,valorMovimentado);
            ps = Seter.set(ps, 6, observacoes);
            ps = Seter.set(ps, 7, idOperacaoFuncionario);
            id = Integer.valueOf(idL);
            ps.setInt(8, id);
            ps.setInt(9, Parametros.idEntidade);
    
    
            OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request);
            outrosMovimentos.id = id;
            outrosMovimentos.getDados();
            tipoOperacaoAnt = outrosMovimentos.tipoOperacao;
            
            ps.execute();
            gravaOutrosMovimentos();
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    public VelocityContext getHtml(VelocityContext contextPrinc,VelocityEngine ve,String idS){        
        Template templateConteudo;
        VelocityContext contextConteudo;
        StringWriter writerConteudo;
        templateConteudo = ve.getTemplate( "templates/Modern/processos/outros_movimentos.html" , "UTF-8");
        contextConteudo = new VelocityContext();
        writerConteudo = new StringWriter();
        contextConteudo.put("btns_percorrer",getSWBotoesPercorrer(ve).toString());       
        setOpcoesFiltro("outros_movimentos");
        templateConteudo.merge( contextConteudo, writerConteudo );
        contextPrinc.put("conteudo", writerConteudo.toString());
        contextPrinc.put("popup", getSWPopup(ve,"outros_movimentos").toString());
        return contextPrinc;
    }
    
    public BigDecimal getValorMovimentado() {
        return valorMovimentado;
    }
    
    public Integer getTipoOperacao() {
        return tipoOperacao;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public Integer getIdFuncionario() {
        return idFuncionario;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public Boolean getEntrada() {
        return entrada;
    }
    
    public Timestamp getDataHoraMov() {
        return dataHoraMov;
    }
    
    public Integer getIdEdicaoItem() {
        return idEdicaoItem;
    }
    
    public void setIdEdicaoItem(Integer idEdicaoItem) {
        this.idEdicaoItem = idEdicaoItem;
        if (operacoes != null) {
            operacoes.setEdicaoItem(idEdicaoItem);
        }
    }
    
    public Integer getTipoOperacaoAnt() {
        return tipoOperacaoAnt;
    }
    
    public Operacoes getOperacoes() {
        return operacoes;
    }
    
    public void setOperacoes(Operacoes operacoes) {
        this.operacoes = operacoes;
    }
}
