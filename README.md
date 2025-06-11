# 🤖 Simulador de Robôs - Laboratório 5

> Este projeto é a continuação do desenvolvimento de um simulador de robôs em um ambiente tridimensional, como parte da disciplina MC322. O objetivo é criar um laboratório de robótica virtual onde diversos robôs inteligentes podem interagir, mover-se e realizar tarefas complexas e cooperativas.

Nesta versão, o foco foi a introdução de um **sistema de missões autônomas**, a refatoração da arquitetura dos robôs para usar o princípio de **composição sobre herança** e a criação de **agentes inteligentes** capazes de executar tarefas dinamicamente atribuídas.

***

## ✨ Principais Novidades e Mudanças

* ### Sistema de Missões
    * Foi introduzida a interface `Missao`, que define um contrato para as tarefas que os robôs podem executar.
    * Foram criadas implementações concretas como `MissaoExplorar` (movimento aleatório), `MissaoPatrulhar` (segue uma rota pré-definida) e `MissaoMonitorar` (aciona os sensores).
    * A classe utilitária `Log` foi implementada para registrar eventos importantes da simulação, como o início e o resultado das missões, no arquivo `missao_log.txt`.

* ### Agentes Inteligentes
    * Foi criada a classe abstrata `AgenteInteligente`, que herda de `Robo` e adiciona a capacidade de receber e executar missões através do método `definirMissao()`.
    * As classes `RoboTerrestre` e `RoboAereo` foram refatoradas para herdarem de `AgenteInteligente`, permitindo que qualquer robô desses tipos possa agora executar missões complexas.

* ### Composição com Módulos Especializados
    * A lógica de movimentação foi extraída dos robôs e delegada a módulos de controle de movimento, como `ControleMovimentoTerrestre` e `ControleMovimentoAereo`. Cada robô agora *tem um* controle de movimento, aplicando o princípio de composição.
    * A lógica de comunicação foi delegada a um `ModuloComunicacao` dentro do `RoboComunicador`.
    * A gestão e o acionamento dos sensores também foram encapsulados no módulo `GerenciadorSensores`.

* ### Menu Interativo Aprimorado
    * O menu principal na classe `Main` foi atualizado com a opção **"7. Gerenciar Missões"**, permitindo ao usuário atribuir e executar missões nos agentes inteligentes de forma interativa.

***

## 🧠 Conceitos Aplicados

* [cite_start]**Abstração e Herança**: A hierarquia `Robo` → `AgenteInteligente` → `RoboTerrestre` define contratos e especializa comportamentos.  [cite_start]A classe `AgenteInteligente` é abstrata, pois sabe que *deve* executar uma missão, mas não define *como*. 

* [cite_start]**Composição**: Robôs **têm** um `ControleMovimento` e um `GerenciadorSensores`.  Essa abordagem favorece a flexibilidade sobre uma herança rígida, permitindo montar robôs com diferentes capacidades.

* [cite_start]**Polimorfismo**: A interface `Missao` permite que um `AgenteInteligente` execute qualquer tipo de tarefa (`MissaoExplorar`, `MissaoPatrulhar`) através de uma única chamada: `missao.executar()`.  Isso desacopla o robô da implementação específica da missão.

***

## 🔌 Lista das Interfaces Criadas

1.  **`Entidade`**
    * **Implementada por**: `Robo` (classe abstrata) e `Obstaculo`. Define a base para qualquer objeto no ambiente.

2.  **`Missao`**
    * **Implementada por**: `MissaoExplorar`, `MissaoMonitorar`, `MissaoPatrulhar`. Contrato para tarefas autônomas.

3.  **`Sensoreavel`**
    * **Implementada por**: `RoboTerrestre` e `RoboAereo`. Permite que um robô utilize sensores.

4.  **`Comunicavel`**
    * **Implementada por**: `RoboComunicador`. Define a capacidade de enviar e receber mensagens.

5.  **`Autonomo`** (Interface Funcional)
    * **Implementada por**: `RoboDroneDeCarga`. Para robôs com ciclos de ações autônomas.

6.  **`Explorador`** e **`Coletor<T>`** (Interfaces Funcionais)
    * Definidas e disponíveis para uso futuro.

***

## ⚠️ Sistema de Exceções

O tratamento de erros continua robusto, utilizando exceções personalizadas para garantir a integridade da simulação:

* `AcaoNaoPermitidaException`: Lançada quando uma ação viola uma regra (ex: mover um robô terrestre na vertical).
* `ColisaoException`: Lançada ao tentar mover ou adicionar uma entidade a uma célula já ocupada.
* `ErroComunicacaoException`: Para falhas no envio de mensagens entre robôs.
* `ForaDosLimitesException`: Ao tentar acessar ou mover uma entidade para fora do mapa do ambiente.
* `RecursoInsuficienteException`: Quando uma ação requer um recurso não disponível (ex: descarregar um drone que já está vazio).
* `RoboDesligadoException`: Lançada ao tentar executar uma ação em um robô que está desligado.

