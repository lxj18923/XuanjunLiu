import request from "@/utils/request";

let originUrl = "http://121.40.51.107:8087/origin";
let labelResultUrl = "http://121.40.51.107:8087/label/result";
// let originUrl = "http://106.14.7.183:8081/origin";
// let labelResultUrl = "http://106.14.7.183:8081/label/result";

export async function getList(payload) {
  let url = originUrl + "/page";
  if (payload.pageNum != null && payload.pageSize != null) {
    url += "?current=" + payload.pageNum + "&size=" + payload.pageSize;
  }
  return request(url);
}
export async function update(payload) {
  let data = {
    body: payload,
    method: "PUT"
  };
  return request(labelResultUrl, data);
}

export async function deleteCover(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(labelResultUrl, data);
}
