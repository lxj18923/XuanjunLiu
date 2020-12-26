import request from "@/utils/request";

// let userUrl = "http://106.14.7.183:8081//user";
let userUrl = "http://121.40.51.107:8087/user";

// let userUrl = "http://localhost:8088/user";
export async function queryRoles() {
  return request(userUrl + `/role/list`);
}

export async function getRoleById(id) {
  return request(userUrl + `/role/` + id);
}

export async function getPermissionTreeByType(type) {
  return request(userUrl + `/resource/list`);
}

export async function addRole(payload) {
  let data = {
    body: payload,
    method: "POST"
  };
  return request(userUrl + `/role`, data);
}

export async function deleteRole(id) {
  let data = {
    method: "DELETE"
  };
  return request(userUrl + `/role/` + id, data);
}

export async function updateRole(payload) {
  let id = payload.id;
  //角色信息修改
  let data = {
    body: {
      id: id,
      name: payload.name
    },
    method: "PUT"
  };
  request(`/role/` + id, data);
  // //权限配置修改
  let permissions = payload.permissions;
  permissions.push(payload.user_data_range);
  permissions.push(payload.article_data_range);
  data = {
    body: {
      permissions
    },
    method: "POST"
  };
  return request(userUrl + `/role/` + id + `/resource`, data);
}
