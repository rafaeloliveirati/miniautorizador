import http from "k6/http";
import { sleep, check, fail } from "k6";
import { Trend, Counter, Gauge } from "k6/metrics";

let responseTimeTrend = new Trend("response_time_trend");
let successfulTransactions = new Counter("successful_transactions");
let failedTransactions = new Counter("failed_transactions");
let responseTimeGauge = new Gauge("response_time_gauge");

let params = {
  headers: {
    "Content-Type": "application/json",
  },
};

export let options = {
  thresholds: {
    response_time_trend: ["p(95)<500"], // 95% das requisições devem ser menores que 500ms
    successful_transactions: ["count>0"], // Deve haver pelo menos uma transação bem-sucedida
    failed_transactions: ["count==0"], // Não deve haver transações falhas
  },
};

export function setup() {
  let payload = JSON.stringify({
    numeroCartao: __ENV.NUMERO_CARTAO || "123456789",
    senha: __ENV.SENHA_CARTAO || "123456789",
  });

  let response = http.post("http://localhost:8080/cartoes", payload, params);
  sleep(3);

  return response.json();
}

export default function () {
  let payload = JSON.stringify({
    numeroCartao: __ENV.NUMERO_CARTAO || "123456789",
    senhaCartao: __ENV.SENHA_CARTAO || "123456789",
    valor: 1,
  });

  console.log("Transaction payload: " + payload);

  let response = http.post("http://localhost:8080/transacoes", payload, params);

  console.log("Transaction response status: " + response.status);
  console.log("Transaction response body: " + response.body);
  console.log("Transaction response time: " + response.timings.duration + " ms");

  // Registrando métricas
  responseTimeTrend.add(response.timings.duration);
  responseTimeGauge.add(response.timings.duration);

  if (
    check(response, {
      "status is 200 or 201": (r) => r.status === 200 || r.status === 201,
    })
  ) {
    successfulTransactions.add(1);
  } else {
    failedTransactions.add(1);
    fail("Failed to transaction");
  }

  sleep(2);
}

export function handleSummary(data) {
  console.log(JSON.stringify(data, null, 2)); // Exibe as métricas no console
  return {
    "summary.html": htmlReport(data), // Gera um relatório HTML
  };
}