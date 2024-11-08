export function parseJwt(token: string) {
	const base64Url = token.split('.')[1];
	const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
	const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
		return '%' + ('00' + c.charCodeAt(0).toString(16).slice(-2));
	}).join(''));

	return JSON.parse(jsonPayload);
}


export function isJWTExpired(token: string) {
	const segments = token.split('.');

	const payloadBase64 = segments[1];
	const payloadJson = atob(payloadBase64);
	const payload = JSON.parse(payloadJson);

	if (payload.exp && Date.now() >= payload.exp * 1000) {
		return true;
	} else {
		return false;
	}
}
