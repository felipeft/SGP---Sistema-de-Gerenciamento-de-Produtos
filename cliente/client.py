from cliente.proxy_cliente import ProxyClient

def menu():
    while True:
        print("\n===== Gerenciador de Produtos =====")
        print("1. Adicionar Produto")
        print("2. Listar Produtos")
        print("3. Editar Produto")
        print("4. Remover Produto")
        print("5. Sair\n")
        
        opcao = input("Escolha uma opção: ")

        if opcao == "1":
            nome = input("Nome do produto: ")
            preco = float(input("Preço: "))
            ProxyClient.adicionar_produto(nome, preco)  
            
        elif opcao == "2":
            produtos = ProxyClient.listar_produtos()  
            
        elif opcao == "3":
            id_produto = int(input("ID do produto a editar: "))
            nome = input("Novo nome: ")
            preco = float(input("Novo preço: "))
            ProxyClient.editar_produto(id_produto, nome, preco)  
            
        elif opcao == "4":
            id_produto = int(input("ID do produto a remover: "))
            ProxyClient.remover_produto(id_produto)
            
        elif opcao == "5":
            print("Saindo...")
            break
        else:
            print("Opção inválida!")

if __name__ == "__main__":
    menu()
