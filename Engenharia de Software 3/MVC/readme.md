Arquitetura MVC e Padrões de Projeto
O MVC (Model-View-Controller) é um padrão arquitetural focado na separação de responsabilidades. Seu principal objetivo é desacoplar a interface do usuário (View) das regras de negócio e dados (Model), utilizando o Controller como mediador da entrada de dados.

Para que essa separação funcione de forma eficiente, o MVC clássico se apoia fortemente em dois padrões de projeto do GoF: Observer e Strategy.

Estrutura e Responsabilidades
1. Model (O Detentor do Estado)
Representa o núcleo da aplicação (dados e lógica de negócio).

Papel no Padrão Observer: Atua como o Sujeito (Subject).

Comportamento: Ele não conhece a interface gráfica. Quando seu estado muda, ele notifica automaticamente todos os "assinantes" (Views) registrados.

2. View (A Interface Visual)
É a representação visual dos dados.

Papel no Padrão Observer: Atua como o Observador (Observer).

Comportamento: A View se registra no Model. Ao receber uma notificação de mudança, ela consulta o Model para obter os dados mais recentes e se redesenhar. Isso permite ter múltiplas telas (ex: gráfico, tabela, alerta) reagindo ao mesmo dado simultaneamente.

3. Controller (O Processador de Entrada)
Gerencia a interação do usuário.

Papel no Padrão Strategy: Define a estratégia de como lidar com o input.

Comportamento: Ele intercepta o comando do usuário, valida as regras de entrada (ex: impedir dados inválidos) e, se tudo estiver correto, invoca a alteração no Model. Isso protege o Model de receber dados "sujos" diretamente da View.

O Fluxo de Execução
Sem o uso de diagramas, o ciclo de vida de uma interação ocorre na seguinte ordem:

Interação: O Usuário aciona um comando (ex: clica em "Aumentar Temperatura").

Processamento: O Controller recebe esse comando. Ele verifica se a operação é válida (regras de validação).

Alteração: Se o comando for válido, o Controller chama um método no Model (ex: setTemperatura).

Notificação: O Model altera seu estado interno e percorre sua lista de observadores, avisando que houve mudança.

Atualização: Cada View registrada recebe o aviso, busca o novo valor no Model e atualiza o que é exibido para o usuário.

Implementação Prática (Java)
O exemplo a seguir demonstra um Sistema de Termostato.

Diferente de exemplos simples, aqui o Controller realmente atua como guardião (Strategy), impedindo temperaturas fisicamente impossíveis (abaixo do zero absoluto).

O Model notifica duas Views distintas: um display simples e um sistema de segurança.

Java

import java.util.ArrayList;
import java.util.List;

// ---------------------------------------------------------
// Padrão Observer: Interface Comum para as Views
// ---------------------------------------------------------
interface TermostatoListener {
    void atualizar(int temperatura);
}

// ---------------------------------------------------------
// Componente: MODEL (Subject)
// Detém os dados e notifica as Views sem conhecê-las concretamente
// ---------------------------------------------------------
class TermostatoModel {
    private int temperatura;
    private List<TermostatoListener> ouvintes = new ArrayList<>();

    public TermostatoModel(int tempInicial) {
        this.temperatura = tempInicial;
    }

    // Get: usado pelas Views para puxar o dado atualizado
    public int getTemperatura() {
        return temperatura;
    }

    // Set: usado pelo Controller para alterar o estado
    public void setTemperatura(int novaTemp) {
        this.temperatura = novaTemp;
        notificarOuvintes(); // Gatilho do Observer
    }

    // Método para registrar novos observadores
    public void adicionarOuvinte(TermostatoListener l) {
        ouvintes.add(l);
    }

    private void notificarOuvintes() {
        for (TermostatoListener l : ouvintes) {
            l.atualizar(this.temperatura);
        }
    }
}

// ---------------------------------------------------------
// Componente: VIEWS (Observers)
// Reagem às mudanças do Model
// ---------------------------------------------------------

// View 1: Apenas exibe o número
class DisplaySimples implements TermostatoListener {
    @Override
    public void atualizar(int temperatura) {
        System.out.println("[DISPLAY] Temperatura atualizada: " + temperatura + "°C");
    }
}

// View 2: Monitora riscos (Demonstra a flexibilidade de ter múltiplos observers)
class SistemaAlerta implements TermostatoListener {
    @Override
    public void atualizar(int temperatura) {
        if (temperatura > 100) {
            System.out.println("[ALERTA] !!! PERIGO: Risco de superaquecimento !!!");
        }
    }
}

// ---------------------------------------------------------
// Componente: CONTROLLER (Strategy)
// Processa o input e protege o Model de dados inválidos
// ---------------------------------------------------------
class TermostatoController {
    private TermostatoModel model;

    public TermostatoController(TermostatoModel model) {
        this.model = model;
    }

    public void receberComando(String acao, int valor) {
        System.out.println("\n[CONTROLLER] Processando comando: " + acao);

        if ("DEFINIR_TEMP".equals(acao)) {
            // Validação (Strategy): Impede temperaturas abaixo do zero absoluto
            if (valor < -273) {
                System.out.println("[CONTROLLER] Erro: Temperatura fisicamente impossível ignorada.");
            } else {
                System.out.println("[CONTROLLER] Comando válido. Alterando Model...");
                model.setTemperatura(valor);
            }
        }
    }
}

// ---------------------------------------------------------
// Execução da Aplicação
// ---------------------------------------------------------
public class AppMVC {
    public static void main(String[] args) {
        // 1. Inicialização do Model
        TermostatoModel model = new TermostatoModel(25);

        // 2. Configuração das Views (Wiring)
        // As views são criadas e registradas no model
        model.adicionarOuvinte(new DisplaySimples());
        model.adicionarOuvinte(new SistemaAlerta());

        // 3. Inicialização do Controller
        TermostatoController controller = new TermostatoController(model);

        // -- Simulação de Uso --

        // Caso 1: Ajuste normal (apenas atualiza o display)
        controller.receberComando("DEFINIR_TEMP", 30);

        // Caso 2: Ajuste crítico (atualiza display e dispara alerta)
        controller.receberComando("DEFINIR_TEMP", 120);

        // Caso 3: Tentativa de erro (bloqueada pelo Controller)
        controller.receberComando("DEFINIR_TEMP", -300);
    }
}