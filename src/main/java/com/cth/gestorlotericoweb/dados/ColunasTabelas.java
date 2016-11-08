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
    public Map<String,String> mColNome;
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
        mColNome = new HashMap<>();
        this.request = request;
        carregaTabOpts();
        setColTab();
        carregaTabelas();
        carregaTabColsBusca();
        carregaTabColsDados();
        carregaColNome();
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
        putMs("id_terminal","Código do Terminal (Sistema)");
        putMs("id_funcionario", "Código do Funcionário (Sistema)");
        putMs("tipo_operacao_caixa", "Tipo de movimento caixa");
        putMs("id_operacao", "Código da Operação (Sistema)");
        putMs("id_cofre", "Código do Cofre (Sistema)");
        putMs("id_conta", "Código da Conta (Sistema)");
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
        mTabelas.put("operacoes", "operacoes");
        mTabelas.put("cofres", "cofres");
        mTabelas.put("abertura_terminais", "abertura_terminais");
        mTabelas.put("movimentos_caixas", "movimentos_caixas");
        mTabelas.put("fechamento_terminais", "fechamento_terminais");
        mTabelas.put("outros_movimentos", "outros_movimentos");
        mTabelas.put("tarifas_operacoes", "tarifas_operacoes");
        mTabelas.put("movimentos_cofres", "movimentos_cofres");
        mTabelas.put("movimentos_contas", "movimentos_contas");
    }
    
    private void carregaColNome(){
        mColNome.put("lotericas", "nome");
        mColNome.put("terminais", "nome");
        mColNome.put("funcionarios", "nome");
        mColNome.put("contas", "nome_conta");
        mColNome.put("operacoes", "nome_oper");
        mColNome.put("cofres", "nome_cofre");
    }
    
    public String getColNome(String tabela){
        return mColNome.get(tabela);
    }
    
    
    private void carregaTabColsBusca(){
        mTabColsSelBusca.put("lotericas", "id,nome,codigo_caixa");
        mTabColsSelBusca.put("terminais", "id,nome,codigo_caixa, id_loterica,  marca, modelo");
        mTabColsSelBusca.put("funcionarios", "id, nome, codigo_caixa, cpf");
        mTabColsSelBusca.put("contas", "id, nome_conta, conta_corrente, dv,  operacao, agencia");
        mTabColsSelBusca.put("operacoes", "id, nome_oper_caixa, nome_oper, tipo_oper");
        mTabColsSelBusca.put("cofres", "id, nome_cofre, tipo_cofre, id_loterica");
        mTabColsSelBusca.put("abertura_terminais", "id, id_terminal, id_funcionario, data_abertura");
        mTabColsSelBusca.put("movimentos_caixas", "id, id_terminal, id_funcionario, tipo_operacao_caixa, data_hora_mov");
        mTabColsSelBusca.put("outros_movimentos", "id, id_terminal, id_funcionario, tipo_operacao_caixa,data_hora_mov");
        mTabColsSelBusca.put("fechamento_terminais", "id, id_terminal, id_funcionario, data_encerramento");
        mTabColsSelBusca.put("tarifas_operacoes", "id, id_operacao, data_base");
        mTabColsSelBusca.put("movimentos_cofres", "id, id_cofre, data_hora_mov");
        mTabColsSelBusca.put("movimentos_contas", "id, id_conta, data_hora_mov");
    }
    private void carregaTabColsDados(){
        mTabColsSelDados.put("lotericas", "codigo_caixa,nome");
        mTabColsSelDados.put("terminais", "codigo_caixa, nome, marca, modelo, observacoes, id_loterica");
        mTabColsSelDados.put("funcionarios", "codigo_caixa, nome, cpf, tipo_func, observacoes");
        mTabColsSelDados.put("contas", "conta_corrente, dv, nome_conta, operacao, agencia, telefone, gerente, id_loterica, observacoes");
        mTabColsSelDados.put("operacoes", "nome_oper_caixa, nome_oper, tipo_oper, observacoes");
        mTabColsSelDados.put("cofres", "nome_cofre, tipo_cofre, observacoes, id_loterica");
        mTabColsSelDados.put("abertura_terminais", " id_terminal, id_funcionario, data_abertura, troco_dia_anterior, troco_dia, observacoes");
        mTabColsSelDados.put("movimentos_caixas", "tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, valor_movimentado, observacoes, id_cofre");
        mTabColsSelDados.put("outros_movimentos", "tipo_operacao_caixa, id_terminal, id_funcionario, data_hora_mov, valor_movimentado, observacoes");
        mTabColsSelDados.put("fechamento_terminais", "id_terminal, id_funcionario, data_encerramento,resto_caixa, total_movimentos_dia, total_creditos_terminal, total_debitos_terminal,saldo_terminal, diferenca_caixa, observacoes");
        mTabColsSelDados.put("tarifas_operacoes", "id_operacao, data_base, valor_tarifa, observacoes");
        mTabColsSelDados.put("movimentos_cofres", "id_cofre, tipo_movimento_cofre, data_hora_mov, valor_movimentado, observacoes");    
        mTabColsSelDados.put("movimentos_contas", "id_conta,id_cofre, tipo_movimento_conta,forma_deposito, data_hora_mov, valor_movimentado, observacoes");   
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
        lOpts = new ArrayList<>();
        lOpts.add("<option>Nome da Operação (Caixa)</option>");
        lOpts.add("<option>Nome da Operação</option>");
        lOpts.add("<option>Tipo Operação</option>");
        mTabOpts.put("operacoes", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código da lotérica (Sistema)</option>");
        lOpts.add("<option>Nome do Cofre</option>");
        lOpts.add("<option>Tipo Cofre</option>");
        mTabOpts.put("cofres", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código do Terminal (Sistema)</option>");
        lOpts.add("<option>Código do Funcionário (Sistema)</option>");
        mTabOpts.put("abertura_terminais", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código do Terminal (Sistema)</option>");
        lOpts.add("<option>Código do Funcionário (Sistema)</option>");
        lOpts.add("<option>Tipo de movimento caixa</option>");
        mTabOpts.put("movimentos_caixas", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código do Terminal (Sistema)</option>");
        lOpts.add("<option>Código do Funcionário (Sistema)</option>");
        lOpts.add("<option>Tipo de movimento caixa</option>");
        mTabOpts.put("outros_movimentos", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código do Terminal (Sistema)</option>");
        lOpts.add("<option>Código do Funcionário (Sistema)</option>");
        mTabOpts.put("fechamento_terminais", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código da Operação (Sistema)</option>");
        mTabOpts.put("tarifas_operacoes", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código do Cofre (Sistema)</option>");
        mTabOpts.put("movimentos_cofres", lOpts);
        lOpts = new ArrayList<>();
        lOpts.add("<option>Código da Conta (Sistema)</option>");
        mTabOpts.put("movimentos_contas", lOpts);
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
