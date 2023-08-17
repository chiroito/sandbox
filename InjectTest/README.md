パッケージは以下を表します。

- abパッケージが Application Business Rules
- iaパッケージが Interface Adapters

abパッケージからは Jakarta EE 固有のAPIを排除して、JAX-RSのコードをテストする方法を模索。

JAX-RSではinteractorをInjectするようにし、環境ごとにプロデューサを作ることでテスト環境ではモックに差し替える。
詳しくはInjectTestクラスを参照
