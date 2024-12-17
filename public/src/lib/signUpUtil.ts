import { SendRequest } from './request';

export function ConfirmPasswordMatch(
  password: string,
  confirmPassword: string,
) {
  return password === confirmPassword;
}
export function IsAGoodPassword(password: string) {
  return (
    password.length >= 8 &&
    password.length <= 128 &&
    /[a-z]/.test(password) &&
    /[A-Z]/.test(password) &&
    /[0-9]/.test(password) &&
    /[!@#$%^&*]/.test(password)
  );
}

export async function IsDataTheSame() {
  const email = localStorage.getItem('email');
  const id = localStorage.getItem('uuid');
  if (!email || !id) {
    return false;
  }
  const request = await SendRequest(`/api/users/info/${email}`, 'GET');
  const data = await request.json();
  console.log(data);

  if (data) {
    return email === data.email && data.id == id;
  }
  return false;
}

export default { ConfirmPasswordMatch, IsAGoodPassword, IsDataTheSame };
