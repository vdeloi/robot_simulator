# 🤖 Simulador de Robôs - Laboratório 5

📖 Descrição Geral
Bem-vindo ao Simulador de Robôs! Este projeto, desenvolvido para a disciplina de Programação Orientada a Objetos (MC322), é um laboratório virtual onde robôs inteligentes se movem, interagem e executam tarefas em um ambiente 3D.

O foco deste laboratório é a aplicação de conceitos avançados de POO, como abstração, composição e polimorfismo, para criar um sistema modular e flexível. Os robôs agora são capazes de executar missões de forma autônoma, utilizando subsistemas internos para movimento, sensores e comunicação, com todas as ações importantes registradas em um arquivo de log. 

✨ Principais Funcionalidades
Ambiente Tridimensional: Um grid 3D onde robôs e obstáculos coexistem e interagem.
Hierarquia de Robôs: Diversos tipos de robôs (terrestres e aéreos) que herdam de classes base como Robo e AgenteInteligente. 
Composição de Módulos: Robôs são construídos com subsistemas especializados para movimento, sensores e comunicação, promovendo alta coesão e baixo acoplamento. 
Missões Autônomas: Robôs inteligentes podem receber e executar missões como explorar, patrulhar ou monitorar, de forma independente da interação do usuário. 

Sistema de Sensores: Robôs equipados com sensores de proximidade e altitude para perceber o ambiente.
Comunicação: Robôs comunicadores podem trocar mensagens através de uma central de comunicação.
Tratamento de Exceções: Um sistema robusto de exceções personalizadas garante o tratamento de erros como colisões, movimentos inválidos e ações em robôs desligados.
Registro de Atividades (Logging): Todas as ações e eventos importantes da simulação são registrados com data e hora no arquivo missao_log.txt. 
Menu Interativo: Uma interface de console completa para gerenciar e interagir com a simulação.
🚀 Como Executar
Pré-requisitos:

Java Development Kit (JDK) instalado.
Compilação:
Navegue até o diretório src do projeto e compile todos os arquivos .java:

Bash

javac */*.java */*/*.java
Execução:
A partir do diretório src, execute a classe Main:

Bash

java Main
Isso iniciará o menu interativo, onde você poderá controlar a simulação.

📂 Estrutura do Projeto
O código-fonte está organizado em pacotes para garantir a modularidade e a clareza. 

src/
├── Main.java              # Classe principal que inicia a simulação
├── ambiente/              # Classes relacionadas ao ambiente (Ambiente, Entidade, Obstaculo, Exceções)
├── comunicacao/           # Classes para comunicação entre robôs (CentralComunicacao, Comunicavel)
├── missao/                # Define a interface Missao e suas implementações concretas (MissaoExplorar, etc.)
├── robo/                  # Classes base dos robôs (Robo, AgenteInteligente) e suas especializações
│   └── modulos/           # Módulos de comportamento (ControleMovimento, GerenciadorSensores)
├── sensores/              # Classes de sensores (Sensor, SensorProximidade, etc.)
└── util/                  # Classes utilitárias (Log)
🧠 Conceitos e Padrões de Projeto
Este simulador foi projetado com base em princípios fundamentais de Orientação a Objetos.

1. Herança e Abstração
A hierarquia de classes permite reutilizar código e definir contratos claros.

Robo: Classe abstrata base para todos os robôs, definindo atributos e comportamentos comuns.
AgenteInteligente: Uma especialização abstrata de Robo para aqueles capazes de executar missões.  Isso separa os robôs com capacidade de autonomia dos mais simples.

Classes Concretas: RoboTerrestre e RoboAereo herdam as funcionalidades e as especializam.
2. Composição sobre Herança
Para evitar uma hierarquia de classes rígida e complexa, os robôs são compostos por módulos de comportamento.  Isso permite que as funcionalidades de movimento, sensores e comunicação sejam encapsuladas em suas próprias classes e reutilizadas.

ControleMovimento: Define a lógica de como um robô se move. Existem implementações para ControleMovimentoTerrestre e ControleMovimentoAereo.
GerenciadorSensores: Centraliza a lógica de acionamento de todos os sensores de um robô.
ModuloComunicacao: Encapsula a lógica de envio e recebimento de mensagens.
3. Polimorfismo com Interfaces
Interfaces são usadas para definir "contratos" de comportamento, permitindo que diferentes classes interajam de maneira uniforme.

Entidade: Contrato implementado por Robo e Obstaculo, garantindo que qualquer objeto no ambiente tenha uma posição e representação.
Missao: Define o método executar(), permitindo que qualquer AgenteInteligente execute diferentes tipos de missões (explorar, patrulhar) sem conhecer os detalhes de cada uma. 
Sensoreavel e Comunicavel: Interfaces que "adicionam" as capacidades de usar sensores e de se comunicar a um robô.
🎯 Missões Autônomas
O sistema de missões é um dos principais recursos do simulador.

A interface Missao define um contrato único: void executar(Robo robo, Ambiente ambiente);. 
Classes como MissaoExplorar, MissaoPatrulhar e MissaoMonitorar implementam essa interface, cada uma com uma lógica de comportamento específica. 
Qualquer AgenteInteligente pode receber uma instância de Missao e executá-la, aplicando o polimorfismo. 
⚠️ Exceções Personalizadas
Para um controle de erros mais claro e robusto, o projeto utiliza um conjunto de exceções personalizadas:

AcaoNaoPermitidaException: Lançada quando uma ação viola uma regra (ex: um robô terrestre tentando voar).
ColisaoException: Ocorre quando uma entidade tenta se mover para uma célula já ocupada.
ForaDosLimitesException: Lançada se uma ação ocorre fora das dimensões do ambiente.
RoboDesligadoException: Impede que ações sejam executadas por robôs que estão desligados.
ErroComunicacaoException: Sinaliza falhas no envio ou recebimento de mensagens.
RecursoInsuficienteException: Usada para ações que consomem um recurso indisponível.
📝 Registro de Logs (Logging)
A classe Log no pacote util é responsável por registrar eventos importantes em um arquivo de texto.

Arquivo: missao_log.txt
Formato: Cada linha contém um timestamp (data e hora) e a descrição do evento.
Exemplo:
2025/06/11 12:16:00 - Missão MissaoExplorar atribuída a Drone-01
2025/06/11 12:16:46 - MISSAO EXPLORAR: Iniciada por Drone-01
2025/06/11 12:16:46 - MISSAO EXPLORAR: Drone-01 moveu-se para (6, 6, 2)
Isso é fundamental para depurar o comportamento dos robôs e analisar o resultado de uma simulação.