import request from "@/utils/request";

// let url = "http://106.14.7.183:8081";
let url = "http://121.40.51.107:8087"
export async function uploadFile(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(url + `/article/upload`, data);
}

export async function searchBar(payload) {
  let urlPrams = "";
  for (let a in payload) {
    if (payload[a] != null) {
      urlPrams += a + "=" + payload[a] + "&";
    }
  }
  urlPrams = urlPrams.substr(0, urlPrams.length - 1);
  let url1 = url + `/origin/searchPage`;
  if (payload != null) {
    url1 += "?" + urlPrams;
  }
  return request(url1);
}
