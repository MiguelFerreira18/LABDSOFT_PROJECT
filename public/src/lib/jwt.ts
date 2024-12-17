export function ParseJwt(header: string) {
  const token = header.substring(7);
  if (!token) {
    return null;
  }
  const base64Url = token.split('.')[1];
  const payloadJson = atob(base64Url);
  return JSON.parse(payloadJson);
}

export function SaveJwtFieldsToLocaStorate(payload: any) {
  if (!payload) {
    return;
  }
  for (const key in payload) {
    localStorage.setItem(key, payload[key]);
  }
}
export function IsJWTExpired(token: string) {
  const payload = ParseJwt(token);

  if (payload && payload.exp && Date.now() >= payload.exp * 1000) {
    cleanLocalStorage();
    return true;
  } else {
    return false;
  }
}

function cleanLocalStorage() {
  localStorage.clear();
}
