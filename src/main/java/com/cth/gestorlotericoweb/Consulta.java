/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb;

import com.cth.gestorlotericoweb.dados.ColunasTabelas;
import com.cth.gestorlotericoweb.parametros.Parametros;
import com.cth.gestorlotericoweb.processos.*;
import com.cth.gestorlotericoweb.utils.Parser;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CarlosEduardo
 */
public class Consulta {
    public Integer id;
    public String output;
    public Map<String,String> mapDados;
    HttpServletRequest request;

    public Consulta(Integer id) {
        this.id = id;
    }

    public Consulta(HttpServletRequest request) {
        this.request = request;
        setConsulta();
    }
    
    public void setConsulta(){
        switch(request.getParameter("tipo").trim()){
            case "busca":
                geraTabelaPopUp(request.getParameter("coluna_buscar"));
                break;
            case "dado":
                buscaDado();
                break;
            case "dados":
                geraTabelaDados();
                break;
            case "id":
                getId();
                break;
            case "nome":
                getNome();
                break;
            case "opt":
                getOpt();
                break;
            case "item_componente":
                getItemComponente();
                break;
            default:
                output ="definir o tipo de busca";
                break;
        }
    }
    
    private void getItemComponente(){
            String sql = "select nome_componente_sistema||';'||nome_coluna_id_componente from componentes where id = ? ";
            try {
                String id = request.getParameter("id");
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setInt(1, Integer.valueOf(id));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    output = rs.getString(1);
                }else{
                    output = "";
                }
            } catch (SQLException ex) {
                output = ex.getMessage();
                new LogError(ex.getMessage(), ex,request);
            } catch (Exception ex) {
                new LogError(ex.getMessage(), ex,request);
            }
    }
    
    private void getNome(){
        try {
            ColunasTabelas colunasTabelas = new ColunasTabelas(request);
            String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
            if (tabela != null) {
                String id = request.getParameter("id");
                String col = colunasTabelas.getColNome(tabela);
                if (col == null) {
                    col = "*";
                }
                String sql;
                if (colunasTabelas.getPossuiIEntidades(tabela)) {
                    sql = "select " + col + " from " + tabela + " where id = ? and id_entidade = ? ";
                } else {
                    sql = "select " + col + " from " + tabela + " where id = ? ";
                }
                try {
                    PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                    ps.setInt(1, Integer.valueOf(id));
                    if (colunasTabelas.getPossuiIEntidades(tabela)) ps.setInt(2, Parametros.idEntidade);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        output = rs.getString(1);
                    } else {
                        output = "";
                    }
                } catch (SQLException ex) {
                    output = ex.getMessage();
                    new LogError(ex.getMessage(), ex, request);
                }
            }
        } catch (Exception ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    private void getOpt(){        
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(tabela !=null){
            output = colunasTabelas.getOpts(tabela);
        }
    }
    
    private void getId(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(tabela !=null){
            String btn = request.getParameter("btn");
            String id = request.getParameter("id");
            String col;
            String order;
            switch(btn){
                case "i":
                    col = "";
                    order = "asc";
                    break;
                case "a":
                    col = " and id < "+id;
                    order = "desc";
                    break;
                case "p":
                    col = " and id > "+id;
                    order = "asc";
                    break;
                case "f":
                    col = "";
                    order = "desc";
                    break;
                default:
                    col = "";
                    order = "";
                    break;
            }
            String sql = "select id from "+tabela+" where id_entidade = ? "+col+" order by id "+order+" Limit 1";
            try {
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setInt(1, Parametros.idEntidade);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    output = rs.getString(1);
                }else{
                    output = id;
                }
            } catch (SQLException ex) {
                output = ex.getMessage();
                new LogError(ex.getMessage(), ex,request);
            }
        }
    }
    
    private void geraTabelaDados(){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        Integer colCount = 0;
        if(tabela !=null){
            String sql = "select "+colunasTabelas.getTabColsDados(tabela)+" from "+tabela+" where id_entidade = ? and id = ? ";
            try {
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setInt(1, Parametros.idEntidade);
                ps.setInt(2, Integer.valueOf(request.getParameter("id").trim()));
                ResultSet rs = ps.executeQuery();
                List<String> lInputs = new ArrayList<>();
                if (rs.next()) {
                    colCount = rs.getMetaData().getColumnCount();
                    for(int i = 1; i<=rs.getMetaData().getColumnCount();i++){
                        lInputs.add("<input type=\"text\" id=\"nome_coluna_"+i+"\" value=\""+rs.getMetaData().getColumnName(i)+"\" readonly>");
                        if(rs.getString(i)!=null){
                            switch(rs.getMetaData().getColumnType(i)){
                                case java.sql.Types.NUMERIC:
                                    lInputs.add("<input type=\"text\" id=\"busca_col_"+rs.getMetaData().getColumnName(i)+"\" value=\""+Parser.toHtmlBigDecimal(rs.getBigDecimal(i))+"\" readonly>");
                                    break;
                                case java.sql.Types.TIMESTAMP:
                                    lInputs.add("<input type=\"text\" id=\"busca_col_"+rs.getMetaData().getColumnName(i)+"\" value=\""+rs.getString(i).trim().replace(" ", "T")+"\" readonly>");
                                    break;
                                default:
                                    lInputs.add("<input type=\"text\" id=\"busca_col_"+rs.getMetaData().getColumnName(i)+"\" value=\""+rs.getString(i)+"\" readonly>");
                                    break;
                            }
                        }else{
                            lInputs.add("<input type=\"text\" id=\"busca_col_"+rs.getMetaData().getColumnName(i)+"\" value=\""+rs.getString(i)+"\" readonly>");
                        }
                    }
                    switch(tabela){
                        case "operacoes_diarias":
                            String sqlDet = "select "+colunasTabelas.getTabColsDados(tabela+"_det")+" from "+tabela+"_det where id_operacoes_diarias = ? ";
                            PreparedStatement psDet = Parametros.getConexao(request).getPst(sqlDet, false);
                            psDet.setInt(1, Integer.valueOf(request.getParameter("id")));
                            ResultSet rsDet = psDet.executeQuery();
                            Integer nLinhas = 0;
                            while (rsDet.next()) {
                                nLinhas++;
                                for(int i = 1; i<=rsDet.getMetaData().getColumnCount();i++){
                                    Integer nCol = i+colCount;
                                    lInputs.add("<input type=\"text\" id=\"nome_coluna_"+nCol+"\" value=\""+rsDet.getMetaData().getColumnName(i)+"_"+nLinhas+"\" readonly>");
                                    if(rsDet.getString(i)!=null){
                                        switch(rsDet.getMetaData().getColumnType(i)){
                                            case java.sql.Types.NUMERIC:
                                                lInputs.add("<input type=\"text\" id=\"busca_col_"+rsDet.getMetaData().getColumnName(i)+"_"+nLinhas+"\" value=\""+Parser.toHtmlBigDecimal(rsDet.getBigDecimal(i))+"\" readonly>");
                                                break;
                                            case java.sql.Types.TIMESTAMP:
                                                lInputs.add("<input type=\"text\" id=\"busca_col_"+rsDet.getMetaData().getColumnName(i)+"_"+nLinhas+"\" value=\""+rsDet.getString(i).trim().replace(" ", "T")+"\" readonly>");
                                                break;
                                            default:
                                                lInputs.add("<input type=\"text\" id=\"busca_col_"+rsDet.getMetaData().getColumnName(i)+"_"+nLinhas+"\" value=\""+rsDet.getString(i)+"\" readonly>");
                                                break;
                                        }
                                    }else{
                                        lInputs.add("<input type=\"text\" id=\"busca_col_"+rsDet.getMetaData().getColumnName(i)+"_"+nLinhas+"\" value=\""+rsDet.getString(i)+"\" readonly>");
                                    }
                                }
                                colCount += rs.getMetaData().getColumnCount();
                            }
                            lInputs.add("<input type=\"text\" id=\"num_linhas\" value=\""+nLinhas+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"nome_linhas\" value=\"operacoes_diarias_linhas\" readonly>");
                        break;
                    }
                }
                lInputs.add("<input type=\"text\" id=\"num_cols\" value=\""+colCount+"\" readonly>");
                output = StringUtils.join(lInputs, "\n");
            } catch (SQLException ex) {
                new LogError(ex.getMessage(), ex,request);
            }
        }
    }
    
    private void geraTabelaPopUp(String colunaBuscar){
        ColunasTabelas colunasTabelas = new ColunasTabelas(request);
        String coluna = colunasTabelas.getColuna(request.getParameter("coluna"));
        String tabela = colunasTabelas.getTabela(request.getParameter("tabela"));
        if(coluna!=null && tabela !=null){
            coluna = coluna.replace("nome_tabela",tabela);
            String sql;
            if(colunasTabelas.getPossuiIEntidades(tabela)){
                sql = "select "+colunasTabelas.getTabColsBusca(tabela)+" from "+tabela+" where lower(CAST( "+coluna+" AS text )) like lower(?) and id_entidade = ? order by "+coluna;
                
            }else{
                sql = "select "+colunasTabelas.getTabColsBusca(tabela)+" from "+tabela+" where lower(CAST( "+coluna+" AS text )) like lower(?) order by "+coluna;
            }
            try {
                PreparedStatement ps = Parametros.getConexao(request).getPst(sql, false);
                ps.setString(1, "%"+request.getParameter("id")+"%");
                if (colunasTabelas.getPossuiIEntidades(tabela)) ps.setInt(2, Parametros.idEntidade);
                ResultSet rs = ps.executeQuery();
                List lCols = new ArrayList();
                List lLinhas = new ArrayList();
                List lVals;
                if(rs !=null){
                    while (rs.next()) {  
                        lVals = new ArrayList();
                        for(int i = 1;i<=rs.getMetaData().getColumnCount();i++){                                    
                            if(rs.isFirst()){
                                lCols.add("<th>"+colunasTabelas.getDescricao(rs.getMetaData().getColumnName(i))+"</th>");
                            }
                            if(colunaBuscar == null){
                                lVals.add("<th onclick=\"setaIdEBusca('"+rs.getString("id")+"')\">"+rs.getString(i)+"</th>");
                            }else{
                                if(colunaBuscar.equals("id")){
                                    lVals.add("<th onclick=\"setaIdEBusca('"+rs.getString("id")+"')\">"+rs.getString(i)+"</th>");
                                }else{
                                    lVals.add("<th onclick=\"setaIdFk('"+rs.getString(1)+"','"+colunaBuscar+"','"+rs.getString(2)+"')\">"+rs.getString(i)+"</th>");
                                }
                            }
                        }
                        lLinhas.add("<tr >"+StringUtils.join(lVals, ' ')+"</tr>");
                    }
                    output="<thead>"+StringUtils.join(lCols, ' ')+"</thead><tbody>"+StringUtils.join(lLinhas, ' ')+"</tbody>";  
                }else{
                    output = ps.toString();
                }
            } catch (SQLException ex) {
                new LogError(ex.getMessage(), ex,request);
            }
        }else{
            output = "coluna==null || tabela ==null"+request.getParameter("coluna")+request.getParameter("tabela")+coluna+tabela;
        }
    }
    
    private void buscaDado(){
        String tabela = request.getParameter("tabela");
        Parametros.gravaLogSessao(request);
        String valorBuscar = request.getParameter("valor_buscar");
        List<String> lInputs = new ArrayList<>();
        if(valorBuscar!=null){
            switch(tabela){
                case "fechamento_terminais":
                    FechamentoTerminal fechamentoTerminal = new FechamentoTerminal(request);
                    switch(valorBuscar){
                        case "id_terminal":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+fechamentoTerminal.getIdTerminal()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_funcionario":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+fechamentoTerminal.getIdFuncionario()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "total_movimentos_dia":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+fechamentoTerminal.getTotalMovimentosDia()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "saldo_terminal":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+fechamentoTerminal.getSaldoTerminal()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "diferenca_caixa":    
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+fechamentoTerminal.getDiferencaCaixa()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "data_encerramento":    
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+fechamentoTerminal.getDataFechar()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                    }
                    break;
                case "abertura_terminais":
                    AberturaTerminal aberturaTerminal = new AberturaTerminal(request);
                    switch (valorBuscar){
                        case "troco_dia_anterior":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+aberturaTerminal.getTrocoDiaAnteriorAbertura()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_cofre":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+aberturaTerminal.getIdCofre()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_terminal":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+aberturaTerminal.getIdTerminal()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_funcionario":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+aberturaTerminal.getIdFuncionario()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                    }
                    break;
                case "movimentos_caixas":
                    MovimentoCaixa movimentoCaixa = new MovimentoCaixa(request);
                    switch (valorBuscar){
                        case "id_terminal":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+movimentoCaixa.getIdTerminal()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_funcionario":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+movimentoCaixa.getIdFuncionario()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "data_hora_mov":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+movimentoCaixa.getDataHoraMov()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_cofre":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+movimentoCaixa.getIdCofre()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                    }
                    break;
    
                case "outros_movimentos":
                    OutrosMovimentos outrosMovimentos = new OutrosMovimentos(request);
                    switch (valorBuscar){
                        case "id_terminal":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+outrosMovimentos.getIdTerminal()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "id_funcionario":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+outrosMovimentos.getIdFuncionario()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                        case "data_hora_mov":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+outrosMovimentos.getDataHoraMov()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                    }
                    break;
                case "movimentos_cofres":
                    MovimentoCofre movimentoCofre = new MovimentoCofre(request);
                    switch (valorBuscar){
                        case "id_cofre":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+movimentoCofre.getIdCofre()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                    }
    
                case "movimentos_contas":
                    MovimentoConta movimentoConta = new MovimentoConta(request);
                    switch (valorBuscar){
                        case "id_cofre":
                            lInputs.add("<input type=\"text\" id=\"nome_coluna\" value=\""+valorBuscar+"\" readonly>");
                            lInputs.add("<input type=\"text\" id=\"valor\" value=\""+movimentoConta.getIdCofre()+"\" readonly>");
                            output = StringUtils.join(lInputs,'\n');
                            break;
                    }
            }
        }
    }
    
}
