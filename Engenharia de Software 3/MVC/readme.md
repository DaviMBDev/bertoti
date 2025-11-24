# 🏛 Arquitetura MVC e Padrões de Projeto

> **Conceito Fundamental:** O MVC (*Model-View-Controller*) é um padrão arquitetural focado na **Separação de Responsabilidades** (*Separation of Concerns*). Seu objetivo é desacoplar a interface (View) das regras de negócio (Model), utilizando o Controller como mediador.

Para garantir esse desacoplamento, o MVC Clássico se apoia em dois padrões comportamentais do GoF: **Observer** e **Strategy**.

## 🤝 A Sinergia dos Padrões: Como o MVC os Une?

O poder do MVC não reside apenas em dividir o código em três pastas, mas na forma como esses componentes conversam entre si utilizando padrões consagrados:

### 1. Model + View = Padrão Observer
A relação entre Model e View é definida pelo padrão **Observer**.
* **O Problema:** O Model (dados) muda e a tela precisa ser atualizada, mas o Model não pode depender da tela (senão, não poderíamos mudar o design sem quebrar a regra de negócio).
* **A Solução:** O Model torna-se um **Sujeito (Subject)**. Ele apenas grita *"Mudei!"*. A View, agindo como **Observadora**, escuta esse grito e se vira para buscar os dados novos.
* **Resultado:** O Model permanece "cego" em relação à View, garantindo baixo acoplamento.

### 2. View + Controller = Padrão Strategy
A relação entre a Interface e o Processamento é definida pelo padrão **Strategy**.
* **O Problema:** A View deve apenas exibir coisas, ela não deve saber *o que fazer* com um clique ou um dado digitado.
* **A Solução:** O Controller encapsula a **Estratégia** de como lidar com a entrada. Ele é o "algoritmo" que decide se o input é salvo, descartado ou validado.
* **Resultado:** Podemos trocar o Controller (a estratégia) para alterar o comportamento do sistema sem precisar redesenhar a tela.

---

## ⚙️ Estrutura e Responsabilidades

### 1. Model (O Detentor do Estado)
Representa o núcleo da aplicação: os dados e a lógica de negócio.
* **Comportamento:** Quando seu estado muda, ele notifica automaticamente todos os "assinantes" registrados, sem saber quem são.

### 2. View (A Interface Visual)
É a representação visual dos dados para o usuário.
* **Comportamento:** A View se registra no Model. Ao receber uma notificação, ela consulta o estado atual do Model e se redesenha. Isso permite múltiplas interfaces (ex: gráfico, tabela, alerta) reagindo ao mesmo dado simultaneamente.

### 3. Controller (O Processador de Entrada)
Gerencia a interação do usuário e orquestra o fluxo.
* **Comportamento:** Intercepta o comando do usuário, aplica validações (regras de entrada) e invoca a alteração no Model. Isso protege o Model de receber dados "sujos" diretamente da View.

---

## 🔄 O Fluxo de Execução

O ciclo de vida de uma interação no MVC ocorre na seguinte ordem:

1.  **Interação:** O usuário aciona um comando na interface (ex: clica em "Alterar Temperatura").
2.  **Estratégia (Controller):** O Controller captura o evento e decide como processá-lo (valida se é permitido).
3.  **Alteração:** Se válido, o Controller invoca a mudança de estado no Model.
4.  **Notificação (Observer):** O Model atualiza seu estado e dispara o aviso para os ouvintes.
5.  **Atualização:** As Views recebem o aviso, buscam o novo valor e atualizam a tela.
---

## ⚙️ Estrutura e Responsabilidades

### 1. Model (O Detentor do Estado)
Representa o núcleo da aplicação: os dados e a lógica de negócio.

* **Papel no Padrão Observer:** Atua como o **Sujeito** (*Subject*).
* **Comportamento:** O Model **não conhece** a interface gráfica. Quando seu estado muda, ele notifica automaticamente todos os "assinantes" registrados, sem se importar com quem são ou o que farão com a informação.

### 2. View (A Interface Visual)
É a representação visual dos dados para o usuário.

* **Papel no Padrão Observer:** Atua como o **Observador** (*Observer*).
* **Comportamento:** A View se registra no Model. Ao receber uma notificação de mudança, ela consulta o Model (via `get`) e se redesenha.
    * *Benefício:* Isso permite múltiplas interfaces (ex: gráfico, tabela, alerta) reagindo ao mesmo dado simultaneamente.

### 3. Controller (O Processador de Entrada)
Gerencia a interação do usuário e orquestra o fluxo.

* **Papel no Padrão Strategy:** Define a **estratégia** de manipulação do input.
* **Comportamento:** Intercepta o comando do usuário, aplica validações e invoca a alteração no Model.
    * *Benefício:* Protege o Model de receber dados inválidos ou "sujos" diretamente da View.

