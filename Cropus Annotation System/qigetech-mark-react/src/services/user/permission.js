import request from "@/utils/request";

let userUrl = "http://121.40.51.107:8087/user";
// let userUrl = "http://106.14.7.183:8081/user";

export async function queryPermissions() {
  return request(userUrl + `/resource/list`);
}

export async function addPermission(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(userUrl + `/resource`, data);
}

export async function updatePermission(payload) {
  let id = payload.id;
  let data = {
    body: payload,
    method: "PUT"
  };
  return request(userUrl + `/resource/` + id, data);
}

export async function deletePermission(id) {
  let data = {
    method: "DELETE"
  };
  return request(userUrl + `/resource/` + id, data);
}
