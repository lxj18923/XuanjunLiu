import request from "@/utils/request";

let userUrl = "http://121.40.51.107:8087/user";
// let userUrl = "http://106.14.7.183:8081/user";
export async function queryUsers(payload) {
  console.log(payload);
  let url = userUrl + "/user/list";
  if (payload.pageNum != null && payload.pageSize != null) {
    url += "?pageNum=" + payload.pageNum + "&pageSize=" + payload.pageSize;
  }
  return request(url);
}

export async function queryCurrent() {
  return request("/api/currentUser");
}

export async function getUserById(payload) {
  return request(userUrl + "/user/" + payload);
}

export async function addUser(payload) {
  let data = {
    body: {
      ...payload
    },
    method: "POST"
  };
  return request(userUrl + "/user", data);
}

export async function updateUser(payload) {
  let id = payload.id;
  let data = {
    body: {
      ...payload
    },
    method: "PUT"
  };
  console.log(data);
  return request(userUrl + "/user/" + id, data);
}

export async function deleteUser(payload) {
  let data = {
    method: "DELETE"
  };
  return request(userUrl + "/user/" + payload, data);
}

export async function getRoleByUserId(payload) {
  return request(userUrl + "/user/" + payload + "/role");
}

export async function getRoles() {
  return request(userUrl + "/role/list");
}