---

## 🔄 O Fluxo de Execução

O ciclo de vida de uma interação no MVC ocorre na seguinte ordem:

1.  **Interação:** O usuário aciona um comando na interface (ex: clica em "Alterar Temperatura").
2.  **Processamento:** O `Controller` captura o evento e valida se a operação é permitida.
3.  **Alteração:** Se válido, o `Controller` invoca a mudança de estado no `Model` (ex: `setValor()`).
4.  **Notificação:** O `Model` atualiza seu estado e dispara o aviso: *"Meus dados mudaram!"*.
5.  **Atualização:** Todas as `Views` registradas recebem o aviso, buscam o novo valor e atualizam a tela.

---

## 💻 Implementação Prática (Java)

O exemplo abaixo simula um **Sistema de Termostato Inteligente**.

* **Destaque do Controller:** Atua como *Gatekeeper*, impedindo temperaturas fisicamente impossíveis (abaixo do zero absoluto).
* **Destaque do Observer:** O Model notifica duas Views distintas: um **Display Numérico** e um **Sistema de Alerta**.

```java
import java.util.ArrayList;
import java.util.List;

// =========================================================
// 1. O CONTRATO (Observer Pattern)
// Interface comum para qualquer tela que queira "ouvir" o Model
// =========================================================
interface TermostatoListener {
    void atualizar(int temperatura);
}

// =========================================================
// 2. MODEL (Subject)
// Detém os dados e notifica as Views
// =========================================================
class TermostatoModel {
    private int temperatura;
    private List<TermostatoListener> ouvintes = new ArrayList<>();

    public TermostatoModel(int tempInicial) {
        this.temperatura = tempInicial;
    }

    // Get: As Views usam para puxar o dado atualizado
    public int getTemperatura() {
        return temperatura;
    }

    // Set: O Controller usa para alterar o estado
    public void setTemperatura(int novaTemp) {
        this.temperatura = novaTemp;
        notificarOuvintes(); // O gatilho do Observer
    }

    public void adicionarOuvinte(TermostatoListener l) {
        ouvintes.add(l);
    }

    private void notificarOuvintes() {
        for (TermostatoListener l : ouvintes) {
            l.atualizar(this.temperatura);
        }
    }
}

// =========================================================
// 3. VIEWS (Observers Concretos)
// Reagem às mudanças do Model de formas diferentes
// =========================================================

// View A: Exibe apenas o número
class DisplaySimples implements TermostatoListener {
    @Override
    public void atualizar(int temperatura) {
        System.out.println("[DISPLAY] Temperatura atualizada: " + temperatura + "°C");
    }
}

// View B: Monitora riscos (Lógica de apresentação independente)
class SistemaAlerta implements TermostatoListener {
    @Override
    public void atualizar(int temperatura) {
        if (temperatura > 100) {
            System.out.println("[ALERTA] 🚨 PERIGO: Risco de superaquecimento !!!");
        }
    }
}

// =========================================================
// 4. CONTROLLER (Strategy)
// Processa o input e protege o Model (Regra de Negócio/Validação)
// =========================================================
class TermostatoController {
    private TermostatoModel model;

    public TermostatoController(TermostatoModel model) {
        this.model = model;
    }

    public void receberComando(String acao, int valor) {
        System.out.println("\n[CONTROLLER] Processando comando: " + acao);

        if ("DEFINIR_TEMP".equals(acao)) {
            // Validação: Impede temperaturas abaixo do zero absoluto (-273°C)
            if (valor < -273) {
                System.out.println("[CONTROLLER] ❌ Erro: Temperatura fisicamente impossível ignorada.");
            } else {
                System.out.println("[CONTROLLER] ✅ Comando válido. Atualizando Model...");
                model.setTemperatura(valor);
            }
        }
    }
}

// =========================================================
// 5. EXECUÇÃO (Wiring)
// =========================================================
public class AppMVC {
    public static void main(String[] args) {
        // 1. Instanciação independente (Baixo Acoplamento)
        TermostatoModel model = new TermostatoModel(25);
        TermostatoController controller = new TermostatoController(model);

        // 2. Registro das Views no Model
        model.adicionarOuvinte(new DisplaySimples());
        model.adicionarOuvinte(new SistemaAlerta());

        // -- Simulação de Uso --

        // Caso 1: Ajuste normal
        controller.receberComando("DEFINIR_TEMP", 30);

        // Caso 2: Ajuste crítico (Ativa o Display e o Alerta)
        controller.receberComando("DEFINIR_TEMP", 120);

        // Caso 3: Tentativa de erro (Bloqueada pelo Controller)
        controller.receberComando("DEFINIR_TEMP", -300);
    }
}