package servidor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import proto.ProdutoOuterClass.Produto;
import proto.ProdutoOuterClass.GenericResponse;

public class ProdutoServente {

	private static List<Produto> produtos = new ArrayList<>();
	private static int proximoId = 1; // Controle para IDs automáticos
	
	// Método para adicionar um produto
	public GenericResponse adicionarProduto(String nome, float preco) {
		if (nome == null || nome.isEmpty() || preco <= 0) {
			return GenericResponse.newBuilder()
					.setCodigo(400)
					.setMensagem("Nome e preço devem ser válidos!")
					.build();
		}

		Produto novoProduto = Produto.newBuilder()
				.setId(proximoId++)
				.setNome(nome)
				.setPreco(preco)
				.build();
		
		produtos.add(novoProduto);
		System.out.println("Produto adicionado com sucesso!");
		
		return GenericResponse.newBuilder()
				.setCodigo(200)
				.setMensagem("Produto adicionado com sucesso!")
				.build();
	}
	
	// Método para listar produtos
	public List<Produto> listarProdutos() {
		return new ArrayList<>(produtos);
	}

	// Método para remover um produto
	public GenericResponse removerProduto(int id) {
		Iterator<Produto> iterator = produtos.iterator();
		while (iterator.hasNext()) {
			Produto produto = iterator.next();
			if (produto.getId() == id) {
				iterator.remove();
				return GenericResponse.newBuilder()
						.setCodigo(200)
						.setMensagem("Produto removido com sucesso!")
						.build();
			}
		}

		System.out.println("Produto removido com sucesso!");
		
		return GenericResponse.newBuilder()
				.setCodigo(404)
				.setMensagem("Produto não encontrado!")
				.build();
	}
	
	// Método para editar um produto
	public GenericResponse editarProduto(int id, String nome, float preco) {
		for (int i = 0; i < produtos.size(); i++) {
			if (produtos.get(i).getId() == id) {
				if (nome == null || nome.isEmpty() || preco <= 0) {
					return GenericResponse.newBuilder()
							.setCodigo(400)
							.setMensagem("Nome e preço devem ser válidos!")
							.build();
				}

				Produto produtoAtualizado = produtos.get(i).toBuilder()
						.setNome(nome)
						.setPreco(preco)
						.build();
				
				produtos.set(i, produtoAtualizado);
				System.out.println("Produto editado com sucesso!");

				return GenericResponse.newBuilder()
						.setCodigo(200)
						.setMensagem("Produto editado com sucesso!")
						.build();
			}
		}
		
		return GenericResponse.newBuilder()
				.setCodigo(404)
				.setMensagem("Produto não encontrado!")
				.build();
	}
}
