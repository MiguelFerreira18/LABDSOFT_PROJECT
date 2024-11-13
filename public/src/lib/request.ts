import { apiConfig, apiKey } from "./config";
import { Encrypt } from "./encryption";

const { baseUrl } = apiConfig;

export async function SendRequest(
  path: string,
  method: string,
  data: Record<string, string> = {},
  fieldsToEncrypt: string[] = [],
  token = ""
): Promise<Response> {
  const headers: Record<string, string> = {
    "Content-type": "application/json",
    Authorization: "",
  };
  headers["X-API-KEY"] = apiKey;
  if (token !== "") {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const processedData: Record<string, string> = { ...data };
  if (["POST", "PATCH", "PUT"].includes(method.toUpperCase()) && data) {
    for (const key of fieldsToEncrypt) {
      if (processedData[key]) {
        const encryptedValue = Encrypt(processedData[key]);
        if (encryptedValue) {
          processedData[key] = await encryptedValue;
        } else {
          console.warn(`Encryption failed for field: ${key}`);
        }
      }
    }
  }

  const options: RequestInit = {
    method: method,
    headers: headers,
    mode: "cors",
    credentials: "include",
  };

  if (Object.keys(data).length > 0) {
    options.body = JSON.stringify(processedData);
  }

  const response = await fetch(`${baseUrl}${path}`, options);

  return response;
}
