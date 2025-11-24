# 🏛 Arquitetura MVC e Padrões de Projeto

> **Conceito Fundamental:** O MVC (*Model-View-Controller*) é um padrão arquitetural focado na **Separação de Responsabilidades** (*Separation of Concerns*). Seu objetivo é desacoplar a interface (View) das regras de negócio (Model), utilizando o Controller como mediador.

Para garantir esse desacoplamento, o MVC Clássico se apoia na união de dois padrões comportamentais do GoF: **Observer** e **Strategy**.

---

## 🤝 A Sinergia dos Padrões: Como o MVC os Une?

O poder do MVC não reside apenas em dividir o código em três pastas, mas na forma como esses componentes conversam entre si utilizando padrões consagrados:

### 1. Model + View = Padrão Observer
A relação entre Model e View é definida pelo padrão **Observer**.
* **O Problema:** O Model (dados) muda e a tela precisa ser atualizada, mas o Model não pode depender da tela (senão, não poderíamos mudar o design sem quebrar a regra de negócio).
* **A Solução:** O Model torna