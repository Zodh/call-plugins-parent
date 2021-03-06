# call-plugins-parent
Serviço "principal", que deve publicar mensagens na fila para o serviço https://github.com/Zodh/rabbit-mq-plugin e realizar chamadas HTTP para o serviço https://github.com/Zodh/rest-template-plugina a fim de responder os seguintes tópicos:

- Qual forma de implementação é mais veloz (tanto para implementar quanto em mensagens por segundo)?
- Qual forma de implementação garante menos falhas?

Este projeto é responsável por receber uma chamada HTTP, que informa a quantidade de mensagens que:

- devem ser publicadas em uma fila (consumida pelo serviço de RabbitMQ); e também
- serão enviadas a outra endpoint.

Tal serviço de escuta (plugin de rabbitmq) e tal endpoint (serviço rest template), devem salvar algum dado e retornar uma mensagem.

Resultados da POC:

![image](https://user-images.githubusercontent.com/53479337/144547117-e62b05b1-f89c-4417-a128-5678b25e6475.png)

---

![image](https://user-images.githubusercontent.com/53479337/144550430-e91797b7-746b-4f7c-8731-d7fb1aed40dd.png)

---

![image](https://user-images.githubusercontent.com/53479337/144550718-3a57d538-ea80-41f5-80e6-b46f32f0d514.png)


## outros serviços: 

serviço rabbit mq (**vencedor devido a garantia de entrega das mensagens**): https://github.com/Zodh/rabbit-mq-plugin 

serviço rest template: https://github.com/Zodh/rest-template-plugin

# conclusão

A implementação do serviço de RabbitMQ é muito mais veloz, pois não necessita de uma modelagem OpenAPI e nem muita coisa além de um @RabbitListener.

Para esta POC, não considero negociável perder informação entre serviços internos. A pequena redução de performance (ainda assim atendendo 427.3 chamadas por segundo em um computador com um processador AMD FX8300 e com uma memória de 8GB de RAM) me parece melhor se comparado à perda de requisições.

OBS: esta poc foi realizada para saber qual o melhor caminho para conectar dois serviços internos. Tanto rabbit mq quanto Rest Template possuem vantagens e desvantagens, não indicando aqui se um se sobressai ao outro, mas sim, validar os tópicos listados anteriormente.

