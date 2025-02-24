package servidor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import proto.ProdutoOuterClass.Message;
import utils.RequestKey;
//import java.util.Arrays;

public class ServidorUDP {

    public static void main(String args[]) {
        try {
            int serverPort = 5005; // Porta para comunicação UDP
            try (DatagramSocket socket = new DatagramSocket(serverPort)) {
                socket.setSoTimeout(5000);
                System.out.println("Servidor UDP rodando na porta " + serverPort);
                
                while (true) {
                    try {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        
                        socket.receive(packet);
                        
                        new Connection(socket, packet);
                    } catch (IOException e) {
                        if (!e.getMessage().contains("timed out")) {
                        	System.out.println("Erro ao receber o pacote: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Connection extends Thread {
    DatagramSocket socket;
    DatagramPacket packet;
    ProdutoDespachante despachante;
    static Set<RequestKey> processedRequests = new HashSet<>();
    static Map<RequestKey, byte[]> responses = new HashMap<>();
    static short quantidadeRequisicao = 0;
    
    public Connection(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
        despachante = new ProdutoDespachante();
        this.start();
    }

    public byte[] getRequest() {
        return packet.getData();
    }

    public Message desempacotaRequisicao(byte[] array) {
        try {
            //System.out.println("Recebendo array de bytes: " + Arrays.toString(array));
    
            int length = packet.getLength();
            //System.out.println("Tamanho do pacote recebido: " + length);
    
            byte[] relevantBytes = new byte[length];
            System.arraycopy(array, 0, relevantBytes, 0, length);
    
            //System.out.println("Bytes relevantes copiados: " + Arrays.toString(relevantBytes));
    
            Message mensagem = Message.parseFrom(relevantBytes);
            //System.out.println("Mensagem desempacotada com sucesso: " + mensagem);
    
            return mensagem;
        } catch (Exception e) {
            System.out.println("Erro ao desempacotar a requisição: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    

    public byte[] empacotaResposta(byte[] resultado, int requestId) {
        try {
            Message response = Message.newBuilder()
                .setType(2)
                .setId(requestId)
                .setObfReference("ProdutoService")
                .setMethodId("resposta")
                .setArguments(com.google.protobuf.ByteString.copyFrom(resultado))
                .build();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            response.writeTo(byteArrayOutputStream);
            
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Erro ao empacotar a resposta: " + e.getMessage());
            return null;
        }
    }

    public void sendReply(byte[] resposta) throws Exception {
        try {
            DatagramPacket replyPacket = new DatagramPacket(resposta, resposta.length, packet.getAddress(), packet.getPort());
            
            socket.send(replyPacket);
        } catch (Exception e) {
           throw new Exception(e.getMessage());
        }
    }

    public void run() {
        
        Message requisicao = desempacotaRequisicao(getRequest());
        //System.err.println(requisicao);
        
        if (requisicao != null) {
            quantidadeRequisicao++;
            System.out.println("quantidadeRequisicao: " + quantidadeRequisicao);
            
            int requestId = requisicao.getId();
            String clientIP = packet.getAddress().getHostAddress();
            int clientPort = packet.getPort();
            
            RequestKey key = new RequestKey(clientIP, clientPort, requestId);

            if (processedRequests.contains(key)) {
                System.out.println("Mensagem duplicada recebida de " + clientIP + ":" + clientPort + " com ID: " + requestId + ". Ignorando.");
                
                byte[] respostaDuplicada = responses.get(key);
                
                if (respostaDuplicada != null) {
                    try {
                        sendReply(respostaDuplicada);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            

            processedRequests.add(key);
            if (quantidadeRequisicao == 3) return;  //fazendo o teste para msg duplicada
            
            byte[] resultado = despachante.selecionaEsqueleto(requisicao.toByteArray());
            
            responses.put(key, resultado);
            
            
            try {
				sendReply(empacotaResposta(resultado, requestId));
			} catch (Exception e) {
				e.printStackTrace();
			}
        } else {
            System.out.println("Requisição inválida recebida.");
        }
    }
}
