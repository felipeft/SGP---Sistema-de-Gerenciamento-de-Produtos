package servidor;

import java.util.List;

import com.google.protobuf.ByteString;
import proto.ProdutoOuterClass.Produto;
import proto.ProdutoOuterClass.AddProductRequest;
import proto.ProdutoOuterClass.EditarProdutoArgs;
import proto.ProdutoOuterClass.GenericResponse;
import proto.ProdutoOuterClass.ListAllProductResponse;
import proto.ProdutoOuterClass.RemoverProdutoArgs;

public class ProdutoEsqueleto {
	
	private ProdutoServente servente;
	
	public ProdutoEsqueleto() {
		servente = new ProdutoServente();
	}
	
	public byte[] adicionarProduto(ByteString args) {
		try {
			AddProductRequest requisicao = AddProductRequest.parseFrom(args);
			GenericResponse resposta = servente.adicionarProduto(requisicao.getNome(), requisicao.getPreco());
			return resposta.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return GenericResponse.newBuilder().setMensagem("Erro ao adicionar produto").build().toByteArray();
		}
	}
	
	public byte[] listarProdutos() {
		try {
			List<Produto> produtos = servente.listarProdutos();
			
			return ListAllProductResponse.newBuilder()
					.addAllProdutos(produtos)
					.build()
					.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] removerProduto(ByteString args){
		try {
			RemoverProdutoArgs requisicao = RemoverProdutoArgs.parseFrom(args);
			GenericResponse resposta = servente.removerProduto(requisicao.getId());
			return resposta.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return GenericResponse.newBuilder().setMensagem("Erro ao remover produto").build().toByteArray();
		}
	}
	
	public byte[] editarProduto(ByteString args) {
		try {
			EditarProdutoArgs requisicao = EditarProdutoArgs.parseFrom(args);
			GenericResponse resposta = servente.editarProduto(requisicao.getId(), requisicao.getNome(), requisicao.getPreco());
			return resposta.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return GenericResponse.newBuilder().setCodigo(500).setMensagem("Erro ao editar produto").build().toByteArray();
		}
	}
}
