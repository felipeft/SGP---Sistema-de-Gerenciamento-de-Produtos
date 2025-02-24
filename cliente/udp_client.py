import socket
from cliente.config import SERVER_IP, SERVER_PORT

# Definição de parâmetros para retransmissão
MAX_TENTATIVAS = 2 # Número máximo de tentativas
TIMEOUT = 0.5  # Tempo limite para resposta em segundos

def enviar_requisicao(mensagem_serializada):
    """ Envia uma mensagem UDP para o servidor e recebe a resposta """
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.sendto(mensagem_serializada, (SERVER_IP, SERVER_PORT))

    resposta, _ = sock.recvfrom(4096)  # Aguarda resposta do servidor
    return resposta


def enviar_requisicao_rtx(mensagem_serializada):
    """ 
    Envia uma mensagem UDP para o servidor e recebe a resposta, 
    com retransmissão em caso de falha.
    """
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.settimeout(TIMEOUT)  # Define um timeout para evitar bloqueio infinito
    
    tentativas = 0
    
    while tentativas < MAX_TENTATIVAS:
        try:
            print(f"Tentativa {tentativas + 1} de {MAX_TENTATIVAS}... Enviando requisição para {SERVER_IP}:{SERVER_PORT}")
            
            sock.sendto(mensagem_serializada, (SERVER_IP, SERVER_PORT))
            
            resposta, _ = sock.recvfrom(4096)  # Aguarda resposta do servidor
            print("Resposta recebida com sucesso!")
            return resposta  # Se recebeu resposta, retorna imediatamente

        except socket.timeout:
            print(f"Timeout! Nenhuma resposta do servidor após {TIMEOUT} segundos.")
            tentativas += 1  # Conta a tentativa falha

    print("Erro: O servidor não respondeu após várias tentativas.")
    return None  # Retorna None se todas as tentativas falharem
