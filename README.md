# SGP - Sistema de Gerenciamento de Produtos

Projeto de um sistema distribuído para gerenciamento de produtos, implementado em **Python** e **Java**.  
O sistema permite adicionar, listar, editar e remover produtos, utilizando comunicação via **UDP**.  
Inclui mecanismos para **tratamento de falhas** e retransmissão de mensagens.

---

## 📌 Funcionalidades

- 📌 **Adicionar Produto**: Registra novos produtos no sistema com código, nome e preço.
- 📌 **Listar Produtos**: Retorna a lista de produtos registrados, incluindo informações detalhadas.
- 📌 **Editar Produto**: Permite modificar os dados de um produto existente.
- 📌 **Remover Produto**: Exclui um produto do sistema com base em seu identificador.

---

## 🖥️ Arquitetura do Sistema

O SGP segue uma arquitetura distribuída baseada em comunicação via **UDP**, onde o cliente interage com o servidor para realizar as operações sobre os produtos.  

### 📌 Estrutura do Sistema:

📌 **Servidor** (Java)  
- `ServidorUDP.java` → Gerencia o socket UDP, recebendo e processando requisições.  
- `ProdutoServente.java` → Implementa as regras de negócio para cadastro, consulta, edição e remoção.  
- `ProdutoDespachante.java` → Atua como intermediário entre o servidor e as classes de processamento.  
- `ProdutoEsqueleto.java` → Define a interface entre cliente e servidor para chamadas remotas.  

📌 **Cliente** (Python)  
- `client.py` → Interface do cliente para interagir com o servidor.  
- `proxy_cliente.py` → Traduz chamadas de métodos em mensagens de rede.  
- `udp_client.py` → Gerencia o envio de mensagens UDP para comunicação com o servidor.  

---

## 📊 Modelo de Dados

O sistema utiliza **arquivos .proto** para definir a estrutura das mensagens trocadas.  

📌 **Produto**  
```json
{
  "id": int32,
  "nome": "string",
  "preco": float
}
