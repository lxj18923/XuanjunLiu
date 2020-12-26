// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority(str) {
  const authorityString =
    typeof str === 'undefined' ? localStorage.getItem('iktbl-admin-authority') : str;
  // authorityString could be admin, "admin", ["admin"]
  let authority;
  try {
    authority = JSON.parse(authorityString);
  } catch (e) {
    authority = authorityString;
  }
  if (typeof authority === 'string') {
    return [authority];
  }
  return authority || ['guest'];
}

export function setAuthority(authority) {
  //token
  let proAuthority = authority == null ? ['guest'] : authority;
  if(proAuthority.jwtToken!=null){
    localStorage.setItem(
      "iktbl-admin-authority-token",
      "Bearer "+proAuthority.jwtToken
    );
    return localStorage.setItem('iktbl-admin-authority', JSON.stringify(['admin']));
  }else{
    localStorage.removeItem("iktbl-admin-authority-token");
    return localStorage.setItem('iktbl-admin-authority', JSON.stringify(['guest']));
  }
}
