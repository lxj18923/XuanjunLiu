import request from "@/utils/request";

// let url = "http://106.14.7.183:8081";
let url = "http://121.40.51.107:8087"

export async function getOriginByRandom() {
  return request(url + `/origin/random`);
  // return request('/api/mark/mark.json');
}

export async function getUserResults() {
  return request(url + `/label/result/random`);
}

export async function skipOrigin() {
  return request(url + `/origin/skip`);
}

export async function deleteSentence() {
  let data = {
    method: "PUT"
  };
  return request(`http://121.40.51.107:8087/origin/delete`, data);
}

export async function getList() {
  return request(url + `/label/result/list`);
}

export async function getCount() {
  return request(url + `/label/result/count`);
}

export async function saveResult(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(url + `/label/result`, data);
}

export async function searchResult(payload) {
  let url1 = url + "/dictionary/partofspeech";
  if (payload != null) {
    url1 += "?word=" + payload;
  }
  return request(url1);
}

export async function getUserInfo() {
  return request(url + `/user/user/username`);
}
