import request from "@/utils/request";

let url = "http://121.40.51.107:8087";
// let url = "http://106.14.7.183:8081";

export async function getProofreading() {
  return request(url + `/label/result/proofreading`);
}
export async function saveProofreading(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(url + `/label/result/proofreading`, data);
}
