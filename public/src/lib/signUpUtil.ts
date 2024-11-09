export function ConfirmPasswordMatch(
  password: string,
  confirmPassword: string
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

export default { ConfirmPasswordMatch, IsAGoodPassword };
