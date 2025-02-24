from cliente import Produto_pb2
from cliente.udp_client import enviar_requisicao_rtx

class ProxyClient:
    
    servidor = "localhost"
    porta = 5005
    
    @staticmethod
    def adicionar_produto(nome, preco):
        """ 
        Envia uma requisição para adicionar um produto ao servidor. 
        O servidor responde com uma mensagem confirmando o sucesso ou erro da operação.
        """
        
        # Cria um objeto de requisição AddProductRequest do Protobuf
        request = Produto_pb2.AddProductRequest()
        request.nome = nome  # Define o nome do produto
        request.preco = preco  # Define o preço do produto

        # Criar uma mensagem `Message` para empacotar a requisição
        message = Produto_pb2.Message()
        message.type = 1  # Define que é uma requisição
        message.id = 123  # ID único para a requisição (pode ser dinâmico)
        message.obfReference = "ProdutoService"  # Nome do serviço no servidor
        message.methodId = "adicionarProduto"  # Nome do método a ser chamado no servidor
        message.arguments = request.SerializeToString()  # Serializa a requisição para envio

        # Enviar a requisição ao servidor via UDP
        resposta_bytes = enviar_requisicao_rtx(message.SerializeToString())

        # Desserializar a resposta como `Message`
        mensagem = Produto_pb2.Message()
        try:
            mensagem.ParseFromString(resposta_bytes)
        except Exception as e:
            print(f"Erro ao desserializar resposta: {e}")
            return

        # Agora extraímos os argumentos da mensagem e os desserializamos como `GenericResponse`
        resposta = Produto_pb2.GenericResponse()
        resposta.ParseFromString(mensagem.arguments)

        # Exibe a resposta final do servidor
        print(f"Resposta do Servidor: {resposta.mensagem}")

    @staticmethod
    def listar_produtos():
        """ 
        Envia uma requisição para listar todos os produtos cadastrados no servidor. 
        O servidor retorna uma lista contendo os produtos disponíveis.
        """
        
        # Cria uma requisição vazia (pois listar produtos não precisa de argumentos)
        request = Produto_pb2.ListarProdutosArgs()

        # Criar uma mensagem `Message` para empacotar a requisição
        message = Produto_pb2.Message()
        message.type = 1  # Define que é uma requisição
        message.id = 124  # ID único para a requisição
        message.obfReference = "ProdutoService"  # Nome do serviço no servidor
        message.methodId = "listarProdutos"  # Nome do método a ser chamado
        message.arguments = request.SerializeToString()  # Serializa a requisição para envio

        # Enviar a requisição ao servidor
        resposta_bytes = enviar_requisicao_rtx(message.SerializeToString())

        # Desserializar a resposta como `Message`
        mensagem = Produto_pb2.Message()
        try:
            mensagem.ParseFromString(resposta_bytes)
        except Exception as e:
            print(f"Erro ao desserializar resposta: {e}")
            return

        # Agora extraímos os argumentos da mensagem e os desserializamos como `ListarProdutosResponse`
        resposta = Produto_pb2.ListarProdutosResponse()  # Mensagem correta para a resposta
        resposta.ParseFromString(mensagem.arguments)

        # Exibe a lista de produtos retornada pelo servidor
        print("Lista de Produtos:")
        for produto in resposta.produtos:
            print(f"ID: {produto.id}, Nome: {produto.nome}, Preço: {produto.preco}")

    @staticmethod
    def remover_produto(id_produto):
        """ 
        Envia uma requisição para remover um produto pelo ID. 
        O servidor retorna uma mensagem indicando se a remoção foi bem-sucedida ou não.
        """
        
        # Cria a requisição com o ID do produto a ser removido
        request = Produto_pb2.RemoverProdutoArgs()
        request.id = id_produto  # Define o ID do produto a ser removido

        # Criar uma mensagem `Message` para empacotar a requisição
        message = Produto_pb2.Message()
        message.type = 1
        message.id = 125
        message.obfReference = "ProdutoService"
        message.methodId = "removerProduto"
        message.arguments = request.SerializeToString()

        # Enviar a requisição ao servidor
        resposta_bytes = enviar_requisicao_rtx(message.SerializeToString())

        # Desserializar a resposta como `Message`
        mensagem = Produto_pb2.Message()
        try:
            mensagem.ParseFromString(resposta_bytes)
        except Exception as e:
            print(f"Erro ao desserializar resposta: {e}")
            return

        # Agora extraímos os argumentos da mensagem e os desserializamos como `GenericResponse`
        resposta = Produto_pb2.GenericResponse()
        resposta.ParseFromString(mensagem.arguments)

        # Exibe a resposta final do servidor
        print(f"Resposta do Servidor: {resposta.mensagem}")

    @staticmethod
    def editar_produto(id_produto, nome, preco):
        """ 
        Envia uma requisição para editar um produto pelo ID. 
        O servidor retorna uma mensagem indicando se a edição foi bem-sucedida ou não.
        """
        
        # Cria a requisição com os novos dados do produto
        request = Produto_pb2.EditarProdutoArgs()
        request.id = id_produto  # Define o ID do produto a ser editado
        request.nome = nome  # Define o novo nome do produto
        request.preco = preco  # Define o novo preço do produto

        # Criar uma mensagem `Message` para empacotar a requisição
        message = Produto_pb2.Message()
        message.type = 1
        message.id = 126
        message.obfReference = "ProdutoService"
        message.methodId = "editarProduto"
        message.arguments = request.SerializeToString()

        # Enviar a requisição ao servidor
        resposta_bytes = enviar_requisicao_rtx(message.SerializeToString())

        # Desserializar a resposta como `Message`
        mensagem = Produto_pb2.Message()
        try:
            mensagem.ParseFromString(resposta_bytes)
        except Exception as e:
            print(f"Erro ao desserializar resposta: {e}")
            return

        # Agora extraímos os argumentos da mensagem e os desserializamos como `GenericResponse`
        resposta = Produto_pb2.GenericResponse()
        resposta.ParseFromString(mensagem.arguments)

        # Exibe a resposta final do servidor
        print(f"Resposta do Servidor: {resposta.mensagem}")
