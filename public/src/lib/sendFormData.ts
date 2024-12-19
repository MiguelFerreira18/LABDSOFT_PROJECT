import { apiConfig, apiKey } from './config';
import { IsJWTExpired } from './jwt';

const { baseUrl } = apiConfig;

export async function SendFormData(
  path: string,
  method: string,
  formData: FormData, // Recebe FormData como argumento
): Promise<Response> {
  const token = localStorage.getItem('token') || '';

  const headers: Record<string, string> = {
    'X-API-KEY': apiKey,
    Authorization: '',
    credentials: 'include',
    'ngrok-skip-browser-warning': '69420',
  };

  // Verifica se o token está presente e não expirado, e adiciona o header Authorization
  if (token !== '' && !IsJWTExpired(token)) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const options: RequestInit = {
    method: method,
    headers: headers,
    mode: 'cors',
    credentials: 'include',
  };

  // Definindo o corpo como FormData e deixando o navegador definir o Content-Type
  options.body = formData;

  // Envia a requisição
  const response = await fetch(`${baseUrl}${path}`, options);
  return response;
}
