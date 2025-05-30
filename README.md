# Simulador de Robôs - Laboratório 4

## Descrição Geral

Este projeto é a continuação do desenvolvimento de um simulador de robôs em um ambiente tridimensional, como parte da disciplina MC322. O objetivo é criar um laboratório de robótica virtual onde diversos robôs inteligentes podem interagir, mover-se e realizar tarefas cooperativas. 

Neste Laboratório 4, o foco foi a consolidação de conceitos anteriores e a introdução de novos, como o uso avançado de interfaces para polimorfismo e herança múltipla, a implementação de um sistema de mapa tridimensional no ambiente, a criação e utilização de enumerações, e o tratamento robusto de erros através de exceções personalizadas.


## Principais Mudanças Realizadas (Laboratório 4)

* **Reforço de Enumerações**: Utilização consolidada de enums como `TipoEntidade`  e `EstadoRobo` para controle de fluxo e identificação.

* **Interfaces e Polimorfismo**:
    * A interface `Entidade` é implementada por todas as classes que existem no ambiente (`Robo`, `Obstaculo`). 
    * Robôs específicos implementam interfaces de comportamento como `Sensoreavel`, `Comunicavel`.
    * Foram criadas novas interfaces funcionais como `Autonomo`, `Explorador` e `Coletor` para definir contratos de funcionalidades específicas dos robôs. 

* **Herança Múltipla via Interfaces**: Robôs podem herdar de uma classe base (`Robo`) e implementar múltiplas interfaces para adquirir diferentes capacidades (e.g., `RoboDroneDeCarga` herda de `RoboAereo` e implementa `Autonomo`).

* **Exceções Personalizadas**:
    * Introdução de um sistema robusto de tratamento de erros com exceções personalizadas como `RoboDesligadoException`, `ColisaoException`, `ErroComunicacaoException`. 
    * Criação de novas exceções personalizadas: `ForaDosLimitesException`, `AcaoNaoPermitidaException`, `RecursoInsuficienteException`. 

* **Mapa Tridimensional no Ambiente**: A classe `Ambiente` agora inclui um mapa `TipoEntidade[][][]` para representar explicitamente a ocupação do espaço tridimensional. 

* **Tarefas Específicas para Robôs**: Cada tipo de robô possui uma implementação do método `executarTarefa()`, demonstrando comportamentos especializados. 

* **Menu Interativo Aprimorado**: O menu na classe `Main` foi atualizado para permitir a interação com as novas funcionalidades, como acionar sensores, comunicação e execução de tarefas específicas dos robôs. 

* **Central de Comunicação**: Implementação da `CentralComunicacao` para registrar mensagens trocadas entre robôs comunicáveis.


## Lista das Interfaces Criadas e Onde Foram Implementadas

1.  **`Entidade`** 
    * Implementada por: `Robo` (classe abstrata), `Obstaculo` (classe). Consequentemente, todas as subclasses de `Robo` (`RoboTerrestre`, `Main.RoboAereo`, `Main.RoboDroneDeCarga`, `Main.RoboComunicador`) também são `Entidade`.

2.  **`Sensoreavel`** 
    * Implementada por: `RoboTerrestre`, `Main.RoboAereo`. Por herança, `Main.RoboDroneDeCarga` e `Main.RoboComunicador` também são `Sensoreavel`.

3.  **`Comunicavel`** 
    * Implementada por: `Main.RoboComunicador`.

4.  **`Autonomo`** (Interface Funcional) 
    * Implementada por: `Main.RoboDroneDeCarga`.

5.  **`Explorador`** (Interface Funcional) 
    * Definida no arquivo `Explorador.java`. (Nenhum robô na classe `Main` a implementa diretamente como exemplo, mas está disponível para uso).

6.  **`Coletor<T>`** (Interface Funcional) 
    * Definida no arquivo `Coletor.java`. (Nenhum robô na classe `Main` a implementa diretamente como exemplo, mas está disponível para uso).


## Lista das Exceções Personalizadas Implementadas e Onde São Lançadas

1.  **`AcaoNaoPermitidaException`** 
    * Propósito: Lançada quando uma ação não é permitida pelo estado atual da entidade ou regras do sistema.
    * Lançada em:
        * `Ambiente`: `moverEntidade()`
        * `Robo`: `moverPara()`, `moverRelativamente()` (indiretamente pela chamada ao `Ambiente`)
        * `RoboTerrestre`: `moverPara()`, `moverRelativamente()` (validações específicas de altitude e velocidade)
        * `Main.RoboAereo`: `subir()`, `descer()`, `moverRelativamente()` (validações de altitude)
        * `Main.RoboDroneDeCarga`: `carregar()`, `descarregar()`
        * Métodos que implementam `Autonomo.executarProximaAcaoAutonoma()`

2.  **`ColisaoException`** 
    * Propósito: Lançada ao tentar adicionar ou mover uma entidade para uma posição já ocupada.
    * Lançada em:
        * `Ambiente`: `adicionarEntidade()`, `moverEntidade()`

3.  **`ErroComunicacaoException`**
    * Propósito: Lançada quando ocorre um erro na comunicação entre entidades.
    * Lançada em:
        * `Comunicavel`: `enviarMensagem()` (assinatura do método)
        * `Main.RoboComunicador`: `enviarMensagem()`

4.  **`ForaDosLimitesException`**
    * Propósito: Lançada quando uma entidade tenta se mover ou realizar uma ação fora das dimensões do ambiente.
    * Lançada em:
        * `Ambiente`: `estaOcupado()`, `getEntidadeEm()`, `adicionarEntidade()`, `removerEntidade()`, `moverEntidade()`, `visualizarAmbiente()`

5.  **`RecursoInsuficienteException`** 
    * Propósito: Lançada quando uma ação requer um recurso que não está disponível em quantidade suficiente.
    * Lançada em:
        * `Coletor`: `coletarRecurso()` (assinatura do método)
        * `Robo`: `executarTarefa()` (assinatura do método abstrato)
        * `Main.RoboDroneDeCarga`: `executarTarefa()` (potencialmente, se a lógica de carregar fosse de um recurso limitado do ambiente)

6.  **`RoboDesligadoException`** 
    * Propósito: Lançada ao tentar executar uma ação em um robô que está desligado.
    * Lançada em:
        * `Ambiente`: `moverEntidade()`, `executarSensoresGlobais()`
        * `Robo`: `moverPara()`, `moverRelativamente()`, `executarTarefa()`
        * `RoboTerrestre`: `moverPara()`, `moverRelativamente()`, `executarTarefa()`, `acionarSensores()`
        * `Main.RoboAereo`: `subir()`, `descer()`, `moverRelativamente()`, `acionarSensores()`
        * `Main.RoboDroneDeCarga`: `carregar()`, `descarregar()`, `executarProximaAcaoAutonoma()`
        * `Main.RoboComunicador`: `enviarMensagem()`, `receberMensagem()`
        * Interfaces: `Sensoreavel.acionarSensores()`, `Comunicavel.enviarMensagem()`, `Comunicavel.receberMensagem()`, `Autonomo.executarProximaAcaoAutonoma()`, `Explorador.explorarArea()`, `Coletor.coletarRecurso()` (nas assinaturas dos métodos)

