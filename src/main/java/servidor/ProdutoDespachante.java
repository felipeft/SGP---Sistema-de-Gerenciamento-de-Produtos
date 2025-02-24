package servidor;

import java.lang.reflect.Method;
import proto.ProdutoOuterClass.Message;

public class ProdutoDespachante {

	public byte[] selecionaEsqueleto(byte[] requestBytes) {
		try {
			// Desserializa a requisição para obter methodId e arguments
			Message request = Message.parseFrom(requestBytes);
			String method = request.getMethodId();
			System.out.println("Método requisitado: " + method);

			// Instancia o ProdutoEsqueleto
			ProdutoEsqueleto esqueleto = new ProdutoEsqueleto();

			// Percorre todos os métodos da classe ProdutoEsqueleto
			for (Method m : ProdutoEsqueleto.class.getDeclaredMethods()) {

				if (m.getName().equals(method)) {
					if (m.getParameterCount() == 0) {
						// Método sem parâmetros
						return (byte[]) m.invoke(esqueleto);
					} else {
						// Método com parâmetros (convertendo ByteString para byte[])
						return (byte[]) m.invoke(esqueleto, request.getArguments());
					}
				}
			}

			// Se o método não for encontrado, imprime erro e retorna null
			System.err.println("Erro: método '" + method + "' não encontrado no ProdutoEsqueleto!");
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
