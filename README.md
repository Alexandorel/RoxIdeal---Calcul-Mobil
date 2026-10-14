# RoxIdeal

Shop online de **materiale de construcții** (riflaje, plăci etc.), aplicație Android nativă în **Kotlin**, cu backend propriu în **Spring Boot** și bază de date pe server (PostgreSQL). 

---

## Scenariu

Magazin online prin care clienții răsfoiesc materiale de construcții, le adaugă în coș și plasează comenzi de pe telefon. Administratorul gestionează catalogul (adaugă / editează / șterge produse).

- **Client (beneficiarul afacerii):** firma de materiale de construcții „RoxIdeal", care vrea un canal de vânzare pe mobil și gestiune simplă a produselor.

### Utilizatori (roluri)

| Rol | Ce poate face |
|-----|---------------|
| **Vizitator / invitat** | Răsfoiește produse, vede detalii, poate comanda **fără cont**. |
| **Client cu cont** | Tot ce face invitatul + **istoric comenzi** și date salvate. |
| **Administrator** | Cont dedicat; **gestionează produsele** (CRUD) și vede comenzile. |

---

## Stivă tehnică & arhitectură

| Strat | Tehnologie |
|-------|-----------|
| **UI** | Jetpack Compose (Kotlin, nativ) |
| **Rețea** | Retrofit + Coroutines |
| **Stocare locală** | Room (coș / sesiune) + DataStore (token JWT) |
| **Backend** | Spring Boot + Spring Data JPA + Spring Security / JWT |
| **Bază de date** | PostgreSQL |

- **Backend partajat** cu o viitoare aplicație web.

### Structura repo

```
RoxIdeal---Calcul-Mobil/
├── frontend/     # aplicația Android (Kotlin + Jetpack Compose)
├── backend/      # API REST (Spring Boot + PostgreSQL)
└── README.md
```

---

## Funcționalități

### Nucleu (cerințe minime)
- Mai multe ecrane cu navigare: **listă produse → detaliu produs → coș → checkout → cont / istoric**.
- Ecran **listă produse** (Compose `LazyColumn`) alimentat din API.
- **Selecție produs → ecran de detaliu**.
- **Stocare locală (Room):** coșul și sesiunea / token-ul, care persistă la repornire.
- **Consum API extern** (backend Spring Boot): produse, comenzi, autentificare.

- Autentificare cont client (**JWT**) + **istoric comenzi**.
- **Checkout ca invitat** (fără cont).
- Rol **administrator**: CRUD produse din aplicație.
- **Căutare + filtrare** produse.
- **Notificări** la schimbarea stării comenzii.
- Administratorul poate **încărca poze** de produs.

### Deocamdată excluse
- **Plată:** doar plasare comandă / ramburs (fără plăți reale).

---

## API

Backend REST/JSON propriu (Spring Boot). Endpoint-uri principale (în dezvoltare):

| Metodă | Rută | Acces |
|--------|------|-------|
| `GET` | `/api/products`, `/api/products/{id}` | public |
| `POST` | `/api/auth/register`, `/api/auth/login` | public |
| `POST` | `/api/orders` | invitat sau client logat |
| `GET` | `/api/orders` | client logat (istoric) |
| `POST` / `PUT` / `DELETE` | `/api/products` | doar ADMIN |





## Plan de lucru

| Săptămâni | Etapă |
|-----------|-------|
| **1** | Setup: repo + GitHub Projects, schelet Android + Spring Boot, README inițial |
| **2–3** | Backend produse: model `Product`, DB, `GET /products` și `GET /products/{id}` |
| **4–5** | Listă + detaliu: Retrofit → listă produse + ecran detaliu |
| **6–7** | Stocare locală: Room pentru coș și sesiune, persistă la repornire |
| **7–8** | Checkout + comenzi: `POST /orders`, checkout invitat, consum API |
| **8–9** | Cont + istoric: autentificare JWT, înregistrare / login, istoric comenzi |
| **9–10** | Admin: rol administrator cu CRUD pe produse |
| **10–11** | Polish + PPTX: testare pe emulator/dispozitiv, README final, prezentare |

---

## Echipă

- Alex — dezvoltare integrală (frontend + backend).
