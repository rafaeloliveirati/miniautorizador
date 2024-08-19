import { setup as k6Setup, default as k6Execute } from "./scenarios/post.js";
import { group } from "k6";
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

export function handleSummary(data) {
  return {
    "summary.html": htmlReport(data),
  };
}

export function setup() {
  return k6Setup();
}

export default () => {
  group("Miniautomatizador", () => {
    k6Execute();
  });
};
