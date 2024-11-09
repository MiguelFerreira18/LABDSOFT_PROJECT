export function ParseJwt(token: string) {
  const base64Url = token.split(".")[1];
  const payloadJson = atob(base64Url);
  return JSON.parse(payloadJson);
}

export function IsJWTExpired(token: string) {
  const payload = ParseJwt(token);

  if (payload.exp && Date.now() >= payload.exp * 1000) {
    return true;
  } else {
    return false;
  }
}
