import request from "@/utils/request";


let url = "http://121.40.51.107:8087";
// let url = "http://106.14.7.183:8081";
export async function getOrigin() {
  return request(url + `/triad/getOrigin`);
}

export async function getList(payload) {
  let url = url + "/triad/list";
  if (payload.pageNum != null && payload.pageSize != null) {
    url += "?current=" + payload.pageNum + "&size=" + payload.pageSize;
  }
  return request(url);
}

export async function save(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(url + `/triad`, data);
}

export async function update(payload) {
  let data = {
    body: payload,
    method: "PUT"
  };
  return request(url + `/triad`, data);
}
