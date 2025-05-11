# stock-service

The below problem statement was given ...

- Nasdaq is the upstream data source where the sell side subscription service should subscribe to.
- Client should 'connect' to subscription service with the target list of stocks symbols.
- 'Disconnect' scenario: e.g. when Client3 disconnect from subscription service, subscription service should unsubscribe from Nasdaq too.
- This is because after checking all the connected clients, sub service understands no one is still interested in stock D.

```mermaid

flowchart LR
    C1("Client1: <br>[A, B]") <--> SellSide("SellSide: <br>SubService: <br>[A, B, C, D]")
    C2("Client2: <br>[B, C]") <--> SellSide("SellSide: <br>SubService: <br>[A, B, C, D]")
    C3("Client3: <br>[D]")    <--> SellSide("SellSide: <br>SubService: <br>[A, B, C, D]")
    SellSide("SellSide: <br>SubService: <br>[A, B, C, D]") <--> Nasdaq("Nasdaq: <br>[A, B, C, D, E, F]")

```
Focus on ...

- Service contracts (POJO)
- Services
- Test Coverage
- Concurrency
