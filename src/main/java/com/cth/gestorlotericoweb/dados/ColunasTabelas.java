/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cth.gestorlotericoweb.dados;

import com.cth.gestorlotericoweb.LogError;
import com.cth.gestorlotericoweb.parametros.Parametros;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author CarlosEduardo
 */
public class ColunasTabelas {
    public Map<String,String> mDescri;
    public Map<String,String> mCol;
    public Map<String,String> mTabelas;
    public Map<String,String> mTabColsSelBusca;
    public Map<String,String> mTabColsSelDados;
    public Map<String,List<String>> mTabOpts;
    final HttpServletRequest request;
    
    public ColunasTabelas(HttpServletRequest request){
        mDescri = new HashMap<>();
        mCol = new HashMap<>();
        mTabelas = new HashMap<>();
        mTabColsSelBusca = new HashMap<>();
        mTabColsSelDados = new HashMap<>();
        mTabOpts = new HashMap<>();
        this.request = request;
        carregaTabOpts();
        setColTab();
        carregaTabelas();
        carregaTabColsBusca();
        carregaTabColsDados();
    }
    
    private void setColTab(){
        try {
            ResultSet rs = Parametros.getConexao().getRs("select * from cols_descri");
            while (rs.next()) {                
                putMs(rs.getString(2), rs.getString(3));
            }
        } catch (SQLException ex) {
            new LogError(ex.getMessage(), ex,request);
        }
    }
    
    private void putMs(String coluna,String descri){
        mDescri.put(coluna, descri);
        mCol.put(descri, coluna);
    }
    
    public String getDescricao(String coluna){
        return mDescri.get(coluna);
    }
    
    public String getColuna(String descricao){
        return mCol.get(descricao);
    }
    
    private void carregaTabelas(){
        mTabelas.put("lotericas", "lotericas");
        mTabelas.put("terminais", "terminais");
        mTabelas.put("funcionarios", "funcionarios");
        mTabelas.put("contas", "contas");
    }
    
    private void carregaTabColsBusca(){
        mTabColsSelBusca.put("lotericas", "id,codigo_caixa,nome");
        mTabColsSelBusca.put("terminais", "id,codigo_caixa, id_loterica, nome, marca, modelo");
        mTabColsSelBusca.put("funcionarios", "id, codigo_caixa, nome, cpf");
        mTabColsSelBusca.put("contas", "id, conta_corrente, dv, nome_conta, operacao, agencia");
    }
    private void carregaTabColsDados(){
        mTabColsSelDados.put("lotericas", "codigo_caixa,nome");
        mTabColsSelDados.put("terminais", "codigo_caixa, nome, marca, modelo, observacoes, id_loterica");
        mTabColsSelDados.put("funcionarios", "codigo_caixa, nome, cpf, tipo_func, observacoes");
        mTabColsSelDados.put("contas", "conta_corrente, dv, nome_conta, operacao, agencia, telefone, gerente, id_loterica, observacoes");
    }
    private void carregaTabOpts(){
        List<String> lOpts = new ArrayList<>();
        lOpts.add("<option>Código na Caixa</option>");
        lOpts.add("<option>Nome</option>");
        mTabOpts.put("lotericas", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código na Caixa</option>");
        lOpts.add("<option>Lotérica</option>");
        lOpts.add("<option>Marca</option>");
        lOpts.add("<option>Modelo</option>");
        lOpts.add("<option>Nome</option>");
        mTabOpts.put("terminais", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código na Caixa</option>");
        lOpts.add("<option>CPF</option>");
        lOpts.add("<option>Nome</option>");
        lOpts.add("<option>Tipo Funcionário</option>");
        mTabOpts.put("funcionarios", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Nome da conta</option>");
        lOpts.add("<option>Número da Conta</option>");
        lOpts.add("<option>Operação</option>");
        mTabOpts.put("contas", lOpts);
    }
    
    public String getOpts(String tabela){
        if(mTabOpts.get(tabela)!=null){
            return StringUtils.join(mTabOpts.get(tabela), '\n');
        }else{
            return null;
        }
    }
    
    public List getlOpts(String tabela){
        if(mTabOpts.get(tabela)!=null){
            return mTabOpts.get(tabela);
        }else{
            return null;
        }
    }
    
    public String getTabela(String tabela){
        return mTabelas.get(tabela);
    }
    
    public String getTabColsBusca(String tabela){
        return mTabColsSelBusca.get(tabela);
    }
    public String getTabColsDados(String tabela){
        return mTabColsSelDados.get(tabela);
    }
    
}
